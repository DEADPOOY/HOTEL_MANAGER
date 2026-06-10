/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author deadpooy
 */

public class Cliente {
    private int idCliente; // ID numérico relacional del cliente
    private String nomCliente; // Nombre completo guardado en sistema
    private String numCliente; // Número telefónico de control de contacto

    public Cliente(int idCliente, String nomCliente, String numCliente) { // Inicializador del objeto cliente
        this.idCliente = idCliente; // Define el ID
        this.nomCliente = nomCliente; // Define el nombre
        this.numCliente = numCliente; // Define el teléfono
    }

    // Encapsulamiento del modelo de datos de clientes
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNomCliente() { return nomCliente; }
    public void setNomCliente(String nomCliente) { this.nomCliente = nomCliente; }
    public String getNumCliente() { return numCliente; }
    public void setNumCliente(String numCliente) { this.numCliente = numCliente; }
}