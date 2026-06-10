/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.HabitacionDAO;
import dao.ReservacionDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import modelo.Reservacion;

// Clase principal del panel de recepción que administra las reservaciones en estado "Activa"
public class Reservaciones extends JFrame {
    
    private JTable tablaRes;
    private DefaultTableModel modeloTabla;
    // Se añade el botón 'btnClientes' junto a los componentes ya existentes en la vista principal
    private JButton btnCrear, btnModificar, btnCancelar, btnHabitaciones, btnHistorial, btnClientes;
    private JLabel lblTitulo, lblEstadoSistema; // Componentes visuales para indicar la actividad o estado interno
    private ReservacionDAO reservacionDAO;
    private HabitacionDAO habitacionDAO;

    public Reservaciones() {
        // Inicialización de los objetos de acceso a datos (Capa DAO)
        reservacionDAO = new ReservacionDAO();
        habitacionDAO = new HabitacionDAO();
        
        // Configuración de la ventana principal con dimensiones extendidas de alta visibilidad
        setTitle("Módulo de Reservaciones Activas - Sistema de Gestión");
        setSize(1000, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Finaliza el proceso completo de la aplicación al cerrar la ventana
        setLocationRelativeTo(null); // Centra la ventana en el monitor del usuario
        
        // Configuración del panel contenedor principal con márgenes internos de separación (Padding)
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));
        panelPrincipal.setBackground(new Color(245, 247, 250)); // Color de fondo gris claro moderno
        setContentPane(panelPrincipal);

