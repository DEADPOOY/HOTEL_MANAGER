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
    private static Usuario usuarioLogueado = null;

    public static void iniciar(Usuario u) { usuarioLogueado = u; }
    public static void cerrar() { usuarioLogueado = null; }
    public static Usuario getUsuario() { return usuarioLogueado; }
    public static boolean esAdmin() { return usuarioLogueado != null && "Administrador".equals(usuarioLogueado.getRol()); }
    public static boolean esAnalista() { return usuarioLogueado != null && "Analista".equals(usuarioLogueado.getRol()); }
    public static boolean esRecepcionista() { return usuarioLogueado != null && "Recepcionista".equals(usuarioLogueado.getRol()); }
}