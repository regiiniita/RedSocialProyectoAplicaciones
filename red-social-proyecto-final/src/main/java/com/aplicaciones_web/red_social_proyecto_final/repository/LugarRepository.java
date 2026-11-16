package com.aplicaciones_web.red_social_proyecto_final.repository;


import com.aplicaciones_web.red_social_proyecto_final.model.Categoria;
import com.aplicaciones_web.red_social_proyecto_final.model.Lugar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Consulta y guarda los lugares del catálogo. */
@Repository
public interface LugarRepository extends JpaRepository<Lugar, Long> {

    /** Muestra primero los lugares más recientes. */
    List<Lugar> findAllByOrderByIdDesc();

    /** Busca coincidencias parciales en el nombre, la dirección o el ID, sin distinguir mayúsculas. */
    @Query("""
            select l from Lugar l
            where lower(l.nombre) like lower(concat('%', :buscar, '%'))
               or lower(l.direccion) like lower(concat('%', :buscar, '%'))
               or cast(l.id as string) like concat('%', :buscar, '%')
            order by l.id desc
            """)
    List<Lugar> buscar(@Param("buscar") String buscar);

    /** Evita registrar dos veces el mismo lugar al crear. */
    boolean existsByNombreIgnoreCaseAndDireccionIgnoreCase(String nombre, String direccion);

    /** Igual que el anterior, pero excluye al lugar que se está editando. */
    boolean existsByNombreIgnoreCaseAndDireccionIgnoreCaseAndIdNot(String nombre, String direccion, Long id);

    /** Cantidad de lugares de una categoría; alimenta totalLugares. */
    long countByCategoria(Categoria categoria);
}
