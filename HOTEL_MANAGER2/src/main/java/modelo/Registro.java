/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author deadpooy
 */

import java.sql.Time; // Clase SQL para capturar únicamente horas, minutos y segundos
import java.util.Date; // Clase estándar para fechas generales

public class Registro {
    private int idRegistro; // Identificador correlativo numérico
    private int idCliente; // Cliente que genera el evento de entrada
    private int idReservacion; // Ticket de reservación vinculado
    private int idHabitacion; // Cuarto físico involucrado en el check-in
    private Time horaReg; // Marca temporal de hora exacta
    private Date fechaReg; // Marca de fecha del día

    public Registro(int idRegistro, int idCliente, int idReservacion, int idHabitacion, Time horaReg, Date fechaReg) { // Constructor para instanciar registros rápidos
        this.idRegistro = idRegistro;
        this.idCliente = idCliente;
        this.idReservacion = idReservacion;
        this.idHabitacion = idHabitacion;
        this.horaReg = horaReg;
        this.fechaReg = fechaReg;
    }

    // Getters y Setters de control de flujo
    public int getIdTarget() { return idRegistro; }
    public int getIdRegistro() { return idRegistro; }
    public void setIdRegistro(int idRegistro) { this.idRegistro = idRegistro; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdReservacion() { return idReservacion; }
    public void setIdReservacion(int idReservacion) { this.idReservacion = idReservacion; }
    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public Time getHoraReg() { return horaReg; }
    public void setHoraReg(Time horaReg) { this.horaReg = horaReg; }
    public Date getFechaReg() { return fechaReg; }
    public void setFechaReg(Date fechaReg) { this.fechaReg = fechaReg; }
}