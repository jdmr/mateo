/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.dao;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.model.OrdenPago;
import mx.edu.um.mateo.general.model.Usuario;
/**
 *
 * @author osoto
 */
public interface OrdenPagoDao {
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets ordePago's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the ordePago's id
     * @return ordePago populated ordePago object
     */
    public OrdenPago obtiene(final Long id);
    
    /**
     * Saves a ordePago's information
     * @param ordePago the object to be saved
     */    
    public void crea(OrdenPago ordePago, Usuario usuario);
    /**
     * Actualiza un ordePago
     * @param ordePago
     * @param usuario 
     */
    public void actualiza(OrdenPago ordePago, Usuario usuario);

    /**
     * Removes a ordePago from the database by id
     * @param id the ordePago's id
     */
    public String elimina(final Long id);
}
