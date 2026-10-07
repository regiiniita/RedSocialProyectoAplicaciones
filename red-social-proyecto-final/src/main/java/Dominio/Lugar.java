package Dominio;

import Dominio.Excepciones.ReglaNegocioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lugar {

    private Long id;
    private String nombre;
    private Direccion direccion;
    private String descripcion;
    private Categoria categoria;
    private String imagen;
    private String horario;
    private String telefono;
    private RangoPrecios rangoPrecios;
    private final List<Resenia> resenias = new ArrayList<>();

    public Lugar() {
    }

    public Lugar(Long id, String nombre, Direccion direccion, String descripcion, Categoria categoria,
                 String imagen, String horario, String telefono, RangoPrecios rangoPrecios) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.imagen = imagen;
        this.horario = horario;
        this.telefono = telefono;
        this.rangoPrecios = rangoPrecios;
    }

    public void agregarResenia(Resenia resenia) {
        if (resenia == null || resenia.getLugar() != this) {
            throw new ReglaNegocioException("La reseña no pertenece a este lugar.");
        }
        for (Resenia existente : resenias) {
            if (existente.getAutor().esMismoUsuario(resenia.getAutor())) {
                throw new ReglaNegocioException("Ya dejaste una reseña en este lugar; edítala en lugar de crear otra.");
            }
        }
        resenias.add(resenia);
    }

    public double getPromedioCalificacion() {
        return resenias.stream()
                .filter(r -> r.getEstado() != EstadoResenia.OCULTA)
                .mapToInt(Resenia::getCalificacion)
                .average()
                .orElse(0.0);
    }

    public List<Resenia> getResenias() { return Collections.unmodifiableList(resenias); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public RangoPrecios getRangoPrecios() { return rangoPrecios; }
    public void setRangoPrecios(RangoPrecios rangoPrecios) { this.rangoPrecios = rangoPrecios; }
}