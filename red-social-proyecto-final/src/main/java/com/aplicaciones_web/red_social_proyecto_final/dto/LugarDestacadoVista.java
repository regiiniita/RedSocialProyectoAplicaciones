package com.aplicaciones_web.red_social_proyecto_final.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** Cantidad de reseñas y porcentaje usado en la barra del dashboard. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LugarDestacadoVista {
    private String nombre;
    private int porcentaje;
    private int totalResenas;
}
