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
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class InicioSesion extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private UsuarioDAO usuarioDAO;

    public InicioSesion() {
        usuarioDAO = new UsuarioDAO();
        
        setTitle("LUXE - Inicio de Sesión");
        setSize(420, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0xF7, 0xF5, 0xF0)); // Fondo Crema
        setLayout(new BorderLayout());

        // Header Superior Decorativo
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0x1A, 0x27, 0x44)); // Azul Marino
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(420, 120));
        headerPanel.setLayout(new GridBagLayout());
        
        JLabel lblTitulo = new JLabel("LUXE");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(0xC9, 0xA8, 0x4C)); // Dorado
        headerPanel.add(lblTitulo);
        add(headerPanel, BorderLayout.NORTH);

        // Formulario central
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;

        // Usuario
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUser = new JLabel("Nombre de Usuario");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(new Color(0x1A, 0x27, 0x44));
        formPanel.add(lblUser, gbc);

        gbc.gridy = 1;
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xBD, 0xC3, 0xC7), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(txtUsuario, gbc);

        // Contraseña
        gbc.gridy = 2;
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(new Color(0x1A, 0x27, 0x44));
        formPanel.add(lblPass, gbc);

        gbc.gridy = 3;
        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xBD, 0xC3, 0xC7), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(txtContrasena, gbc);

        // Botón Ingresar Redondeado
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 0, 10, 0);
        JButton btnIngresar = new JButton("Iniciar Sesión") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0xC9, 0xA8, 0x4C)); // Dorado
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setContentAreaFilled(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setPreferredSize(new Dimension(0, 45));
        
        btnIngresar.addActionListener(e -> ejecutarLogin());
        formPanel.add(btnIngresar, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void ejecutarLogin() {
        String nombre = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();

        if (nombre.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario user = usuarioDAO.login(nombre, contrasena);

        if (user != null) {
            if (user.getPrimerLogin() == 1) {
                // Forzar cambio de contraseña en primer ingreso
                String nueva = JOptionPane.showInputDialog(this, "Primer ingreso detectado. Ingrese su nueva contraseña:", "Cambio de Contraseña", JOptionPane.QUESTION_MESSAGE);
                if (nueva != null && !nueva.trim().isEmpty()) {
                    usuarioDAO.cambiarContrasena(user.getIdUsuario(), nueva.trim());
                    user.setPrimerLogin(0);
                    JOptionPane.showMessageDialog(this, "Contraseña actualizada con éxito. Ingrese de nuevo.");
                    txtContrasena.setText("");
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "Acción cancelada o inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            dispose();
            // Abrir el contenedor principal inyectando el usuario autenticado para validar roles
            new MainFrame(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Intente de nuevo.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}
