/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author deadpooy
 */

import conexion.Conexion; // Importa el conector Singleton de la BD
import java.sql.*; // Importa clases SQL esenciales para el manejo de sentencias
import java.util.ArrayList; // Colección dinámica para empaquetar listas de objetos
import java.util.List; // Interfaz estándar de colecciones indexadas
import modelo.Usuario; // Entidad lógica mapeada de usuarios

public class UsuarioDAO {

    private String hashSHA256(String texto) { // Algoritmo criptográfico estricto para proteger contraseñas en crudo
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256"); // Instancia el encriptador SHA-256
            byte[] hash = md.digest(texto.getBytes(java.nio.charset.StandardCharsets.UTF_8)); // Convierte la cadena en un arreglo de bytes seguro
            StringBuilder sb = new StringBuilder(); // Estructura de construcción rápida de texto
            for (byte b : hash) { // Itera byte por byte el resultado
                sb.append(String.format("%02x", b)); // Convierte el byte a representación hexadecimal de dos caracteres
            }
            return sb.toString(); // Devuelve la cadena encriptada lista para guardar o comparar
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e); // Dispara error fatal si el entorno Java carece de seguridad estándar
        }
    }

    public boolean insertar(Usuario u) { // Guarda un nuevo usuario en la base de datos
        String sql = "INSERT INTO usuario (nombre, contrasena, rol, primer_login) VALUES (?, ?, ?, ?)"; // Consulta con parámetros seguros
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) { // Precompila la sentencia SQL evitando inyecciones de código
            ps.setString(1, u.getNombre()); // Inserta el nombre de manera segura
            ps.setString(2, hashSHA256(u.getContrasena())); // Encripta la clave antes de mandarla a la tabla
            ps.setString(3, u.getRol()); // Inserta el tipo de empleado
            ps.setInt(4, u.getPrimerLogin()); // Estado inicial de acceso provisional
            return ps.executeUpdate() > 0; // Ejecuta la inserción y retorna verdadero si afectó filas de la base de datos
        } catch (SQLException e) {
            e.printStackTrace(); // Muestra el rastro detallado en consola si la inserción colapsa
            return false; // Retorna falso para notificar el fallo operativo
        }
    }

    public boolean actualizar(Usuario u) { // Modifica los datos existentes de un usuario en base a su ID
        String sql = "UPDATE usuario SET nombre = ?, contrasena = ?, rol = ?, primer_login = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, hashSHA256(u.getContrasena()));
            ps.setString(3, u.getRol());
            ps.setInt(4, u.getPrimerLogin());
            ps.setInt(5, u.getIdUsuario()); // ID único de la fila a alterar
            return ps.executeUpdate() > 0; // Retorna verdadero si la edición fue un éxito
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) { // Elimina la tupla de un usuario permanentemente por su ID
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, id); // Define qué ID se va a borrar
            return ps.executeUpdate() > 0; // Confirma si se borró de la BD
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario obtenerPorId(int id) { // Busca un único usuario por su clave primaria
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { // Almacena el resultado plano de la consulta SQL
                if (rs.next()) { // Verifica si el puntero encontró una fila coincidente
                    return new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"),
                            rs.getString("contrasena"), rs.getString("rol"), rs.getInt("primer_login")); // Empaqueta la consulta a objeto Java
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna nulo si no se encontró coincidencia en la BD
    }

    public List<Usuario> obtenerTodos() { // Genera la lista completa de usuarios existentes para las tablas de la interfaz gráfica
        List<Usuario> lista = new ArrayList<>(); // Lista dinámica donde meteremos los objetos mapeados
        String sql = "SELECT * FROM usuario";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { // Itera mientras sigan existiendo filas en el ResultSet
                lista.add(new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"),
                        rs.getString("contrasena"), rs.getString("rol"), rs.getInt("primer_login"))); // Añade el objeto mapeado a la colección
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista; // Retorna la lista con los registros procesados
    }

    public Usuario login(String nombre, String contrasena) { // Valida el acceso general de los empleados del hotel
        String sql = "SELECT * FROM usuario WHERE nombre = ? AND contrasena = ?";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, hashSHA256(contrasena)); // Compara hashes de seguridad encriptados
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"),
                            rs.getString("contrasena"), rs.getString("rol"), rs.getInt("primer_login"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Bloquea el login retornando nulo por credenciales inválidas
    }

    public Usuario loginAdmin(String contrasena) { // Validación de firma de seguridad exclusiva para el Administrador principal
        String sql = "SELECT * FROM usuario WHERE contrasena = ? AND rol = 'Administrador'";
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, hashSHA256(contrasena)); // Verifica el hash de la contraseña ingresada
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"),
                            rs.getString("contrasena"), rs.getString("rol"), rs.getInt("primer_login"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean cambiarContrasena(int idUsuario, String nuevaContrasena) { // Método para actualizar contraseña obligatoria
        String sql = "UPDATE usuario SET contrasena = ?, primer_login = 0 WHERE id_usuario = ?"; // Cambia el estado a 0 para quitar advertencia
        try (PreparedStatement ps = Conexion.getInstancia().prepareStatement(sql)) {
            ps.setString(1, hashSHA256(nuevaContrasena));
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}