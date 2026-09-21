package Dominio;

/**
 * Enumeración que define las categorías disponibles para clasificar los lugares en la plataforma.
 */
public enum Categoria {

    RESTAURANTE("Restaurante", "Establecimiento enfocado en servicio de alimentos y bebidas preparadas."),
    CAFETERIA("Cafetería", "Espacio especializado en café, postres y alimentos ligeros."),
    BAR("Bar", "Establecimiento social centrado en bebidas y vida nocturna."),
    PARQUE("Parque", "Área verde pública o recreativa al aire libre."),
    MUSEO("Museo", "Espacio cultural, histórico o artístico abierto al público.");

    private final String nombre;
    private final String descripcion;

    /**
     * Constructor del enum Categoria.
     *
     * @param nombre Nombre descriptivo de la categoría.
     * @param descripcion Detalle de la clasificación.
     */
    Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}