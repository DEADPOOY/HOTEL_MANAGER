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
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Si se cierra de la X superior, solo se esconde sin matar el programa
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

        btnRegresar.addActionListener(e -> { // Acción de retorno
            new PantallaInicio().setVisible(true); // Abre de nuevo el menú raíz del hotel
            dispose(); // Destruye el formulario actual
        });

        btnAcceder.addActionListener(e -> { // Acción de acceso estricto administrativo
            String pass = new String(txtPass.getPassword());
            
            if (Validador.textoVacio(pass)) {
                JOptionPane.showMessageDialog(this, "Ingrese la contraseña.");
                return;
            }

            Usuario u = usuarioDAO.loginAdmin(pass); // Consulta en base de datos si la clave coincide con un Administrador
            if (u != null) {
                Sesion.iniciar(u); // Inicializa sesión del administrador de forma global
                new ControlUsuarios().setVisible(true); // Abre la sección crítica de personal y cuartos
                dispose(); // Cierra el login secundario
            } else {
                JOptionPane.showMessageDialog(this, "Contraseña de administrador incorrecta."); // Bloqueo de seguridad
            }
        });
    }
}