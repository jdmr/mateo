/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.inscripciones.model.AFETipoDescuento;

/**
 *
 * @author zorch
 */
public interface AFETipoDescuentoManager {
    /**
     * 
     * @param params
     * @return regresa la lista de todos los tipos de Descuentos
     */ 
    
    public Map<String, Object>  lista(Map<String, Object> params);

    /**
     * Obtiene la informacion del tipoDescuento basandose en el id
     * @param id el id del tipoDescuento
     
     */
    public AFETipoDescuento obtiene(final Long id);

    /**
     * Graba de un tipoDescuento(ya sea crear o actualizar)
     * @param AFETipoDescuento  el objeto que sera grabado
     */
    public void graba(AFETipoDescuento afeTipoDescuento, Organizacion organizacion);

    /**
     * Cambia el status del tipoDescuento a I
     * @param id el id del tipoDescuento 
     */
    public String elimina(final Long id);
    

}
