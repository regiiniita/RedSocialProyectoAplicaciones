package com.aplicaciones_web.red_social_proyecto_final.config;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webmvc.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * Da el mismo formato JSON que ApiExceptionHandler a los errores que Spring resuelve antes de llegar
 * a un controlador: método no permitido (405), Content-Type no soportado (415) o ruta inexistente (404).
 * Solo cambia las rutas /api; las páginas del sitio conservan el formato normal de Spring.
 */
@Component
public class ApiErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest request, ErrorAttributeOptions options) {
        Map<String, Object> original = super.getErrorAttributes(request, options);
        Object ruta = original.get("path");
        if (ruta == null || !ruta.toString().startsWith("/api/")) {
            return original;
        }
        int status = (int) original.get("status");
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", original.get("timestamp"));
        cuerpo.put("status", status);
        cuerpo.put("error", original.get("error"));
        cuerpo.put("mensaje", mensaje(status));
        cuerpo.put("ruta", ruta);
        cuerpo.put("campos", null);
        return cuerpo;
    }

    private static String mensaje(int status) {
        return switch (status) {
            case 404 -> "La ruta solicitada no existe";
            case 405 -> "El método HTTP no está permitido en esta ruta";
            case 415 -> "El cuerpo debe enviarse como application/json";
            case 401 -> "Se requiere un token JWT válido";
            case 403 -> "No tienes permiso para realizar esta acción";
            default -> "Ocurrió un error al procesar la petición";
        };
    }
}