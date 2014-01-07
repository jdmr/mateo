/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.Jefe;

/**
 *
 * @author develop
 */
public interface JefeManager {

    /**
     * Regresa una lista de jefe.
     *
     * @param jefe
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una jefe
     *
     * @param id
     * @return
     */
    public Jefe obtiene(final Long id);

    /**
     * graba informacion sobre una jefe
     *
     * @param jefe the object to be saved
     */
    public void graba(Jefe jefe, Usuario usuario);

    /**
     * Elimina el jefe
     *
     * @param id el id de jefe
     */
    public String elimina(final Long id);
}
