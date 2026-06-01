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

public class CrearCliente extends JDialog {
    private JTextField txtNom, txtTel;
    private JButton btnGuardar, btnCancelar;
    private ClienteDAO clienteDAO;
    private Clientes padre;

    public CrearCliente(Clientes parent) {
        super(parent, "Registrar Huésped", true);
        this.padre = parent;
        clienteDAO = new ClienteDAO();
        setSize(300, 180);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("  Nombre:")); txtNom = new JTextField(); add(txtNom);
        add(new JLabel("  Teléfono:")); txtTel = new JTextField(); add(txtTel);

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(Color.decode("#28a745"));
        btnGuardar.setForeground(Color.WHITE);

        add(btnGuardar); add(btnCancelar);

        btnCancelar.addActionListener(e -> this.dispose());
        btnGuardar.addActionListener(e -> {
            String nom = txtNom.getText();
            String tel = txtTel.getText();

            if (Validador.textoVacio(nom) || !Validador.formatoTelefono(tel)) {
                JOptionPane.showMessageDialog(this, "Datos inválidos o teléfono fuera de formato.");
                return;
            }

            if (clienteDAO.insertar(new Cliente(0, nom, tel))) {
                padre.llenarTabla();
                this.dispose();
            }
        });
    }
}