package com.aplicaciones_web.red_social_proyecto_final.services;

import Dominio.EstadoReporte;
import Dominio.EstadoResenia;
import com.aplicaciones_web.red_social_proyecto_final.dto.CategoriaVista;
import com.aplicaciones_web.red_social_proyecto_final.dto.LugarVista;
import com.aplicaciones_web.red_social_proyecto_final.dto.ResenaVista;
import com.aplicaciones_web.red_social_proyecto_final.dto.UsuarioVista;
import com.aplicaciones_web.red_social_proyecto_final.model.Resena;
import com.aplicaciones_web.red_social_proyecto_final.repository.ReporteRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Dominio.Excepciones.RecursoNoEncontradoException;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.CrearResenaRequest;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.ResenaResponse;
import com.aplicaciones_web.red_social_proyecto_final.model.CuentaUsuario;
import com.aplicaciones_web.red_social_proyecto_final.repository.LugarRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.UsuarioRepository;
import java.time.LocalDate;

import java.util.List;

/**
 * Contiene las consultas y las reglas para moderar reseñas.
 */
@Service
@RequiredArgsConstructor
public class ResenaService {
    private final ResenaRepository repositorio;
    private final ReporteRepository reportes;
    private final UsuarioRepository usuarios;
    private final LugarRepository lugares;

    /**
     * Busca coincidencias en el usuario o en el lugar reseñado.
     *
     * @param busqueda texto escrito en el formulario; vacío para mostrar todas
     * @return reseñas que coinciden con la búsqueda
     */
    @Transactional(readOnly = true)
    public List<ResenaVista> consultar(String busqueda) {
        String filtro = busqueda == null ? "" : busqueda.strip();
        var resenas = filtro.isEmpty()
                ? repositorio.findAllByOrderByFechaDescIdDesc()
                : repositorio.findByAutorUsernameContainingIgnoreCaseOrLugarNombreContainingIgnoreCaseOrderByFechaDescIdDesc(
                filtro, filtro);
        return resenas.stream().map(ResenaService::aVista).toList();
    }

    /**
     * Oculta una reseña y resuelve los reportes que seguían pendientes sobre ella.
     *
     * @param id identificador de la reseña seleccionada
     * @throws IllegalArgumentException si la reseña no existe o ya está oculta
     */
    @Transactional
    public void ocultar(long id) {
        var resena = repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La reseña no existe."));
        if (resena.getEstado() == EstadoResenia.OCULTA) {
            throw new IllegalArgumentException("La reseña ya está oculta.");
        }
        // La reseña se conserva en la base de datos; solo deja de mostrarse a la comunidad.
        resena.setEstado(EstadoResenia.OCULTA);
        repositorio.save(resena);
        reportes.findByResenaAndEstado(resena, EstadoReporte.PENDIENTE)
                .forEach(r -> r.setEstado(EstadoReporte.REVISADO));
    }

    /**
     * Reseñas visibles para la comunidad: feed Tu comunidad y pestaña Reseñas de un perfil.
     *
     * @param autor usuario que escribió las reseñas; vacío para no filtrar por autor
     * @param seguidoPor usuario cuyos seguidos escribieron las reseñas; vacío para no filtrar
     * @throws RecursoNoEncontradoException si alguno de los usuarios no existe o está inactivo
     */
    @Transactional(readOnly = true)
    public List<ResenaResponse> consultarPublicas(String autor, String seguidoPor) {
        Long autorId = autor == null || autor.isBlank() ? null : buscarUsuarioActivo(autor).getId();
        Long seguidorId = seguidoPor == null || seguidoPor.isBlank() ? null : buscarUsuarioActivo(seguidoPor).getId();
        return repositorio.buscarPublicas(EstadoResenia.OCULTA, autorId, seguidorId).stream()
                .map(ResenaService::aRespuesta)
                .toList();
    }

    /**
     * Publica una reseña a nombre del usuario autenticado. Queda visible desde el primer momento
     * y se suma al contador de reseñas de su autor.
     *
     * @throws RecursoNoEncontradoException si el usuario o el lugar no existen
     */
    @Transactional
    public ResenaResponse publicar(String username, CrearResenaRequest solicitud) {
        CuentaUsuario autor = buscarUsuarioActivo(username);
        var lugar = lugares.findById(solicitud.getLugarId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El lugar no existe"));

        Resena resena = new Resena();
        resena.setFecha(LocalDate.now());
        resena.setAutor(autor);
        resena.setLugar(lugar);
        resena.setCalificacion(solicitud.getCalificacion());
        resena.setTexto(solicitud.getTexto().strip());
        resena.setEstado(EstadoResenia.PUBLICADA);
        Resena guardada = repositorio.save(resena);

        autor.setTotalResenas(autor.getTotalResenas() + 1);
        usuarios.save(autor);
        return aRespuesta(guardada);
    }

    private CuentaUsuario buscarUsuarioActivo(String username) {
        return usuarios.findByUsernameIgnoreCase(username.strip())
                .filter(CuentaUsuario::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe"));
    }

    /** Convierte la entidad en el JSON que consumen el feed y los perfiles. */
    private static ResenaResponse aRespuesta(Resena r) {
        var autor = new ResenaResponse.AutorResumen(r.getAutor().getUsername(), r.getAutor().getNombre());
        var l = r.getLugar();
        var lugar = new ResenaResponse.LugarResumen(l.getId(), l.getNombre(), l.getCategoria().getNombre(),
                l.getDireccion(), l.getImagen());
        return new ResenaResponse(r.getId(), r.getFecha(), r.getCalificacion(), r.getTexto(), r.getImagen(),
                autor, lugar);
    }

    /** Convierte la entidad en los datos que necesitan las plantillas. */
    static ResenaVista aVista(Resena r) {
        var autor = r.getAutor();
        var usuario = new UsuarioVista(autor.getId(), autor.getNombre(), autor.getUsername(), autor.getEmail(),
                autor.getTotalResenas(), autor.isActivo() ? "Activo" : "Inactivo", autor.esAdministrador());
        var l = r.getLugar();
        var c = l.getCategoria();
        var categoria = new CategoriaVista(c.getId(), c.getNombre(), c.getDescripcion(), c.getTotalLugares());
        var lugar = new LugarVista(l.getId(), l.getNombre(), categoria, l.getDireccion(), 0, 0,
                l.getDescripcion() == null ? "" : l.getDescripcion());
        return new ResenaVista(r.getId(), r.getFecha(), usuario, lugar, r.getCalificacion(),
                etiqueta(r.getEstado()), r.getTexto(), r.getImagen());
    }

    private static String etiqueta(EstadoResenia estado) {
        return switch (estado) {
            case PUBLICADA -> "Publicado";
            case REPORTADA -> "Reportado";
            case OCULTA -> "Oculto";
        };
    }
}