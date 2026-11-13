package com.aplicaciones_web.red_social_proyecto_final.services;

import Dominio.Excepciones.CredencialesInvalidasException;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.SesionRequest;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.SesionResponse;
import com.aplicaciones_web.red_social_proyecto_final.model.CuentaUsuario;
import com.aplicaciones_web.red_social_proyecto_final.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Comprueba las credenciales del cliente y solicita su token. */
@Service
@RequiredArgsConstructor
public class SesionService {


    private final UsuarioRepository repositorio;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /** Solo genera el token si la cuenta está activa y la contraseña coincide. */
    public SesionResponse iniciar(SesionRequest solicitud) {
        CuentaUsuario cuenta = repositorio.findByUsernameIgnoreCase(solicitud.getUsername().strip())
                .filter(usuario -> usuario.isActivo())
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos."));
        if(!passwordEncoder.matches(
                solicitud.getPassword(),cuenta.getPasswordHash()
        )){
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }
        String token =jwtService.generar(cuenta.getUsername());
        return new SesionResponse(token);
    }

}
