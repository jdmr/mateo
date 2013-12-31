/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.VacacionesEmpleado;

/**
 *
 * @author develop
 */
public interface VacacionesEmpleadoManager {

    /**
     * Regresa una lista de vacaciones.
     *
     * @param params
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una vacaciones
     *
     * @param id
     * @return
     */
    public VacacionesEmpleado obtiene(final Long id);

    /**
     * graba informacion sobre las vacaciones de algun empleado
     *
     * @param vacaciones the object to be saved
     * @param usuario
     */
    public void graba(VacacionesEmpleado vacaciones, Usuario usuario);

    /**
     * Elimina las vacaciones
     *
     * @param id el id de vacaciones
     * @return
     */
    public String elimina(final Long id);
}
