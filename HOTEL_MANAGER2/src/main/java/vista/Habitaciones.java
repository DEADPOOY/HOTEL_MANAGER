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
import java.util.List;
import javax.swing.*;
import modelo.Habitacion;

public class Habitaciones extends JFrame {
    private JPanel panelGrilla;
    private JButton btnCrear, btnModificar, btnRegresar;
    private HabitacionDAO habitacionDAO;

    public Habitaciones() {
        habitacionDAO = new HabitacionDAO();
        setTitle("Monitoreo de Habitaciones");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        panelGrilla = new JPanel(new GridLayout(0, 4, 15, 15));
        panelGrilla.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCrear = new JButton("Crear Habitación");
        btnModificar = new JButton("Modificar Habitación");
        btnRegresar = new JButton("Regresar");

        btnCrear.setBackground(Color.decode("#28a745"));
        btnCrear.setForeground(Color.WHITE);
        btnModificar.setBackground(Color.decode("#fd7e14"));
        btnModificar.setForeground(Color.WHITE);
        btnRegresar.setBackground(Color.decode("#6c757d"));
        btnRegresar.setForeground(Color.WHITE);

        if (!Sesion.esAdmin()) {
            btnCrear.setVisible(false);
            btnModificar.setVisible(false);
        }

        panelAcciones.add(btnCrear); panelAcciones.add(btnModificar); panelAcciones.add(btnRegresar);
        add(new JScrollPane(panelGrilla), BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);

        cargarHabitaciones();

        btnRegresar.addActionListener(e -> this.dispose());
        btnCrear.addActionListener(e -> new CrearHabitacion(this).setVisible(true));
        btnModificar.addActionListener(e -> new ModificarHabitacion(this).setVisible(true));
    }

    public void cargarHabitaciones() {
        panelGrilla.removeAll();
        List<Habitacion> lista = habitacionDAO.obtenerTodos();
        for (Habitacion h : lista) {
            JButton btn = new JButton("<html><center><b>Hab: " + h.getNumHabitacion() + "</b><br>" + h.getTipo() + "<br>" + h.getEstado() + "</center></html>");
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            
            if ("Libre".equals(h.getEstado())) btn.setBackground(Color.decode("#90EE90"));
            else if ("Ocupada".equals(h.getEstado())) btn.setBackground(Color.decode("#FF6B6B"));
            else btn.setBackground(Color.decode("#FFD700"));

            panelGrilla.add(btn);
        }
        panelGrilla.revalidate();
        panelGrilla.repaint();
    }
}