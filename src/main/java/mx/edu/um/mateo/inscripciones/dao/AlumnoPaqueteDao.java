/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.AlumnoPaquete;

/**
 *
 * @author semdariobarbaamaya
 */
public interface AlumnoPaqueteDao {
     public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets alumnoPaquete's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the alumnoPaquete's id
     * @return paquete populated alumnoPaquete object
     */
    public AlumnoPaquete obtiene(final Long id);

    
    public AlumnoPaquete actualiza(AlumnoPaquete alumnoPaquete);
    
    
    /**
     * Saves a alumnoPaquete's information
     * @param alumnoPaquete the object to be saved
     */    
    public void graba(AlumnoPaquete alumnoPaquete, Usuario usuario);

    /**
     * Removes a alumnoPaquete from the database by id
     * @param id the alumnoPaquete's id
     */
    public String elimina(final Long id);
    
}
