package com.aplicaciones_web.red_social_proyecto_final.dto.api;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** Parámetros de búsqueda compartidos por la página y la API de lugares. */
@Getter
@Setter
public class FiltroCatalogo {

    @Size(max = 100)
    private String query = "";

    @Size(max = 50)
    private String categoria = "";

    @Pattern(regexp = "recientes|mejor_valorado|mas_resenados")
    private String orden = "recientes";

    /** Usa el orden por fecha de alta si el formulario envía el campo vacío. */
    public void setOrden(String orden) {
        this.orden = orden == null || orden.isEmpty() ? "recientes" : orden;
    }
}