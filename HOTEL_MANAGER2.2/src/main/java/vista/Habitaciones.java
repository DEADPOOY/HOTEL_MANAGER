package vista;

/**
 *
 * @author deadpooy
 */

import dao.HabitacionDAO;
import modelo.Habitacion;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Habitaciones extends JPanel {

    private JPanel gridPanel;
    private HabitacionDAO habitacionDAO;
    
    public Habitaciones() {
        habitacionDAO = new HabitacionDAO();
        
        setBackground(new Color(0xF7, 0xF5, 0xF0));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel accionesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        accionesPanel.setOpaque(false);

        JButton btnRecargar = crearBotonAccion("Recargar", new Color(0x1A, 0x27, 0x44), 130);
        btnRecargar.addActionListener(e -> refrescarGrid());

        JButton btnNueva = crearBotonAccion("Nueva Habitación", new Color(0xC9, 0xA8, 0x4C), 180);
        btnNueva.addActionListener(e -> {
            CrearHabitacion dial = new CrearHabitacion((Frame) SwingUtilities.getWindowAncestor(this));
            dial.setVisible(true);
            refrescarGrid();
        });

        accionesPanel.add(btnRecargar);
        accionesPanel.add(btnNueva);
        topPanel.add(accionesPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        gridPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        add(scroll, BorderLayout.CENTER);

        refrescarGrid();
    }

    public void refrescarGrid() {
        gridPanel.removeAll();
        List<Habitacion> lista = habitacionDAO.obtenerTodos();

        for (Habitacion h : lista) {
            JPanel card = new JPanel(new BorderLayout(10, 10));
            card.setBackground(Color.WHITE);
            
            // ASIGNACIÓN DE COLORES SEGÚN TU ENUM DE BASE DE DATOS
            Color colorEstado;
            String estadoActual = h.getEstado();

            if (estadoActual.equalsIgnoreCase("Limpieza") || estadoActual.equalsIgnoreCase("Mantenimiento")) {
                colorEstado = new Color(0xF3, 0x9C, 0x12); // Amarillo Naranja de Advertencia
                card.setBackground(new Color(0xFF, 0xFA, 0xE6)); // Fondo sutilmente amarillo
            } else if (estadoActual.equalsIgnoreCase("Ocupada")) {
                colorEstado = new Color(0xC6, 0x28, 0x28); // Rojo
            } else { // 'Libre'
                colorEstado = new Color(0x2E, 0x7D, 0x32); // Verde
            }

            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 5, 0, 0, colorEstado),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JLabel lblNum = new JLabel("Habitación " + h.getNumHabitacion());
            lblNum.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblNum.setForeground(new Color(0x1A, 0x27, 0x44));

            JPanel datosPanel = new JPanel(new GridLayout(5, 1, 3, 3));
            datosPanel.setOpaque(false);
            datosPanel.add(new JLabel("Tipo: " + h.getTipo()));
            datosPanel.add(new JLabel("Piso: " + h.getPiso()));
            datosPanel.add(new JLabel("Capacidad: " + h.getNumCapacidad() + " pers."));
            datosPanel.add(new JLabel("Precio/Hr: $" + h.getPrecioHora()));
            
            // Etiqueta visual del estado de la habitación
            JLabel lblEstado = new JLabel("Estado: " + (estadoActual.equalsIgnoreCase("Limpieza") ? "MANTENIMIENTO" : estadoActual.toUpperCase()));
            lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblEstado.setForeground(colorEstado);
            datosPanel.add(lblEstado);

            JButton btnModificar = new JButton("Gestionar");
            btnModificar.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnModificar.setForeground(new Color(0x1A, 0x27, 0x44));
            btnModificar.addActionListener(e -> {
                ModificarHabitacion dialog = new ModificarHabitacion((Frame) SwingUtilities.getWindowAncestor(this), h);
                dialog.setVisible(true);
                refrescarGrid();
            });

            card.add(lblNum, BorderLayout.NORTH);
            card.add(datosPanel, BorderLayout.CENTER);
            card.add(btnModificar, BorderLayout.SOUTH);

            gridPanel.add(card);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JButton crearBotonAccion(String texto, Color fondo, int ancho) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(fondo);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(ancho, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}