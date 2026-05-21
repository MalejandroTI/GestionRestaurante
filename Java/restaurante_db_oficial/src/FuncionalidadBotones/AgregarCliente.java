/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.Cliente;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import logica.ClienteJpaController;

/**
 *
 * @author ASUS
 */
public class AgregarCliente extends javax.swing.JFrame{
    private final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory(
                    "restaurante_db_oficialPU"
            );

    public AgregarCliente() {

        initComponents();

        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtCedula = new javax.swing.JTextField();
        txtCelular = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();

        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(
                javax.swing.WindowConstants.DISPOSE_ON_CLOSE
        );

        jLabel1.setText("NUEVO CLIENTE");

        jLabel2.setText("Nombre:");
        jLabel3.setText("Apellido:");
        jLabel4.setText("Cédula:");
        jLabel5.setText("Celular:");
        jLabel6.setText("Correo:");

        btnGuardar.setText("GUARDAR");

        btnGuardar.addActionListener(
                new java.awt.event.ActionListener() {

            public void actionPerformed(
                    java.awt.event.ActionEvent evt) {

                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setText("CANCELAR");

        btnCancelar.addActionListener(
                new java.awt.event.ActionListener() {

            public void actionPerformed(
                    java.awt.event.ActionEvent evt) {

                dispose();
            }
        });

        javax.swing.GroupLayout layout
                = new javax.swing.GroupLayout(
                        getContentPane()
                );

        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING
                )
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.LEADING)

                                        .addComponent(jLabel1)

                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.LEADING)

                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel6))

                                                .addGap(20, 20, 20)

                                                .addGroup(layout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                        false)

                                                        .addComponent(txtNombre)
                                                        .addComponent(txtApellido)
                                                        .addComponent(txtCedula)
                                                        .addComponent(txtCelular)
                                                        .addComponent(txtCorreo,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                250,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnGuardar)
                                                .addGap(20, 20, 20)
                                                .addComponent(btnCancelar)))
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING
                )
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)

                                .addComponent(jLabel1)

                                .addGap(30, 30, 30)

                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(txtNombre,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(15, 15, 15)

                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(txtApellido,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(15, 15, 15)

                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(txtCedula,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(15, 15, 15)

                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(txtCelular,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(15, 15, 15)

                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(txtCorreo,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))

                                .addGap(30, 30, 30)

                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnGuardar)
                                        .addComponent(btnCancelar))

                                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }

    private void btnGuardarActionPerformed(
            java.awt.event.ActionEvent evt) {

        try {

            if (txtNombre.getText().isBlank()
                    || txtApellido.getText().isBlank()
                    || txtCedula.getText().isBlank()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Complete los campos obligatorios"
                );

                return;
            }

            Cliente cliente = new Cliente();

            cliente.setNombre(
                    txtNombre.getText()
            );

            cliente.setApellido(
                    txtApellido.getText()
            );

            cliente.setCedula(
                    txtCedula.getText()
            );

            cliente.setCelular(
                    txtCelular.getText()
            );

            cliente.setCorreo(
                    txtCorreo.getText()
            );

            ClienteJpaController controller
                    = new ClienteJpaController(emf);

            controller.create(cliente);

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente creado correctamente"
            );

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar cliente:\n"
                    + e.getMessage()
            );
        }
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {

            new AgregarCliente().setVisible(true);
        });
    }

    // Variables declaration
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;

    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtCelular;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtNombre;
}