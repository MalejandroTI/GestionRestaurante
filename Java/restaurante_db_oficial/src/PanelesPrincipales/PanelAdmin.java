/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelesPrincipales;

import Clases.Rol;
import Clases.Usuario;
import FuncionalidadBotones.AgregarCategoria;
import FuncionalidadBotones.AgregarProducto;
import FuncionalidadBotones.AgregarUsuario;
import FuncionalidadBotones.ListaEmpleados;
import PresentacionJFRAME.SelectorDeRol;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class PanelAdmin extends javax.swing.JFrame {

    private final Usuario usuarioactual;
    
    public PanelAdmin(Usuario usuarioactual ) {
        initComponents();
        this.usuarioactual = usuarioactual;

        panelContenido.setLayout(new BorderLayout());

    }

    public void cambiarPanel(JPanel panel) {

        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    public void mostrarPanelPrincipal() {
        panelContenido.removeAll();
        panelContenido.revalidate();
        panelContenido.repaint();
    }

   
    private void btagregarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {
        cambiarPanel(new AgregarUsuario(this));
    }

    private void btnVerEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {
        cambiarPanel(new ListaEmpleados(this));
    }

    private void btagregarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        cambiarPanel(new AgregarProducto(this));
    }

    private void btagregarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {
        cambiarPanel(new AgregarCategoria(this));
    }

    private void btnRegresarRolesActionPerformed(java.awt.event.ActionEvent evt) {

         SelectorDeRol selector
                = new SelectorDeRol(usuarioactual);

        selector.setLocationRelativeTo(null);

        selector.setVisible(true);

        this.dispose();
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        btagregarProducto = new javax.swing.JButton();
        btagregarCategoria = new javax.swing.JButton();
        btagregarUsuario = new javax.swing.JButton();
        btnVerEmpleados = new javax.swing.JButton();
        btnRegresarRoles = new javax.swing.JButton();

        panelContenido = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelContenido.setLayout(new BorderLayout());

        btagregarProducto.setText("AGREGAR PRODUCTO");
        btagregarProducto.addActionListener(this::btagregarProductoActionPerformed);

        btagregarCategoria.setText("AGREGAR CATEGORIA");
        btagregarCategoria.addActionListener(this::btagregarCategoriaActionPerformed);

        btagregarUsuario.setText("CREAR USUARIO");
        btagregarUsuario.addActionListener(this::btagregarUsuarioActionPerformed);

        btnVerEmpleados.setText("EMPLEADOS");
        btnVerEmpleados.addActionListener(this::btnVerEmpleadosActionPerformed);

        btnRegresarRoles.setText("REGRESAR");
        btnRegresarRoles.addActionListener(this::btnRegresarRolesActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnVerEmpleados, 180, 180, 180)
                                        .addComponent(btagregarProducto, 180, 180, 180)
                                        .addComponent(btagregarUsuario, 180, 180, 180)
                                        .addComponent(btagregarCategoria, 180, 180, 180))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnRegresarRoles, 150, 150, 150)
                                        .addComponent(panelContenido, 700, 700, Short.MAX_VALUE))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30)
                                .addComponent(btnVerEmpleados, 60, 60, 60)
                                .addGap(20)
                                .addComponent(btagregarProducto, 60, 60, 60)
                                .addGap(20)
                                .addComponent(btagregarUsuario, 60, 60, 60)
                                .addGap(20)
                                .addComponent(btagregarCategoria, 60, 60, 60)
                                .addContainerGap(200, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10)
                                .addComponent(btnRegresarRoles, 35, 35, 35)
                                .addGap(5)
                                .addComponent(panelContenido, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    // Variables
    private javax.swing.JButton btnRegresarRoles;
    private javax.swing.JButton btagregarCategoria;
    private javax.swing.JButton btagregarProducto;
    private javax.swing.JButton btagregarUsuario;
    private javax.swing.JButton btnVerEmpleados;
    private javax.swing.JPanel panelContenido;
}