/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import PanelesPrincipales.PanelCajero;
import Clases.Pedido;
import Clases.Rol;
import Clases.Usuario;
import ClasesEnum.enums.EstadoPedido;
import EstilosUI.EstilosUI;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import servicios.PedidoService;

public class ListaPedidosCajero extends JPanel {

    // ── Dependencias ───────────────────────────────────────────────────────
    private final PanelCajero    cajero;
    private final Usuario        usuarioActual;
    private final Rol            rolActual;
    private final PedidoService  servicePedido;

    // ── Componentes ────────────────────────────────────────────────────────
    private JTable            tblPedidos;
    private DefaultTableModel model;
    private JComboBox<String> cbEstado;
    private JComboBox<String> cbTipo;
    private JTextField        txtCodigo;
    private JButton           btnBuscar;
    private JButton           btnLimpiar;
    private JButton           btnNuevo;
    private JButton           btnVer;
    private JButton           btnCambiarEstado;   // ← nuevo
    private JButton           btnRegresar;
    private JLabel            lblResultados;

    // ══════════════════════════════════════════════════════════════════════
    public ListaPedidosCajero(Usuario usuarioActual, PanelCajero cajero, Rol rolActual) {
        this.usuarioActual = usuarioActual;
        this.cajero        = cajero;
        this.rolActual     = rolActual;
        this.servicePedido = new PedidoService();

        initComponents();
        initEvents();
        cargarPedidos();
    }

