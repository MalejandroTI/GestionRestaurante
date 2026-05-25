/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.EntregaPedido;
import Clases.Usuario;
import java.awt.*;
import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import servicios.EntregaService;
import EstilosUI.EstilosUI;

/**
 * Lista de pedidos EN RUTA asignados al repartidor. Acciones: ver detalle,
 * marcar entregado, ver dirección, llamar al cliente.
 */
public class PanelPedidosDelivery extends JFrame {

    // ── Dependencias ───────────────────────────────────────────────────────
    private List<EntregaPedido> listaVisible;
    private final Usuario usuario;
    private final EntregaService entregaService;

    // ── Componentes ────────────────────────────────────────────────────────
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnDetalle;
    private JButton btnMarcarEntregado;
    private JButton btnVerDireccion;
    private JButton btnLlamar;
    private JButton btnRegresar;
    private JLabel lblContador;

    // ══════════════════════════════════════════════════════════════════════
    public PanelPedidosDelivery(List<EntregaPedido> pedidos, Usuario usuario) {
        this.listaVisible = pedidos;
        this.usuario = usuario;
        this.entregaService = new EntregaService();

        initUI();
        EstilosUI.aplicarEstiloVentana(this, "Pedidos en ruta", 820, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        cargarTabla(pedidos);
    }

    // ── UI ─────────────────────────────────────────────────────────────────
    private void initUI() {
        JPanel fondo = new JPanel(new BorderLayout(0, 0));
        fondo.setBackground(EstilosUI.BG_DARK);
        setContentPane(fondo);

        fondo.add(buildHeader(), BorderLayout.NORTH);
        fondo.add(buildTabla(), BorderLayout.CENTER);
        fondo.add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = EstilosUI.panelSecundario();
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        izq.setOpaque(false);

        JLabel icono = new JLabel("📦");
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JLabel titulo = EstilosUI.labelSubtitulo("Pedidos en ruta");

        lblContador = EstilosUI.badge("0", EstilosUI.WARNING);

        izq.add(icono);
        izq.add(titulo);
        izq.add(lblContador);

        btnRegresar = EstilosUI.botonSecundario("← Regresar");
        btnRegresar.addActionListener(e -> dispose());

        header.add(izq, BorderLayout.WEST);
        header.add(btnRegresar, BorderLayout.EAST);
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

        // Anchos de columna
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
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(EstilosUI.BG_DARK);
        footer.setBorder(new EmptyBorder(12, 20, 16, 20));

        // Info selección
        JLabel hint = EstilosUI.labelMuted("Selecciona un pedido para usar las acciones");

        // Botones de acción
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setBackground(EstilosUI.BG_DARK);

        btnLlamar = EstilosUI.botonSecundario("📞 Llamar");
        btnVerDireccion = EstilosUI.botonSecundario("🗺 Dirección");
        btnDetalle = EstilosUI.botonSecundario("Ver detalle");
        btnMarcarEntregado = EstilosUI.botonExito("✓ Marcar entregado");

        btnLlamar.addActionListener(e -> accionLlamar());
        btnVerDireccion.addActionListener(e -> accionVerDireccion());
        btnDetalle.addActionListener(e -> accionVerDetalle());
        btnMarcarEntregado.addActionListener(e -> accionMarcarEntregado());

        botones.add(btnLlamar);
        botones.add(btnVerDireccion);
        botones.add(btnDetalle);
        botones.add(btnMarcarEntregado);

        footer.add(hint, BorderLayout.WEST);
        footer.add(botones, BorderLayout.EAST);
        return footer;
    }

    // ── Carga de datos ─────────────────────────────────────────────────────
    private void cargarTabla(List<EntregaPedido> lista) {
        listaVisible = lista;
        modelo.setRowCount(0);
        if (lista == null) {
            return;
        }
        for (EntregaPedido e : lista) {
            String cliente = e.getIdPedido().getIdCliente() != null
                    ? e.getIdPedido().getIdCliente().getNombre() : "—";
            String direccion = e.getDireccionEntrega() != null
                    ? e.getDireccionEntrega() : "—";
            String distancia = e.getDistanciaKm() != null
                    ? e.getDistanciaKm().toPlainString() : "—";
            String total = e.getIdPedido().getTotal() != null
                    ? "$ " + e.getIdPedido().getTotal().toPlainString() : "—";

            modelo.addRow(new Object[]{
                e.getIdPedido().getCodigo(),
                cliente,
                direccion,
                distancia,
                total
            });
        }
        lblContador.setText(String.valueOf(lista.size()));
    }

    /**
     * Devuelve la EntregaPedido de la fila seleccionada, o null si no hay.
     */
    private EntregaPedido obtenerSeleccionada() {
        int row = tabla.convertRowIndexToModel(
                tabla.getSelectedRow()
        );
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un pedido de la lista.",
                    "Sin selección", JOptionPane.INFORMATION_MESSAGE);
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

        // Reutiliza PanelPedidoDetalle que ya tienes
        PanelPedidoDetalle detalle = new PanelPedidoDetalle(
                entrega.getIdPedido());
        JFrame frame = new JFrame("Detalle del pedido");
        frame.setContentPane(detalle);
        frame.setSize(860, 600);
        frame.setLocationRelativeTo(this);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void accionMarcarEntregado() {
        EntregaPedido entrega = obtenerSeleccionada();
        if (entrega == null) {
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Confirmas que el pedido "
                + entrega.getIdPedido().getCodigo()
                + " fue entregado?",
                "Confirmar entrega",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            entregaService.marcarEntregado(entrega);

            // Recarga desde BD — la vista no toca la colección directamente
            List<EntregaPedido> actualizados = entregaService.obtenerEnRuta(usuario);
            cargarTabla(actualizados);

            JOptionPane.showMessageDialog(this,
                    "Pedido marcado como entregado.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar el pedido: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionVerDireccion() {
        EntregaPedido entrega = obtenerSeleccionada();
        if (entrega == null) {
            return;
        }

        String direccion = entrega.getDireccionEntrega();
        if (direccion == null || direccion.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Este pedido no tiene dirección registrada.",
                    "Sin dirección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Muestra la dirección en texto y ofrece abrir Google Maps
        int op = JOptionPane.showConfirmDialog(this,
                "Dirección: " + direccion
                + "\n\n¿Abrir en Google Maps?",
                "Dirección de entrega",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (op == JOptionPane.YES_OPTION) {
            try {
                String query = direccion.replace(" ", "+");
                Desktop.getDesktop().browse(
                        new URI("https://www.google.com/maps/search/?api=1&query=" + query));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo abrir el navegador.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void accionLlamar() {
        EntregaPedido entrega = obtenerSeleccionada();
        if (entrega == null) {
            return;
        }

        String telefono = entrega.getIdPedido().getIdCliente() != null
                ? entrega.getIdPedido().getIdCliente().getCelular()
                : null;

        if (telefono == null || telefono.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "El cliente no tiene teléfono registrado.",
                    "Sin teléfono", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Muestra el número para marcarlo manualmente (Desktop Java no soporta tel://)
        JOptionPane.showMessageDialog(this,
                "Llama al cliente:\n\n📞  " + telefono,
                "Contacto del cliente",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
