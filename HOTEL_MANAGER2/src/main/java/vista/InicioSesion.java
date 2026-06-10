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
        usuarioDAO = new UsuarioDAO(); // Instancia el objeto de control de base de datos de usuarios
        
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
        txtPass = new JPasswordField(); // Campo oculto para contraseñas protegiendo los caracteres

        btnIniciar = new JButton("Iniciar");
        btnSalir = new JButton("Salir");

        btnIniciar.setBackground(Color.decode("#28a745")); // Color verde institucional para acciones positivas
        btnIniciar.setForeground(Color.WHITE);
        btnSalir.setBackground(Color.decode("#6c757d")); // Color gris neutral
        btnSalir.setForeground(Color.WHITE);

        add(lblTitulo); add(new JLabel("")); // Espacio en blanco de alineación
        add(lblUser);   add(txtUser);
        add(lblPass);   add(txtPass);
        add(btnIniciar); add(btnSalir);

        btnSalir.addActionListener(e -> System.exit(0)); // Cierra el programa por completo inmediatamente

        btnIniciar.addActionListener(e -> { // Acción para validar credenciales de entrada
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword()); // Extrae el texto del arreglo de caracteres oculto

            if (Validador.textoVacio(user) || Validador.textoVacio(pass)) {
                JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios."); // Muestra alerta flotante de datos incompletos
                return; // Interrumpe el hilo de ejecución actual
            }

            Usuario u = usuarioDAO.login(user, pass); // Ejecuta la consulta de login encriptada en la base de datos
            if (u != null) { // Si se encontró coincidencia válida
                Sesion.iniciar(u); // Inicializa las variables de sesión global de la aplicación

                if (u.getPrimerLogin() == 1) { // Evalúa si tiene contraseña provisional obligatoria de cambio
                    new VerificacionUsuario().setVisible(true); // Redirecciona a la ventana de cambio obligatorio de clave
                } else if (Sesion.esAdmin() || Sesion.esAnalista()) {
                    new HistorialReservaciones().setVisible(true); // Los analistas y administradores entran al dashboard histórico
                } else {
                    new Reservaciones().setVisible(true); // Los recepcionistas entran directo a operar las reservas activas
                }
                this.dispose(); // Cierra de forma limpia el Login
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos."); // Deniega el acceso al sistema
            }
        });
    }
}