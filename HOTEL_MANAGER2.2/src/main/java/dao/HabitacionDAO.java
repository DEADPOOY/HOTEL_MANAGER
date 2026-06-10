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
import java.util.ArrayList;
import java.util.List;
import modelo.Habitacion;

public class HabitacionDAO {

    public boolean insertar(Habitacion h) {
        String sql = "INSERT INTO habitacion (num_habitacion, tipo, piso, precio_hora, num_capacidad, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, h.getNumHabitacion());
            ps.setString(2, h.getTipo());
            ps.setInt(3, h.getPiso());
            ps.setDouble(4, h.getPrecioHora());
            ps.setInt(5, h.getNumCapacidad());
            ps.setString(6, h.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Habitacion h) {
        String sql = "UPDATE habitacion SET tipo = ?, piso = ?, precio_hora = ?, num_capacidad = ?, estado = ? WHERE id_habitacion = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, h.getTipo());
            ps.setInt(2, h.getPiso());
            ps.setDouble(3, h.getPrecioHora());
            ps.setInt(4, h.getNumCapacidad());
            ps.setString(5, h.getEstado());
            ps.setInt(6, h.getIdHabitacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Habitacion obtenerPorId(int id) {
        String sql = "SELECT * FROM habitacion WHERE id_habitacion = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Habitacion(rs.getInt("id_habitacion"), rs.getString("num_habitacion"),
                            rs.getString("tipo"), rs.getInt("piso"), rs.getDouble("precio_hora"),
                            rs.getInt("num_capacidad"), rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Habitacion> obtenerTodos() {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM habitacion";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Habitacion(rs.getInt("id_habitacion"), rs.getString("num_habitacion"),
                        rs.getString("tipo"), rs.getInt("piso"), rs.getDouble("precio_hora"),
                        rs.getInt("num_capacidad"), rs.getString("estado")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Habitacion> obtenerPorEstado(String estado) {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM habitacion WHERE estado = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Habitacion(rs.getInt("id_habitacion"), rs.getString("num_habitacion"),
                            rs.getString("tipo"), rs.getInt("piso"), rs.getDouble("precio_hora"),
                            rs.getInt("num_capacidad"), rs.getString("estado")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean cambiarEstado(int id, String estado) {
        String sql = "UPDATE habitacion SET estado = ? WHERE id_habitacion = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}