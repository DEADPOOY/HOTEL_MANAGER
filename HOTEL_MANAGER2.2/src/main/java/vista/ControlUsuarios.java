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
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ControlUsuarios extends JPanel {

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private UsuarioDAO usuarioDAO;

    public ControlUsuarios() {
        usuarioDAO = new UsuarioDAO();

        setBackground(new Color(0xF7, 0xF5, 0xF0));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Barra superior de acciones
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JButton btnNuevo = new JButton("Registrar Nuevo Usuario") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0xC9, 0xA8, 0x4C));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        btnNuevo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setContentAreaFilled(false);
        btnNuevo.setBorderPainted(false);
        btnNuevo.setPreferredSize(new Dimension(200, 38));
        btnNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevo.addActionListener(e -> {
            CrearUsuario dial = new CrearUsuario((Frame) SwingUtilities.getWindowAncestor(this));
            dial.setVisible(true);
            cargarDatos();
        });

        topPanel.add(btnNuevo, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Card contenedora de la Tabla
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String[] columnas = {"ID Usuario", "Nombre de Usuario", "Rol Asignado", "Primer Login"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(30);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaUsuarios.getTableHeader().setBackground(new Color(0x1A, 0x27, 0x44));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        tableCard.add(scroll, BorderLayout.CENTER);

        // Barra inferior para la eliminación de usuarios
        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botPanel.setOpaque(false);
        
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.setForeground(new Color(0xE7, 0x4C, 0x3C));
        btnEliminar.addActionListener(e -> eliminarUsuario());
        botPanel.add(btnEliminar);
        
        tableCard.add(botPanel, BorderLayout.SOUTH);
        add(tableCard, BorderLayout.CENTER);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Usuario> lista = usuarioDAO.obtenerTodos();
        for (Usuario u : lista) {
            modeloTabla.addRow(new Object[]{
                u.getIdUsuario(), u.getNombre(), u.getRol(), (u.getPrimerLogin() == 1 ? "Sí" : "No")
            });
        }
    }

    private void eliminarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);

        int conf = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al usuario '" + nombre + "'?", "Confirmar acción", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Usuario revocado correctamente.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
