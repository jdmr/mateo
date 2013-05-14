/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;

/**
 *
 * @author develop
 */
public interface TiposBecasDao {

    /**
     * Retrieves all of the TipoBeca
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets tipoBeca's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the tipoBeca's id
     * @return tipoBeca populated tipoBeca object
     */
    public TiposBecas obtiene(final Integer id);

    /**
     * Saves a tipoBeca's information
     * @param tipoBeca the object to be saved
     */    
    public void graba(TiposBecas tipoBeca, Usuario usuario);

    /**
     * Removes a tipoBeca from the database by id
     * @param id the tipoBeca's id
     */
    public String elimina(final Integer id);
    
    /**
     * Gets tipoBeca's information based on description
     * @param descripcion
     * @return 
     */
    public TiposBecas obtiene(final String descripcion);
}
