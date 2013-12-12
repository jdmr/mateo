/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.model;

/**
 *
 * @author osoto
 */
public enum FormaPago {
    CONTADO("C", "Contado"),
    SEMANAL("S", "Semanal"),
    QUINCENAL("Q", "Quincenal"),
    MENSUAL("M", "Mensual"),
    TANDA("T", "Tanda");
    
    private String inicial;
    private String nombre;
    
    private FormaPago(String inicial, String nombre){
        this.inicial = inicial;
        this.nombre = nombre;
    }

    public String getInicial() {
        return inicial;
    }

    public void setInicial(String inicial) {
        this.inicial = inicial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    
}
