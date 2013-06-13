/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeEmpleadoDetalleDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
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
public class InformeEmpleadoDetalleDaoHibernate extends BaseDao implements InformeEmpleadoDetalleDao {

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
        Criteria criteria = currentSession().createCriteria(InformeEmpleadoDetalle.class);
        Criteria countCriteria = currentSession().createCriteria(InformeEmpleadoDetalle.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombreProveedor", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("folioFactura", filtro,
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
            criteria.addOrder(Order.asc("folioFactura"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONTAINSKEY_INFORMESDETALLES, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public InformeEmpleadoDetalle obtiene(final Long id) {
        InformeEmpleadoDetalle detalle = (InformeEmpleadoDetalle) currentSession().get(InformeEmpleadoDetalle.class, id);
        if (detalle == null) {
            log.warn("uh oh, Informe Empleado Detalle with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(InformeEmpleadoDetalle.class, id);
        }
        return detalle;
    }

    @Override
    public void crea(final InformeEmpleadoDetalle detalle, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            detalle.setEmpresa(usuario.getEmpresa());
        }
        currentSession().save(detalle);
        currentSession().merge(detalle);
        currentSession().flush();
    }

    @Override
    public void actualiza(final InformeEmpleadoDetalle detalle, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            detalle.setEmpresa(usuario.getEmpresa());
        }
        try {
            currentSession().update(detalle);
        } catch (NonUniqueObjectException e) {
            try {
                currentSession().merge(detalle);
            } catch (Exception ex) {
                log.error("No se pudo actualizar el informe", ex);
                throw new RuntimeException("No se pudo actualizar el informe",
                        ex);
            }
        } finally {
            currentSession().flush();
        }
    }

    @Override
    public String elimina(Long id) {
        log.debug("Eliminando prorroga con id {}", id);
        InformeEmpleadoDetalle detalle = obtiene(id);
        detalle.setStatus("I");
        currentSession().saveOrUpdate(detalle);
        currentSession().merge(detalle);
        currentSession().flush();
        String proveedor = detalle.getNombreProveedor();
        return proveedor;
    }
}
