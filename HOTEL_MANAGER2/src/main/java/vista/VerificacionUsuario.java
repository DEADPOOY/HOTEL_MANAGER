/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.UsuarioDAO;
import java.awt.*;
import javax.swing.*;

public class VerificacionUsuario extends JFrame {
    private JLabel lblInfo, lblNueva;
    private JPasswordField txtNueva;
    private JButton btnValidar;
    private UsuarioDAO usuarioDAO;

    public VerificacionUsuario() {
        usuarioDAO = new UsuarioDAO();
        setTitle("Actualización de Seguridad");
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        lblInfo = new JLabel("Primer inicio de sesión: Debe cambiar su contraseña", JLabel.CENTER);
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblInfo.setForeground(Color.decode("#dc3545"));

        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 5, 5));
        lblNueva = new JLabel("  Nueva Contraseña:");
        txtNueva = new JPasswordField();
        panelCentro.add(lblNueva);
        panelCentro.add(txtNueva);

        btnValidar = new JButton("Validar y Guardar");
        btnValidar.setBackground(Color.decode("#28a745"));
        btnValidar.setForeground(Color.WHITE);

        add(lblInfo, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
        add(btnValidar, BorderLayout.SOUTH);

        btnValidar.addActionListener(e -> {
            String nueva = new String(txtNueva.getPassword());
            if (Validador.textoVacio(nueva)) {
                JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía.");
                return;
            }

            int id = Sesion.getUsuario().getIdUsuario();
            if (usuarioDAO.cambiarContrasena(id, nueva)) {
                JOptionPane.showMessageDialog(this, "Contraseña actualizada. Inicie sesión nuevamente.");
                Sesion.cerrar();
                new InicioSesion().setVisible(true);
                this.dispose();
            }
        });
    }
}