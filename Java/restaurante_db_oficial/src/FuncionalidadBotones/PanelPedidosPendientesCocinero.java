/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

/**
 *
 * @author ASUS
 */
import Clases.DetallePedido;
import Clases.Pedido;
import Clases.Rol;
import Clases.Usuario;
import ClasesEnum.enums.EstadoPedido;
import EstilosUI.EstilosUI;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Collection;
import java.util.List;
import servicios.DetallePedidoService;
import servicios.PedidoService;
 
public class PanelPedidosPendientesCocinero extends JFrame {
 
    private final Usuario usuarioActual;
    private final Rol rolActual;
    private final PedidoService pedidoService;
    private final DetallePedidoService detallePedidoService;
 
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
 
    // Mapa para recuperar el objeto Pedido completo por fila
    private final java.util.List<Pedido> pedidosCargados = new java.util.ArrayList<>();
 
    private JButton btnVerDetalle;
    private JButton btnMarcarEnPreparacion;
    private JButton btnMarcarListo;
    private JButton btnRefrescar;
    private JButton btnCerrar;
 
    private JLabel lblContador;
 
    public PanelPedidosPendientesCocinero(Usuario usuarioActual, Rol rolActual) {
        this.usuarioActual        = usuarioActual;
        this.rolActual            = rolActual;
        this.pedidoService        = new PedidoService();
        this.detallePedidoService = new DetallePedidoService();
        initUI();
        cargarPedidos();
    }
 
