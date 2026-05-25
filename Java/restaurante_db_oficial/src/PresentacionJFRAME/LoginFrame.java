/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PresentacionJFRAME;

import Clases.Usuario;
import EstilosUI.EstilosUI;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.persistence.EntityManagerFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import servicios.AutenticacionService;
import utilJpa.JPAUtil;

public class LoginFrame extends JFrame {

    // ── Dependencias ───────────────────────────────────────────────────────
    private final EntityManagerFactory emf;
    private final AutenticacionService authService;

    // ── Componentes ────────────────────────────────────────────────────────
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton botonLogin;
    private JLabel lblError;

    // ══════════════════════════════════════════════════════════════════════
    public LoginFrame() {

        this.emf = JPAUtil.getEMF();
        this.authService = new AutenticacionService(emf);

        initComponents();

        setTitle("Restaurante UTPL — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        pack();
        setLocationRelativeTo(null);
    }

    // ── UI ─────────────────────────────────────────────────────────────────
    private void initComponents() {

        // Fondo
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBackground(EstilosUI.BG_DARK);
        fondo.setPreferredSize(new Dimension(460, 520));

        setContentPane(fondo);

        // Card principal
        JPanel card = EstilosUI.cardRedondeada(20);

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setPreferredSize(
                new Dimension(360, 420)
        );

        card.setBorder(
                new EmptyBorder(40, 40, 40, 40)
        );

        // ── Ícono ──────────────────────────────────────────────────────────
        JLabel icono = new JLabel(
                "🍽",
                SwingConstants.CENTER
        );

        icono.setFont(
                new Font("Segoe UI Emoji", Font.PLAIN, 48)
        );

        icono.setAlignmentX(CENTER_ALIGNMENT);

        // ── Títulos ────────────────────────────────────────────────────────
        JLabel titulo = EstilosUI.labelTitulo("Bienvenido");

        titulo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        titulo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitulo = EstilosUI.labelMuted(
                "Inicia sesión para continuar"
        );

        subtitulo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        subtitulo.setAlignmentX(CENTER_ALIGNMENT);

        // ── Correo ─────────────────────────────────────────────────────────
        JLabel lblCorreo = EstilosUI.labelMuted(
                "Correo electrónico"
        );

        lblCorreo.setAlignmentX(CENTER_ALIGNMENT);

        txtCorreo = EstilosUI.campoTexto();

        txtCorreo.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );

        txtCorreo.setAlignmentX(CENTER_ALIGNMENT);

        txtCorreo.addActionListener(
                e -> txtContrasena.requestFocus()
        );

        // ── Contraseña ─────────────────────────────────────────────────────
        JLabel lblClave = EstilosUI.labelMuted("Contraseña");

        lblClave.setAlignmentX(CENTER_ALIGNMENT);

        Object[] passPanel = EstilosUI.panelPassword();

        JPanel panelPass = (JPanel) passPanel[0];

        txtContrasena = (JPasswordField) passPanel[1];

        panelPass.setAlignmentX(CENTER_ALIGNMENT);

        txtContrasena.addActionListener(
                e -> intentarLogin()
        );

        // ── Label error ────────────────────────────────────────────────────
        lblError = new JLabel(
                " ",
                SwingConstants.CENTER
        );

        lblError.setFont(EstilosUI.FONT_SMALL);

        lblError.setForeground(
                EstilosUI.DANGER
        );

        lblError.setAlignmentX(CENTER_ALIGNMENT);

        // ── Botón login ────────────────────────────────────────────────────
        botonLogin = EstilosUI.botonPrimario("ENTRAR");

        botonLogin.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42)
        );

        botonLogin.setAlignmentX(CENTER_ALIGNMENT);

        botonLogin.addActionListener(
                e -> intentarLogin()
        );

        // ── Ensamblar ──────────────────────────────────────────────────────
        card.add(icono);
        card.add(Box.createVerticalStrut(10));

        card.add(titulo);
        card.add(Box.createVerticalStrut(4));

        card.add(subtitulo);
        card.add(Box.createVerticalStrut(28));

        card.add(lblCorreo);
        card.add(Box.createVerticalStrut(6));

        card.add(txtCorreo);
        card.add(Box.createVerticalStrut(16));

        card.add(lblClave);
        card.add(Box.createVerticalStrut(6));

        card.add(panelPass);
        card.add(Box.createVerticalStrut(8));

        card.add(lblError);
        card.add(Box.createVerticalStrut(16));

        card.add(botonLogin);

        fondo.add(card);
    }

    // ── Login ──────────────────────────────────────────────────────────────
    private void intentarLogin() {

        String correo = txtCorreo.getText().trim();

        String password = new String(
                txtContrasena.getPassword()
        );

        if (correo.isBlank()) {

            mostrarError(
                    "Ingresa el correo.",
                    txtCorreo
            );

            return;
        }

        if (password.isBlank()) {

            mostrarError(
                    "Ingresa la contraseña.",
                    txtContrasena
            );

            return;
        }

        setEstadoCarga(true);

        SwingWorker<Usuario, Void> worker
                = new SwingWorker<>() {

            @Override
            protected Usuario doInBackground()
                    throws Exception {

                return authService.login(
                        correo,
                        password
                );
            }

            @Override
            protected void done() {

                try {

                    Usuario usuario = get();

                    onLoginExitoso(usuario);

                } catch (Exception ex) {

                    mostrarError(
                            ex.getMessage(),
                            txtContrasena
                    );

                } finally {

                    setEstadoCarga(false);
                }
            }
        };

        worker.execute();
    }

    // ── Navegación ─────────────────────────────────────────────────────────
    private void onLoginExitoso(
            Usuario usuario
    ) {

        SelectorDeRol selector
                = new SelectorDeRol(usuario);

        selector.setLocationRelativeTo(null);

        selector.setVisible(true);

        dispose();
    }

    // ── Helpers UI ────────────────────────────────────────────────────────
    private void mostrarError(
            String mensaje,
            JComponent componente
    ) {

        lblError.setText(mensaje);

        EstilosUI.shake(componente);
    }

    private void setEstadoCarga(
            boolean cargando
    ) {

        botonLogin.setEnabled(!cargando);

        botonLogin.setText(
                cargando
                ? "Verificando..."
                : "ENTRAR"
        );

        if (!cargando) {

            lblError.setText(" ");
        }
    }

    // ── Main ───────────────────────────────────────────────────────────────
    public static void main(String args[]) {

        try {

            for (UIManager.LookAndFeelInfo info
                    : UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(info.getName())) {

                    UIManager.setLookAndFeel(
                            info.getClassName()
                    );

                    break;
                }
            }

        } catch (
                ClassNotFoundException
                | IllegalAccessException
                | InstantiationException
                | UnsupportedLookAndFeelException ex
        ) {

            java.util.logging.Logger
                    .getLogger(LoginFrame.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(
                () -> new LoginFrame().setVisible(true)
        );
    }
}