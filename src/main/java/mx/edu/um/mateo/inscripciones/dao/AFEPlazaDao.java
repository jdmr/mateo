/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.AFEPlaza;
import mx.edu.um.mateo.inscripciones.model.Paquete;

/**
 *
 * @author develop
 */
public interface AFEPlazaDao {

    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets Plaza's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
     * found.
     *
     * @param id the paquete's id
     * @return plaza populated plaza object
     */
    public AFEPlaza obtiene(final Long id);

    /**
     * Saves a afePlaza's information
     *
     * @param plaza the object to be saved
     */
    public void crea(AFEPlaza afePlaza, Usuario usuario);

    /**
     * Actualiza una plaza
     *
     * @param plaza
     * @param usuario
     */
    public void actualiza(AFEPlaza afePlaza, Usuario usuario);

    /**
     * Removes a plaza from the database by id
     *
     * @param id the plaza's id
     */
    public String elimina(final Long id);
}
