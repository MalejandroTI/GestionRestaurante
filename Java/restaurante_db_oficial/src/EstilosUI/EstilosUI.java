/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EstilosUI;

/**
 *
 * @author ASUS
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * ═══════════════════════════════════════════════════════════════ EstilosUI —
 * hoja de estilos centralizada para toda la app
 *
 * USO EN CUALQUIER PANEL / FRAME:
 *
 * import utilUI.EstilosUI;
 *
 *      // Fondo del panel/frame EstilosUI.aplicarFondoOscuro(this);
 *
 *      // Crear componentes ya estilizados JButton btn =
 * EstilosUI.botonPrimario("Guardar"); JButton btn2 =
 * EstilosUI.botonSecundario("Cancelar"); JButton btn3 =
 * EstilosUI.botonPeligro("Eliminar"); JTextField tf = EstilosUI.campoTexto();
 * JPasswordField pf = EstilosUI.campoPassword(); JComboBox<String> cb =
 * EstilosUI.combo(new String[]{"A","B"}); JLabel titulo =
 * EstilosUI.labelTitulo("Mi Título"); JLabel normal =
 * EstilosUI.labelNormal("Texto"); JLabel muted =
 * EstilosUI.labelMuted("Subtexto"); JLabel badge = EstilosUI.badge("ACTIVO",
 * EstilosUI.SUCCESS); JPanel card = EstilosUI.card(); JPanel barra =
 * EstilosUI.barraLateral(); JSeparator sep = EstilosUI.separador();
 * EstilosUI.aplicarEstiloTabla(miJTable);
 *
 * ═══════════════════════════════════════════════════════════════
 */
public class EstilosUI {

    // ══════════════════════════════════════════════════════════════
    // PALETA DE COLORES  (accesibles desde cualquier clase)
    // ══════════════════════════════════════════════════════════════
    public static final Color BG_DARK = new Color(18, 22, 30);
    public static final Color BG_PANEL = new Color(26, 32, 44);
    public static final Color BG_CARD = new Color(30, 38, 52);
    public static final Color BG_ROW_EVEN = new Color(30, 38, 52);
    public static final Color BG_ROW_ODD = new Color(24, 30, 42);
    public static final Color BG_SIDEBAR = new Color(15, 18, 25);

    public static final Color ACCENT = new Color(99, 179, 237);
    public static final Color ACCENT_DARK = new Color(66, 153, 225);

    public static final Color TEXT_PRIMARY = new Color(237, 242, 247);
    public static final Color TEXT_MUTED = new Color(113, 128, 150);
    public static final Color TEXT_DISABLED = new Color(74, 85, 104);

    public static final Color BORDER = new Color(45, 55, 72);

    public static final Color SUCCESS = new Color(72, 187, 120);
    public static final Color WARNING = new Color(246, 173, 85);
    public static final Color DANGER = new Color(252, 129, 129);
    public static final Color INFO = new Color(99, 179, 237);
    public static final Color PURPLE = new Color(159, 122, 234);

    // Tipografías
    public static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITULO = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MUTED = new Font("Segoe UI", Font.ITALIC, 12);

    // Constructor privado — solo métodos estáticos
    private EstilosUI() {
    }

    // ══════════════════════════════════════════════════════════════
    // FONDOS / CONTENEDORES
    // ══════════════════════════════════════════════════════════════
    /**
     * Pinta el fondo oscuro a cualquier JPanel o JFrame
     *
     * @param componente
     */
    public static void aplicarFondoOscuro(JComponent componente) {
        componente.setBackground(BG_DARK);
    }

    /**
     * Sobrecarga específica para JFrame
     *
     * @param frame
     */
    public static void aplicarFondoOscuro(JFrame frame) {
        frame.getContentPane().setBackground(BG_DARK);
    }

