/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.rh.dao.SeccionDao;
import mx.edu.um.mateo.rh.model.Seccion;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author IrasemaBalderas
 */
public class SeccionDaoHibernate extends BaseDao implements SeccionDao{

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> Lista(Map<String, Object> params) {
        log.debug("Buscando lista de secciones con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Seccion.class);
        Criteria countCriteria = currentSession().createCriteria(Seccion.class);

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
        } else {
            criteria.addOrder(Order.asc("nombre"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("secciones", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.rh.dao.SeccionDao#getSeccion(Integer id)
     */
    @Override
    @Transactional(readOnly = true)
    public Seccion obtiene(final Long id) {
        Seccion seccion = (Seccion) currentSession().get(Seccion.class, id);
        if (seccion == null) {
            log.warn("uh oh, seccion with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Seccion.class, id);
        }

        return seccion;
    }

    /**
     * @see mx.edu.um.rh.dao.SeccionDao#saveSeccion(Seccion seccion)
     */
    @Override
    public void graba(final Seccion seccion) {
        if (seccion.getId() == null) {
            currentSession().saveOrUpdate(seccion);
        } else {
            log.debug("{}", seccion);
            getSession().merge(seccion);
        }
    }
        
        @SuppressWarnings("unchecked")
        @Transactional(readOnly = true)
        @Override
        public void elimina(final Long id) {
        currentSession().delete(obtiene(id));
        }
    }