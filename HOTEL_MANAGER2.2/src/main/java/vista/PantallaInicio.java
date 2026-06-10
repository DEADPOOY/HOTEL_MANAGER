/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import java.awt.*;
import javax.swing.*;

public class PantallaInicio extends JFrame {
    private JButton btnPersonal, btnRecepcion;

    public PantallaInicio() {
        setTitle("Hotel Manager 2.0 - Inicio");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel lblInfo = new JLabel("Seleccione el Panel de Acceso", JLabel.CENTER);
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblInfo.setForeground(Color.decode("#1a3a5c"));

        btnPersonal = new JButton("Área de Operaciones (Login General)");
        btnPersonal.setBackground(Color.decode("#1a3a5c"));
        btnPersonal.setForeground(Color.WHITE);
        btnPersonal.setSize(50, 30);

        btnRecepcion = new JButton("Control de Personal (Solo Admin)");
        btnRecepcion.setBackground(Color.decode("#fd7e14"));
        btnRecepcion.setForeground(Color.WHITE);

        add(lblInfo);
        add(btnPersonal);
        add(btnRecepcion);

        btnPersonal.addActionListener(e -> {
            new InicioSesion().setVisible(true);
            this.dispose();
        });

        btnRecepcion.addActionListener(e -> {
            new AdminLogin().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PantallaInicio().setVisible(true));
    }
}