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
    private JSpinner spInicio, spFin; // Selectores de fecha avanzados incorporados de Swing
    private Habitacion habitacionSeleccionada; // Almacenará de forma temporal el cuarto desocupado seleccionado para rentar
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
        add(new JLabel("  Habitación ID:")); txtHabId = new JTextField(); txtHabId.setEditable(false); add(txtHabId); // El campo de ID no se puede editar a mano para forzar el uso del buscador de habitaciones
        
        btnSeleccionarHab = new JButton("Seleccionar Hab"); add(btnSeleccionarHab); add(new JLabel(""));

        spInicio = new JSpinner(new SpinnerDateModel()); // Configura los selectores en modo de calendario de fechas completas con hora integrada
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

        btnSeleccionarHab.addActionListener(e -> { // Acción para abrir el selector rápido de cuartos disponibles
            ElegirHabitacion dialog = new ElegirHabitacion(this); // Lanza el catálogo modal flotante secundario de habitaciones
            dialog.setVisible(true);
            habitacionSeleccionada = dialog.getHabitacionSeleccionada(); // Recupera el cuarto desocupado seleccionado por el usuario al cerrarse el catálogo
            if (habitacionSeleccionada != null) {
                txtHabId.setText(String.valueOf(habitacionSeleccionada.getIdHabitacion())); // Muestra el ID de la habitación en el campo del formulario de reservaciones
            }
        });

        btnCancelar.addActionListener(e -> this.dispose());

        btnGuardar.addActionListener(e -> { // Ejecuta la validación y cálculo del cobro financiero de la reservación
            String nom = txtNomCliente.getText().trim();
            String num = txtNumCliente.getText().trim();
            Date inicio = (Date) spInicio.getValue(); // Extrae los objetos Date seleccionados del calendario de los selectores correspondientes
            Date fin = (Date) spFin.getValue();

            if (Validador.textoVacio(nom) || Validador.textoVacio(num) || habitacionSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Datos incompletos. Verifique todos los campos.");
                return;
            }
            if (!Validador.rangoFechasValido(inicio, fin)) { // Lógica de protección horaria comercial
                JOptionPane.showMessageDialog(this, "La fecha de fin debe ser posterior a la de inicio.");
                return;
            }

            Cliente c = clienteDAO.obtenerPorNumero(num); // Busca si el cliente ya está registrado por su número de teléfono en MySQL para evitar duplicar registros
            if (c == null) { // Si el huésped visita el hotel por primera vez, se registra de manera automatizada al instante
                c = new Cliente(0, nom, num);
                if (!clienteDAO.insertar(c)) { // Guarda al cliente y recupera su ID en caliente
                    JOptionPane.showMessageDialog(this, "Error al registrar el cliente.");
                    return;
                }
            }

            long diff = fin.getTime() - inicio.getTime(); // Calcula la diferencia de tiempo en milisegundos entre el inicio y fin de la reservación
            double horas = diff / (1000.0 * 60 * 60); // Transforma los milisegundos a horas reales con decimales exactos
            double total = horas * habitacionSeleccionada.getPrecioHora(); // Multiplica las horas de estancia por la tarifa por hora del cuarto asignado

            Reservacion r = new Reservacion(0, c.getIdCliente(),
                    habitacionSeleccionada.getIdHabitacion(),
                    new Date(), inicio, fin, horas, total, "Activa"); // Inicializa la reservación con estado 'Activa'

            if (reservacionDAO.insertar(r)) { // Guarda la reservación en MySQL
                habitacionDAO.cambiarEstado(habitacionSeleccionada.getIdHabitacion(), "Ocupada"); // Cambia el estado de la habitación a 'Ocupada' para que no aparezca disponible en otras reservaciones

                RegistroDAO registroDAO = new RegistroDAO(); // Guarda una auditoría física en el historial de entradas (check-in)
                Registro reg = new Registro(
                    0,
                    c.getIdCliente(),
                    r.getIdReservacion(),
                    habitacionSeleccionada.getIdHabitacion(),
                    new java.sql.Time(new Date().getTime()), // Guarda la hora actual exacta del servidor
                    new Date() // Guarda la fecha del día actual
                );
                registroDAO.insertar(reg); // Guarda el registro de auditoría en la base de datos

                padre.llenarTabla(); // Refresca la tabla principal de reservaciones para mostrar la nueva renta activos
                JOptionPane.showMessageDialog(this, "Reservación creada y habitación marcada como ocupada correctamente.");
                this.dispose(); // Cierra el formulario de check-in de forma limpia
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear la reservación.");
            }
        });
    }
}