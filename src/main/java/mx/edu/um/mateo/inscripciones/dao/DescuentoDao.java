/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.inscripciones.model.Descuento;

/**
 *
 * @author zorch
 */
public interface DescuentoDao {
        
        public Map<String, Object> lista(Map<String, Object> params);

	public Descuento obtiene(final Long id);
        
        public void graba(Descuento descuento, Organizacion organizacion);
        
        public String elimina (final Long id);
        
}