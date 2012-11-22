/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.dao.SeccionDao;
import mx.edu.um.mateo.rh.model.Seccion;
import mx.edu.um.mateo.rh.service.SeccionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author osoto
 */

@Service
@Transactional
public class SeccionManagerImpl implements SeccionManager {
        @Autowired
	private SeccionDao seccionDao;

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#getSeccions(mx.edu.um.rh.model.Seccion)
	 */
	@Override
        @Transactional(readOnly = true)
	public Map<String, Object> Lista(Map<String, Object> params) {
		return seccionDao.Lista(params);
	}

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#getSeccion(String id)
	 */
	@Override
        @Transactional(readOnly = true)
	public Seccion Obtiene(final Long id) throws ObjectRetrievalFailureException{
		return seccionDao.obtiene(id);
	}

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#saveSeccion(Seccion seccion)
	 */
	@Override
	public void graba(Seccion seccion) {
            seccionDao.graba(seccion);
        }

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#removeSeccion(String id)
	 */
	@Override
//        @Transactional(readOnly = true)
	public void elimina(final Long id) {
		seccionDao.elimina(id);
	}
}
