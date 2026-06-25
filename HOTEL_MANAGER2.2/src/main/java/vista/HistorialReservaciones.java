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
import modelo.Reservacion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistorialReservaciones extends JPanel {

    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private ReservacionDAO reservacionDAO;
    private JLabel lblTotalIngresos;

    public HistorialReservaciones() {
        reservacionDAO = new ReservacionDAO();

        setBackground(new Color(0xF7, 0xF5, 0xF0));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitulo = new JLabel("Historial General y Auditoría de Reservaciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0x1A, 0x27, 0x44));
        topPanel.add(lblTitulo, BorderLayout.WEST);

        lblTotalIngresos = new JLabel("Cargando ingresos...");
        lblTotalIngresos.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotalIngresos.setForeground(new Color(0x2E, 0x7D, 0x32));
        topPanel.add(lblTotalIngresos, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel mainCard = new JPanel(new BorderLayout());
        mainCard.setBackground(Color.WHITE);
        mainCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String[] columnas = {"ID", "ID Cliente", "ID Habitación", "Check-In", "Check-Out", "Horas", "Monto Total", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setRowHeight(30);
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaHistorial.getTableHeader().setBackground(new Color(0x1A, 0x27, 0x44));
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        mainCard.add(scroll, BorderLayout.CENTER);
        add(mainCard, BorderLayout.CENTER);

        cargarHistorialCompleto();
    }

    private void cargarHistorialCompleto() {
        modeloTabla.setRowCount(0);
        List<Reservacion> lista = reservacionDAO.obtenerTodos();
        double ingresosAcumulados = 0;

        for (Reservacion r : lista) {
            Object[] fila = {
                r.getIdReservacion(),
                r.getIdCliente(),
                r.getIdHabitacion(),
                r.getFechaInicio(),
                r.getFechaFin(),
                r.getPeriodo(),
                r.getPrecioTotal(),
                r.getEstado()
            };
            modeloTabla.addRow(fila);
            
            if (r.getEstado().equalsIgnoreCase("Activa") || r.getEstado().equalsIgnoreCase("Finalizada")) {
                ingresosAcumulados += r.getPrecioTotal();
            }
        }

        lblTotalIngresos.setText("Ingresos Brutos Acumulados: $" + ingresosAcumulados);
    }
}