package com.aplicaciones_web.red_social_proyecto_final.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** Datos del listado y sus controles de presentación. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportesListadoVista {
    private List<ReporteVista> reportes;
    private long pendientes;
    private List<String> motivos;
}
