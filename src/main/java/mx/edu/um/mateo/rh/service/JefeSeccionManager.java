/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.JefeSeccion;

/**
 *
 * @author develop
 */
public interface JefeSeccionManager {

    /**
     * Regresa una lista de jefeSecciones.
     *
     * @param params
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una jefeSeccion
     *
     * @param id
     * @return
     */
    public JefeSeccion obtiene(final Long id);

    /**
     * graba informacion sobre una jefeSeccion
     *
     * @param jefeSeccion the object to be saved
     * @param usuario
     * @param ids
     */
    public void graba(JefeSeccion jefeSeccion, Usuario usuario, List ids);

    /**
     * Cambia el status de la jefeSeccion a I
     *
     * @param id el id de jefeSeccion
     * @return
     */
    public String elimina(final Long id);

}
