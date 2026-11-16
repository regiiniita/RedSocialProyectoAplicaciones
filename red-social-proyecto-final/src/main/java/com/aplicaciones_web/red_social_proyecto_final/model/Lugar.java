package com.aplicaciones_web.red_social_proyecto_final.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** Lugar almacenado en la tabla lugares. */
@Entity
@Table(name = "lugares")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(length = 500)
    private String descripcion;

    // Ruta pública de la imagen, por ejemplo /uploads/lugares/<uuid>.png
    @Column(length = 255)
    private String imagen;
}
