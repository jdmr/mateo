/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;
import java.util.Map;
import mx.edu.um.mateo.rh.model.Dependiente;

/**
 *
 * @author zorch
 */

public interface DependienteDao {
      
     /**
     * Regresa una lista de dependientes.
     * @param Dependiente
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene un Dependiente
     * @param id
     * @return 
     */
    public Dependiente obtiene(final Long id);

    /**
     * graba informacion sobre un Dependiente 
     * @param Dependiente the object to be saved
     */  
    public void graba(Dependiente dependiente);

     /**
     * Cambia el status del Dependiente a I
     * @param id el id de dependiente
     */
    public String elimina(final Long id) ;
    
}