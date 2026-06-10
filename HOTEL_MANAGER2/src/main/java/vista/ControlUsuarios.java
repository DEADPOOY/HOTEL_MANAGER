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
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel; // Clase para estructurar los datos lógicos de las tablas gráficos
import modelo.Usuario;

// Pantalla de administración de personal que hereda de JFrame y unifica la estética del sistema
public class ControlUsuarios extends JFrame {
    
    private JTable tablaRecepcionistas, tablaAnalistas; // Tablas visuales separadas por rol para claridad
    private DefaultTableModel modeloRec, modeloAna; // Modelos de datos para manipular filas de las tablas
    private JButton btnNuevo, btnEliminar, btnHabitaciones, btnRegresar; // Botones operativos
    private JLabel lblTitulo, lblSubtitulo; // Títulos del encabezado institucional
    private UsuarioDAO usuarioDAO;

    public ControlUsuarios() {
        // Inicialización de la capa de acceso a datos para MySQL
        usuarioDAO = new UsuarioDAO();
        
        // Configuración de la ventana con las dimensiones gigantes estándar de la aplicación
        setTitle("Control de Usuarios - Administrador");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Libera memoria al cerrar esta ventana secundaria
        setLocationRelativeTo(null); // Centra la ventana en el monitor del usuario
        
        // Panel contenedor raíz configurado con BorderLayout y márgenes acolchados
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));
        panelPrincipal.setBackground(new Color(245, 247, 250)); // Gris claro moderno uniforme
        setContentPane(panelPrincipal);

        // Subpanel superior (Norte) estructurado de forma vertical para los títulos
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));
        panelNorte.setBackground(new Color(245, 247, 250));

        // Título principal con tipografía grande y color azul institucional
        lblTitulo = new JLabel("Panel de Control de Personal");
        lblTitulo.setFont(new Font("Helvetica", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(26, 58, 92));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo descriptivo que sirve como guía visual para el administrador
        lblSubtitulo = new JLabel("Consola General - Administración y Separación de Roles en el Sistema");
        lblSubtitulo.setFont(new Font("Helvetica", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(108, 117, 125));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Inserción secuencial de los componentes de texto con un espaciador intermedio
        panelNorte.add(lblTitulo);
        panelNorte.add(Box.createRigidArea(new Dimension(0, 10)));
        panelNorte.add(lblSubtitulo);
        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // Inicialización y configuración visual de la JTable de Recepcionistas
        modeloRec = new DefaultTableModel(new Object[]{"ID Recepcionista", "Nombre Completo"}, 0);
        tablaRecepcionistas = new JTable(modeloRec);
        tablaRecepcionistas.setFont(new Font("Helvetica", Font.PLAIN, 14));
        tablaRecepcionistas.setRowHeight(30); // Altura de fila incrementada para mayor legibilidad
        tablaRecepcionistas.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        
        // Inicialización y configuración visual de la JTable de Analistas
        modeloAna = new DefaultTableModel(new Object[]{"ID Analista", "Nombre Completo"}, 0);
        tablaAnalistas = new JTable(modeloAna);
        tablaAnalistas.setFont(new Font("Helvetica", Font.PLAIN, 14));
        tablaAnalistas.setRowHeight(30);
        tablaAnalistas.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));

        // Subpanel central con un GridLayout para dividir la pantalla simétricamente en 2 columnas
        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 20, 0)); 
        panelTablas.setBackground(new Color(245, 247, 250));
        
        // Se envuelven las JTables en JScrollPanes y se agregan al contenedor de la cuadrícula
        panelTablas.add(new JScrollPane(tablaRecepcionistas)); 
        panelTablas.add(new JScrollPane(tablaAnalistas));
        panelPrincipal.add(panelTablas, BorderLayout.CENTER);

        // Subpanel inferior (Sur) para la distribución horizontal de botones de control
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15)); 
        panelBotones.setBackground(new Color(245, 247, 250));

        // Dimensiones idénticas a los botones operativos de la pantalla de reservaciones
        Dimension dimBoton = new Dimension(165, 60);
        Font fuenteBoton = new Font("Helvetica", Font.BOLD, 13);

        // Configuración estética del botón de altas de personal (Color verde)
        btnNuevo = new JButton("Nuevo Usuario");
        btnNuevo.setFont(fuenteBoton);
        btnNuevo.setPreferredSize(dimBoton);
        btnNuevo.setMinimumSize(dimBoton); // Fuerza la dimensión en layouts dinámicos
        btnNuevo.setBackground(Color.decode("#28a745"));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFocusPainted(false);

        // Configuración estética del botón de bajas de personal (Color rojo)
        btnEliminar = new JButton("Eliminar Usuario");
        btnEliminar.setFont(fuenteBoton);
        btnEliminar.setPreferredSize(dimBoton);
        btnEliminar.setBackground(Color.decode("#dc3545"));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        
        // Configuración estética del botón de administración de cuartos restringido (Color azul oscuro)
        btnHabitaciones = new JButton("Gestionar Habitaciones"); 
        btnHabitaciones.setFont(fuenteBoton);
        btnHabitaciones.setPreferredSize(dimBoton);
        btnHabitaciones.setBackground(Color.decode("#1a3a5c")); 
        btnHabitaciones.setForeground(Color.WHITE);
        btnHabitaciones.setFocusPainted(false);

        // Configuración estética del botón de retorno (Color gris neutral)
        btnRegresar = new JButton("Regresar al Menú");
        btnRegresar.setFont(fuenteBoton);
        btnRegresar.setPreferredSize(dimBoton);
        btnRegresar.setBackground(Color.decode("#6c757d"));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        // Inserción secuencial en el contenedor de botones inferior
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnHabitaciones); 
        panelBotones.add(btnRegresar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Carga y mapeo inicial automático de la información desde MySQL hacia la interfaz
        llenarTablas();

        // CONTROL DE ACCIONES DE LOS BOTONES (LISTENERS)

        // Abre el cuadro modal flotante secundario para registrar un nuevo empleado
        btnNuevo.addActionListener(e -> new CrearUsuario(this).setVisible(true)); 
        
        // Abre el monitor general de habitaciones desde la perspectiva del Administrador
        btnHabitaciones.addActionListener(e -> new Habitaciones().setVisible(true)); 
        
        // Destruye la ventana actual y redirige el flujo del programa a la consola principal
        btnRegresar.addActionListener(e -> {
            new AdminLogin().setVisible(true); // Abre la pantalla de control del administrador modificada
            dispose(); // Destruye esta ventana de control de usuarios
        });
        
        // Lanza la subrutina para remover el registro de personal seleccionado
        btnEliminar.addActionListener(e -> eliminarUsuario()); 
    }

    // Consulta la base de datos y dibuja las tuplas de usuarios dividiéndolas en las tablas según su rol
    public void llenarTablas() { 
        modeloRec.setRowCount(0); // Vacía filas previas para prevenir duplicados visuales en pantalla
        modeloAna.setRowCount(0);
        
        List<Usuario> lista = usuarioDAO.obtenerTodos(); // Invoca el listado general desde el DAO
        
        for (Usuario u : lista) { // Itera el mapa de objetos recuperado
            if ("Recepcionista".equals(u.getRol())) {
                modeloRec.addRow(new Object[]{u.getIdUsuario(), u.getNombre()}); // Inserta en la JTable de recepción
            } else if ("Analista".equals(u.getRol())) {
                modeloAna.addRow(new Object[]{u.getIdUsuario(), u.getNombre()}); // Inserta en la JTable de análisis
            }
        }
    }

    // Rutina encargada de procesar las validaciones y confirmar la baja lógica o física en MySQL
    private void eliminarUsuario() { 
        int filaRec = tablaRecepcionistas.getSelectedRow(); // Retorna el índice de la celda de recepción (-1 si no hay selección)
        int filaAna = tablaAnalistas.getSelectedRow(); // Retorna el índice de la celda de análisis
        int idEliminar = -1; // Bandera de control inicializada en error

        // Prioriza y evalúa cuál de las dos tablas mantiene un elemento enfocado por el cursor
        if (filaRec != -1) {
            idEliminar = (int) modeloRec.getValueAt(filaRec, 0); // Extrae la llave primaria (ID) de la columna cero
        } else if (filaAna != -1) {
            idEliminar = (int) modeloAna.getValueAt(filaAna, 0); // Extrae la llave primaria (ID) del analista
        }

        // Validación de seguridad en caso de presionar el botón sin seleccionar ninguna fila
        if (idEliminar == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de cualquier tabla para eliminar.");
            return;
        }

        // Despliega el cuadro de diálogo modal absoluto para confirmar la operación destructiva
        ConfirmarEliminar dialogo = new ConfirmarEliminar(this); 
        dialogo.setVisible(true);

        // Valida la confirmación del Administrador
        if (dialogo.isConfirmado()) { 
            if (usuarioDAO.eliminar(idEliminar)) { // Borra el registro en la persistencia de datos mediante el DAO
                llenarTablas(); // Actualiza y redibuja de inmediato las tablas de la ventana actual
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente del sistema.");
            }
        }
    }
}