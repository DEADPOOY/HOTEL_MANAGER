/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iv9_e1_hotman.hotel_manager2;

/**
 *
 * @author deadpooy
 */
import javax.swing.SwingUtilities;
import javax.swing.UIManager; // Manejador de estilos visuales de componentes e interfaces gráficas en Java
import vista.PantallaInicio;

public class Main {
    public static void main(String[] args) {
        // Aplica de forma automática el estilo visual y diseño nativo del sistema operativo (Windows, Mac, Linux) para modernizar las ventanas
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo establecer el Look and Feel nativo del sistema operativo, se usarán los estilos por defecto de Java Swing.");
        }

        // Lanza de forma segura la ejecución de la interfaz gráfica abriendo la Pantalla de Inicio principal del hotel
        SwingUtilities.invokeLater(() -> {
            new PantallaInicio().setVisible(true);
        });
    }
}