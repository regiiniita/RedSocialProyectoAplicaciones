package com.aplicaciones_web.red_social_proyecto_final.dto.api;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Datos de una tarjeta de reseña del feed Tu comunidad y de la pestaña Reseñas de un perfil.
 */
@Data
@AllArgsConstructor
public class ResenaResponse {

    private Long id;
    private LocalDate fecha;
    private int calificacion;
    private String texto;
    private String imagen;
    private AutorResumen autor;
    private LugarResumen lugar;

    /** Usuario que escribió la reseña. */
    @Data
    @AllArgsConstructor
    public static class AutorResumen {
        private String username;
        private String nombre;
    }

    /** Lugar reseñado, con lo que muestra la tarjeta. */
    @Data
    @AllArgsConstructor
    public static class LugarResumen {
        private Long id;
        private String nombre;
        private String categoria;
        private String direccion;
        private String imagen;
    }
}