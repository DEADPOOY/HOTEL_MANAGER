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
    private int idCliente;
    private String nomCliente;
    private String numCliente;

    public Cliente(int idCliente, String nomCliente, String numCliente) {
        this.idCliente = idCliente;
        this.nomCliente = nomCliente;
        this.numCliente = numCliente;
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNomCliente() { return nomCliente; }
    public void setNomCliente(String nomCliente) { this.nomCliente = nomCliente; }
    public String getNumCliente() { return numCliente; }
    public void setNumCliente(String numCliente) { this.numCliente = numCliente; }
    
    @Override
    public String toString() {
    return this.nomCliente;
    }
}