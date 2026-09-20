package Dominio;

/**
 * Representa un establecimiento o sitio de interés dentro del catálogo del sistema.
 */
public class Lugar {

    private Long id;
    private String nombre;
    private String direccion;
    private String descripcion;
    private String imagen;
    private Float promedioCalificacion;
    private Categoria categoria;

    /**
     * Constructor por defecto.
     */
    public Lugar() {
    }

    /**
     * Constructor con todos los atributos del lugar.
     *
     * @param id Identificador único.
     * @param nombre Nombre del lugar.
     * @param direccion Dirección física.
     * @param descripcion Descripción informativa.
     * @param imagen URL o ruta de la imagen.
     * @param promedioCalificacion Calificación promedio inicial.
     * @param categoria Categoría asociada.
     */
    public Lugar(Long id, String nombre, String direccion, String descripcion, String imagen, Float promedioCalificacion, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.promedioCalificacion = promedioCalificacion;
        this.categoria = categoria;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Float getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public void setPromedioCalificacion(Float promedioCalificacion) {
        this.promedioCalificacion = promedioCalificacion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
