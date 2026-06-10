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
import java.util.Date;
import javax.swing.*;
import modelo.Habitacion;
import modelo.Reservacion;

public class ModificarReservacion extends JDialog {
    
    private JTextField txtIdRes, txtIdCliente, txtIdHab;
    private JSpinner spInicio, spFin;
    private JButton btnModificar, btnCancelar;
    private ReservacionDAO reservacionDAO;
    private Reservaciones padre;
    private Reservacion reservacionActual;

    public ModificarReservacion(JFrame parent) {
        super(parent, "Editar Reservación", true);
        this.padre = (Reservaciones) parent;
        reservacionDAO = new ReservacionDAO();
        setSize(380, 280);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(6, 2, 10, 10));

        txtIdRes = new JTextField(); txtIdRes.setEditable(false);
        txtIdCliente = new JTextField(); txtIdCliente.setEditable(false);
        txtIdHab = new JTextField(); txtIdHab.setEditable(false);
        spInicio = new JSpinner(new SpinnerDateModel());
        spFin = new JSpinner(new SpinnerDateModel());

        add(new JLabel("  ID Reservación:")); add(txtIdRes);
        add(new JLabel("  ID Cliente:")); add(txtIdCliente);
        add(new JLabel("  ID Habitación:")); add(txtIdHab);
        add(new JLabel("  Fecha Inicio:")); add(spInicio);
        add(new JLabel("  Fecha Fin:")); add(spFin);

        btnModificar = new JButton("Modificar");
        btnCancelar = new JButton("Cancelar");
        
        btnModificar.setBackground(Color.decode("#fd7e14"));
        btnModificar.setForeground(Color.WHITE);
        btnCancelar.setBackground(Color.decode("#6c757d"));
        btnCancelar.setForeground(Color.WHITE);

        add(btnModificar); add(btnCancelar);

        btnCancelar.addActionListener(e -> this.dispose());
        
        btnModificar.addActionListener(e -> {
            if (reservacionActual == null) return;
            Date inicio = (Date) spInicio.getValue();
            Date fin = (Date) spFin.getValue();

            if (!Validador.rangoFechasValido(inicio, fin)) {
                JOptionPane.showMessageDialog(this, "La fecha de fin debe ser posterior a la de inicio.");
                return;
            }

            reservacionActual.setFechaInicio(inicio);
            reservacionActual.setFechaFin(fin);
            
            long diff = fin.getTime() - inicio.getTime();
            double horas = diff / (1000.0 * 60 * 60);
            reservacionActual.setPeriodo(horas);

            HabitacionDAO habitacionDAO = new HabitacionDAO();
            Habitacion hab = habitacionDAO.obtenerPorId(reservacionActual.getIdHabitacion());
            if (hab != null) {
                reservacionActual.setPrecioTotal(horas * hab.getPrecioHora());
            }

            if (reservacionDAO.actualizar(reservacionActual)) {
                padre.llenarTabla();
                JOptionPane.showMessageDialog(this, "Reservación modificada correctamente.");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al modificar la reservación.");
            }
        });
    }

    public void cargarDatos(Reservacion r) {
        this.reservacionActual = r;
        txtIdRes.setText(String.valueOf(r.getIdReservacion()));
        txtIdCliente.setText(String.valueOf(r.getIdCliente()));
        txtIdHab.setText(String.valueOf(r.getIdHabitacion()));
        spInicio.setValue(r.getFechaInicio());
        spFin.setValue(r.getFechaFin());
    }
}