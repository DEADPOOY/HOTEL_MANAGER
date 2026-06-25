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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservacionDAO {

    // Método para la pestaña de Reservaciones Activas (Recepcionista)
    public List<String[]> obtenerVistaTabla(String estadoFiltro) {
        verificarYExpirarReservaciones();
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT r.id_reservacion, c.nom_cliente, h.num_habitacion, h.tipo, " +
                     "r.fecha_inicio, r.fecha_fin, r.periodo, r.precio_total, r.estado " +
                     "FROM reservacion r " +
                     "INNER JOIN cliente c ON r.id_cliente = c.id_cliente " +
                     "INNER JOIN habitacion h ON r.id_habitacion = h.id_habitacion " +
                     "WHERE r.estado = ?";
        
        try (Connection con = Conexion.getInstancia();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estadoFiltro);
            try (ResultSet rs = ps.executeQuery()) {
                int contador = 1;
                while (rs.next()) {
                    String[] fila = new String[10];
                    fila[0] = String.valueOf(contador);
                    fila[1] = rs.getString("nom_cliente");
                    fila[2] = rs.getString("num_habitacion");
                    fila[3] = rs.getString("tipo");
                    fila[4] = String.valueOf(rs.getTimestamp("fecha_inicio"));
                    fila[5] = String.valueOf(rs.getTimestamp("fecha_fin"));
                    fila[6] = String.valueOf(rs.getDouble("periodo"));
                    fila[7] = String.valueOf(rs.getDouble("precio_total"));
                    fila[8] = rs.getString("estado");
                    fila[9] = String.valueOf(rs.getInt("id_reservacion"));
                    lista.add(fila);
                    contador++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Automatización de tiempo de reservación expirado
    public void verificarYExpirarReservaciones() {
        String buscarExpiradas = "SELECT id_reservacion, id_habitacion FROM reservacion WHERE estado = 'Activa' AND fecha_fin <= NOW()";
        String finalizarReserva = "UPDATE reservacion SET estado = 'Concluida' WHERE id_reservacion = ?";
        String ponerEnLimpieza = "UPDATE habitacion SET estado = 'Limpieza' WHERE id_habitacion = ?";

        try (Connection con = Conexion.getInstancia();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(buscarExpiradas)) {

            while (rs.next()) {
                int idRes = rs.getInt("id_reservacion");
                int idHab = rs.getInt("id_habitacion");

                try (PreparedStatement ps1 = con.prepareStatement(finalizarReserva);
                     PreparedStatement ps2 = con.prepareStatement(ponerEnLimpieza)) {
                    
                    ps1.setInt(1, idRes);
                    ps1.executeUpdate();

                    ps2.setInt(1, idHab);
                    ps2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al expirar reservaciones automáticamente: " + e.getMessage());
        }
    }

    public boolean insertar(Reservacion r) {
        int idHabitacionReal = r.getIdHabitacion();
        
        String buscarIdReal = "SELECT id_habitacion FROM habitacion WHERE id_habitacion = ? OR num_habitacion = ?";
        try (Connection con = Conexion.getInstancia();
             PreparedStatement psBusq = con.prepareStatement(buscarIdReal)) {
            psBusq.setInt(1, r.getIdHabitacion());
            psBusq.setString(2, String.valueOf(r.getIdHabitacion()));
            try (ResultSet rsBusq = psBusq.executeQuery()) {
                if (rsBusq.next()) {
                    idHabitacionReal = rsBusq.getInt("id_habitacion");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!habitacionEstaLibre(idHabitacionReal)) {
            System.out.println("[ERROR DAO] La habitación ID " + idHabitacionReal + " no está Disponible/Libre. Registro rechazado.");
            return false;
        }

        String sql = "INSERT INTO reservacion (id_cliente, id_habitacion, fecha_res, fecha_inicio, fecha_fin, periodo, precio_total, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String updateHab = "UPDATE habitacion SET estado = 'Ocupada' WHERE id_habitacion = ?";

        try (Connection con = Conexion.getInstancia()) {
            con.setAutoCommit(false);
            
            try (PreparedStatement ps = con.prepareStatement(sql);
                 PreparedStatement psHab = con.prepareStatement(updateHab)) {
                
                ps.setInt(1, r.getIdCliente());
                ps.setInt(2, idHabitacionReal);
                ps.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
                ps.setTimestamp(4, new java.sql.Timestamp(r.getFechaInicio().getTime()));
                ps.setTimestamp(5, new java.sql.Timestamp(r.getFechaFin().getTime()));
                ps.setDouble(6, r.getPeriodo());
                ps.setDouble(7, r.getPrecioTotal());
                ps.setString(8, r.getEstado());
                
                int res1 = ps.executeUpdate();

                psHab.setInt(1, idHabitacionReal);
                int res2 = psHab.executeUpdate();

                if (res1 > 0 && res2 > 0) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean habitacionEstaLibre(int idHabitacion) {
        String sql = "SELECT estado FROM habitacion WHERE id_habitacion = ?";
        try (Connection con = Conexion.getInstancia();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHabitacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String estadoBD = rs.getString("estado");
                    return estadoBD.equalsIgnoreCase("Disponible") || estadoBD.equalsIgnoreCase("Libre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cancelar(int idReservacion) {
        String sqlSelect = "SELECT id_habitacion FROM reservacion WHERE id_reservacion = ?";
        String sqlUpdateRes = "UPDATE reservacion SET estado = 'Cancelada' WHERE id_reservacion = ?";
        String sqlUpdateHab = "UPDATE habitacion SET estado = 'Disponible' WHERE id_habitacion = ?";

        try (Connection con = Conexion.getInstancia()) {
            con.setAutoCommit(false);
            int idHabitacion = -1;

            try (PreparedStatement psSel = con.prepareStatement(sqlSelect)) {
                psSel.setInt(1, idReservacion);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) idHabitacion = rs.getInt("id_habitacion");
                }
            }

            try (PreparedStatement psRes = con.prepareStatement(sqlUpdateRes);
                 PreparedStatement psHab = con.prepareStatement(sqlUpdateHab)) {
                
                psRes.setInt(1, idReservacion);
                int r1 = psRes.executeUpdate();

                int r2 = 1;
                if (idHabitacion != -1) {
                    psHab.setInt(1, idHabitacion);
                    r2 = psHab.executeUpdate();
                }

                if (r1 > 0 && r2 > 0) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Consulta para el Historial del Analista (Usa LEFT JOIN por seguridad)
    public List<String[]> obtenerHistorialGeneral() {
        verificarYExpirarReservaciones();
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT r.id_reservacion, c.nom_cliente, h.num_habitacion, h.tipo, " +
                     "r.fecha_inicio, r.fecha_fin, r.periodo, r.precio_total, r.estado " +
                     "FROM reservacion r " +
                     "LEFT JOIN cliente c ON r.id_cliente = c.id_cliente " +
                     "LEFT JOIN habitacion h ON r.id_habitacion = h.id_habitacion " +
                     "ORDER BY r.id_reservacion DESC";
        try (Connection con = Conexion.getInstancia();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            int contador = 1;
            while (rs.next()) {
                String[] fila = new String[9];
                fila[0] = String.valueOf(contador);
                fila[1] = rs.getString("nom_cliente") != null ? rs.getString("nom_cliente") : "N/A";
                fila[2] = rs.getString("num_habitacion") != null ? rs.getString("num_habitacion") : "N/A";
                fila[3] = rs.getString("tipo") != null ? rs.getString("tipo") : "N/A";
                fila[4] = String.valueOf(rs.getTimestamp("fecha_inicio"));
                fila[5] = String.valueOf(rs.getTimestamp("fecha_fin"));
                fila[6] = String.valueOf(rs.getDouble("periodo"));
                fila[7] = String.valueOf(rs.getDouble("precio_total"));
                fila[8] = rs.getString("estado") != null ? rs.getString("estado") : "Activa";
                lista.add(fila);
                contador++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // MÉTODO PUENTE CRÍTICO EXIGIDO POR HISTORIALRESERVACIONES.JAVA
    public List<String[]> obtenerReservacionesDetalladas() {
        return obtenerHistorialGeneral();
    }
}