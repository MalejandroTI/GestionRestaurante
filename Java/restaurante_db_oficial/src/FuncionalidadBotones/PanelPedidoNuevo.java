/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.Cliente;
import Clases.DetallePedido;
import Clases.EntregaPedido;
import Clases.Factura;
import Clases.Pedido;
import Clases.Producto;
import ClasesEnum.enums.TipoPedido;
import Clases.Usuario;
import ClasesTemporales.ResumenPedido;
import CreacionDocsPdf.PdfFacturaService;
import CreacionDocsPdf.PdfReciboPedido;
import FuncionalidadBotones.ListaClientesSeleccion;
import FuncionalidadBotones.ListaProductosSeleccion;
import PresentacionJFRAME.PanelCajero;
import java.awt.HeadlessException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import logica.UsuarioJpaController;
import servicios.FacturaService;
import servicios.PedidoService;

public class PanelPedidoNuevo extends javax.swing.JFrame {

    private final Usuario usuarioActual;

    private Cliente clienteSeleccionado;
    private UsuarioJpaController controladorUsuario;

    private final List<DetallePedido> carritoTemporal;

    private final DefaultTableModel modeloCarrito;
    private javax.swing.JLabel lblResumen;
    private final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory(
                    "restaurante_db_oficialPU"
            );
    private final PedidoService pedidoService;
    private final UsuarioJpaController usuarioController;
    private final FacturaService facturaService;

    public PanelPedidoNuevo(Usuario usuarioActual) {

        initComponents();

        this.usuarioActual = usuarioActual;

        carritoTemporal = new ArrayList<>();

        modeloCarrito = new DefaultTableModel();

        tablaCarrito.setModel(modeloCarrito);
        pedidoService = new PedidoService(emf);
        this.usuarioController = new UsuarioJpaController(emf);
        this.facturaService = new FacturaService(emf);

        configurarTabla();

        cargarTiposPedido();
        cbTipoPedido.addActionListener(e -> {

            TipoPedido seleccionado
                    = (TipoPedido) cbTipoPedido.getSelectedItem();

            boolean esDelivery
                    = seleccionado == TipoPedido.DELIVERY;

            panelDelivery.setVisible(esDelivery);

            if (esDelivery) {

                cargarRepartidores();
            }

            pack();
        });
    }

