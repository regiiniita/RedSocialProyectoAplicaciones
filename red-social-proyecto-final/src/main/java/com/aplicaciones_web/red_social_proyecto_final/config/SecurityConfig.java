package com.aplicaciones_web.red_social_proyecto_final.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Define el acceso al panel y el manejo de la sesión del administrador.
 */
@Configuration
public class SecurityConfig {
    /**
     * Proporciona BCrypt para cifrar y comprobar las contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    /**
     * Configura las rutas públicas, el rol requerido y el inicio y cierre de sesión.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF permanece habilitado; los formularios POST deben enviar su token.
        http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/admin/login", "/styles/**", "/uploads/**", "/images/**", "/img/**").permitAll()
                    .requestMatchers("/admin", "/admin/**").hasRole("ADMINISTRADOR")
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
