/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
public interface InformeProveedorDao {

    /**
     * Lista los informes por proveedor en session, la lista de informe se
     * filtra por el id de el proveedor en este caso.
     *
     * @param params
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Lista los informes del empleado en session, la lista de informes se
     * filtra por el id del empleado en este caso.
     *
     * @param params
     * @return
     */
    public Map<String, Object> listaEmpleado(Map<String, Object> params);

    public Map<String, Object> revisar(Map<String, Object> params);

    /**
     * Gets informe's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
     * found.
     *
     * @param id the informe's id
     * @return informe populated informe object
     */
    public InformeProveedor obtiene(final Long id);

    /**
     * Saves a informe's information
     *
     * @param informe the object to be saved
     */
    public void crea(InformeProveedor informe, Usuario usuario);

    /**
     * Actualiza un informe
     *
     * @param informe
     * @param usuario
     */
    public void actualiza(InformeProveedor informe, Usuario usuario);

    /**
     * Removes a informe from the database by id
     *
     * @param id the informe's id
     */
    public String elimina(final Long id);
}
