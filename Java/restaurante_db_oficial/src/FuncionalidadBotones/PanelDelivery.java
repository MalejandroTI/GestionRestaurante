/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.EntregaPedido;
import Clases.Rol;
import Clases.Usuario;
import EstilosUI.EstilosUI;
import PresentacionJFRAME.LoginFrame;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import servicios.EntregaService;

/**
 * Ventana principal del repartidor. JFrame independiente — se abre desde
 * SelectorDeRol.
 */
public class PanelDelivery extends JFrame {

    private final Usuario usuario;
    private final EntregaService entregaService;
    private final Rol rolActual;

    private int contadorEnRuta = 0;

    private JButton btnPedidosNuevos;
    private JButton btnHistorial;
    private JButton btnCerrarSesion;

    private JLabel lblBadgeEnRuta;
    private JLabel lblBadgeHistorial;
    private JLabel lblEstadoRefresco;

    private Timer timerRefresco;

    // ✔ evita múltiples SwingWorkers simultáneos
    private final AtomicBoolean refrescando = new AtomicBoolean(false);

    public PanelDelivery(Usuario usuario, Rol rolActual) {

        this.usuario = usuario;
        this.entregaService = new EntregaService();
        this.rolActual = rolActual;

        initUI();
        EstilosUI.aplicarEstiloVentana(this,
                "Panel Delivery — " + usuario.getNombre(), 500, 340);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        refrescar();
        iniciarAutoRefresco();
    }

    private void initUI() {
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(EstilosUI.BG_DARK);
        setContentPane(fondo);

        fondo.add(buildHeader(), BorderLayout.NORTH);
        fondo.add(buildCentro(), BorderLayout.CENTER);
        fondo.add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {

        JPanel header = EstilosUI.panelSecundario();
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JPanel saludo = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        saludo.setOpaque(false);

        saludo.add(EstilosUI.labelMuted("Hola,"));
        saludo.add(EstilosUI.labelSubtitulo(usuario.getNombre() + " 🛵"));

        btnCerrarSesion = EstilosUI.botonPeligro("Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        header.add(saludo, BorderLayout.WEST);
        header.add(btnCerrarSesion, BorderLayout.EAST);

        return header;
    }

    private JPanel buildCentro() {

        JPanel centro = new JPanel(new GridLayout(1, 2, 20, 0));
        centro.setBackground(EstilosUI.BG_DARK);
        centro.setBorder(new EmptyBorder(30, 30, 20, 30));

        JPanel cardEnRuta = buildTarjetaAccion("📦", "Pedidos en ruta", EstilosUI.ACCENT);
        lblBadgeEnRuta = (JLabel) cardEnRuta.getClientProperty("badge");
        btnPedidosNuevos = (JButton) cardEnRuta.getClientProperty("btn");
        btnPedidosNuevos.addActionListener(e -> abrirEnRuta());

        JPanel cardHistorial = buildTarjetaAccion("📋", "Historial", EstilosUI.SUCCESS);
        lblBadgeHistorial = (JLabel) cardHistorial.getClientProperty("badge");
        btnHistorial = (JButton) cardHistorial.getClientProperty("btn");
        btnHistorial.addActionListener(e -> abrirHistorial());

        centro.add(cardEnRuta);
        centro.add(cardHistorial);

        return centro;
    }

    private JPanel buildTarjetaAccion(String icono, String texto, Color color) {

        JPanel card = EstilosUI.cardRedondeada(14);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblIcono = new JLabel(icono, SwingConstants.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        lblIcono.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblTexto = EstilosUI.labelNormal(texto);
        lblTexto.setAlignmentX(CENTER_ALIGNMENT);

        JLabel badge = EstilosUI.badge("0", color);
        badge.setAlignmentX(CENTER_ALIGNMENT);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton btn = EstilosUI.botonPrimario("Ver");
        btn.setBackground(color);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(CENTER_ALIGNMENT);

        card.add(lblIcono);
        card.add(Box.createVerticalStrut(8));
        card.add(badge);
        card.add(Box.createVerticalStrut(4));
        card.add(lblTexto);
        card.add(Box.createVerticalGlue());
        card.add(btn);

        card.putClientProperty("badge", badge);
        card.putClientProperty("btn", btn);

        return card;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(EstilosUI.BG_DARK);

        lblEstadoRefresco = EstilosUI.labelMuted("Actualizando...");
        footer.add(lblEstadoRefresco);

        return footer;
    }

    private void iniciarAutoRefresco() {

        timerRefresco = new Timer(10_000, e -> refrescar());
        timerRefresco.setInitialDelay(10_000);
        timerRefresco.start();
    }

    private void refrescar() {

        // ✔ evita solapamiento de threads
        if (!refrescando.compareAndSet(false, true)) {
            return;
        }

        new SwingWorker<int[], Void>() {

            @Override
            protected int[] doInBackground() {

                return new int[]{
                    entregaService.obtenerEnRuta(usuario).size(),
                    entregaService.obtenerEntregados(usuario).size()
                };
            }

            @Override
            protected void done() {

                try {
                    int[] counts = get();

                    int enRutaActual = counts[0];

                    if (enRutaActual > contadorEnRuta) {
                        Toolkit.getDefaultToolkit().beep();
                    }

                    contadorEnRuta = enRutaActual;

                    lblBadgeEnRuta.setText(String.valueOf(enRutaActual));
                    lblBadgeHistorial.setText(String.valueOf(counts[1]));

                    lblBadgeEnRuta.setForeground(
                            enRutaActual > 0
                                    ? EstilosUI.WARNING
                                    : EstilosUI.TEXT_MUTED
                    );

                    lblEstadoRefresco.setText(
                            entregaService.obtenerTextoActualizacion()
                    );

                } catch (Exception ex) {
                    lblEstadoRefresco.setText(
                            entregaService.obtenerTextoErrorActualizacion()
                    );
                } finally {
                    refrescando.set(false);
                }
            }
        }.execute();
    }

    private void abrirEnRuta() {

        List<EntregaPedido> pedidos
                = entregaService.obtenerEnRuta(usuario);

        new PanelPedidosDelivery(pedidos, usuario).setVisible(true);
    }

    private void abrirHistorial() {

        List<EntregaPedido> pedidos
                = entregaService.obtenerEntregados(usuario);

        new PanelPedidosHistorial(pedidos, usuario).setVisible(true);
    }

    private void cerrarSesion() {

        int op = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar sesión?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (op == JOptionPane.YES_OPTION) {

            if (timerRefresco != null) {
                timerRefresco.stop();
            }

            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
