/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.Rol;
import Clases.Usuario;
import PresentacionJFRAME.PanelAdmin;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import logica.RolJpaController;
import logica.UsuarioJpaController;

/**
 *
 * @author ASUS
 */
public class AgregarUsuario extends javax.swing.JPanel {

    private final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

    private final PanelAdmin panelAdmin;

    private Map<JCheckBox, Rol> mapaRoles = new HashMap<>();

    /**
     * Creates new form AgregarUsuario
     */
    public AgregarUsuario(PanelAdmin panelAdmin) {

        initComponents();

        this.panelAdmin = panelAdmin;

        cargarRolesDinamicos();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jButton1 = new javax.swing.JButton();

        btnRegresar = new javax.swing.JButton();

        nombre = new javax.swing.JTextField();
        apellido = new javax.swing.JTextField();
        cedula = new javax.swing.JTextField();
        fecha = new javax.swing.JTextField();
        celular = new javax.swing.JTextField();
        correo = new javax.swing.JTextField();
        contrasena = new javax.swing.JTextField();

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        panelRoles = new javax.swing.JPanel();

        jButton1.setText("AGREGAR");

        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnRegresar.setText("Regresar");

        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        fecha.setText("yyyy-MM-dd");

        jLabel1.setText("INGRESE SU CORREO");

        jLabel2.setText("INGRESE SU APELLIDO");

        jLabel3.setText("INGRESE SU CEDULA");

        jLabel4.setText("INGRESE SU FECHA DE NACIMIENTO");

        jLabel5.setText("INGRESE SU CONTRASENA");

        jLabel6.setText("INGRESE SU CELULAR");

        jLabel7.setText("INGRESE SU NOMBRE");

        jLabel8.setText("INGRESE LOS DATOS DEL NUEVO USUARIO!");

        jLabel9.setText("SELECCIONE EL ROL A CUMPLIR");

        panelRoles.setBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelRolesLayout
                = new javax.swing.GroupLayout(panelRoles);

        panelRoles.setLayout(panelRolesLayout);

        panelRolesLayout.setHorizontalGroup(
                panelRolesLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 190, Short.MAX_VALUE)
        );

        panelRolesLayout.setVerticalGroup(
                panelRolesLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout
                = new javax.swing.GroupLayout(this);

        this.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                layout.createSequentialGroup()
                                        .addContainerGap(500, Short.MAX_VALUE)
                                        .addComponent(btnRegresar)
                                        .addGap(20, 20, 20))

                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(63, 63, 63)
                                                .addComponent(jLabel8))

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(45, 45, 45)
                                                .addComponent(nombre,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        188,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addGap(45, 45, 45)
                                                .addComponent(apellido,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        188,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addGap(45, 45, 45)
                                                .addComponent(cedula,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        188,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addGap(18, 18, 18)
                                                .addComponent(fecha,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        188,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addGap(23, 23, 23)
                                                .addComponent(celular,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        188,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(23, 23, 23)
                                                .addComponent(correo,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        188,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addGap(18, 18, 18)
                                                .addComponent(contrasena,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        188,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addGap(18, 18, 18)
                                                .addComponent(panelRoles,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))

                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(148, 148, 148)
                                                .addComponent(jButton1)))

                                .addContainerGap(40, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                        .addGroup(layout.createSequentialGroup()

                                .addGap(20, 20, 20)

                                .addComponent(btnRegresar)

                                .addGap(20, 20, 20)

                                .addComponent(jLabel8)

                                .addGap(20, 20, 20)

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(nombre,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(18, 18, 18)

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(apellido,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(18, 18, 18)

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(cedula,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(18, 18, 18)

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(fecha,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(18, 18, 18)

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(celular,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(18, 18, 18)

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(correo,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(18, 18, 18)

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(contrasena,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(25, 25, 25)

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(panelRoles,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(25, 25, 25)

                                .addComponent(jButton1)

                                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

        try {

            Usuario nuevoUser = new Usuario();

            nuevoUser.setNombre(nombre.getText());

            nuevoUser.setApellido(apellido.getText());

            nuevoUser.setCedula(cedula.getText());

            nuevoUser.setCelular(celular.getText());

            nuevoUser.setCorreo(correo.getText());

            nuevoUser.setContrasena(contrasena.getText());

            SimpleDateFormat sdf
                    = new SimpleDateFormat("yyyy-MM-dd");

            Date fechaNacimiento
                    = sdf.parse(fecha.getText());

            nuevoUser.setFechaNacimiento(fechaNacimiento);

            List<Rol> rolesSeleccionados = new ArrayList<>();

            for (Map.Entry<JCheckBox, Rol> entry : mapaRoles.entrySet()) {

                if (entry.getKey().isSelected()) {

                    rolesSeleccionados.add(entry.getValue());
                }
            }

            nuevoUser.setRolCollection(rolesSeleccionados);

            nuevoUser.setActivo(true);

            UsuarioJpaController controladorUsuario
                    = new UsuarioJpaController(emf);

            controladorUsuario.create(nuevoUser);

            JOptionPane.showMessageDialog(this,
                    "Usuario creado correctamente");

            limpiarCampos();

        } catch (ParseException e) {

            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido (yyyy-MM-dd)");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage());
        }
    }

    private void btnRegresarActionPerformed(
            java.awt.event.ActionEvent evt) {

        panelAdmin.mostrarPanelPrincipal();
    }

    private void cargarRolesDinamicos() {

        RolJpaController rolController
                = new RolJpaController(emf);

        List<Rol> roles
                = rolController.findRolEntities();

        panelRoles.setLayout(
                new BoxLayout(panelRoles, BoxLayout.Y_AXIS));

        panelRoles.removeAll();

        mapaRoles.clear();

        for (Rol rol : roles) {

            JCheckBox check
                    = new JCheckBox(rol.getNombre());

            panelRoles.add(check);

            mapaRoles.put(check, rol);
        }

        panelRoles.revalidate();

        panelRoles.repaint();
    }

    private void limpiarCampos() {

        nombre.setText("");

        apellido.setText("");

        cedula.setText("");

        fecha.setText("");

        celular.setText("");

        correo.setText("");

        contrasena.setText("");

        for (JCheckBox check : mapaRoles.keySet()) {

            check.setSelected(false);
        }
    }

    // Variables declaration
    private javax.swing.JTextField apellido;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JTextField cedula;
    private javax.swing.JTextField celular;
    private javax.swing.JTextField contrasena;
    private javax.swing.JTextField correo;
    private javax.swing.JTextField fecha;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField nombre;
    private javax.swing.JPanel panelRoles;
    // End of variables declaration
}