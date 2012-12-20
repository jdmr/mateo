/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.rh.model.EmpleadoEstudios;

/**
 *
 * @author AMDA
 */
public interface EmpleadoEstudiosDao {
    public Map<String, Object> lista(Map<String, Object> params);

	/**
	 * Gets EmpleadoEstudios's information based on primary key. An
	 * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
	 * found.
	 * 
	 * @param id
	 *            the EmpleadoEstudios's id
	 * @return dependiente populated dependiente object
	 */
	public EmpleadoEstudios obtiene(final Long id);

	/**
	 * Saves a EmpleadoEstudios's information
	 * 
	 * @param empleadoEstudios
	 *            the object to be saved
	 */
	public void graba(EmpleadoEstudios empleadoEstudios);

	/**
	 * Removes a EmpleadoEstudios from the database by id
	 * 
	 * @param id
	 *            the dependiente's id
	 */
	public String elimina(final Long id);
    
}
    

