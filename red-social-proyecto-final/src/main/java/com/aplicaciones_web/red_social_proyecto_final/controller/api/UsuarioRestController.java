package com.aplicaciones_web.red_social_proyecto_final.controller.api;

import Dominio.Usuario;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.RegistroRequest;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.RegistroResponse;
import com.aplicaciones_web.red_social_proyecto_final.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.ActualizarPerfilRequest;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.PerfilResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

   @Autowired
    private UsuarioService usuarioService;

    /**
     * La pantalla de Registro envía el usuario, correo y contraseña a POST /api/usuarios.
     * Se usa POST porque se crea una cuenta. La respuesta JSON es 201 con ID, usuario y correo.
     */
   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistroResponse> registrar(@Valid @RequestBody RegistroRequest solicitud) {
       RegistroResponse cuenta = usuarioService.registrar(solicitud);
       return ResponseEntity.status(HttpStatus.CREATED).body(cuenta);
   }

    /**
     * La pantalla Mi Perfil muestra nombre, @usuario y contadores del usuario autenticado.
     * La pantalla Configuración también lo usa para llenar el formulario. Requiere JWT.
     */
    @GetMapping("/me")
    public PerfilResponse miPerfil(Authentication autenticacion) {
        return usuarioService.obtenerMiPerfil(autenticacion.getName());
    }

    /**
     * El botón "Guardar Cambios" de Configura tu cuenta envía nombres, apellidos,
     * fecha de nacimiento y género. Se usa PATCH porque solo cambia algunos campos. Requiere JWT.
     */
    @PatchMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PerfilResponse actualizarMiPerfil(Authentication autenticacion,
                                             @Valid @RequestBody ActualizarPerfilRequest solicitud) {
        return usuarioService.actualizarPerfil(autenticacion.getName(), solicitud);
    }

    /**
     * La pantalla de Perfil público muestra el encabezado de otro usuario. Es pública.
     */
    @GetMapping("/{username}")
    public PerfilResponse perfilPublico(@PathVariable String username) {
        return usuarioService.obtenerPerfilPublico(username);
    }

}
