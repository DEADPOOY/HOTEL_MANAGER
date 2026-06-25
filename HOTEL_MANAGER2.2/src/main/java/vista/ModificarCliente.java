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

public class ModificarCliente extends JDialog {
    private JTextField txtNombre, txtDoc;
    private ClienteDAO clienteDAO;
    private Cliente clienteActual;

    public ModificarCliente(Frame padre, Cliente c) {
        super(padre, "Modificar Huésped", true);
        this.clienteActual = c;
        this.clienteDAO = new ClienteDAO();
        setSize(400, 300);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 1, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField(clienteActual.getNomCliente());
        form.add(txtNombre);
        form.add(new JLabel("Documento / Celular:"));
        txtDoc = new JTextField(clienteActual.getNumCliente());
        form.add(txtDoc);
        add(form, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> {
            clienteActual.setNomCliente(txtNombre.getText().trim());
            clienteActual.setNumCliente(txtDoc.getText().trim());
            if(clienteDAO.actualizar(clienteActual)) {
                JOptionPane.showMessageDialog(this, "Actualizado con éxito.");
                dispose();
            }
        });
        add(btnActualizar, BorderLayout.SOUTH);
    }
}