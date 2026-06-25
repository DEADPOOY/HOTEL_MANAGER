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

public class CrearHabitacion extends JDialog {
    private JTextField txtNum, txtPiso, txtPrecio, txtCap;
    private JComboBox<String> cbTipo;
    private HabitacionDAO habitacionDAO;

    public CrearHabitacion(Frame padre) {
        super(padre, "Nueva Habitación", true);
        habitacionDAO = new HabitacionDAO();
        setSize(380, 420);
        setLocationRelativeTo(padre);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel p = new JPanel(new GridLayout(10, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        p.add(new JLabel("Número Habitación:"));
        txtNum = new JTextField();
        p.add(txtNum);
        p.add(new JLabel("Tipo:"));
        cbTipo = new JComboBox<>(new String[]{"Individual", "Doble", "Triple"});
        p.add(cbTipo);
        p.add(new JLabel("Piso:"));
        txtPiso = new JTextField();
        p.add(txtPiso);
        p.add(new JLabel("Precio por Hora:"));
        txtPrecio = new JTextField();
        p.add(txtPrecio);
        p.add(new JLabel("Capacidad Máxima:"));
        txtCap = new JTextField();
        p.add(txtCap);

        add(p);
        JButton btn = new JButton("Guardar Habitación");
        btn.addActionListener(e -> guardarHabitacion());
        add(btn);
    }

    private void guardarHabitacion() {
        String num = txtNum.getText().trim();
        String tipo = cbTipo.getSelectedItem().toString();
        String pisoTexto = txtPiso.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String capTexto = txtCap.getText().trim();

        if (num.isEmpty() || pisoTexto.isEmpty() || precioTexto.isEmpty() || capTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int piso = Integer.parseInt(pisoTexto);
            double precio = Double.parseDouble(precioTexto);
            int capacidad = Integer.parseInt(capTexto);

            if (piso < 0 || precio <= 0 || capacidad <= 0) {
                JOptionPane.showMessageDialog(this, "Piso, precio y capacidad deben ser valores positivos.", "Datos inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (habitacionDAO.existePorNumeroYPiso(num, piso)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe la habitación N° " + num + " en el piso " + piso + ".",
                        "Habitación duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Habitacion h = new Habitacion(0, num, tipo, piso, precio, capacidad, "Disponible");
            if (habitacionDAO.insertar(h)) {
                JOptionPane.showMessageDialog(this, "Habitación registrada correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la habitación. Verifique que el número no esté duplicado y que la base de datos esté activa.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Piso, precio y capacidad deben ser valores numéricos válidos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}