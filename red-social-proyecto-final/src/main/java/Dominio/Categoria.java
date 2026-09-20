package Dominio;

/**
 * Representa una categoría para clasificar los lugares en la red social.
 */
public class Categoria {
    private Long id;
    private String nombre;
    private String descripcion;

    /**
     * Constructor por defecto.
     */
    public Categoria() {
    }

    /**
     * Constructor con todos los atributos de la categoría.
     *
     * @param id Identificador único.
     * @param nombre Nombre de la categoría.
     * @param descripcion Descripción de la categoría.
     */
    public Categoria(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
