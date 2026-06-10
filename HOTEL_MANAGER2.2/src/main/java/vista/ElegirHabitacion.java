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
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.util.List;
import javax.swing.*;
import modelo.Habitacion;

public class ElegirHabitacion extends JDialog {
    
    private JPanel panelGrilla;
    private HabitacionDAO habitacionDAO;
    private Habitacion habitacionSeleccionada = null;

    public ElegirHabitacion(Window parent) {
        super(parent, "Seleccionar Habitación Disponible", ModalityType.APPLICATION_MODAL);
        habitacionDAO = new HabitacionDAO();
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        panelGrilla = new JPanel(new GridLayout(0, 3, 10, 10));
        panelGrilla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JScrollPane(panelGrilla), BorderLayout.CENTER);
        add(new JLabel(" Azul = Individual | Rojo = Doble | Verde = Triple", JLabel.CENTER), BorderLayout.SOUTH);

        cargarDisponibles();
    }

    private void cargarDisponibles() {
        List<Habitacion> libres = habitacionDAO.obtenerPorEstado("Libre");
        for (Habitacion h : libres) {
            JButton btn = new JButton("Num: " + h.getNumHabitacion());
            
            if ("Individual".equals(h.getTipo())) {
                btn.setBackground(Color.decode("#AEC6CF"));
            } else if ("Doble".equals(h.getTipo())) {
                btn.setBackground(Color.decode("#FFB6C1"));
            } else {
                btn.setBackground(Color.decode("#98FB98"));
            }

            btn.addActionListener(e -> {
                this.habitacionSeleccionada = h;
                this.dispose();
            });
            panelGrilla.add(btn);
        }
    }

    public Habitacion getHabitacionSeleccionada() {
        return habitacionSeleccionada;
    }
}