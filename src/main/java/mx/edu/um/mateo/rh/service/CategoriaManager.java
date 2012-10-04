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
     * Regresa todas las categorias
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene las categorias basandose en su id
     * @param id el id de la categoria
     * @return regresa la categoria que obtuvo mediante el id
     */
    public Categoria obtiene(final String id);

    /**
     * Graba la informacion de la categoria
     * @param categoria el objeto que sera grabado
     */
    public void graba(Categoria categoria, Usuario usuario);

    /**
     * Elimina la categoria de la base de datos mediante su id
     * @param id el id de la categoria
     */
    public String elimina(final String id);
}
 
