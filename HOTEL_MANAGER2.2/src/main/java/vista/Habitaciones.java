package vista;

/**
 *
 * @author deadpooy
 */

import dao.HabitacionDAO;
import modelo.Habitacion;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class Habitaciones extends JPanel {

    private static final String TODOS_LOS_PISOS = "Todos los pisos";
    private static final String TODOS_LOS_ESTADOS = "Todos los estados";

    private JPanel gridPanel;
    private HabitacionDAO habitacionDAO;
    private JTextField txtBuscarNumero;
    private JComboBox<String> cbFiltroPiso;
    private JComboBox<String> cbFiltroEstado;
    
    public Habitaciones() {
        habitacionDAO = new HabitacionDAO();
        
        setBackground(new Color(0xF7, 0xF5, 0xF0));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setOpaque(false);

        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filtrosPanel.setOpaque(false);

        JLabel lblBuscar = new JLabel("Buscar N°:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBuscar.setForeground(new Color(0x1A, 0x27, 0x44));
        filtrosPanel.add(lblBuscar);

        txtBuscarNumero = new JTextField(12);
        txtBuscarNumero.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscarNumero.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { refrescarGrid(); }
            @Override
            public void removeUpdate(DocumentEvent e) { refrescarGrid(); }
            @Override
            public void changedUpdate(DocumentEvent e) { refrescarGrid(); }
        });
        filtrosPanel.add(txtBuscarNumero);

        JLabel lblPiso = new JLabel("Piso:");
        lblPiso.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPiso.setForeground(new Color(0x1A, 0x27, 0x44));
        filtrosPanel.add(lblPiso);

        cbFiltroPiso = new JComboBox<>();
        cbFiltroPiso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFiltroPiso.addActionListener(e -> refrescarGrid());
        filtrosPanel.add(cbFiltroPiso);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEstado.setForeground(new Color(0x1A, 0x27, 0x44));
        filtrosPanel.add(lblEstado);

        cbFiltroEstado = new JComboBox<>(new String[]{
                TODOS_LOS_ESTADOS, "Disponible", "Ocupada", "Mantenimiento"
        });
        cbFiltroEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFiltroEstado.addActionListener(e -> refrescarGrid());
        filtrosPanel.add(cbFiltroEstado);

        JPanel accionesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        accionesPanel.setOpaque(false);

        JButton btnRecargar = crearBotonAccion("Recargar", new Color(0x1A, 0x27, 0x44), 130);
        btnRecargar.addActionListener(e -> {
            cargarFiltroPisos();
            refrescarGrid();
        });

        JButton btnNueva = crearBotonAccion("Nueva Habitación", new Color(0xC9, 0xA8, 0x4C), 180);
        btnNueva.addActionListener(e -> {
            CrearHabitacion dial = new CrearHabitacion((Frame) SwingUtilities.getWindowAncestor(this));
            dial.setVisible(true);
            cargarFiltroPisos();
            refrescarGrid();
        });

        accionesPanel.add(btnRecargar);
        accionesPanel.add(btnNueva);
        topPanel.add(filtrosPanel, BorderLayout.WEST);
        topPanel.add(accionesPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        gridPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        add(scroll, BorderLayout.CENTER);

        cargarFiltroPisos();
        refrescarGrid();
    }

    private void cargarFiltroPisos() {
        String seleccionActual = cbFiltroPiso.getSelectedItem() != null
                ? cbFiltroPiso.getSelectedItem().toString()
                : TODOS_LOS_PISOS;

        cbFiltroPiso.removeAllItems();
        cbFiltroPiso.addItem(TODOS_LOS_PISOS);
        for (int piso : habitacionDAO.obtenerPisos()) {
            cbFiltroPiso.addItem(String.valueOf(piso));
        }

        boolean existeSeleccion = false;
        for (int i = 0; i < cbFiltroPiso.getItemCount(); i++) {
            if (cbFiltroPiso.getItemAt(i).equals(seleccionActual)) {
                cbFiltroPiso.setSelectedIndex(i);
                existeSeleccion = true;
                break;
            }
        }
        if (!existeSeleccion) {
            cbFiltroPiso.setSelectedIndex(0);
        }
    }

    public void refrescarGrid() {
        gridPanel.removeAll();
        List<Habitacion> lista = habitacionDAO.obtenerTodos();

        String busqueda = txtBuscarNumero.getText().trim().toLowerCase();
        String pisoSeleccionado = cbFiltroPiso.getSelectedItem() != null
                ? cbFiltroPiso.getSelectedItem().toString()
                : TODOS_LOS_PISOS;
        Integer pisoFiltro = pisoSeleccionado.equals(TODOS_LOS_PISOS) ? null : Integer.parseInt(pisoSeleccionado);
        String estadoSeleccionado = cbFiltroEstado.getSelectedItem() != null
                ? cbFiltroEstado.getSelectedItem().toString()
                : TODOS_LOS_ESTADOS;

        int coincidencias = 0;
        for (Habitacion h : lista) {
            if (!busqueda.isEmpty() && !h.getNumHabitacion().toLowerCase().contains(busqueda)) {
                continue;
            }
            if (pisoFiltro != null && h.getPiso() != pisoFiltro) {
                continue;
            }
            if (!estadoSeleccionado.equals(TODOS_LOS_ESTADOS) && !coincideEstado(h.getEstado(), estadoSeleccionado)) {
                continue;
            }
            coincidencias++;
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

        if (coincidencias == 0) {
            JLabel lblVacio = new JLabel("No se encontraron habitaciones con los filtros aplicados.", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblVacio.setForeground(new Color(0x64, 0x74, 0x8B));
            gridPanel.add(lblVacio);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private boolean coincideEstado(String estadoActual, String filtro) {
        if (estadoActual == null) {
            return false;
        }
        String estado = estadoActual.trim();
        return switch (filtro) {
            case "Disponible" -> estado.equalsIgnoreCase("Disponible") || estado.equalsIgnoreCase("Libre");
            case "Ocupada" -> estado.equalsIgnoreCase("Ocupada");
            case "Mantenimiento" -> estado.equalsIgnoreCase("Limpieza") || estado.equalsIgnoreCase("Mantenimiento");
            default -> true;
        };
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