package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.model.EmpleadoPerDed;

/**
 *
 * @author osoto@um.edu.mx
 */
public interface EmpleadoPerDedDao {

    /**
     * Regresa una lista de todos los registros de percepciones/deducciones del empleado
     *
     * @param params
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Regresa un registro de percepciones/deducciones del empleado
     *
     * @param id
     * @return
     */
    public EmpleadoPerDed obtiene(Long id);

    /**
     * Graba un registro de percepciones/deducciones del empleado
     *
     * @param empleado
     * @return
     */
    public EmpleadoPerDed graba(EmpleadoPerDed empleadoPD);
    
    /**
     * Borra un registro de percepciones/deducciones del empleado
     * @param id
     * @return 
     */
    public String elimina(Long id);
}
