package com.aplicaciones_web.red_social_proyecto_final.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** Datos de una categoría y su cantidad de lugares. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaVista {
    private long id;
    private String nombre;
    private String descripcion;
    private int totalLugares;
}
