/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

/**
 *
 * @author ASUS
 */
import Clases.EntregaPedido;
import Clases.Usuario;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import EstilosUI.EstilosUI;
import servicios.EntregaService;

/**
 * Historial de pedidos ENTREGADOS del repartidor. Solo lectura — no permite
 * acciones de modificación.
 */
public class PanelPedidosHistorialRepartidor extends JFrame {

    // ── Dependencias ───────────────────────────────────────────────────────
    private List<EntregaPedido> listaVisible;
    private final Usuario usuario;
    private final EntregaService entregaService;
    // ── Componentes ────────────────────────────────────────────────────────
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblContador;
    private JLabel lblTotal;
    private JButton btnDetalle;
    private JButton btnRegresar;

    // ══════════════════════════════════════════════════════════════════════
    public PanelPedidosHistorialRepartidor(List<EntregaPedido> pedidos, Usuario usuario) {
        this.listaVisible = pedidos;
        this.usuario = usuario;
        this.entregaService = new EntregaService();

        initUI();
        EstilosUI.aplicarEstiloVentana(this, "Historial de entregas", 820, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        cargarTabla(pedidos);
    }

    // ── UI ─────────────────────────────────────────────────────────────────
    private void initUI() {
        JPanel fondo = new JPanel(new java.awt.BorderLayout(0, 0));
        fondo.setBackground(EstilosUI.BG_DARK);
        setContentPane(fondo);

        fondo.add(buildHeader(), java.awt.BorderLayout.NORTH);
        fondo.add(buildTabla(), java.awt.BorderLayout.CENTER);
        fondo.add(buildFooter(), java.awt.BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = EstilosUI.panelSecundario();
        header.setLayout(new java.awt.BorderLayout());
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JPanel izq = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
        izq.setOpaque(false);

        JLabel icono = new JLabel("📋");
        icono.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 22));

        JLabel titulo = EstilosUI.labelSubtitulo("Historial de entregas");

        lblContador = EstilosUI.badge("0", EstilosUI.SUCCESS);

        izq.add(icono);
        izq.add(titulo);
        izq.add(lblContador);

        btnRegresar = EstilosUI.botonSecundario("← Regresar");
        btnRegresar.addActionListener(e -> dispose());

        header.add(izq, java.awt.BorderLayout.WEST);
        header.add(btnRegresar, java.awt.BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildTabla() {
        modelo = new DefaultTableModel(
                new Object[]{"Código", "Cliente", "Dirección", "Dist. (km)", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        EstilosUI.aplicarEstiloTabla(tabla);

        int[] anchos = {120, 160, 220, 90, 90};
        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        // Doble clic → ver detalle
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    accionVerDetalle();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        EstilosUI.aplicarEstiloScroll(scroll);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 20, 0, 20),
                BorderFactory.createLineBorder(EstilosUI.BORDER, 1)));
        return scroll;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new java.awt.BorderLayout());
        footer.setBackground(EstilosUI.BG_DARK);
        footer.setBorder(new EmptyBorder(12, 20, 16, 20));

        // Resumen de total acumulado
        JPanel resumen = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 0));
        resumen.setBackground(EstilosUI.BG_DARK);
        resumen.add(EstilosUI.labelMuted("Total acumulado:"));
        lblTotal = EstilosUI.labelNormal("$ 0.00");
        lblTotal.setForeground(EstilosUI.SUCCESS);
        resumen.add(lblTotal);

        // Botón detalle
        JPanel botones = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        botones.setBackground(EstilosUI.BG_DARK);
        btnDetalle = EstilosUI.botonSecundario("Ver detalle");
        btnDetalle.addActionListener(e -> accionVerDetalle());
        botones.add(btnDetalle);

        footer.add(resumen, java.awt.BorderLayout.WEST);
        footer.add(botones, java.awt.BorderLayout.EAST);
        return footer;
    }

    // ── Datos ──────────────────────────────────────────────────────────────
    private void cargarTabla(List<EntregaPedido> lista) {

        listaVisible = lista;

        modelo.setRowCount(0);

        if (lista == null) {
            return;
        }

        for (EntregaPedido entrega : lista) {

            modelo.addRow(new Object[]{
                entrega.getIdPedido().getCodigo(),
                entregaService.obtenerNombreCliente(entrega),
                entregaService.obtenerDireccion(entrega),
                entregaService.obtenerDistanciaTexto(entrega),
                entregaService.obtenerTotalTexto(entrega)
            });
        }

        lblContador.setText(
                String.valueOf(lista.size())
        );

        lblTotal.setText(
                "$ "
                + entregaService
                        .calcularTotalEntregado(lista)
                        .toPlainString()
        );
    }

    private EntregaPedido obtenerSeleccionada() {

        int row = tabla.convertRowIndexToModel(
                tabla.getSelectedRow()
        );

        if (row < 0) {

            JOptionPane.showMessageDialog(this,
                    "Selecciona un pedido de la lista.",
                    "Sin selección",
                    JOptionPane.INFORMATION_MESSAGE);

            return null;
        }

        return listaVisible.get(row);
    }

    // ── Acciones ───────────────────────────────────────────────────────────
    private void accionVerDetalle() {

        EntregaPedido entrega = obtenerSeleccionada();
        if (entrega == null) {
            return;
        }
        PanelPedidoDetalle detalle = new PanelPedidoDetalle(entrega.getIdPedido());
        JFrame frame = new JFrame("Detalle — " + entrega.getIdPedido().getCodigo());

        frame.setContentPane(detalle);
        frame.setSize(860, 600);
        frame.setLocationRelativeTo(this);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
