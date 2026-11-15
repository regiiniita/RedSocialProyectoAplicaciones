package com.aplicaciones_web.red_social_proyecto_final.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.time.LocalDate;

/** Cuenta de usuario almacenada en la tabla usuarios. */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Se guarda únicamente el hash BCrypt, no la contraseña original.
    @ToString.Exclude
    @Column(name = "password_hash", nullable = false, length = 60)
    private String passwordHash;

    @Column(nullable = false, length = 30)
    private String rol;

    @Column(nullable = false)
    private boolean activo;

    @Column(name = "total_resenas", nullable = false)
    private int totalResenas;
    /** Mantiene en un solo lugar la identificación del rol administrativo. */
    public boolean esAdministrador() {
        return Dominio.Rol.ADMINISTRADOR.name().equals(rol);
    }

    @Column(length = 100)
    private String apellidos;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(length = 20)
    private String genero;
}
