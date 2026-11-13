package com.aplicaciones_web.red_social_proyecto_final.controller.api;

import com.aplicaciones_web.red_social_proyecto_final.dto.api.UsuarioSeguidoResponse;
import com.aplicaciones_web.red_social_proyecto_final.services.SeguimientoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class SeguimientoRestController {

    @Autowired
    private SeguimientoService seguimientoService;

    /**
     * Devuelve los usuarios que sigue
     */
    @GetMapping("/{username}/seguidos")
    public List<UsuarioSeguidoResponse> listarSeguidos(@PathVariable String username) {
        return seguimientoService.listarSeguidos(username);
    }

    /**
     * El usuario autenticado empieza a seguir al usuario que contenga el username indicado
     */
    @PutMapping("/me/seguidos/{username}")
    public ResponseEntity<Void> seguir(Authentication autenticacion, @PathVariable String username) {
        seguimientoService.seguir(autenticacion.getName(), username);
        return ResponseEntity.noContent().build();
    }

    /**
     * El usuario autenticado deja de seguir al usuario que contenga el username indicado
     */
    @DeleteMapping("/me/seguidos/{username}")
    public ResponseEntity<Void> dejarDeSeguir(Authentication autenticacion, @PathVariable String username) {
        seguimientoService.dejarDeSeguir(autenticacion.getName(), username);
        return ResponseEntity.noContent().build();
    }
}
