/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelesPrincipales;

/**
 *
 * @author ASUS
 */
import Clases.Usuario;
import Clases.Rol;
import EstilosUI.EstilosUI;
import FuncionalidadBotones.PanelHistorialCocinero;
import FuncionalidadBotones.PanelPedidosPendientesCocinero;
import PresentacionJFRAME.SelectorDeRol;
import javax.swing.*;
import java.awt.*;
 
public class PanelCocinero extends JFrame {
 
    private Usuario usuarioActual;
    private Rol rolActual;
 
    private JLabel nombreCocinero;
    private JButton btnPedidosPendientes;
    private JButton btnHistorial;
    private JButton btnRegresar;
 
    public PanelCocinero(Usuario usuarioActual, Rol rolActual) {
        this.usuarioActual = usuarioActual;
        this.rolActual = rolActual;
        initUI();
    }

    private void initUI() {
        EstilosUI.aplicarFondoOscuro(this);
        EstilosUI.aplicarEstiloVentana(this, "Panel Cocinero", 500, 320);
 
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        add(crearHeader(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
    }
 
    private JPanel crearHeader() {
        JPanel header = EstilosUI.panelSecundario();
        header.setLayout(new BorderLayout());
 
        JLabel saludo = EstilosUI.labelTitulo("Hola, ");
        nombreCocinero = EstilosUI.labelSubtitulo(usuarioActual.getNombre());
 
        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        izquierda.setOpaque(false);
        izquierda.add(saludo);
        izquierda.add(nombreCocinero);
 
        // Rol badge
        JLabel lblRol = EstilosUI.labelSubtitulo("  [" + rolActual.getNombre() + "]");
        lblRol.setForeground(new Color(255, 165, 0)); // naranja para destacar el rol
        izquierda.add(lblRol);
 
        btnRegresar = EstilosUI.botonPeligro("Cerrar sesión");
        btnRegresar.addActionListener(e -> cerrarSesion());
 
        header.add(izquierda, BorderLayout.WEST);
        header.add(btnRegresar, BorderLayout.EAST);
 
        return header;
    }
 
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
 
        btnPedidosPendientes = EstilosUI.botonPrimario("🍳  Pedidos Pendientes");
        btnHistorial         = EstilosUI.botonSecundario("📋  Historial");
 
        btnPedidosPendientes.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnHistorial.setFont(new Font("Segoe UI", Font.BOLD, 15));
 
        btnPedidosPendientes.addActionListener(e -> abrirPedidosPendientes());
        btnHistorial.addActionListener(e -> abrirHistorial());
 
        panel.add(btnPedidosPendientes);
        panel.add(btnHistorial);
 
        return panel;
    }
 
    // ACCIONES
    private void abrirPedidosPendientes() {
        new PanelPedidosPendientesCocinero(usuarioActual, rolActual).setVisible(true);
    }
 
    private void abrirHistorial() {
        new PanelHistorialCocinero(usuarioActual).setVisible(true);
    }
 
    private void cerrarSesion() {
        int op = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar sesión?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );
        if (op == JOptionPane.YES_OPTION) {
            new SelectorDeRol(usuarioActual).setVisible(true);
            dispose();
        }
    }
}