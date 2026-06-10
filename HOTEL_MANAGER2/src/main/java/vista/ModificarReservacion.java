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

// Formulario emergente tipo JDialog diseñado para alterar los parámetros de tiempo de una reservación activa
public class ModificarReservacion extends JDialog {
    
    private JTextField txtIdRes, txtIdCliente, txtIdHab;
    private JSpinner spInicio, spFin;
    private JButton btnModificar, btnCancelar;
    private ReservacionDAO reservacionDAO;
    private Reservaciones padre; // Referencia de acoplamiento hacia la ventana padre para invocar actualizaciones
    private Reservacion reservacionActual; // Objeto temporal de persistencia en memoria durante la edición

    public ModificarReservacion(JFrame parent) {
        // Bloquea el enfoque del sistema trasero al configurarlo en modo modal (true)
        super(parent, "Editar Reservación", true);
        this.padre = (Reservaciones) parent;
        reservacionDAO = new ReservacionDAO();
        setSize(380, 280);
        setLocationRelativeTo(parent); // Despliega la ventana flotante alineada al centro del panel de reservaciones
        setLayout(new GridLayout(6, 2, 10, 10)); // Grid estructurado para organizar etiquetas junto a controles

        // Los campos llave se configuran en modo no editable para resguardar la integridad referencial de la BD
        txtIdRes = new JTextField(); txtIdRes.setEditable(false);
        txtIdCliente = new JTextField(); txtIdCliente.setEditable(false);
        txtIdHab = new JTextField(); txtIdHab.setEditable(false);
        
        // Componentes avanzados de Swing configurados para la captura de fechas completas y horas precisas
        spInicio = new JSpinner(new SpinnerDateModel());
        spFin = new JSpinner(new SpinnerDateModel());

        // Inserción secuencial de filas y columnas en el GridLayout de la ventana emergente
        add(new JLabel("  ID Reservación:")); add(txtIdRes);
        add(new JLabel("  ID Cliente:")); add(txtIdCliente);
        add(new JLabel("  ID Habitación:")); add(txtIdHab);
        add(new JLabel("  Fecha Inicio:")); add(spInicio);
        add(new JLabel("  Fecha Fin:")); add(spFin);

        btnModificar = new JButton("Modificar");
        btnCancelar = new JButton("Cancelar");
        
        btnModificar.setBackground(Color.decode("#fd7e14")); // Color naranja distintivo de actualización
        btnModificar.setForeground(Color.WHITE);
        btnCancelar.setBackground(Color.decode("#6c757d")); // Color gris neutral de cancelación
        btnCancelar.setForeground(Color.WHITE);

        add(btnModificar); add(btnCancelar);

        // Descarta de la memoria la ventana actual sin interrumpir o apagar el programa raíz
        btnCancelar.addActionListener(e -> this.dispose());
        
        // Procesa el guardado físico de los nuevos horarios y recalcula el impacto financiero en caliente
        btnModificar.addActionListener(e -> {
            if (reservacionActual == null) return; // Validation preventiva de carga de datos nula
            
            Date inicio = (Date) spInicio.getValue();
            Date fin = (Date) spFin.getValue();

            // Valida mediante la clase de utilidad que el check-out sea cronológicamente posterior al check-in
            if (!Validador.rangoFechasValido(inicio, fin)) {
                JOptionPane.showMessageDialog(this, "La fecha de fin debe ser posterior a la de inicio.");
                return;
            }

            // Actualiza los atributos cronológicos de la instancia en memoria volátil
            reservacionActual.setFechaInicio(inicio);
            reservacionActual.setFechaFin(fin);
            
            // Deducción matemática de horas transcurridas a partir de marcas de tiempo en milisegundos
            long diff = fin.getTime() - inicio.getTime();
            double horas = diff / (1000.0 * 60 * 60); // Conversión formal de milisegundos a unidades de hora con decimales
            reservacionActual.setPeriodo(horas);

            // Consulta la tarifa por hora asignada a la habitación para regenerar el costo total
            HabitacionDAO habitacionDAO = new HabitacionDAO();
            Habitacion hab = habitacionDAO.obtenerPorId(reservacionActual.getIdHabitacion());
            if (hab != null) {
                // Modifica el costo total multiplicando las nuevas horas calculadas por el precio base de la habitación
                reservacionActual.setPrecioTotal(horas * hab.getPrecioHora());
            }

            // Ejecuta la sentencia UPDATE en MySQL a través de la capa de persistencia del DAO
            if (reservacionDAO.actualizar(reservacionActual)) {
                padre.llenarTabla(); // Invoca el refresco dinámico de la tabla en el panel trasero de recepción
                JOptionPane.showMessageDialog(this, "Reservación modificada correctamente y costos recalculados de forma automática.");
                this.dispose(); // Destruye de forma limpia el cuadro de diálogo actual
            } else {
                JOptionPane.showMessageDialog(this, "Error al modificar la reservación.");
            }
        });
    }

    // Mapea la información original del registro seleccionado en los campos de texto y calendarios del formulario
    public void cargarDatos(Reservacion r) {
        this.reservacionActual = r;
        txtIdRes.setText(String.valueOf(r.getIdReservacion()));
        txtIdCliente.setText(String.valueOf(r.getIdCliente()));
        txtIdHab.setText(String.valueOf(r.getIdHabitacion()));
        spInicio.setValue(r.getFechaInicio()); // Posiciona de forma automática el selector en el día y hora guardados
        spFin.setValue(r.getFechaFin());
    }
}