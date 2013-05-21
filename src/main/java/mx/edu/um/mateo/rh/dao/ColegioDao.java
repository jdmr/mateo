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
    public Colegio obtiene(final Long id);
    
    public void crea(Colegio colegio);

    /**
     * Removes a colegio from the database by id
     * @param id the colegio's id
     */
    public String elimina(final Long id);
}


