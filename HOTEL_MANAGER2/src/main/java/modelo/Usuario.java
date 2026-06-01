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
    private int idUsuario;
    private String nombre;
    private String contrasena;
    private String rol;
    private int primerLogin;

    public Usuario(int idUsuario, String nombre, String contrasena, String rol, int primerLogin) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
        this.primerLogin = primerLogin;
    }

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