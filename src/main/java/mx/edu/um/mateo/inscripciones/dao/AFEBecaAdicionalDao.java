/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.AFEBecaAdicional;
import mx.edu.um.mateo.inscripciones.model.AFEBecaAdicional;

/**
 *
 * @author develop
 */
public interface AFEBecaAdicionalDao {

    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets AFEBecaAdicional's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
     * found.
     *
     * @param id the AFEBecaAdicional's id
     * @return AFEBecaAdicional populated AFEBecaAdicional object
     */
    public AFEBecaAdicional obtiene(final Long id);

    /**
     * Saves an AFEBecaAdicional's information
     *
     * @param AFEBecaAdicional the object to be saved
     */
    public void crea(AFEBecaAdicional becaAdicional, Usuario usuario);

    /**
     * Actualiza una AFEBecaAdicional
     *
     * @param AFEBecaAdicional
     * @param usuario
     */
    public void actualiza(AFEBecaAdicional becaAdicional, Usuario usuario);

    /**
     * Removes an AFEBecaAdicional from the database by id
     *
     * @param id the AFEBecaAdicional's id
     */
    public String elimina(final Long id);
}
