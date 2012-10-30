/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

/**
 *
 * @author zorch
 */
public enum TipoDependiente  {
     UNKNOW (0, "Todas"),
     HIJO  (1, "Hijo"),
     HIJA (2, "Hijo"),
     ESPOSO (3, "Esposo"),
     ESPOSA (4, "Esposa");
     
    private Integer id;
    private String descripcion;
    
    
      private TipoDependiente(Integer id, String descripcion){
        this.id = id;
        this.descripcion = descripcion;
    }

    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    

    public Integer getId() {
        return id;
    }
   
    public void setId(Integer id) {
        this.id = id;
    }
    
    public static TipoDependiente valueOf (Integer id){
        switch (id){
            
            case 1: return HIJO;
            case 2: return HIJA;
            case 3: return ESPOSO;
            case 4: return ESPOSA;
            default: return UNKNOW;  
        }
              
    }
    
   
    
    
 
//    /**
//     *
//     * @return
//     */
//    public static ArrayList<TipoDependiente> lista(){
//        ArrayList<TipoDependiente> lista= new ArrayList<>();
//        lista.add(TipoDependiente.ESPOSO);
//        lista.add(TipoDependiente.ESPOSA);
//        lista.add(TipoDependiente.HIJA);
//        lista.add(TipoDependiente.HIJO);
//        lista.add(TipoDependiente.UNKNOW);
//        return lista;
//    }
     
     
     @Override
    public String toString() {
        return "NivelEstudios{" + "descripcion=" + descripcion + '}';
    }
}
