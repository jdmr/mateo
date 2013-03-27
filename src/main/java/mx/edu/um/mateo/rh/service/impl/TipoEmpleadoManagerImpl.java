/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.TipoEmpleadoDao;
import mx.edu.um.mateo.rh.model.TipoEmpleado;
import mx.edu.um.mateo.rh.service.TipoEmpleadoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@Service
@Transactional
public class TipoEmpleadoManagerImpl implements TipoEmpleadoManager{
    @Autowired
    private TipoEmpleadoDao dao;

     /**
     *@see mx.edu.um.mateo.rh.service.TipoEmpleadoManager#lista(java.util.Map)   
     */
    
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.TipoEmpleadoManager#obtiene(java.lang.Long)  
     */
    @Override
    public TipoEmpleado obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    /**
     * @see mx.edu.um.mateo.rh.service.TipoEmpleadoManager#graba(mx.edu.um.mateo.rh.model.TipoEmpleado) 
     */
    @Override
    public TipoEmpleado crea(TipoEmpleado tipoEmpleado, Usuario usuario) {
        return dao.crea(tipoEmpleado, usuario);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.TipoEmpleadoManager#elimina(java.lang.Long)  
     */
    @Override
    public String elimina(final Long id) {
        return dao.elimina(id);
    }

   
    
}
