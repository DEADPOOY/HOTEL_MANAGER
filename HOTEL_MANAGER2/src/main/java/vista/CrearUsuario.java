/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.UsuarioDAO;
import java.awt.*;
import javax.swing.*;
import modelo.Usuario;

public class CrearUsuario extends JDialog {
    private JLabel lblNom, lblPass, lblRol;
    private JTextField txtNom;
    private JPasswordField txtPass;
    private JRadioButton rbRec, rbAna;
    private ButtonGroup grupoRol;
    private JButton btnCrear, btnCancelar;
    private UsuarioDAO usuarioDAO;
    private ControlUsuarios padre;

    public CrearUsuario(ControlUsuarios parent) {
        super(parent, "Nuevo Usuario", true);
        this.padre = parent;
        usuarioDAO = new UsuarioDAO();
        setSize(300, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(5, 2, 10, 10));

        lblNom = new JLabel("  Nombre:");
        lblPass = new JLabel("  Contraseña:");
        lblRol = new JLabel("  Rol:");

        txtNom = new JTextField();
        txtPass = new JPasswordField();

        rbRec = new JRadioButton("Recepcionista", true);
        rbAna = new JRadioButton("Analista");
        grupoRol = new ButtonGroup();
        grupoRol.add(rbRec); grupoRol.add(rbAna);

        btnCrear = new JButton("Crear");
        btnCancelar = new JButton("Cancelar");

        btnCrear.setBackground(Color.decode("#28a745"));
        btnCrear.setForeground(Color.WHITE);
        btnCancelar.setBackground(Color.decode("#6c757d"));
        btnCancelar.setForeground(Color.WHITE);

        add(lblNom); add(txtNom);
        add(lblPass); add(txtPass);
        add(lblRol); JPanel p = new JPanel(new GridLayout(2,1)); p.add(rbRec); p.add(rbAna); add(p);
        add(btnCrear); add(btnCancelar);

        btnCancelar.addActionListener(e -> this.dispose());

        btnCrear.addActionListener(e -> {
            String nombre = txtNom.getText();
            String pass = new String(txtPass.getPassword());
            String rol = rbRec.isSelected() ? "Recepcionista" : "Analista";

            if (Validador.textoVacio(nombre) || Validador.textoVacio(pass)) {
                JOptionPane.showMessageDialog(this, "Campos vacíos.");
                return;
            }

            Usuario nuevo = new Usuario(0, nombre, pass, rol, 1);
            if (usuarioDAO.insertar(nuevo)) {
                padre.llenarTablas();
                this.dispose();
            }
        });
    }
}