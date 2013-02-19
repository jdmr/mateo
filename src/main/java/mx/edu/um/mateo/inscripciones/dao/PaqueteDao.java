/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;

/**
 *
 * @author develop
 */
public interface PaqueteDao {
      public Map<String, Object> getPaquetes(Map<String, Object> params);

    /**
     * Gets paquete's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the paquete's id
     * @return paquete populated paquete object
     */
    public Paquete getPaquete(final Integer id);

    /**
     * Saves a paquete's information
     * @param paquete the object to be saved
     */    
    public void savePaquete(Paquete paquete);

    /**
     * Removes a paquete from the database by id
     * @param id the paquete's id
     */
    public String removePaquete(final Integer id);
    
}
