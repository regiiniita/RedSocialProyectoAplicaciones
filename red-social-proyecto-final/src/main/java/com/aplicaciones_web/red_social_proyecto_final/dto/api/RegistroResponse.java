package com.aplicaciones_web.red_social_proyecto_final.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Datos de la cuenta que se devuelven después del registro. */
@Data
@AllArgsConstructor
public class RegistroResponse {

    private Long id;
    private String username;
    private String email;
}