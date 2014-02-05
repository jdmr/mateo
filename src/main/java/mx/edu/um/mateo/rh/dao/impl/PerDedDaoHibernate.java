/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.PerDedDao;
import mx.edu.um.mateo.rh.model.PerDed;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author semdariobarbaamaya
 */
@Repository
@Transactional
public class PerDedDaoHibernate extends BaseDao implements PerDedDao {

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de perded con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(PerDed.class).add(Restrictions.eq("status", Constantes.STATUS_ACTIVO));
        Criteria countCriteria = currentSession().createCriteria(PerDed.class).add(Restrictions.eq("status", Constantes.STATUS_ACTIVO));


        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("clave", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("status", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.PERDED_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.service.PerDedManager#obtiene(java.lang.String)
     */
    @Override
    public PerDed obtiene(final Long id) {
        PerDed perded = (PerDed) currentSession().get(PerDed.class, id);
        if (perded == null) {
            log.warn("uh oh, perded with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(PerDed.class, id);
        }

        return perded;
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.service.PerDedManager#graba(mx.edu.um.mateo.rh.model.PerDed,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void graba(final PerDed perded, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            perded.setEmpresa(usuario.getEmpresa());
        }
        currentSession().saveOrUpdate(perded);
        currentSession().merge(perded);
        currentSession().flush();

    }

    /**
     * @see mx.edu.um.mateo.rh.service.PerDedManager#elimina(java.lang.String)
     */
    @Override
    public String elimina(final Long id) {
        log.debug("Eliminando la perded {}", id);
        PerDed perded = this.obtiene(id);
        String nombre = perded.getNombre();
        currentSession().delete(perded);

        currentSession().flush();

        return nombre;


    }
}
