/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.nomina.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.nomina.model.PerDed;

/**
 *
 * @author semdariobarbaamaya
 */
public interface PerDedDao {
    
    /**
     * Regresa una lista de perded.
     * @param perded
     * @return 
     */
    public Map<String, Object> lista (Map<String, Object> params);

    /**
     * Obtiene una perded
     * @param id
     * @return 
     */
    public PerDed obtiene(final Long id);

    /**
     * Guarda la informacion de las perded
     * @param perded el objeto que sera guardado
     */    
    public void graba(PerDed perded, Usuario usuario);

    /**
     * Borra la categoria de la base de datos mediante el id
     * @param id id de la perded
     */
    public String elimina(final Long id);
}
