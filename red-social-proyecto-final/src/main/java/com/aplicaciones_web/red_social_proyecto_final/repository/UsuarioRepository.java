package com.aplicaciones_web.red_social_proyecto_final.repository;

import com.aplicaciones_web.red_social_proyecto_final.model.CuentaUsuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Spring Data JPA realiza las consultas y guarda los cambios en la base de datos. */
@Repository
public interface UsuarioRepository extends JpaRepository<CuentaUsuario, Long> {
    /** Busca las credenciales sin distinguir mayúsculas en el nombre de usuario. */
    Optional<CuentaUsuario> findByUsernameIgnoreCase(String username);

    /** Mantiene un orden estable en el listado. */
    List<CuentaUsuario> findAllByOrderByIdAsc();

    /** Busca coincidencias parciales en los datos visibles de la cuenta. */
    List<CuentaUsuario> findByNombreContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByIdAsc(
            String nombre, String username, String email);

    /** Cuenta los usuarios activos en la plataforma. */
    long countByActivoTrue();

    /** Busca los 5 usuarios activos con mas reseñas en la plataforma. */
    List<CuentaUsuario> findTop5ByActivoTrueOrderByTotalResenasDesc();
}
