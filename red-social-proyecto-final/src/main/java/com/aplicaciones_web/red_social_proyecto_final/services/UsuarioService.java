package com.aplicaciones_web.red_social_proyecto_final.services;

import Dominio.Excepciones.RecursoDuplicadoException;
import Dominio.Rol;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.RegistroRequest;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.RegistroResponse;
import com.aplicaciones_web.red_social_proyecto_final.model.CuentaUsuario;
import lombok.RequiredArgsConstructor;
import com.aplicaciones_web.red_social_proyecto_final.repository.UsuarioRepository;
import java.util.List;
import com.aplicaciones_web.red_social_proyecto_final.dto.UsuarioVista;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Dominio.Excepciones.RecursoNoEncontradoException;
import Dominio.Excepciones.ReglaNegocioException;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.ActualizarPerfilRequest;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.PerfilResponse;
import com.aplicaciones_web.red_social_proyecto_final.repository.SeguimientoRepository;

/**
 * Contiene las consultas y las reglas para desactivar cuentas de usuario.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repositorio;
    private final PasswordEncoder passwordEncoder;
    private final SeguimientoRepository seguimientos;
    /**
     * Busca coincidencias en el nombre, usuario o correo.
     *
     * @param busqueda texto escrito en el formulario; vacío para mostrar todos
     * @return usuarios que coinciden con la búsqueda
     */
    @Transactional(readOnly = true)
    public List<UsuarioVista> consultar(String busqueda) {
        String filtro = busqueda == null ? "" : busqueda.strip();
        var usuarios = filtro.isEmpty()
                ? repositorio.findAllByOrderByIdAsc()
                : repositorio.findByNombreContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByIdAsc(
                        filtro, filtro, filtro);
        return usuarios.stream()
                .map(u -> new UsuarioVista(u.getId(), u.getNombre(), u.getUsername(), u.getEmail(),
                        u.getTotalResenas(), u.isActivo() ? "Activo" : "Inactivo", u.esAdministrador())).toList();
    }

    /**
     * Marca una cuenta como inactiva después de validar la solicitud.
     *
     * @param id identificador del usuario seleccionado
     * @param administrador nombre del administrador que realiza la acción
     * @throws IllegalArgumentException si la cuenta no existe o no se puede desactivar
     */
    @Transactional
    public void desactivar(long id, String administrador) {
        var usuario = repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));
        // Evita que un administrador pierda su acceso desde esta pantalla.
        if (usuario.getUsername().equalsIgnoreCase(administrador)) {
            throw new IllegalArgumentException("No puedes desactivar tu propia cuenta.");
        }
        if (usuario.esAdministrador()) {
            throw new IllegalArgumentException("No se permite desactivar administradores desde este panel.");
        }
        if (!usuario.isActivo()) throw new IllegalArgumentException("El usuario ya está inactivo.");
        // La desactivación conserva el registro y sus datos asociados.
        usuario.setActivo(false);
        repositorio.save(usuario);
    }

    /** Crea una cuenta normal con usuario y correo únicos y contraseña con BCrypt. */
    @Transactional
    public RegistroResponse registrar(RegistroRequest solicitud) {
        String username = solicitud.getUsername().strip();
        String email = solicitud.getEmail().strip();

        if (username.equalsIgnoreCase("me")) {
            throw new ReglaNegocioException("Ese nombre de usuario no está disponible.");
        }

        if (repositorio.findByUsernameIgnoreCase(username).isPresent()) {
            throw new RecursoDuplicadoException("El usuario ya está registrado.");
        }

        if (repositorio.existsByEmailIgnoreCase(email)) {
            throw new RecursoDuplicadoException("El correo ya está registrado.");
        }

        CuentaUsuario cuenta = new CuentaUsuario();
        cuenta.setNombre(username);
        cuenta.setUsername(username);
        cuenta.setEmail(email);
        cuenta.setPasswordHash(passwordEncoder.encode(solicitud.getPassword()));
        cuenta.setRol(Rol.USUARIO_REGISTRADO.name());
        cuenta.setActivo(true);
        cuenta.setTotalResenas(0);

        CuentaUsuario guardada = repositorio.save(cuenta);

        return new RegistroResponse(
                guardada.getId(),
                guardada.getUsername(),
                guardada.getEmail()
        );


    }

    /** Datos de Mi Perfil del usuario autenticado. Incluye los datos privados. */
    @Transactional(readOnly = true)
    public PerfilResponse obtenerMiPerfil(String username) {
        return aPerfil(buscarActivo(username), true);
    }

    /** Datos del perfil público de otro usuario. No incluye correo, fecha ni género. */
    @Transactional(readOnly = true)
    public PerfilResponse obtenerPerfilPublico(String username) {
        return aPerfil(buscarActivo(username), false);
    }

    /** Guarda solo los campos que llegaron en la solicitud (PATCH). */
    @Transactional
    public PerfilResponse actualizarPerfil(String username, ActualizarPerfilRequest solicitud) {
        CuentaUsuario cuenta = buscarActivo(username);
        if (solicitud.getNombres() != null) cuenta.setNombre(solicitud.getNombres().strip());
        if (solicitud.getApellidos() != null) cuenta.setApellidos(solicitud.getApellidos().strip());
        if (solicitud.getFechaNacimiento() != null) cuenta.setFechaNacimiento(solicitud.getFechaNacimiento());
        if (solicitud.getGenero() != null) cuenta.setGenero(solicitud.getGenero());
        return aPerfil(repositorio.save(cuenta), true);
    }

    private CuentaUsuario buscarActivo(String username) {
        return repositorio.findByUsernameIgnoreCase(username.strip())
                .filter(CuentaUsuario::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe"));
    }

    private PerfilResponse aPerfil(CuentaUsuario u, boolean propio) {
        return new PerfilResponse(
                u.getId(),
                u.getUsername(),
                u.getNombre(),
                u.getApellidos(),
                propio ? u.getEmail() : null,
                propio ? u.getFechaNacimiento() : null,
                propio ? u.getGenero() : null,
                u.getTotalResenas(),
                seguimientos.contarSeguidores(u.getId()),
                seguimientos.contarSeguidos(u.getId()));
    }

}
