package Dominio;

import Dominio.Excepciones.ReglaNegocioException;

import java.time.LocalDateTime;

/**
 * Representa una reseña y calificación otorgada por un usuario a un lugar específico.
 */
public class Resenia {

    public static final int CALIFICACION_MINIMA = 1;
    public static final int CALIFICACION_MAXIMA = 5;

    private Long id;
    private Float calificacion;
    private String comentario;
    private LocalDateTime fechaPublicacion;
    private Usuario autor;
    private Lugar lugar;
    private Boolean estado;

    /**
     * Constructor por defecto.
     */
    public Resenia() {
        this.fechaPublicacion = LocalDateTime.now();
    }

    /**
     * Constructor con todos los atributos de la reseña.
     *
     * @param id Identificador único.
     * @param calificacion Puntuación otorgada (1 a 5).
     * @param comentario Texto descriptivo de la experiencia.
     * @param fechaPublicacion Fecha y hora de emisión.
     * @param autor Usuario que redacta la reseña.
     * @param lugar Lugar al que se le realiza la reseña.
     */
    public Resenia(Long id, Float calificacion, String comentario, LocalDateTime fechaPublicacion, Usuario autor, Lugar lugar) {
        this.id = id;
        validarYAsignarCalificacion(calificacion);
        validarObjetosAsociados(autor, lugar);
        this.comentario = comentario;
        this.fechaPublicacion = (fechaPublicacion != null) ? fechaPublicacion : LocalDateTime.now();
        this.autor = autor;
        this.lugar = lugar;
        this.estado = false;
    }

    private void validarYAsignarCalificacion(Float calificacion) {
        if (calificacion == null) {
            throw new ReglaNegocioException("La calificación no puede ser nula.");
        }
        if (calificacion < CALIFICACION_MINIMA || calificacion > CALIFICACION_MAXIMA) {
            throw new ReglaNegocioException("La calificación debe estar entre " + CALIFICACION_MINIMA + " y " + CALIFICACION_MAXIMA + ".");
        }
        this.calificacion = calificacion;
    }

    private void validarObjetosAsociados(Usuario autor, Lugar lugar) {
        if (autor == null) {
            throw new ReglaNegocioException("La reseña debe estar asociada a un autor válido.");
        }
        if (lugar == null) {
            throw new ReglaNegocioException("La reseña debe estar asociada a un lugar válido.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Float calificacion) {
        validarYAsignarCalificacion(calificacion);
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        if (autor == null) {
            throw new ReglaNegocioException("El autor de la reseña no puede ser nulo.");
        }
        this.autor = autor;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setLugar(Lugar lugar) {
        if (lugar == null) {
            throw new ReglaNegocioException("El lugar de la reseña no puede ser nulo.");
        }
        this.lugar = lugar;
    }
}