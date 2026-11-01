package com.aplicaciones_web.red_social_proyecto_final.model;

import Dominio.EstadoResenia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/** Reseña almacenada en la tabla resenas. */
@Entity
@Table(name = "resenas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private CuentaUsuario autor;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "lugar_id", nullable = false)
    private Lugar lugar;

    @Column(nullable = false)
    private int calificacion;

    @Column(nullable = false, length = 1000)
    private String texto;

    @Column(length = 255)
    private String imagen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoResenia estado;
}