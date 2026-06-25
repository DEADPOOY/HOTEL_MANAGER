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
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private HabitacionDAO habitacionDAO;
    private ReservacionDAO reservacionDAO;

    public DashboardPanel() {
        habitacionDAO = new HabitacionDAO();
        reservacionDAO = new ReservacionDAO();

        setBackground(new Color(0xF7, 0xF5, 0xF0)); // Crema
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // --- PANEL DE MÉTRICAS (SUPERIOR) ---
        JPanel metricsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        metricsPanel.setOpaque(false);

        int totalHabitaciones = habitacionDAO.obtenerTodos().size();
        int disponibles = habitacionDAO.obtenerPorEstado("Disponible").size();
        int ocupadas = habitacionDAO.obtenerPorEstado("Ocupada").size();
        
        int reservacionesActivas = reservacionDAO.obtenerPorEstado("Activa").size();

        metricsPanel.add(crearCardMetrica("Habitaciones Totales", String.valueOf(totalHabitaciones), new Color(0x1A, 0x27, 0x44)));
        metricsPanel.add(crearCardMetrica("Disponibles", String.valueOf(disponibles), new Color(0x2E, 0x7D, 0x32))); 
        metricsPanel.add(crearCardMetrica("Ocupadas", String.valueOf(ocupadas), new Color(0xC6, 0x28, 0x28))); 
        metricsPanel.add(crearCardMetrica("Reservas Activas", String.valueOf(reservacionesActivas), new Color(0xC9, 0xA8, 0x4C))); 

        add(metricsPanel, BorderLayout.NORTH);

        // --- CUERPO CENTRAL ---
        JPanel cuerpoPanel = new JPanel(new BorderLayout(20, 20));
        cuerpoPanel.setOpaque(false);

        JPanel mainCard = new JPanel(new BorderLayout());
        mainCard.setBackground(Color.WHITE);
        mainCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2, 0xE8, 0xF0), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblSeccion = new JLabel("Resumen Operativo del Sistema");
        lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSeccion.setForeground(new Color(0x1A, 0x27, 0x44));
        mainCard.add(lblSeccion, BorderLayout.NORTH);

        JTextArea txtWelcome = new JTextArea("\nBienvenido al Sistema de Gestión Hotelera LUXE.\n\n"
                + "Use la barra de navegación lateral izquierda para acceder a los diferentes módulos operativos de acuerdo con su rol asignado en el sistema.\n\n"
                + "Monitoree el estado de las habitaciones en tiempo real desde el menú 'Habitaciones' o procese ingresos y egresos de huéspedes de forma automatizada.");
        txtWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtWelcome.setForeground(new Color(0x64, 0x74, 0x8B));
        txtWelcome.setEditable(false);
        txtWelcome.setOpaque(false);
        mainCard.add(txtWelcome, BorderLayout.CENTER);

        cuerpoPanel.add(mainCard, BorderLayout.CENTER);
        add(cuerpoPanel, BorderLayout.CENTER);
    }

    private JPanel crearCardMetrica(String titulo, String valor, Color colorBorde) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, colorBorde),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(0x64, 0x74, 0x8B));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(new Color(0x1A, 0x27, 0x44));

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);

        return card;
    }
}