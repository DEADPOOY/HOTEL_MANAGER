/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.HabitacionDAO;
import modelo.Habitacion;
import javax.swing.*;
import java.awt.*;

public class ModificarHabitacion extends JDialog {

    private JComboBox<String> cbEstado;
    private JButton btnGuardar, btnCancelar;
    private HabitacionDAO habitacionDAO;
    private Habitacion habitacionActual;

    public ModificarHabitacion(Frame parent, Habitacion h) {
        super(parent, "Gestionar Habitación " + h.getNumHabitacion(), true);
        this.habitacionActual = h;
        this.habitacionDAO = new HabitacionDAO();

        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel central = new JPanel(new GridLayout(2, 2, 10, 10));
        central.add(new JLabel("Habitación:"));
        central.add(new JLabel(h.getNumHabitacion() + " (" + h.getTipo() + ")"));
        
        central.add(new JLabel("Estado Actual:"));
        
        // Estados que usa tu BD actual: Disponible, Ocupada, Limpieza
        cbEstado = new JComboBox<>(new String[]{"Disponible", "Ocupada", "Limpieza"});
        cbEstado.setSelectedItem(h.getEstado() != null && h.getEstado().equalsIgnoreCase("Libre") ? "Disponible" : h.getEstado());
        central.add(cbEstado);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Actualizar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            String nuevoEstado = cbEstado.getSelectedItem().toString();
            // Llama correctamente a HabitacionDAO
            boolean exito = habitacionDAO.actualizarEstadoManual(habitacionActual.getIdHabitacion(), nuevoEstado);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Estado actualizado con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
        botones.add(btnCancelar);
        botones.add(btnGuardar);

        add(central, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }
}