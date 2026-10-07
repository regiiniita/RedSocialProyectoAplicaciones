package Dominio;

import Dominio.Excepciones.ReglaNegocioException;

import java.time.LocalDateTime;

public class Resenia {

    public static final int CALIFICACION_MINIMA = 1;
    public static final int CALIFICACION_MAXIMA = 5;

    private Long id;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime fechaPublicacion;
    private EstadoResenia estado;
    private Usuario autor;
    private Lugar lugar;

    public Resenia() {
        this.fechaPublicacion = LocalDateTime.now();
        this.estado = EstadoResenia.PUBLICADA;
    }

    public Resenia(Long id, Integer calificacion, String comentario, LocalDateTime fechaPublicacion,
                   Usuario autor, Lugar lugar) {
        if (autor == null) {
            throw new ReglaNegocioException("La reseña debe estar asociada a un autor válido.");
        }
        if (lugar == null) {
            throw new ReglaNegocioException("La reseña debe estar asociada a un lugar válido.");
        }
        this.id = id;
        setCalificacion(calificacion);
        this.comentario = comentario;
        this.fechaPublicacion = (fechaPublicacion != null) ? fechaPublicacion : LocalDateTime.now();
        this.estado = EstadoResenia.PUBLICADA;
        this.autor = autor;
        this.lugar = lugar;
    }

    // Regla: la calificación es un entero de 1 a 5
    public void setCalificacion(Integer calificacion) {
        if (calificacion == null || calificacion < CALIFICACION_MINIMA || calificacion > CALIFICACION_MAXIMA) {
            throw new ReglaNegocioException("La calificación debe ser un entero entre "
                    + CALIFICACION_MINIMA + " y " + CALIFICACION_MAXIMA + ".");
        }
        this.calificacion = calificacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getCalificacion() { return calificacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    public EstadoResenia getEstado() { return estado; }
    public void setEstado(EstadoResenia estado) { this.estado = estado; }
    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }
    public Lugar getLugar() { return lugar; }
    public void setLugar(Lugar lugar) { this.lugar = lugar; }
}