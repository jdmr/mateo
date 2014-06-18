/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;

/**
 *
 * @author develop
 */
public interface InformeEmpleadoDao {

    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets informe's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
     * found.
     *
     * @param id the informe's id
     * @return informe populated informe object
     */
    public InformeEmpleado obtiene(final Long id);

    /**
     * Saves a informe's information
     *
     * @param informe the object to be saved
     */
    public void crea(InformeEmpleado informe, Usuario usuario);

    /**
     * Actualiza un informe
     *
     * @param informe
     * @param usuario
     */
    public void actualiza(InformeEmpleado informe, Usuario usuario);

    /**
     * Removes a informe from the database by id
     *
     * @param id the informe's id
     */
    public String elimina(final Long id);
}
