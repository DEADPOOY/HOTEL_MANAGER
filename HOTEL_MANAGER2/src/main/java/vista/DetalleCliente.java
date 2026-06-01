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
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Reservacion;

public class DetalleCliente extends JFrame {
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private ReservacionDAO reservacionDAO;

    public DetalleCliente(int idCliente) {
        reservacionDAO = new ReservacionDAO();
        setTitle("Historial de Actividad del Huésped");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Habitación ID", "Inicio", "Fin", "Estado", "Total"}, 0);
        tablaHistorial = new JTable(modeloTabla);
        add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        List<Reservacion> lista = reservacionDAO.obtenerPorCliente(idCliente);
        for (Reservacion r : lista) {
            modeloTabla.addRow(new Object[]{r.getIdReservacion(), r.getIdHabitacion(), r.getFechaInicio(), r.getFechaFin(), r.getEstado(), r.getPrecioTotal()});
        }
    }
}