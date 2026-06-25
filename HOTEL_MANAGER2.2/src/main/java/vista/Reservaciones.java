/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.ReservacionDAO;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Reservaciones extends JPanel {

    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    private ReservacionDAO reservacionDAO;
    private List<String[]> registrosActuales;

    public Reservaciones(Usuario usuario) {
        // 1. Inicializar el DAO e interfaz básica
        this.reservacionDAO = new ReservacionDAO();

        setBackground(new Color(0xF7, 0xF5, 0xF0));
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // 2. CONSTRUIR EL MODELO Y LAS COLUMNAS PRIMERO (Crítico para evitar NullPointerException)
        String[] columnas = {"N°", "Huésped", "Habitación", "Tipo", "Inicio", "Fin", "Horas", "Total", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaReservas = new JTable(modeloTabla);
        tablaReservas.setRowHeight(30);
        tablaReservas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaReservas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaReservas.getTableHeader().setBackground(new Color(0x1A, 0x27, 0x44));
        tablaReservas.getTableHeader().setForeground(Color.WHITE);
        tablaReservas.getTableHeader().setReorderingAllowed(false);

        // 3. Crear paneles de la interfaz (Superior)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JButton btnNueva = crearBotonPremium("Nueva Reservación", new Color(0xC9, 0xA8, 0x4C));
        btnNueva.addActionListener(e -> {
            CrearReservacion dialog = new CrearReservacion((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            cargarDatos();
        });
        topPanel.add(btnNueva, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 4. Panel central contenedor de la tabla
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JScrollPane scroll = new JScrollPane(tablaReservas);
        tableCard.add(scroll, BorderLayout.CENTER);

        // 5. Panel inferior con botones de acción
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomPanel.setOpaque(false);

        JButton btnCancelar = crearBotonPremium("Cancelar Reserva Seleccionada", new Color(0xE7, 0x4C, 0x3C));
        btnCancelar.addActionListener(e -> cancelarReserva());
        bottomPanel.add(btnCancelar);

        tableCard.add(bottomPanel, BorderLayout.SOUTH);
        add(tableCard, BorderLayout.CENTER);

        cargarDatos();
    }

    public void cargarDatos() {
        // Validación de seguridad para que la app no colapse si se invoca antes de tiempo
        if (modeloTabla == null) {
            return;
        }

        modeloTabla.setRowCount(0);
        registrosActuales = reservacionDAO.obtenerVistaTabla("Activa");
        
        if (registrosActuales != null) {
            for (String[] fila : registrosActuales) {
                Object[] filaVisual = {fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8]};
                modeloTabla.addRow(filaVisual);
            }
        }
        
        revalidate();
        repaint();
    }

    private void cancelarReserva() {
        int filaSeleccionada = tablaReservas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reservación de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idReal = Integer.parseInt(registrosActuales.get(filaSeleccionada)[9]);
        String huesped = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
        
        int conf = JOptionPane.showConfirmDialog(this, "¿Seguro de cancelar la reservación de " + huesped + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            if (reservacionDAO.cancelar(idReal)) {
                JOptionPane.showMessageDialog(this, "Reservación cancelada correctamente.");
                cargarDatos();
            }
        }
    }

    private JButton crearBotonPremium(String texto, Color fondo) {
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 38));
        return btn;
    }
}