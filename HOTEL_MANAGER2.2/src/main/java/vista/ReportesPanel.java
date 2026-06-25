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
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportesPanel extends JPanel {

    private JTable tablaAnalisis;
    private DefaultTableModel modeloTabla;
    private ReservacionDAO reservacionDAO;
    private JLabel lblIngresosBrutos; 

    public ReportesPanel() {
        // 1. Inicializar el DAO primero
        reservacionDAO = new ReservacionDAO();
        
        // 2. Configurar el contenedor principal
        setBackground(new Color(0xF7, 0xF5, 0xF0));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // 3. Crear el panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel("Historial General y Auditoría de Reservaciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0x1A, 0x27, 0x44));
        topPanel.add(lblTitulo, BorderLayout.WEST);

        // OBLIGATORIO: Crear el Label ANTES de llamar a generarMétricas
        lblIngresosBrutos = new JLabel("Ingresos Brutos Acumulados: $0.0");
        lblIngresosBrutos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblIngresosBrutos.setForeground(new Color(0x2E, 0x7D, 0x32)); 
        topPanel.add(lblIngresosBrutos, BorderLayout.EAST); 

        add(topPanel, BorderLayout.NORTH);

        // 4. Crear la tarjeta de la tabla
        JPanel mainCard = new JPanel(new BorderLayout());
        mainCard.setBackground(Color.WHITE);
        mainCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columnas = {"N°", "Huésped", "N° Habitación", "Tipo", "Check-In", "Check-Out", "Horas", "Monto Total", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaAnalisis = new JTable(modeloTabla);
        tablaAnalisis.setRowHeight(30);
        tablaAnalisis.getTableHeader().setReorderingAllowed(false);
        tablaAnalisis.getTableHeader().setBackground(new Color(0x1A, 0x27, 0x44));
        tablaAnalisis.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaAnalisis);
        mainCard.add(scroll, BorderLayout.CENTER);
        add(mainCard, BorderLayout.CENTER);

        // 5. LLAMADA FINAL: Ahora que todo está creado en memoria, cargamos los datos seguros
        generarMétricas();
    }

    public void generarMétricas() {
        modeloTabla.setRowCount(0);
        List<String[]> datos = reservacionDAO.obtenerHistorialGeneral(); 
        
        double ingresosTotales = 0;
        if (datos != null) {
            for (String[] row : datos) {
                modeloTabla.addRow(row);
                if (row[8] != null && !row[8].equalsIgnoreCase("Cancelada")) {
                    try {
                        if (row[7] != null && !row[7].trim().isEmpty() && !row[7].equalsIgnoreCase("null")) {
                            ingresosTotales += Double.parseDouble(row[7]);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[WARNING] Error al parsear precio en fila.");
                    }
                }
            }
        }
        
        // Aquí ya no dará NullPointerException porque el label ya existe
        if (lblIngresosBrutos != null) {
            lblIngresosBrutos.setText("Ingresos Brutos Acumulados: $" + ingresosTotales); 
        }
        
        revalidate();
        repaint();
    }
}