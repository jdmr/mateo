
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.TipoEmpleado;

/**
 *
 * @author zorch
 */
    public interface TipoEmpleadoManager{
    /**
     * 
     * @param params
     * @return regresa la lista de todos los TipoEmpleados
     */ 
    
    public Map<String, Object>  lista(Map<String, Object> params);

    /**
     * Obtiene la informacion del TipoEmpleado basandose en el id
     * @param id el id del TipoEmpleado
     
     */
    public TipoEmpleado obtiene(final Long id);

    /**
     * Graba la informacion nueva de un TipoEmpleado(ya sea crear o actualizar)
     * @param tipoEmpleados  el objeto que sera grabado
     * @param usuario  contiene la organizacion y empresa
     * @return TipoEmpleado
     */
    public TipoEmpleado crea(TipoEmpleado tipoEmpleado, Usuario usuario);

    /**
     * Elimina el TipoEmpleado
     * @param id el id del TipoEmpleado 
     */
    public String elimina(final Long id);
    
}
    

