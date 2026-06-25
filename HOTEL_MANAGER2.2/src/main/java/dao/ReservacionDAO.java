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
import modelo.Reservacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservacionDAO {

    public List<Reservacion> obtenerTodos() {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservacion";
        try (Connection con = Conexion.getInstancia();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Reservacion(
                    rs.getInt(1), // Columna 1: ID (Evita el error de 'idReservacion' not found)
                    rs.getInt(2), // Columna 2: idCliente
                    rs.getInt(3), // Columna 3: idHabitacion
                    rs.getTimestamp(4), // Columna 4: fechaRegistro
                    rs.getTimestamp(5), // Columna 5: fechaInicio
                    rs.getTimestamp(6), // Columna 6: fechaFin
                    rs.getDouble(7),    // Columna 7: periodo
                    rs.getDouble(8),    // Columna 8: precioTotal
                    rs.getString(9)     // Columna 9: estado
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerTodos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public List<Reservacion> obtenerPorEstado(String estado) {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservacion WHERE estado = ?";
        try (Connection con = Conexion.getInstancia();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Reservacion(
                        rs.getInt(1), // Columna 1: ID
                        rs.getInt(2), // Columna 2: idCliente
                        rs.getInt(3), // Columna 3: idHabitacion
                        rs.getTimestamp(4),
                        rs.getTimestamp(5),
                        rs.getTimestamp(6),
                        rs.getDouble(7),
                        rs.getDouble(8),
                        rs.getString(9)
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerPorEstado: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean insertar(Reservacion r) {
        String sql = "INSERT INTO reservacion (idCliente, idHabitacion, fechaInicio, fechaFin, periodo, precioTotal, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getIdCliente());
            ps.setInt(2, r.getIdHabitacion());
            ps.setTimestamp(3, new java.sql.Timestamp(r.getFechaInicio().getTime()));
            ps.setTimestamp(4, new java.sql.Timestamp(r.getFechaFin().getTime()));
            ps.setDouble(5, r.getPeriodo());
            ps.setDouble(6, r.getPrecioTotal());
            ps.setString(7, r.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelar(int id) {
        // Si tu columna ID en la base de datos no se llama idReservacion, cambia "idReservacion = ?" por el nombre real abajo, p.ej. "id = ?"
        String sql = "UPDATE reservacion SET estado = 'Cancelada' WHERE idReservacion = ?"; 
        try (Connection con = Conexion.getInstancia();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}