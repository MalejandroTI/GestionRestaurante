/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.Cliente;
import Clases.DetallePedido;
import Clases.EntregaPedido;
import Clases.Pedido;
import Clases.Producto;
import Clases.Rol;
import ClasesEnum.enums.TipoPedido;
import Clases.Usuario;
import ClasesTemporales.ResumenPedido;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import logica.UsuarioJpaController;
import servicios.PedidoService;
import EstilosUI.EstilosUI;
import utilJpa.JPAUtil;

/**
 * Ventana para crear un nuevo pedido. Presentación pura — toda la lógica de
 * negocio delega en PedidoService.
 */
public class PanelPedidoNuevo extends JFrame {

    // ── Dependencias ───────────────────────────────────────────────────────
    private final Usuario usuarioActual;
    private final PedidoService pedidoService;
    private final UsuarioJpaController usuarioController;

    // ── Estado ─────────────────────────────────────────────────────────────
    private Cliente clienteSeleccionado;
    private final List<DetallePedido> carritoTemporal = new ArrayList<>();
    private final DefaultTableModel modeloCarrito = new DefaultTableModel();
    private Rol rolActual;

    // ── Componentes ────────────────────────────────────────────────────────
    private JLabel lblClienteSeleccionado;
    private JLabel lblSubtotal, lblIVA, lblTotal;
    private JComboBox<TipoPedido> cbTipoPedido;
    private JComboBox<Usuario> cbRepartidor;
    private JTextField txtDireccion, txtKm;
    private JPanel panelDelivery;
    private JTable tablaCarrito;
    private JButton btnSeleccionarCliente;
    private JButton btnAgregarProducto;
    private JButton btnConfirmarPedido;
    private JButton btnRegresar;

    // ══════════════════════════════════════════════════════════════════════
    public PanelPedidoNuevo(Usuario usuarioActual, Rol rolActual) {
        this.usuarioActual = usuarioActual;
        this.rolActual = rolActual;

        this.pedidoService = new PedidoService();
        this.usuarioController = new UsuarioJpaController(JPAUtil.getEMF());

        initUI();
        EstilosUI.aplicarEstiloVentana(this, "Crear Pedido", 900, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        configurarTabla();
        cargarTiposPedido();
        cbTipoPedido.addActionListener(e -> onTipoPedidoCambiado());
    }

    // ══════════════════════════════════════════════════════════════════════
    // UI
    // ══════════════════════════════════════════════════════════════════════
    private void initUI() {
        JPanel fondo = new JPanel(new BorderLayout(0, 0));
        fondo.setBackground(EstilosUI.BG_DARK);
        setContentPane(fondo);

        fondo.add(buildHeader(), BorderLayout.NORTH);
        fondo.add(buildCuerpo(), BorderLayout.CENTER);
        fondo.add(buildFooter(), BorderLayout.SOUTH);
    }

    // ── Header ─────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = EstilosUI.panelSecundario();
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        izq.setOpaque(false);
        JLabel icono = new JLabel("🧾");
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        izq.add(icono);
        izq.add(EstilosUI.labelSubtitulo("Crear Pedido"));

        btnRegresar = EstilosUI.botonSecundario("← Regresar");
        btnRegresar.addActionListener(e -> btnRegresarActionPerformed());

        header.add(izq, BorderLayout.WEST);
        header.add(btnRegresar, BorderLayout.EAST);
        return header;
    }

    // ── Cuerpo: formulario izquierda + carrito derecha ─────────────────────
    private JPanel buildCuerpo() {
        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setBackground(EstilosUI.BG_DARK);
        cuerpo.setBorder(new EmptyBorder(16, 20, 0, 20));

        cuerpo.add(buildFormulario(), BorderLayout.WEST);
        cuerpo.add(buildCarrito(), BorderLayout.CENTER);
        return cuerpo;
    }

    // ── Formulario izquierdo ───────────────────────────────────────────────
    private JPanel buildFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(EstilosUI.BG_DARK);
        form.setPreferredSize(new Dimension(300, 0));

        // ─ Sección cliente ─────────────────────────────────────────────────
        form.add(buildSeccion("Cliente"));
        form.add(Box.createVerticalStrut(6));

