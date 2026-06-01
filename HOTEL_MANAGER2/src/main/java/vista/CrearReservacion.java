/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.ClienteDAO;
import dao.HabitacionDAO;
import dao.RegistroDAO;
import dao.ReservacionDAO;
import java.awt.*;
import java.util.Date;
import javax.swing.*;
import modelo.Cliente;
import modelo.Habitacion;
import modelo.Registro;
import modelo.Reservacion;

public class CrearReservacion extends JDialog {
    
    private JTextField txtNomCliente, txtNumCliente, txtHabId;
    private JButton btnSeleccionarHab, btnGuardar, btnCancelar;
    private JSpinner spInicio, spFin;
    private Habitacion habitacionSeleccionada;
    private ReservacionDAO reservacionDAO;
    private ClienteDAO clienteDAO;
    private HabitacionDAO habitacionDAO;
    private Reservaciones padre;

    public CrearReservacion(Reservaciones parent) {
        super(parent, "Nueva Reservación", true);
        this.padre = parent;
        reservacionDAO = new ReservacionDAO();
        clienteDAO = new ClienteDAO();
        habitacionDAO = new HabitacionDAO();

        setSize(400, 320);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("  Nombre Cliente:")); txtNomCliente = new JTextField(); add(txtNomCliente);
        add(new JLabel("  Teléfono Cliente:")); txtNumCliente = new JTextField(); add(txtNumCliente);
        add(new JLabel("  Habitación ID:")); txtHabId = new JTextField(); txtHabId.setEditable(false); add(txtHabId);
        
        btnSeleccionarHab = new JButton("Seleccionar Hab"); add(btnSeleccionarHab); add(new JLabel(""));

        spInicio = new JSpinner(new SpinnerDateModel());
        spFin = new JSpinner(new SpinnerDateModel());
        add(new JLabel("  Fecha Inicio:")); add(spInicio);
        add(new JLabel("  Fecha Fin:")); add(spFin);

        btnGuardar = new JButton("Reservar");
        btnCancelar = new JButton("Cancelar");
        
        btnGuardar.setBackground(Color.decode("#28a745"));
        btnGuardar.setForeground(Color.WHITE);
        btnCancelar.setBackground(Color.decode("#6c757d"));
        btnCancelar.setForeground(Color.WHITE);

        add(btnGuardar); add(btnCancelar);

        btnSeleccionarHab.addActionListener(e -> {
            ElegirHabitacion dialog = new ElegirHabitacion(this);
            dialog.setVisible(true);
            habitacionSeleccionada = dialog.getHabitacionSeleccionada();
            if (habitacionSeleccionada != null) {
                txtHabId.setText(String.valueOf(habitacionSeleccionada.getIdHabitacion()));
            }
        });

        btnCancelar.addActionListener(e -> this.dispose());

        btnGuardar.addActionListener(e -> {
            String nom = txtNomCliente.getText().trim();
            String num = txtNumCliente.getText().trim();
            Date inicio = (Date) spInicio.getValue();
            Date fin = (Date) spFin.getValue();

            if (Validador.textoVacio(nom) || Validador.textoVacio(num) || habitacionSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Datos incompletos. Verifique todos los campos.");
                return;
            }
            if (!Validador.rangoFechasValido(inicio, fin)) {
                JOptionPane.showMessageDialog(this, "La fecha de fin debe ser posterior a la de inicio.");
                return;
            }

            Cliente c = clienteDAO.obtenerPorNumero(num);
            if (c == null) {
                c = new Cliente(0, nom, num);
                if (!clienteDAO.insertar(c)) {
                    JOptionPane.showMessageDialog(this, "Error al registrar el cliente.");
                    return;
                }
            }

            long diff = fin.getTime() - inicio.getTime();
            double horas = diff / (1000.0 * 60 * 60);
            double total = horas * habitacionSeleccionada.getPrecioHora();

            Reservacion r = new Reservacion(0, c.getIdCliente(),
                    habitacionSeleccionada.getIdHabitacion(),
                    new Date(), inicio, fin, horas, total, "Activa");

            if (reservacionDAO.insertar(r)) {
                habitacionDAO.cambiarEstado(habitacionSeleccionada.getIdHabitacion(), "Ocupada");

                RegistroDAO registroDAO = new RegistroDAO();
                Registro reg = new Registro(
                    0,
                    c.getIdCliente(),
                    r.getIdReservacion(),
                    habitacionSeleccionada.getIdHabitacion(),
                    new java.sql.Time(new Date().getTime()),
                    new Date()
                );
                registroDAO.insertar(reg);

                padre.llenarTabla();
                JOptionPane.showMessageDialog(this, "Reservación creada correctamente.");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear la reservación.");
            }
        });
    }
}