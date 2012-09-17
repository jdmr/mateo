
package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.Categoria;

public interface CategoriaDao{

    /**
     * Regresa una lista de categorias.
     * @param categoria
     * @return 
     */
    public Map<String, Object> lista (Map<String, Object> params);

    /**
     * Obtiene una categoria
     * @param id
     * @return 
     */
    public Categoria obtiene(final Integer id);

    /**
     * Saves a categoria's information
     * @param categoria the object to be saved
     */    
    public void graba(Categoria categoria, Usuario usuario);

    /**
     * Removes a categoria from the database by id
     * @param id the categoria's id
     */
    public String elimina(final Integer id);

}

