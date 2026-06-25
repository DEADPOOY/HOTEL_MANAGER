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

public class Personal extends JPanel {

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private UsuarioDAO usuarioDAO;
    private List<Usuario> listaInterna;

    public Personal() {
        usuarioDAO = new UsuarioDAO();
        setBackground(new Color(0xF7, 0xF5, 0xF0));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

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
        int index = 1;
        for (Usuario u : listaInterna) {
            Object[] fila = {
                index,
                u.getIdUsuario(), 
                u.getRol(),
                u.getPrimerLogin()
            };
            modeloTabla.addRow(fila);
            index++;
        }
    }
}