        JPanel filaCli = new JPanel(new BorderLayout(8, 0));
        filaCli.setOpaque(false);
        filaCli.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        lblClienteSeleccionado = EstilosUI.labelMuted("No seleccionado");
        btnSeleccionarCliente = EstilosUI.botonPrimario("Seleccionar");
        btnSeleccionarCliente.addActionListener(e -> btnSeleccionarClienteActionPerformed());

        filaCli.add(lblClienteSeleccionado, BorderLayout.CENTER);
        filaCli.add(btnSeleccionarCliente, BorderLayout.EAST);
        form.add(filaCli);
        form.add(Box.createVerticalStrut(20));

        // ─ Sección tipo de pedido ───────────────────────────────────────────
        form.add(buildSeccion("Tipo de pedido"));
        form.add(Box.createVerticalStrut(6));

        cbTipoPedido = new JComboBox<>();
        EstilosUI.aplicarEstiloCombo(cbTipoPedido);
        cbTipoPedido.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(cbTipoPedido);
        form.add(Box.createVerticalStrut(12));

        // ─ Panel delivery (oculto por defecto) ─────────────────────────────
        form.add(buildPanelDelivery());
        form.add(Box.createVerticalStrut(20));

        // ─ Resumen de totales ───────────────────────────────────────────────
        form.add(buildResumen());