    private void cargarRepartidores() {

        cbRepartidor.removeAllItems();

        cbRepartidor.setRenderer(
                new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list,
                    Object value, int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list, value, index,
                        isSelected, cellHasFocus);

                if (value instanceof Usuario) {
                    Usuario u = (Usuario) value;
                    setText(u.getNombre() + " " + u.getApellido());
                }
                return this;
            }
        }
        );

        List<Usuario> repartidores = usuarioController.obtenerRepartidores();

        if (repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay repartidores disponibles"
            );
            return;
        }

        for (Usuario u : repartidores) {
            cbRepartidor.addItem(u);
        }
    }

    private void configurarTabla() {

        modeloCarrito.addColumn("Producto");
        modeloCarrito.addColumn("Cantidad");
        modeloCarrito.addColumn("P. Unitario");
        modeloCarrito.addColumn("Subtotal");
    }

    private void cargarTiposPedido() {

        cbTipoPedido.removeAllItems();

        for (TipoPedido tipo : TipoPedido.values()) {

            cbTipoPedido.addItem(tipo);
        }
    }

    // ==================================================
    // CLIENTE
    // ==================================================
    public void setClienteSeleccionado(Cliente cliente) {

        this.clienteSeleccionado = cliente;

        lblClienteSeleccionado.setText(
                cliente.getNombre()
                + " "
                + cliente.getApellido()
        );
    }

    // ==================================================
    // CARRITO
    // ==================================================
    public void agregarProductoAlCarrito(
            Producto producto,
            int cantidad) {

        DetallePedido detalle = new DetallePedido();

        detalle.setIdProducto(producto);

        detalle.setCantidad(cantidad);

        detalle.setPrecioUnitario(producto.getPrecio());

        BigDecimal subtotal
                = producto.getPrecio().multiply(
                        BigDecimal.valueOf(cantidad)
                );

        detalle.setSubtotal(subtotal);

        carritoTemporal.add(detalle);

        actualizarTablaCarrito();

        calcularTotales();
    }

    private void actualizarTablaCarrito() {

        modeloCarrito.setRowCount(0);

        for (DetallePedido d : carritoTemporal) {

            modeloCarrito.addRow(new Object[]{
                d.getIdProducto().getNombre(),
                d.getCantidad(),
                d.getPrecioUnitario(),
                d.getSubtotal()
            });
        }
    }

    private void calcularTotales() {

        ResumenPedido resumen
                = pedidoService.calcularResumen(
                        carritoTemporal
                );

        lblSubtotal.setText(
                resumen.getSubtotal().toString()
        );

        lblIVA.setText(
                resumen.getIva().toString()
        );

        lblTotal.setText(
                resumen.getTotal().toString()
        );
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panelDelivery = new javax.swing.JPanel();
        panelDelivery.setLayout(new javax.swing.BoxLayout(
                panelDelivery, javax.swing.BoxLayout.Y_AXIS));
        panelDelivery.setBorder(javax.swing.BorderFactory
                .createTitledBorder("Datos Delivery"));
        panelDelivery.setVisible(false);

        lblRepartidor = new javax.swing.JLabel("REPARTIDOR:");
        cbRepartidor = new javax.swing.JComboBox<>();

        lblDireccion = new javax.swing.JLabel("DIRECCIÓN:");
        txtDireccion = new javax.swing.JTextField(20);

        lblKm = new javax.swing.JLabel("KM TOTAL:");
        txtKm = new javax.swing.JTextField(5);

        panelDelivery.add(lblRepartidor);
        panelDelivery.add(cbRepartidor);
        panelDelivery.add(javax.swing.Box.createVerticalStrut(8));
        panelDelivery.add(lblDireccion);
        panelDelivery.add(txtDireccion);
        panelDelivery.add(javax.swing.Box.createVerticalStrut(8));
        panelDelivery.add(lblKm);
        panelDelivery.add(txtKm);
        btnRegresar = new javax.swing.JButton();

        jLabel1 = new javax.swing.JLabel();

        jLabel2 = new javax.swing.JLabel();

        lblClienteSeleccionado = new javax.swing.JLabel();

        btnSeleccionarCliente = new javax.swing.JButton();

        jLabel3 = new javax.swing.JLabel();

        cbTipoPedido = new javax.swing.JComboBox<>();

        btnAgregarProducto = new javax.swing.JButton();

        jScrollPane1 = new javax.swing.JScrollPane();

        tablaCarrito = new javax.swing.JTable();

        jLabel4 = new javax.swing.JLabel();

        jLabel5 = new javax.swing.JLabel();

        jLabel6 = new javax.swing.JLabel();

        lblSubtotal = new javax.swing.JLabel();

        lblIVA = new javax.swing.JLabel();

        lblTotal = new javax.swing.JLabel();

        btnConfirmarPedido = new javax.swing.JButton();
        lblResumen = new javax.swing.JLabel();

        lblResumen.setText("RESUMEN");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnRegresar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                btnRegresarActionPerformed(evt);
            }
        });

        jLabel1.setText("CREAR PEDIDO");

        jLabel2.setText("CLIENTE:");

        lblClienteSeleccionado.setText("NO SELECCIONADO");

        btnSeleccionarCliente.setText("SELECCIONAR");

        btnSeleccionarCliente.addActionListener(
                new java.awt.event.ActionListener() {

            public void actionPerformed(
                    java.awt.event.ActionEvent evt) {

                btnSeleccionarClienteActionPerformed(evt);
            }
        });

        jLabel3.setText("TIPO PEDIDO:");

        btnAgregarProducto.setText("AGREGAR PRODUCTO");

        btnAgregarProducto.addActionListener(
                new java.awt.event.ActionListener() {

            public void actionPerformed(
                    java.awt.event.ActionEvent evt) {

                btnAgregarProductoActionPerformed(evt);
            }
        });

        tablaCarrito.setModel(
                new javax.swing.table.DefaultTableModel(
                        new Object[][]{},
                        new String[]{}
                )
        );

        jScrollPane1.setViewportView(tablaCarrito);

        jLabel4.setText("SUBTOTAL:");

        jLabel5.setText("IVA:");

        jLabel6.setText("TOTAL:");

        lblSubtotal.setText("0.00");

        lblIVA.setText("0.00");

        lblTotal.setText("0.00");

        btnConfirmarPedido.setText("CONFIRMAR PEDIDO");

        btnConfirmarPedido.addActionListener(
                new java.awt.event.ActionListener() {

            public void actionPerformed(
                    java.awt.event.ActionEvent evt) {

                btnConfirmarPedidoActionPerformed(evt);
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
                                        // ======================
                                        // TABLA
                                        // ======================
                                        .addComponent(jScrollPane1)
                                        // ======================
                                        // HEADER
                                        // ======================
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnRegresar)
                                                .addGap(250, 250, 250)
                                                .addComponent(jLabel1)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        // ======================
                                        // CLIENTE + TIPO
                                        // ======================
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel3))
                                                .addGap(20, 20, 20)
                                                .addGroup(layout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                        // CLIENTE
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(lblClienteSeleccionado)
                                                                .addGap(30, 30, 30)
                                                                .addComponent(btnSeleccionarCliente))
                                                        // TIPO + DELIVERY
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(cbTipoPedido,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        200,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(panelDelivery,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        // ======================
                                        // RESUMEN
                                        // ======================
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnAgregarProducto)
                                                .addPreferredGap(
                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        300,
                                                        Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblResumen)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel4)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(lblSubtotal))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel5)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(lblIVA))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel6)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(lblTotal)))
                                        )
                                )
                                .addContainerGap())

                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                layout.createSequentialGroup()
                                        .addContainerGap(
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                        .addComponent(btnConfirmarPedido)
                                        .addGap(40, 40, 40))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING
                )
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                // HEADER
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnRegresar)
                                        .addComponent(jLabel1))
                                .addGap(30, 30, 30)
                                // CLIENTE
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(lblClienteSeleccionado)
                                        .addComponent(btnSeleccionarCliente))
                                .addGap(20, 20, 20)
                                // TIPO + DELIVERY
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(cbTipoPedido,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(panelDelivery,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                // RESUMEN
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnAgregarProducto)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblResumen)
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel4)
                                                        .addComponent(lblSubtotal))
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(lblIVA))
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel6)
                                                        .addComponent(lblTotal))))
                                .addGap(20, 20, 20)
                                // TABLA
                                .addComponent(jScrollPane1,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        300,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                // BOTON FINAL
                                .addComponent(btnConfirmarPedido)
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }

    private void btnSeleccionarClienteActionPerformed(
            java.awt.event.ActionEvent evt) {

        ListaClientesSeleccion ventana
                = new ListaClientesSeleccion(this);

        ventana.setLocationRelativeTo(this);

        ventana.setVisible(true);
    }

    private void btnAgregarProductoActionPerformed(
            java.awt.event.ActionEvent evt) {

        ListaProductosSeleccion ventana;
        ventana = new ListaProductosSeleccion(this);

        ventana.setLocationRelativeTo(this);

        ventana.setVisible(true);
    }

    private void btnConfirmarPedidoActionPerformed(
            java.awt.event.ActionEvent evt) {

        try {

            // =========================
            // VALIDAR CLIENTE
            // =========================
            if (clienteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente");
                return;
            }

            // =========================
            // VALIDAR CARRITO
            // =========================
            if (carritoTemporal.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Agregue productos");
                return;
            }

            // =========================
            // CREAR PEDIDO
            // =========================
            Pedido pedido = new Pedido();

            TipoPedido tipo = (TipoPedido) cbTipoPedido.getSelectedItem();
            pedido.setTipoPedido(tipo);

            
            
            
            EntregaPedido entrega = null;

            if (tipo == TipoPedido.DELIVERY) {

                Usuario repartidor = (Usuario) cbRepartidor.getSelectedItem();
                if (repartidor == null) {
                    JOptionPane.showMessageDialog(this, "Seleccione un repartidor");
                    return;
                }

                String direccion = txtDireccion.getText().trim();
                if (direccion.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Ingrese la dirección");
                    return;
                }

                String kmTexto = txtKm.getText().trim();
                if (kmTexto.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Ingrese los km");
                    return;
                }

                BigDecimal km;
                try {
                    km = new BigDecimal(kmTexto);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Km inválido");
                    return;
                }

                entrega = new EntregaPedido();
                entrega.setDireccionEntrega(direccion);
                entrega.setDistanciaKm(km);
                entrega.setIdUsuarioRepartidor(repartidor);
            }

       
            Pedido pedidoCreado = pedidoService.crearPedido(
                    pedido,
                    carritoTemporal,
                    entrega,
                    usuarioActual,
                    clienteSeleccionado
            );

            JTextArea area = new JTextArea(
                    "Pedido creado correctamente\n\n"
                    + "Código: " + pedidoCreado.getCodigo() + "\n\n"
                    + "Selecciona el texto y presiona Ctrl + C para copiar"
            );

            area.setEditable(false);
            area.setFocusable(true);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);

            JOptionPane.showMessageDialog(
                    this,
                    area,
                    "Pedido Creado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al crear el pedido: " + e.getMessage()
            );
        }
    }

    private void limpiarFormulario() {

        carritoTemporal.clear();
        actualizarTablaCarrito();
        calcularTotales();

        clienteSeleccionado = null;
        lblClienteSeleccionado.setText("NO SELECCIONADO");

        panelDelivery.setVisible(false);
        txtDireccion.setText("");
        txtKm.setText("");
        cbRepartidor.removeAllItems();
        cbTipoPedido.setSelectedIndex(0);
    }

    private void btnRegresarActionPerformed(
            java.awt.event.ActionEvent evt) {

        PanelCajero panel
                = new PanelCajero(usuarioActual);

        panel.setLocationRelativeTo(null);

        panel.setVisible(true);

        dispose();
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {

        });
    }

    // Variables declaration
    private javax.swing.JLabel lblRepartidor;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblKm;
    private javax.swing.JComboBox<Usuario> cbRepartidor;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtKm;
    private javax.swing.JPanel panelDelivery;
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnConfirmarPedido;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnSeleccionarCliente;
    private javax.swing.JComboBox<TipoPedido> cbTipoPedido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblClienteSeleccionado;
    private javax.swing.JLabel lblIVA;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tablaCarrito;
    // End of variables declaration
}
