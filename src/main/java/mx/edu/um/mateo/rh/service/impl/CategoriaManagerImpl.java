
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.CategoriaDao;
import mx.edu.um.mateo.rh.model.Categoria;
import mx.edu.um.mateo.rh.service.CategoriaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoriaManagerImpl extends BaseDao implements CategoriaManager {
    @Autowired
    private CategoriaDao dao;

    
    

    /**
     * @see mx.edu.um.rh.service.CategoriaManager#lista(mx.edu.um.rh.model.Categoria)
     */
    
    @Override
     public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * @see mx.edu.um.rh.service.CategoriaManager#obtiene(String id)
     */
    
    
    @Override
    public Categoria obtiene(final String id) {
        return dao.obtiene(new Integer(id));
    }

    /**
     * @see mx.edu.um.rh.service.CategoriaManager#graba(Categoria categoria)
     */
    @Override
    public void graba(Categoria categoria, Usuario usuario) {
        dao.graba(categoria, usuario);
    }

    /**
     * @see mx.edu.um.rh.service.CategoriaManager#elimina(String id)
     */
    @Override
    public String elimina(final String id) {
        String nombre = dao.elimina(new Integer(id));
        return nombre;
        
    }

   

  

   

  


        

   

    
}
