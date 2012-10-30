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

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constants;

import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.PuestoDao;
import mx.edu.um.mateo.rh.model.Puesto;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author osoto
 */
@Transactional
@Repository
public class PuestoDaoHibernate extends BaseDao implements PuestoDao {

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de puestos con params {}", params);
		if (params == null) {
			params = new HashMap<>();
		}

		if (!params.containsKey("max")) {
			params.put("max", 10);
		} else {
			params.put("max", Math.min((Integer) params.get("max"), 100));
		}

		if (params.containsKey("pagina")) {
			Long pagina = (Long) params.get("pagina");
			Long offset = (pagina - 1) * (Integer) params.get("max");
			params.put("offset", offset.intValue());
		}

		if (!params.containsKey("offset")) {
			params.put("offset", 0);
		}
		Criteria criteria = currentSession().createCriteria(Puesto.class);
		Criteria countCriteria = currentSession().createCriteria(
				Puesto.class);

		if (params.containsKey("empresa")) {
			criteria.createCriteria("empresa").add(
					Restrictions.idEq(params.get("empresa")));
			countCriteria.createCriteria("empresa").add(
					Restrictions.idEq(params.get("empresa")));
		}
                if (params.containsKey("seccion")) {
			criteria.createCriteria("seccion").add(
					Restrictions.idEq(params.get("seccion")));
			countCriteria.createCriteria("seccion").add(
					Restrictions.idEq(params.get("seccion")));
		}


		if (params.containsKey("filtro")) {
			String filtro = (String) params.get("filtro");
			Disjunction propiedades = Restrictions.disjunction();
			propiedades.add(Restrictions.ilike("nombre", filtro,
					MatchMode.ANYWHERE));
			propiedades.add(Restrictions.ilike("nombreCompleto", filtro,
					MatchMode.ANYWHERE));
			propiedades.add(Restrictions.ilike("rfc", filtro,
					MatchMode.ANYWHERE));
			propiedades.add(Restrictions.ilike("correo", filtro,
					MatchMode.ANYWHERE));
			propiedades.add(Restrictions.ilike("contacto", filtro,
					MatchMode.ANYWHERE));
			criteria.add(propiedades);
			countCriteria.add(propiedades);
		}

		if (params.containsKey("order")) {
			String campo = (String) params.get("order");
			if (params.get("sort").equals("desc")) {
				criteria.addOrder(Order.desc(campo));
			} else {
				criteria.addOrder(Order.asc(campo));
			}
		}

		if (!params.containsKey("reporte")) {
			criteria.setFirstResult((Integer) params.get("offset"));
			criteria.setMaxResults((Integer) params.get("max"));
		}
		params.put(Constants.PUESTO_LIST, criteria.list());

		countCriteria.setProjection(Projections.rowCount());
		params.put("cantidad", (Long) countCriteria.list().get(0));

		return params;
    }

    @Override
    public Puesto obtiene(Long id) {
        Puesto puesto = (Puesto) currentSession().get(Puesto.class, id);
		return puesto;
    }

    @Override
    public Puesto graba(Puesto puesto, Usuario usuario) {
         Session session = currentSession();
		if (usuario != null) {
			puesto.setEmpresa(usuario.getEmpresa());
		}
		session.saveOrUpdate(puesto);
                session.merge(puesto);
		session.flush();
		return puesto;
    }


    @Override
    public String elimina(Long id) {
        Puesto puesto = obtiene(id);
		String descripcion = puesto.getDescripcion();
		currentSession().delete(puesto);
		currentSession().flush();
		return descripcion;
    }

	

}
