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

        llenarTabla(); // Descarga la lista completa de huéspedes al iniciar la pantalla

        btnCerrar.addActionListener(e -> this.dispose());
        btnNuevo.addActionListener(e -> new CrearCliente(this).setVisible(true));
        
        btnEditar.addActionListener(e -> { // Abre el diálogo de edición para el cliente seleccionado en la tabla
            int fila = tablaClientes.getSelectedRow();
            if (fila == -1) return;
            int id = (int) modeloTabla.getValueAt(fila, 0); // Extrae el ID del cliente seleccionado de la columna cero
            Cliente c = clienteDAO.obtenerPorId(id); // Busca los datos del cliente en la base de datos
            if (c != null) new ModificarCliente(this, c).setVisible(true); // Abre la ventana de edición enviándole la instancia del cliente cargado
        });

        btnDetalle.addActionListener(e -> { // Abre el historial de rentas del cliente seleccionado en la tabla
            int fila = tablaClientes.getSelectedRow();
            if (fila == -1) return;
            int id = (int) modeloTabla.getValueAt(fila, 0);
            new DetalleCliente(id).setVisible(true); // Abre la ventana de historial enviándole el ID del cliente seleccionado
        });
    }

    public void llenarTabla() { // Limpia y actualiza la lista de clientes mostrada en la tabla desde MySQL
        modeloTabla.setRowCount(0);
        List<Cliente> lista = clienteDAO.obtenerTodos();
        for (Cliente c : lista) {
            modeloTabla.addRow(new Object[]{c.getIdCliente(), c.getNomCliente(), c.getNumCliente()});
        }
    }
}