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

public class ConfirmarEliminar extends JDialog {
    private boolean confirmado = false; // Bandera de control para indicar si el usuario confirmó la acción o no
    private JButton btnSi, btnNo;

    public ConfirmarEliminar(Window parent) {
        super(parent, "¿Eliminar Registro?", ModalityType.APPLICATION_MODAL); // Bloqueo absoluto de la ventana padre de fondo
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        btnSi = new JButton("Sí"); btnNo = new JButton("No");
        btnSi.setBackground(Color.decode("#dc3545")); btnSi.setForeground(Color.WHITE); // Botón rojo para indicar una acción peligrosa/destructiva

        add(new JLabel("¿Está seguro de eliminar este registro?"));
        add(btnSi); add(btnNo);

        btnSi.addActionListener(e -> { confirmado = true; this.dispose(); }); // Cambia la bandera a verdadero al confirmar y cierra la ventana flotante
        btnNo.addActionListener(e -> { confirmado = false; this.dispose(); }); // Mantiene la bandera en falso al rechazar la acción
    }

    public boolean isConfirmado() { // Método público para consultar la decisión tomada por el usuario desde el formulario padre que abrió esta confirmación
        return confirmado;
    }
}