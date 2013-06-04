/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.InformeEmpleado;

/**
 *
 * @author develop
 */
public interface InformeEmpleadoManager {

    /**
     * Regresa una lista de Prorrogas.
     *
     * @param InformeEmpleado
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una InformeEmpleado
     *
     * @param id
     * @return
     */
    public InformeEmpleado obtiene(final Integer id);

    /**
     * graba informacion sobre una nacionalidad
     *
     * @param InformeEmpleado the object to be saved
     */
    public void graba(InformeEmpleado informe, Usuario usuario);

    /**
     * Cambia el status de la nacionalidad a I
     *
     * @param id el id de InformeEmpleado
     */
    public String elimina(final Integer id);
}
