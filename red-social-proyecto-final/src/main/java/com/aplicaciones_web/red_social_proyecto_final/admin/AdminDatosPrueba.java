package com.aplicaciones_web.red_social_proyecto_final.admin;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.aplicaciones_web.red_social_proyecto_final.dto.*;

/** Datos de muestra. No consulta ni modifica ninguna base de datos. */
@Service
public class AdminDatosPrueba implements AdminDatos {
    private static final String IMAGEN_RESENA = "/uploads/grand_teton.jpg";
    private static final String TEXTO_RESENA = "Una buena experiencia; el ambiente y la atención hicieron agradable la visita.";

    // Datos de presentación para los módulos que todavía no tienen operaciones.
    private final List<CategoriaVista> categorias = List.of(
        new CategoriaVista(1, "Restaurantes", "Espacios gastronómicos para comer y cenar.", 428),
        new CategoriaVista(2, "Cafes", "Cafeterías, coffee shops y espacios de especialidad.", 316),
        new CategoriaVista(3, "Parques", "Áreas verdes y espacios recreativos al aire libre.", 184),
        new CategoriaVista(4, "Museos", "Museos, galerías y centros culturales.", 147),
        new CategoriaVista(5, "Bares", "Bares, coctelerías y espacios de ocio nocturno.", 209));
    private final List<UsuarioVista> autores = List.of(
        new UsuarioVista(1, "Regina Jiménez", "regiiniita", "reginameneses@gmail.com", 48, "Activo", false),
        new UsuarioVista(2, "Mariana Orduño", "mariana1", "marianaord@gmail.com", 41, "Activo", false),
        new UsuarioVista(3, "Ramses", "rams3s", "ramses30@gmail.com", 29, "Activo", false),
        new UsuarioVista(4, "Jesús", "jesus101", "jesusm101@gmail.com", 22, "Activo", false));
    private final List<LugarVista> lugares = List.of(
        new LugarVista(1284, "Nomada Coffee Lab", categorias.get(1), "Malasaña - Madrid", 128, 4.9, "Descripción de prueba del lugar."),
        new LugarVista(1283, "Casa Oliva", categorias.get(0), "Chamberí - Madrid", 246, 4.8, "Descripción de prueba del lugar."),
        new LugarVista(1282, "Lumen", categorias.get(4), "Las Letras - Madrid", 94, 4.7, "Descripción de prueba del lugar."),
        new LugarVista(1281, "Bruma", categorias.get(0), "Chueca - Madrid", 173, 4.6, "Descripción de prueba del lugar."),
        new LugarVista(1280, "Café Central", categorias.get(1), "Lavapiés - Madrid", 81, 4.8, "Descripción de prueba del lugar."),
        new LugarVista(1279, "Mesa Norte", categorias.get(0), "Salesas - Madrid", 119, 4.5, "Descripción de prueba del lugar."));
    private final List<ResenaVista> resenas = List.of(
        new ResenaVista(105, LocalDate.of(2025, 6, 18), autores.get(0), lugares.get(0), 4, "Publicado", TEXTO_RESENA, IMAGEN_RESENA),
        new ResenaVista(106, LocalDate.of(2025, 6, 17), autores.get(1), lugares.get(1), 4, "Publicado", TEXTO_RESENA, IMAGEN_RESENA),
        new ResenaVista(107, LocalDate.of(2025, 6, 16), autores.get(2), lugares.get(2), 4, "Oculto", TEXTO_RESENA, IMAGEN_RESENA),
        new ResenaVista(108, LocalDate.of(2025, 6, 15), autores.get(3), lugares.get(3), 4, "Publicado", TEXTO_RESENA, IMAGEN_RESENA));
    // Cada reporte incluye la reseña que se presenta en su ventana de detalle.
    private final List<ReporteVista> reportes = List.of(
        crearReporte(1, 26, "@carlos.r", 1, "Museo Sonora", 3, "Contenido ofensivo", "Pendiente"),
        crearReporte(2, 25, "@ana_lopez", 2, "AOME", 0, "Spam o publicidad", "Pendiente"),
        crearReporte(3, 24, "@luis.m", 2, "Casa Olivia", 0, "Contenido ofensivo", "Pendiente"),
        crearReporte(4, 22, "@sofi.g", 1, "Parque Madero", 2, "No corresponde al lugar", "Revisado"),
        crearReporte(5, 20, "@regina.jm", 3, "Cerro de la Campana", 2, "Otro", "Revisado"));
    /** Reúne los valores comunes de los reportes de muestra. */
    private ReporteVista crearReporte(long id, int dia, String reportante, int autor,
                                     String nombreLugar, int categoria, String motivo, String estado) {
        var fecha = LocalDate.of(2026, 10, dia);
        var lugar = new LugarVista(299 + id, nombreLugar, categorias.get(categoria),
                "Hermosillo, Sonora", 1, 2.0, "Lugar de prueba del reporte.");
        var resena = new ResenaVista(199 + id, fecha.minusDays(1), autores.get(autor), lugar,
                2, "Publicado", "Texto de prueba de la reseña reportada.", IMAGEN_RESENA);
        return new ReporteVista(id, fecha, reportante, resena, motivo, estado,
                "Comentario de prueba del reportante.");
    }

    @Override
    public ResumenVista resumen() {
        return new ResumenVista(1284, 8642, 24890, 4.6);
    }

    @Override
    public List<CategoriaVista> categorias() {
        return categorias;
    }

    @Override
    public List<LugarVista> lugares() {
        return lugares;
    }

    @Override
    public List<ResenaVista> resenas() {
        return resenas;
    }

    @Override
    public List<ReporteVista> reportes() {
        return reportes;
    }

    @Override
    public List<LugarDestacadoVista> lugaresMasResenados() {
        return List.of(new LugarDestacadoVista("Nomada Coffee Lab", 85, 218), new LugarDestacadoVista("Casa Olivia", 65, 168), new LugarDestacadoVista("Lumen", 55, 165), new LugarDestacadoVista("Bruna", 40, 120), new LugarDestacadoVista("Savia Cafe", 28, 100));
    }

    @Override
    public List<UsuarioActivoVista> usuariosMasActivos() {
        return List.of(new UsuarioActivoVista("Regina Jiménez", "@regiiniita", 48), new UsuarioActivoVista("Mariana Orduño", "@mariana1", 41), new UsuarioActivoVista("Ramses", "@rams3s", 34));
    }

}
