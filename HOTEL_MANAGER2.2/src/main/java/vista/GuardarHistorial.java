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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import modelo.Reservacion;

public class GuardarHistorial extends JDialog {
    private JRadioButton rbDia, rbSemana, rbMes;
    private ButtonGroup grupoPeriodo;
    private JButton btnExportar, btnCancelar;
    private ReservacionDAO reservacionDAO;

    public GuardarHistorial(JFrame parent) {
        super(parent, "Exportar Reporte Comercial", true);
        reservacionDAO = new ReservacionDAO();
        setSize(320, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 1, 10, 10));

        rbDia = new JRadioButton("Último Día", true);
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
        btnExportar.addActionListener(e -> {
            Calendar cal = Calendar.getInstance();
            Date fin = cal.getTime();
            
            if (rbDia.isSelected()) cal.add(Calendar.DAY_OF_YEAR, -1);
            else if (rbSemana.isSelected()) cal.add(Calendar.WEEK_OF_YEAR, -1);
            else cal.add(Calendar.MONTH, -1);
            
            Date inicio = cal.getTime();
            List<Reservacion> filtradas = reservacionDAO.obtenerPorPeriodo(inicio, fin);

            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String ruta = chooser.getSelectedFile().getAbsolutePath();
                if (!ruta.toLowerCase().endsWith(".csv")) ruta += ".csv";
                
                if (Exportador.exportarCSV(ruta, filtradas)) {
                    JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente.");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error de escritura de archivo.");
                }
            }
        });
    }
}