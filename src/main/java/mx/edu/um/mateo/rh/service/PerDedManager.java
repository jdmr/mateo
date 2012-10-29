/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.PerDed;

/**
 *
 * @author semdariobarbaamaya
 */
public interface PerDedManager {
     /**
     * Regresa todas las perded
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene las perded basandose en su id
     * @param id el id de la perded
     * @return regresa la perded que obtuvo mediante el id
     */
    public PerDed obtiene(final String id);

    /**
     * Graba la informacion de la perded
     * @param perded el objeto que sera grabado
     */
    public void graba(PerDed perded, Usuario usuario);

    /**
     * Elimina la perded de la base de datos mediante su id
     * @param id el id de la perded
     */
    public String elimina(final Long id);
}
