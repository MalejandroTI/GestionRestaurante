/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelesPrincipales;

import Clases.Rol;
import Clases.Usuario;
import EstilosUI.EstilosUI;
import FuncionalidadBotones.AgregarCliente;
import FuncionalidadBotones.ListaClientesCajero;
import FuncionalidadBotones.ListaPedidosCajero;
import FuncionalidadBotones.PanelPedidoNuevo;
import PresentacionJFRAME.SelectorDeRol;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelCajero extends JFrame {

    private final Rol rolActual;
    private final Usuario usuarioActual;

    public PanelCajero(Usuario usuario, Rol rolActual) {
        this.rolActual = rolActual;
        this.usuarioActual = usuario;
        initUI();
    }

    // ═══════════════════════════════════════
    // UI PRINCIPAL
    // ═══════════════════════════════════════
    private void initUI() {
        EstilosUI.aplicarEstiloVentana(this, "Panel Cajero", 520, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(460, 380));

        JPanel fondo = new JPanel(new BorderLayout(0, 0));
        fondo.setBackground(EstilosUI.BG_DARK);
        setContentPane(fondo);

        fondo.add(crearHeader(), BorderLayout.NORTH);
        fondo.add(crearCentro(), BorderLayout.CENTER);
        fondo.add(crearFooter(), BorderLayout.SOUTH);
    }

    // ═══════════════════════════════════════
    // HEADER
    // ═══════════════════════════════════════
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(EstilosUI.BG_PANEL);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, EstilosUI.BORDER),
                new EmptyBorder(16, 20, 16, 20)));

        // Saludo + nombre
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        izq.setOpaque(false);

        JLabel saludo = EstilosUI.labelNormal("Hola,");
        JLabel nombre = EstilosUI.labelSubtitulo(usuarioActual.getNombre());
        nombre.setForeground(EstilosUI.ACCENT);

        JLabel rolBadge = EstilosUI.badge("CAJERO", EstilosUI.ACCENT);

        izq.add(saludo);
        izq.add(nombre);
        izq.add(rolBadge);

        // Botón cerrar sesión
        JButton btnRegresar = EstilosUI.botonFantasma("← Cambiar rol");
        btnRegresar.setForeground(EstilosUI.DANGER);
        btnRegresar.addActionListener(e -> regresar());

        header.add(izq, BorderLayout.WEST);
        header.add(btnRegresar, BorderLayout.EAST);

        return header;
    }

    // ═══════════════════════════════════════
    // CENTRO — GRID DE ACCIONES
    // ═══════════════════════════════════════
    private JPanel crearCentro() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(EstilosUI.BG_DARK);
        wrapper.setBorder(new EmptyBorder(30, 40, 20, 40));

        JLabel lblAcciones = EstilosUI.labelMuted("ACCIONES RÁPIDAS");
        lblAcciones.setBorder(new EmptyBorder(0, 0, 14, 0));

        JPanel grid = new JPanel(new GridLayout(2, 2, 14, 14));
        grid.setOpaque(false);

        grid.add(crearCard("🧾", "Pedido Nuevo", "Registrar un nuevo pedido", EstilosUI.ACCENT, e -> abrirPedidoNuevo()));
        grid.add(crearCard("📋", "Ver Pedidos", "Listar y gestionar pedidos", EstilosUI.WARNING, e -> abrirListaPedidos()));
        grid.add(crearCard("👤", "Cliente Nuevo", "Registrar un nuevo cliente", EstilosUI.SUCCESS, e -> abrirClienteNuevo()));
        grid.add(crearCard("👥", "Ver Clientes", "Consultar clientes registrados", EstilosUI.PURPLE, e -> abrirListaClientes()));

        wrapper.add(lblAcciones, BorderLayout.NORTH);
        wrapper.add(grid, BorderLayout.CENTER);

        return wrapper;
    }

    // ═══════════════════════════════════════
    // CARD DE ACCIÓN
    // ═══════════════════════════════════════
    private JPanel crearCard(String icono, String titulo, String descripcion,
            Color color, java.awt.event.ActionListener accion) {

        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosUI.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                // Borde izquierdo de color
                g2.setColor(color);
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Icono
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        // Texto
        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));

        JLabel lblTitulo = EstilosUI.labelNormal(titulo);
        lblTitulo.setFont(EstilosUI.FONT_BOLD);
        lblTitulo.setForeground(color);

        JLabel lblDesc = EstilosUI.labelMuted(descripcion);

        texto.add(lblTitulo);
        texto.add(lblDesc);

        // Flecha
        JLabel flecha = EstilosUI.labelMuted("›");
        flecha.setFont(new Font("Segoe UI", Font.BOLD, 20));

        card.add(lblIcono, BorderLayout.WEST);
        card.add(texto, BorderLayout.CENTER);
        card.add(flecha, BorderLayout.EAST);

        // Hover + click
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                accion.actionPerformed(null);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(EstilosUI.BG_PANEL);
                card.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(EstilosUI.BG_CARD);
                card.repaint();
            }
        });

        return card;
    }

    // ═══════════════════════════════════════
    // FOOTER
    // ═══════════════════════════════════════
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(EstilosUI.BG_DARK);
        footer.setBorder(new EmptyBorder(0, 20, 12, 20));

        JLabel lblVersion = EstilosUI.labelMuted("Sistema de Gestión · Cajero");
        footer.add(lblVersion);

        return footer;
    }

    // ═══════════════════════════════════════
    // ACCIONES
    // ═══════════════════════════════════════
    private void abrirPedidoNuevo() {
        PanelPedidoNuevo ventana = new PanelPedidoNuevo(usuarioActual, rolActual);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        this.dispose();
    }

    private void abrirListaPedidos() {
        ListaPedidosCajero panel = new ListaPedidosCajero(usuarioActual, this, rolActual);
        setContentPane(panel);
        revalidate();
        repaint();
    }

    private void abrirClienteNuevo() {
        new AgregarCliente().setVisible(true);
    }

    private void abrirListaClientes() {
        // TODO: implementar lista de clientes
        ListaClientesCajero ventana = new ListaClientesCajero();

        ventana.setLocationRelativeTo(this);

        ventana.setVisible(true);
    }

    private void regresar() {
        int op = JOptionPane.showConfirmDialog(this,
                "¿Deseas volver al selector de rol?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            SelectorDeRol selector = new SelectorDeRol(usuarioActual);
            selector.setLocationRelativeTo(null);
            selector.setVisible(true);
            this.dispose();
        }
    }
}
