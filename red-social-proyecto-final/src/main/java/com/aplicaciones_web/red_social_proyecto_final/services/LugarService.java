package com.aplicaciones_web.red_social_proyecto_final.services;


import Dominio.EstadoResenia;
import com.aplicaciones_web.red_social_proyecto_final.dto.CategoriaVista;
import com.aplicaciones_web.red_social_proyecto_final.dto.LugarVista;
import com.aplicaciones_web.red_social_proyecto_final.model.Categoria;
import com.aplicaciones_web.red_social_proyecto_final.model.Lugar;
import com.aplicaciones_web.red_social_proyecto_final.model.Resena;
import com.aplicaciones_web.red_social_proyecto_final.repository.CategoriaRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.LugarRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.ReporteRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Contiene las consultas y las reglas para administrar lugares.
 */
@Service
@RequiredArgsConstructor
public class LugarService {

    private static final long MAX_IMAGEN = 10L * 1024 * 1024;
    private static final String RUTA_PUBLICA = "/uploads/lugares/";

    private final LugarRepository repositorio;
    private final CategoriaRepository categorias;
    private final ResenaRepository resenas;
    private final ReporteRepository reportes;

    // Debe coincidir con la carpeta que sirve WebConfig en /uploads/**.
    @Value("${app.uploads.dir:src/main/resources/static/uploads}")
    private String directorioUploads;

    /**
     * Busca coincidencias en el nombre, la dirección o el ID de los lugares.
     * @param busqueda texto escrito en el formulario; vacío para mostrar todos
     * @return lugares que coinciden, con su número de reseñas y su promedio
     */
    @Transactional(readOnly = true)
    public List<LugarVista> consultar(String busqueda) {
        String filtro = busqueda == null ? "" : busqueda.strip();
        // Permite escribir el ID como aparece en la tabla (#1284).
        if (filtro.startsWith("#")) filtro = filtro.substring(1).strip();
        var lugares = filtro.isEmpty() ? repositorio.findAllByOrderByIdDesc() : repositorio.buscar(filtro);
        Map<Long, ResenaRepository.ResumenLugar> resumen = resenas.resumenPorLugar(EstadoResenia.OCULTA).stream()
                .collect(Collectors.toMap(ResenaRepository.ResumenLugar::getLugarId, Function.identity()));
        return lugares.stream().map(l -> aVista(l, resumen.get(l.getId()))).toList();
    }

    /**
     * Registra un lugar nuevo después de validar sus datos y guardar la imagen.
     * @throws IllegalArgumentException si algún dato no es válido o el lugar ya existe
     */
    @Transactional
    public void guardar(String nombre, Long idCategoria, String direccion, String descripcion, MultipartFile imagen) {
        String n = validarNombre(nombre);
        String dir = validarDireccion(direccion);
        String desc = validarDescripcion(descripcion);
        Categoria categoria = buscarCategoria(idCategoria);
        if (repositorio.existsByNombreIgnoreCaseAndDireccionIgnoreCase(n, dir)) {
            throw new IllegalArgumentException("Ya existe un lugar con ese nombre y esa dirección.");
        }
        String ruta = guardarImagen(imagen);
        try {
            repositorio.save(new Lugar(null, n, categoria, dir, desc, ruta));
            actualizarTotal(categoria);
        } catch (RuntimeException ex) {
            borrarImagen(ruta);
            throw ex;
        }
    }

    /**
     * Modifica los datos de un lugar; la imagen se conserva.
     * @throws IllegalArgumentException si el lugar o la categoría no existen o los datos no son válidos
     */
    @Transactional
    public void actualizar(long id, String nombre, Long idCategoria, String direccion, String descripcion) {
        var lugar = repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El lugar no existe."));
        String n = validarNombre(nombre);
        String dir = validarDireccion(direccion);
        String desc = validarDescripcion(descripcion);
        Categoria nueva = buscarCategoria(idCategoria);
        // Se excluye el propio lugar para permitir guardar sin cambiar nombre ni dirección.
        if (repositorio.existsByNombreIgnoreCaseAndDireccionIgnoreCaseAndIdNot(n, dir, id)) {
            throw new IllegalArgumentException("Ya existe otro lugar con ese nombre y esa dirección.");
        }
        Categoria anterior = lugar.getCategoria();
        lugar.setNombre(n);
        lugar.setDireccion(dir);
        lugar.setDescripcion(desc);
        lugar.setCategoria(nueva);
        repositorio.save(lugar);
        if (!anterior.getId().equals(nueva.getId())) {
            actualizarTotal(anterior);
            actualizarTotal(nueva);
        }
    }

