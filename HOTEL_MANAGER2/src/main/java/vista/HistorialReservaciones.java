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

public class HistorialReservaciones extends JFrame {
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private JButton btnExportar, btnCerrar;
    private ReservacionDAO reservacionDAO;

    public HistorialReservaciones() {
        reservacionDAO = new ReservacionDAO();
        setTitle("Historial Cronológico de Reservaciones (Auditoría)");
        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Cliente ID", "Habitación ID", "Fecha Registro", "Inicio", "Fin", "Horas", "Costo", "Estado"}, 0);
        tablaHistorial = new JTable(modeloTabla);
        add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnExportar = new JButton("Exportar Historial");
        btnCerrar = new JButton("Cerrar");
        
        btnExportar.setBackground(Color.decode("#1a3a5c"));
        btnExportar.setForeground(Color.WHITE);

        if (Sesion.esRecepcionista()) {
            btnExportar.setVisible(false);
        }

        panelAcciones.add(btnExportar); panelAcciones.add(btnCerrar);
        add(panelAcciones, BorderLayout.SOUTH);

        llenarTabla();

        btnCerrar.addActionListener(e -> this.dispose());
        btnExportar.addActionListener(e -> new GuardarHistorial(this).setVisible(true));
    }

    private void llenarTabla() {
        modeloTabla.setRowCount(0);
        List<Reservacion> lista = reservacionDAO.obtenerHistorial();
        for (Reservacion r : lista) {
            modeloTabla.addRow(new Object[]{r.getIdReservacion(), r.getIdCliente(), r.getIdHabitacion(),
                    r.getFechaRes(), r.getFechaInicio(), r.getFechaFin(), r.getPeriodo(), r.getPrecioTotal(), r.getEstado()});
        }
    }
}
