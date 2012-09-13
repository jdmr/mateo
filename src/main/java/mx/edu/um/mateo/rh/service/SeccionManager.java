/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;

import mx.edu.um.mateo.rh.model.Puesto;

/**
 * 
 * @author osoto
 */
public interface SeccionManager {
	/**
	 * Retrieves all of the seccions
	 */
	public Map<String, Object> getSecciones(Puesto seccion);

	/**
	 * Gets seccion's information based on id.
	 * 
	 * @param id
	 *            the seccion's id
	 * @return seccion populated seccion object
	 */
	public Puesto getSeccion(final String id);

	/**
	 * Saves a seccion's information
	 * 
	 * @param seccion
	 *            the object to be saved
	 */
	public void saveSeccion(Puesto seccion);

	/**
	 * Removes a seccion from the database by id
	 * 
	 * @param id
	 *            the seccion's id
	 */
	public void removeSeccion(final String id);
}
