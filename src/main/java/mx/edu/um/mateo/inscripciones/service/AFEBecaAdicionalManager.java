/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.AFEBecaAdicional;

/**
 *
 * @author develop
 */
public interface AFEBecaAdicionalManager {

    /**
     * Regresa una lista de AFE Plazas.
     *
     * @param AFEBecaAdicional
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una AFEBecaAdicional
     *
     * @param id
     * @return
     */
    public AFEBecaAdicional obtiene(final Long id);

    /**
     * graba informacion sobre una afeBecaAdicional
     *
     * @param AFEBecaAdicional the object to be saved
     */
    public void crea(AFEBecaAdicional becaAdicional, Usuario usuario);

    /**
     * Cambia el status de la AFEBecaAdicional a I
     *
     * @param id el id de afeBecaAdicional
     */
    public String elimina(final Long id);

    public void actualiza(final AFEBecaAdicional becaAdicional, Usuario usuario);
}
