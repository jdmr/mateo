/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.List;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;

/**
 *
 * @author develop
 */
public interface TipoBecasDao {

    /**
     * Retrieves all of the TipoBeca
     */
    public List getTiposBeca(TiposBecas tipoBeca);

    /**
     * Gets tipoBeca's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the tipoBeca's id
     * @return tipoBeca populated tipoBeca object
     */
    public TiposBecas getTipoBeca(final Integer id);

    /**
     * Saves a tipoBeca's information
     * @param tipoBeca the object to be saved
     */    
    public void saveTipoBeca(TiposBecas tipoBeca);

    /**
     * Removes a tipoBeca from the database by id
     * @param id the tipoBeca's id
     */
    public void removeTipoBeca(final Integer id);
}
