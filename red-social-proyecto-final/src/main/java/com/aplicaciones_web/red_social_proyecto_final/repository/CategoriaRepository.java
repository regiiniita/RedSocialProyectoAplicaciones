package com.aplicaciones_web.red_social_proyecto_final.repository;

import com.aplicaciones_web.red_social_proyecto_final.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Consulta y guarda las categorías del catálogo. */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Obtiene todas las categorías ordenadas alfabéticamente.
     * @return lista completa de categorías ordenada por nombre
     */
    List<Categoria> findAllByOrderByNombreAsc();

    /**
     * Busca coincidencias parciales en el nombre o en la descripción, sin distinguir mayúsculas.
     *
     * @param nombre texto a buscar dentro del nombre
     * @param descripcion texto a buscar dentro de la descripción
     * @return categorías que coinciden, ordenadas por nombre
     */
    List<Categoria> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrderByNombreAsc(
            String nombre, String descripcion);

    /**
     * Verifica si ya existe una categoría con el mismo nombre; se usa al crear una nueva.
     *
     * @param nombre nombre a comprobar
     * @return true si el nombre ya está registrado
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Verifica si otra categoría distinta ya usa el nombre; se usa al editar.
     *
     * @param nombre nombre a comprobar
     * @param id identificador de la categoría que se está editando, que se excluye de la búsqueda
     * @return true si otra categoría ya tiene ese nombre
     */
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
