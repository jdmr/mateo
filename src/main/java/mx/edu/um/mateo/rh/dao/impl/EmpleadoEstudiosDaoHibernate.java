/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.rh.dao.EmpleadoEstudiosDao;
import mx.edu.um.mateo.rh.model.EmpleadoEstudios;
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
public class EmpleadoEstudiosDaoHibernate extends BaseDao implements EmpleadoEstudiosDao {
    
    @Override
	public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de EmpleadoEstudios con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(EmpleadoEstudios.class);
        Criteria countCriteria = currentSession().createCriteria(
                EmpleadoEstudios.class);

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombreEstudios", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("titulado", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("fechaTitulacion", filtro,
                    MatchMode.ANYWHERE));
             propiedades.add(Restrictions.ilike("nivelEstudios", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("userCaptura", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("fechaCaptura", filtro,
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
        params.put(Constantes.EMPLEADOESTUDIOS_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#getSeccion(Integer id)
	 */
	@Override
	public EmpleadoEstudios obtiene(final Long id) {
		EmpleadoEstudios empleadoEstudios = (EmpleadoEstudios) currentSession().get(EmpleadoEstudios.class, id);
		if (empleadoEstudios == null) {
			log.warn("uh oh, empleadoEstudios with id '" + id + "' not found...");
			throw new ObjectRetrievalFailureException(EmpleadoEstudios.class, id);
		}

		return empleadoEstudios;
	}

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#saveSeccion(Seccion seccion)
	 */
	@Override
	public void graba(final EmpleadoEstudios empleadoEstudios) {
		Session session = currentSession();
                session.saveOrUpdate(empleadoEstudios);
                session.merge(empleadoEstudios);
                session.flush();
                

	}

	/**
	 * @see mx.edu.um.rh.dao.SeccionDao#removeSeccion(Integer id)
	 */
	@Override
	public String elimina(final Long id) {
            EmpleadoEstudios empleadoEstudios = obtiene(id);
		currentSession().delete(empleadoEstudios);
                return empleadoEstudios.getNombreEstudios();
                
	}
    
}
