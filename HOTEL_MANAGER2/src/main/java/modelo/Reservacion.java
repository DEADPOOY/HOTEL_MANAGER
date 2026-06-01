/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author deadpooy
 */

import java.util.Date;

public class Reservacion {
    private int idReservacion;
    private int idCliente;
    private int idHabitacion;
    private Date fechaRes;
    private Date fechaInicio;
    private Date fechaFin;
    private double periodo;
    private double precioTotal;
    private String estado;

    public Reservacion(int idReservacion, int idCliente, int idHabitacion, Date fechaRes, Date fechaInicio, Date fechaFin, double periodo, double precioTotal, String estado) {
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

    public int getIdReservacion() { return idReservacion; }
    public void setIdReservacion(int idReservacion) { this.idReservacion = idReservacion; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int idHabitacion() { return idHabitacion; }
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