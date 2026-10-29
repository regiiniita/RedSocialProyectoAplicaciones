package com.aplicaciones_web.red_social_proyecto_final.admin;

import java.util.List;
import com.aplicaciones_web.red_social_proyecto_final.dto.*;

/** Agrupa las consultas de prueba que necesitan las pantallas del panel. */
public interface AdminDatos {
    // Las vistas reciben estos datos mediante el controlador.
    ResumenVista resumen();
    List<CategoriaVista> categorias();
    List<LugarVista> lugares();
    List<ResenaVista> resenas();
    List<ReporteVista> reportes();
    List<LugarDestacadoVista> lugaresMasResenados();
    List<UsuarioActivoVista> usuariosMasActivos();
}
