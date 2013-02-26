/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.inscripciones.model.AFEConvenio;
import mx.edu.um.mateo.inscripciones.model.Alumno;

/**
 *
 * @author zorch
 */
public interface AFEConvenioDao {
        
        public Map<String, Object> lista(Map<String, Object> params);

	public AFEConvenio obtiene(final Long id);
        
        public void graba(AFEConvenio afeConvenio);
        
        public String elimina (final Long id);
        
}
