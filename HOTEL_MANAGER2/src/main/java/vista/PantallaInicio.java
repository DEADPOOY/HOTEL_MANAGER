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

public class PantallaInicio extends JFrame { // Ventana inicial que divide el sistema en dos accesos
    private JButton btnPersonal, btnRecepcion;

    public PantallaInicio() {
        setTitle("Hotel Manager 2.0 - Inicio"); // Título de la ventana
        setSize(400, 250); // Tamaño en píxeles
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra por completo el proceso de Java al salir
        setLocationRelativeTo(null); // Centra la ventana automáticamente en la pantalla
        setLayout(new GridLayout(3, 1, 10, 10)); // Distribución de rejilla de 3 filas y 1 columna

        JLabel lblInfo = new JLabel("Seleccione el Panel de Acceso", JLabel.CENTER); // Etiqueta informativa centrada
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fuente estilizada
        lblInfo.setForeground(Color.decode("#1a3a5c")); // Color institucional azul obscuro

        btnPersonal = new JButton("Área de Operaciones (Login General)"); // Botón de entrada general
        btnPersonal.setBackground(Color.decode("#1a3a5c")); // Fondo azul obscuro
        btnPersonal.setForeground(Color.WHITE); // Texto blanco

        btnRecepcion = new JButton("Control de Personal (Solo Admin)"); // Botón exclusivo de administración
        btnRecepcion.setBackground(Color.decode("#fd7e14")); // Fondo naranja brillante
        btnRecepcion.setForeground(Color.WHITE); // Texto blanco

        add(lblInfo); // Añade los componentes en orden descendente
        add(btnPersonal);
        add(btnRecepcion);

        btnPersonal.addActionListener(e -> { // Evento de clic para el acceso general
            new InicioSesion().setVisible(true); // Abre la ventana de login general
            this.dispose(); // Destruye la ventana de inicio para liberar espacio en memoria
        });

        btnRecepcion.addActionListener(e -> { // Evento de clic para el panel de administración
            new AdminLogin().setVisible(true); // Abre el login de verificación de firma del administrador
            this.dispose(); // Cierra el menú inicial
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PantallaInicio().setVisible(true)); // Hilo de ejecución de la interfaz gráfica
    }
}