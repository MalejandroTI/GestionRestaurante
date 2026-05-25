/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.Factura;
import Clases.Pedido;
import Clases.Rol;
import Clases.Usuario;
import ClasesEnum.enums.EstadoPedido;
import CreacionDocsPdf.PdfFacturaService;
import CreacionDocsPdf.PdfReciboPedido;
import EstilosUI.EstilosUI;
import java.awt.*;
import java.math.BigDecimal;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import servicios.FacturaService;
import servicios.PedidoService;

public class PanelPedidoDetalle extends JPanel {

    // ── Dependencias ───────────────────────────────────────────────────────
    private final Pedido            pedido;
    private final Rol               rolActual;       // rol con el que el usuario entró
    private final Usuario           usuarioActual;
    private final FacturaService    facturaService;
    private final PdfFacturaService pdfFacturaService;
    private final PdfReciboPedido   pdfReciboPedido;
    private final PedidoService     pedidoService;

    // ── Estado ─────────────────────────────────────────────────────────────
    private Factura facturaActual = null;

    // ── Componentes ────────────────────────────────────────────────────────
    private JLabel lblCodigo, lblCliente, lblEstado, lblTipo;
    private JLabel lblUsuario, lblFecha, lblRepartidor, lblDireccion, lblTotal;
    private JTable            tblDetalle;
    private DefaultTableModel model;
    private JButton btnCrearFactura;
    private JButton btnVerFactura;      // deshabilitado hasta que haya factura
    private JButton btnRecibo;
    private JButton btnCambiarEstado;
    private JButton btnRegresar;
    private JLabel  lblFacturaStatus;

    // ══════════════════════════════════════════════════════════════════════
    // Constructor completo — con rol y usuario para cambiar estado
    // ══════════════════════════════════════════════════════════════════════
    public PanelPedidoDetalle(Pedido pedido, Rol rolActual, Usuario usuarioActual) {
        this.pedido            = pedido;
        this.rolActual         = rolActual;
        this.usuarioActual     = usuarioActual;
        this.facturaService    = new FacturaService();
        this.pdfFacturaService = new PdfFacturaService();
        this.pdfReciboPedido   = new PdfReciboPedido();
        this.pedidoService     = new PedidoService();

        initUI();
        cargarDatos();
        initEvents();
        verificarFacturaExistente();
    }

    // Constructor sin rol — para vistas de solo lectura (ej: historial delivery)
    public PanelPedidoDetalle(Pedido pedido) {
        this(pedido, null, null);
    }

    // ── UI ─────────────────────────────────────────────────────────────────
    private void initUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(EstilosUI.BG_DARK);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabla(),  BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(EstilosUI.BG_DARK);
        outer.setBorder(new EmptyBorder(16, 20, 8, 20));

