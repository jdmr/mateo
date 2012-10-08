/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.Constants;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.rh.dao.DependienteDao;
import mx.edu.um.mateo.rh.dao.SeccionDao;
import mx.edu.um.mateo.rh.model.Dependiente;
import mx.edu.um.mateo.rh.model.Seccion;
import mx.edu.um.mateo.rh.service.DependienteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author AMDA
 */
@Service
@Transactional

public class DependienteManagerImpl extends BaseDao implements DependienteManager {
    
     @Autowired 
    private DependienteDao dao;

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#getSeccions(mx.edu.um.rh.model.Seccion)
	 */
	@Override
	public Map<String, Object> lista (Map<String, Object>params) {
		
		return dao.lista(params);
                
	}

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#getSeccion(String id)
	 */
	@Override
	public Dependiente obtiene(final String id) {
		return dao.obtiene(new Long(id));
	}

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#saveSeccion(Seccion seccion)
	 */
	@Override
	public void graba(Dependiente dependiente) {
            if(dependiente.getId() == null){
                dependiente.setStatus(Constantes.STATUS_ACTIVO);
            }
		dao.graba(dependiente);
	}

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#removeSeccion(String id)
	 */
	@Override
	public String elimina(final String id) {
		String nombre = dao.elimina(new Long(id));
                return nombre;
	}

}
