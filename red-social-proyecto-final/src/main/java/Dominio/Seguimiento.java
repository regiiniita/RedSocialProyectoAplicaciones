package Dominio;

import Dominio.Excepciones.ReglaNegocioException;

import java.time.LocalDateTime;

/**
 * Entidad que modela la relación de seguimiento entre dos usuarios de la plataforma.
 */
public class Seguimiento {

    private Long id;
    private Usuario seguidor;
    private Usuario seguido;
    private LocalDateTime fechaSeguimiento;

    /**
     * Constructor por defecto.
     */
    public Seguimiento() {
        this.fechaSeguimiento = LocalDateTime.now();
    }

    /**
     * Constructor con parámetros.
     *
     * @param id Identificador único del registro de seguimiento.
     * @param seguidor Usuario que realiza la acción de seguir.
     * @param seguido Usuario que es seguido.
     * @param fechaSeguimiento Fecha y hora en que se originó el seguimiento.
     */
    public Seguimiento(Long id, Usuario seguidor, Usuario seguido, LocalDateTime fechaSeguimiento) {
        this.id = id;
        validarRelacion(seguidor, seguido);
        this.seguidor = seguidor;
        this.seguido = seguido;
        this.fechaSeguimiento = (fechaSeguimiento != null) ? fechaSeguimiento : LocalDateTime.now();
    }

    private void validarRelacion(Usuario seguidor, Usuario seguido) {
        if (seguidor == null || seguido == null) {
            throw new ReglaNegocioException("Tanto el seguidor como el seguido deben ser usuarios válidos.");
        }
        if (seguidor.getId() != null && seguido.getId() != null && seguidor.getId().equals(seguido.getId())) {
            throw new ReglaNegocioException("Un usuario no puede seguirse a sí mismo.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getSeguidor() {
        return seguidor;
    }

    public void setSeguidor(Usuario seguidor) {
        validarRelacion(seguidor, this.seguido);
        this.seguidor = seguidor;
    }

    public Usuario getSeguido() {
        return seguido;
    }

    public void setSeguido(Usuario seguido) {
        validarRelacion(this.seguidor, seguido);
        this.seguido = seguido;
    }

    public LocalDateTime getFechaSeguimiento() {
        return fechaSeguimiento;
    }

    public void setFechaSeguimiento(LocalDateTime fechaSeguimiento) {
        this.fechaSeguimiento = fechaSeguimiento;
    }
}