/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.AFEPlaza;

/**
 *
 * @author develop
 */
public interface AFEPlazaManager {

    /**
     * Regresa una lista de AFE Plazas.
     *
     * @param AFEPlaza
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una AFEPlaza
     *
     * @param id
     * @return
     */
    public AFEPlaza obtiene(final Long id);

    /**
     * graba informacion sobre una AFE Plaza
     *
     * @param AFEPlaza the object to be saved
     */
    public void crea(AFEPlaza afePlaza, Usuario usuario);

    /**
     * Cambia el status de la AFEPlaza a I
     *
     * @param id el id de AFE Plaza
     */
    public String elimina(final Long id);
}
