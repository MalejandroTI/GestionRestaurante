/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PresentacionJFRAME;

import FuncionalidadBotones.PanelDelivery;
import Clases.Rol;
import Clases.Usuario;
import EstilosUI.EstilosUI;
import FuncionalidadBotones.PanelAdmin;
import FuncionalidadBotones.PanelCajero;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SelectorDeRol extends JFrame {

    private final Usuario usuario;

    private static final java.util.Map<String, String> ICONOS = java.util.Map.of(
            "Administrador", "⚙️",
            "Cajero", "🧾",
            "Cocinero", "👨‍🍳",
            "Repartidor", "🛵"
    );
    private static final java.util.Map<String, Color> COLORES = java.util.Map.of(
            "Administrador", EstilosUI.PURPLE,
            "Cajero", EstilosUI.ACCENT,
            "Cocinero", EstilosUI.WARNING,
            "Repartidor", EstilosUI.SUCCESS
    );

    public SelectorDeRol(Usuario usuario) {
        this.usuario = usuario;
        initUI();
        EstilosUI.aplicarEstiloVentana(this, "Seleccionar rol", 420, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    public SelectorDeRol() {
        this.usuario = null;
        initUI();
        EstilosUI.aplicarEstiloVentana(this, "Seleccionar rol", 420, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    // ── UI ─────────────────────────────────────────────────────────────────
    private void initUI() {
        JPanel fondo = new JPanel(new BorderLayout(0, 0));
        fondo.setBackground(EstilosUI.BG_DARK);
        setContentPane(fondo);

        fondo.add(buildHeader(), BorderLayout.NORTH);
        fondo.add(buildRoles(), BorderLayout.CENTER);
        fondo.add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(EstilosUI.BG_PANEL);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, EstilosUI.BORDER),
                new EmptyBorder(28, 20, 24, 20)));

        JLabel avatar = new JLabel(inicialUsuario(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosUI.ACCENT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setForeground(EstilosUI.BG_DARK);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 28));
        avatar.setPreferredSize(new Dimension(64, 64));
        avatar.setMaximumSize(new Dimension(64, 64));
        avatar.setAlignmentX(CENTER_ALIGNMENT);
        avatar.setOpaque(false);

        JLabel nombre = EstilosUI.labelSubtitulo(
                usuario != null
                        ? usuario.getNombre() + " " + usuario.getApellido()
                        : "Usuario");
        nombre.setAlignmentX(CENTER_ALIGNMENT);
        nombre.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel correo = EstilosUI.labelMuted(
                usuario != null ? usuario.getCorreo() : "");
        correo.setAlignmentX(CENTER_ALIGNMENT);
        correo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitulo = EstilosUI.labelMuted("Selecciona con qué rol deseas continuar");
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        header.add(avatar);
        header.add(Box.createVerticalStrut(12));
        header.add(nombre);
        header.add(Box.createVerticalStrut(2));
        header.add(correo);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitulo);

        return header;
    }

    private JPanel buildRoles() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(EstilosUI.BG_DARK);
        panel.setBorder(new EmptyBorder(20, 30, 10, 30));

        // ✅ Condición corregida — antes estaba invertida con ||
        if (usuario != null && usuario.getRolCollection() != null) {
            for (Rol rol : usuario.getRolCollection()) {
                panel.add(buildBotonRol(rol.getNombre()));
                panel.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel sinRoles = EstilosUI.labelMuted("No hay roles asignados.");
            sinRoles.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(sinRoles);
        }

        return panel;
    }

    private JPanel buildBotonRol(String nombreRol) {
        String icono = ICONOS.getOrDefault(nombreRol, "👤");
        Color color = COLORES.getOrDefault(nombreRol, EstilosUI.ACCENT);

        JPanel card = new JPanel(new BorderLayout(14, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosUI.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));
        JLabel lblNombre = EstilosUI.labelNormal(nombreRol);
        lblNombre.setForeground(color);
        lblNombre.setFont(EstilosUI.FONT_BOLD);
        JLabel lblSub = EstilosUI.labelMuted(descripcionRol(nombreRol));
        texto.add(lblNombre);
        texto.add(lblSub);

        JLabel flecha = EstilosUI.labelMuted("›");
        flecha.setFont(new Font("Segoe UI", Font.BOLD, 20));

        card.add(lblIcono, BorderLayout.WEST);
        card.add(texto, BorderLayout.CENTER);
        card.add(flecha, BorderLayout.EAST);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                abrirPanel(nombreRol);
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

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(EstilosUI.BG_DARK);
        footer.setBorder(new EmptyBorder(10, 30, 20, 30));

        JButton cerrar = EstilosUI.botonFantasma("Cerrar sesión");
        cerrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        cerrar.setForeground(EstilosUI.DANGER);
        cerrar.addActionListener(e -> cerrarSesion());

        footer.add(cerrar, BorderLayout.CENTER);
        return footer;
    }

    // ── Navegación ─────────────────────────────────────────────────────────
    private void abrirPanel(String rol) {
        JFrame frame = resolverPantalla(rol);
        if (frame != null) {
            frame.setVisible(true);
            this.dispose();
        }
    }

    private JFrame resolverPantalla(String rol) {
        // Busca el objeto Rol que coincide con el nombre seleccionado
        Rol rolSeleccionado = usuario.getRolCollection()
                .stream()
                .filter(r -> r.getNombre().equals(rol))
                .findFirst()
                .orElse(null);

        return switch (rol) {
            case "Administrador" ->
                new PanelAdmin(usuario);
            case "Cajero" ->
                new PanelCajero(usuario, rolSeleccionado);
            case "Cocinero" ->
                new PanelCocineroBoceto(usuario, rolSeleccionado);
            case "Repartidor" ->
                new PanelDelivery(usuario, rolSeleccionado);
            default -> {
                JOptionPane.showMessageDialog(this, "Rol no reconocido: " + rol,
                        "Error", JOptionPane.ERROR_MESSAGE);
                yield null;
            }
        };
    }

    private void cerrarSesion() {
        int op = JOptionPane.showConfirmDialog(this,
                "¿Deseas cerrar sesión?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    private String inicialUsuario() {
        if (usuario == null || usuario.getNombre() == null
                || usuario.getNombre().isBlank()) {
            return "?";
        }
        return String.valueOf(usuario.getNombre().charAt(0)).toUpperCase();
    }

    private String descripcionRol(String rol) {
        return switch (rol) {
            case "Administrador" ->
                "Gestión completa del sistema";
            case "Cajero" ->
                "Pedidos, clientes y facturación";
            case "Cocinero" ->
                "Órdenes y estado de cocina";
            case "Repartidor" ->
                "Pedidos en ruta e historial";
            default ->
                "Acceder con este rol";
        };
    }
}
