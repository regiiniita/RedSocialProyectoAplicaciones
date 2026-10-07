package Dominio;

public enum RangoPrecios {
    ECONOMICO("$"),
    MODERADO("$$"),
    ALTO("$$$"),
    MUY_ALTO("$$$");

    private final String simbolo;
    RangoPrecios(String simbolo) {this.simbolo = simbolo;}
    public String getSimbolo() {return simbolo;}
}
