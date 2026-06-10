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
    private boolean confirmado = false;
    private JButton btnSi, btnNo;

    public ConfirmarEliminar(Window parent) {
        super(parent, "¿Eliminar Registro?", ModalityType.APPLICATION_MODAL);
        setSize(250, 120);
        setLocationRelativeTo(parent);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        btnSi = new JButton("Sí"); btnNo = new JButton("No");
        btnSi.setBackground(Color.decode("#dc3545")); btnSi.setForeground(Color.WHITE);

        add(new JLabel("¿Está seguro de eliminar este registro?"));
        add(btnSi); add(btnNo);

        btnSi.addActionListener(e -> { confirmado = true; this.dispose(); });
        btnNo.addActionListener(e -> { confirmado = false; this.dispose(); });
    }

    public boolean isConfirmado() { return confirmado; }
}