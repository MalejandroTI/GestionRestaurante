/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.Cliente;
import java.awt.Component;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import logica.ClienteJpaController;

public class ListaClientesSeleccion extends javax.swing.JFrame {

    private final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

    private final DefaultTableModel modelo;

    private List<Cliente> listaClientes;

    private List<Cliente> listaVisible;

    private final PanelPedidoNuevo panelPedido;

    public ListaClientesSeleccion(PanelPedidoNuevo panelPedido) {

        initComponents();

        this.panelPedido = panelPedido;

        modelo = new DefaultTableModel();

        tablaClientes.setModel(modelo);

        configurarTabla();

        cargarClientes();

        tablaClientes.getColumn("Seleccionar")
                .setCellRenderer(new ButtonRenderer());

        tablaClientes.getColumn("Seleccionar")
                .setCellEditor(
                        new ButtonEditor(
                                new JCheckBox(),
                                tablaClientes
                        )
                );
    }

    private void configurarTabla() {

        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Cédula");
        modelo.addColumn("Celular");
        modelo.addColumn("Seleccionar");
    }

    private void cargarClientes() {

        ClienteJpaController controller
                = new ClienteJpaController(emf);

        listaClientes = controller.findClienteEntities();

        mostrarTabla(listaClientes);
    }

    private void mostrarTabla(List<Cliente> lista) {

        listaVisible = lista;

        modelo.setRowCount(0);

        for (Cliente c : lista) {

            modelo.addRow(new Object[]{
                c.getIdCliente(),
                c.getNombre(),
                c.getApellido(),
                c.getCedula(),
                c.getCelular(),
                "Seleccionar"
            });
        }
    }

    private void buscar(String texto) {

        String t = texto.toLowerCase();

        List<Cliente> filtrados = listaClientes.stream()
                .filter(c
                        -> String.valueOf(c.getIdCliente()).contains(t)
                || c.getNombre().toLowerCase().contains(t)
                || c.getApellido().toLowerCase().contains(t)
                || c.getCedula().contains(t)
                || c.getCelular().contains(t)
                )
                .collect(Collectors.toList());

        mostrarTabla(filtrados);
    }

    class ButtonRenderer extends JButton
            implements javax.swing.table.TableCellRenderer {

        public ButtonRenderer() {
            setText("Seleccionar");
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private final JButton button;

        private final JTable table;

        public ButtonEditor(
                JCheckBox checkBox,
                JTable table) {

            super(checkBox);

            this.table = table;

            button = new JButton("Seleccionar");

            button.addActionListener(e -> {

                int row = table.convertRowIndexToModel(
                        table.getEditingRow()
                );

                if (row < 0) {
                    return;
                }

                Cliente cliente
                        = listaVisible.get(row);

                panelPedido.setClienteSeleccionado(cliente);

                dispose();
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {

            return button;
        }

        @Override
        public Object getCellEditorValue() {

            return "Seleccionar";
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnNuevoCliente = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnBuscar.setText("BUSCAR");

        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnNuevoCliente.setText("CREAR CLIENTE");

        btnNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoClienteActionPerformed(evt);
            }
        });

        tablaClientes.setModel(
                new javax.swing.table.DefaultTableModel(
                        new Object[][]{},
                        new String[]{}
                )
        );

        jScrollPane1.setViewportView(tablaClientes);

        jLabel1.setText("SELECCIONAR CLIENTE");

        btnRegresar.setText("REGRESAR");

        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
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
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtBuscar,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        300,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnBuscar)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnNuevoCliente)
                                                .addGap(0, 120, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnRegresar)
                                                .addGap(220, 220, 220)
                                                .addComponent(jLabel1)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnRegresar)
                                        .addComponent(jLabel1))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtBuscar,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnBuscar)
                                        .addComponent(btnNuevoCliente))
                                .addGap(20, 20, 20)
                                .addComponent(jScrollPane1,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        400,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }

    private void btnBuscarActionPerformed(
            java.awt.event.ActionEvent evt) {

        buscar(txtBuscar.getText());
    }

    private void btnNuevoClienteActionPerformed(
            java.awt.event.ActionEvent evt) {

        AgregarCliente ventana
                = new AgregarCliente();

        ventana.setLocationRelativeTo(this);

        ventana.setVisible(true);
    }

    private void btnRegresarActionPerformed(
            java.awt.event.ActionEvent evt) {

        dispose();
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            // new ListaClientesSeleccion(null).setVisible(true);
        });
    }

    // Variables declaration
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnNuevoCliente;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration
}
