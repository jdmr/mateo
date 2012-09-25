/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.HashMap;
import java.util.Map;

import mx.edu.um.mateo.Constants;
import mx.edu.um.mateo.rh.dao.SeccionDao;
import mx.edu.um.mateo.rh.model.Seccion;
import mx.edu.um.mateo.rh.service.SeccionManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author osoto
 */
@Service
@Transactional
public class SeccionManagerImpl implements SeccionManager {

	private SeccionDao dao;

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#getSeccions(mx.edu.um.rh.model.Seccion)
	 */
	@Override
	public Map<String, Object> getSecciones(final Seccion seccion) {
		Map<String, Object> params = new HashMap<>();
		params.put(Constants.SECCION_LIST, dao.getSecciones(seccion));
		return params;
	}

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#getSeccion(String id)
	 */
	@Override
	public Seccion getSeccion(final String id) {
		return dao.getSeccion(new Integer(id));
	}

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#saveSeccion(Seccion seccion)
	 */
	@Override
	public void saveSeccion(Seccion seccion) {
		dao.saveSeccion(seccion);
	}

	/**
	 * @see mx.edu.um.rh.service.SeccionManager#removeSeccion(String id)
	 */
	@Override
	public void removeSeccion(final String id) {
		dao.removeSeccion(new Integer(id));
	}
}
