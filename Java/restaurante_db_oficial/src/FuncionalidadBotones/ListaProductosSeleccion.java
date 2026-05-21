/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

/**
 *
 * @author ASUS
 */
import Clases.Producto;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
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
import logica.ProductoJpaController;

public class ListaProductosSeleccion extends javax.swing.JFrame {

    private final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

    private final DefaultTableModel modelo;

    private List<Producto> listaProductos;

    private List<Producto> listaVisible;

    private final PanelPedidoNuevo panelPedido;

    public ListaProductosSeleccion(PanelPedidoNuevo panelPedido) {

        initComponents();

        this.panelPedido = panelPedido;

        modelo = new DefaultTableModel();

        tablaProductos.setModel(modelo);

        configurarTabla();

        cargarProductos();

        tablaProductos.getColumn("Agregar")
                .setCellRenderer(new ButtonRenderer());

        tablaProductos.getColumn("Agregar")
                .setCellEditor(
                        new ButtonEditor(
                                new JCheckBox(),
                                tablaProductos
                        )
                );
    }

    private void configurarTabla() {

        modelo.addColumn("ID");
        modelo.addColumn("Producto");
        modelo.addColumn("Categoría");
        modelo.addColumn("Precio");
        modelo.addColumn("Agregar");
    }

    private void cargarProductos() {

        ProductoJpaController controller
                = new ProductoJpaController(emf);

        listaProductos = controller.findProductoEntities();

        mostrarTabla(listaProductos);
    }

    private void mostrarTabla(List<Producto> lista) {

        listaVisible = lista;

        modelo.setRowCount(0);

        for (Producto p : lista) {

            modelo.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombre(),
                p.getIdCategoria().getNombre(),
                p.getPrecio(),
                "Agregar"
            });
        }
    }

    private void buscar(String texto) {

        String t = texto.toLowerCase();

        List<Producto> filtrados = listaProductos.stream()
                .filter(p
                        -> String.valueOf(p.getIdProducto()).contains(t)
                || p.getNombre().toLowerCase().contains(t)
                || p.getIdCategoria()
                        .getNombre()
                        .toLowerCase()
                        .contains(t)
                )
                .collect(Collectors.toList());

        mostrarTabla(filtrados);
    }

    class ButtonRenderer extends JButton
            implements javax.swing.table.TableCellRenderer {

        public ButtonRenderer() {

            setText("Agregar");
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

            button = new JButton("Agregar");

            button.addActionListener((ActionEvent e) -> {

                int row = table.convertRowIndexToModel(
                        table.getEditingRow()
                );

                if (row < 0) {
                    return;
                }

                Producto producto
                        = listaVisible.get(row);

                String cantidadTexto
                        = JOptionPane.showInputDialog(
                                button,
                                "Ingrese cantidad:"
                        );

                if (cantidadTexto == null
                        || cantidadTexto.isBlank()) {

                    return;
                }

                try {

                    int cantidad
                            = Integer.parseInt(cantidadTexto);

                    if (cantidad <= 0) {

                        JOptionPane.showMessageDialog(
                                button,
                                "Cantidad inválida"
                        );

                        return;
                    }

                    panelPedido.agregarProductoAlCarrito(
                            producto,
                            cantidad
                    );

                    JOptionPane.showMessageDialog(
                            button,
                            "Producto agregado"
                    );

                } catch (NumberFormatException ex) {

                    JOptionPane.showMessageDialog(
                            button,
                            "Ingrese un número válido"
                    );
                }
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

            return "Agregar";
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        txtBuscar = new javax.swing.JTextField();

        btnBuscar = new javax.swing.JButton();

        jScrollPane1 = new javax.swing.JScrollPane();

        tablaProductos = new javax.swing.JTable();

        jLabel1 = new javax.swing.JLabel();

        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(
                javax.swing.WindowConstants.DISPOSE_ON_CLOSE
        );

        btnBuscar.setText("BUSCAR");

        btnBuscar.addActionListener(
                new java.awt.event.ActionListener() {

            public void actionPerformed(
                    java.awt.event.ActionEvent evt) {

                btnBuscarActionPerformed(evt);
            }
        });

        tablaProductos.setModel(
                new javax.swing.table.DefaultTableModel(
                        new Object[][]{},
                        new String[]{}
                )
        );

        jScrollPane1.setViewportView(tablaProductos);

        jLabel1.setText("SELECCIONAR PRODUCTO");

        btnRegresar.setText("REGRESAR");

        btnRegresar.addActionListener(
                new java.awt.event.ActionListener() {

            public void actionPerformed(
                    java.awt.event.ActionEvent evt) {

                btnRegresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout
                = new javax.swing.GroupLayout(getContentPane());

        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING
                )
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtBuscar,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        300,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnBuscar)
                                                .addGap(0, 400, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnRegresar)
                                                .addGap(220, 220, 220)
                                                .addComponent(jLabel1)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING
                )
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnRegresar)
                                        .addComponent(jLabel1))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtBuscar,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnBuscar))
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

    private void btnRegresarActionPerformed(
            java.awt.event.ActionEvent evt) {

        dispose();
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {

            // new ListaProductosSeleccion(null).setVisible(true);
        });
    }

    // Variables declaration
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration
}
