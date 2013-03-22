/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.dao.EmpleadoPuestoDao;
import mx.edu.um.mateo.rh.model.EmpleadoPuesto;
import mx.edu.um.mateo.rh.service.EmpleadoPuestoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author semdariobarbaamaya
 */
@Transactional
@Service
public class EmpleadoPuestoManagerImpl extends BaseDao implements EmpleadoPuestoManager{
    
    @Autowired
    private EmpleadoPuestoDao dao;
    
    /**
     *  @see mx.edu.um.mateo.rh.service.EmpleadoPuestoManager#lista(java.util.Map) 
     */
    
    @Override
     public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     *  @see mx.edu.um.mateo.rh.service.EmpleadoPuestoManager#obtiene(java.lang.String) 
     */
    
    @Override
    public EmpleadoPuesto obtiene(final Long id) throws ObjectRetrievalFailureException{
        return dao.obtiene(id);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoPuestoManager#graba(mx.edu.um.mateo.rh.model.EmpleadoPuesto, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void graba(EmpleadoPuesto empleadoPuesto, Usuario usuario) {
        if(empleadoPuesto.getId()==null){
            empleadoPuesto.setStatus(Constantes.STATUS_ACTIVO);
        }
        dao.graba(empleadoPuesto, usuario);
    }

    /**
     *  @see mx.edu.um.mateo.rh.service.EmpleadoPuestoManager#elimina(java.lang.String) 
     */
    @Override
    public String elimina(final Long id) {
        EmpleadoPuesto empleadoPuesto = dao.obtiene(new Long(id));
        empleadoPuesto.setStatus(Constantes.STATUS_INACTIVO);
        return empleadoPuesto.getPuesto().getDescripcion();
    }
}
