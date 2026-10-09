package com.aplicaciones_web.red_social_proyecto_final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TheSpotController {


    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }
    @GetMapping("/admin/lugares")
    public String lugares() {
        return "admin/lugares";
    }
    @GetMapping("/admin/categorias")
    public String categorias() {
        return "admin/categorias";
    }
    @GetMapping("/admin/resenas")
    public String resenas() {
        return "admin/resenas";
    }
    @GetMapping("/admin/usuarios")
    public String usuarios() {
        return "admin/usuarios";
    }
}
