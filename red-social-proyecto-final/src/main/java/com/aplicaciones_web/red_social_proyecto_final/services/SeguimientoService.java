package com.aplicaciones_web.red_social_proyecto_final.services;
import Dominio.Excepciones.ReglaNegocioException;
import Dominio.Excepciones.RecursoNoEncontradoException;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.UsuarioSeguidoResponse;
import com.aplicaciones_web.red_social_proyecto_final.model.CuentaUsuario;
import com.aplicaciones_web.red_social_proyecto_final.model.SeguimientoUsuario;
import com.aplicaciones_web.red_social_proyecto_final.repository.SeguimientoRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeguimientoService {

    private final UsuarioRepository usuarios;
    private final SeguimientoRepository seguimientos;

    /**
     * Lista los usuarios activos que sigue la cuenta indicada
     */
    @Transactional(readOnly = true)
    public List<UsuarioSeguidoResponse> listarSeguidos(String username) {
        CuentaUsuario cuenta = buscarActivo(username);
        return seguimientos.findSeguidosBySeguidorId(cuenta.getId()).stream()
                .filter(CuentaUsuario::isActivo)
                .map(u -> new UsuarioSeguidoResponse(u.getId(), u.getUsername(), u.getNombre()))
                .toList();
    }

    /**
     * Hace que el usuario autenticado siga a otro
     */
    @Transactional
    public void seguir(String usernameActual, String usernameSeguido) {
        CuentaUsuario seguidor = buscarActivo(usernameActual);
        CuentaUsuario seguido = buscarActivo(usernameSeguido);
        if (seguidor.getId().equals(seguido.getId())) {
            throw new ReglaNegocioException("Un usuario no puede seguirse a si mismo");
        }
        if (seguimientos.existsBySeguidorIdAndSeguidoId(seguidor.getId(), seguido.getId())) {
            return;
        }
        SeguimientoUsuario nuevo = new SeguimientoUsuario();
        nuevo.setSeguidor(seguidor);
        nuevo.setSeguido(seguido);
        seguimientos.save(nuevo);
    }

    /**
     * Deja de seguir a un usuario
     */
    @Transactional
    public void dejarDeSeguir(String usernameActual, String usernameSeguido) {
        CuentaUsuario seguidor = buscarActivo(usernameActual);
        // Se permite dejar de seguir aunque la cuenta se haya desactivado después.
        CuentaUsuario seguido = usuarios.findByUsernameIgnoreCase(usernameSeguido.strip())
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe"));
        seguimientos.eliminarPorSeguidorYSeguido(seguidor.getId(), seguido.getId());
    }

    /**
     * Lista los usuarios activos que siguen la cuenta indicada
     */
    @Transactional(readOnly = true)
    public List<UsuarioSeguidoResponse> listarSeguidores(String username) {
        CuentaUsuario cuenta = buscarActivo(username);
        return seguimientos.findSeguidoresBySeguidoId(cuenta.getId()).stream()
                .filter(CuentaUsuario::isActivo)
                .map(u -> new UsuarioSeguidoResponse(u.getId(), u.getUsername(), u.getNombre()))
                .toList();
    }

    private CuentaUsuario buscarActivo(String username) {
        return usuarios.findByUsernameIgnoreCase(username.strip())
                .filter(CuentaUsuario::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe"));
    }

}
