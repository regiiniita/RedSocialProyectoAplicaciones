package com.aplicaciones_web.red_social_proyecto_final.controller.api;

import Dominio.Excepciones.CredencialesInvalidasException;
import Dominio.Excepciones.RecursoDuplicadoException;
import Dominio.Excepciones.RecursoNoEncontradoException;
import Dominio.Excepciones.ReglaNegocioException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Manejo centralizado de errores de la API. El cliente siempre recibe un JSON con
 * timestamp, status, error, mensaje, ruta y, en errores de validación, los campos que fallaron.
 * Nunca se devuelve el stacktrace.
 */
@RestControllerAdvice(basePackages = "com.aplicaciones_web.red_social_proyecto_final.controller.api")
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /** Cuerpo JSON que reciben todos los errores. */
    public record ErrorResponse(Instant timestamp, int status, String error, String mensaje, String ruta,
                                Map<String, String> campos) {
    }

    /** Un recurso (usuario, lugar...) no existe o está inactivo: 404. */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(RecursoNoEncontradoException ex, HttpServletRequest req) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    /** Ya existe un recurso con el mismo dato único (usuario o correo): 409. */
    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> duplicado(RecursoDuplicadoException ex, HttpServletRequest req) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage(), req, null);
    }

    /** Se rompió una regla de negocio, por ejemplo seguirse a sí mismo: 422. */
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> reglaNegocio(ReglaNegocioException ex, HttpServletRequest req) {
        return respuesta(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), req, null);
    }

    /** Usuario o contraseña incorrectos al iniciar sesión: 401. */
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> credenciales(CredencialesInvalidasException ex, HttpServletRequest req) {
        return respuesta(HttpStatus.UNAUTHORIZED, ex.getMessage(), req, null);
    }

    /** Falló Bean Validation en el cuerpo JSON: 400 con el mensaje de cada campo. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return respuesta(HttpStatus.BAD_REQUEST, "Los datos enviados no son válidos", req, campos);
    }

    /** Falló la validación de parámetros (query o ruta): 400. */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> validacionParametros(HandlerMethodValidationException ex,
                                                              HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, "Los parámetros enviados no son válidos", req, null);
    }

    /** El cuerpo no es un JSON válido o un campo trae un tipo incorrecto: 400. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> jsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo de la petición no es un JSON válido", req, null);
    }

    /** Un parámetro de la URL tiene un tipo incorrecto: 400. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> tipoInvalido(MethodArgumentTypeMismatchException ex,
                                                      HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, "El parámetro '" + ex.getName() + "' no es válido", req, null);
    }

    /** Se usó un verbo HTTP que la ruta no acepta: 405. */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> metodoNoPermitido(HttpRequestMethodNotSupportedException ex,
                                                           HttpServletRequest req) {
        return respuesta(HttpStatus.METHOD_NOT_ALLOWED, "Método " + ex.getMethod() + " no permitido", req, null);
    }

    /** Se envió un Content-Type que la ruta no acepta: 415. */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> tipoContenido(HttpMediaTypeNotSupportedException ex,
                                                       HttpServletRequest req) {
        return respuesta(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "El cuerpo debe enviarse como application/json", req, null);
    }

    /** Cualquier otro error: 500 con un mensaje genérico; el detalle solo queda en el log del servidor. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> inesperado(Exception ex, HttpServletRequest req) {
        log.error("Error no controlado en {}", req.getRequestURI(), ex);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado", req, null);
    }

    private static ResponseEntity<ErrorResponse> respuesta(HttpStatus status, String mensaje, HttpServletRequest req,
                                                           Map<String, String> campos) {
        var cuerpo = new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), mensaje,
                req.getRequestURI(), campos);
        return ResponseEntity.status(status).body(cuerpo);
    }
}