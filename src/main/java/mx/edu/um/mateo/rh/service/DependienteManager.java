
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.rh.model.Dependiente;

/**
 *
 * @author zorch
 */
    public interface DependienteManager{
    /**
     * 
     * @param params
     * @return regresa la lista de todos los Dependientes
     */ 
    
    public Map<String, Object>  lista(Map<String, Object> params);

    /**
     * Obtiene la informacion del Dependiente basandose en el id
     * @param id el id del Dependiente
     
     */
    public Dependiente obtiene(final Long id);

    /**
     * Graba la informacion nueva de un Dependiente(ya sea crear o actualizar)
     * @param dependientes  el objeto que sera grabado
     */
    public void graba(Dependiente dependiente);

    /**
     * Cambia el status del Dependiente a I
     * @param id el id del Dependiente 
     */
    public String elimina(final Long id);
    
}
    

