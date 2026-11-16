package com.aplicaciones_web.red_social_proyecto_final.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Token que se devuelve cuando el inicio de sesión es correcto. */
@Data
@AllArgsConstructor
public class SesionResponse {
    private String token;
}
