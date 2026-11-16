package com.aplicaciones_web.red_social_proyecto_final.dto.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Datos que envía el formulario Compartir reseña. */
@Data
public class CrearResenaRequest {

    @NotNull(message = "El lugar es obligatorio")
    private Long lugarId;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(max = 1000, message = "El comentario no puede pasar de 1000 caracteres")
    private String texto;
}