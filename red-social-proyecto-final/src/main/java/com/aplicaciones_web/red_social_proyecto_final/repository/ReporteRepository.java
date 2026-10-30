package com.aplicaciones_web.red_social_proyecto_final.repository;

import Dominio.EstadoReporte;
import com.aplicaciones_web.red_social_proyecto_final.model.ReporteResena;
import com.aplicaciones_web.red_social_proyecto_final.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Consulta y guarda los reportes de reseñas. */
@Repository
public interface ReporteRepository extends JpaRepository<ReporteResena, Long> {
    /**
     * Filtra por estado, motivo y texto; un valor nulo o vacío no aplica ese filtro.
     * El texto se compara con el reportante, el autor de la reseña y el lugar.
     */
    @Query("""
            select r from ReporteResena r
            where (:estado is null or r.estado = :estado)
              and (:motivo = '' or r.motivo = :motivo)
              and (:buscar = ''
                   or lower(r.reportante.username) like lower(concat('%', :buscar, '%'))
                   or lower(r.resena.autor.username) like lower(concat('%', :buscar, '%'))
                   or lower(r.resena.lugarNombre) like lower(concat('%', :buscar, '%')))
            order by r.fecha desc, r.id desc
            """)
    List<ReporteResena> filtrar(@Param("estado") EstadoReporte estado,
                                @Param("motivo") String motivo,
                                @Param("buscar") String buscar);

    /** Motivos distintos para el selector de filtros. */
    @Query("select distinct r.motivo from ReporteResena r order by r.motivo")
    List<String> findMotivos();

    long countByEstado(EstadoReporte estado);

    /** Reportes de una reseña que siguen sin resolverse. */
    List<ReporteResena> findByResenaAndEstado(Resena resena, EstadoReporte estado);
}
