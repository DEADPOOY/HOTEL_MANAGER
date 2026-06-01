/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author deadpooy
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import modelo.Reservacion;

public class Exportador {
    public static boolean exportarCSV(String ruta, List<Reservacion> lista) {
        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write("ID Reservacion,ID Cliente,ID Habitacion,Fecha Res,Fecha Inicio,Fecha Fin,Periodo,Precio Total,Estado\n");
            for (Reservacion r : lista) {
                fw.write(r.getIdReservacion() + "," + r.getIdCliente() + "," + r.getIdHabitacion() + "," +
                        r.getFechaRes() + "," + r.getFechaInicio() + "," + r.getFechaFin() + "," +
                        r.getPeriodo() + "," + r.getPrecioTotal() + "," + r.getEstado() + "\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}