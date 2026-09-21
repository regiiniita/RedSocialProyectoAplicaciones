package Dominio.Excepciones;

/**
 * Excepción lanzada cuando ocurre una violación en las reglas de negocio del dominio.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}