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

public class ElegirHabitacion extends JDialog {
    
    private JPanel panelGrilla;
    private HabitacionDAO habitacionDAO;
    private Habitacion habitacionSeleccionada = null; // Guardará el cuarto elegido antes de cerrar el cuadro

    public ElegirHabitacion(Window parent) { // Permite recibir herencia de cualquier ventana o sub-cuadro abierto
        super(parent, "Seleccionar Habitación Disponible", ModalityType.APPLICATION_MODAL); // Modalidad absoluta de bloqueo del sistema trasero
        habitacionDAO = new HabitacionDAO();
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        panelGrilla = new JPanel(new GridLayout(0, 3, 10, 10)); // Rejilla interna compacta de 3 columnas para optimizar espacio
        panelGrilla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JScrollPane(panelGrilla), BorderLayout.CENTER);
        add(new JLabel(" Azul Pastel = Individual | Rosa = Doble | Menta = Triple", JLabel.CENTER), BorderLayout.SOUTH); // Guía de colores de categorías

        cargarDisponibles(); // Carga en pantalla los cuartos desocupados
    }

    private void cargarDisponibles() { // Trae los cuartos con estatus 'Libre' desde MySQL y genera botones rápidos de selección
        List<Habitacion> libres = habitacionDAO.obtenerPorEstado("Libre");
        for (Habitacion h : libres) {
            JButton btn = new JButton("Num: " + h.getNumHabitacion()); // Inicializa el control con el número identificador
            
            // Colores suaves para distinguir fácilmente las categorías de las habitaciones
            if ("Individual".equals(h.getTipo())) {
                btn.setBackground(Color.decode("#AEC6CF")); // Azul pastel para las individuales
            } else if ("Doble".equals(h.getTipo())) {
                btn.setBackground(Color.decode("#FFB6C1")); // Rosa claro para las dobles
            } else {
                btn.setBackground(Color.decode("#98FB98")); // Verde menta para las triples
            }

            btn.addActionListener(e -> { // Evento de clic: selecciona el cuarto y cierra la ventana flotante automáticamente
                this.habitacionSeleccionada = h; // Guarda la instancia del cuarto seleccionado
                this.dispose(); // Cierra el buscador auxiliar de cuartos
            });
            panelGrilla.add(btn);
        }
    }

    public Habitacion getHabitacionSeleccionada() { // Método público crucial para recuperar el cuarto seleccionado desde el formulario que abrió esta ventana
        return habitacionSeleccionada;
    }
}