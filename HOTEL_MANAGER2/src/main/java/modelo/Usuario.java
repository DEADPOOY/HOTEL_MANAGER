/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author deadpooy
 */

public class Usuario {
    private int idUsuario; // Almacena el ID del usuario de la base de datos
    private String nombre; // Almacena el nombre público del perfil
    private String contrasena; // Almacena la cadena segura encriptada
    private String rol; // Guarda el rol (Administrador, Recepcionista, Analista)
    private int primerLogin; // Guarda el estado de contraseña provisional

    public Usuario(int idUsuario, String nombre, String contrasena, String rol, int primerLogin) { // Constructor con parámetros completos
        this.idUsuario = idUsuario; // Mapea el identificador numérico
        this.nombre = nombre; // Mapea el login string
        this.contrasena = contrasena; // Mapea la clave hash
        this.rol = rol; // Mapea el nivel de acceso
        this.primerLogin = primerLogin; // Mapea la validación de ingreso inicial
    }

    // Métodos Getter y Setter estándar para acceso de lectura y escritura limpia de atributos
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public int getPrimerLogin() { return primerLogin; }
    public void setPrimerLogin(int primerLogin) { this.primerLogin = primerLogin; }
}