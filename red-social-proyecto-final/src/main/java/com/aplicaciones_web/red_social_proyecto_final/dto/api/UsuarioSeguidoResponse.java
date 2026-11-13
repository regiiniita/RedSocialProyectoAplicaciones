package com.aplicaciones_web.red_social_proyecto_final.dto.api;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioSeguidoResponse {

    private Long id;
    private String username;
    private String nombre;
}
