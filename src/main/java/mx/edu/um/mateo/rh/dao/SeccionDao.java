/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.List;

import mx.edu.um.mateo.rh.model.Seccion;

/**
 * 
 * @author osoto
 */
public interface SeccionDao {
	/**
	 * Retrieves all of the seccions
	 */
	public List<Seccion> getSecciones(Seccion seccion);

	/**
	 * Gets seccion's information based on primary key. An
	 * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
	 * found.
	 * 
	 * @param id
	 *            the seccion's id
	 * @return seccion populated seccion object
	 */
	public Seccion getSeccion(final Integer id);

	/**
	 * Saves a seccion's information
	 * 
	 * @param seccion
	 *            the object to be saved
	 */
	public void saveSeccion(Seccion seccion);

	/**
	 * Removes a seccion from the database by id
	 * 
	 * @param id
	 *            the seccion's id
	 */
	public void removeSeccion(final Integer id);

}
