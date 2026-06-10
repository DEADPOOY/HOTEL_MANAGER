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
        btnGuardar.addActionListener(e -> { // Valida y procesa el registro manual del cliente
            String nom = txtNom.getText().trim();
            String tel = txtTel.getText().trim();

            if (Validador.textoVacio(nom) || !Validador.formatoTelefono(tel)) { // Valida campos vacíos y que el teléfono tenga un formato válido mediante el validador
                JOptionPane.showMessageDialog(this, "Datos inválidos o teléfono fuera del formato requerido (10 a 20 dígitos).");
                return;
            }

            if (clienteDAO.insertar(new Cliente(0, nom, tel))) { // Guarda al nuevo cliente en la base de datos
                padre.llenarTabla(); // Refresca de forma instantánea la tabla de clientes trasera del formulario padre
                this.dispose(); // Cierra el cuadro flotante actual
            }
        });
    }
}