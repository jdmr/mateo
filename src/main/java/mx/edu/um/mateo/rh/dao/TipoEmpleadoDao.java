
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.TipoEmpleado;

/**
 *
 * @author osoto@um.edu.mx
 */
public interface TipoEmpleadoDao {

    /**
     * Regresa una lista de todos los tipoEmpleados
     * @param params
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params);
    /**
     * Regresa al tipoEmpleado correspondiente al id
     * @param id
     * @return 
     */
    public TipoEmpleado obtiene(Long id);
    /**
     * Graba un tipoEmpleado con su empresa, la cual obtiene del usuario
     * @param tipoEmpleado
     * @param usuario
     * @return 
     */
    public TipoEmpleado crea(TipoEmpleado tipoEmpleado, Usuario usuario);
    /**
     * Graba un tipoEmpleado, SIN empresa. Este metodo se utiliza para pruebas DAO
     * @param tipoEmpleado
     * @return 
     */
    public TipoEmpleado crea(TipoEmpleado tipoEmpleado);
    /**
     * Elimina un tipoempleado
     * @param id
     * @return 
     */
    public String elimina(Long id);
}
