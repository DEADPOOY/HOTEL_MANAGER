/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private Usuario usuarioLogueado;
    private JPanel panelContenido;
    private CardLayout cardLayout;
    private JLabel lblModuloActual;

    public MainFrame(Usuario usuario) {
        this.usuarioLogueado = usuario;
        
        setTitle("Hotel Management - Bienvenido " + usuarioLogueado.getNombre());
        setSize(1280, 760);
        setMinimumSize(new Dimension(1024, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setLayout(new BorderLayout());

        // 1. SIDEBAR IZQUIERDA (Fija 260px)
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0x1A, 0x27, 0x44)); // Azul Marino
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Logo en Sidebar
        JLabel lblLogo = new JLabel("HOT-MAN");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(new Color(0xC9, 0xA8, 0x4C)); // Dorado
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(30));

        // 2. ÁREA DE CONTENIDO CENTRAL (CardLayout)
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(new Color(0xF7, 0xF5, 0xF0)); // Crema

        // Inicializar paneles de la vista
        DashboardPanel Principal = new DashboardPanel();
        Reservaciones reservaciones = new Reservaciones(usuarioLogueado);
        Habitaciones habitaciones = new Habitaciones();
        Clientes clientes = new Clientes();

        panelContenido.add(Principal, "Principal");
        panelContenido.add(reservaciones, "Reservaciones");
        panelContenido.add(habitaciones, "Habitaciones");
        panelContenido.add(clientes, "Clientes");

        // Agregar botones a la barra lateral según los Roles especificados
        agregarBotonMenu(sidebar, "Principal", "Principal");
        agregarBotonMenu(sidebar, "Reservaciones", "Reservaciones");
        agregarBotonMenu(sidebar, "Habitaciones", "Habitaciones");
        agregarBotonMenu(sidebar, "Huéspedes", "Clientes");

        // Control de Accesos por Roles
        String rol = usuarioLogueado.getRol().toLowerCase();
        if (rol.contains("admin") || rol.contains("administrador")) {
            ControlUsuarios controlUsuarios = new ControlUsuarios();
            panelContenido.add(controlUsuarios, "Usuarios");
            agregarBotonMenu(sidebar, "Personal / Usuarios", "Usuarios");
        }
        
        if (rol.contains("analista") || rol.contains("admin") || rol.contains("administrador")) {
            HistorialReservaciones historial = new HistorialReservaciones();
            panelContenido.add(historial, "Reportes");
            agregarBotonMenu(sidebar, "Reportes / Historial", "Reportes");
        }

        sidebar.add(Box.createVerticalGlue());

        // Botón Cerrar Sesión
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setForeground(new Color(0xE7, 0x4C, 0x3C));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            dispose();
            new InicioSesion().setVisible(true);
        });
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // 3. HEADER SUPERIOR
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 65));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE2, 0xE8, 0xF0)));
        header.setBorder(BorderFactory.createCompoundBorder(header.getBorder(), BorderFactory.createEmptyBorder(0, 25, 0, 25)));

        lblModuloActual = new JLabel("Principal");
        lblModuloActual.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblModuloActual.setForeground(new Color(0x1A, 0x27, 0x44));
        header.add(lblModuloActual, BorderLayout.WEST);

        JLabel lblUserPanel = new JLabel("Rol: " + usuarioLogueado.getRol() + " | " + usuarioLogueado.getNombre());
        lblUserPanel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUserPanel.setForeground(new Color(0x64, 0x74, 0x8B));
        header.add(lblUserPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);
    }

    private void agregarBotonMenu(JPanel sidebar, String texto, String cardName) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        btn.setMaximumSize(new Dimension(230, 40));
        btn.setPreferredSize(new Dimension(230, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(0xF7, 0xF5, 0xF0));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(0xC9, 0xA8, 0x4C));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(0xF7, 0xF5, 0xF0));
            }
        });

        btn.addActionListener(e -> {
            cardLayout.show(panelContenido, cardName);
            lblModuloActual.setText(cardName);
        });

        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(8));
    }
}