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
import java.util.Calendar; // Clase para manipulación avanzada y cálculo de periodos de fechas y tiempos
import java.util.Date;
import java.util.List;
import javax.swing.*;
import modelo.Reservacion;

public class GuardarHistorial extends JDialog {
    private JRadioButton rbDia, rbSemana, rbMes; // Botones selectores radiales para delimitar el periodo del reporte comercial
    private ButtonGroup grupoPeriodo;
    private JButton btnExportar, btnCancelar;
    private ReservacionDAO reservacionDAO;

    public GuardarHistorial(JFrame parent) {
        super(parent, "Exportar Reporte Comercial", true);
        reservacionDAO = new ReservacionDAO();
        setSize(320, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 1, 10, 10));

        rbDia = new JRadioButton("Último Día", true); // Opción seleccionada por defecto al arrancar
        rbSemana = new JRadioButton("Última Semana");
        rbMes = new JRadioButton("Último Mes");
        
        grupoPeriodo = new ButtonGroup();
        grupoPeriodo.add(rbDia); grupoPeriodo.add(rbSemana); grupoPeriodo.add(rbMes);

        JPanel panelRadio = new JPanel(new FlowLayout());
        panelRadio.add(rbDia); panelRadio.add(rbSemana); panelRadio.add(rbMes);

        btnExportar = new JButton("Exportar CSV");
        btnCancelar = new JButton("Cancelar");
        btnExportar.setBackground(Color.decode("#28a745"));
        btnExportar.setForeground(Color.WHITE);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnExportar); panelBotones.add(btnCancelar);

        add(new JLabel("Seleccione el periodo de exportación:", JLabel.CENTER));
        add(panelRadio);
        add(panelBotones);

        btnCancelar.addActionListener(e -> this.dispose());
        btnExportar.addActionListener(e -> { // Procesa el filtrado por fecha y exportación de archivos
            Calendar cal = Calendar.getInstance(); // Inicializa el gestor de calendario con la fecha del día actual
            Date fin = cal.getTime(); // Define la fecha de fin del reporte como el día actual exacto
            
            // Resta periodos de tiempo en el calendario dinámico según la opción seleccionada por el usuario
            if (rbDia.isSelected()) cal.add(Calendar.DAY_OF_YEAR, -1); // Resta un día completo al calendario para el reporte diario
            else if (rbSemana.isSelected()) cal.add(Calendar.WEEK_OF_YEAR, -1); // Resta una semana para el reporte semanal
            else cal.add(Calendar.MONTH, -1); // Resta un mes completo para el reporte mensual
            
            Date inicio = cal.getTime(); // Guarda la fecha resultante calculada como el inicio del periodo del reporte
            List<Reservacion> filtradas = reservacionDAO.obtenerPorPeriodo(inicio, fin); // Descarga de MySQL las reservaciones que se encuentren dentro del periodo de fechas calculado

            JFileChooser chooser = new JFileChooser(); // Abre el cuadro de diálogo estándar para guardar archivos del sistema operativo
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { // Evalúa si el usuario seleccionó una ruta válida para guardar el archivo
                String ruta = chooser.getSelectedFile().getAbsolutePath(); // Extrae la ruta de almacenamiento seleccionada por el usuario en el disco duro
                if (!ruta.toLowerCase().endsWith(".csv")) ruta += ".csv"; // Asegura que el archivo tenga la extensión .csv para que se abra correctamente en Excel
                
                if (Exportador.exportarCSV(ruta, filtradas)) { // Genera la escritura física del archivo CSV con los registros filtrados
                    JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente en formato CSV legible por Excel.");
                    this.dispose(); // Cierra la ventana emergente de exportación
                } else {
                    JOptionPane.showMessageDialog(this, "Error de escritura. Verifique los permisos de disco del sistema.");
                }
            }
        });
    }
}