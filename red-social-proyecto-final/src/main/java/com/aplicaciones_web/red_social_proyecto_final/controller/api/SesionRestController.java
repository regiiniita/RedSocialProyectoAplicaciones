package com.aplicaciones_web.red_social_proyecto_final.controller.api;

import com.aplicaciones_web.red_social_proyecto_final.dto.api.SesionRequest;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.SesionResponse;
import com.aplicaciones_web.red_social_proyecto_final.services.SesionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sesiones")
public class SesionRestController {


    @Autowired
    private SesionService sesionService;

    /**
     * El formulario de acceso del cliente envía usuario y contraseña a POST /api/sesiones.
     * Se usa POST para iniciar una sesión y devolver el JWT con un 200 en la respuesta JSON.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public SesionResponse iniciar(@Valid @RequestBody SesionRequest solicitud) {
        return sesionService.iniciar(solicitud);
    }

}

