package com.aplicaciones_web.red_social_proyecto_final.controller;

import Dominio.Excepciones.CredencialesInvalidasException;
import Dominio.Excepciones.RecursoDuplicadoException;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.FiltroCatalogo;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.RegistroRequest;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.SesionRequest;
import com.aplicaciones_web.red_social_proyecto_final.services.CategoriaService;
import com.aplicaciones_web.red_social_proyecto_final.services.LugarService;
import com.aplicaciones_web.red_social_proyecto_final.services.SesionService;
import com.aplicaciones_web.red_social_proyecto_final.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/** Atiende las pantallas y los formularios del sitio para clientes. */
@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private LugarService lugarService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private SesionService sesionService;

    /** Indica a la plantilla compartida que debe mostrar el acceso del cliente. */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginCliente", true);
        return "usuario/login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "usuario/registro";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "usuario/index";
    }

    /** Abre el catálogo con la lista inicial de lugares. */
    @GetMapping("/catalogo")
    public String catalogo() {
        return "redirect:/api/lugares?vista=catalogo";
    }

    /** Carga los resultados del catálogo y las opciones del formulario. */
    @GetMapping(value = "/api/lugares", params = "vista=catalogo")
    public String catalogoConResultados(
            @Valid @ModelAttribute("filtro") FiltroCatalogo filtro,
            BindingResult resultado,
            Model model) {

        model.addAttribute("categorias", categoriaService.consultar(""));

        if (resultado.hasErrors()) {
            model.addAttribute("errorCatalogo", "Revisa los filtros del catálogo.");
            return "usuario/catalogo";
        }

        model.addAttribute("lugares", lugarService.consultarCatalogo(
                filtro.getQuery(), filtro.getCategoria(), filtro.getOrden()));
        return "usuario/catalogo";
    }

    @GetMapping("/perfil")
    public String miperfil() {
        return "usuario/perfil";
    }

    @GetMapping("/perfil/{username}")
    public String perfilPublico(@PathVariable("username") String username, Model model) {
        // Al conectar la base de datos se buscará el perfil por 'username'.
        return "usuario/perfil-publico";
    }

    @GetMapping("/perfil/configuracion")
    public String mostrarConfiguracion(Model model) {
        return "usuario/configuracion";
    }

    /** Procesa el registro desde el navegador y vuelve al formulario si hay errores. */
    @PostMapping(
            value = "/api/usuarios",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public String registrarFormulario(
            @Valid @ModelAttribute RegistroRequest solicitud,
            BindingResult resultado,
            Model model) {

        if (resultado.hasErrors()) {
            model.addAttribute("errorRegistro", "Revisa los datos del registro.");
            return "usuario/registro";
        }

        try {
            usuarioService.registrar(solicitud);
            return "redirect:/login";
        } catch (RecursoDuplicadoException ex) {
            model.addAttribute("errorRegistro", ex.getMessage());
            return "usuario/registro";
        }
    }

    /** Procesa el acceso del cliente y lo lleva a Inicio cuando las credenciales son válidas. */
    @PostMapping(
            value = "/api/sesiones",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public String iniciarSesionFormulario(
            @Valid @ModelAttribute SesionRequest solicitud,
            BindingResult resultado,
            Model model,
            HttpServletRequest peticion) {

        model.addAttribute("loginCliente", true);

        if (resultado.hasErrors()) {
            model.addAttribute("errorLogin", "Escribe tu usuario y contraseña.");
            return "usuario/login";
        }

        try {
            var respuesta = sesionService.iniciar(solicitud);
            var sesion = peticion.getSession();
            // Renueva el identificador al iniciar sesión y guarda el token del cliente.
            peticion.changeSessionId();
            sesion.setAttribute("jwtCliente", respuesta.getToken());

            return "redirect:/inicio";
        } catch (CredencialesInvalidasException ex) {
            model.addAttribute("errorLogin", "Usuario o contraseña incorrectos.");
            return "usuario/login";
        }
    }
}