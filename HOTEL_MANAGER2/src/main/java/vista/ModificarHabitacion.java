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
import java.awt.*;
import java.util.List;
import javax.swing.*;
import modelo.Habitacion;

public class ModificarHabitacion extends JDialog {
    private JComboBox<String> cbHabitaciones; // Menú desplegable de habitaciones creadas
    private JComboBox<String> cbTipo, cbEstado;
    private JTextField txtPrecio;
    private JSpinner spPiso, spCapacidad;
    private JButton btnGuardar, btnCancelar;
    private HabitacionDAO habitacionDAO;
    private List<Habitacion> listaHabitaciones; // Caché local para mapear la selección del menú desplegable con los objetos Java
    private Habitaciones padre;

    public ModificarHabitacion(Habitaciones parent) {
        super(parent, "Modificar Habitación", true);
        this.padre = parent;
        habitacionDAO = new HabitacionDAO();
        setSize(350, 320);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(7, 2, 10, 10));

        cbHabitaciones = new JComboBox<>();
        listaHabitaciones = habitacionDAO.obtenerTodos(); // Descarga todo el inventario de cuartos para alimentar el selector
        for (Habitacion h : listaHabitaciones) {
            cbHabitaciones.addItem(h.getNumHabitacion()); // Añade solo los números de cuarto como opciones en el menú desplegable
        }

        cbTipo = new JComboBox<>(new String[]{"Individual", "Doble", "Triple"});
        cbEstado = new JComboBox<>(new String[]{"Libre", "Ocupada", "Limpieza"});
        txtPrecio = new JTextField();
        spPiso = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spCapacidad = new JSpinner(new SpinnerNumberModel(1, 1, 6, 1));

        add(new JLabel("  Seleccionar:")); add(cbHabitaciones);
        add(new JLabel("  Tipo:")); add(cbTipo);
        add(new JLabel("  Estado:")); add(cbEstado);
        add(new JLabel("  Precio/Hora:")); add(txtPrecio);
        add(new JLabel("  Piso:")); add(spPiso);
        add(new JLabel("  Capacidad:")); add(spCapacidad);

        btnGuardar = new JButton("Modificar");
        btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(Color.decode("#fd7e14"));
        btnGuardar.setForeground(Color.WHITE);
        btnCancelar.setBackground(Color.decode("#6c757d"));
        btnCancelar.setForeground(Color.WHITE);

        add(btnGuardar); add(btnCancelar);

        cbHabitaciones.addActionListener(e -> actualizarCamposCargados()); // Evento de cambio de opción: rellena los campos automáticamente al cambiar de cuarto en la lista
        if (!listaHabitaciones.isEmpty()) actualizarCamposCargados(); // Carga la información del primer cuarto de la lista de forma automática al abrir la pantalla

        btnCancelar.addActionListener(e -> this.dispose());
        btnGuardar.addActionListener(e -> { // Procesa los cambios en caliente de la habitación
            int index = cbHabitaciones.getSelectedIndex(); // Recupera la opción seleccionada (coincide con el índice de la lista en memoria)
            if (index == -1) return;
            try {
                Habitacion h = listaHabitaciones.get(index); // Extrae la instancia mapeada del cuarto
                h.setTipo(cbTipo.getSelectedItem().toString());
                h.setEstado(cbEstado.getSelectedItem().toString());
                h.setPrecioHora(Double.parseDouble(txtPrecio.getText().trim()));
                h.setPiso((int) spPiso.getValue());
                h.setNumCapacidad((int) spCapacidad.getValue());

                if (habitacionDAO.actualizar(h)) { // Manda la actualización a MySQL
                    parent.cargarHabitaciones(); // Refresca el panel visual principal del hotel de fondo
                    this.dispose(); // Cierra el cuadro
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Verifique los datos de costo e ingresos.");
            }
        });
    }

    private void actualizarCamposCargados() { // Carga la información del cuarto seleccionado en los campos correspondientes
        int index = cbHabitaciones.getSelectedIndex();
        if (index != -1) {
            Habitacion h = listaHabitaciones.get(index); // Obtiene el cuarto seleccionado de la lista en memoria
            cbTipo.setSelectedItem(h.getTipo()); // Rellena el combo con la opción guardada
            cbEstado.setSelectedItem(h.getEstado());
            txtPrecio.setText(String.valueOf(h.getPrecioHora()));
            spPiso.setValue(h.getPiso());
            spCapacidad.setValue(h.getNumCapacidad());
        }
    }
}