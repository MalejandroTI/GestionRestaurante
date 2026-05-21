/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.Categoria;
import PresentacionJFRAME.PanelAdmin;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javax.swing.JOptionPane;

import logica.CategoriaJpaController;

public class AgregarCategoria extends javax.swing.JPanel {

    private final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

    private final CategoriaJpaController categoriaControlador;

    private final PanelAdmin panelAdmin;

    public AgregarCategoria(PanelAdmin panelAdmin) {

        initComponents();

        this.panelAdmin = panelAdmin;

        categoriaControlador
                = new CategoriaJpaController(emf);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();

        lblNombre = new javax.swing.JLabel();

        txtNombre = new javax.swing.JTextField();

        lblDescripcion = new javax.swing.JLabel();

        scrollDescripcion = new javax.swing.JScrollPane();

        txtDescripcion = new javax.swing.JTextArea();

        btnGuardar = new javax.swing.JButton();

        btnRegresar = new javax.swing.JButton();

        lblTitulo.setText("AGREGAR CATEGORÍA");

        lblNombre.setText("Nombre:");

        lblDescripcion.setText("Descripción:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);

        scrollDescripcion.setViewportView(txtDescripcion);

        btnGuardar.setText("GUARDAR");

        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnRegresar.setText("REGRESAR");

        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout
                = new javax.swing.GroupLayout(this);

        this.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnRegresar)
                                                .addGap(140, 140, 140)
                                                .addComponent(lblTitulo))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblNombre)
                                                        .addComponent(lblDescripcion))
                                                .addGap(30, 30, 30)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtNombre)
                                                        .addComponent(scrollDescripcion,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                300,
                                                                Short.MAX_VALUE)
                                                        .addComponent(btnGuardar,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE))))
                                .addContainerGap(40, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnRegresar)
                                        .addComponent(lblTitulo))
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblNombre)
                                        .addComponent(txtNombre,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblDescripcion)
                                        .addComponent(scrollDescripcion,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                120,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addComponent(btnGuardar)
                                .addContainerGap(40, Short.MAX_VALUE))
        );
    }

    private void btnGuardarActionPerformed(
            java.awt.event.ActionEvent evt) {

        if (txtNombre.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Ingrese el nombre de la categoría");

            txtNombre.requestFocus();

            return;
        }

        try {

            Categoria categoria = new Categoria();

            categoria.setNombre(
                    txtNombre.getText().trim()
            );

            categoria.setDescripcion(
                    txtDescripcion.getText().trim()
            );

            categoriaControlador.create(categoria);

            JOptionPane.showMessageDialog(this,
                    "Categoría agregada correctamente");

            limpiarCampos();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Error al guardar:\n" + e.getMessage());
        }
    }

    private void btnRegresarActionPerformed(
            java.awt.event.ActionEvent evt) {

        panelAdmin.mostrarPanelPrincipal();
    }

    private void limpiarCampos() {

        txtNombre.setText("");

        txtDescripcion.setText("");

        txtNombre.requestFocus();
    }

    // VARIABLES
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRegresar;

    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTitulo;

    private javax.swing.JScrollPane scrollDescripcion;

    private javax.swing.JTextArea txtDescripcion;

    private javax.swing.JTextField txtNombre;
}
