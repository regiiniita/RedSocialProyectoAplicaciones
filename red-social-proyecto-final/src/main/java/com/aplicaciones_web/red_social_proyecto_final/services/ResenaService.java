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

import java.util.List;

/**
 * Contiene las consultas y las reglas para moderar reseñas.
 */
@Service
@RequiredArgsConstructor
public class ResenaService {
    private final ResenaRepository repositorio;
    private final ReporteRepository reportes;

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

    /** Convierte la entidad en los datos que necesitan las plantillas. */
    static ResenaVista aVista(Resena r) {
        var autor = r.getAutor();
        var usuario = new UsuarioVista(autor.getId(), autor.getNombre(), autor.getUsername(), autor.getEmail(),
                autor.getTotalResenas(), autor.isActivo() ? "Activo" : "Inactivo", autor.esAdministrador());
        // El lugar aún no tiene tabla propia; se muestra con los datos guardados en la reseña.
        var categoria = new CategoriaVista(0, r.getLugarCategoria(), "", 0);
        var lugar = new LugarVista(0, r.getLugarNombre(), categoria, "", 0, 0, "");
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
