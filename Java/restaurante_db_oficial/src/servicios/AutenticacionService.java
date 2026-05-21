/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import Clases.Usuario;
import javax.persistence.EntityManagerFactory;
import logica.UsuarioJpaController;

/**
 *
 * @author ASUS
 */
public class AutenticacionService {

    private final UsuarioJpaController usuarioController;

    public AutenticacionService(EntityManagerFactory emf) {
        this.usuarioController = new UsuarioJpaController(emf);
    }

    public Usuario login(String correo, String password) {

        Usuario usuario = usuarioController.findUsuarioByCorreo(correo);

        if (usuario == null) {
            throw new RuntimeException("Usuario no existe");
        }

        if (!usuario.getContrasena().equals(password)) {
            throw new RuntimeException("Password incorrecta");
        }

        return usuario;
    }
}
