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

public class CrearUsuario extends JDialog { // JDialog bloquea la ventana de atrás hasta cerrarse
    private JLabel lblNom, lblPass, lblRol;
    private JTextField txtNom;
    private JPasswordField txtPass;
    private JRadioButton rbRec, rbAna; // Botones circulares de opción única
    private ButtonGroup grupoRol; // Junta los radio buttons para evitar selección múltiple
    private JButton btnCrear, btnCancelar;
    private UsuarioDAO usuarioDAO;
    private ControlUsuarios padre; // Guarda referencia de la ventana padre para poder actualizar sus tablas al guardar

    public CrearUsuario(ControlUsuarios parent) {
        super(parent, "Nuevo Usuario", true); // Abre de forma modal sobre el Control de Usuarios
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

        rbRec = new JRadioButton("Recepcionista", true); // Seleccionado por defecto al arrancar
        rbAna = new JRadioButton("Analista");
        grupoRol = new ButtonGroup();
        grupoRol.add(rbRec); grupoRol.add(rbAna); // Agrupa los controles para condicionar selección única

        btnCrear = new JButton("Crear");
        btnCancelar = new JButton("Cancelar");

        btnCrear.setBackground(Color.decode("#28a745"));
        btnCrear.setForeground(Color.WHITE);
        btnCancelar.setBackground(Color.decode("#6c757d"));
        btnCancelar.setForeground(Color.WHITE);

        add(lblNom); add(txtNom);
        add(lblPass); add(txtPass);
        add(lblRol); JPanel p = new JPanel(new GridLayout(2,1)); p.add(rbRec); p.add(rbAna); add(p); // Añade controles radiales agrupados en subpanel
        add(btnCrear); add(btnCancelar);

        btnCancelar.addActionListener(e -> this.dispose()); // Cierra la ventana flotante actual

        btnCrear.addActionListener(e -> { // Procesa el guardado del nuevo empleado
            String nombre = txtNom.getText().trim();
            String pass = new String(txtPass.getPassword());
            String rol = rbRec.isSelected() ? "Recepcionista" : "Analista"; // Define el string del rol en base a la selección

            if (Validador.textoVacio(nombre) || Validador.textoVacio(pass)) {
                JOptionPane.showMessageDialog(this, "Campos vacíos.");
                return;
            }

            Usuario nuevo = new Usuario(0, nombre, pass, rol, 1); // Genera objeto con primer_login en 1 para obligar cambio de clave posterior
            if (usuarioDAO.insertar(nuevo)) { // Guarda en la BD
                padre.llenarTablas(); // Llama al método público del padre para refrescar las tablas visuales al instante
                this.dispose(); // Destruye de forma limpia el cuadro flotante
            }
        });
    }
}