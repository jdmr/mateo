package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.rh.dao.ColegioDao;
import mx.edu.um.mateo.rh.model.Colegio;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ColegioDaoHibernate extends BaseDao implements ColegioDao {

    /**
     * @see mx.edu.um.rh.dao.ColegioDao#getColegios(mx.edu.um.rh.model.Colegio)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de colegios con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Colegio.class);
        Criteria countCriteria = currentSession().createCriteria(Colegio.class);

        if (params.containsKey("colegio")) {
            criteria.createCriteria("colegio").add(
                    Restrictions.idEq(params.get("colegio")));
            countCriteria.createCriteria("colegio").add(
                    Restrictions.idEq(params.get("colegio")));
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
        params.put("colegios", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /*
     * Remove the line above and uncomment this code block if you want to use
     * Hibernate's Query by Example API. if (colegio == null) { return
     * getHibernateTemplate().find("from Colegio"); } else { // filter on
     * properties set in the colegio HibernateCallback callback = new
     * HibernateCallback() { public Object doInHibernate(Session session) throws
     * HibernateException { Example ex =
     * Example.create(colegio).ignoreCase().enableLike(MatchMode.ANYWHERE);
     * return session.createCriteria(Colegio.class).add(ex).list(); } }; return
     * (List) getHibernateTemplate().execute(callback);
        }
     */
    /**
     * @see mx.edu.um.rh.dao.ColegioDao#getColegio(Integer id)
     */

    @Override
    public Colegio getColegio(final Long id) {
        Colegio colegio = (Colegio) currentSession().get(Colegio.class, id);
        return colegio;
    }

    /**
     * @see mx.edu.um.rh.dao.ColegioDao#saveColegio(Colegio colegio)
     */
    @Override
    public void saveColegio(final Colegio colegio) {
        currentSession().save(colegio);
    }

    /**
     * @see mx.edu.um.rh.dao.ColegioDao#removeColegio(Integer id)
     */
    @Override
    public void removeColegio(final Long id) {
        Colegio colegio = getColegio(id);
        currentSession().delete(colegio);
                
    }
}
