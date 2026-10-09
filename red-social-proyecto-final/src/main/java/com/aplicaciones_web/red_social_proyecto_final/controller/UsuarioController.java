package com.aplicaciones_web.red_social_proyecto_final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UsuarioController {

    @GetMapping("/login")
    public String login() {
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

    @GetMapping("/catalogo")
    public String catalogo() {
        return "usuario/catalogo";
    }

    @GetMapping("/perfil")
    public String miperfil() {
        return "usuario/perfil";
    }

    @GetMapping("/perfil/{username}")
    public String perfilPublico(@PathVariable("username") String username, Model model) {
        // Al conectar la base de datos se buscará el perfil por 'username'
        return "usuario/perfil-publico";
    }

    @GetMapping("/perfil/configuracion")
    public String mostrarConfiguracion(Model model) {
        return "usuario/configuracion";
    }

}
