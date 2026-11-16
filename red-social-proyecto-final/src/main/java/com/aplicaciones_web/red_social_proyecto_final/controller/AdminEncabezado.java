package com.aplicaciones_web.red_social_proyecto_final.controller;

import java.security.Principal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Comparte los datos de la sesión con el encabezado de las vistas administrativas.
 */
@ControllerAdvice(assignableTypes = AdminController.class)
public class AdminEncabezado {
    /**
     * Agrega al modelo el nombre de la cuenta autenticada.
     */
    @ModelAttribute
    public void encabezado(Principal principal, Model model) {
        model.addAttribute("adminNombre", principal == null ? "" : principal.getName());
        model.addAttribute("adminRol", "Administrador");
    }
}
