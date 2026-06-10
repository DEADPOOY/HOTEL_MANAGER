/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import modelo.Usuario;

public class Sesion {
    private static Usuario usuarioLogueado = null; // Almacena de manera global el objeto del usuario que inició sesión

    public static void iniciar(Usuario u) { usuarioLogueado = u; } // Guarda el usuario validado en memoria volátil
    public static void cerrar() { usuarioLogueado = null; } // Limpia la sesión actual al cerrar la sesión
    public static Usuario getUsuario() { return usuarioLogueado; } // Devuelve los datos del empleado activo
    // Métodos rápidos para validar el rol del usuario conectado y controlar accesos en la interfaz gráfica
    public static boolean esAdmin() { return usuarioLogueado != null && "Administrador".equals(usuarioLogueado.getRol()); }
    public static boolean esAnalista() { return usuarioLogueado != null && "Analista".equals(usuarioLogueado.getRol()); }
    public static boolean esRecepcionista() { return usuarioLogueado != null && "Recepcionista".equals(usuarioLogueado.getRol()); }
}