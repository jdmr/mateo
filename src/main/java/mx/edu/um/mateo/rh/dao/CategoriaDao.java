
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
     * Guarda la informacion de la categoria
     * @param categoria el objeto que sera guardado
     */    
    public void graba(Categoria categoria, Usuario usuario);

    /**
     * Borra la categoria de la base de datos mediante el id
     * @param id id de la categoria
     */
    public String elimina(final Integer id);

}