        // Subpanel superior (Norte) estructurado de forma vertical para los títulos
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));
        panelNorte.setBackground(new Color(245, 247, 250));

        // Título principal del encabezado con tipografía grande y color institucional
        lblTitulo = new JLabel("Control de Reservaciones Activas");
        lblTitulo.setFont(new Font("Helvetica", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(26, 58, 92));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo indicador que cambia dinámicamente según el estado operacional de la pantalla
        lblEstadoSistema = new JLabel("Panel Operativo - Vista General");
        lblEstadoSistema.setFont(new Font("Helvetica", Font.PLAIN, 16));
        lblEstadoSistema.setForeground(new Color(108, 117, 125));
        lblEstadoSistema.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adición de componentes al bloque del encabezado con un espaciador rígido intermedio
        panelNorte.add(lblTitulo);
        panelNorte.add(Box.createRigidArea(new Dimension(0, 10)));
        panelNorte.add(lblEstadoSistema);
        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // Configuración de la estructura lógica y visual de la tabla de datos principal
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "ID Cliente", "ID Habitación", "Inicio", "Fin", "Precio Total"}, 0);
        tablaRes = new JTable(modeloTabla);
        tablaRes.setFont(new Font("Helvetica", Font.PLAIN, 14));
        tablaRes.setRowHeight(30); // Incremento de la altura de las filas para una lectura cómoda
        tablaRes.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        
        // Contenedor con barras de desplazamiento automáticas para envolver la tabla central
        JScrollPane scrollTabla = new JScrollPane(tablaRes);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);

        // Subpanel inferior (Sur) para agrupar los botones operativos horizontalmente
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelAcciones.setBackground(new Color(245, 247, 250));

        // Dimensiones y tipografías uniformes para garantizar botones grandes y legibles
        Dimension dimBoton = new Dimension(150, 60);
        Font fuenteBoton = new Font("Helvetica", Font.BOLD, 13);

        // Configuración y estilo del botón de altas de reservación
        btnCrear = new JButton("Crear Reservación");
        btnCrear.setFont(fuenteBoton);
        btnCrear.setPreferredSize(dimBoton);
        btnCrear.setMinimumSize(dimBoton); // Restricción para forzar su visibilidad en layouts dinámicos
        btnCrear.setBackground(Color.decode("#28a745")); // Color verde institucional
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);

        // Configuración y estilo del botón de modificaciones horarias
        btnModificar = new JButton("Modificar");
        btnModificar.setFont(fuenteBoton);
        btnModificar.setPreferredSize(dimBoton);
        btnModificar.setBackground(Color.decode("#fd7e14")); // Color naranja
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFocusPainted(false);

        // Configuración y estilo del botón de bajas/cancelaciones
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(fuenteBoton);
        btnCancelar.setPreferredSize(dimBoton);
        btnCancelar.setBackground(Color.decode("#dc3545")); // Color rojo
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        // Configuración y estilo del botón de acceso al catálogo de habitaciones
        btnHabitaciones = new JButton("Ver Habitaciones");
        btnHabitaciones.setFont(fuenteBoton);
        btnHabitaciones.setPreferredSize(dimBoton);
        btnHabitaciones.setBackground(Color.decode("#1a3a5c")); // Color azul oscuro
        btnHabitaciones.setForeground(Color.WHITE);
        btnHabitaciones.setFocusPainted(false);

        // Configuración estética del nuevo botón que abre el catálogo de Clientes
        btnClientes = new JButton("Ver Clientes");
        btnClientes.setFont(fuenteBoton);
        btnClientes.setPreferredSize(dimBoton);
        btnClientes.setBackground(Color.decode("#17a2b8")); // Color azul turquesa distintivo
        btnClientes.setForeground(Color.WHITE);
        btnClientes.setFocusPainted(false);

        // Configuración y estilo del botón de auditoría general
        btnHistorial = new JButton("Historial General");
        btnHistorial.setFont(fuenteBoton);
        btnHistorial.setPreferredSize(dimBoton);
        btnHistorial.setBackground(Color.decode("#6c757d")); // Color gris neutral
        btnHistorial.setForeground(Color.WHITE);
        btnHistorial.setFocusPainted(false);
        // Filtro de seguridad por rol: visible únicamente para administradores o analistas
        btnHistorial.setVisible(Sesion.esAdmin() || Sesion.esAnalista());

        // Inserción ordenada de los botones dentro del panel de acciones inferior
        panelAcciones.add(btnCrear); 
        panelAcciones.add(btnModificar); 
        panelAcciones.add(btnCancelar);
        panelAcciones.add(btnHabitaciones); 
        panelAcciones.add(btnClientes); // Inserción física del botón de clientes en la interfaz gráfica
        panelAcciones.add(btnHistorial);
        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH);

        // Carga inicial automática de registros activos desde la base de datos
        llenarTabla();

        // CONTROL DE ASIGNACIÓN DE EVENTOS (LISTENERS)
        
        // Abre el monitor de habitaciones en una ventana flotante secundaria
        btnHabitaciones.addActionListener(e -> new Habitaciones().setVisible(true));
        
        // Evento que abre la ventana 'Clientes.java' con sus respectivas opciones de creación y edición
        btnClientes.addActionListener(e -> new Clientes().setVisible(true));
        
        // Abre el formulario modal para registrar una nueva renta
        btnCrear.addActionListener(e -> {
            new CrearReservacion(this).setVisible(true);
            actualizarEstadoVisual(true); // Altera el estado superior a modo transacción en proceso
        });
        
        // Procesa la edición del registro seleccionado actualmente en la tabla
        btnModificar.addActionListener(e -> {
            int fila = tablaRes.getSelectedRow();
            if (fila == -1) { // Validación de selección vacía
                JOptionPane.showMessageDialog(this, "Seleccione una reservación de la tabla para modificar.");
                return;
            }
            // Recupera el identificador de la columna 0 de la fila seleccionada
            int idRes = (int) modeloTabla.getValueAt(fila, 0);
            Reservacion r = reservacionDAO.obtenerPorId(idRes);
            if (r != null) {
                ModificarReservacion dialogo = new ModificarReservacion(this);
                dialogo.cargarDatos(r); // Transfiere el objeto cargado al formulario de edición
                dialogo.setVisible(true);
                actualizarEstadoVisual(true); // Cambia el estado visual a modo transacción
            }
        });
        
        // Redirecciona al panel de visualización cronológica completa del hotel
        btnHistorial.addActionListener(e -> {
            new HistorialReservaciones().setVisible(true);
        });

        // Ejecuta la baja lógica de la reservación seleccionada y libera el cuarto físico
        btnCancelar.addActionListener(e -> {
            int fila = tablaRes.getSelectedRow();
            if (fila == -1) { // Validación de seguridad de selección
                JOptionPane.showMessageDialog(this, "Seleccione una reservación activa de la tabla.");
                return;
            }
            int idRes = (int) modeloTabla.getValueAt(fila, 0);
            int idHab = (int) modeloTabla.getValueAt(fila, 2); // Extrae el ID de la habitación vinculada

            // Lanza el cuadro de confirmación modal absoluto
            ConfirmarCancelar dialog = new ConfirmarCancelar(this);
            dialog.setVisible(true);
            if (dialog.isConfirmado()) { // Evalúa si la respuesta del usuario fue afirmativa
                if (reservacionDAO.cancelar(idRes)) { // Actualiza el estado en la base de datos a 'Cancelada'
                    habitacionDAO.cambiarEstado(idHab, "Libre"); // Cambia el estado de la habitación a 'Libre' en MySQL
                    llenarTabla(); // Limpia y refresca el listado activo en pantalla
                    actualizarEstadoVisual(false); // Retorna el indicador superior al estado general por defecto
                    JOptionPane.showMessageDialog(this, "Reservación cancelada y habitación liberada correctamente.");
                }
            }
        });
    }

    // Consulta la base de datos y dibuja las reservaciones con estatus 'Activa' en las celdas
    public void llenarTabla() {
        modeloTabla.setRowCount(0); // Vacía el contenedor visual interno para evitar duplicidad de líneas
        List<Reservacion> lista = reservacionDAO.obtenerActivas();
        for (Reservacion r : lista) {
            modeloTabla.addRow(new Object[]{r.getIdReservacion(), r.getIdCliente(), r.getIdHabitacion(), r.getFechaInicio(), r.getFechaFin(), r.getPrecioTotal()});
        }
    }

    // Altera exclusivamente la cadena informativa de estado sin reconstruir ni romper la interfaz gráfica
    public void actualizarEstadoVisual(boolean ocupadoEnOperacion) {
        if (ocupadoEnOperacion) {
            lblEstadoSistema.setText("Estado: Transacción en Proceso - Esperando Actualización de Base de Datos");
            lblEstadoSistema.setForeground(Color.decode("#fd7e14")); // Tono naranja preventivo
        } else {
            lblEstadoSistema.setText("Panel Operativo - Vista General");
            lblEstadoSistema.setForeground(new Color(108, 117, 125)); // Tono gris estándar
        }
        
        // Fuerza la visibilidad del botón crítico y solicita redibujo del lienzo gráfico para prevenir pérdidas de enfoque
        btnCrear.setVisible(true);
        this.revalidate(); // Reajusta el árbol de componentes internos del layout
        this.repaint(); // Redibuja los píxeles visuales en la pantalla de la computadora
    }
}