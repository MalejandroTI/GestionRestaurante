/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PresentacionJFRAME;

import Clases.Usuario;
import FuncionalidadBotones.AgregarCategoria;

import FuncionalidadBotones.AgregarProducto;
import FuncionalidadBotones.AgregarUsuario;
import FuncionalidadBotones.ListaEmpleados;
import FuncionalidadBotones.ListaEmpleadosBoceto;

import java.awt.BorderLayout;

/**
 *
 * @author ASUS
 */
public class PanelAdmin extends javax.swing.JFrame {

    Usuario usuarioactual;
    SelectorDeRol selectorRoles;

    /**
     * Creates new form PanelAdmin
     */
    public PanelAdmin() {
        initComponents();
    }

    public PanelAdmin(Usuario usuarioactual, SelectorDeRol selectorRoles) {

        initComponents();

        this.usuarioactual = usuarioactual;
        this.selectorRoles = selectorRoles;

        System.out.println("PanelAdmin abierto");
        System.out.println("Usuario: " + usuarioactual.getNombre());
    }

    public void cambiarPanel(javax.swing.JPanel panel) {

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
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

        btagregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btagregarProductoActionPerformed(evt);
            }
        });

        btagregarCategoria.setText("AGREGAR CATEGORIA");

        btagregarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btagregarCategoriaActionPerformed(evt);
            }
        });

        btagregarUsuario.setText("CREAR USUARIO");

        btagregarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btagregarUsuarioActionPerformed(evt);
            }
        });

        btnVerEmpleados.setText("EMPLEADOS");

        btnVerEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerEmpleadosActionPerformed(evt);
            }
        });

        btnRegresarRoles.setText("REGRESAR");

        btnRegresarRoles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarRolesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout
                = new javax.swing.GroupLayout(getContentPane());

        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnVerEmpleados,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 180,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btagregarProducto,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 180,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btagregarUsuario,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 180,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btagregarCategoria,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 180,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnRegresarRoles,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(panelContenido,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, 700,
                                                Short.MAX_VALUE))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(btnVerEmpleados,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 60,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(btagregarProducto,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 60,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(btagregarUsuario,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 60,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(btagregarCategoria,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 60,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(200, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(btnRegresarRoles,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(panelContenido,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE))
        );

        pack();

        setLocationRelativeTo(null);
    }// </editor-fold>

    private void btagregarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {

        AgregarUsuario panel = new AgregarUsuario(this);
        cambiarPanel(panel);

    }

    private void btnVerEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {

        ListaEmpleados panel = new ListaEmpleados(this);

        panelContenido.removeAll();

        panelContenido.setLayout(new java.awt.BorderLayout());

        panelContenido.add(panel, java.awt.BorderLayout.CENTER);

        panelContenido.revalidate();

        panelContenido.repaint();
    }

    private void btagregarProductoActionPerformed(java.awt.event.ActionEvent evt) {

        AgregarProducto panel = new AgregarProducto(this);

        cambiarPanel(panel);
    }

    private void btagregarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {
        AgregarCategoria panel
                = new AgregarCategoria(this);

        panelContenido.removeAll();

        panelContenido.setLayout(new java.awt.BorderLayout());

        panelContenido.add(panel,
                java.awt.BorderLayout.CENTER);

        panelContenido.revalidate();

        panelContenido.repaint();
    }

    private void btnRegresarRolesActionPerformed(java.awt.event.ActionEvent evt) {

        selectorRoles.setVisible(true);

        this.dispose();
    }

    // Variables declaration
    private javax.swing.JButton btnRegresarRoles;
    private javax.swing.JButton btagregarCategoria;
    private javax.swing.JButton btagregarProducto;
    private javax.swing.JButton btagregarUsuario;

    private javax.swing.JButton btnVerEmpleados;

    private javax.swing.JPanel panelContenido;
    // End of variables declaration
}
