/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeEmpleadoDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
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
public class InformeEmpleadoDaoHibernate extends BaseDao implements InformeEmpleadoDao {

    /**
     * @see
     * mx.edu.um.mateo.inscripciones.dao.InformeEmpleadoDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
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
        Criteria criteria = currentSession().createCriteria(InformeEmpleado.class);
        Criteria countCriteria = currentSession().createCriteria(InformeEmpleado.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("numNomina", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("informe", filtro,
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
            criteria.addOrder(Order.asc("numNomina"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONTAINSKEY_INFORMESEMPLEADO, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see
     * mx.edu.um.mateo.inscripciones.dao.InformeEmpleadoDao#obtiene(java.lang.Integer)
     */
    @Override
    public InformeEmpleado obtiene(final Long id) {
        InformeEmpleado informe = (InformeEmpleado) currentSession().get(InformeEmpleado.class, id);
        if (informe == null) {
            log.warn("uh oh, tipoBeca with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(InformeEmpleado.class, id);
        }

        return informe;
    }

    /**
     * @see
     * mx.edu.um.mateo.inscripciones.dao.InformeEmpleadoDao#crea(mx.edu.um.mateo.inscripciones.model.InformeEmpleado,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void crea(final InformeEmpleado informe, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            informe.setEmpresa(usuario.getEmpresa());
        }
        currentSession().save(informe);
        currentSession().merge(informe);
        currentSession().flush();

    }

    /**
     * @see
     * mx.edu.um.mateo.inscripciones.dao.InformeEmpleadoDao#actualiza(mx.edu.um.mateo.inscripciones.model.InformeEmpleado,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void actualiza(final InformeEmpleado informe, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            informe.setEmpresa(usuario.getEmpresa());
        }
        try {
            currentSession().update(informe);
        } catch (NonUniqueObjectException e) {
            try {
                currentSession().merge(informe);
            } catch (Exception ex) {
                log.error("No se pudo actualizar el informe", ex);
                throw new RuntimeException("No se pudo actualizar el informe",
                        ex);
            }
        } finally {
            currentSession().flush();
        }

    }

    /**
     * @see mx.edu.um.mateo.rh.dao.InformeEmpleadoDao#elimina(java.lang.Long)
     */
    @Override
    public String elimina(Long id) {
        log.debug("Eliminando prorroga con id {}", id);
        InformeEmpleado informe = obtiene(id);
        informe.setStatus("I");
        currentSession().saveOrUpdate(informe);
        currentSession().merge(informe);
        currentSession().flush();
        String nomina = informe.getNumNomina();
        return nomina;
    }
}
