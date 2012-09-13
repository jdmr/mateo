/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package mx.edu.um.mateo.rh.dao.impl;

import java.util.List;

import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.rh.dao.SeccionDao;
import mx.edu.um.mateo.rh.model.Puesto;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author osoto
 */
@Transactional
@Repository
public class SeccionDaoHibernate extends BaseDao implements SeccionDao {

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#getSeccions(mx.edu.um.rh.model.Puesto)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Puesto> getSecciones(final Puesto seccion) {
		return currentSession().createCriteria(Puesto.class).list();

	}

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#getSeccion(Integer id)
	 */
	@Override
	public Puesto getSeccion(final Integer id) {
		Puesto seccion = (Puesto) currentSession().get(Puesto.class, id);
		if (seccion == null) {
			log.warn("uh oh, seccion with id '" + id + "' not found...");
			throw new ObjectRetrievalFailureException(Puesto.class, id);
		}

		return seccion;
	}

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#saveSeccion(Puesto seccion)
	 */
	@Override
	public void saveSeccion(final Puesto seccion) {
		if (seccion.getId() == null) {
			getSession().save(seccion);
		} else {
			log.debug("{}", seccion);
			getSession().merge(seccion);
		}

	}

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#removeSeccion(Integer id)
	 */
	@Override
	public void removeSeccion(final Integer id) {
		currentSession().delete(getSeccion(id));
	}

}
