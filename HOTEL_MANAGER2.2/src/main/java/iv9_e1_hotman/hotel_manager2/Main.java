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
import javax.swing.UIManager;
import vista.PantallaInicio;

public class Main {
    public static void main(String[] args) {
        // Aplica el diseño visual nativo del sistema operativo para que las ventanas se vean modernas
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo establecer la interfaz nativa, se usará el diseño por defecto.");
        }

        // Lanza la aplicación desde la Pantalla de Inicio principal de forma segura en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new PantallaInicio().setVisible(true);
        });
    }
}