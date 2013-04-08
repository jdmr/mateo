/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.AlumnoPaquete;
/**
 *
 * @author semdariobarbaamaya
 */
public interface AlumnoPaqueteManager {
    
      public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets alumnoPaquete's information based on id.
     *
     * @param id the tipoBeca's id
     * @return AlumnoPaquete populated paquete object
     */
    public AlumnoPaquete obtiene(final Long id);

    
    public AlumnoPaquete actualiza(AlumnoPaquete alumnoPaquete);
    
    /**
     * Saves a AlumnoPaquete's information
     *
     * @param alumnoPaquete the object to be saved
     */
    public void graba(AlumnoPaquete alumnoPaquete, Usuario usuario);

    /**
     * Removes a alumnoPaquete from the database by id
     *
     * @param id the alumnoPaquete's id
     */
    public void elimina(final Long id);
}
