/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import logica.ProductoJpaController;
import logica.CategoriaJpaController;

import Clases.Categoria;
import Clases.Producto;
import PresentacionJFRAME.PanelAdmin;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;

public class AgregarProducto extends javax.swing.JPanel {

    private final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

    private final ProductoJpaController productoControlador;
    private final CategoriaJpaController categoriaControlador;
    private final PanelAdmin panelAdmin;

    /**
     * Creates new form AgregarProductoX
     *
     * @param panelAdmin
     */
    public AgregarProducto(PanelAdmin panelAdmin) {

        initComponents();

        this.panelAdmin = panelAdmin;

        productoControlador = new ProductoJpaController(emf);

        categoriaControlador = new CategoriaJpaController(emf);

        cargarCategorias();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();

        lblPrecio = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();

        lblCategoria = new javax.swing.JLabel();
        cbCategoria = new javax.swing.JComboBox<>();

        btnGuardar = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();

        lblNombre.setText("Nombre:");

        lblPrecio.setText("Precio:");

        lblCategoria.setText("Categoría:");

        btnGuardar.setText("Guardar Producto");

        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnRegresar.setText("Regresar");

        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        GroupLayout groupLayout = new GroupLayout(this);

        this.setLayout(groupLayout);

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                groupLayout.createSequentialGroup()
                                        .addContainerGap(300, Short.MAX_VALUE)
                                        .addComponent(btnRegresar)
                                        .addGap(20, 20, 20))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(groupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblNombre)
                                        .addComponent(lblPrecio)
                                        .addComponent(lblCategoria))
                                .addGap(30, 30, 30)
                                .addGroup(groupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtNombre)
                                        .addComponent(txtPrecio)
                                        .addComponent(cbCategoria, 0, 220, Short.MAX_VALUE)
                                        .addComponent(btnGuardar,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE))
                                .addContainerGap(40, Short.MAX_VALUE))
        );

        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(btnRegresar)
                                .addGap(20, 20, 20)
                                .addGroup(groupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblNombre)
                                        .addComponent(txtNombre,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(groupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblPrecio)
                                        .addComponent(txtPrecio,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(groupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblCategoria)
                                        .addComponent(cbCategoria,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addComponent(btnGuardar)
                                .addContainerGap(40, Short.MAX_VALUE))
        );

    }// </editor-fold>

    private void cargarCategorias() {

        try {

            List<Categoria> listaCategorias
                    = categoriaControlador.findCategoriaEntities();

            DefaultComboBoxModel<Categoria> modelo
                    = new DefaultComboBoxModel<>();

            for (Categoria categoria : listaCategorias) {

                modelo.addElement(categoria);
            }

            cbCategoria.setModel(modelo);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Error al cargar categorías:\n" + e.getMessage());
        }
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        if (txtNombre.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Ingrese el nombre del producto");

            txtNombre.requestFocus();

            return;
        }

        if (txtPrecio.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Ingrese el precio");

            txtPrecio.requestFocus();

            return;
        }

        try {

            String nombre = txtNombre.getText().trim();

            BigDecimal precio
                    = new BigDecimal(txtPrecio.getText().trim());

            Categoria categoriaSeleccionada
                    = (Categoria) cbCategoria.getSelectedItem();

            Producto producto = new Producto();

            producto.setNombre(nombre);

            producto.setPrecio(precio);

            producto.setIdCategoria(categoriaSeleccionada);

            productoControlador.create(producto);

            JOptionPane.showMessageDialog(this,
                    "Producto agregado correctamente");

            limpiarCampos();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "El precio debe ser numérico");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Error al guardar:\n" + e.getMessage());
        }
    }

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {

        panelAdmin.mostrarPanelPrincipal();
    }

    private void limpiarCampos() {

        txtNombre.setText("");

        txtPrecio.setText("");

        if (cbCategoria.getItemCount() > 0) {

            cbCategoria.setSelectedIndex(0);
        }

        txtNombre.requestFocus();
    }

    /**
     * @param args the command line arguments
     */
    // Variables declaration
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRegresar;

    private javax.swing.JComboBox<Categoria> cbCategoria;

    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPrecio;

    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration
}
