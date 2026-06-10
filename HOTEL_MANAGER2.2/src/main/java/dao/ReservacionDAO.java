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
import modelo.Reservacion;

public class ReservacionDAO {

    public boolean insertar(Reservacion r) {
        String sql = "INSERT INTO reservacion (id_cliente, id_habitacion, fecha_res, fecha_inicio, fecha_fin, periodo, precio_total, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getIdCliente());
            ps.setInt(2, r.getIdHabitacion());
            ps.setTimestamp(3, new Timestamp(r.getFechaRes().getTime()));
            ps.setTimestamp(4, new Timestamp(r.getFechaInicio().getTime()));
            ps.setTimestamp(5, new Timestamp(r.getFechaFin().getTime()));
            ps.setDouble(6, r.getPeriodo());
            ps.setDouble(7, r.getPrecioTotal());
            ps.setString(8, r.getEstado());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        r.setIdReservacion(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizar(Reservacion r) {
        String sql = "UPDATE reservacion SET id_cliente = ?, id_habitacion = ?, fecha_inicio = ?, fecha_fin = ?, periodo = ?, precio_total = ?, estado = ? WHERE id_reservacion = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, r.getIdCliente());
            ps.setInt(2, r.getIdHabitacion());
            ps.setTimestamp(3, new Timestamp(r.getFechaInicio().getTime()));
            ps.setTimestamp(4, new Timestamp(r.getFechaFin().getTime()));
            ps.setDouble(5, r.getPeriodo());
            ps.setDouble(6, r.getPrecioTotal());
            ps.setString(7, r.getEstado());
            ps.setInt(8, r.getIdReservacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Reservacion obtenerPorId(int id) {
        String sql = "SELECT * FROM reservacion WHERE id_reservacion = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Reservacion(
                        rs.getInt("id_reservacion"), rs.getInt("id_cliente"), rs.getInt("id_habitacion"),
                        rs.getTimestamp("fecha_res"), rs.getTimestamp("fecha_inicio"), 
                        rs.getTimestamp("fecha_fin"), rs.getDouble("periodo"), 
                        rs.getDouble("precio_total"), rs.getString("estado")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Reservacion> obtenerActivas() {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservacion WHERE estado = 'Activa'";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Reservacion(rs.getInt("id_reservacion"), rs.getInt("id_cliente"),
                        rs.getInt("id_habitacion"), rs.getTimestamp("fecha_res"),
                        rs.getTimestamp("fecha_inicio"), rs.getTimestamp("fecha_fin"),
                        rs.getDouble("periodo"), rs.getDouble("precio_total"), rs.getString("estado")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Reservacion> obtenerHistorial() {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservacion";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Reservacion(rs.getInt("id_reservacion"), rs.getInt("id_cliente"),
                        rs.getInt("id_habitacion"), rs.getTimestamp("fecha_res"),
                        rs.getTimestamp("fecha_inicio"), rs.getTimestamp("fecha_fin"),
                        rs.getDouble("periodo"), rs.getDouble("precio_total"), rs.getString("estado")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Reservacion> obtenerPorCliente(int idCliente) {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservacion WHERE id_cliente = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Reservacion(rs.getInt("id_reservacion"), rs.getInt("id_cliente"),
                            rs.getInt("id_habitacion"), rs.getTimestamp("fecha_res"),
                            rs.getTimestamp("fecha_inicio"), rs.getTimestamp("fecha_fin"),
                            rs.getDouble("periodo"), rs.getDouble("precio_total"), rs.getString("estado")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Reservacion> obtenerPorPeriodo(java.util.Date inicio, java.util.Date fin) {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservacion WHERE fecha_inicio >= ? AND fecha_fin <= ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(inicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fin.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Reservacion(rs.getInt("id_reservacion"), rs.getInt("id_cliente"),
                            rs.getInt("id_habitacion"), rs.getTimestamp("fecha_res"),
                            rs.getTimestamp("fecha_inicio"), rs.getTimestamp("fecha_fin"),
                            rs.getDouble("periodo"), rs.getDouble("precio_total"), rs.getString("estado")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean cancelar(int idReservacion) {
        String sql = "UPDATE reservacion SET estado = 'Cancelada' WHERE id_reservacion = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, idReservacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}