/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.Prorroga;

/**
 *
 * @author develop
 */
public interface ProrrogaDao {

    /**
     * Regresa una lista de Prorrogas.
     *
     * @param Prorroga
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una Prorroga
     *
     * @param id
     * @return
     */
    public Prorroga obtiene(final Integer id);

    /**
     * graba informacion sobre una nacionalidad
     *
     * @param Prorroga the object to be saved
     */
    public void graba(Prorroga prorroga, Usuario usuario);

    /**
     * Cambia el status de la nacionalidad a I
     *
     * @param id el id de Prorroga
     */
    public String elimina(final Integer id);
}
