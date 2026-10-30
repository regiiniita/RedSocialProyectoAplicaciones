package com.aplicaciones_web.red_social_proyecto_final.repository;

import com.aplicaciones_web.red_social_proyecto_final.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Consulta y guarda las reseñas que modera el administrador. */
@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    /** Muestra primero las reseñas más recientes. */
    List<Resena> findAllByOrderByFechaDescIdDesc();

    /** Busca coincidencias parciales en el usuario o en el lugar reseñado. */
    List<Resena> findByAutorUsernameContainingIgnoreCaseOrLugarNombreContainingIgnoreCaseOrderByFechaDescIdDesc(
            String username, String lugar);
}
