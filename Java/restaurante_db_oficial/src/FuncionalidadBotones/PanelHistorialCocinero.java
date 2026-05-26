/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

/**
 *
 * @author ASUS
 */
import Clases.Pedido;
import Clases.Usuario;
import ClasesEnum.enums.EstadoPedido;
import EstilosUI.EstilosUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import servicios.PedidoService;

public class PanelHistorialCocinero extends JFrame {

    private final Usuario usuarioActual;
    private final PedidoService pedidoService;

    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    private JComboBox<Object> filtroEstado;  // Object para mezclar "Todos" + EstadoPedido
    private JLabel lblTotal;
    private JButton btnFiltrar;
    private JButton btnCerrar;

    // Estados visibles en el historial del cocinero
    private static final EstadoPedido[] ESTADOS_HISTORIAL = {
        EstadoPedido.EN_PREPARACION,
        EstadoPedido.LISTO
    };

    public PanelHistorialCocinero(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.pedidoService = new PedidoService();
        initUI();
        cargarTodos();
    }

    // UI PRINCIPAL
    private void initUI() {
        EstilosUI.aplicarFondoOscuro(this);
        EstilosUI.aplicarEstiloVentana(this, "Historial de Pedidos — Cocina", 750, 500);

        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(crearHeader(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    // HEADER CON FILTRO
    
    private JPanel crearHeader() {
        JPanel header = EstilosUI.panelSecundario();
        header.setLayout(new BorderLayout(10, 0));
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel titulo = EstilosUI.labelTitulo("📋  Historial de Pedidos");

        // Panel derecho: filtro + botón
        JPanel filtroPan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filtroPan.setOpaque(false);

        JLabel lblFiltro = EstilosUI.labelSubtitulo("Filtrar: ");
        filtroEstado = new JComboBox<>();
        filtroEstado.addItem("Todos");
        for (EstadoPedido e : ESTADOS_HISTORIAL) {
            filtroEstado.addItem(e);
        }
        filtroEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filtroEstado.setBackground(new Color(50, 50, 65));
        filtroEstado.setForeground(Color.WHITE);
        filtroEstado.setPreferredSize(new Dimension(150, 30));

        btnFiltrar = EstilosUI.botonPrimario("Filtrar");
        btnFiltrar.setPreferredSize(new Dimension(90, 30));
        btnFiltrar.addActionListener(e -> aplicarFiltro());

        filtroPan.add(lblFiltro);
        filtroPan.add(filtroEstado);
        filtroPan.add(btnFiltrar);

        header.add(titulo, BorderLayout.WEST);
        header.add(filtroPan, BorderLayout.EAST);

        return header;
    }

    // ═══════════════════════════════════════
    // TABLA
    // ═══════════════════════════════════════
    private JScrollPane crearTabla() {
        String[] columnas = {"ID", "Cliente", "Detalle / Platos", "Estado", "Hora"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setRowHeight(30);
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        tablaHistorial.setBackground(new Color(45, 45, 55));
        tablaHistorial.setForeground(Color.WHITE);
        tablaHistorial.setGridColor(new Color(70, 70, 85));
        tablaHistorial.getTableHeader().setBackground(new Color(30, 30, 40));
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);
        tablaHistorial.setSelectionBackground(new Color(80, 120, 200));
        tablaHistorial.setSelectionForeground(Color.WHITE);

        // Renderer para columna Estado con colores
        tablaHistorial.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) {
                    String estado = value != null ? value.toString() : "";
                    if (estado.equals(EstadoPedido.LISTO.name())) {
                        setBackground(new Color(30, 150, 80));
                        setForeground(Color.WHITE);
                    } else if (estado.equals(EstadoPedido.EN_RUTA.name())) {
                        setBackground(new Color(20, 100, 180));
                        setForeground(Color.WHITE);
                    } else if (estado.equals(EstadoPedido.ENTREGADO.name())) {
                        setBackground(new Color(60, 60, 80));
                        setForeground(new Color(180, 180, 200));
                    } else {
                        setBackground(new Color(45, 45, 55));
                        setForeground(Color.WHITE);
                    }
                }
                return this;
            }
        });

        // Anchos
        tablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaHistorial.getColumnModel().getColumn(1).setPreferredWidth(130);
        tablaHistorial.getColumnModel().getColumn(2).setPreferredWidth(280);
        tablaHistorial.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaHistorial.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.getViewport().setBackground(new Color(45, 45, 55));
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        return scroll;
    }

    // ═══════════════════════════════════════
    // PANEL INFERIOR
    // ═══════════════════════════════════════
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 12, 10, 12));

        lblTotal = EstilosUI.labelSubtitulo("Total: 0 pedidos");
        lblTotal.setForeground(new Color(160, 160, 180));

        btnCerrar = EstilosUI.botonPeligro("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        panel.add(lblTotal, BorderLayout.WEST);
        panel.add(btnCerrar, BorderLayout.EAST);

        return panel;
    }

    // ═══════════════════════════════════════
    // CARGA Y FILTRO DE DATOS
    // ═══════════════════════════════════════
    private void cargarTodos() {
        poblarTabla(pedidoService.listarPedidosCocina());
    }

    private void aplicarFiltro() {
        Object seleccionado = filtroEstado.getSelectedItem();

        if ("Todos".equals(seleccionado)) {
            cargarTodos();
            return;
        }

        // El ítem es directamente un EstadoPedido
        List<Pedido> resultado = pedidoService.listarPedidosPorEstado((EstadoPedido) seleccionado);
        poblarTabla(resultado);
    }

    private void poblarTabla(List<Pedido> pedidos) {
        modeloTabla.setRowCount(0);

        if (pedidos == null || pedidos.isEmpty()) {
            lblTotal.setText("Total: 0 pedidos");
            return;
        }

        for (Pedido p : pedidos) {
            modeloTabla.addRow(new Object[]{
                p.getIdPedido(),
                p.getIdCliente() != null ? p.getIdCliente().getNombre() : "—",
                p.getDetallePedidoCollection(),
                // Guardamos .name() del enum → String comparable en renderer y validaciones
                p.getEstado() != null ? p.getEstado().name() : "—",
                p.getFechaHora() != null ? p.getFechaHora().toString() : "—"
            });
        }

        lblTotal.setText("Total: " + pedidos.size() + " pedidos");
    }
}
