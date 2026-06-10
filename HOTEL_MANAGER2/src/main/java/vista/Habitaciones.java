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

public class Habitaciones extends JFrame {
    private JPanel panelGrilla; // Panel flexible que se adaptará al inventario de cuartos
    private JButton btnCrear, btnModificar, btnRegresar;
    private HabitacionDAO habitacionDAO;

    public Habitaciones() {
        habitacionDAO = new HabitacionDAO();
        setTitle("Monitoreo de Habitaciones");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Solo esconde la ventana al cerrar sin apagar el programa
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        panelGrilla = new JPanel(new GridLayout(0, 4, 15, 15)); // Rejilla adaptativa infinita de 4 columnas de ancho fijo
        panelGrilla.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Margen interno de separación

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Acciones alineadas a la derecha de la barra inferior
        btnCrear = new JButton("Crear Habitación");
        btnModificar = new JButton("Modificar Habitación");
        btnRegresar = new JButton("Regresar");

        btnCrear.setBackground(Color.decode("#28a745"));
        btnCrear.setForeground(Color.WHITE);
        btnModificar.setBackground(Color.decode("#fd7e14"));
        btnModificar.setForeground(Color.WHITE);
        btnRegresar.setBackground(Color.decode("#6c757d"));
        btnRegresar.setForeground(Color.WHITE);

        panelAcciones.add(btnCrear); panelAcciones.add(btnModificar); panelAcciones.add(btnRegresar);
        add(new JScrollPane(panelGrilla), BorderLayout.CENTER); // Soporta listas enormes metiendo scrollbars automáticos al panel
        add(panelAcciones, BorderLayout.SOUTH);

        cargarHabitaciones(); // Dibuja la cuadrícula de botones interactivos desde MySQL

        btnRegresar.addActionListener(e -> this.dispose());
        btnCrear.addActionListener(e -> new CrearHabitacion(this).setVisible(true)); // Abre diálogo de creación pasándole esta instancia
        btnModificar.addActionListener(e -> new ModificarHabitacion(this).setVisible(true)); // Abre diálogo de edición de cuartos
    }

    public void cargarHabitaciones() { // Lee la BD y genera botones interactivos coloreados según el estado del cuarto
        panelGrilla.removeAll(); // Limpia por completo la pantalla para redibujar cambios de estado
        List<Habitacion> lista = habitacionDAO.obtenerTodos(); // Pide el inventario del hotel
        for (Habitacion h : lista) {
            // Crea un botón renderizado con código HTML básico interno para centrar texto y forzar saltos de línea sin complicar componentes
            JButton btn = new JButton("<html><center><b>Hab: " + h.getNumHabitacion() + "</b><br>" + h.getTipo() + "<br>" + h.getEstado() + "</center></html>");
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            
            // Asigna colores institucionales de semáforo operacional según el estatus del cuarto en MySQL
            if ("Libre".equals(h.getEstado())) btn.setBackground(Color.decode("#90EE90")); // Verde claro libre
            else if ("Ocupada".equals(h.getEstado())) btn.setBackground(Color.decode("#FF6B6B")); // Rojo claro ocupada
            else btn.setBackground(Color.decode("#FFD700")); // Amarillo dorado para limpieza/mantenimiento

            panelGrilla.add(btn); // Inserta el botón directamente en el contenedor flexible
        }
        panelGrilla.revalidate(); // Reajusta dinámicamente los tamaños de los nuevos botones del Layout
        panelGrilla.repaint(); // Redibuja el lienzo gráfico en la pantalla de la computadora
    }
}