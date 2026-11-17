package com.aplicaciones_web.red_social_proyecto_final.controller.api;

import com.aplicaciones_web.red_social_proyecto_final.dto.api.CrearResenaRequest;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.FiltroResenas;
import com.aplicaciones_web.red_social_proyecto_final.dto.api.ResenaResponse;
import com.aplicaciones_web.red_social_proyecto_final.services.ResenaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resenas")
public class ResenaRestController {

    @Autowired
    private ResenaService resenaService;

    /**
     * El feed Tu comunidad consulta GET /api/resenas?seguidoPor={username} para mostrar las reseñas
     * de los usuarios que sigue la persona. La pestaña Reseñas de Mi Perfil y de Perfil público
     * consulta GET /api/resenas?autor={username}. Se usa GET porque solo se consultan datos.
     * Es pública: no necesita token. No incluye las reseñas ocultas por moderación.
     */
    @GetMapping
    public List<ResenaResponse> listar(@Valid @ModelAttribute FiltroResenas filtro) {
        return resenaService.consultarPublicas(filtro.getAutor(), filtro.getSeguidoPor());
    }

    /**
     * El botón Publicar de Compartir reseña envía el lugar, la calificación de 1 a 5 y el comentario
     * a POST /api/resenas. Se usa POST porque se crea una reseña. El autor es el usuario del token.
     * Requiere JWT. Responde 201 con la reseña creada.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResenaResponse> publicar(Authentication autenticacion,
                                                   @Valid @RequestBody CrearResenaRequest solicitud) {
        ResenaResponse creada = resenaService.publicar(autenticacion.getName(), solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }
}