package Dominio;

import Dominio.Excepciones.ReglaNegocioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa a un usuario dentro del sistema.
 * Mantiene la información del perfil, las credenciales de acceso, el rol asignado y
 * la relación de seguimiento con otros usuarios de la red social.
 */
public class Usuario {

    private Long id;
    private String username;
    private String email;
    private String contrasenia;
    private Rol rolUsuario;
    private String nombre;
    private String fotoPerfil;
    private String biografia;
    private final List<Seguimiento> seguimientos = new ArrayList<>(); // a quién sigue este usuario

    public Usuario() {
    }

    public Usuario(Long id, String username, String email, String contrasenia, Rol rolUsuario,
                   String nombre, String fotoPerfil, String biografia) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.contrasenia = contrasenia;
        this.rolUsuario = rolUsuario;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.biografia = biografia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Rol getRolUsuario() {
        return rolUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public void setRolUsuario(Rol rolUsuario) {
        this.rolUsuario = rolUsuario;
    }
    public boolean esMismoUsuario(Usuario otro) {
        if (otro == null) return false;
        if (this == otro) return true;
        if (id != null && otro.id != null) return id.equals(otro.id);
        return username != null && username.equalsIgnoreCase(otro.username);
    }

    public Seguimiento seguir(Usuario otro) {
        Seguimiento nuevo = new Seguimiento(null, this, otro, null);
        for (Seguimiento s : seguimientos) {
            if (s.getSeguido().esMismoUsuario(otro)) {
                throw new ReglaNegocioException("Ya sigues a este usuario.");
            }
        }
        seguimientos.add(nuevo);
        return nuevo;
    }

    public void dejarDeSeguir(Usuario otro) {
        seguimientos.removeIf(s -> s.getSeguido().esMismoUsuario(otro));
    }

    public List<Seguimiento> getSeguimientos() { return Collections.unmodifiableList(seguimientos); }
}
