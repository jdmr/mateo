/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.VacacionesEmpleado;

/**
 *
 * @author develop
 */
public interface VacacionesEmpleadoDao {

    /**
     * Regresa una lista de vacacionesEmpleadoes.
     *
     * @param vacacionesEmpleado
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una vacacionesEmpleado
     *
     * @param id
     * @return
     */
    public VacacionesEmpleado obtiene(final Long id);

    /**
     * graba informacion sobre una vacacionesEmpleado
     *
     * @param vacacionesEmpleado the object to be saved
     */
    public void graba(VacacionesEmpleado vacacionesEmpleado, Usuario usuario);

    /**
     * Elimina las vacaciones
     *
     * @param id el id de vacacionesEmpleado
     */
    public String elimina(final Long id);
}
