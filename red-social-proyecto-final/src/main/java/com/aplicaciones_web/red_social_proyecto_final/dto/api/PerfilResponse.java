package com.aplicaciones_web.red_social_proyecto_final.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Datos del encabezado de Mi Perfil y del Perfil público.
 * En el perfil público, email, fechaNacimiento y genero van en null y no aparecen en el JSON.
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerfilResponse {

    private Long id;
    private String username;
    private String nombres;
    private String apellidos;
    private String email;
    private LocalDate fechaNacimiento;
    private String genero;
    private int totalResenas;
    private long totalSeguidores;
    private long totalSeguidos;
}