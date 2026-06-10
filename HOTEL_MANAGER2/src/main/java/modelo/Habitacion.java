/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author deadpooy
 */

public class Habitacion {
    private int idHabitacion; // ID primario de la celda de datos
    private String numHabitacion; // Texto del número del cuarto
    private String tipo; // Categoría (Individual, Doble, Triple)
    private int piso; // Planta física donde se ubica
    private double precioHora; // Costo por unidad de tiempo transcurrido
    private int numCapacidad; // Límite de personas físicas simultáneas
    private String estado; // Estado actual (Libre, Ocupada, Limpieza)

    public Habitacion(int idHabitacion, String numHabitacion, String tipo, int piso, double precioHora, int numCapacidad, String estado) { // Constructor completo
        this.idHabitacion = idHabitacion;
        this.numHabitacion = numHabitacion;
        this.tipo = tipo;
        this.piso = piso;
        this.precioHora = precioHora;
        this.numCapacidad = numCapacidad;
        this.estado = estado;
    }

    // Getters y Setters para operaciones lógicas del sistema
    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public String getNumHabitacion() { return numHabitacion; }
    public void setNumHabitacion(String numHabitacion) { this.numHabitacion = numHabitacion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getPiso() { return piso; }
    public void setPiso(int piso) { this.piso = piso; }
    public double getPrecioHora() { return precioHora; }
    public void setPrecioHora(double precioHora) { this.precioHora = precioHora; }
    public int getNumCapacidad() { return numCapacidad; }
    public void setNumCapacidad(int numCapacidad) { this.numCapacidad = numCapacidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}