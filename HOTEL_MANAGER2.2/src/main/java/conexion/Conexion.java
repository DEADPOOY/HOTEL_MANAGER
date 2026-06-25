/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

/**
 *
 * @author deadpooy
 */

import java.sql.Connection; // Clase para manejar la conexión activa
import java.sql.DriverManager; // Clase para gestionar los drivers de conexiones
import java.sql.SQLException; // Manejador de excepciones de base de datos

public class Conexion {
    
    private static Connection instancia = null; // Variable estática que almacena la conexión bajo el patrón Singleton
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_manager"; // Ruta del servidor MySQL con la base de datos
    private static final String USUARIO = "root"; // Usuario por defecto del gestor de base de datos
    private static final String PASS = "091119"; // Contraseña por defecto (vacía en entornos locales XAMPP/WAMP)

    private Conexion() {} // Constructor privado para evitar que creen instancias de esta clase en otros lados

    public static synchronized Connection getInstancia() { // Método global para pedir la conexión del sistema de forma segura
        try {
            if (instancia == null || instancia.isClosed()) { // Verifica si la conexión no ha sido creada o está cerrada
                Class.forName("com.mysql.cj.jdbc.Driver"); // Carga dinámicamente el controlador JDBC de MySQL Connector/J
                instancia = DriverManager.getConnection(URL, USUARIO, PASS); // Intenta establecer el puente con las credenciales
                System.out.println("Conexión a MySQL exitosa"); // Imprime confirmación interna en la terminal
            }
        } catch (SQLException | ClassNotFoundException e) { // Captura fallas de driver o fallas de credenciales de red
            e.printStackTrace(); // Imprime el árbol del error detallado
            System.out.println("Error al conectar con la base de datos"); // Alerta de fallo en consola
        }
        return instancia; // Retorna la conexión activa lista para usarse
    }
}