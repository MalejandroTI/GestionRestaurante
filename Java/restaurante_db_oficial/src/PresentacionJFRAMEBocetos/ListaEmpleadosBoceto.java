/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PresentacionJFRAMEBocetos;

/**
 *
 * @author ASUS
 */
import Clases.Usuario;
import Clases.Rol;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import logica.UsuarioJpaController;

public class ListaEmpleadosBoceto extends javax.swing.JFrame {

    private List<Usuario> listaUsuarios;
    private final DefaultTableModel modelo;
    private List<Usuario> listaVisible;

    /**
     * Creates new form ListaEmpleados
     */
    public ListaEmpleadosBoceto() {

        initComponents();

        modelo = new DefaultTableModel();
        tablaUsuarios.setModel(modelo);

        configurarTabla();
        cargarUsuarios();

        tablaUsuarios.getColumn("Detalle")
                .setCellRenderer(new ButtonRenderer());

        tablaUsuarios.getColumn("Detalle")
                .setCellEditor(new ButtonEditor(new javax.swing.JCheckBox(), tablaUsuarios));
    }

    private EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("restaurante_db_oficialPU");

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

        UsuarioJpaController controller = new UsuarioJpaController(emf);

        listaUsuarios = controller.findUsuarioEntities();

        mostrarTabla(listaUsuarios);
    }

    private void mostrarTabla(List<Usuario> lista) {

        listaVisible = lista; // 👈 CLAVE

        modelo.setRowCount(0);

        for (Usuario u : lista) {

            String roles = u.getRolCollection()
                    .stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.joining(","));

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

    class ButtonRenderer extends javax.swing.JButton
            implements javax.swing.table.TableCellRenderer {

        public ButtonRenderer() {
            setText("Ver");
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private JButton button;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);

            this.table = table;
            button = new JButton("Ver");

            button.addActionListener(e -> {

                int row = table.convertRowIndexToModel(table.getSelectedRow());
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
                area.setLineWrap(true);
                area.setWrapStyleWord(true);

                JButton btnAccion = new JButton();

                if (Boolean.TRUE.equals(u.getActivo())) {
                    btnAccion.setText("Despedir");
                } else {
                    btnAccion.setText("Recontratar");
                }

                JDialog dialog = new JDialog();
                dialog.setTitle("Detalle Usuario");
                dialog.setLayout(new java.awt.BorderLayout());

                dialog.add(new JScrollPane(area), java.awt.BorderLayout.CENTER);
                boolean activo = Boolean.TRUE.equals(u.getActivo());

                btnAccion.setText(activo ? "Despedir" : "Recontratar");

                btnAccion.addActionListener(ev -> {

                    try {
                        UsuarioJpaController controller
                                = new UsuarioJpaController(emf);

                        if (Boolean.TRUE.equals(u.getActivo())) {
                            u.setActivo(false);
                            JOptionPane.showMessageDialog(dialog, "Empleado despedido");
                        } else {
                            u.setActivo(true);
                            JOptionPane.showMessageDialog(dialog, "Empleado recontratado");
                        }

                        controller.edit(u);

                        dialog.dispose();
                        cargarUsuarios();

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
                    }
                });
                dialog.add(btnAccion, java.awt.BorderLayout.SOUTH);
                dialog.setSize(450, 350);
                dialog.setLocationRelativeTo(button);
                dialog.setVisible(true);
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Ver";
        }
    }

    private void buscar(String texto) {

        String t = texto.toLowerCase();

        List<Usuario> filtrados = listaUsuarios.stream()
                .filter(u
                        -> String.valueOf(u.getIdUsuario()).contains(t)
                || u.getNombre().toLowerCase().contains(t)
                || u.getApellido().toLowerCase().contains(t)
                || u.getCedula().contains(t)
                || u.getCorreo().toLowerCase().contains(t)
                )
                .collect(Collectors.toList());

        mostrarTabla(filtrados);
    }

    private void filtrarPorActivo(boolean activo) {

        List<Usuario> filtrados = listaUsuarios.stream()
                .filter(u -> u.getActivo() == activo)
                .collect(Collectors.toList());

        mostrarTabla(filtrados);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBuscarPorTexto = new javax.swing.JButton();
        btnActivos = new javax.swing.JButton();
        btnInactivos = new javax.swing.JButton();
        btnTodos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUsuarios = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnBuscarPorTexto.setText("BUSCAR");
        btnBuscarPorTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPorTextoActionPerformed(evt);
            }
        });

        btnActivos.setText("ACTIVOS");
        btnActivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivosActionPerformed(evt);
            }
        });

        btnInactivos.setText("INACTIVOS");
        btnInactivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInactivosActionPerformed(evt);
            }
        });

        btnTodos.setText("TODOS");
        btnTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTodosActionPerformed(evt);
            }
        });

        tablaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaUsuarios);

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        jLabel1.setText("LISTA DE EMPLEADOS");

        jButton1.setText("REGRESAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnActivos)
                        .addGap(18, 18, 18)
                        .addComponent(btnInactivos)
                        .addGap(18, 18, 18)
                        .addComponent(btnTodos)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtBuscar)
                                .addGap(18, 18, 18)
                                .addComponent(btnBuscarPorTexto)))
                        .addGap(31, 31, 31))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 429, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(482, 482, 482))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarPorTexto))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActivos)
                    .addComponent(btnInactivos)
                    .addComponent(btnTodos))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarPorTextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPorTextoActionPerformed
        // TODO add your handling code here:
        buscar(txtBuscar.getText());

    }//GEN-LAST:event_btnBuscarPorTextoActionPerformed

    private void btnInactivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInactivosActionPerformed
        // TODO add your handling code here:
        filtrarPorActivo(false);
    }//GEN-LAST:event_btnInactivosActionPerformed

    private void btnTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTodosActionPerformed
        // TODO add your handling code here:
        mostrarTabla(listaUsuarios);
    }//GEN-LAST:event_btnTodosActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnActivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivosActionPerformed
        // TODO add your handling code here:
        filtrarPorActivo(true);
    }//GEN-LAST:event_btnActivosActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListaEmpleadosBoceto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListaEmpleadosBoceto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListaEmpleadosBoceto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListaEmpleadosBoceto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ListaEmpleadosBoceto().setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivos;
    private javax.swing.JButton btnBuscarPorTexto;
    private javax.swing.JButton btnInactivos;
    private javax.swing.JButton btnTodos;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaUsuarios;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
