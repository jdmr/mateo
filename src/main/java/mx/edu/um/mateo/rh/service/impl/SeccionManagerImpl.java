/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
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
	public Map<String, Object> getLista(Map<String, Object> params) {
		return seccionDao.Lista(params);
	}

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#getSeccion(String id)
	 */
	@Override
	public Seccion getSeccion(final Long id) throws ObjectRetrievalFailureException{
		return seccionDao.getSeccion(id);
	}

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#saveSeccion(Seccion seccion)
	 */
	@Override
	public void grabaSeccion(Seccion seccion) {
            seccionDao.grabaSeccion(seccion);
        }

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#removeSeccion(String id)
	 */
	@Override
	public void removeSeccion(final Long id) {
		seccionDao.removeSeccion(id);
	}
}
