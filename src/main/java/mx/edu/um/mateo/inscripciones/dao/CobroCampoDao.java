/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;

/**
 *
 * @author develop
 */
public interface CobroCampoDao {
    
    /**
     * Regresa una lista de Cobros a Campos.
     *
     * @param CobroCampo
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene un Cobro a Campo
     *
     * @param id
     * @return
     */
    public CobroCampo obtiene(final Integer id);

    /**
     * graba informacion sobre un CobroCampo
     *
     * @param CobroCampo the object to be saved
     */
    public void graba(CobroCampo cobroCampo, Usuario usuario);

    /**
     * Cambia el status de la CobroCampo a I
     *
     * @param id el id de CobroCampo
     */
    public String elimina(final Integer id);
}
