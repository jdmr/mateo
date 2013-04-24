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
import mx.edu.um.mateo.inscripciones.dao.AFEPlazaDao;
import mx.edu.um.mateo.inscripciones.model.AFEPlaza;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import mx.edu.um.mateo.inscripciones.model.Paquete;
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
public class AFEPlazaDaoHibernate extends BaseDao implements AFEPlazaDao {

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de AFE plazas con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Paquete.class);
        Criteria countCriteria = currentSession().createCriteria(Paquete.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("observaciones", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("tipoPlaza", filtro,
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
            criteria.addOrder(Order.asc("tipoPlaza"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONTAINSKEY_AFEPLAZAS, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public AFEPlaza obtiene(final Long id) {
        AFEPlaza afePlaza = (AFEPlaza) currentSession().get(AFEPlaza.class, id);
        if (afePlaza == null) {
            log.warn("uh oh, AFE Plaza with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(AFEPlaza.class, id);
        }

        return afePlaza;
    }

    @Override
    public void crea(final AFEPlaza afePlaza, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            afePlaza.setEmpresa(usuario.getEmpresa());
        }
        currentSession().save(afePlaza);
        currentSession().merge(afePlaza);
        currentSession().flush();

    }

    @Override
    public void actualiza(final AFEPlaza afePlaza, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            afePlaza.setEmpresa(usuario.getEmpresa());
        }
        try {
            currentSession().update(afePlaza);
        } catch (NonUniqueObjectException e) {
            try {
                currentSession().merge(afePlaza);
            } catch (Exception ex) {
                log.error("No se pudo actualizar la Plaza", ex);
                throw new RuntimeException("No se pudo actualizar la Plaza",
                        ex);
            }
        } finally {
            currentSession().flush();
        }
    }

    @Override
    public String elimina(Long id) {
        log.debug("Eliminando prorroga con id {}", id);
        AFEPlaza afePlaza = obtiene(id);
        afePlaza.setStatus("I");
        currentSession().saveOrUpdate(afePlaza);
        currentSession().merge(afePlaza);
        currentSession().flush();
        String tipoPlaza = afePlaza.getTipoPlaza();
        return tipoPlaza;
    }
}
