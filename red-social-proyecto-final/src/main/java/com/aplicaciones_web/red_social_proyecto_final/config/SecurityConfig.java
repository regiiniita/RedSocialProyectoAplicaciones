package com.aplicaciones_web.red_social_proyecto_final.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.time.Instant;
import org.springframework.http.HttpMethod;

/**
 * Define el acceso al panel y el manejo de la sesión del administrador.
 */
@Configuration
public class SecurityConfig {
    /**
     * Proporciona BCrypt para cifrar y comprobar las contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    /**
     * Valida los JWT que emite JwtService, usando la misma clave configurada
     */
    @Bean
    public JwtDecoder jwtDecoder(@Value("${JWT_SECRET}") String secretoBase64) {
        var clave = new SecretKeySpec(Base64.getDecoder().decode(secretoBase64), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(clave).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /**
     * Configura las rutas publicas, el rol requerido y el inicio y cierre de sesion
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF permanece habilitado para los formularios del panel; la API usa JWT sin sesión y queda exenta.
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
        // Sin token, con token inválido o vencido, la API responde 401 en JSON en lugar de redirigir al login.
        AuthenticationEntryPoint noAutenticado = (request, response, ex) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"timestamp\":\"" + Instant.now() + "\",\"status\":401,"
                    + "\"error\":\"Unauthorized\",\"mensaje\":\"Se requiere un token JWT válido\","
                    + "\"ruta\":\"" + request.getRequestURI() + "\"}");
        };
        http.oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults())
                .authenticationEntryPoint(noAutenticado));
        http.exceptionHandling(ex -> ex.defaultAuthenticationEntryPointFor(noAutenticado,
                request -> request.getRequestURI().startsWith("/api/")));
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/me", "/api/usuarios/me/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/resenas").authenticated()
                        .requestMatchers("/admin/login", "/styles/**", "/uploads/**", "/images/**", "/img/**").permitAll()
                        .requestMatchers("/","/admin", "/admin/**").hasRole("ADMINISTRADOR")
                        .anyRequest().permitAll())
                .formLogin(login -> login.loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/usuarios", true)
                        .failureUrl("/admin/login?error").permitAll())
                // Al salir se elimina la autenticación y se invalida la sesión HTTP.
                .logout(logout -> logout.logoutUrl("/admin/logout")
                        .invalidateHttpSession(true).clearAuthentication(true)
                        .deleteCookies("JSESSIONID").logoutSuccessUrl("/admin/login?logout"));
        return http.build();
    }
}
