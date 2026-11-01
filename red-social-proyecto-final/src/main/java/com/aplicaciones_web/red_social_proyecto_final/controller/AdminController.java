package com.aplicaciones_web.red_social_proyecto_final.controller;

import com.aplicaciones_web.red_social_proyecto_final.admin.AdminDatos;
import com.aplicaciones_web.red_social_proyecto_final.services.*;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Reúne las rutas del panel y delega las operaciones a los servicios. */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminDatos datos;
    private final UsuarioService usuarioService;
    private final ReporteConsultaService reporteService;
    private final ResenaService resenaService;
    private final CategoriaService categoriaService;
    private final DashboardService dashboardService;
    private final LugarService lugarService;

    /** Envía las reseñas y el texto de búsqueda a la plantilla de moderación. */
    @GetMapping("/resenas")
    public String resenas(@RequestParam(defaultValue = "") String buscar, Model model) {
        model.addAttribute("resenas", resenaService.consultar(buscar));
        model.addAttribute("buscar", buscar);
        return "admin/resenas";
    }

    /** Oculta la reseña y redirige al listado para evitar repetir el envío. */
    @PostMapping("/resenas/ocultar")
    public String ocultarResena(@RequestParam long idResena, RedirectAttributes redirect) {
        try {
            resenaService.ocultar(idResena);
            redirect.addFlashAttribute("mensaje", "Reseña ocultada correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/resenas";
    }

    /** Prepara los datos de la pantalla de reportes junto con los filtros aplicados. */
    @GetMapping("/reportes")
    public String reportes(@RequestParam(defaultValue = "") String estado,
                           @RequestParam(defaultValue = "") String motivo,
                           @RequestParam(defaultValue = "") String buscar, Model model) {
        var listado = reporteService.consultar(estado, motivo, buscar);
        model.addAttribute("reportes", listado.getReportes());
        model.addAttribute("pendientes", listado.getPendientes());
        model.addAttribute("motivos", listado.getMotivos());
        model.addAttribute("estado", estado);
        model.addAttribute("motivo", motivo);
        model.addAttribute("buscar", buscar);
        return "admin/reportes";
    }

    /** Revisa el reporte ocultando la reseña reportada. */
    @PostMapping("/reportes/ocultar-resena")
    public String ocultarResenaReportada(@RequestParam long idReporte, RedirectAttributes redirect) {
        try {
            reporteService.ocultarResena(idReporte);
            redirect.addFlashAttribute("mensaje", "Reseña ocultada y reporte revisado.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/reportes";
    }

    /** Descarta el reporte y deja la reseña visible. */
    @PostMapping("/reportes/descartar")
    public String descartarReporte(@RequestParam long idReporte, RedirectAttributes redirect) {
        try {
            reporteService.descartar(idReporte);
            redirect.addFlashAttribute("mensaje", "Reporte descartado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/reportes";
    }

    /**
     * Reutiliza el login de usuario para el acceso administrativo.
     */
    @GetMapping("/login")
    public String login() { return "usuario/login"; }

    /**
     * Establece la pantalla inicial del panel.
     */
    @GetMapping({"", "/"})
    public String panel() { return "redirect:/admin/usuarios"; }

    /**
     * Envía los resultados y el texto de búsqueda a la plantilla de usuarios.
     */
    @GetMapping("/usuarios")
    public String usuarios(@RequestParam(defaultValue = "") String buscar, Model model) {
        model.addAttribute("usuarios", usuarioService.consultar(buscar));
        model.addAttribute("buscar", buscar);
        return "admin/usuarios";
    }

    /**
     * Procesa la desactivación y redirige al listado para evitar repetir el envío.
     */
    @PostMapping("/usuarios/desactivar")
    public String desactivar(@RequestParam long idUsuario, Principal principal, RedirectAttributes redirect) {
        try {
            usuarioService.desactivar(idUsuario, principal.getName());
            // El mensaje se conserva únicamente para la siguiente solicitud.
            redirect.addFlashAttribute("mensaje", "Usuario desactivado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    // categorias

    @GetMapping("/categorias")
    public String categorias(@RequestParam(defaultValue = "") String buscar, Model model) {
        model.addAttribute("categorias", categoriaService.consultar(buscar));
        model.addAttribute("buscar", buscar);
        return "admin/categorias";
    }

    @PostMapping("/categorias/guardar")
    public String guardarCategoria(@RequestParam String nombreCategoria,
                                   @RequestParam String descripcionCategoria, RedirectAttributes redirect) {
        try {
            categoriaService.guardar(nombreCategoria, descripcionCategoria);
            redirect.addFlashAttribute("mensaje", "Categoría creada correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/actualizar")
    public String actualizarCategoria(@RequestParam long idCategoria, @RequestParam String nombreCategoria,
                                      @RequestParam String descripcionCategoria, RedirectAttributes redirect) {
        try {
            categoriaService.actualizar(idCategoria, nombreCategoria, descripcionCategoria);
            redirect.addFlashAttribute("mensaje", "Categoría actualizada correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/categorias";
    }

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("resumen", dashboardService.resumen());
        model.addAttribute("lugaresMasResenados", dashboardService.lugaresMasResenados());
        model.addAttribute("usuariosMasActivos", dashboardService.usuariosMasActivos());
        return "admin/dashboard";
    }

    // Lugares

    @GetMapping("/lugares")
    public String lugares(@RequestParam(defaultValue = "") String buscar, Model model) {
        model.addAttribute("lugares", lugarService.consultar(buscar));
        model.addAttribute("categorias", categoriaService.consultar(""));
        model.addAttribute("buscar", buscar);
        return "admin/lugares";
    }

    @PostMapping("/lugares/guardar")
    public String guardarLugar(@RequestParam String nombreLugar,
                               @RequestParam(required = false) Long categoriaLugar,
                               @RequestParam String direccionCompleta,
                               @RequestParam(defaultValue = "") String descripcionLugar,
                               @RequestParam(required = false) MultipartFile imagenLugar,
                               RedirectAttributes redirect) {
        try {
            lugarService.guardar(nombreLugar, categoriaLugar, direccionCompleta, descripcionLugar, imagenLugar);
            redirect.addFlashAttribute("mensaje", "Lugar creado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/lugares";
    }

    @PostMapping("/lugares/actualizar")
    public String actualizarLugar(@RequestParam long idLugar,
                                  @RequestParam String nombreLugar,
                                  @RequestParam(required = false) Long categoriaLugar,
                                  @RequestParam String direccionCompleta,
                                  @RequestParam(defaultValue = "") String descripcionLugar,
                                  RedirectAttributes redirect) {
        try {
            lugarService.actualizar(idLugar, nombreLugar, categoriaLugar, direccionCompleta, descripcionLugar);
            redirect.addFlashAttribute("mensaje", "Lugar actualizado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/lugares";
    }

    @PostMapping("/lugares/eliminar")
    public String eliminarLugar(@RequestParam long idLugar, RedirectAttributes redirect) {
        try {
            lugarService.eliminar(idLugar);
            redirect.addFlashAttribute("mensaje", "Lugar eliminado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/lugares";
    }
}
