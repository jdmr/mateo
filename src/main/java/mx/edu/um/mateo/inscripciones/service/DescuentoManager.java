/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.inscripciones.model.Descuento;

/**
 *
 * @author zorch
 */
public interface DescuentoManager {
     /**
     * Regresa todos los descuentos
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene el descuento basandose en su id
     * @param id el id del descuento
     * @return regresa el descuento que obtuvo mediante el id
     */
    public Descuento obtiene(final Long id);

    /**
     * Graba la informacion del descuento
     * @param descuento el objeto que sera grabado
     */
    public void graba(Descuento descuento, Organizacion organizacion);

    /**
     * Elimina el descuento de la base de datos mediante su id
     * @param id el id del Descuento
     */
    public String elimina(final Long id);
    
}
