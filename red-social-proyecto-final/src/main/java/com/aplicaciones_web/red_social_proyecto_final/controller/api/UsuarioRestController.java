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



}
