/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.rh.model.Nacionalidad;

/**
 *
 * @author developer
 */
public interface NacionalidadDao {
      
    public Map<String, Object> lista(Map<String, Object> params);

    public Nacionalidad obtiene(Long id);

    public Nacionalidad crea(Nacionalidad nacionalidad);

    public Nacionalidad actualiza(Nacionalidad nacionalidad);

    public String elimina(Long id) throws UltimoException;
    
}
