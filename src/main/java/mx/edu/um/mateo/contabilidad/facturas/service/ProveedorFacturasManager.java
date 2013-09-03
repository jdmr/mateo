/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
public interface ProveedorFacturasManager {

    /**
     * Regresa una lista de Informes.
     *
     * @param InformeProveedor
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una Proveedor
     *
     * @param id
     * @return
     */
    public ProveedorFacturas obtiene(final Long id);

    /**
     * graba informacion sobre un proveedor
     *
     * @param Proveedor the object to be saved
     */
    public void graba(ProveedorFacturas proveedorFacturas, Usuario usuario);

    public void actualiza(ProveedorFacturas proveedorFacturas, Usuario usuario);

    /**
     * Cambia el status de la proveedor a I
     *
     * @param id el id de proveedor
     */
    public String elimina(final Long id);
}
