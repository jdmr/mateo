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
import mx.edu.um.mateo.inscripciones.dao.AFEBecaAdicionalDao;
import mx.edu.um.mateo.inscripciones.model.AFEBecaAdicional;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
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
 * @author develop
 */
@Repository
@Transactional
public class AFEBecaAdicionalDaoHibernate extends BaseDao implements AFEBecaAdicionalDao {

    /**
     * @see
     * mx.edu.um.mateo.inscripciones.dao.AFEBecaAdicionalDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de AFEBecaAdicional con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(AFEBecaAdicional.class);
        Criteria countCriteria = currentSession().createCriteria(AFEBecaAdicional.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("matricula", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("importe", filtro,
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
            criteria.addOrder(Order.asc("matricula"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONTAINSKEY_BECASADICIONALES, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see
     * mx.edu.um.mateo.inscripciones.dao.AFEBecaAdicionalDao#obtiene(java.lang.Integer)
     */
    @Override
    public AFEBecaAdicional obtiene(final Long id) {
        AFEBecaAdicional becaAdicional = (AFEBecaAdicional) currentSession().get(AFEBecaAdicional.class, id);
        if (becaAdicional == null) {
            log.warn("uh oh, tipoBeca with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(AFEBecaAdicional.class, id);
        }

        return becaAdicional;
    }

    /**
     * @see
     * mx.edu.um.mateo.inscripciones.dao.AFEBecaAdicionalDao#crea(mx.edu.um.mateo.inscripciones.model.AFEBecaAdicional,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void crea(final AFEBecaAdicional becaAdicional, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            becaAdicional.setEmpresa(usuario.getEmpresa());
        }
        currentSession().save(becaAdicional);
        currentSession().merge(becaAdicional);
        currentSession().flush();

    }

    /**
     * @see
     * mx.edu.um.mateo.inscripciones.dao.AFEBecaAdicionalDao#actualiza(mx.edu.um.mateo.inscripciones.model.AFEBecaAdicional,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void actualiza(final AFEBecaAdicional becaAdicional, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            becaAdicional.setEmpresa(usuario.getEmpresa());
        }
        try {
            currentSession().update(becaAdicional);
        } catch (NonUniqueObjectException e) {
            try {
                currentSession().merge(becaAdicional);
            } catch (Exception ex) {
                log.error("No se pudo actualizar la becaAdicional", ex);
                throw new RuntimeException("No se pudo actualizar la becaAdicional",
                        ex);
            }
        } finally {
            currentSession().flush();
        }

    }

    /**
     * @see
     * mx.edu.um.mateo.inscripciones.dao.AFEBecaAdicionalDao#elimina(java.lang.Integer)
     */
    @Override
    public String elimina(final Long id) {
        AFEBecaAdicional becaAdicional = obtiene(id);
        becaAdicional.setStatus("I");
        currentSession().saveOrUpdate(becaAdicional);
        currentSession().merge(becaAdicional);
        currentSession().flush();
        String matricula = becaAdicional.getMatricula();
        return matricula;

    }
}
