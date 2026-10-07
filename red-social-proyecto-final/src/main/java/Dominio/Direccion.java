package Dominio;
/**
 * Representa la dirección física detallada de un lugar dentro del catálogo.
 */
public class Direccion {

    private String nombreCalle;
    private String numero;
    private String codigoPostal;
    private String colonia;
    private String ciudad;

    /**
     * Constructor por defecto.
     */
    public Direccion() {
    }

    public Direccion(String nombreCalle, String numero, String codigoPostal, String colonia, String ciudad) {
        this.nombreCalle = nombreCalle;
        this.numero = numero;
        this.codigoPostal = codigoPostal;
        this.colonia = colonia;
        this.ciudad = ciudad;
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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
