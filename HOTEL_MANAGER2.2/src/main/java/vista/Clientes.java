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
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;

public class Clientes extends JFrame {
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo, btnEditar, btnDetalle, btnCerrar;
    private ClienteDAO clienteDAO;

    public Clientes() {
        clienteDAO = new ClienteDAO();
        setTitle("Catálogo General de Clientes");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Teléfono"}, 0);
        tablaClientes = new JTable(modeloTabla);
        add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnDetalle = new JButton("Ver Historial");
        btnCerrar = new JButton("Cerrar");

        btnNuevo.setBackground(Color.decode("#28a745"));
        btnNuevo.setForeground(Color.WHITE);
        btnEditar.setBackground(Color.decode("#fd7e14"));
        btnEditar.setForeground(Color.WHITE);

        panelAcciones.add(btnNuevo); panelAcciones.add(btnEditar); panelAcciones.add(btnDetalle); panelAcciones.add(btnCerrar);
        add(panelAcciones, BorderLayout.SOUTH);

        llenarTabla();

        btnCerrar.addActionListener(e -> this.dispose());
        btnNuevo.addActionListener(e -> new CrearCliente(this).setVisible(true));
        
        btnEditar.addActionListener(e -> {
            int fila = tablaClientes.getSelectedRow();
            if (fila == -1) return;
            int id = (int) modeloTabla.getValueAt(fila, 0);
            Cliente c = clienteDAO.obtenerPorId(id);
            if (c != null) new ModificarCliente(this, c).setVisible(true);
        });

        btnDetalle.addActionListener(e -> {
            int fila = tablaClientes.getSelectedRow();
            if (fila == -1) return;
            int id = (int) modeloTabla.getValueAt(fila, 0);
            new DetalleCliente(id).setVisible(true);
        });
    }

    public void llenarTabla() {
        modeloTabla.setRowCount(0);
        List<Cliente> lista = clienteDAO.obtenerTodos();
        for (Cliente c : lista) {
            modeloTabla.addRow(new Object[]{c.getIdCliente(), c.getNomCliente(), c.getNumCliente()});
        }
    }
}