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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialReservaciones extends JPanel {

    private static final String[] COLUMNAS = {
        "ID", "Huésped", "N° Habitación", "Tipo", "Check-In", "Check-Out", "Horas", "Monto Total", "Estado"
    };

    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private ReservacionDAO reservacionDAO;
    private JLabel lblTotalIngresos;
    private List<String[]> datosHistorial;
    private double ingresosAcumulados;

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

        JPanel accionesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        accionesPanel.setOpaque(false);
        accionesPanel.add(lblTotalIngresos);

        JButton btnDescargar = crearBotonDescarga();
        btnDescargar.addActionListener(e -> descargarHistorial());
        accionesPanel.add(btnDescargar);
        topPanel.add(accionesPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel mainCard = new JPanel(new BorderLayout());
        mainCard.setBackground(Color.WHITE);
        mainCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
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
        datosHistorial = reservacionDAO.obtenerReservacionesDetalladas();
        ingresosAcumulados = 0;

        if (datosHistorial != null) {
            for (String[] fila : datosHistorial) {
                modeloTabla.addRow(fila);
                try {
                    double monto = Double.parseDouble(fila[7]);
                    ingresosAcumulados += monto;
                } catch (Exception e) {
                    // Ignorar filas con monto no numérico
                }
            }
        }

        lblTotalIngresos.setText("Ingresos Brutos Acumulados: $" + ingresosAcumulados);
    }

    private JButton crearBotonDescarga() {
        JButton btn = new JButton("Descargar Historial") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0x1A, 0x27, 0x44));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(190, 36));
        return btn;
    }

    private void descargarHistorial() {
        if (datosHistorial == null || datosHistorial.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay registros en el historial para exportar.",
                    "Sin datos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar historial de reservaciones");
        selector.setSelectedFile(new File("historial_reservaciones_" + fecha + ".csv"));

        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo = selector.getSelectedFile();
        if (!archivo.getName().toLowerCase().endsWith(".csv")) {
            archivo = new File(archivo.getAbsolutePath() + ".csv");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write("Reporte generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            writer.newLine();
            writer.write("Ingresos brutos acumulados: $" + ingresosAcumulados);
            writer.newLine();
            writer.newLine();
            writer.write(String.join(",", COLUMNAS));
            writer.newLine();

            for (String[] fila : datosHistorial) {
                StringBuilder linea = new StringBuilder();
                for (int i = 0; i < fila.length; i++) {
                    if (i > 0) {
                        linea.append(",");
                    }
                    linea.append(escaparCsv(fila[i]));
                }
                writer.write(linea.toString());
                writer.newLine();
            }

            JOptionPane.showMessageDialog(this,
                    "Historial exportado correctamente en:\n" + archivo.getAbsolutePath(),
                    "Descarga completada",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el archivo.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String escaparCsv(String valor) {
        if (valor == null) {
            return "\"\"";
        }
        String texto = valor.replace("\"", "\"\"");
        return "\"" + texto + "\"";
    }
}