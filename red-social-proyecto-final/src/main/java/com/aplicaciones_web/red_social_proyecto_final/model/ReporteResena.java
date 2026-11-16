package com.aplicaciones_web.red_social_proyecto_final.model;

import Dominio.EstadoReporte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/** Reporte de una reseña almacenado en la tabla reportes. */
@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "reportante_id", nullable = false)
    private CuentaUsuario reportante;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "resena_id", nullable = false)
    private Resena resena;

    @Column(nullable = false, length = 100)
    private String motivo;

    @Column(length = 500)
    private String comentario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReporte estado;
}
