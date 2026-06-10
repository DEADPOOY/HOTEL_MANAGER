/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author deadpooy
 */

import conexion.Conexion;
import java.sql.*;
import modelo.Registro;

public class RegistroDAO {
    public boolean insertar(Registro r) { // Inserta una auditoría de entrada de huéspedes al hotel
        String sql = "INSERT INTO registro (id_cliente, id_reservacion, id_habitacion, hora_reg, fecha_reg) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, r.getIdCliente());
            ps.setInt(2, r.getIdReservacion());
            ps.setInt(3, r.getIdHabitacion());
            ps.setTime(4, r.getHoraReg()); // Guarda el objeto de tiempo de SQL
            ps.setDate(5, new java.sql.Date(r.getFechaReg().getTime())); // Convierte la fecha útil de Java al formato DATE de SQL
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}