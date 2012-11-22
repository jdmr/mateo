/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.EmpleadoEstudiosDao;
import mx.edu.um.mateo.rh.model.Categoria;
import mx.edu.um.mateo.rh.model.EmpleadoEstudios;
import mx.edu.um.mateo.rh.service.EmpleadoEstudiosManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author AMDA
 */

@Transactional
@Service
public class EmpleadoEstudiosManagerImpl extends BaseDao implements EmpleadoEstudiosManager {
    
     @Autowired
    private EmpleadoEstudiosDao dao;

    
    

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
    public EmpleadoEstudios obtiene(final String id) {
        return dao.obtiene(new Long(id));
    }

    /**
     * @see mx.edu.um.mateo.rh.service.CategoriaManager#graba(mx.edu.um.mateo.rh.model.Categoria, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void graba(EmpleadoEstudios empleadoEstudios) {
        if(empleadoEstudios.getId()==null){
            empleadoEstudios.setStatus(Constantes.STATUS_ACTIVO);
        }
        dao.graba(empleadoEstudios);
    }

    /**
     *  @see mx.edu.um.mateo.rh.service.CategoriaManager#elimina(java.lang.String) 
     */
    @Override
    public String elimina(final Long id) {
        EmpleadoEstudios empleadoEstudios = dao.obtiene(new Long(id));
        empleadoEstudios.setStatus(Constantes.STATUS_INACTIVO);
         return empleadoEstudios.getNombreEstudios();
        
        
    }

   
    
}
