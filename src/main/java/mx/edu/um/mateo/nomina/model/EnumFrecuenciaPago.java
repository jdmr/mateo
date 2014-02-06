/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.nomina.model;

/**
 *
 * @author osoto
 */
public enum EnumFrecuenciaPago {
    SEMANAL(1, "Semanal"),
    QUINCENA_1(2, "Semanal"),
    QUINCENA_2(3, "Semanal"),
    MENSUAL(4, "Semanal");
    
    private Integer id;
    private String nombre;

    private EnumFrecuenciaPago(Integer id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
