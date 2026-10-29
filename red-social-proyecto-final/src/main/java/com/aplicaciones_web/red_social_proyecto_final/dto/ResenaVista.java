package com.aplicaciones_web.red_social_proyecto_final.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

/** Contenido y datos del autor de una reseña. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaVista {
    private long id;
    private LocalDate fecha;
    private UsuarioVista usuario;
    private LugarVista lugar;
    private int calificacion;
    private String estado;
    private String texto;
    private String imagen;
}
