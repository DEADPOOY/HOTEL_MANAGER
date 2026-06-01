/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author deadpooy
 */

import java.sql.Time;
import java.util.Date;

public class Registro {
    private int idRegistro;
    private int idCliente;
    private int idReservacion;
    private int idHabitacion;
    private Time horaReg;
    private Date fechaReg;

    public Registro(int idRegistro, int idCliente, int idReservacion, int idHabitacion, Time horaReg, Date fechaReg) {
        this.idRegistro = idRegistro;
        this.idCliente = idCliente;
        this.idReservacion = idReservacion;
        this.idHabitacion = idHabitacion;
        this.horaReg = horaReg;
        this.fechaReg = fechaReg;
    }

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