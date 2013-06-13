/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.service;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.model.OrdenPago;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author osoto
 */
public interface OrdenPagoManager {
    /**
     * 
     * @param params
     * @return regresa la lista de todas las ordenes de pago
     */ 
    
    public Map<String, Object>  lista(Map<String, Object> params);

    /**
     * Obtiene la informacion de la OrdenPago basandose en el id
     * @param id el id de la OrdenPago
     
     */
    public OrdenPago obtiene(final Long id);

    /**
     * Graba una OrdenPago
     * @param OrdenPago  el objeto que sera grabado
     */
    public void graba(OrdenPago ordenPago, Usuario usuario);
    
    /**
     * Actualiza una OrdenPago
     * @param OrdenPago  el objeto que sera grabado
     */
    public void actualiza(OrdenPago ordenPago, Usuario usuario);

    /**
     * Elimina una OrdenPago
     * @param id el id de la OrdenPago
     */
    public String elimina(final Long id);
    

}
