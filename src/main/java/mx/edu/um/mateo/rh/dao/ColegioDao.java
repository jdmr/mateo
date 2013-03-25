package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.rh.model.Colegio;

public interface ColegioDao  {

    /**
     * Retrieves all of the colegios
     */
   public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets colegio's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the colegio's id
     * @return colegio populated colegio object
     */
    public Colegio getColegio(final Long id);
    
    public void grabaColegio(Colegio colegio);

    /**
     * Saves a colegio's information
     * @param colegio the object to be saved
     */    
    

    /**
     * Removes a colegio from the database by id
     * @param id the colegio's id
     */
    public String removeColegio(final Long id);
}


