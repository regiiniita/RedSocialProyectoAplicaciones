package com.aplicaciones_web.red_social_proyecto_final.controller.api;

import com.aplicaciones_web.red_social_proyecto_final.dto.LugarVista;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.FiltroCatalogo;
import com.aplicaciones_web.red_social_proyecto_final.services.LugarService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lugares")
public class LugarRestController {

    @Autowired
    private LugarService lugarService;

    /**
     * El Catálogo consulta GET /api/lugares para listar, buscar, filtrar por categoría y ordenar.
     * Se usa GET porque solo se consultan datos. La respuesta JSON incluye la ruta de la foto.
     */
    @GetMapping(params = "!vista")
    public List<LugarVista> listar(@Valid @ModelAttribute FiltroCatalogo filtro) {
        return lugarService.consultarCatalogo(
                filtro.getQuery(), filtro.getCategoria(), filtro.getOrden());
    }
}