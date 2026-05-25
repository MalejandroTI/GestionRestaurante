/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FuncionalidadBotones;

import Clases.Rol;
import Clases.Usuario;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import servicios.UsuarioService;

public class ListaEmpleados extends JPanel {

    private final UsuarioService usuarioService;
    private final PanelAdmin panelAdmin;
    private List<Usuario> listaUsuarios;
    private List<Usuario> listaVisible;
    private final DefaultTableModel modelo;

    public ListaEmpleados(PanelAdmin panelAdmin) {

        this.panelAdmin = panelAdmin;

        initComponents();

        usuarioService = new UsuarioService();

        modelo = new DefaultTableModel();

        tablaUsuarios.setModel(modelo);

        configurarTabla();

        cargarUsuarios();

        tablaUsuarios.getColumn("Detalle")
                .setCellRenderer(new ButtonRenderer());

        tablaUsuarios.getColumn("Detalle")
                .setCellEditor(
                        new ButtonEditor(
                                new JCheckBox(),
                                tablaUsuarios
                        )
                );
    }

    private void configurarTabla() {

        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Rol");
        modelo.addColumn("Celular");
        modelo.addColumn("Correo");
        modelo.addColumn("Fecha Nac");
        modelo.addColumn("Cédula");
        modelo.addColumn("Activo");
        modelo.addColumn("Detalle");
    }

    private void cargarUsuarios() {

        listaUsuarios = usuarioService.obtenerUsuarios();

        mostrarTabla(listaUsuarios);
    }

    private void mostrarTabla(List<Usuario> lista) {

        listaVisible = lista;

        modelo.setRowCount(0);

        for (Usuario u : lista) {

            String roles = u.getRolCollection()
                    .stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.joining(", "));

            modelo.addRow(new Object[]{
                u.getIdUsuario(),
                u.getNombre(),
                u.getApellido(),
                roles,
                u.getCelular(),
                u.getCorreo(),
                u.getFechaNacimiento(),
                u.getCedula(),
                u.getActivo(),
                "Ver"
            });
        }
    }

    private void buscar(String texto) {

        List<Usuario> filtrados
                = usuarioService.buscarUsuarios(texto);

        mostrarTabla(filtrados);
    }

    private void filtrarPorActivo(boolean activo) {

        List<Usuario> filtrados
                = usuarioService.filtrarPorActivo(activo);

        mostrarTabla(filtrados);
    }

    class ButtonRenderer extends JButton
            implements javax.swing.table.TableCellRenderer {

        public ButtonRenderer() {
            setText("Ver");
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private final JButton button;

        private final JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable table) {

            super(checkBox);

            this.table = table;

            button = new JButton("Ver");

            button.addActionListener((ActionEvent e) -> {
                int row = table.convertRowIndexToModel(
                        table.getEditingRow()
                );

                if (row < 0) {
                    return;
                }

                Usuario u = listaVisible.get(row);

                JTextArea area = new JTextArea(
                        """
                        ===== DETALLE USUARIO =====
                            ID: """ + u.getIdUsuario() + "\n"
                        + "Nombre: " + u.getNombre() + "\n"
                        + "Apellido: " + u.getApellido() + "\n"
                        + "Cédula: " + u.getCedula() + "\n"
                        + "Celular: " + u.getCelular() + "\n"
                        + "Correo: " + u.getCorreo() + "\n"
                        + "Fecha Nacimiento: " + u.getFechaNacimiento() + "\n"
                        + "Activo: " + u.getActivo()
                );

                area.setEditable(false);

                JButton btnAccion = new JButton();

                boolean activo = Boolean.TRUE.equals(u.getActivo());

                btnAccion.setText(
                        activo ? "Despedir" : "Recontratar"
                );

                JDialog dialog = new JDialog();

                dialog.setTitle("Detalle Usuario");

                dialog.setLayout(new BorderLayout());

                dialog.add(new JScrollPane(area),
                        BorderLayout.CENTER);

                btnAccion.addActionListener(ev -> {

                    try {

                        usuarioService.cambiarEstadoUsuario(u);

                        JOptionPane.showMessageDialog(dialog,
                                activo
                                        ? "Empleado despedido"
                                        : "Empleado recontratado");

                        dialog.dispose();

                        cargarUsuarios();

                    } catch (Exception ex) {

                        JOptionPane.showMessageDialog(dialog,
                                "Error: " + ex.getMessage());
                    }
                });

                dialog.add(btnAccion, BorderLayout.SOUTH);

                dialog.setSize(450, 350);

                dialog.setLocationRelativeTo(button);

                dialog.setVisible(true);
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Ver";
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        btnBuscarPorTexto = new javax.swing.JButton();
        btnActivos = new javax.swing.JButton();
        btnInactivos = new javax.swing.JButton();
        btnTodos = new javax.swing.JButton();

        jScrollPane1 = new javax.swing.JScrollPane();

        tablaUsuarios = new javax.swing.JTable();

        txtBuscar = new javax.swing.JTextField();

        lblTitulo = new javax.swing.JLabel();

        btnRegresar = new javax.swing.JButton();

        btnBuscarPorTexto.setText("BUSCAR");

        btnBuscarPorTexto.addActionListener(evt -> {
            buscar(txtBuscar.getText());
        });

        btnActivos.setText("ACTIVOS");

        btnActivos.addActionListener(evt -> {
            filtrarPorActivo(true);
        });

        btnInactivos.setText("INACTIVOS");

        btnInactivos.addActionListener(evt -> {
            filtrarPorActivo(false);
        });

        btnTodos.setText("TODOS");

        btnTodos.addActionListener(evt -> {
            cargarUsuarios();
        });
        lblTitulo.setText("LISTA DE EMPLEADOS");

        btnRegresar.setText("REGRESAR");

        btnRegresar.addActionListener(evt -> {
            panelAdmin.mostrarPanelPrincipal();
        });

        tablaUsuarios.setModel(
                new DefaultTableModel(
                        new Object[][]{},
                        new String[]{}
                )
        );

        jScrollPane1.setViewportView(tablaUsuarios);

        javax.swing.GroupLayout layout
                = new javax.swing.GroupLayout(this);

        this.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnActivos)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnInactivos)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnTodos)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtBuscar)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnBuscarPorTexto))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnRegresar)
                                                .addGap(300, 300, 300)
                                                .addComponent(lblTitulo)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(20, 20, 20))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnRegresar)
                                        .addComponent(lblTitulo))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtBuscar,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnBuscarPorTexto))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnActivos)
                                        .addComponent(btnInactivos)
                                        .addComponent(btnTodos))
                                .addGap(20, 20, 20)
                                .addComponent(jScrollPane1,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        350,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(20, Short.MAX_VALUE))
        );
    }

    // VARIABLES
    private javax.swing.JButton btnActivos;
    private javax.swing.JButton btnBuscarPorTexto;
    private javax.swing.JButton btnInactivos;
    private javax.swing.JButton btnTodos;
    private javax.swing.JButton btnRegresar;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JLabel lblTitulo;

    private javax.swing.JTable tablaUsuarios;

    private javax.swing.JTextField txtBuscar;
}
