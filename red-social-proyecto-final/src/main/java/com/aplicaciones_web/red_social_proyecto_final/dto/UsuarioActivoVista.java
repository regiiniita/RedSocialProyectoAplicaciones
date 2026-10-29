package com.aplicaciones_web.red_social_proyecto_final.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** Actividad de un usuario en el resumen del panel. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioActivoVista {
    private String nombre;
    private String handle;
    private int conteoResenas;
}
