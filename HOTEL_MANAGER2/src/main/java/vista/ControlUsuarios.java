/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.UsuarioDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Usuario;

public class ControlUsuarios extends JFrame {
    
    private JTable tablaRecepcionistas, tablaAnalistas;
    private DefaultTableModel modeloRec, modeloAna;
    private JButton btnNuevo, btnEliminar, btnRegresar;
    private UsuarioDAO usuarioDAO;

    public ControlUsuarios() {
        usuarioDAO = new UsuarioDAO();
        setTitle("Control de Usuarios - Administrador");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloRec = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0);
        tablaRecepcionistas = new JTable(modeloRec);
        
        modeloAna = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0);
        tablaAnalistas = new JTable(modeloAna);

        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 15, 10));
        panelTablas.add(new JScrollPane(tablaRecepcionistas));
        panelTablas.add(new JScrollPane(tablaAnalistas));

        JPanel panelBotones = new JPanel();
        btnNuevo = new JButton("Nuevo Usuario");
        btnEliminar = new JButton("Eliminar Usuario");
        btnRegresar = new JButton("Regresar");

        btnNuevo.setBackground(Color.decode("#28a745"));
        btnNuevo.setForeground(Color.WHITE);
        btnEliminar.setBackground(Color.decode("#dc3545"));
        btnEliminar.setForeground(Color.WHITE);
        btnRegresar.setBackground(Color.decode("#6c757d"));
        btnRegresar.setForeground(Color.WHITE);

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRegresar);

        add(panelTablas, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        llenarTablas();

        btnNuevo.addActionListener(e -> new CrearUsuario(this).setVisible(true));
        btnRegresar.addActionListener(e -> {
            new PantallaInicio().setVisible(true);
            dispose();
        });
        btnEliminar.addActionListener(e -> eliminarUsuario());
    }

    public void llenarTablas() {
        modeloRec.setRowCount(0);
        modeloAna.setRowCount(0);
        
        List<Usuario> lista = usuarioDAO.obtenerTodos();
        if (lista == null || lista.isEmpty()) {
            lista = usuarioDAO.obtenerTodos();
        }
        
        for (Usuario u : lista) {
            if ("Recepcionista".equals(u.getRol())) {
                modeloRec.addRow(new Object[]{u.getIdUsuario(), u.getNombre()});
            } else if ("Analista".equals(u.getRol())) {
                modeloAna.addRow(new Object[]{u.getIdUsuario(), u.getNombre()});
            }
        }
    }

    private void eliminarUsuario() {
        int filaRec = tablaRecepcionistas.getSelectedRow();
        int filaAna = tablaAnalistas.getSelectedRow();
        int idEliminar = -1;

        if (filaRec != -1) {
            idEliminar = (int) modeloRec.getValueAt(filaRec, 0);
        } else if (filaAna != -1) {
            idEliminar = (int) modeloAna.getValueAt(filaAna, 0);
        }

        if (idEliminar == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
            return;
        }

        ConfirmarEliminar dialogo = new ConfirmarEliminar(this);
        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            if (usuarioDAO.eliminar(idEliminar)) {
                llenarTablas();
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
            }
        }
    }
}