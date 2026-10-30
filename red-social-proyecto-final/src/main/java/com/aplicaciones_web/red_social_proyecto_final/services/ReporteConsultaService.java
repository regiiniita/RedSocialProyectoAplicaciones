package com.aplicaciones_web.red_social_proyecto_final.services;

import Dominio.EstadoReporte;
import Dominio.EstadoResenia;
import com.aplicaciones_web.red_social_proyecto_final.admin.AdminDatos;
import com.aplicaciones_web.red_social_proyecto_final.dto.ReporteVista;
import com.aplicaciones_web.red_social_proyecto_final.dto.ReportesListadoVista;
import com.aplicaciones_web.red_social_proyecto_final.model.ReporteResena;
import com.aplicaciones_web.red_social_proyecto_final.repository.ReporteRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Prepara el listado de reportes a partir de los datos de muestra. */
@Service
@RequiredArgsConstructor
public class ReporteConsultaService {
    private final AdminDatos datos;

    private final ReporteRepository repositorio;
    private final ResenaRepository resenas;

    /*
    public ReportesListadoVista consultar() {
        var reportes = datos.reportes();
        long pendientes = reportes.stream().filter(ReporteVista::isPendiente).count();
        var motivos = reportes.stream().map(ReporteVista::getMotivo).distinct().toList();
        return new ReportesListadoVista(reportes, pendientes, motivos);
    }
    */

    /**
     * Arma el listado de reportes aplicando los filtros recibidos.
     *
     * @param estado PENDIENTE, REVISADO o DESCARTADO; vacío para mostrar todos
     * @param motivo motivo exacto del reporte; vacío para mostrar todos
     * @param busqueda texto para buscar por reportante, autor o lugar; vacío para no filtrar
     */
    public ReportesListadoVista consultar(String estado, String motivo, String busqueda) {
        var reportes = repositorio.filtrar(convertirEstado(estado),
                        motivo == null ? "" : motivo.strip(),
                        busqueda == null ? "" : busqueda.strip())
                .stream().map(ReporteConsultaService::aVista).toList();
        return new ReportesListadoVista(reportes, repositorio.countByEstado(EstadoReporte.PENDIENTE),
                repositorio.findMotivos());
    }

    /**
     * Revisa un reporte ocultando la reseña. Los demás reportes pendientes de esa reseña también se resuelven.
     *
     * @throws IllegalArgumentException si el reporte no existe o ya fue resuelto
     */
    public void ocultarResena(long idReporte) {
        var reporte = buscarPendiente(idReporte);
        var resena = reporte.getResena();
        resena.setEstado(EstadoResenia.OCULTA);
        resenas.save(resena);

        var pendientes = repositorio.findByResenaAndEstado(resena, EstadoReporte.PENDIENTE);
        pendientes.forEach(r -> r.setEstado(EstadoReporte.REVISADO));
        repositorio.saveAll(pendientes);
    }

    /**
     * Descarta un reporte y deja la reseña visible si ya no tiene reportes pendientes.
     *
     * @throws IllegalArgumentException si el reporte no existe o ya fue resuelto
     */
    public void descartar(long idReporte) {
        var reporte = buscarPendiente(idReporte);
        reporte.setEstado(EstadoReporte.DESCARTADO);
        repositorio.save(reporte);

        var resena = reporte.getResena();
        // El reporte descartado ya no cuenta como pendiente porque se acaba de guardar.
        boolean sinPendientes = repositorio.findByResenaAndEstado(resena, EstadoReporte.PENDIENTE).isEmpty();
        if (resena.getEstado() == EstadoResenia.REPORTADA && sinPendientes) {
            resena.setEstado(EstadoResenia.PUBLICADA);
            resenas.save(resena);
        }
    }

    private ReporteResena buscarPendiente(long id) {
        var reporte = repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El reporte no existe."));
        if (reporte.getEstado() != EstadoReporte.PENDIENTE) {
            throw new IllegalArgumentException("El reporte ya fue resuelto.");
        }
        return reporte;
    }

    // Un valor desconocido equivale a no filtrar por estado.
    private static EstadoReporte convertirEstado(String estado) {
        try {
            return estado == null || estado.isBlank() ? null : EstadoReporte.valueOf(estado.strip().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private static ReporteVista aVista(ReporteResena r) {
        String estado = switch (r.getEstado()) {
            case PENDIENTE -> "Pendiente";
            case REVISADO -> "Revisado";
            case DESCARTADO -> "Descartado";
        };
        return new ReporteVista(r.getId(), r.getFecha(), "@" + r.getReportante().getUsername(),
                ResenaService.aVista(r.getResena()), r.getMotivo(), estado, r.getComentario());
    }
}