    // ═══════════════════════════════════════
    // UI PRINCIPAL
    // ═══════════════════════════════════════
    private void initUI() {
        EstilosUI.aplicarFondoOscuro(this);
        EstilosUI.aplicarEstiloVentana(this, "Pedidos Pendientes — Cocina", 750, 500);
 
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        add(crearHeader(),       BorderLayout.NORTH);
        add(crearPanelTabla(),   BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }
 
    // ═══════════════════════════════════════
    // HEADER
    // ═══════════════════════════════════════
    private JPanel crearHeader() {
        JPanel header = EstilosUI.panelSecundario();
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
 
        JLabel titulo = EstilosUI.labelTitulo("🍳  Pedidos Pendientes");
 
        lblContador = EstilosUI.labelSubtitulo("Cargando...");
        lblContador.setForeground(new Color(255, 165, 0));
 
        header.add(titulo,      BorderLayout.WEST);
        header.add(lblContador, BorderLayout.EAST);
 
        return header;
    }
 
    // ═══════════════════════════════════════
    // TABLA
    // ═══════════════════════════════════════
    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Cliente", "Detalle / Platos", "Estado", "Hora"};
 
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
 
        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPedidos.setRowHeight(32);
        tablaPedidos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaPedidos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
 
        tablaPedidos.setBackground(new Color(45, 45, 55));
        tablaPedidos.setForeground(Color.WHITE);
        tablaPedidos.setGridColor(new Color(70, 70, 85));
        tablaPedidos.getTableHeader().setBackground(new Color(30, 30, 40));
        tablaPedidos.getTableHeader().setForeground(Color.WHITE);
        tablaPedidos.setSelectionBackground(new Color(80, 120, 200));
        tablaPedidos.setSelectionForeground(Color.WHITE);
 
        // Renderer — usa .name() del enum para comparar sin magic strings
        tablaPedidos.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) {
                    String estado = value != null ? value.toString() : "";
                    if (estado.equals(EstadoPedido.PENDIENTE.name())) {
                        setBackground(new Color(180, 100, 20));
                        setForeground(Color.WHITE);
                    } else if (estado.equals(EstadoPedido.EN_PREPARACION.name())) {
                        setBackground(new Color(20, 100, 180));
                        setForeground(Color.WHITE);
                    } else if (estado.equals(EstadoPedido.LISTO.name())) {
                        setBackground(new Color(30, 150, 80));
                        setForeground(Color.WHITE);
                    } else {
                        setBackground(new Color(45, 45, 55));
                        setForeground(Color.WHITE);
                    }
                }
                return this;
            }
        });
 
        tablaPedidos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaPedidos.getColumnModel().getColumn(1).setPreferredWidth(130);
        tablaPedidos.getColumnModel().getColumn(2).setPreferredWidth(280);
        tablaPedidos.getColumnModel().getColumn(3).setPreferredWidth(140);
        tablaPedidos.getColumnModel().getColumn(4).setPreferredWidth(100);
 
        JScrollPane scroll = new JScrollPane(tablaPedidos);
        scroll.getViewport().setBackground(new Color(45, 45, 55));
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
 
        return scroll;
    }
 
    // ═══════════════════════════════════════
    // BOTONES
    // ═══════════════════════════════════════
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        panel.setOpaque(false);
 
        btnVerDetalle          = EstilosUI.botonSecundario("🔍  Ver Detalle");
        btnMarcarEnPreparacion = EstilosUI.botonPrimario("▶  En Preparación");
        btnMarcarListo         = EstilosUI.botonSecundario("✔  Marcar Listo");
        btnRefrescar           = EstilosUI.botonSecundario("↻  Refrescar");
        btnCerrar              = EstilosUI.botonPeligro("Cerrar");
 
        btnMarcarListo.setBackground(new Color(34, 139, 60));
        btnMarcarListo.setForeground(Color.WHITE);
 
        btnVerDetalle.addActionListener(e          -> verDetallePedido());
        btnMarcarEnPreparacion.addActionListener(e -> cambiarEstadoSeleccionado(EstadoPedido.EN_PREPARACION));
        btnMarcarListo.addActionListener(e         -> cambiarEstadoSeleccionado(EstadoPedido.LISTO));
        btnRefrescar.addActionListener(e           -> cargarPedidos());
        btnCerrar.addActionListener(e              -> dispose());
 
        panel.add(btnVerDetalle);
        panel.add(btnMarcarEnPreparacion);
        panel.add(btnMarcarListo);
        panel.add(btnRefrescar);
        panel.add(btnCerrar);
 
        return panel;
    }
 
    // ═══════════════════════════════════════
    // CARGA DE DATOS
    // ═══════════════════════════════════════
    private void cargarPedidos() {
        modeloTabla.setRowCount(0);
        pedidosCargados.clear();
 
        List<Pedido> pendientes    = pedidoService.listarPedidosPorEstado(EstadoPedido.PENDIENTE);
        List<Pedido> enPreparacion = pedidoService.listarPedidosPorEstado(EstadoPedido.EN_PREPARACION);
 
        int total = 0;
 
        if (pendientes != null) {
            for (Pedido p : pendientes) {
                agregarFilaTabla(p);
                pedidosCargados.add(p);
                total++;
            }
        }
        if (enPreparacion != null) {
            for (Pedido p : enPreparacion) {
                agregarFilaTabla(p);
                pedidosCargados.add(p);
                total++;
            }
        }
 
        lblContador.setText("Total activos: " + total);
    }
 
    private void agregarFilaTabla(Pedido p) {
        modeloTabla.addRow(new Object[]{
            p.getIdPedido(),
            p.getIdCliente() != null ? p.getIdCliente().getNombre() : "—",
            p.getDetallePedidoCollection() != null ? p.getDetallePedidoCollection().size() + " producto(s)" : "—",
            p.getEstado()    != null ? p.getEstado().name() : "—",
            p.getFechaHora() != null ? p.getFechaHora().toString() : "—"
        });
    }
 
    // ═══════════════════════════════════════
    // VER DETALLE DEL PEDIDO SELECCIONADO
    // ═══════════════════════════════════════
    private void verDetallePedido() {
        int fila = tablaPedidos.getSelectedRow();
 
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un pedido de la tabla primero.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        Pedido pedidoSeleccionado = pedidosCargados.get(fila);
        Collection<DetallePedido> detalles =
                detallePedidoService.obtenerDetallesPorPedido(pedidoSeleccionado);
 
        if (detalles == null || detalles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Este pedido no tiene productos registrados.",
                    "Sin detalle",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
 
        // ── Tabla solo con Producto y Cantidad ──
        String[] columnas = {"Producto", "Cantidad"};
        DefaultTableModel modeloDetalle = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
 
        for (DetallePedido d : detalles) {
            String nombreProducto = (d.getIdProducto() != null)
                    ? d.getIdProducto().getNombre()
                    : "—";
            modeloDetalle.addRow(new Object[]{
                nombreProducto,
                d.getCantidad()
            });
        }
 
        JTable tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.setRowHeight(28);
        tablaDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaDetalle.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaDetalle.setBackground(new Color(45, 45, 55));
        tablaDetalle.setForeground(Color.WHITE);
        tablaDetalle.setGridColor(new Color(70, 70, 85));
        tablaDetalle.getTableHeader().setBackground(new Color(30, 30, 40));
        tablaDetalle.getTableHeader().setForeground(Color.WHITE);
        tablaDetalle.setEnabled(false);
 
        // Columna Cantidad centrada
        tablaDetalle.getColumnModel().getColumn(1)
                .setCellRenderer(new DefaultTableCellRenderer() {{
                    setHorizontalAlignment(SwingConstants.CENTER);
                }});
 
        tablaDetalle.getColumnModel().getColumn(0).setPreferredWidth(220);
        tablaDetalle.getColumnModel().getColumn(1).setPreferredWidth(80);
 
        JScrollPane scrollDetalle = new JScrollPane(tablaDetalle);
        scrollDetalle.setPreferredSize(new Dimension(340, Math.min(detalles.size() * 30 + 40, 250)));
        scrollDetalle.getViewport().setBackground(new Color(45, 45, 55));
 
        // Título del diálogo con ID y cliente
        String cliente = (pedidoSeleccionado.getIdCliente() != null)
                ? pedidoSeleccionado.getIdCliente().getNombre()
                : "—";
 
        JOptionPane.showMessageDialog(
                this,
                scrollDetalle,
                "Detalle — Pedido #" + pedidoSeleccionado.getIdPedido() + "  |  " + cliente,
                JOptionPane.PLAIN_MESSAGE
        );
    }
 
    // ═══════════════════════════════════════
    // CAMBIAR ESTADO — recibe EstadoPedido (no String)
    // ═══════════════════════════════════════
    private void cambiarEstadoSeleccionado(EstadoPedido nuevoEstado) {
        int fila = tablaPedidos.getSelectedRow();
 
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un pedido de la tabla primero.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        // Recuperamos el objeto completo desde la lista paralela
        Pedido pedidoSeleccionado = pedidosCargados.get(fila);
        int    pedidoId           = pedidoSeleccionado.getIdPedido();
        String estadoNombre       = (String) modeloTabla.getValueAt(fila, 3);
 
        // Convertimos el String de la tabla de vuelta al enum
        EstadoPedido estadoActual;
        try {
            estadoActual = EstadoPedido.valueOf(estadoNombre);
        } catch (IllegalArgumentException ex) {
            estadoActual = null;
        }
 
        // Validación de flujo: PENDIENTE → EN_PREPARACION → LISTO
        if (nuevoEstado == EstadoPedido.LISTO && estadoActual == EstadoPedido.PENDIENTE) {
            JOptionPane.showMessageDialog(this,
                    "Debes marcar el pedido como 'En Preparación' antes de marcarlo como Listo.",
                    "Orden de estados",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Cambiar estado del pedido #" + pedidoId + " a \"" + nuevoEstado.name() + "\"?",
                "Confirmar cambio",
                JOptionPane.YES_NO_OPTION);
 
        if (confirmacion == JOptionPane.YES_OPTION) {
            // ✅ Firma real: cambiarEstado(int, EstadoPedido, Usuario, Rol)
            Pedido resultado = pedidoService.cambiarEstado(pedidoId, nuevoEstado, usuarioActual, rolActual);
 
            if (resultado != null) {
                JOptionPane.showMessageDialog(this,
                        "Estado actualizado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarPedidos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el estado. Intenta de nuevo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}