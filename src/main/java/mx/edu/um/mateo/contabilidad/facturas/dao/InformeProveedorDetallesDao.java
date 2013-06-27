/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
public interface InformeProveedorDetallesDao {

    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets informe's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
     * found.
     *
     * @param id the informe's id
     * @return informe populated informe object
     */
    public InformeProveedorDetalle obtiene(final Long id);

    /**
     * Saves a informe's information
     *
     * @param informe the object to be saved
     */
    public void crea(InformeProveedorDetalle proveedorDetalle, Usuario usuario);

    /**
     * Actualiza un informe
     *
     * @param informe
     * @param usuario
     */
    public void actualiza(InformeProveedorDetalle proveedorDetalle, Usuario usuario);

    /**
     * Removes a informe from the database by id
     *
     * @param id the informe's id
     */
    public String elimina(final Long id);
}
