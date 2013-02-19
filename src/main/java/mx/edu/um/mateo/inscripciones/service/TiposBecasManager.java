/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;

/**
 *
 * @author develop
 */
public interface TiposBecasManager {
       /**
     *
     * @param params
     * @return
     */
    public Map<String, Object> getTiposBeca(Map<String, Object> params);

    /**
     * Gets tipoBeca's information based on id.
     * @param id the tipoBeca's id
     * @return tipoBeca populated tipoBeca object
     */
    public TiposBecas getTipoBeca(final String id);

    /**
     * Saves a tipoBeca's information
     * @param tipoBeca the object to be saved
     */
    public void saveTipoBeca(TiposBecas tipoBeca);

    /**
     * Removes a tipoBeca from the database by id
     * @param id the tipoBeca's id
     */
    public void removeTipoBeca(final String id);
    
}