    /**
     * Panel tipo "tarjeta" con borde sutil
     *
     * @return
     */
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(16, 20, 16, 20)));
        return p;
    }

    /**
     * Panel barra lateral oscura
     *
     * @return
     */
    public static JPanel barraLateral() {
        JPanel p = new JPanel();
        p.setBackground(BG_SIDEBAR);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));
        return p;
    }

    /**
     * Panel con fondo de panel (un tono más claro que BG_DARK)
     *
     * @return
     */
    public static JPanel panelSecundario() {
        JPanel p = new JPanel();
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        return p;
    }

    // ══════════════════════════════════════════════════════════════
    // BOTONES
    // ══════════════════════════════════════════════════════════════
    /**
     * Botón principal — fondo azul accent
     */
    public static JButton botonPrimario(String texto) {
        return crearBoton(texto, ACCENT, BG_DARK);
    }

    /**
     * Botón secundario — fondo oscuro, texto accent
     */
    public static JButton botonSecundario(String texto) {
        return crearBoton(texto, BG_PANEL, ACCENT);
    }

    /**
     * Botón de peligro — fondo rojo
     */
    public static JButton botonPeligro(String texto) {
        return crearBoton(texto, DANGER, BG_DARK);
    }

    /**
     * Botón de éxito — fondo verde
     */
    public static JButton botonExito(String texto) {
        return crearBoton(texto, SUCCESS, BG_DARK);
    }

    /**
     * Botón fantasma — sin relleno, solo borde
     */
    public static JButton botonFantasma(String texto) {
        JButton btn = crearBoton(texto, BG_DARK, TEXT_MUTED);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(6, 14, 6, 14)));
        return btn;
    }

    /**
     * Botón de barra lateral (ancho completo, alineado izquierda)
     */
    public static JButton botonNavegacion(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BG_CARD : BG_SIDEBAR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(FONT_NORMAL);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        return btn;
    }

    // ── constructor interno de botones ────────────────────────────
    private static JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = isEnabled() ? bg : bg.darker();
                g2.setColor(getModel().isRollover() && isEnabled() ? base.brighter() : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(fg);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        return btn;
    }

    // ══════════════════════════════════════════════════════════════
    // CAMPOS DE TEXTO
    // ══════════════════════════════════════════════════════════════
    public static JTextField campoTexto() {
        JTextField tf = new JTextField();
        estilizarCampo(tf);
        return tf;
    }

    public static JTextField campoTexto(int columnas) {
        JTextField tf = new JTextField(columnas);
        estilizarCampo(tf);
        return tf;
    }

    public static JPasswordField campoPassword() {
        JPasswordField pf = new JPasswordField();
        estilizarCampo(pf);
        return pf;
    }

    private static void estilizarCampo(JTextField tf) {
        tf.setBackground(BG_PANEL);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT);
        tf.setFont(FONT_NORMAL);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)));
    }

    /**
     * Aplica estilo a un campo ya existente (útil para campos del Form Editor)
     */
    public static void aplicarEstiloCampo(JTextField tf) {
        estilizarCampo(tf);
    }

    public static void aplicarEstiloCampo(JPasswordField pf) {
        estilizarCampo(pf);
    }

    // ══════════════════════════════════════════════════════════════
    // COMBOS
    // ══════════════════════════════════════════════════════════════
    public static <T> JComboBox<T> combo(T[] items) {
        JComboBox<T> cb = new JComboBox<>(items);
        estilizarCombo(cb);
        return cb;
    }

    /**
     * Aplica estilo a un combo ya existente
     */
    public static void aplicarEstiloCombo(JComboBox<?> cb) {
        estilizarCombo(cb);
    }

    private static void estilizarCombo(JComboBox<?> cb) {
        cb.setBackground(BG_PANEL);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_NORMAL);
        cb.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    // ══════════════════════════════════════════════════════════════
    // LABELS
    // ══════════════════════════════════════════════════════════════
    public static JLabel labelTitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FONT_TITULO);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    public static JLabel labelSubtitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FONT_SUBTITULO);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    public static JLabel labelNormal(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FONT_NORMAL);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    public static JLabel labelMuted(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FONT_SMALL);
        l.setForeground(TEXT_MUTED);
        return l;
    }

    /**
     * Badge de color: estado, tipo, etc. Ejemplo: EstilosUI.badge("PAGADO",
     * EstilosUI.SUCCESS)
     */
    public static JLabel badge(String texto, Color color) {
        JLabel l = new JLabel(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setForeground(color);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setBorder(new EmptyBorder(3, 8, 3, 8));
        l.setOpaque(false);
        return l;
    }

    /**
     * Aplica estilo a un JLabel ya existente (Form Editor)
     */
    public static void aplicarEstiloLabel(JLabel lbl, Font fuente, Color color) {
        lbl.setFont(fuente);
        lbl.setForeground(color);
    }

    // ══════════════════════════════════════════════════════════════
    // TABLA
    // ══════════════════════════════════════════════════════════════
    /**
     * Aplica el tema oscuro completo a una JTable existente. También estiliza
     * su header.
     */
    public static void aplicarEstiloTabla(JTable tabla) {
        tabla.setBackground(BG_ROW_EVEN);
        tabla.setForeground(TEXT_PRIMARY);
        tabla.setFont(FONT_NORMAL);
        tabla.setRowHeight(32);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(BORDER);
        tabla.setSelectionBackground(new Color(99, 179, 237, 60));
        tabla.setSelectionForeground(TEXT_PRIMARY);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setFillsViewportHeight(true);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(BG_PANEL);
        header.setForeground(TEXT_MUTED);
        header.setFont(FONT_BOLD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));
        header.setReorderingAllowed(false);

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(sel ? new Color(99, 179, 237, 60)
                        : (row % 2 == 0 ? BG_ROW_EVEN : BG_ROW_ODD));
                setForeground(TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                setFont(FONT_NORMAL);
                return this;
            }
        });
    }

    /**
     * Estiliza la tabla y colorea automáticamente la columna de estado.
     *
     * @param colEstado índice de la columna que contiene el estado (ej: 3)
     */
    public static void aplicarEstiloTablaConEstado(JTable tabla, int colEstado) {
        aplicarEstiloTabla(tabla);
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(sel ? new Color(99, 179, 237, 60)
                        : (row % 2 == 0 ? BG_ROW_EVEN : BG_ROW_ODD));
                setForeground(TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                setFont(FONT_NORMAL);
                if (col == colEstado && val != null) {
                    switch (val.toString().toUpperCase()) {
                        case "PAGADO", "ACTIVO", "COMPLETADO" ->
                            setForeground(SUCCESS);
                        case "PENDIENTE", "INACTIVO" ->
                            setForeground(WARNING);
                        case "CANCELADO", "BLOQUEADO" ->
                            setForeground(DANGER);
                        case "DELIVERY" ->
                            setForeground(PURPLE);
                        case "LOCAL" ->
                            setForeground(INFO);
                    }
                }
                return this;
            }
        });
    }

    /**
     * Estiliza el JScrollPane que envuelve una tabla
     */
    public static void aplicarEstiloScroll(JScrollPane scroll) {
        scroll.setBackground(BG_DARK);
        scroll.getViewport().setBackground(BG_ROW_EVEN);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));
    }

    // ══════════════════════════════════════════════════════════════
    // SEPARADORES
    // ══════════════════════════════════════════════════════════════
    public static JSeparator separador() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBackground(BG_DARK);
        return sep;
    }

    // ══════════════════════════════════════════════════════════════
    // VENTANAS (JFrame)
    // ══════════════════════════════════════════════════════════════
    /**
     * Aplica configuración visual base a un JFrame. Llamar en el constructor,
     * después de initComponents().
     */
    public static void aplicarEstiloVentana(JFrame frame, String titulo, int ancho, int alto) {
        frame.setTitle(titulo);
        frame.setSize(ancho, alto);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG_DARK);
    }

    // ══════════════════════════════════════════════════════════════
    // COLORES DE ESTADO  (para uso directo)
    // ══════════════════════════════════════════════════════════════
    /**
     * Devuelve el color correspondiente al estado textual. Útil para colorear
     * labels dinámicamente.
     *
     * Ejemplo:
     * lblEstado.setForeground(EstilosUI.colorDeEstado(pedido.getEstado().name()));
     */
    public static Color colorDeEstado(String estado) {
        if (estado == null) {
            return TEXT_MUTED;
        }
        return switch (estado.toUpperCase()) {
            case "PAGADO", "ACTIVO", "COMPLETADO" ->
                SUCCESS;
            case "PENDIENTE", "INACTIVO" ->
                WARNING;
            case "CANCELADO", "BLOQUEADO" ->
                DANGER;
            case "DELIVERY" ->
                PURPLE;
            case "LOCAL" ->
                INFO;
            default ->
                TEXT_MUTED;
        };
    }
    // ══════════════════════════════════════════════════════════════════════
