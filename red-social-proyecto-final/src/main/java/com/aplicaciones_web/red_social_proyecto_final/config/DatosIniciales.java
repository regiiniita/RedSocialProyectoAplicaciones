package com.aplicaciones_web.red_social_proyecto_final.config;

import Dominio.EstadoReporte;
import Dominio.EstadoResenia;
import com.aplicaciones_web.red_social_proyecto_final.model.CuentaUsuario;
import com.aplicaciones_web.red_social_proyecto_final.model.Lugar;
import com.aplicaciones_web.red_social_proyecto_final.model.ReporteResena;
import com.aplicaciones_web.red_social_proyecto_final.model.Resena;
import com.aplicaciones_web.red_social_proyecto_final.repository.ReporteRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.ResenaRepository;
import com.aplicaciones_web.red_social_proyecto_final.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/** Agrega cuentas mock para probar el módulo sin reemplazar los datos existentes. */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.datos-prueba.habilitados", havingValue = "true")
public class DatosIniciales implements ApplicationRunner {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final ReporteRepository reportes;
    private final ResenaRepository resenas;

    @Value("${app.datos-prueba.admin-password}")
    private String adminPassword;

    @Value("${app.datos-prueba.usuario-password}")
    private String usuarioPassword;

    /** Se ejecuta una vez al arrancar; las contraseñas se guardan como hashes BCrypt. */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        crearSiNoExiste("Administrador de prueba", "admin", "admin@example.com",
                adminPassword, "ADMINISTRADOR", true, 0);
        crearSiNoExiste("Regina Jiménez", "regiiniita", "reginameneses@gmail.com",
                usuarioPassword, "USUARIO_REGISTRADO", true, 48);
        crearSiNoExiste("Jesus Martinez", "Yisus0714", "JesusM@gmail.com",
                usuarioPassword, "USUARIO_REGISTRADO", true, 41);
        crearSiNoExiste("Ramses", "rams3s", "ramses30@gmail.com",
                usuarioPassword, "USUARIO_REGISTRADO", false, 29);
    }

    private Resena crearResena(CuentaUsuario autor, Lugar lugar,
                               int calificacion, int diasAtras, EstadoResenia estado) {
        return resenas.save(new Resena(null, LocalDate.now().minusDays(diasAtras), autor, lugar,
                calificacion, "Texto de prueba de la reseña de " + lugar.getNombre() + ".", null, estado));
    }

    private void crearReporte(CuentaUsuario reportante, Resena resena, String motivo,
                              int diasAtras, EstadoReporte estado) {
        reportes.save(new ReporteResena(null, LocalDate.now().minusDays(diasAtras), reportante, resena,
                motivo, "Comentario de prueba del reportante.", estado));
    }

    private void crearSiNoExiste(String nombre, String username, String email,
                                String password, String rol, boolean activo, int totalResenas) {
        // No cambia contraseñas ni reactiva cuentas al reiniciar la aplicación.
        if (usuarios.findByUsernameIgnoreCase(username).isEmpty()) {
            usuarios.save(new CuentaUsuario(null, nombre, username, email,
                    encoder.encode(password), rol, activo, totalResenas));
        }
    }
}
