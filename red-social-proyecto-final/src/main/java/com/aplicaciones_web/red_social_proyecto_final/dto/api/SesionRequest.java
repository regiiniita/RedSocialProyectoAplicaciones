package com.aplicaciones_web.red_social_proyecto_final.dto.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Usuario y contraseña que se reciben al iniciar sesión. */
@Data
public class SesionRequest {

    @NotBlank(message = "El usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraséña es obligatoria")
    private String password;
}
