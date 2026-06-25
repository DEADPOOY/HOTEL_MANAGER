/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.ReservacionDAO;
import dao.ClienteDAO;
import dao.HabitacionDAO;
import modelo.Reservacion;
import modelo.Cliente;
import modelo.Habitacion;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class CrearReservacion extends JDialog {

    private JComboBox<Cliente> comboClientes;
    private JComboBox<Habitacion> comboHabitaciones;
    private JTextField txtHoras;
    
    private ReservacionDAO reservacionDAO;
    private ClienteDAO clienteDAO;
    private HabitacionDAO habitacionDAO;

    public CrearReservacion(Frame padre) {
        super(padre, "Nueva Reservación - LUXE", true);
        reservacionDAO = new ReservacionDAO();
        clienteDAO = new ClienteDAO();
        habitacionDAO = new HabitacionDAO();

        setSize(480, 420);
        setLocationRelativeTo(padre);
        setResizable(false);
        getContentPane().setBackground(new Color(0xF7, 0xF5, 0xF0));
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        header.setBackground(new Color(0x1A, 0x27, 0x44));
        JLabel lblTitulo = new JLabel("Registrar Reservación");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0xC9, 0xA8, 0x4C));
        header.add(lblTitulo);
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;

        // 1. Desplegable de Clientes
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Seleccione el Huésped:"), gbc);
        gbc.gridy = 1;
        comboClientes = new JComboBox<>();
        llenarComboClientes();
        form.add(comboClientes, gbc);

        // 2. Desplegable de Habitaciones
        gbc.gridy = 2;
        form.add(new JLabel("Seleccione la Habitación Disponible:"), gbc);
        gbc.gridy = 3;
        comboHabitaciones = new JComboBox<>();
        llenarComboHabitaciones();
        form.add(comboHabitaciones, gbc);

        // 3. Campo de Horas
        gbc.gridy = 4;
        form.add(new JLabel("Periodo de Estadía (Horas):"), gbc);
        gbc.gridy = 5;
        txtHoras = new JTextField();
        txtHoras.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        form.add(txtHoras, gbc);

        add(form, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        acciones.setOpaque(false);
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar = new JButton("Confirmar Reserva");
        btnGuardar.setBackground(new Color(0xC9, 0xA8, 0x4C));
        btnGuardar.setForeground(Color.WHITE);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardarReservacion());
        
        acciones.add(btnCancelar);
        acciones.add(btnGuardar);
        add(acciones, BorderLayout.SOUTH);
    }

    private void llenarComboClientes() {
        try {
            List<Cliente> clientes = clienteDAO.obtenerTodos();
            for (Cliente c : clientes) {
                comboClientes.addItem(c);
            }
        } catch(Exception e) {
            System.out.println("Error al cargar clientes en combo");
        }
    }

    private void llenarComboHabitaciones() {
        try {
            List<Habitacion> habitaciones = habitacionDAO.obtenerPorEstado("Disponible");
            for (Habitacion h : habitaciones) {
                comboHabitaciones.addItem(h);
            }
        } catch(Exception e) {
            System.out.println("Error al cargar habitaciones en combo");
        }
    }

    private void guardarReservacion() {
        try {
            Cliente clienteSeleccionado = (Cliente) comboClientes.getSelectedItem();
            Habitacion habSeleccionada = (Habitacion) comboHabitaciones.getSelectedItem();
            
            if (clienteSeleccionado == null || habSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un huésped y una habitación válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String textoHoras = txtHoras.getText().trim();
            if (textoHoras.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo de horas no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double horas = Double.parseDouble(textoHoras);
            Date ahora = new Date();
            long finMs = ahora.getTime() + (long)(horas * 3600000);
            Date fechaFin = new Date(finMs);
            double total = horas * habSeleccionada.getPrecioHora();

            Reservacion r = new Reservacion(0, clienteSeleccionado.getIdCliente(), habSeleccionada.getIdHabitacion(), ahora, ahora, fechaFin, horas, total, "Activa");
            
            if (reservacionDAO.insertar(r)) {
                // Ajusta este método de tu HabitacionDAO si se llama diferente en tu proyecto (ej: actualizarEstado)
                habitacionDAO.cambiarEstado(habSeleccionada.getIdHabitacion(), "Ocupada"); 
                JOptionPane.showMessageDialog(this, "Reservación creada con éxito.\nMonto total a liquidar: $" + total);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "La base de datos rechazó la inserción del registro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido (ej: 12 o 4.5) en las horas.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.out.println("[CRITICAL] Error al guardar reservación: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}