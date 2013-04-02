/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.CobroCampoDao;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
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
public class CobroCampoDaoHibernate extends BaseDao implements CobroCampoDao {

    /**
     * @see mx.edu.um.mateo.rh.dao.CobroCampoDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de cobros a campos con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(CobroCampo.class)
                .setFetchMode("usuario", FetchMode.SELECT);
        Criteria countCriteria = currentSession().createCriteria(CobroCampo.class)
                .setFetchMode("usuario", FetchMode.SELECT);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("institucion", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("matricula", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.eq("fecha", filtro));
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
            criteria.addOrder(Order.asc("matricula"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONTAINSKEY_COBROSCAMPOS, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.CobroCampoDao#obtiene(java.lang.Long)
     */
    @Override
    public CobroCampo obtiene(Long id) {
        log.debug("Obtiene cobro a campo con id = {}", id);
        CobroCampo cobroCampo = (CobroCampo) currentSession().get(CobroCampo.class, id);
        if (cobroCampo == null) {
            log.warn("uh oh, la cobro a campo con el id" + id + "no se encontro...");
            throw new ObjectRetrievalFailureException(CobroCampo.class, id);

        }
        return cobroCampo;
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.CobroCampoDao#graba(mx.edu.um.mateo.rh.model.Prorroga,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void graba(final CobroCampo cobroCampo, Usuario usuario) {
        if (usuario != null) {
            cobroCampo.setEmpresa(usuario.getEmpresa());
        }
        currentSession().saveOrUpdate(cobroCampo);
        currentSession().merge(cobroCampo);
        currentSession().flush();


    }

    /**
     * @see mx.edu.um.mateo.rh.dao.CobroCampoDao#elimina(java.lang.Long)
     */
    @Override
    public String elimina(Long id) {
        log.debug("Eliminando prorroga con id {}", id);
        CobroCampo cobroCampo = obtiene(id);
        cobroCampo.setStatus("I");
        currentSession().saveOrUpdate(cobroCampo);
        currentSession().merge(cobroCampo);
        currentSession().flush();
        String matricula = cobroCampo.getMatricula();
        return matricula;
    }
}
