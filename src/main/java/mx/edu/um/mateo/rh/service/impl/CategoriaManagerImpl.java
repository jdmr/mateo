
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.CategoriaDao;
import mx.edu.um.mateo.rh.model.Categoria;
import mx.edu.um.mateo.rh.service.CategoriaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class CategoriaManagerImpl extends BaseDao implements CategoriaManager {
    @Autowired
    private CategoriaDao dao;

    
    

    /**
     *  @see mx.edu.um.mateo.rh.service.CategoriaManager#lista(java.util.Map) 
     */
    
    @Override
     public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     *  @see mx.edu.um.mateo.rh.service.CategoriaManager#obtiene(java.lang.String) 
     */
    
    
    @Override
    public Categoria obtiene(final String id) {
        return dao.obtiene(new Integer(id));
    }

    /**
     * @see mx.edu.um.mateo.rh.service.CategoriaManager#graba(mx.edu.um.mateo.rh.model.Categoria, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void graba(Categoria categoria, Usuario usuario) {
        if(categoria.getId()==null){
            categoria.setStatus(Constantes.STATUS_ACTIVO);
        }
        dao.graba(categoria, usuario);
    }

    /**
     *  @see mx.edu.um.mateo.rh.service.CategoriaManager#elimina(java.lang.String) 
     */
    @Override
    public String elimina(final String id) {
        String nombre = dao.elimina(new Integer(id));
        return nombre;
        
    }

   

  

   

  


        

   

    
}
