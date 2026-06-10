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
import javax.swing.*;
import modelo.Habitacion;

public class CrearHabitacion extends JDialog {
    private JTextField txtNum, txtPrecio;
    private JComboBox<String> cbTipo; // Menú desplegable selector
    private JSpinner spPiso, spCapacidad; // Selectores numéricos controlados mediante flechas
    private JButton btnGuardar, btnCancelar;
    private HabitacionDAO habitacionDAO;
    private Habitaciones padre;

    public CrearHabitacion(Habitaciones parent) {
        super(parent, "Nueva Habitación", true);
        this.padre = parent;
        habitacionDAO = new HabitacionDAO();
        setSize(320, 280);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("  Número:")); txtNum = new JTextField(); add(txtNum);
        add(new JLabel("  Tipo:")); cbTipo = new JComboBox<>(new String[]{"Individual", "Doble", "Triple"}); add(cbTipo);
        add(new JLabel("  Piso:")); spPiso = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); add(spPiso); // Modelo numérico de rango 1 a 10 con incrementos unitarios
        add(new JLabel("  Precio/Hora:")); txtPrecio = new JTextField(); add(txtPrecio);
        add(new JLabel("  Capacidad:")); spCapacidad = new JSpinner(new SpinnerNumberModel(1, 1, 6, 1)); add(spCapacidad); // Capacidad controlada de personas (rango 1 a 6)

        btnGuardar = new JButton("Crear");
        btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(Color.decode("#28a745"));
        btnGuardar.setForeground(Color.WHITE);
        btnCancelar.setBackground(Color.decode("#6c757d"));
        btnCancelar.setForeground(Color.WHITE);

        add(btnGuardar); add(btnCancelar);

        btnCancelar.addActionListener(e -> this.dispose());

        btnGuardar.addActionListener(e -> { // Acción de inserción física
            String num = txtNum.getText().trim();
            String precioStr = txtPrecio.getText().trim();

            if (Validador.textoVacio(num) || Validador.textoVacio(precioStr)) {
                JOptionPane.showMessageDialog(this, "Campos obligatorios vacíos.");
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr); // Convierte la entrada de texto a valor flotante monetario
                Habitacion h = new Habitacion(0, num, cbTipo.getSelectedItem().toString(),
                        (int) spPiso.getValue(), precio, (int) spCapacidad.getValue(), "Libre"); // Inicializa el cuarto libre por defecto
                
                if (habitacionDAO.insertar(h)) {
                    padre.cargarHabitaciones(); // Forzar redibujo instantáneo de la cuadrícula trasera del menú de cuartos
                    this.dispose(); // Cierra el cuadro actual
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Formato de precio inválido. Use solo números y punto decimal.");
            }
        });
    }
}