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
    private JTextField txtNum, txtTipo, txtPiso, txtPrecio, txtCap;
    private HabitacionDAO habitacionDAO;

    public CrearHabitacion(Frame padre) {
        super(padre, "Nueva Habitación", true);
        habitacionDAO = new HabitacionDAO();
        setSize(380, 420);
        setLocationRelativeTo(padre);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel p = new JPanel(new GridLayout(10, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        p.add(new JLabel("Número Habitación:")); txtNum = new JTextField(); p.add(txtNum);
        p.add(new JLabel("Tipo (Ej: Suite, Simple):")); txtTipo = new JTextField(); p.add(txtTipo);
        p.add(new JLabel("Piso:")); txtPiso = new JTextField(); p.add(txtPiso);
        p.add(new JLabel("Precio por Hora:")); txtPrecio = new JTextField(); p.add(txtPrecio);
        p.add(new JLabel("Capacidad Máxima:")); txtCap = new JTextField(); p.add(txtCap);
        
        add(p);
        JButton btn = new JButton("Guardar Habitación");
        btn.addActionListener(e -> {
            try {
                Habitacion h = new Habitacion(0, txtNum.getText(), txtTipo.getText(), Integer.parseInt(txtPiso.getText()), 
                                              Double.parseDouble(txtPrecio.getText()), Integer.parseInt(txtCap.getText()), "Disponible");
                if(habitacionDAO.insertar(h)) { dispose(); }
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error de formatos numéricos."); }
        });
        add(btn);
    }
}