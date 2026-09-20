package Dominio;

import java.util.ArrayList;
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
    private List<Usuario> seguidos;

    /**
     * Constructor por defecto.
     */
    public Usuario() {
        this.seguidos = new ArrayList<>();
    }

    /**
     * Constructor con atributos principales del usuario.
     *
     * @param id Identificador único.
     * @param username Nombre de usuario.
     * @param email Correo electrónico.
     * @param contrasenia Contraseña del usuario.
     * @param rolUsuario Rol asignado.
     */
    public Usuario(Long id, String username, String email, String contrasenia, Rol rolUsuario) {
        this();
        this.id = id;
        this.username = username;
        this.email = email;
        this.contrasenia = contrasenia;
        this.rolUsuario = rolUsuario;
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

    public void setRolUsuario(Rol rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public List<Usuario> getSeguidos() {
        return seguidos;
    }

    public void setSeguidos(List<Usuario> seguidos) {
        this.seguidos = seguidos;
    }
}