// INTERACCIÓN / ANIMACIONES
// ══════════════════════════════════════════════════════════════════════

    /**
     * Animación shake horizontal — úsala cuando hay un error de validación.
     * Ejemplo: EstilosUI.shake(txtCorreo);
     */
    public static void shake(JComponent comp) {
        Point original = comp.getLocation();
        Timer timer = new Timer(30, null);
        int[] moves = {-8, 8, -6, 6, -4, 4, -2, 2, 0};
        int[] step = {0};
        timer.addActionListener(e -> {
            if (step[0] < moves.length) {
                comp.setLocation(original.x + moves[step[0]], original.y);
                step[0]++;
            } else {
                comp.setLocation(original);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    /**
     * Alterna mostrar/ocultar contraseña en un JPasswordField. Cambia el color
     * del botón para indicar el estado activo. Ejemplo:
     * EstilosUI.togglePassword(txtContrasena, btnOjo);
     */
    public static void togglePassword(JPasswordField campo, JButton btnOjo) {
        boolean oculto = campo.getEchoChar() != (char) 0;
        campo.setEchoChar(oculto ? (char) 0 : '●');
        btnOjo.setForeground(oculto ? ACCENT : TEXT_MUTED);
    }

// ══════════════════════════════════════════════════════════════════════
// COMPONENTES COMPUESTOS
// ══════════════════════════════════════════════════════════════════════
    /**
     * Panel de contraseña con campo + botón ojo ya integrado. Devuelve un
     * array: [0] = JPanel, [1] = JPasswordField, [2] = JButton (ojo)
     *
     * Ejemplo: Object[] resultado = EstilosUI.panelPassword(); JPanel panel =
     * (JPanel) resultado[0]; JPasswordField pf = (JPasswordField)resultado[1];
     */
    public static Object[] panelPassword() {
        JPasswordField campo = new JPasswordField();
        aplicarEstiloCampo(campo);

        JButton btnOjo = new JButton("👁");
        btnOjo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btnOjo.setBackground(BG_PANEL);
        btnOjo.setForeground(TEXT_MUTED);
        btnOjo.setFocusPainted(false);
        btnOjo.setBorderPainted(false);
        btnOjo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOjo.setPreferredSize(new Dimension(38, 38));
        btnOjo.setToolTipText("Mostrar/ocultar contraseña");
        btnOjo.addActionListener(e -> togglePassword(campo, btnOjo));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panel.add(campo, BorderLayout.CENTER);
        panel.add(btnOjo, BorderLayout.EAST);

        return new Object[]{panel, campo, btnOjo};
    }

    /**
     * Card con bordes redondeados y fondo BG_PANEL.
     *
     * @param radio radio de las esquinas (ej: 20)
     */
    public static JPanel cardRedondeada(int radio) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        return card;
    }
}
