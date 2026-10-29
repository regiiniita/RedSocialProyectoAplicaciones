package com.aplicaciones_web.red_social_proyecto_final.services;

import lombok.RequiredArgsConstructor;
import com.aplicaciones_web.red_social_proyecto_final.repository.UsuarioRepository;
import java.util.List;
import com.aplicaciones_web.red_social_proyecto_final.dto.UsuarioVista;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contiene las consultas y las reglas para desactivar cuentas de usuario.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repositorio;

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

}
