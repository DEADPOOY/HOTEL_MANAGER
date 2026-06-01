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
import modelo.Cliente;

public class ClienteDAO {

    public boolean insertar(Cliente c) {
        String sql = "INSERT INTO cliente (nom_cliente, num_cliente) VALUES (?, ?)";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNomCliente());
            ps.setString(2, c.getNumCliente());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setIdCliente(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizar(Cliente c) {
        String sql = "UPDATE cliente SET nom_cliente = ?, num_cliente = ? WHERE id_cliente = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, c.getNomCliente());
            ps.setString(2, c.getNumCliente());
            ps.setInt(3, c.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cliente obtenerPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(rs.getInt("id_cliente"), rs.getString("nom_cliente"), rs.getString("num_cliente"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cliente obtenerPorNumero(String numCliente) {
        String sql = "SELECT * FROM cliente WHERE num_cliente = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, numCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(rs.getInt("id_cliente"), rs.getString("nom_cliente"), rs.getString("num_cliente"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cliente> obtenerTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Cliente(rs.getInt("id_cliente"), rs.getString("nom_cliente"), rs.getString("num_cliente")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}