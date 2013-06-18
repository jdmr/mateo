/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
public interface InformeProveedorManager {

    /**
     * Regresa una lista de Informes.
     *
     * @param InformeProveedor
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una InformeProveedor
     *
     * @param id
     * @return
     */
    public InformeProveedor obtiene(final Long id);

    /**
     * graba informacion sobre un proveedor
     *
     * @param InformeProveedor the object to be saved
     */
    public void graba(InformeProveedor informeProveedor, Usuario usuario);

    public void actualiza(InformeProveedor informeProveedor, Usuario usuario);

    /**
     * Cambia el status de la proveedor a I
     *
     * @param id el id de InformeEmpleado
     */
    public String elimina(final Long id);
}
