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
    CON("Contado"),
    SEM("Semanal"),
    QUI("Quincenal"),
    MEN("Mensual"),
    TAN("Tanda");
    
    private String value;
    
    private FormaPago(String value){
        this.value = value;
    }
    
    public String getName(){
        return name();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
