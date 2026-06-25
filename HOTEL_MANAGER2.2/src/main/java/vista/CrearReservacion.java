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

    private JTextField txtNumCliente, txtNumHabitacion, txtHoras;
    private ReservacionDAO reservacionDAO;
    private ClienteDAO clienteDAO;
    private HabitacionDAO habitacionDAO;

    public CrearReservacion(Frame padre) {
        super(padre, "Nueva Reservación - LUXE", true);
        reservacionDAO = new ReservacionDAO();
        clienteDAO = new ClienteDAO();
        habitacionDAO = new HabitacionDAO();

        setSize(450, 400);
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

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Número / Doc. del Huésped:"), gbc);
        gbc.gridy = 1;
        txtNumCliente = crearTextField();
        form.add(txtNumCliente, gbc);

        gbc.gridy = 2;
        form.add(new JLabel("Número de Habitación:"), gbc);
        gbc.gridy = 3;
        txtNumHabitacion = crearTextField();
        form.add(txtNumHabitacion, gbc);

        gbc.gridy = 4;
        form.add(new JLabel("Periodo de Estadía (Horas):"), gbc);
        gbc.gridy = 5;
        txtHoras = crearTextField();
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

    private void guardarReservacion() {
        try {
            Cliente c = clienteDAO.obtenerPorNumero(txtNumCliente.getText().trim());
            int numHab = Integer.parseInt(txtNumHabitacion.getText().trim());
            double horas = Double.parseDouble(txtHoras.getText().trim());

            List<Habitacion> habs = habitacionDAO.obtenerTodos();
            Habitacion targetHab = null;
            for(Habitacion h : habs) {
                if(Integer.parseInt(h.getNumHabitacion()) == numHab) {
                    targetHab = h;
                    break;
                }
            }

            if (c == null || targetHab == null) {
                JOptionPane.showMessageDialog(this, "Huésped o Habitación no encontrados.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!targetHab.getEstado().equalsIgnoreCase("Disponible")) {
                JOptionPane.showMessageDialog(this, "La habitación seleccionada no se encuentra disponible.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date ahora = new Date();
            long finMs = ahora.getTime() + (long)(horas * 3600000);
            Date fechaFin = new Date(finMs);
            double total = horas * targetHab.getPrecioHora();

            // LÍNEA 128 CORREGIDA: Creación e inserción correcta de objeto Reservacion
            Reservacion r = new Reservacion(0, c.getIdCliente(), targetHab.getIdHabitacion(), ahora, ahora, fechaFin, horas, total, "Activa");
            
            if (reservacionDAO.insertar(r)) {
                habitacionDAO.cambiarEstado(targetHab.getIdHabitacion(), "Ocupada");
                JOptionPane.showMessageDialog(this, "Reservación creada con éxito. Total: $" + total);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Verifique que los datos numéricos sean correctos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField crearTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xBD, 0xC3, 0xC7), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return tf;
    }
}