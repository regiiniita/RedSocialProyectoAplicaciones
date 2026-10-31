package com.aplicaciones_web.red_social_proyecto_final.services;

import com.aplicaciones_web.red_social_proyecto_final.admin.AdminDatos;
import com.aplicaciones_web.red_social_proyecto_final.dto.ResumenVista;
import com.aplicaciones_web.red_social_proyecto_final.dto.UsuarioActivoVista;
import com.aplicaciones_web.red_social_proyecto_final.repository.ResenaRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ResenaRepository resenas;
    private final UsuarioRepository usuarios;
    // Temporal: el total de lugares sale de los datos de muestra hasta que exista la tabla de lugares.
    private final AdminDatos datos;

    @Transactional(readOnly = true)
    public ResumenVista resumen() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        Double promedio = resenas.promedioCalificacion();

        return new ResumenVista(
                datos.resumen().getTotalLugares(),
                (int) resenas.countByFechaBetween(inicioMes, hoy),
                (int) usuarios.countByActivoTrue(),
                // Redondea a un decimal; 0 cuando todavía no hay reseñas.
                promedio == null ? 0 : Math.round(promedio * 10) / 10.0);
    }

    @Transactional(readOnly = true)
    public List<UsuarioActivoVista> usuariosMasActivos() {
        return usuarios.findTop5ByActivoTrueOrderByTotalResenasDesc().stream()
                .map(u -> new UsuarioActivoVista(u.getNombre(), "@" + u.getUsername(), u.getTotalResenas()))
                .toList();
    }
}