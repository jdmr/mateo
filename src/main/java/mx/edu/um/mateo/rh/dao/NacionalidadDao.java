/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.Nacionalidad;

/**
 *
 * @author zorch
 */
public interface NacionalidadDao {
      
     /**
     * Regresa una lista de nacionalidades.
     * @param nacionalidad
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una nacionalidad
     * @param id
     * @return 
     */
    public Nacionalidad obtiene(final Long id);

    /**
     * graba informacion sobre una nacionalidad 
     * @param nacionalidad the object to be saved
     */  
    public void graba(Nacionalidad nacionalidad, Usuario usuario);

     /**
     * elimina una nacionalidad de la base de datos mediante el id
     * @param id el id de nacionalidad
     */
    public String elimina(final Long id) ;
    
}
