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
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class CrearUsuario extends JDialog {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cbRol;
    private UsuarioDAO usuarioDAO;

    public CrearUsuario(Frame padre) {
        super(padre, "Registrar Colaborador ", true);
        usuarioDAO = new UsuarioDAO();
        setSize(380, 360);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(6, 1, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        p.add(new JLabel("Nombre de Usuario (Login):"));
        txtUser = new JTextField();
        p.add(txtUser);

        p.add(new JLabel("Contraseña Temporal:"));
        txtPass = new JPasswordField();
        p.add(txtPass);

        p.add(new JLabel("Rol del Sistema:"));
        cbRol = new JComboBox<>(new String[]{"Administrador", "Recepcionista", "Analista"});
        p.add(cbRol);

        add(p, BorderLayout.CENTER);

        JButton btnCrear = new JButton("Dar de Alta");
        btnCrear.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String c = new String(txtPass.getPassword()).trim();
            String r = (String) cbRol.getSelectedItem();

            if(!u.isEmpty() && !c.isEmpty()) {
                Usuario nuevo = new Usuario(0, u, c, r, 1); // 1 indica primer_login activado para forzar cambio
                if(usuarioDAO.insertar(nuevo)) {
                    JOptionPane.showMessageDialog(this, "Usuario registrado de manera exitosa.");
                    dispose();
                }
            }
        });
        add(btnCrear, BorderLayout.SOUTH);
    }
}
