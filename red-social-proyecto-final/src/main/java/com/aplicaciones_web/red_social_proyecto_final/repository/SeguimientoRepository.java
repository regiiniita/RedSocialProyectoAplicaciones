package com.aplicaciones_web.red_social_proyecto_final.repository;

import com.aplicaciones_web.red_social_proyecto_final.model.CuentaUsuario;
import com.aplicaciones_web.red_social_proyecto_final.model.SeguimientoUsuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface SeguimientoRepository extends JpaRepository<SeguimientoUsuario, Long> {

    /** Indica si el seguidor ya sigue a el usuario */
    boolean existsBySeguidorIdAndSeguidoId(Long seguidorId, Long seguidoId);

    /** Usuarios seguidos por una cuenta*/
    @Query("select s.seguido from SeguimientoUsuario s "
            + "where s.seguidor.id = :seguidorId order by s.fechaSeguimiento desc, s.id desc")
    List<CuentaUsuario> findSeguidosBySeguidorId(Long seguidorId);

    /** Elimina el seguimiento, devuelve cuantos registros fueron borrados*/
    @Modifying
    @Query("delete from SeguimientoUsuario s where s.seguidor.id = :seguidorId and s.seguido.id = :seguidoId")
    int eliminarPorSeguidorYSeguido(Long seguidorId, Long seguidoId);

    /** Cuenta los seguidores activos de una cuenta */
    @Query("select count(s) from SeguimientoUsuario s "
            + "where s.seguido.id = :usuarioId and s.seguidor.activo = true")
    long contarSeguidores(Long usuarioId);

    /** Cuenta las cuentas activas que sigue una cuenta */
    @Query("select count(s) from SeguimientoUsuario s "
            + "where s.seguidor.id = :usuarioId and s.seguido.activo = true")
    long contarSeguidos(Long usuarioId);
}