/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.Cliente;
import EstilosUI.EstilosUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import servicios.ClienteService;

public class ListaClientesCajero extends JFrame {

    private final ClienteService clienteService;
    private final DefaultTableModel modelo;

    private List<Cliente> listaClientes;

    private JTextField txtBuscar;
    private JTable tablaClientes;

    public ListaClientesCajero() {
        clienteService = new ClienteService();
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        configurarTabla();
        initUI();   
        cargarClientes();
    }

    // ═══════════════════════════════════════
    // UI PRINCIPAL
    // ═══════════════════════════════════════
    private void initUI() {
        EstilosUI.aplicarEstiloVentana(this, "Lista de Clientes", 700, 540);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(560, 420));

        JPanel fondo = new JPanel(new BorderLayout(0, 0));
        fondo.setBackground(EstilosUI.BG_DARK);
        setContentPane(fondo);

        fondo.add(crearHeader(), BorderLayout.NORTH);
        fondo.add(crearBarra(), BorderLayout.CENTER);
    }

    // ═══════════════════════════════════════
    // HEADER
    // ═══════════════════════════════════════
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(EstilosUI.BG_PANEL);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, EstilosUI.BORDER),
                new EmptyBorder(14, 20, 14, 20)));

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        izq.setOpaque(false);

        JLabel icono = new JLabel("👥");
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        JLabel titulo = EstilosUI.labelSubtitulo("Lista de Clientes");
        JLabel badge = EstilosUI.badge("CLIENTES", EstilosUI.ACCENT);

        izq.add(icono);
        izq.add(titulo);
        izq.add(badge);

        JButton btnNuevo = EstilosUI.botonExito("+ Nuevo Cliente");
        btnNuevo.addActionListener(e -> abrirNuevoCliente());

        JButton btnCerrar = EstilosUI.botonFantasma("Cerrar");
        btnCerrar.setForeground(EstilosUI.DANGER);
        btnCerrar.addActionListener(e -> dispose());

        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        der.setOpaque(false);
        der.add(btnNuevo);
        der.add(btnCerrar);

        header.add(izq, BorderLayout.WEST);
        header.add(der, BorderLayout.EAST);

        return header;
    }

    // ═══════════════════════════════════════
    // BARRA BÚSQUEDA + TABLA
    // ═══════════════════════════════════════
    private JPanel crearBarra() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(EstilosUI.BG_DARK);
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));

        panel.add(crearFilaBusqueda(), BorderLayout.NORTH);
        panel.add(crearTabla(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearFilaBusqueda() {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);

        txtBuscar = EstilosUI.campoTexto();
        txtBuscar.setToolTipText("Buscar por nombre, apellido o cédula");

        // Búsqueda en tiempo real al escribir
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                buscar(txtBuscar.getText());
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                buscar(txtBuscar.getText());
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                buscar(txtBuscar.getText());
            }
        });

        JButton btnBuscar = EstilosUI.botonPrimario("🔍  Buscar");
        btnBuscar.addActionListener(e -> buscar(txtBuscar.getText()));

        JButton btnLimpiar = EstilosUI.botonFantasma("✕  Limpiar");
        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            mostrarTabla(listaClientes);
        });

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        botones.add(btnBuscar);
        botones.add(btnLimpiar);

        fila.add(txtBuscar, BorderLayout.CENTER);
        fila.add(botones, BorderLayout.EAST);

        return fila;
    }

    private JScrollPane crearTabla() {
        tablaClientes = new JTable(modelo);
        EstilosUI.aplicarEstiloTabla(tablaClientes);

        // Anchos de columna
        tablaClientes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaClientes.getColumnModel().getColumn(1).setPreferredWidth(160);
        tablaClientes.getColumnModel().getColumn(2).setPreferredWidth(160);
        tablaClientes.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaClientes.getColumnModel().getColumn(4).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(tablaClientes);
        EstilosUI.aplicarEstiloScroll(scroll);

        return scroll;
    }

    // ═══════════════════════════════════════
    // DATOS
    // ═══════════════════════════════════════
    private void configurarTabla() {
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Cédula");
        modelo.addColumn("Celular");
    }

    private void cargarClientes() {
        listaClientes = clienteService.obtenerClientes();
        mostrarTabla(listaClientes);
    }

    private void mostrarTabla(List<Cliente> lista) {
        modelo.setRowCount(0);
        if (lista == null || lista.isEmpty()) {
            return;
        }

        for (Cliente c : lista) {
            modelo.addRow(new Object[]{
                c.getIdCliente(),
                c.getNombre(),
                c.getApellido(),
                c.getCedula(),
                c.getCelular()
            });
        }

        // Actualiza el badge del header con el conteo actual
        setTitle("Lista de Clientes  (" + lista.size() + ")");
    }

    private void buscar(String texto) {
        List<Cliente> filtrados = clienteService.buscarClientes(texto);
        mostrarTabla(filtrados);
    }

    // ═══════════════════════════════════════
    // ACCIONES
    // ═══════════════════════════════════════
    private void abrirNuevoCliente() {
        AgregarCliente ventana = new AgregarCliente();
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }
}
