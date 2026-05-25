/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

/**
 *
 * @author ASUS
 */
import Clases.Rol;
import Clases.Usuario;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;

import logica.UsuarioJpaController;
import utilJpa.JPAUtil;

public class UsuarioService {

    private final UsuarioJpaController usuarioController;

    public UsuarioService() {

        usuarioController = new UsuarioJpaController(JPAUtil.getEMF());
    }

    public void crearUsuario(
            String nombre,
            String apellido,
            String cedula,
            String fechaTexto,
            String celular,
            String correo,
            String contrasena,
            List<Rol> roles
    ) throws Exception {

        if (nombre == null || nombre.trim().isEmpty()) {

            throw new Exception("Ingrese el nombre");
        }

        if (apellido == null || apellido.trim().isEmpty()) {

            throw new Exception("Ingrese el apellido");
        }

        if (cedula == null || cedula.trim().isEmpty()) {

            throw new Exception("Ingrese la cédula");
        }

        if (fechaTexto == null || fechaTexto.trim().isEmpty()) {

            throw new Exception("Ingrese la fecha");
        }

        if (correo == null || correo.trim().isEmpty()) {

            throw new Exception("Ingrese el correo");
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {

            throw new Exception("Ingrese la contraseña");
        }

        if (roles == null || roles.isEmpty()) {

            throw new Exception("Seleccione al menos un rol");
        }

        Date fechaNacimiento;

        try {

            SimpleDateFormat sdf
                    = new SimpleDateFormat("yyyy-MM-dd");

            sdf.setLenient(false);

            fechaNacimiento = sdf.parse(fechaTexto.trim());

        } catch (ParseException e) {

            throw new Exception(
                    "Formato de fecha inválido (yyyy-MM-dd)"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre.trim());

        usuario.setApellido(apellido.trim());

        usuario.setCedula(cedula.trim());

        usuario.setFechaNacimiento(fechaNacimiento);

        usuario.setCelular(celular.trim());

        usuario.setCorreo(correo.trim());

        usuario.setContrasena(contrasena.trim());

        usuario.setRolCollection(roles);

        usuario.setActivo(true);

        usuarioController.create(usuario);
    }

    public List<Usuario> obtenerUsuarios() {

        return usuarioController.findUsuarioEntities();
    }

    public List<Usuario> buscarUsuarios(String texto) {

        if (texto == null) {

            texto = "";
        }

        String t = texto.toLowerCase().trim();

        return obtenerUsuarios().stream()
                .filter(u
                        -> String.valueOf(
                                u.getIdUsuario()
                        ).contains(t)
                        || u.getNombre().toLowerCase().contains(t)
                        || u.getApellido().toLowerCase().contains(t)
                        || u.getCedula().contains(t)
                        || u.getCorreo().toLowerCase().contains(t)
                )
                .collect(Collectors.toList());
    }

    public List<Usuario> filtrarPorActivo(boolean activo) {

        return obtenerUsuarios().stream()
                .filter(u -> Boolean.TRUE.equals(u.getActivo()) == activo)
                .collect(Collectors.toList());
    }

    public void cambiarEstadoUsuario(Usuario usuario)
            throws Exception {

        if (usuario == null) {

            throw new Exception("Usuario inválido");
        }

        usuario.setActivo(!usuario.getActivo());

        usuarioController.edit(usuario);
    }
}