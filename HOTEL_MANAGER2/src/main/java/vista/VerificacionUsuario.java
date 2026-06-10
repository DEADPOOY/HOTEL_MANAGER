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
        setLayout(new BorderLayout(10, 10)); // Distribución de bordes (Norte, Sur, Centro, Este, Oeste)

        lblInfo = new JLabel("Primer inicio de sesión: Debe cambiar su contraseña", JLabel.CENTER);
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblInfo.setForeground(Color.decode("#dc3545")); // Color rojo de advertencia crítica

        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 5, 5)); // Subpanel interno en rejilla
        lblNueva = new JLabel("  Nueva Contraseña:");
        txtNueva = new JPasswordField();
        panelCentro.add(lblNueva);
        panelCentro.add(txtNueva);

        btnValidar = new JButton("Validar y Guardar");
        btnValidar.setBackground(Color.decode("#28a745"));
        btnValidar.setForeground(Color.WHITE);

        add(lblInfo, BorderLayout.NORTH); // Posiciona la advertencia arriba
        add(panelCentro, BorderLayout.CENTER); // Posiciona el campo en medio
        add(btnValidar, BorderLayout.SOUTH); // Posiciona el botón en la parte inferior

        btnValidar.addActionListener(e -> { // Evento de cambio de clave
            String nueva = new String(txtNueva.getPassword());
            if (Validador.textoVacio(nueva)) {
                JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía.");
                return;
            }

            int id = Sesion.getUsuario().getIdUsuario(); // Obtiene el ID del usuario actual de la sesión en memoria
            if (usuarioDAO.cambiarContrasena(id, nueva)) { // Cambia el hash y desactiva la bandera primer_login a 0 en MySQL
                JOptionPane.showMessageDialog(this, "Contraseña actualizada. Inicie sesión nuevamente.");
                Sesion.cerrar(); // Limpia los datos de la memoria
                new InicioSesion().setVisible(true); // Lo manda al login general para entrar de nuevo con su nueva clave
                this.dispose(); // Destruye esta ventana de control de seguridad
            }
        });
    }
}