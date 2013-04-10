/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;

/**
 *
 * @author develop
 */
public interface PaqueteDao {
      public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets paquete's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the paquete's id
     * @return paquete populated paquete object
     */
<<<<<<< HEAD
    public Paquete obtiene(final Long id);
    
=======
    public Paquete getPaquete(final Long id);

>>>>>>> b20b2e03d2080fc6bc589dd66140f8133a75a89e
    /**
     * Saves a paquete's information
     * @param paquete the object to be saved
     */    
    public void crea(Paquete paquete, Usuario usuario);

    /**
     * Removes a paquete from the database by id
     * @param id the paquete's id
     */
<<<<<<< HEAD
    public String elimina(final Long id);
=======
    public String removePaquete(final Long id);
>>>>>>> b20b2e03d2080fc6bc589dd66140f8133a75a89e
    
}
