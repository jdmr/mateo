/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.inventario.model.TipoProducto;
import mx.edu.um.mateo.rh.dao.DependienteDao;
import mx.edu.um.mateo.rh.dao.SeccionDao;
import mx.edu.um.mateo.rh.model.Dependiente;
import mx.edu.um.mateo.rh.model.Seccion;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author AMDA
 */
@Transactional
@Repository
public class DependienteDaoHibernate extends BaseDao implements DependienteDao {

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#getSeccions(mx.edu.um.rh.model.Seccion)
	 */
	
	@Override
	public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de dependiente con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Dependiente.class);
        Criteria countCriteria = currentSession().createCriteria(
                Dependiente.class);

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("fechaNacimiento", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("estudios", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("grado", filtro,
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
        params.put(Constantes.DEPENDIENTE_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#getSeccion(Integer id)
	 */
	@Override
	public Dependiente obtiene(final Long id) {
		Dependiente dependiente = (Dependiente) currentSession().get(Dependiente.class, id);
		if (dependiente == null) {
			log.warn("uh oh, dependiente with id '" + id + "' not found...");
			throw new ObjectRetrievalFailureException(Dependiente.class, id);
		}

		return dependiente;
	}

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#saveSeccion(Seccion seccion)
	 */
	@Override
	public void graba(final Dependiente dependiente) {
		Session session = currentSession();
                session.saveOrUpdate(dependiente);
                session.merge(dependiente);
                session.flush();
                

	}

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#removeSeccion(Integer id)
	 */
	@Override
	public String elimina(final Long id) {
            Dependiente dependiente = obtiene(id);
		currentSession().delete(dependiente);
                return dependiente.getNombre();
                
	}

}

