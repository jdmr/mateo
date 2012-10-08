/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.rh.model.Dependiente;


/**
 *
 * @author AMDA
 */
public interface DependienteDao {
    
    public Map<String, Object> lista(Map<String, Object> params);

	/**
	 * Gets dependiente's information based on primary key. An
	 * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
	 * found.
	 * 
	 * @param id
	 *            the dependiente's id
	 * @return dependiente populated dependiente object
	 */
	public Dependiente obtiene(final Long id);

	/**
	 * Saves a dependiente's information
	 * 
	 * @param dependiente
	 *            the object to be saved
	 */
	public void graba(Dependiente dependiente);

	/**
	 * Removes a dependiente from the database by id
	 * 
	 * @param id
	 *            the dependiente's id
	 */
	public String elimina(final Long id);
    
}
