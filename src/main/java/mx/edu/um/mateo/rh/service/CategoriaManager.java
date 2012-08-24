/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.rh.model.Categoria;

/**
 *
 * @author osoto
 */
public interface CategoriaManager {
    /**
     * Retrieves all of the Categorias
     */
    public Map<String, Object> getCategorias(Categoria categorias);

    /**
     * Gets Categoria's information based on id.
     * @param id the Categoria's id
     * @return Categoria populated categoria object
     */
    public Categoria getCategoria(final String id);

    /**
     * Saves a Categoria's information
     * @param categoria the object to be saved
     */
    public void saveCategoria(Categoria categoria);

    /**
     * Removes a Categoria from the database by id
     * @param id the categoria's id
     */
    public void removeCategoria(final String id);
}
