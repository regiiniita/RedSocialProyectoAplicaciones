package com.aplicaciones_web.red_social_proyecto_final.controller;

import com.aplicaciones_web.red_social_proyecto_final.admin.AdminDatos;
import com.aplicaciones_web.red_social_proyecto_final.services.ReporteConsultaService;
import com.aplicaciones_web.red_social_proyecto_final.services.UsuarioService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Reúne las rutas del panel y delega las operaciones a los servicios. */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminDatos datos;
    private final UsuarioService usuarioService;
    private final ReporteConsultaService reporteService;

    /** Prepara los datos de la pantalla de dashboard. */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("resumen", datos.resumen());
        model.addAttribute("lugaresMasResenados", datos.lugaresMasResenados());
        model.addAttribute("usuariosMasActivos", datos.usuariosMasActivos());
        return "admin/dashboard";
    }

    /** Prepara los datos de la pantalla de lugares. */
    @GetMapping("/lugares")
    public String lugares(Model model) {
        model.addAttribute("lugares", datos.lugares());
        model.addAttribute("categorias", datos.categorias());
        return "admin/lugares";
    }

    /** Prepara los datos de la pantalla de categorias. */
    @GetMapping("/categorias")
    public String categorias(Model model) {
        model.addAttribute("categorias", datos.categorias());
        return "admin/categorias";
    }

    /** Prepara los datos de la pantalla de resenas. */
    @GetMapping("/resenas")
    public String resenas(Model model) {
        model.addAttribute("resenas", datos.resenas());
        return "admin/resenas";
    }

    /** Prepara los datos de la pantalla de reportes. */
    @GetMapping("/reportes")
    public String reportes(Model model) {
        var listado = reporteService.consultar();
        model.addAttribute("reportes", listado.getReportes());
        model.addAttribute("pendientes", listado.getPendientes());
        model.addAttribute("motivos", listado.getMotivos());
        return "admin/reportes";
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
}
