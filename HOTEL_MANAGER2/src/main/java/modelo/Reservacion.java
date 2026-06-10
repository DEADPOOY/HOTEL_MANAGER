/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author deadpooy
 */

import java.util.Date; // Librería estándar de fechas de Java

public class Reservacion {
    private int idReservacion; // ID único correlativo de control
    private int idCliente; // ID asociado del cliente titular
    private int idHabitacion; // ID del cuarto reservado
    private Date fechaRes; // Cuándo se registró la cita en sistema
    private Date fechaInicio; // Cuándo inicia la estancia física
    private Date fechaFin; // Cuándo concluye la estancia física
    private double periodo; // Cuantificación horaria de duración
    private double precioTotal; // Monto financiero final de cobro
    private String estado; // Estado de reservación (Activa, Cancelada, Concluida)

    public Reservacion(int idReservacion, int idCliente, int idHabitacion, Date fechaRes, Date fechaInicio, Date fechaFin, double periodo, double precioTotal, String estado) { // Inicializador estructurado
        this.idReservacion = idReservacion;
        this.idCliente = idCliente;
        this.idHabitacion = idHabitacion;
        this.fechaRes = fechaRes;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.periodo = periodo;
        this.precioTotal = precioTotal;
        this.estado = estado;
    }

    // Getters y Setters para lectura y reajuste de parámetros comerciales
    public int getIdReservacion() { return idReservacion; }
    public void setIdReservacion(int idReservacion) { this.idReservacion = idReservacion; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public Date getFechaRes() { return fechaRes; }
    public void setFechaRes(Date fechaRes) { this.fechaRes = fechaRes; }
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    public double getPeriodo() { return periodo; }
    public void setPeriodo(double periodo) { this.periodo = periodo; }
    public double getPrecioTotal() { return precioTotal; }
    public void setPrecioTotal(double precioTotal) { this.precioTotal = precioTotal; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}