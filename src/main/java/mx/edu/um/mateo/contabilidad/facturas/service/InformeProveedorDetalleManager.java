/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.Contrarecibo;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
public interface InformeProveedorDetalleManager {

    /**
     * Regresa una lista de Informes.
     *
     * @param InformeProveedor
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    public Map<String, Object> contrarecibo(Map<String, Object> params);

    public Map<String, Object> revisar(Map<String, Object> params);

    /**
     * Obtiene una InformeProveedor
     *
     * @param id
     * @return
     */
    public InformeProveedorDetalle obtiene(final Long id);

    /**
     * graba informacion sobre un proveedor
     *
     * @param InformeProveedor the object to be saved
     */
    public void graba(InformeProveedorDetalle proveedorDetalle, Usuario usuario);

    public void actualiza(InformeProveedorDetalle proveedorDetalle, Usuario usuario);

    /**
     * Cambia el status de la proveedor a I
     *
     * @param id el id de InformeEmpleado
     */
    public String elimina(final Long id);

    public Contrarecibo autorizar(List ids, Usuario usuario) throws Exception;

    public void rechazar(List ids, Usuario usuario) throws Exception;
}
