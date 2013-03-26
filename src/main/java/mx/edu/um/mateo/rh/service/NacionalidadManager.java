/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.Nacionalidad;

/**
 *
 * @author zorch
 */
public interface NacionalidadManager{
    /**
     * 
     * @param params
     * @return regresa la lista de todas las nacionalidades
     */ 
    
    public Map<String, Object>  lista(Map<String, Object> params);

    /**
     * Obtiene la informacion de nacionalidad basandose en el id
     * @param id el id de nacionalidad
     
     */
    public Nacionalidad obtiene(final Long id);

    /**
     * Graba la informacion nueva de una nacionalidad(ya sea crear o actualizar)
     * @param nacionalidades el objeto que sera grabado
     */
    public void graba(Nacionalidad nacionalidad, Usuario usuario);

    /**
     * Cambia el status de la nacionalidad a I
     * @param id el id de nacionalidad
     */
    public String elimina(final Long id);
    
}