    /**
     * Elimina un lugar junto con sus reseñas y los reportes de esas reseñas.
     * @throws IllegalArgumentException si el lugar no existe
     */
    @Transactional
    public void eliminar(long id) {
        var lugar = repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El lugar no existe."));
        Long idCategoria = lugar.getCategoria().getId();
        String imagen = lugar.getImagen();

        // Cada autor pierde una reseña en su contador.
        for (Resena r : resenas.findByLugar(lugar)) {
            var autor = r.getAutor();
            autor.setTotalResenas(Math.max(0, autor.getTotalResenas() - 1));
        }
        // Orden obligatorio: reportes, luego reseñas y al final el lugar (llaves foráneas).
        reportes.eliminarPorLugar(lugar);
        resenas.eliminarPorLugar(lugar);
        repositorio.deleteById(id);

        categorias.findById(idCategoria).ifPresent(this::actualizarTotal);
        borrarImagen(imagen);
    }

    // El total de la categoría se calcula con una consulta para que siempre coincida con la realidad.
    private void actualizarTotal(Categoria categoria) {
        categoria.setTotalLugares((int) repositorio.countByCategoria(categoria));
        categorias.save(categoria);
    }

    private Categoria buscarCategoria(Long id) {
        if (id == null) throw new IllegalArgumentException("Selecciona una categoría.");
        return categorias.findById(id).orElseThrow(() -> new IllegalArgumentException("La categoría no existe."));
    }

    private static String validarNombre(String nombre) {
        String n = nombre == null ? "" : nombre.strip();
        if (n.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");
        if (n.length() > 150) throw new IllegalArgumentException("El nombre no puede pasar de 150 caracteres.");
        return n;
    }

    private static String validarDireccion(String direccion) {
        String d = direccion == null ? "" : direccion.strip();
        if (d.isEmpty()) throw new IllegalArgumentException("La dirección es obligatoria.");
        if (d.length() > 255) throw new IllegalArgumentException("La dirección no puede pasar de 255 caracteres.");
        return d;
    }

    // La descripción es opcional.
    private static String validarDescripcion(String descripcion) {
        String d = descripcion == null ? "" : descripcion.strip();
        if (d.length() > 500) throw new IllegalArgumentException("La descripción no puede pasar de 500 caracteres.");
        return d;
    }

    /** Valida que sea PNG o JPG de hasta 10 MB y la guarda con un nombre único. */
    private String guardarImagen(MultipartFile imagen) {
        if (imagen == null || imagen.isEmpty()) throw new IllegalArgumentException("La imagen es obligatoria.");
        if (imagen.getSize() > MAX_IMAGEN) throw new IllegalArgumentException("La imagen no puede pasar de 10 MB.");
        String tipo = imagen.getContentType();
        String original = imagen.getOriginalFilename() == null ? "" : imagen.getOriginalFilename().toLowerCase(Locale.ROOT);
        String extension;
        if ("image/png".equals(tipo) && original.endsWith(".png")) {
            extension = ".png";
        } else if ("image/jpeg".equals(tipo) && (original.endsWith(".jpg") || original.endsWith(".jpeg"))) {
            extension = ".jpg";
        } else {
            throw new IllegalArgumentException("La imagen debe ser PNG o JPG.");
        }
        try {
            Path carpeta = Paths.get(directorioUploads, "lugares");
            Files.createDirectories(carpeta);
            String archivo = UUID.randomUUID() + extension;
            try (InputStream in = imagen.getInputStream()) {
                Files.copy(in, carpeta.resolve(archivo));
            }
            return RUTA_PUBLICA + archivo;
        } catch (IOException ex) {
            throw new IllegalArgumentException("No se pudo guardar la imagen.");
        }
    }

    private void borrarImagen(String ruta) {
        if (ruta == null || !ruta.startsWith(RUTA_PUBLICA)) return;
        try {
            Files.deleteIfExists(Paths.get(directorioUploads, "lugares", ruta.substring(RUTA_PUBLICA.length())));
        } catch (IOException ignorada) {
            // Un archivo huérfano no debe impedir eliminar el lugar.
        }
    }

    /** Convierte la entidad en los datos que necesitan las plantillas. */
    private static LugarVista aVista(Lugar l, ResenaRepository.ResumenLugar resumen) {
        var c = l.getCategoria();
        var categoria = new CategoriaVista(c.getId(), c.getNombre(), c.getDescripcion(), c.getTotalLugares());
        int total = resumen == null ? 0 : resumen.getTotal().intValue();
        double promedio = resumen == null || resumen.getPromedio() == null
                ? 0 : Math.round(resumen.getPromedio() * 10.0) / 10.0;
        return new LugarVista(l.getId(), l.getNombre(), categoria, l.getDireccion(), total, promedio,
                l.getDescripcion() == null ? "" : l.getDescripcion());
    }
}
