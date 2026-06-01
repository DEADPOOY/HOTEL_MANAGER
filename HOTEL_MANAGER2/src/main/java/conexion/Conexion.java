/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

/**
 *
 * @author deadpooy
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    private static Connection instancia = null;
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_manager";
    private static final String USUARIO = "root";
    private static final String PASS = "";

    private Conexion() {}

    public static synchronized Connection getInstancia() {
        try {
            if (instancia == null || instancia.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instancia = DriverManager.getConnection(URL, USUARIO, PASS);
                System.out.println("Conexión a MySQL exitosa");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error al conectar con la base de datos");
        }
        return instancia;
    }
}