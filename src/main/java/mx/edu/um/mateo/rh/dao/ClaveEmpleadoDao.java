/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.ClaveEmpleado;

/**
 *
 * @author develop
 */
public interface ClaveEmpleadoDao {

    /**
     * Regresa una lista de claveEmpleadoes.
     *
     * @param claveEmpleado
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una claveEmpleado
     *
     * @param id
     * @return
     */
    public ClaveEmpleado obtiene(final Long id);

    /**
     * graba informacion sobre una claveEmpleado
     *
     * @param claveEmpleado the object to be saved
     */
    public void graba(ClaveEmpleado claveEmpleado, Usuario usuario);

    /**
     * Cambia el status de la claveEmpleado a I
     *
     * @param id el id de claveEmpleado
     */
    public String elimina(final Long id);

    /**
     * Obtiene la claveActiva de un empleado a travez del id del empleado y el
     * estatus de la clave
     *
     * @param idEmpleado
     * @return
     */
    public ClaveEmpleado obtieneClaveActiva(Long idEmpleado);

    /**
     * Veridica que exista claveActiva de un empleado atravez del atributo clave
     *
     * @param clave
     * @return true o false si la clave existe o no
     */
    public Boolean noExisteClave(String clave);
}
