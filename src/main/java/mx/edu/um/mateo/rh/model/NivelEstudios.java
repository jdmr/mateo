/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

/**
 *
 * @author develop
 */


public enum NivelEstudios {
    PRIMARIA(1, "Primaria"),
    SECUNDARIA(2, "secundaria"),
    PREPARATORIA(3, "Preparatoria"),
    LICENCIATURA(4, "Licenciatura"),
    MAESTRIA(5, "Maestria"),
    DOCTORADO(6, "Doctorado");
    
    private Integer id;
    private String descripcion;

    private NivelEstudios(Integer id, String descripcion){
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

    public static NivelEstudios valueOf(Integer id){
        switch(id){
            case 2 : return SECUNDARIA;
            case 3 : return PREPARATORIA;
            case 4 : return LICENCIATURA;
            case 5 : return MAESTRIA;
            case 6: return DOCTORADO;
            default: return PRIMARIA;
        }
    }
    
    
//
//    public static List getNiveles(){
//        List list = new ArrayList();
//        list.add(NivelEstudios.valueOf(0));
//        list.add(NivelEstudios.valueOf(1));
//        list.add(NivelEstudios.valueOf(2));
//        list.add(NivelEstudios.valueOf(3));
//        list.add(NivelEstudios.valueOf(4));
//        list.add(NivelEstudios.valueOf(5));
//        list.add(NivelEstudios.valueOf(6));
//        
//        return list;
//    }

    @Override
    public String toString() {
        return "NivelEstudios{" + "descripcion=" + descripcion + '}';
    }
}
