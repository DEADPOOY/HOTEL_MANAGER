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
import java.awt.*;
import javax.swing.*;
import modelo.Cliente;

public class ModificarCliente extends JDialog {
    private JTextField txtNom, txtTel;
    private JButton btnGuardar, btnCancelar;
    private ClienteDAO clienteDAO;
    private Cliente clienteActual;
    private Clientes padre;

    public ModificarCliente(Clientes parent, Cliente c) {
        super(parent, "Actualizar Datos Cliente", true);
        this.padre = parent;
        this.clienteActual = c;
        clienteDAO = new ClienteDAO();
        setSize(300, 180);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("  Nombre:")); txtNom = new JTextField(c.getNomCliente()); add(txtNom);
        add(new JLabel("  Teléfono:")); txtTel = new JTextField(c.getNumCliente()); add(txtTel);

        btnGuardar = new JButton("Actualizar");
        btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(Color.decode("#fd7e14"));
        btnGuardar.setForeground(Color.WHITE);

        add(btnGuardar); add(btnCancelar);

        btnCancelar.addActionListener(e -> this.dispose());
        btnGuardar.addActionListener(e -> {
            clienteActual.setNomCliente(txtNom.getText());
            clienteActual.setNumCliente(txtTel.getText());

            if (Validador.textoVacio(clienteActual.getNomCliente()) || !Validador.formatoTelefono(clienteActual.getNumCliente())) {
                JOptionPane.showMessageDialog(this, "Verifique los datos.");
                return;
            }

            if (clienteDAO.actualizar(clienteActual)) {
                padre.llenarTabla();
                this.dispose();
            }
        });
    }
}
