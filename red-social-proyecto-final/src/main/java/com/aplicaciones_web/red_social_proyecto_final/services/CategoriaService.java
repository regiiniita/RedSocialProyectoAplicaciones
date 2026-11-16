package com.aplicaciones_web.red_social_proyecto_final.services;

import com.aplicaciones_web.red_social_proyecto_final.dto.CategoriaVista;
import com.aplicaciones_web.red_social_proyecto_final.model.Categoria;
import com.aplicaciones_web.red_social_proyecto_final.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Contiene las consultas y las reglas para administrar categorías.
 */
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repositorio;

    /**
     * Busca coincidencias en el nombre o en la descripción de las categorías.
     * @param busqueda texto escrito en el formulario; vacío para mostrar todas
     * @return categorías que coinciden con la búsqueda, ordenadas por nombre
     */
    @Transactional(readOnly = true)
    public List<CategoriaVista> consultar(String busqueda) {
        String filtro = busqueda == null ? "" : busqueda.strip();
        var categorias = filtro.isEmpty()
                ? repositorio.findAllByOrderByNombreAsc()
                : repositorio.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrderByNombreAsc(filtro, filtro);
        return categorias.stream().map(CategoriaService::aVista).toList();
    }

    /**
     * Registra una nueva categoría después de validar sus datos.
     * @param nombre nombre de la categoría
     * @param descripcion descripción de la categoría
     * @throws IllegalArgumentException si los datos no son válidos o el nombre ya existe
     */
    @Transactional
    public void guardar(String nombre, String descripcion) {
        String n = validarNombre(nombre);
        String d = validarDescripcion(descripcion);
        if (repositorio.existsByNombreIgnoreCase(n)) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
        }
        // El id es null para que la base de datos lo genere; inicia sin lugares asignados.
        repositorio.save(new Categoria(null, n, d, 0));
    }

    /**
     * Modifica el nombre y la descripción de una categoría existente.
     * @param id identificador de la categoría seleccionada
     * @param nombre nuevo nombre de la categoría
     * @param descripcion nueva descripción de la categoría
     * @throws IllegalArgumentException si la categoría no existe, los datos no son válidos
     *                                  o el nombre ya lo usa otra categoría
     */
    @Transactional
    public void actualizar(long id, String nombre, String descripcion) {
        var categoria = repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La categoría no existe."));
        String n = validarNombre(nombre);
        String d = validarDescripcion(descripcion);
        // Se excluye la propia categoría para permitir guardar sin cambiar el nombre.
        if (repositorio.existsByNombreIgnoreCaseAndIdNot(n, id)) {
            throw new IllegalArgumentException("Ya existe otra categoría con ese nombre.");
        }
        categoria.setNombre(n);
        categoria.setDescripcion(d);
        repositorio.save(categoria);
    }

    /**
     * Limpia los espacios del nombre y comprueba que sea obligatorio y no exceda 100 caracteres.
     * @param nombre nombre recibido del formulario
     * @return nombre sin espacios al inicio ni al final
     * @throws IllegalArgumentException si el nombre está vacío o es demasiado largo
     */
    private static String validarNombre(String nombre) {
        String n = nombre == null ? "" : nombre.strip();
        if (n.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");
        if (n.length() > 100) throw new IllegalArgumentException("El nombre no puede pasar de 100 caracteres.");
        return n;
    }

    /**
     * Limpia los espacios de la descripción y comprueba que sea obligatoria y no exceda 250 caracteres.
     * @param descripcion descripción recibida del formulario
     * @return descripción sin espacios al inicio ni al final
     * @throws IllegalArgumentException si la descripción está vacía o es demasiado larga
     */
    private static String validarDescripcion(String descripcion) {
        String d = descripcion == null ? "" : descripcion.strip();
        if (d.isEmpty()) throw new IllegalArgumentException("La descripción es obligatoria.");
        if (d.length() > 250) throw new IllegalArgumentException("La descripción no puede pasar de 250 caracteres.");
        return d;
    }

    /** Convierte la entidad en los datos que necesitan las plantillas. */
    static CategoriaVista aVista(Categoria c) {
        return new CategoriaVista(c.getId(), c.getNombre(), c.getDescripcion(), c.getTotalLugares());
    }
}