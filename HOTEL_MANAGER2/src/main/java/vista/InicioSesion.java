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
import modelo.Usuario;

public class InicioSesion extends JFrame {
    
    private JLabel lblTitulo, lblUser, lblPass;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnIniciar, btnSalir;
    private UsuarioDAO usuarioDAO;

    public InicioSesion() {
        usuarioDAO = new UsuarioDAO();
        
        setTitle("Inicio de Sesión - Hotel Manager 2.0");
        setSize(380, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        lblTitulo = new JLabel("Iniciar Sesión", JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(Color.decode("#1a3a5c"));

        lblUser = new JLabel("  Usuario:");
        lblPass = new JLabel("  Contraseña:");
        txtUser = new JTextField();
        txtPass = new JPasswordField();

        btnIniciar = new JButton("Iniciar");
        btnSalir = new JButton("Salir");

        btnIniciar.setBackground(Color.decode("#28a745"));
        btnIniciar.setForeground(Color.WHITE);
        btnSalir.setBackground(Color.decode("#6c757d"));
        btnSalir.setForeground(Color.WHITE);

        add(lblTitulo); add(new JLabel(""));
        add(lblUser);   add(txtUser);
        add(lblPass);   add(txtPass);
        add(btnIniciar); add(btnSalir);

        btnSalir.addActionListener(e -> System.exit(0));

        btnIniciar.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            if (Validador.textoVacio(user) || Validador.textoVacio(pass)) {
                JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios.");
                return;
            }

            Usuario u = usuarioDAO.login(user, pass);
            if (u != null) {
                Sesion.iniciar(u);

                if (u.getPrimerLogin() == 1) {
                    new VerificacionUsuario().setVisible(true);
                } else if (Sesion.esAdmin() || Sesion.esAnalista()) {
                    new HistorialReservaciones().setVisible(true);
                } else {
                    new Reservaciones().setVisible(true);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        });
    }
}