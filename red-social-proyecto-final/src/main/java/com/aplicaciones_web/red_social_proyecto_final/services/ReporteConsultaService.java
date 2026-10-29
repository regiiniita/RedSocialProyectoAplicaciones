package com.aplicaciones_web.red_social_proyecto_final.services;

import com.aplicaciones_web.red_social_proyecto_final.admin.AdminDatos;
import com.aplicaciones_web.red_social_proyecto_final.dto.ReporteVista;
import com.aplicaciones_web.red_social_proyecto_final.dto.ReportesListadoVista;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Prepara el listado de reportes a partir de los datos de muestra. */
@Service
@RequiredArgsConstructor
public class ReporteConsultaService {
    private final AdminDatos datos;

    public ReportesListadoVista consultar() {
        var reportes = datos.reportes();
        long pendientes = reportes.stream().filter(ReporteVista::isPendiente).count();
        var motivos = reportes.stream().map(ReporteVista::getMotivo).distinct().toList();
        return new ReportesListadoVista(reportes, pendientes, motivos);
    }
}
