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
import dao.ReservacionDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Reservacion;

public class Reservaciones extends JFrame {
    
    private JTable tablaRes;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear, btnModificar, btnCancelar, btnHabitaciones, btnHistorial;
    private ReservacionDAO reservacionDAO;
    private HabitacionDAO habitacionDAO;

    public Reservaciones() {
        reservacionDAO = new ReservacionDAO();
        habitacionDAO = new HabitacionDAO();
        setTitle("Módulo de Reservaciones Activas");
        setSize(850, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "ID Cliente", "ID Habitación", "Inicio", "Fin", "Precio Total"}, 0);
        tablaRes = new JTable(modeloTabla);
        add(new JScrollPane(tablaRes), BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnCrear = new JButton("Crear Reservación");
        btnModificar = new JButton("Modificar");
        btnCancelar = new JButton("Cancelar");
        btnHabitaciones = new JButton("Ver Habitaciones");
        btnHistorial = new JButton("Historial General");

        btnCrear.setBackground(Color.decode("#28a745"));
        btnCrear.setForeground(Color.WHITE);
        btnModificar.setBackground(Color.decode("#fd7e14"));
        btnModificar.setForeground(Color.WHITE);
        btnCancelar.setBackground(Color.decode("#dc3545"));
        btnCancelar.setForeground(Color.WHITE);
        btnHabitaciones.setBackground(Color.decode("#1a3a5c"));
        btnHabitaciones.setForeground(Color.WHITE);

        btnHistorial.setVisible(Sesion.esAdmin() || Sesion.esAnalista());

        panelAcciones.add(btnCrear); 
        panelAcciones.add(btnModificar); 
        panelAcciones.add(btnCancelar);
        panelAcciones.add(btnHabitaciones); 
        panelAcciones.add(btnHistorial);
        add(panelAcciones, BorderLayout.SOUTH);

        llenarTabla();

        btnHabitaciones.addActionListener(e -> new Habitaciones().setVisible(true));
        
        btnCrear.addActionListener(e -> new CrearReservacion(this).setVisible(true));
        
        btnModificar.addActionListener(e -> {
            int fila = tablaRes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una reservación para modificar.");
                return;
            }
            int idRes = (int) modeloTabla.getValueAt(fila, 0);
            Reservacion r = reservacionDAO.obtenerPorId(idRes);
            if (r != null) {
                ModificarReservacion dialogo = new ModificarReservacion(this);
                dialogo.cargarDatos(r);
                dialogo.setVisible(true);
            }
        });
        
        btnHistorial.addActionListener(e -> {
            new HistorialReservaciones().setVisible(true);
        });

        btnCancelar.addActionListener(e -> {
            int fila = tablaRes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una reservación.");
                return;
            }
            int idRes = (int) modeloTabla.getValueAt(fila, 0);
            int idHab = (int) modeloTabla.getValueAt(fila, 2);

            ConfirmarCancelar dialog = new ConfirmarCancelar(this);
            dialog.setVisible(true);
            if (dialog.isConfirmado()) {
                if (reservacionDAO.cancelar(idRes)) {
                    habitacionDAO.cambiarEstado(idHab, "Libre");
                    llenarTabla();
                    JOptionPane.showMessageDialog(this, "Reservación cancelada.");
                }
            }
        });
    }

    public void llenarTabla() {
        modeloTabla.setRowCount(0);
        List<Reservacion> lista = reservacionDAO.obtenerActivas();
        for (Reservacion r : lista) {
            modeloTabla.addRow(new Object[]{r.getIdReservacion(), r.getIdCliente(), r.getIdHabitacion(), r.getFechaInicio(), r.getFechaFin(), r.getPrecioTotal()});
        }
    }
}
