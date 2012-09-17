/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.Categoria;

/**
 *
 * @author zorch
 */
public interface CategoriaManager {
    /**
     * Retrieves all of the Categorias
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Gets Categoria's information based on id.
     * @param id the Categoria's id
     * @return Categoria populated categoria object
     */
    public Categoria obtiene(final String id);

    /**
     * Saves a Categoria's information
     * @param categoria the object to be saved
     */
    public void graba(Categoria categoria, Usuario usuario);

    /**
     * Removes a Categoria from the database by id
     * @param id the categoria's id
     */
    public String elimina(final String id);
}
 
