/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
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
    public EmpleadoPuesto obtiene(final String id) {
        return dao.obtiene(new Long(id));
    }

    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoPuestoManager#graba(mx.edu.um.mateo.rh.model.EmpleadoPuesto, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void graba(EmpleadoPuesto empleadopuesto, Usuario usuario) {
        if(empleadopuesto.getId()==null){
            empleadopuesto.setStatus(Constantes.STATUS_ACTIVO);
        }
        dao.graba(empleadopuesto, usuario);
    }

    /**
     *  @see mx.edu.um.mateo.rh.service.EmpleadoPuestoManager#elimina(java.lang.String) 
     */
    @Override
    public String elimina(final Long id) {
        EmpleadoPuesto empleadopuesto = dao.obtiene(new Long(id));
        empleadopuesto.setStatus(Constantes.STATUS_INACTIVO);
        return empleadopuesto.getPuesto().getDescripcion();
    }
}
