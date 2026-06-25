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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Personal extends JPanel {

    private static final String TODOS_LOS_ROLES = "Todos los roles";

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private UsuarioDAO usuarioDAO;
    private List<Usuario> listaInterna;
    private JTextField txtBuscar;
    private JComboBox<String> cbFiltroRol;

    public Personal() {
        usuarioDAO = new UsuarioDAO();
        setBackground(new Color(0xF7, 0xF5, 0xF0));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setOpaque(false);

        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filtrosPanel.setOpaque(false);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBuscar.setForeground(new Color(0x1A, 0x27, 0x44));
        filtrosPanel.add(lblBuscar);

        txtBuscar = new JTextField(14);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { cargarUsuarios(); }
            @Override
            public void removeUpdate(DocumentEvent e) { cargarUsuarios(); }
            @Override
            public void changedUpdate(DocumentEvent e) { cargarUsuarios(); }
        });
        filtrosPanel.add(txtBuscar);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRol.setForeground(new Color(0x1A, 0x27, 0x44));
        filtrosPanel.add(lblRol);

        cbFiltroRol = new JComboBox<>(new String[]{
                TODOS_LOS_ROLES, "Administrador", "Recepcionista", "Analista"
        });
        cbFiltroRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFiltroRol.addActionListener(e -> cargarUsuarios());
        filtrosPanel.add(cbFiltroRol);

        topPanel.add(filtrosPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel mainCard = new JPanel(new BorderLayout());
        mainCard.setBackground(Color.WHITE);
        mainCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String[] columnas = {"N°", "Nombre de Usuario", "Rol de Acceso", "Estado Operativo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(30);
        tablaUsuarios.getTableHeader().setReorderingAllowed(false); // Bloqueado
        tablaUsuarios.getTableHeader().setBackground(new Color(0x1A, 0x27, 0x44));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        mainCard.add(scroll, BorderLayout.CENTER);
        add(mainCard, BorderLayout.CENTER);

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        listaInterna = usuarioDAO.obtenerTodos();

        String busqueda = txtBuscar.getText().trim().toLowerCase();
        String rolSeleccionado = cbFiltroRol.getSelectedItem() != null
                ? cbFiltroRol.getSelectedItem().toString()
                : TODOS_LOS_ROLES;

        int index = 1;
        for (Usuario u : listaInterna) {
            if (!busqueda.isEmpty() && !u.getNombre().toLowerCase().contains(busqueda)) {
                continue;
            }
            if (!rolSeleccionado.equals(TODOS_LOS_ROLES)
                    && !u.getRol().equalsIgnoreCase(rolSeleccionado)) {
                continue;
            }

            Object[] fila = {
                index,
                u.getNombre(),
                u.getRol(),
                u.getPrimerLogin() == 1 ? "Pendiente" : "Activo"
            };
            modeloTabla.addRow(fila);
            index++;
        }
    }
}
