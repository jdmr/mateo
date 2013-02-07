/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.inscripciones.dao.TipoBecasDao;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import mx.edu.um.mateo.rh.model.Colegio;
import org.hibernate.Criteria;
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
 * @author develop
 */
@Repository
@Transactional
public class TiposBecasDaoHibernate extends BaseDao implements TipoBecasDao{


    /**
     * @see mx.edu.um.afe.dao.TipoAFEBecaDao#getTiposBeca(mx.edu.um.afe.model.TipoAFEBeca)
     */
//    @Override
//    public List getTiposBeca(final TiposBecas tipoBeca) {
//        return getHibernateTemplate().find("from TipoAFEBeca");
//
//    }
 @Override
    public Map<String, Object> getTiposBeca(Map<String, Object> params) {
        log.debug("Buscando lista de Tipos de Becas con params {}", params);
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

        if (params.containsKey("tiposBeca")) {
            criteria.createCriteria("tiposBeca").add(
                    Restrictions.idEq(params.get("tiposBeca")));
            countCriteria.createCriteria("tiposBeca").add(
                    Restrictions.idEq(params.get("tiposBeca")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("descripcion", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("tope", filtro,
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
            criteria.addOrder(Order.asc("descripcion"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("tiposBecas", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.afe.dao.TipoAFEBecaDao#getTipoBeca(Integer id)
     */
    @Override
    public TiposBecas getTipoBeca(final Integer id) {
        TiposBecas tipoBeca =  (TiposBecas) currentSession().get(TiposBecas.class, id);
        if (tipoBeca == null) {
            //log.warn("uh oh, tipoBeca with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(TiposBecas.class, id);
        }

        return tipoBeca;
    }

    /**
     * @see mx.edu.um.afe.dao.TipoAFEBecaDao#saveTipoBeca(TipoAFEBeca tipoBeca)
     */    
    @Override
    public void saveTipoBeca(final TiposBecas tipoBeca) {
        currentSession().saveOrUpdate(tipoBeca);
    }

    /**
     * @see mx.edu.um.afe.dao.TipoAFEBecaDao#removeTipoBeca(Integer id)
     */
    @Override
    public void removeTipoBeca(final Integer id) {
       TiposBecas tiposBecas = this.getTipoBeca(id);
        currentSession().delete(tiposBecas);
        currentSession().flush();
        
    }


}