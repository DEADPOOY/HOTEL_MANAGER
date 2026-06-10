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

public class ConfirmarCancelar extends JDialog {
    private boolean confirmado = false;
    private JButton btnSi, btnNo;

    public ConfirmarCancelar(Window parent) {
        super(parent, "Cancelar Reservación", ModalityType.APPLICATION_MODAL);
        setSize(280, 120);
        setLocationRelativeTo(parent);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        btnSi = new JButton("Confirmar"); btnNo = new JButton("Salir");
        btnSi.setBackground(Color.decode("#dc3545")); btnSi.setForeground(Color.WHITE);

        add(new JLabel("¿Desea anular esta reservación activa?"));
        add(btnSi); add(btnNo);

        btnSi.addActionListener(e -> { confirmado = true; this.dispose(); });
        btnNo.addActionListener(e -> { confirmado = false; this.dispose(); });
    }

    public boolean isConfirmado() { return confirmado; }
}