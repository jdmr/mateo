/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.AlumnoDescuento;

/**
 *
 * @author zorch
 */
public interface AlumnoDescuentoDao {
    public Map<String, Object> lista(Map<String, Object> params);

	public AlumnoDescuento obtiene(final Long id);
        
        public void graba(AlumnoDescuento alumnoDescuento, Usuario usuario);
        
        public String elimina (final Long id);
}
