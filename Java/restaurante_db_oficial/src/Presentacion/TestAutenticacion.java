/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

/**
 *
 * @author ASUS
 */
import Clases.Usuario;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import servicios.AutenticacionService;
import Seguridad.SeguridadService;
import java.util.Set;

public class TestAutenticacion {

    public static void main(String[] args) {

        // 1. Conexión JPA
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

        // 2. Servicios
        AutenticacionService auth = new AutenticacionService(emf);
        SeguridadService seguridad = new SeguridadService();

        try {

            // 3. LOGIN
            Usuario usuario = auth.login("alejandrovargas128@gmial.com", "alejandrovargas128");

            System.out.println("================================");
            System.out.println("BIENVENIDO: " + usuario.getNombre());
            System.out.println("================================");

            // 4. OBTENER PERMISOS
            Set<String> permisos = seguridad.obtenerPermisos(usuario);

            System.out.println("PERMISOS DEL USUARIO:");
            permisos.forEach(p -> System.out.println("- " + p));

            System.out.println("================================");

            // 5. PRUEBA DE PERMISOS ESPECÍFICOS
            System.out.println("Puede crear cliente? " +
                    seguridad.tienePermiso(usuario, "CREAR_CLIENTE"));

            System.out.println("Puede gestionar productos? " +
                    seguridad.tienePermiso(usuario, "GESTIONAR_PRODUCTOS"));

            System.out.println("Puede ver facturas? " +
                    seguridad.tienePermiso(usuario, "VER_FACTURA"));

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        emf.close();
    }
}