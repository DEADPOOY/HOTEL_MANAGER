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
    public static boolean exportarCSV(String ruta, List<Reservacion> lista) { // Genera un archivo CSV legible en Excel con el listado de reservaciones recibido
        try (FileWriter fw = new FileWriter(ruta)) { // Abre un flujo de escritura en la ruta de archivo especificada
            fw.write("ID Reservacion,ID Cliente,ID Habitacion,Fecha Res,Fecha Inicio,Fecha Fin,Periodo,Precio Total,Estado\n"); // Escribe la fila de encabezados
            for (Reservacion r : lista) { // Itera la lista de registros
                fw.write(r.getIdReservacion() + "," + r.getIdCliente() + "," + r.getIdHabitacion() + "," +
                        r.getFechaRes() + "," + r.getFechaInicio() + "," + r.getFechaFin() + "," +
                        r.getPeriodo() + "," + r.getPrecioTotal() + "," + r.getEstado() + "\n"); // Escribe cada registro separado por comas
            }
            return true; // Retorna verdadero si la escritura se completó sin problemas
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Retorna falso en caso de problemas con los permisos del disco duro
        }
    }
}