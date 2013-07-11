
package mx.edu.um.mateo.rh.service;
import java.util.Map;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.model.Colegio;

public interface ColegioManager{
    /**
     * Retrieves all of the colegios
     */
public Map<String, Object> lista(Map<String, Object> params) ;
    /**
     * Gets colegio's information based on id.
     * @param id the colegio's id
     * @return colegio populated colegio object
     */
    public Colegio obtiene(final Long id)throws ObjectRetrievalFailureException;

    /**
     * Saves a colegio's information
     * @param colegio the object to be saved
     */
    public void crea(Colegio colegio);
    

    /**
     * Removes a colegio from the database by id
     * @param id the colegio's id
     */
    public String elimina(final Long id);
}

