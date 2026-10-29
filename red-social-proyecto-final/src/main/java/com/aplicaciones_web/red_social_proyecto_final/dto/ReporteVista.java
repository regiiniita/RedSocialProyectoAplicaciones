package com.aplicaciones_web.red_social_proyecto_final.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

/** Información necesaria para revisar una reseña reportada. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteVista {
    private long id;
    private LocalDate fecha;
    private String reportante;
    private ResenaVista resena;
    private String motivo;
    private String estado;
    private String comentario;

    public boolean isRevisado() {
        return "Revisado".equals(estado);
    }

    public boolean isPendiente() {
        return "Pendiente".equals(estado);
    }
}
