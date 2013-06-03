/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.inscripciones.model.AFETipoDescuento;

/**
 *
 * @author zorch
 */
public interface AFETipoDescuentoDao {
    
      public Map<String, Object> lista(Map<String, Object> params);

	public AFETipoDescuento obtiene(final Long id);
        
        public void graba(AFETipoDescuento afeTipoDescuento, Organizacion organizacion);
        
        public String elimina (final Long id);
}
