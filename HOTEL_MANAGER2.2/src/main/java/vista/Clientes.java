/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.ClienteDAO;
import modelo.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Clientes extends JPanel {

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private ClienteDAO clienteDAO;
    private List<Cliente> listaInterna;

    public Clientes() {
        clienteDAO = new ClienteDAO();
        setBackground(new Color(0xF7, 0xF5, 0xF0));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JButton btnNuevo = new JButton("Nuevo Huésped") {
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
        btnNuevo.setPreferredSize(new Dimension(180, 38));
        btnNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevo.addActionListener(e -> {
            CrearCliente dialog = new CrearCliente((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            cargarClientes();
        });
        topPanel.add(btnNuevo, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel mainCard = new JPanel(new BorderLayout());
        mainCard.setBackground(Color.WHITE);
        mainCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Columnas limpias sin ID Cliente expuesto
        String[] columnas = {"N°", "Nombre Completo", "Teléfono"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(30);
        tablaClientes.getTableHeader().setReorderingAllowed(false); // Bloqueado
        tablaClientes.getTableHeader().setBackground(new Color(0x1A, 0x27, 0x44));
        tablaClientes.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaClientes);
        mainCard.add(scroll, BorderLayout.CENTER);
        add(mainCard, BorderLayout.CENTER);

        cargarClientes();
    }

    private void cargarClientes() {
        modeloTabla.setRowCount(0);
        
        listaInterna = clienteDAO.obtenerTodos(); 
        
        int index = 1;
        for (Cliente c : listaInterna) {
            Object[] fila = {
                index,
                c.getNomCliente(),  
                c.getNumCliente()
            };
            modeloTabla.addRow(fila);
            index++;
        }
    }
}