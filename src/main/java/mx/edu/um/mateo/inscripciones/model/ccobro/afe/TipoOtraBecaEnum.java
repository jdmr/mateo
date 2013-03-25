package mx.edu.um.mateo.inscripciones.model.ccobro.afe;

import java.util.ArrayList;
import java.util.List;


public enum TipoOtraBecaEnum {
    UNKNOWN(0,"Todas"),
    BECA_RECTORIA(1,"Beca Rectoria"),
    PLAN_PROMOCIONAL_EDUCACION(2,"Plan Promocional Educacional"),
    BECAS_ESPECIALES_VRF(3,"Becas Especiales VRF"),
    PLANES_ESPECIALES_UNIONES(4,"Planes Especiales Uniones"),
    BECAS_EDUCATIVAS_HOSPITAL(5,"Becas Educativas HLC"),
    BECAS_MUNICIPALES(6,"Becas Municipales"),
    /**
     * Becas para dormitorios
     */
    BECAS_ESPECIALES(7,"Becas Especiales [Dormitorios]"),
    COMEDOR(10,"Comedor"),
    OTROS(20,"Otros");

    private Integer id;
    private String descripcion;

    private TipoOtraBecaEnum(Integer id, String descripcion){
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

    public static TipoOtraBecaEnum valueOf(Integer id){
        switch(id){
            case 1 : return BECA_RECTORIA;
            case 2 : return PLAN_PROMOCIONAL_EDUCACION;
            case 3 : return BECAS_ESPECIALES_VRF;
            case 4 : return PLANES_ESPECIALES_UNIONES;
            case 5 : return BECAS_EDUCATIVAS_HOSPITAL;
            case 6 : return BECAS_MUNICIPALES;
            case 7 : return BECAS_ESPECIALES;
            case 10 : return COMEDOR;
            case 20 : return OTROS;
            default: return UNKNOWN;
        }
    }

    public static List getTipos(){
        List list = new ArrayList();
        list.add(TipoOtraBecaEnum.valueOf(0));
        list.add(TipoOtraBecaEnum.valueOf(1));
        list.add(TipoOtraBecaEnum.valueOf(2));
        list.add(TipoOtraBecaEnum.valueOf(3));
        list.add(TipoOtraBecaEnum.valueOf(4));
        list.add(TipoOtraBecaEnum.valueOf(5));
        list.add(TipoOtraBecaEnum.valueOf(6));
        list.add(TipoOtraBecaEnum.valueOf(7));
        list.add(TipoOtraBecaEnum.valueOf(10));
        list.add(TipoOtraBecaEnum.valueOf(20));
        return list;
    }
}
