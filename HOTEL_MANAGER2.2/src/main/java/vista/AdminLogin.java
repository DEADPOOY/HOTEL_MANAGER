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

public class AdminLogin extends JFrame {
    
    private JLabel lblTitulo, lblPass;
    private JPasswordField txtPass;
    private JButton btnAcceder, btnRegresar;
    private UsuarioDAO usuarioDAO;

    public AdminLogin() {
        usuarioDAO = new UsuarioDAO();
        
        setTitle("Acceso Administrador");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        lblTitulo = new JLabel("Panel de Administrador", JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setForeground(Color.decode("#1a3a5c"));

        lblPass = new JLabel("  Contraseña:");
        txtPass = new JPasswordField();
        btnAcceder = new JButton("Acceder");
        btnRegresar = new JButton("Regresar");

        btnAcceder.setBackground(Color.decode("#28a745"));
        btnAcceder.setForeground(Color.WHITE);
        btnRegresar.setBackground(Color.decode("#6c757d"));
        btnRegresar.setForeground(Color.WHITE);

        add(lblTitulo); add(new JLabel(""));
        add(lblPass);   add(txtPass);
        add(btnAcceder); add(btnRegresar);

        btnRegresar.addActionListener(e -> {
            new PantallaInicio().setVisible(true);
            dispose();
        });

        btnAcceder.addActionListener(e -> {
            String pass = new String(txtPass.getPassword());
            
            if (Validador.textoVacio(pass)) {
                JOptionPane.showMessageDialog(this, "Ingrese la contraseña.");
                return;
            }

            Usuario u = usuarioDAO.loginAdmin(pass);
            if (u != null) {
                Sesion.iniciar(u);
                new ControlUsuarios().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Contraseña de administrador incorrecta.");
            }
        });
    }
}