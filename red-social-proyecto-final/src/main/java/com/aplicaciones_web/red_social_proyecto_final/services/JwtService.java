package com.aplicaciones_web.red_social_proyecto_final.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

/** Firma los tokens de acceso del cliente con la clave configurada. */
@Service
public class JwtService {

    private final JwtEncoder encoder;

    public JwtService(@Value("${JWT_SECRET}") String secretoBase64) {
        byte[] secreto = Base64.getDecoder().decode(secretoBase64);

        if (secreto.length < 32) {
            throw new IllegalArgumentException(
                    "JWT_SECRET debe contener al menos 32 bytes al decodificarse."
            );
        }

        var clave = new SecretKeySpec(secreto, "HmacSHA256");
        this.encoder = NimbusJwtEncoder.withSecretKey(clave).build();
    }

    /** Guarda el usuario en el token y le da una vigencia de 24 horas. */
    public String generar(String username) {
        Instant ahora = Instant.now();

        JwtClaimsSet datos = JwtClaimsSet.builder()
                .subject(username)
                .issuedAt(ahora)
                .expiresAt(ahora.plus(24, ChronoUnit.HOURS))
                .build();

        JwsHeader encabezado = JwsHeader.with(MacAlgorithm.HS256).build();

        return encoder.encode(JwtEncoderParameters.from(encabezado, datos))
                .getTokenValue();
    }
}