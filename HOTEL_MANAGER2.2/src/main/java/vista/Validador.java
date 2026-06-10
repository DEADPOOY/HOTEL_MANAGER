/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import java.util.Date;

public class Validador {
    public static boolean textoVacio(String t) { return t == null || t.trim().isEmpty(); }
    public static boolean formatoTelefono(String t) { return t != null && t.matches("\\d{10,20}"); }
    public static boolean rangoFechasValido(Date i, Date f) { return i != null && f != null && f.after(i); }
}