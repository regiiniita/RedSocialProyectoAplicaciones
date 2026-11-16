package com.aplicaciones_web.red_social_proyecto_final.seguridad;

import lombok.RequiredArgsConstructor;
import com.aplicaciones_web.red_social_proyecto_final.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Entrega a Spring Security las credenciales y el rol del administrador.
 */
@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {
    private final UsuarioRepository repositorio;

    /**
     * Busca la cuenta que intenta ingresar al panel.
     *
     * @param username nombre escrito en el formulario de acceso
     * @return credenciales, rol y estado de la cuenta
     * @throws UsernameNotFoundException si la cuenta no tiene acceso administrativo
     */
    @Transactional(readOnly = true)
    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = repositorio.findByUsernameIgnoreCase(username)
                .filter(u -> u.esAdministrador())
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales incorrectas."));
        // Spring Security compara el hash y rechaza las cuentas inactivas.
        return User.withUsername(usuario.getUsername()).password(usuario.getPasswordHash())
                .roles(usuario.getRol()).disabled(!usuario.isActivo()).build();
    }
}
