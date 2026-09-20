package Dominio;
/**
 * Representa la dirección física detallada de un lugar dentro del catálogo.
 */
public class Direccion {

    private String nombreCalle;
    private String numero;
    private String codigoPostal;
    private String colonia;

    /**
     * Constructor por defecto.
     */
    public Direccion() {
    }

    /**
     * Constructor con todos los atributos de la dirección.
     *
     * @param nombreCalle Nombre de la calle.
     * @param numero Número exterior o interior.
     * @param codigoPostal Código postal.
     * @param colonia Nombre de la colonia.
     */
    public Direccion(String nombreCalle, String numero, String codigoPostal, String colonia) {
        this.nombreCalle = nombreCalle;
        this.numero = numero;
        this.codigoPostal = codigoPostal;
        this.colonia = colonia;
    }

    public String getNombreCalle() {
        return nombreCalle;
    }

    public void setNombreCalle(String nombreCalle) {
        this.nombreCalle = nombreCalle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
}
