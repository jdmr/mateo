/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;

import mx.edu.um.mateo.rh.model.Seccion;

/**
 * 
 * @author osoto
 */
public interface SeccionManager {
	/**
	 * Retrieves all of the seccions
	 */
	public Map<String, Object> Lista(Map<String, Object> params);

	/**
	 * Gets seccion's information based on id.
	 * 
	 * @param id
	 *            the seccion's id
	 * @return seccion populated seccion object
	 */
	public Seccion Obtiene(final Long id)throws ObjectRetrievalFailureException;

	/**
	 * Saves a seccion's information
	 * 
	 * @param seccion
	 *            the object to be saved
	 */
	public void graba(Seccion seccion);

	/**
	 * Removes a seccion from the database by id
	 * 
	 * @param id
	 *            the seccion's id
	 */
	public void elimina(final Long id);
}
