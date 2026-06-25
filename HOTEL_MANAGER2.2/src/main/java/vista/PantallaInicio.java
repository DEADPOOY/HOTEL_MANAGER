/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PantallaInicio extends JFrame {

    public PantallaInicio() {
        setUndecorated(true);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Hacer la ventana con esquinas redondeadas
        setShape(new RoundRectangle2D.Double(0, 0, 500, 350, 20, 20));

        // Contenedor Principal
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Degradado sofisticado con el azul marino de la paleta
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x1A, 0x27, 0x44), 0, getHeight(), new Color(0x0F, 0x17, 0x2A));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Contenido Central
        JPanel centroPanel = new JPanel(new GridBagLayout());
        centroPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblLogo = new JLabel("HOT-MAN");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 52));
        lblLogo.setForeground(new Color(0xC9, 0xA8, 0x4C)); // Dorado acento
        centroPanel.add(lblLogo, gbc);

        gbc.gridy = 1;
        JLabel lblSub = new JLabel("Hotel Management System");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSub.setForeground(new Color(0xF7, 0xF5, 0xF0)); // Crema fondo
        centroPanel.add(lblSub, gbc);

        // Barra de progreso minimalista dorada
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        JProgressBar progress = new JProgressBar();
        progress.setPreferredSize(new Dimension(300, 4));
        progress.setBackground(new Color(0x2A, 0x3A, 0x5C));
        progress.setForeground(new Color(0xC9, 0xA8, 0x4C));
        progress.setBorderPainted(false);
        centroPanel.add(progress, gbc);

        panelPrincipal.add(centroPanel, BorderLayout.CENTER);
        
        JLabel lblFooter = new JLabel("Cargando módulos del sistema...", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFooter.setForeground(new Color(0x8A, 0x9A, 0xB0));
        panelPrincipal.add(lblFooter, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Simulación de carga elegante
        Timer timer = new Timer(30, null);
        timer.addActionListener(e -> {
            int val = progress.getValue();
            if (val < 100) {
                progress.setValue(val + 2);
            } else {
                timer.stop();
                dispose();
                // Redirige al inicio de sesión mejorado
                new InicioSesion().setVisible(true);
            }
        });
        timer.start();
    }
}