        JLabel titulo = EstilosUI.labelTitulo("Detalle del Pedido");
        titulo.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel card = new JPanel(new GridLayout(0, 4, 20, 6));
        card.setBackground(EstilosUI.BG_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosUI.BORDER, 1),
                new EmptyBorder(12, 16, 12, 16)));

        lblCodigo     = infoLabel();
        lblCliente    = infoLabel();
        lblEstado     = infoLabel();
        lblTipo       = infoLabel();
        lblUsuario    = infoLabel();
        lblFecha      = infoLabel();
        lblRepartidor = infoLabel();
        lblDireccion  = infoLabel();
        lblTotal      = infoLabel();

        card.add(muted("Código:"));      card.add(lblCodigo);
        card.add(muted("Cliente:"));     card.add(lblCliente);
        card.add(muted("Estado:"));      card.add(lblEstado);
        card.add(muted("Tipo:"));        card.add(lblTipo);
        card.add(muted("Cajero:"));      card.add(lblUsuario);
        card.add(muted("Fecha/Hora:")); card.add(lblFecha);
        card.add(muted("Repartidor:")); card.add(lblRepartidor);
        card.add(muted("Dirección:"));  card.add(lblDireccion);
        card.add(muted("Total:"));       card.add(lblTotal);
        card.add(new JLabel());

        outer.add(titulo, BorderLayout.NORTH);
        outer.add(card,   BorderLayout.CENTER);
        return outer;
    }

    private JScrollPane buildTabla() {
        model = new DefaultTableModel(
                new Object[]{"Producto", "Cantidad", "Precio Unit.", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblDetalle = new JTable(model);
        tblDetalle.setBackground(EstilosUI.BG_ROW_EVEN);
        tblDetalle.setForeground(EstilosUI.TEXT_PRIMARY);
        tblDetalle.setFont(EstilosUI.FONT_NORMAL);
        tblDetalle.setRowHeight(30);
        tblDetalle.setShowVerticalLines(false);
        tblDetalle.setGridColor(EstilosUI.BORDER);
        tblDetalle.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = tblDetalle.getTableHeader();
        header.setBackground(EstilosUI.BG_PANEL);
        header.setForeground(EstilosUI.TEXT_MUTED);
        header.setFont(EstilosUI.FONT_BOLD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, EstilosUI.BORDER));
        header.setReorderingAllowed(false);

        tblDetalle.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(row % 2 == 0 ? EstilosUI.BG_ROW_EVEN : EstilosUI.BG_ROW_ODD);
                setForeground(EstilosUI.TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                setFont(EstilosUI.FONT_NORMAL);
                setHorizontalAlignment(col >= 1 ? RIGHT : LEFT);
                return this;
            }
        });

        int[] widths = {200, 80, 110, 110};
        for (int i = 0; i < widths.length; i++)
            tblDetalle.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(tblDetalle);
        scroll.setBackground(EstilosUI.BG_DARK);
        scroll.getViewport().setBackground(EstilosUI.BG_ROW_EVEN);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(8, 20, 0, 20),
                BorderFactory.createLineBorder(EstilosUI.BORDER, 1)));
        return scroll;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(EstilosUI.BG_DARK);
        footer.setBorder(new EmptyBorder(10, 20, 16, 20));

        lblFacturaStatus = new JLabel("Sin factura generada");
        lblFacturaStatus.setForeground(EstilosUI.TEXT_MUTED);
        lblFacturaStatus.setFont(EstilosUI.FONT_MUTED);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setBackground(EstilosUI.BG_DARK);

        btnRegresar      = buildButton("Regresar",             EstilosUI.BG_PANEL,  EstilosUI.TEXT_MUTED);
        btnRecibo        = buildButton("Imprimir Recibo",       EstilosUI.BG_PANEL,  EstilosUI.ACCENT);
        btnCambiarEstado = buildButton("Cambiar estado",        EstilosUI.BG_PANEL,  EstilosUI.WARNING);
        btnCrearFactura  = buildButton("Crear/Buscar Factura",  EstilosUI.SUCCESS,   EstilosUI.BG_DARK);
        btnVerFactura    = buildButton("Ver Factura PDF",       EstilosUI.ACCENT,    EstilosUI.BG_DARK);
        btnVerFactura.setEnabled(false);   // se activa con activarBotonVerFactura()

        // Si no hay rol (solo lectura) ocultar botones de acción
        btnCambiarEstado.setVisible(rolActual != null);
        btnCrearFactura.setVisible(rolActual != null);

        botones.add(btnRegresar);
        botones.add(btnRecibo);
        botones.add(btnCambiarEstado);
        botones.add(btnCrearFactura);
        botones.add(btnVerFactura);

        footer.add(lblFacturaStatus, BorderLayout.WEST);
        footer.add(botones,          BorderLayout.EAST);
        return footer;
    }

    // ── Carga de datos ─────────────────────────────────────────────────────
    private void cargarDatos() {
        lblCodigo.setText(pedido.getCodigo());
        lblCliente.setText(pedido.getIdCliente() != null
                ? pedido.getIdCliente().getNombre() : "—");

        lblEstado.setText(pedido.getEstado().name());
        lblEstado.setForeground(EstilosUI.colorDeEstado(pedido.getEstado().name()));

        String tipo = pedido.getTipoPedido().name();
        lblTipo.setText(tipo);
        lblTipo.setForeground(EstilosUI.colorDeEstado(tipo));

        lblUsuario.setText(pedido.getIdUsuario() != null
                ? pedido.getIdUsuario().getNombre() : "—");
        lblFecha.setText(pedido.getFechaHora() != null
                ? pedido.getFechaHora().toString() : "—");

        if ("DELIVERY".equalsIgnoreCase(tipo) && pedido.getEntregaPedido() != null) {
            lblRepartidor.setText(
                    pedido.getEntregaPedido().getIdUsuarioRepartidor() != null
                    ? pedido.getEntregaPedido().getIdUsuarioRepartidor().getNombre()
                    : "Sin asignar");
            lblDireccion.setText(
                    pedido.getEntregaPedido().getDireccionEntrega() != null
                    ? pedido.getEntregaPedido().getDireccionEntrega()
                    : "Sin dirección");
        } else {
            lblRepartidor.setText("No aplica");
            lblDireccion.setText("No aplica");
        }

        cargarDetalle();
    }

    private void cargarDetalle() {
        model.setRowCount(0);
        if (pedido.getDetallePedidoCollection() == null) return;

        BigDecimal totalCalculado = BigDecimal.ZERO;
        for (var d : pedido.getDetallePedidoCollection()) {
            String     producto = d.getIdProducto() != null
                    ? d.getIdProducto().getNombre() : "Sin producto";
            int        cantidad = d.getCantidad();
            BigDecimal precio   = d.getPrecioUnitario();
            // Usa getSubtotal() si está persistido; recalcula solo si es null
            BigDecimal subtotal = d.getSubtotal() != null
                    ? d.getSubtotal()
                    : precio.multiply(BigDecimal.valueOf(cantidad));
            totalCalculado = totalCalculado.add(subtotal);
            model.addRow(new Object[]{producto, cantidad, precio, subtotal});
        }
        lblTotal.setText("$ " + totalCalculado.toPlainString());
        lblTotal.setFont(EstilosUI.FONT_BOLD);
        lblTotal.setForeground(EstilosUI.SUCCESS);
    }

    // ── Factura ────────────────────────────────────────────────────────────

    private void verificarFacturaExistente() {
        try {
            Factura f = facturaService.buscarFacturaConDetalles(pedido.getIdPedido());
            if (f != null) {
                facturaActual = f;
                activarBotonVerFactura("Factura existente (#" + f.getNumero() + ")");
            }
        } catch (Exception ignored) { }
    }

    /**
     * Método centralizado — único punto que habilita btnVerFactura.
     * Lo llaman tanto verificarFacturaExistente() como accionCrearFactura(),
     * garantizando que el botón siempre quede activo tras crear una factura nueva.
     */
    private void activarBotonVerFactura(String mensaje) {
        btnVerFactura.setEnabled(true);
        lblFacturaStatus.setText("✓ " + mensaje);
        lblFacturaStatus.setForeground(EstilosUI.SUCCESS);
    }

    // ── Eventos ────────────────────────────────────────────────────────────
    private void initEvents() {
        btnCrearFactura.addActionListener(e  -> accionCrearFactura());
        btnVerFactura.addActionListener(e    -> accionVerFacturaPdf());
        btnRecibo.addActionListener(e        -> accionVerRecibo());
        btnCambiarEstado.addActionListener(e -> accionCambiarEstado());
        btnRegresar.addActionListener(e      -> regresar());
    }

    private void accionCrearFactura() {
        try {
            facturaActual = facturaService.obtenerOCrearFactura(pedido);
            // ✅ activarBotonVerFactura es el único lugar que llama setEnabled(true)
            activarBotonVerFactura("Factura lista (#" + facturaActual.getNumero() + ")");
            JOptionPane.showMessageDialog(this,
                    "Factura disponible. Usa 'Ver Factura PDF' para abrirla.",
                    "Factura", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear/buscar la factura: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionVerFacturaPdf() {
        if (facturaActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero crea o carga la factura.",
                    "Sin factura", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            pdfFacturaService.generarPdf(facturaActual);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error generando PDF: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionVerRecibo() {
        try {
            pdfReciboPedido.generarRecibo(pedido);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error generando recibo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionCambiarEstado() {
        if (rolActual == null || usuarioActual == null) return;

        EstadoPedido[] opciones = estadosPermitidosPorRol();
        if (opciones.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Tu rol no tiene permisos para cambiar estados.",
                    "Sin permisos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EstadoPedido seleccionado = (EstadoPedido) JOptionPane.showInputDialog(
                this,
                "Estado actual: " + pedido.getEstado()
                + "\nSelecciona el nuevo estado:",
                "Cambiar estado",
                JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        if (seleccionado == null) return;

        try {
            pedidoService.cambiarEstado(pedido.getIdPedido(), seleccionado, usuarioActual, rolActual);
            // Actualiza el label en tiempo real sin recargar el panel
            lblEstado.setText(seleccionado.name());
            lblEstado.setForeground(EstilosUI.colorDeEstado(seleccionado.name()));

            JOptionPane.showMessageDialog(this,
                    "Estado actualizado a: " + seleccionado,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(),
                    "No permitido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private EstadoPedido[] estadosPermitidosPorRol() {
        if (rolActual == null) return new EstadoPedido[0];
        return switch (rolActual.getNombre()) {
            case "Cajero"     -> new EstadoPedido[]{
                EstadoPedido.EN_PREPARACION, EstadoPedido.ENTREGADO, EstadoPedido.CANCELADO};
            case "Cocinero"   -> new EstadoPedido[]{
                EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO};
            case "Repartidor" -> new EstadoPedido[]{
                EstadoPedido.EN_RUTA, EstadoPedido.ENTREGADO};
            default           -> EstadoPedido.values();
        };
    }

    private void regresar() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame == null) return;
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        frame.dispose();
    }

    // ── Helpers de estilo ──────────────────────────────────────────────────
    private JLabel infoLabel() {
        JLabel lbl = new JLabel("—");
        lbl.setForeground(EstilosUI.TEXT_PRIMARY);
        lbl.setFont(EstilosUI.FONT_NORMAL);
        return lbl;
    }

    private JLabel muted(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(EstilosUI.TEXT_MUTED);
        lbl.setFont(EstilosUI.FONT_SMALL);
        return lbl;
    }

    private JButton buildButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = isEnabled() ? bg : bg.darker();
                g2.setColor(getModel().isRollover() && isEnabled() ? base.brighter() : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(fg);
        btn.setFont(EstilosUI.FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(7, 16, 7, 16));
        return btn;
    }
}