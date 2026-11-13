package Dominio.Excepciones;

/** Indica que la cuenta o la contraseña no permiten iniciar sesión. */
public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String message) {
        super(message);
    }
}
