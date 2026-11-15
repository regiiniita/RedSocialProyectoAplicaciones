package com.aplicaciones_web.red_social_proyecto_final.dto.api;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Datos que envía el formulario "Configura tu cuenta" a PATCH /api/usuarios/me.
 * Todos son opcionales porque PATCH solo cambia los campos que se envían.
 */
@Data
public class ActualizarPerfilRequest {

    @Size(max = 100, message = "Los nombres no pueden pasar de 100 caracteres")
    @Pattern(regexp = ".*\\S.*", message = "Los nombres no pueden estar vacíos")
    private String nombres;

    @Size(max = 100, message = "Los apellidos no pueden pasar de 100 caracteres")
    @Pattern(regexp = ".*\\S.*", message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    @Pattern(regexp = "Femenino|Masculino|Otro", message = "El género debe ser Femenino, Masculino u Otro")
    private String genero;
}