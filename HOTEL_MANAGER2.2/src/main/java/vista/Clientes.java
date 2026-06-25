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
        btnNuevo.setPreferredSize(new Dimension(160, 38));
        btnNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevo.addActionListener(e -> {
            CrearCliente dial = new CrearCliente((Frame) SwingUtilities.getWindowAncestor(this));
            dial.setVisible(true);
            cargarDatos();
        });

        topPanel.add(btnNuevo, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String[] columnas = {"ID Huésped", "Nombre Completo", "Número de Contacto / Documento"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(30);
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaClientes.getTableHeader().setBackground(new Color(0x1A, 0x27, 0x44));
        tablaClientes.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaClientes);
        card.add(scroll, BorderLayout.CENTER);

        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botPanel.setOpaque(false);
        JButton btnMod = new JButton("Modificar Seleccionado");
        btnMod.addActionListener(e -> {
            int fila = tablaClientes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            Cliente c = clienteDAO.obtenerPorId(id);
            ModificarCliente md = new ModificarCliente((Frame) SwingUtilities.getWindowAncestor(this), c);
            md.setVisible(true);
            cargarDatos();
        });
        botPanel.add(btnMod);
        card.add(botPanel, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Cliente> list = clienteDAO.obtenerTodos();
        for (Cliente c : list) {
            modeloTabla.addRow(new Object[]{c.getIdCliente(), c.getNomCliente(), c.getNumCliente()});
        }
    }
}