    // ── UI ─────────────────────────────────────────────────────────────────
    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBackground(EstilosUI.BG_DARK);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabla(),  BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 0));
        wrapper.setBackground(EstilosUI.BG_DARK);
        wrapper.setBorder(new EmptyBorder(16, 20, 0, 20));

        JLabel titulo = EstilosUI.labelTitulo("Gestión de Pedidos");
        titulo.setBorder(new EmptyBorder(0, 0, 14, 0));

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filtros.setBackground(EstilosUI.BG_PANEL);
        filtros.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosUI.BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)));

        cbEstado = EstilosUI.combo(new String[]{"TODOS", "PENDIENTE", "EN_PREPARACION",
                                                 "LISTO", "EN_RUTA", "ENTREGADO", "CANCELADO"});
        cbTipo   = EstilosUI.combo(new String[]{"TODOS", "LOCAL", "DELIVERY"});

        txtCodigo = EstilosUI.campoTexto(12);

        btnBuscar  = EstilosUI.botonPrimario("Buscar");
        btnLimpiar = EstilosUI.botonFantasma("Limpiar");

        filtros.add(EstilosUI.labelMuted("Estado:"));  filtros.add(cbEstado);
        filtros.add(EstilosUI.labelMuted("Tipo:"));    filtros.add(cbTipo);
        filtros.add(EstilosUI.labelMuted("Código:"));  filtros.add(txtCodigo);
        filtros.add(btnBuscar);
        filtros.add(btnLimpiar);

        wrapper.add(titulo,  BorderLayout.NORTH);
        wrapper.add(filtros, BorderLayout.CENTER);
        return wrapper;
    }

    private JScrollPane buildTabla() {
        model = new DefaultTableModel(
                new Object[]{"Código", "Cliente", "Tipo", "Estado", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblPedidos = new JTable(model);
        EstilosUI.aplicarEstiloTablaConEstado(tblPedidos, 3);

        int[] widths = {110, 200, 90, 130, 90};
        for (int i = 0; i < widths.length; i++)
            tblPedidos.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(tblPedidos);
        EstilosUI.aplicarEstiloScroll(scroll);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(12, 20, 0, 20),
                BorderFactory.createLineBorder(EstilosUI.BORDER, 1)));
        return scroll;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(EstilosUI.BG_DARK);
        footer.setBorder(new EmptyBorder(10, 20, 16, 20));

        lblResultados = EstilosUI.labelMuted("0 pedidos");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setBackground(EstilosUI.BG_DARK);

        btnRegresar      = EstilosUI.botonFantasma("Regresar");
        btnVer           = EstilosUI.botonSecundario("Ver detalle");
        btnCambiarEstado = EstilosUI.botonSecundario("Cambiar estado");
        btnCambiarEstado.setForeground(EstilosUI.WARNING);
        btnNuevo         = EstilosUI.botonPrimario("+ Nuevo");

        botones.add(btnRegresar);
        botones.add(btnVer);
        botones.add(btnCambiarEstado);
        botones.add(btnNuevo);

        footer.add(lblResultados, BorderLayout.WEST);
        footer.add(botones,       BorderLayout.EAST);
        return footer;
    }

    // ── Eventos ────────────────────────────────────────────────────────────
    private void initEvents() {
        btnBuscar.addActionListener(e        -> aplicarFiltros());
        btnLimpiar.addActionListener(e       -> limpiarFiltros());
        btnNuevo.addActionListener(e         -> irANuevoPedido());
        btnVer.addActionListener(e           -> verPedidoSeleccionado());
        btnCambiarEstado.addActionListener(e -> accionCambiarEstado());
        btnRegresar.addActionListener(e      -> regresar());
        txtCodigo.addActionListener(e        -> aplicarFiltros());

        tblPedidos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) verPedidoSeleccionado();
            }
        });
    }

    // ── Acciones ───────────────────────────────────────────────────────────
    private void aplicarFiltros() {
        String estadoFiltro  = "TODOS".equals(cbEstado.getSelectedItem()) ? null : (String) cbEstado.getSelectedItem();
        String tipoFiltro    = "TODOS".equals(cbTipo.getSelectedItem())   ? null : (String) cbTipo.getSelectedItem();
        String codigoFiltro  = txtCodigo.getText().trim().isEmpty()        ? null : txtCodigo.getText().trim();
        cargarTabla(servicePedido.buscarFiltrado(estadoFiltro, tipoFiltro, codigoFiltro));
    }

    private void limpiarFiltros() {
        cbEstado.setSelectedIndex(0);
        cbTipo.setSelectedIndex(0);
        txtCodigo.setText("");
        cargarPedidos();
    }

    private void cargarPedidos() {
        cargarTabla(servicePedido.listaPedidos());
    }

    private void cargarTabla(List<Pedido> pedidos) {
        model.setRowCount(0);
        if (pedidos == null) { lblResultados.setText("0 pedido(s)"); return; }
        for (Pedido p : pedidos) {
            String cliente = (p.getIdCliente() != null) ? p.getIdCliente().getNombre() : "—";
            model.addRow(new Object[]{
                p.getCodigo(), cliente, p.getTipoPedido(), p.getEstado(), p.getTotal()
            });
        }
        lblResultados.setText(pedidos.size() + " pedido(s) encontrado(s)");
    }

    private Pedido obtenerPedidoSeleccionado() {
        int row = tblPedidos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la lista.",
                    "Sin selección", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String codigo = model.getValueAt(row, 0).toString();
        Pedido pedido = servicePedido.buscarPorCodigo(codigo);
        if (pedido == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el pedido: " + codigo,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return pedido;
    }

    private void verPedidoSeleccionado() {
        Pedido pedido = obtenerPedidoSeleccionado();
        if (pedido == null) return;
        // Pasa rolActual para que PanelPedidoDetalle también pueda cambiar estado
        cambiarPanel(new PanelPedidoDetalle(pedido, rolActual, usuarioActual));
    }

    /**
     * Muestra un diálogo con los estados permitidos para el rol actual
     * y llama a PedidoService.cambiarEstado().
     */
    private void accionCambiarEstado() {

    Pedido pedido = obtenerPedidoSeleccionado();

    if (pedido == null) {
        return;
    }

    // El service decide qué opciones son válidas
    EstadoPedido[] opciones =
            servicePedido.obtenerEstadosPermitidos(
                    pedido,
                    rolActual
            );

    if (opciones.length == 0) {

        JOptionPane.showMessageDialog(
                this,
                "No existen cambios de estado disponibles para este pedido.",
                "Sin opciones",
                JOptionPane.INFORMATION_MESSAGE
        );

        return;
    }

    EstadoPedido seleccionado =
            (EstadoPedido) JOptionPane.showInputDialog(
                    this,
                    "Estado actual: " + pedido.getEstado()
                    + "\nSeleccione el nuevo estado:",
                    "Cambiar estado",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

    if (seleccionado == null) {
        return;
    }

    try {

        servicePedido.cambiarEstado(
                pedido.getIdPedido(),
                seleccionado,
                usuarioActual,
                rolActual
        );

        JOptionPane.showMessageDialog(
                this,
                "Estado actualizado correctamente a: "
                + seleccionado,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarPedidos();

    } catch (RuntimeException ex) {

        String mensaje =
                ex.getCause() != null
                ? ex.getCause().getMessage()
                : ex.getMessage();

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Cambio no permitido",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

    /** Devuelve los estados a los que este rol puede transicionar */
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

    private void irANuevoPedido() {
        PanelPedidoNuevo ventana = new PanelPedidoNuevo(usuarioActual, rolActual);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) frame.dispose();
    }

    private void regresar() {
        PanelCajero ventanaCajero = new PanelCajero(usuarioActual, rolActual);
        ventanaCajero.setLocationRelativeTo(null);
        ventanaCajero.setVisible(true);
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) frame.dispose();
    }

    private void cambiarPanel(JPanel panel) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame == null) return;
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
    }
}