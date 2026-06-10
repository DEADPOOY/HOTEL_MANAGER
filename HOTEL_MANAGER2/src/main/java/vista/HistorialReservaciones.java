/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import dao.ReservacionDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel; // Clase para estructurar los datos lógicos de las tablas gráficos
import modelo.Reservacion;

// Ventana de auditoría cronológica utilizada por el Analista Comercial y Administrador
public class HistorialReservaciones extends JFrame {
    
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private JButton btnExportar, btnCerrar; // Botones operativos del analista
    private JLabel lblTitulo, lblSubtitulo; // Componentes de texto del encabezado uniforme
    private ReservacionDAO reservacionDAO;

    public HistorialReservaciones() {
        // Inicialización de la capa de acceso a datos para MySQL
        reservacionDAO = new ReservacionDAO();
        
        // Clonación exacta de las dimensiones gigantes y el comportamiento de la pantalla de reservaciones
        setTitle("Historial Cronológico de Reservaciones (Auditoría)");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Libera memoria al cerrar sin tumbar el programa raíz
        setLocationRelativeTo(null); // Centra la ventana de forma automática en la pantalla

        // Panel contenedor raíz configurado con BorderLayout y márgenes acolchados idénticos
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));
        panelPrincipal.setBackground(new Color(245, 247, 250)); // Gris claro moderno corporativo
        setContentPane(panelPrincipal);

        // Subpanel superior (Norte) estructurado de forma vertical para los títulos del analista
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));
        panelNorte.setBackground(new Color(245, 247, 250));

        // Título principal con la tipografía Helvetica Bold y color azul oscuro del sistema
        lblTitulo = new JLabel("Historial General de Reservaciones");
        lblTitulo.setFont(new Font("Helvetica", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(26, 58, 92));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo indicador que delimita el propósito de auditoría comercial de la pantalla
        lblSubtitulo = new JLabel("Módulo de Auditoría Comercial - Consulta completa de registros históricos e ingresos");
        lblSubtitulo.setFont(new Font("Helvetica", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(108, 117, 125));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Inserción secuencial en el bloque del encabezado con su respectivo espaciador rígido
        panelNorte.add(lblTitulo);
        panelNorte.add(Box.createRigidArea(new Dimension(0, 10)));
        panelNorte.add(lblSubtitulo);
        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // Columnas completas para una auditoría a fondo del sistema de reservaciones
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Cliente ID", "Habitación ID", "Fecha Registro", "Inicio", "Fin", "Horas", "Costo", "Estado"}, 0);
        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setFont(new Font("Helvetica", Font.PLAIN, 14));
        tablaHistorial.setRowHeight(30); // Filas más altas idénticas a la pantalla operativa
        tablaHistorial.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        
        // Contenedor de scroll con barras de desplazamiento para rodear la tabla central de datos
        JScrollPane scrollTabla = new JScrollPane(tablaHistorial);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);

        // Subpanel inferior (Sur) para agrupar horizontalmente los botones de acción del analista
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelAcciones.setBackground(new Color(245, 247, 250));

        // Dimensiones idénticas a los botones operativos del hotel para asegurar la simetría gráfica
        Dimension dimBoton = new Dimension(165, 60);
        Font fuenteBoton = new Font("Helvetica", Font.BOLD, 13);

        // Botón de exportación comercial a archivos planos CSV (Color verde de éxito)
        btnExportar = new JButton("Exportar Historial");
        btnExportar.setFont(fuenteBoton);
        btnExportar.setPreferredSize(dimBoton);
        btnExportar.setMinimumSize(dimBoton); // Bloquea la deformación en layouts dinámicos
        btnExportar.setBackground(Color.decode("#28a745"));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);

        // Botón para descartar y cerrar el monitor de auditoría (Color gris neutral)
        btnCerrar = new JButton("Cerrar Ventana");
        btnCerrar.setFont(fuenteBoton);
        btnCerrar.setPreferredSize(dimBoton);
        btnCerrar.setBackground(Color.decode("#6c757d"));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);

        // Control de permisos visuales por rol: Oculta la opción de exportar si ingresó un recepcionista
        if (Sesion.esRecepcionista()) {
            btnExportar.setVisible(false);
        }

        // Colocación de los botones en el panel inferior de acciones
        panelAcciones.add(btnExportar); 
        panelAcciones.add(btnCerrar);
        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH);

        // Descarga y mapeo inmediato de todo el libro de registros de MySQL al iniciar la pantalla
        llenarTabla();

        // ASIGNACIÓN DE EVENTOS (LISTENERS)

        // Destruye de forma limpia la ventana de historial sin detener el proceso del menú principal
        btnCerrar.addActionListener(e -> this.dispose());
        
        // Abre el módulo secundario modal para delimitar periodos y exportar el reporte comercial en CSV
        btnExportar.addActionListener(e -> new GuardarHistorial(this).setVisible(true)); 
    }

    // Limpia y rellena la tabla con el historial de reservaciones descargado de MySQL
    private void llenarTabla() { 
        modeloTabla.setRowCount(0); // Vacía filas viejas del contenedor para prevenir la duplicidad visual
        
        List<Reservacion> lista = reservacionDAO.obtenerHistorial(); // Intenta llamar un alias por compatibilidad
        if (lista == null || lista.isEmpty()) {
            lista = reservacionDAO.obtenerHistorial(); // Llama el método estándar del DAO en caso de fallo primario
        }
        
        // Recorre la lista de reservaciones históricas y añade los objetos al modelo de datos
        for (Reservacion r : lista) {
            modeloTabla.addRow(new Object[]{
                r.getIdReservacion(), 
                r.getIdCliente(), 
                r.getIdHabitacion(),
                r.getFechaRes(), 
                r.getFechaInicio(), 
                r.getFechaFin(), 
                r.getPeriodo(), 
                r.getPrecioTotal(), 
                r.getEstado() // Muestra el estatus de la reservación ("Activa", "Cancelada", "Concluida")
            });
        }
    }
}