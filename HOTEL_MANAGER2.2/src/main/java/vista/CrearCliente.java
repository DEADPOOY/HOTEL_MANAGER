/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.ClienteDAO;
import modelo.Cliente;
import javax.swing.*;
import java.awt.*;

public class CrearCliente extends JDialog {
    private JTextField txtNombre, txtDoc;
    private ClienteDAO clienteDAO;

    public CrearCliente(Frame padre) {
        super(padre, "Registrar Huésped", true);
        clienteDAO = new ClienteDAO();
        setSize(400, 300);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 1, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        form.add(txtNombre);
        form.add(new JLabel("Documento / Celular:"));
        txtDoc = new JTextField();
        form.add(txtDoc);
        add(form, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            if(txtNombre.getText().trim().isEmpty() || txtDoc.getText().trim().isEmpty()) return;
            Cliente c = new Cliente(0, txtNombre.getText().trim(), txtDoc.getText().trim());
            if(clienteDAO.insertar(c)) {
                JOptionPane.showMessageDialog(this, "Registrado.");
                dispose();
            }
        });
        add(btnGuardar, BorderLayout.SOUTH);
    }
}
