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
    private JTextField txtTipo, txtPiso, txtPrecio, txtCap;
    private JComboBox<String> cbEstado;
    private HabitacionDAO habitacionDAO;
    private Habitacion habActual;

    public ModificarHabitacion(Frame padre, Habitacion h) {
        super(padre, "Modificar Habitación", true);
        this.habActual = h;
        this.habitacionDAO = new HabitacionDAO();
        setSize(380, 420);
        setLocationRelativeTo(padre);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel p = new JPanel(new GridLayout(10, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        p.add(new JLabel("Tipo:")); txtTipo = new JTextField(habActual.getTipo()); p.add(txtTipo);
        p.add(new JLabel("Piso:")); txtPiso = new JTextField(String.valueOf(habActual.getPiso())); p.add(txtPiso);
        p.add(new JLabel("Precio por Hora:")); txtPrecio = new JTextField(String.valueOf(habActual.getPrecioHora())); p.add(txtPrecio);
        p.add(new JLabel("Capacidad:")); txtCap = new JTextField(String.valueOf(habActual.getNumCapacidad())); p.add(txtCap);
        p.add(new JLabel("Estado actual:"));
        cbEstado = new JComboBox<>(new String[]{"Disponible", "Ocupada", "Mantenimiento"});
        cbEstado.setSelectedItem(habActual.getEstado());
        p.add(cbEstado);

        add(p);
        JButton btn = new JButton("Actualizar Datos");
        btn.addActionListener(e -> {
            habActual.setTipo(txtTipo.getText());
            habActual.setPiso(Integer.parseInt(txtPiso.getText()));
            habActual.setPrecioHora(Double.parseDouble(txtPrecio.getText().trim()));
            habActual.setNumCapacidad(Integer.parseInt(txtCap.getText()));
            habActual.setEstado((String)cbEstado.getSelectedItem());
            if(habitacionDAO.actualizar(habActual)) { dispose(); }
        });
        add(btn);
    }
}
