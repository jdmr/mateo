/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import mx.edu.um.mateo.rh.model.Categoria;

/**
 *
 * @author osoto
 */
public interface CategoriaDao {
   
    /**
     * Retrieves all of the Categorias
     */
    public List getCategorias(Categoria categoria);

    /**
     * Gets Categoria's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the Categoria's id
     * @return categoria populated Categoria object
     */
    public Categoria getCategoria(final Integer id);

    /**
     * Saves a Categoria's information
     * @param categoria the object to be saved
     */    
    public void saveCategoria(Categoria categoria);

    /**
     * Removes a categoria from the database by id
     * @param id the categoria's id
     */
    public void removeCategoria(final Integer id);
    
}
