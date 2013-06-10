/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
public interface InformeEmpleadoDetalleManager {

    /**
     * Regresa una lista de Informes.
     *
     * @param InformeEmpleadoDetalle
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una InformeEmpleadoDetalle
     *
     * @param id
     * @return
     */
    public InformeEmpleadoDetalle obtiene(final Long id);

    /**
     * graba informacion sobre una nacionalidad
     *
     * @param InformeEmpleadoDetalle the object to be saved
     */
    public void graba(InformeEmpleadoDetalle detalle, Usuario usuario);

    public void actualiza(InformeEmpleadoDetalle detalle, Usuario usuario);

    /**
     * Cambia el status del informe detalle a I
     *
     * @param id el id de InformeEmpleado
     */
    public String elimina(final Long id);
}
