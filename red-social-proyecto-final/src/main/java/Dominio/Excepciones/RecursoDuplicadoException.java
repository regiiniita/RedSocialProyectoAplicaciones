package Dominio.Excepciones;

/** Indica que ya existe un registro con los datos proporcionados. */
public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}