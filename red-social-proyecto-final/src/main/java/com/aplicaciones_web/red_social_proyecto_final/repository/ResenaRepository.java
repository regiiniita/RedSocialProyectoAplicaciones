package com.aplicaciones_web.red_social_proyecto_final.repository;

import Dominio.EstadoResenia;
import com.aplicaciones_web.red_social_proyecto_final.model.Lugar;
import com.aplicaciones_web.red_social_proyecto_final.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

/** Consulta y guarda las reseñas que modera el administrador. */
@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    /** Muestra primero las reseñas más recientes. */
    List<Resena> findAllByOrderByFechaDescIdDesc();

    /** Busca coincidencias parciales en el usuario o en el lugar reseñado. */
    List<Resena> findByAutorUsernameContainingIgnoreCaseOrLugarNombreContainingIgnoreCaseOrderByFechaDescIdDesc(
            String username, String lugar);

    /** Busca la cantidad de reseñas en el mes. */
    long countByFechaBetween(LocalDate inicio, LocalDate fin);

    /** Saca la calificación promedio enre todas las reseñas.  */
    @Query("select avg(r.calificacion) from Resena r where r.estado <> Dominio.EstadoResenia.OCULTA")
    Double promedioCalificacion();

    /** Reseñas de un lugar. */
    List<Resena> findByLugar(Lugar lugar);

    /** Cantidad y promedio de reseñas por lugar, sin contar las ocultas. */
    @Query("""
            select r.lugar.id as lugarId, count(r) as total, avg(r.calificacion) as promedio
            from Resena r
            where r.estado <> :excluido
            group by r.lugar.id
            """)
    List<ResumenLugar> resumenPorLugar(@Param("excluido") EstadoResenia excluido);

    /** Borra las reseñas de un lugar; antes hay que borrar sus reportes. */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Resena r where r.lugar = :lugar")
    void eliminarPorLugar(@Param("lugar") Lugar lugar);

    /** Resultado de resumenPorLugar. */
    interface ResumenLugar {
        Long getLugarId();
        Long getTotal();
        Double getPromedio();
    }

}
