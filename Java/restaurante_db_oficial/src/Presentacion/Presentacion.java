/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Presentacion;

import Clases.Rol;
import Clases.Usuario;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import logica.RolJpaController;
import logica.UsuarioJpaController;

/**
 *
 * @author ASUS
 */
public class Presentacion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

        UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
        RolJpaController rolController = new RolJpaController(emf);

        Usuario nuevo = new Usuario();

        System.out.println("Ingrese Nombre:");
        nuevo.setNombre(sc.nextLine());

        System.out.println("Ingrese apellido:");
        nuevo.setApellido(sc.nextLine());

        System.out.println("Ingrese cedula:");
        nuevo.setCedula(sc.nextLine());

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setLenient(false); // 🔥 importante

        Date fechaNacimiento = null;

        while (fechaNacimiento == null) {
            System.out.print("Ingrese fecha de nacimiento (yyyy-MM-dd): ");
            String fechaTexto = sc.nextLine();

            try {
                fechaNacimiento = formato.parse(fechaTexto);
            } catch (ParseException e) {
                System.out.println("Formato incorrecto, intente de nuevo");
            }
        }

        nuevo.setFechaNacimiento(fechaNacimiento);

        System.out.println("Ingrese Celular:");
        nuevo.setCelular(sc.nextLine());

        System.out.println("Ingrese correo:");
        nuevo.setCorreo(sc.nextLine());

        System.out.println("Ingrese contrasena:");
        nuevo.setContrasena(sc.nextLine());
        nuevo.setActivo(Boolean.TRUE);
        System.out.println("Desea agregar un rol a este usuario S/N");
        String option01 = sc.nextLine();

        if (option01.equalsIgnoreCase("S")) {

            System.out.println("=== ROLES DISPONIBLES ===");

            List<Rol> roles = rolController.findRolEntities();

            for (Rol r : roles) {
                System.out.println(r.getIdRol() + " - " + r.getNombre());
            }

            System.out.print("Ingrese ID del rol: ");
            int idRol = Integer.parseInt(sc.nextLine());

            Rol rol = rolController.findRol(idRol);

            if (rol != null) {
                nuevo.setRolCollection(Arrays.asList(rol)); 
            } else {
                System.out.println("Rol no encontrado");
            }
        }

        // 🔥 guardar al final
        usuarioController.create(nuevo);

        System.out.println("Usuario creado correctamente");
    }
}
