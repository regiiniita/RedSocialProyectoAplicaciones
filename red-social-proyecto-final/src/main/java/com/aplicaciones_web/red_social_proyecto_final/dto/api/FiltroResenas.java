package com.aplicaciones_web.red_social_proyecto_final.dto.api;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroResenas {

    /** Solo las reseñas escritas por este usuario*/
    @Size(max = 50, message = "El autor no puede pasar de 50 caracteres")
    private String autor = "";

    /** Solo las reseñas de los usuarios que sigue este usuario*/
    @Size(max = 50, message = "El usuario no puede pasar de 50 caracteres")
    private String seguidoPor = "";
}