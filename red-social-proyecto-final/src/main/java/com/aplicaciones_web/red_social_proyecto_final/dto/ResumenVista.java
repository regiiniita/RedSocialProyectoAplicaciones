package com.aplicaciones_web.red_social_proyecto_final.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** Totales presentados en las tarjetas de estadísticas. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenVista {
    private int totalLugares;
    private int resenasMes;
    private int usuariosActivos;
    private double calificacionPromedio;
}
