package com.aplicaciones_web.red_social_proyecto_final.services;

import com.aplicaciones_web.red_social_proyecto_final.dto.ResumenVista;
import com.aplicaciones_web.red_social_proyecto_final.dto.UsuarioActivoVista;
import com.aplicaciones_web.red_social_proyecto_final.repository.ResenaRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Dominio.EstadoResenia;
import com.aplicaciones_web.red_social_proyecto_final.dto.LugarDestacadoVista;
import com.aplicaciones_web.red_social_proyecto_final.repository.LugarRepository;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ResenaRepository resenas;
    private final UsuarioRepository usuarios;
    private final LugarRepository lugares;

    @Transactional(readOnly = true)
    public ResumenVista resumen() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        Double promedio = resenas.promedioCalificacion();

        return new ResumenVista(
                (int) lugares.count(),
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

    @Transactional(readOnly = true)
    public List<LugarDestacadoVista> lugaresMasResenados() {
        var top = resenas.lugaresMasResenados(EstadoResenia.OCULTA, PageRequest.of(0, 5));
        long maximo = top.isEmpty() ? 1 : top.get(0).getTotal();
        return top.stream()
                .map(l -> new LugarDestacadoVista(l.getNombre(),
                        (int) (l.getTotal() * 100 / maximo), l.getTotal().intValue()))
                .toList();
    }
}