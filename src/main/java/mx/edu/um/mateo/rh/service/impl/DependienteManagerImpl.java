/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.DependienteDao;
import mx.edu.um.mateo.rh.model.Dependiente;
import mx.edu.um.mateo.rh.model.Nacionalidad;
import mx.edu.um.mateo.rh.service.DependienteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@Service
@Transactional
public class DependienteManagerImpl implements DependienteManager{
    @Autowired
    private DependienteDao dao;

     /**
     *@see mx.edu.um.mateo.rh.service.DependienteManager#lista(java.util.Map)   
     */
    
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.DependienteManager#obtiene(java.lang.Long)  
     */
    @Override
    public Dependiente obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    /**
     * @see mx.edu.um.mateo.rh.service.DependienteManager#graba(mx.edu.um.mateo.rh.model.Dependiente) 
     */
    
    @Override
    public void graba(Dependiente dependiente) {
         if(dependiente.getId()==null){
            dependiente.setStatus(Constantes.STATUS_ACTIVO);
        }
        dao.graba(dependiente);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.DependienteManager#elimina(java.lang.Long)  
     */
    
    
    @Override
    public String elimina(final Long id) {
        Dependiente dependiente= dao.obtiene(id);
        dependiente.setStatus(Constantes.STATUS_INACTIVO);
        dao.graba(dependiente);
        return dependiente.getNombre();
    }

   
    
}
