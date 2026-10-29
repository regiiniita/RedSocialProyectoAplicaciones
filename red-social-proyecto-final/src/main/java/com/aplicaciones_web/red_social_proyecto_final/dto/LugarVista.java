package com.aplicaciones_web.red_social_proyecto_final.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** Información del lugar que se muestra en el listado y sus formularios. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LugarVista {
    private long id;
    private String nombre;
    private CategoriaVista categoria;
    private String direccion;
    private int totalResenas;
    private double promedio;
    private String descripcion;
}
