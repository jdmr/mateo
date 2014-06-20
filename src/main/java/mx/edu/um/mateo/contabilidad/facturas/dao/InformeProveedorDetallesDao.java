/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
public interface InformeProveedorDetallesDao {

    /**
     * Regresa listado de detalles por encabezado. En params espera lo
     * siguiente:<br>
     * <ol>
     * <li><b>empresa:</b>&nbsp;Id de la Empresa. [obligatorio]</li>
     * <li><b>informeProveedor:</b>&nbsp;Id del informe</li>
     * <li><b>statusInforme:</b>&nbsp;Estatus del informe. [Si este est&aacute;
     * presente, se espera que 'statusFactura' tambi&eacute;n lo
     * est&eacute;]</li>
     * <li><b>statusFactura:</b>&nbsp;Estatus de la factura</li>
     * </ol>
     *
     * @param params
     * @return
     */
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
     * Gets informe's information based on the name of the files. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
     * found.
     *
     * @param NombreFactura el nombre del archivo
     * @return informe populated informe object
     */
    public InformeProveedorDetalle obtiene(final String nombreFactura);

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
