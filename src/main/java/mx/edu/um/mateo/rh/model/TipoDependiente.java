/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

/**
 *
 * @author develop
 */
import java.util.ArrayList;
import java.util.List;


public enum TipoDependiente {
//    BECA_RECTORIA(1,"Beca Rectoria"),
//    PLAN_PROMOCIONAL_EDUCACION(2,"Plan Promocional Educacional"),
//    BECAS_ESPECIALES_VRF(3,"Becas Especiales VRF"),
//    PLANES_ESPECIALES_UNIONES(4,"Planes Especiales Uniones"),
//    BECAS_EDUCATIVAS_HOSPITAL(5,"Becas Educativas HLC"),
//    BECAS_MUNICIPALES(6,"Becas Municipales"),
//    OTROS(10,"Otros");
    UNKNOWN(0,"Todas"),
    HIJO(1, "Hijo"),
    HIJA(2, "Hija"),
    ESPOSA(3, "Esposa"),
    ESPOSO(4, "Esposo");
    
    private Integer id;
    private String descripcion;

    private TipoDependiente(Integer id, String descripcion){
        this.id = id;
        this.descripcion = descripcion;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public static TipoDependiente valueOf(Integer id){
        switch(id){
            case 1 : return HIJO;
            case 2 : return HIJA;
            case 3 : return ESPOSA;
            case 4 : return ESPOSO;
            default: return UNKNOWN;
        }
    }

    public static List getTipos(){
        List list = new ArrayList();
        list.add(TipoDependiente.valueOf(0));
        list.add(TipoDependiente.valueOf(1));
        list.add(TipoDependiente.valueOf(2));
        list.add(TipoDependiente.valueOf(3));
        list.add(TipoDependiente.valueOf(4));
        
        return list;
    }
}
