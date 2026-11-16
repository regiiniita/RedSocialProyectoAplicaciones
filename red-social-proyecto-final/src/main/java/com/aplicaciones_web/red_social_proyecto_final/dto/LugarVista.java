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
    private String imagen;

    /** Permite crear vistas de lugares que no incluyen una foto. */
    public LugarVista(long id, String nombre, CategoriaVista categoria,
                     String direccion, int totalResenas, double promedio,
                     String descripcion) {
        this(id, nombre, categoria, direccion, totalResenas, promedio, descripcion, null);
    }
}
