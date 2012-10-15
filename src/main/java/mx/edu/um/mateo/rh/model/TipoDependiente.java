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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(name = "tipoDependiente")

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
     @NotBlank
    @Column(nullable = false, length = 50)
     private String nombre;

    private TipoDependiente(Integer id, String nombre){
        this.id = id;
        this.nombre = nombre;
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
        return nombre;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.nombre = descripcion;
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
