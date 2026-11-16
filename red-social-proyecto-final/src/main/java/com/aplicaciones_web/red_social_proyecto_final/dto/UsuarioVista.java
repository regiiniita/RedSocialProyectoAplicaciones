package com.aplicaciones_web.red_social_proyecto_final.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** Datos públicos de la cuenta que necesita la tabla de usuarios. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioVista {
    private long id;
    private String nombre;
    private String username;
    private String email;
    private int totalResenas;
    private String estado;
    private boolean administrador;

    /** Indica si la tabla debe ofrecer la acción de desactivar. */
    public boolean isDesactivable() {
        return "Activo".equals(estado) && !administrador;
    }
}