        return form;
    }

    private JPanel buildPanelDelivery() {
        panelDelivery = EstilosUI.cardRedondeada(10);
        panelDelivery.setLayout(new BoxLayout(panelDelivery, BoxLayout.Y_AXIS));
        panelDelivery.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosUI.ACCENT, 1),
                new EmptyBorder(12, 12, 12, 12)));
        panelDelivery.setVisible(false);
        panelDelivery.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel titDelivery = EstilosUI.labelMuted("Datos de entrega");
        titDelivery.setAlignmentX(LEFT_ALIGNMENT);
        titDelivery.setForeground(EstilosUI.ACCENT);

        // Repartidor
        JLabel lblRep = EstilosUI.labelMuted("Repartidor");
        lblRep.setAlignmentX(LEFT_ALIGNMENT);
        cbRepartidor = new JComboBox<>();
        EstilosUI.aplicarEstiloCombo(cbRepartidor);
        cbRepartidor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cbRepartidor.setAlignmentX(LEFT_ALIGNMENT);

        // Dirección
        JLabel lblDir = EstilosUI.labelMuted("Dirección de entrega");
        lblDir.setAlignmentX(LEFT_ALIGNMENT);
        txtDireccion = EstilosUI.campoTexto();
        txtDireccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txtDireccion.setAlignmentX(LEFT_ALIGNMENT);

        // Km
        JLabel lblKm = EstilosUI.labelMuted("Distancia (km)");
        lblKm.setAlignmentX(LEFT_ALIGNMENT);
        txtKm = EstilosUI.campoTexto();
        txtKm.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txtKm.setAlignmentX(LEFT_ALIGNMENT);

        panelDelivery.add(titDelivery);
        panelDelivery.add(Box.createVerticalStrut(8));
        panelDelivery.add(lblRep);
        panelDelivery.add(Box.createVerticalStrut(4));
        panelDelivery.add(cbRepartidor);
        panelDelivery.add(Box.createVerticalStrut(8));
        panelDelivery.add(lblDir);
        panelDelivery.add(Box.createVerticalStrut(4));
        panelDelivery.add(txtDireccion);
        panelDelivery.add(Box.createVerticalStrut(8));
        panelDelivery.add(lblKm);
        panelDelivery.add(Box.createVerticalStrut(4));
        panelDelivery.add(txtKm);

        return panelDelivery;
    }

    private JPanel buildResumen() {
        JPanel card = EstilosUI.card();
        card.setLayout(new GridLayout(4, 2, 6, 6));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        lblSubtotal = EstilosUI.labelNormal("0.00");
        lblIVA = EstilosUI.labelNormal("0.00");
        lblTotal = EstilosUI.labelNormal("0.00");
        lblTotal.setForeground(EstilosUI.SUCCESS);
        lblTotal.setFont(EstilosUI.FONT_BOLD);

        card.add(EstilosUI.labelMuted("RESUMEN"));
        card.add(new JLabel());
        card.add(EstilosUI.labelMuted("Subtotal:"));
        card.add(lblSubtotal);
        card.add(EstilosUI.labelMuted("IVA:"));
        card.add(lblIVA);
        card.add(EstilosUI.labelMuted("Total:"));
        card.add(lblTotal);

        return card;
    }

    // ── Carrito derecho ────────────────────────────────────────────────────
    private JPanel buildCarrito() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(EstilosUI.BG_DARK);

        // Encabezado carrito
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(EstilosUI.labelSubtitulo("Carrito"), BorderLayout.WEST);
        btnAgregarProducto = EstilosUI.botonPrimario("+ Agregar producto");
        btnAgregarProducto.addActionListener(e -> btnAgregarProductoActionPerformed());
        encabezado.add(btnAgregarProducto, BorderLayout.EAST);

        // Tabla
        tablaCarrito = new JTable(modeloCarrito);
        EstilosUI.aplicarEstiloTabla(tablaCarrito);

        JScrollPane scroll = new JScrollPane(tablaCarrito);
        EstilosUI.aplicarEstiloScroll(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(EstilosUI.BORDER, 1));

        panel.add(encabezado, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Footer ─────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(EstilosUI.BG_DARK);
        footer.setBorder(new EmptyBorder(12, 20, 16, 20));

        JLabel hint = EstilosUI.labelMuted("Verifica el pedido antes de confirmar");

        btnConfirmarPedido = EstilosUI.botonExito("✓ Confirmar pedido");
        btnConfirmarPedido.addActionListener(e -> btnConfirmarPedidoActionPerformed());

        footer.add(hint, BorderLayout.WEST);
        footer.add(btnConfirmarPedido, BorderLayout.EAST);
        return footer;
    }

    // ── Helper sección ─────────────────────────────────────────────────────
    private JPanel buildSeccion(String titulo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel lbl = EstilosUI.labelMuted(titulo.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        p.add(lbl, BorderLayout.WEST);
        JSeparator sep = EstilosUI.separador();
        p.add(sep, BorderLayout.SOUTH);
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // LÓGICA DE FORMULARIO  (solo coordina — sin cálculos de negocio)
    // ══════════════════════════════════════════════════════════════════════
    private void configurarTabla() {
        modeloCarrito.addColumn("Producto");
        modeloCarrito.addColumn("Cantidad");
        modeloCarrito.addColumn("P. Unitario");
        modeloCarrito.addColumn("Subtotal");
        tablaCarrito.setModel(modeloCarrito);
    }

    private void cargarTiposPedido() {
        cbTipoPedido.removeAllItems();
        for (TipoPedido t : TipoPedido.values()) {
            cbTipoPedido.addItem(t);
        }
    }

    private void onTipoPedidoCambiado() {
        boolean esDelivery = cbTipoPedido.getSelectedItem() == TipoPedido.DELIVERY;
        panelDelivery.setVisible(esDelivery);
        if (esDelivery) {
            cargarRepartidores();
        }
        pack();
        revalidate();
    }

    /**
     * Ahora sí completo — carga repartidores en el combo con renderer de
     * nombre. Lógica de consulta delegada a
     * UsuarioJpaController.obtenerRepartidores().
     */
    private void cargarRepartidores() {
        cbRepartidor.removeAllItems();

        cbRepartidor.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Usuario) {              // ← sin pattern matching
                    Usuario u = (Usuario) value;             // ← cast explícito
                    setText(u.getNombre() + " " + u.getApellido());
                }
                setBackground(isSelected ? EstilosUI.BG_CARD : EstilosUI.BG_PANEL);
                setForeground(EstilosUI.TEXT_PRIMARY);
                return this;
            }
        });

        List<Usuario> repartidores = usuarioController.obtenerRepartidores();
        if (repartidores == null || repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay repartidores disponibles.",
                    "Sin repartidores", JOptionPane.WARNING_MESSAGE);
            return;
        }
        repartidores.forEach(cbRepartidor::addItem);
    }

    // ── API pública para ventanas hijas ────────────────────────────────────
    /**
     * Llamado por ListaClientesSeleccion
     */
    public void setClienteSeleccionado(Cliente cliente) {
        this.clienteSeleccionado = cliente;
        lblClienteSeleccionado.setText(cliente.getNombre() + " " + cliente.getApellido());
        lblClienteSeleccionado.setForeground(EstilosUI.TEXT_PRIMARY);
    }

    /**
     * Llamado por ListaProductosSeleccion. La construcción del DetallePedido se
     * delega aquí porque requiere datos del formulario (producto + cantidad) —
     * no es lógica de negocio pura. El cálculo de subtotales/IVA/total sí va al
     * servicio vía calcularResumen().
     */
    public void agregarProductoAlCarrito(Producto producto, int cantidad) {
        // Lógica delegada al servicio
        pedidoService.agregarOActualizarDetalle(carritoTemporal, producto, cantidad);
        // Vista solo refresca
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

    /**
     * Totales calculados por el servicio — la vista solo muestra el resultado
     */
    private void calcularTotales() {
        if (carritoTemporal.isEmpty()) {
            lblSubtotal.setText("0.00");
            lblIVA.setText("0.00");
            lblTotal.setText("0.00");
            return;
        }
        ResumenPedido resumen = pedidoService.calcularResumen(carritoTemporal);
        lblSubtotal.setText(resumen.getSubtotal().toPlainString());
        lblIVA.setText(resumen.getIva().toPlainString());
        lblTotal.setText(resumen.getTotal().toPlainString());
    }

    // ── Acciones de botones ────────────────────────────────────────────────
    private void btnSeleccionarClienteActionPerformed() {
        new ListaClientesSeleccion(this).setVisible(true);
    }

    private void btnAgregarProductoActionPerformed() {
        new ListaProductosSeleccion(this).setVisible(true);
    }

    private void btnConfirmarPedidoActionPerformed() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (carritoTemporal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agrega al menos un producto.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Pedido pedido = new Pedido();
            TipoPedido tipo = (TipoPedido) cbTipoPedido.getSelectedItem();
            pedido.setTipoPedido(tipo);

            EntregaPedido entrega = null;

            if (tipo == TipoPedido.DELIVERY) {
                Usuario repartidor = (Usuario) cbRepartidor.getSelectedItem();
                if (repartidor == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un repartidor.");
                    return;
                }
                String dir = txtDireccion.getText().trim();
                if (dir.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Ingresa la dirección de entrega.");
                    return;
                }
                String kmTxt = txtKm.getText().trim();
                BigDecimal km;
                try {
                    km = new BigDecimal(kmTxt);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El valor de km no es válido.");
                    return;
                }
                entrega = new EntregaPedido();
                entrega.setDireccionEntrega(dir);
                entrega.setDistanciaKm(km);
                entrega.setIdUsuarioRepartidor(repartidor);
            }

            Pedido creado = pedidoService.crearPedido(
                    pedido, carritoTemporal, entrega, usuarioActual, clienteSeleccionado);

            JTextArea area = new JTextArea(
                    "✓ Pedido creado correctamente\n\n"
                    + "Código: " + creado.getCodigo() + "\n\n"
                    + "Selecciona el texto y presiona Ctrl+C para copiar.");
            area.setEditable(false);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);

            JOptionPane.showMessageDialog(this, area,
                    "Pedido creado", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear el pedido: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        carritoTemporal.clear();
        actualizarTablaCarrito();
        calcularTotales();
        clienteSeleccionado = null;
        lblClienteSeleccionado.setText("No seleccionado");
        lblClienteSeleccionado.setForeground(EstilosUI.TEXT_MUTED);
        panelDelivery.setVisible(false);
        txtDireccion.setText("");
        txtKm.setText("");
        cbRepartidor.removeAllItems();
        cbTipoPedido.setSelectedIndex(0);
    }

    private void btnRegresarActionPerformed() {
        new PanelCajero(usuarioActual, rolActual).setVisible(true);
        dispose();
    }
}
