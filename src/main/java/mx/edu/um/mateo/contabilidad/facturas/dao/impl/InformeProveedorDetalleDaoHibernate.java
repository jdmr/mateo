/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeProveedorDao;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeProveedorDetallesDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
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
public class InformeProveedorDetalleDaoHibernate extends BaseDao implements InformeProveedorDetallesDao {

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de detalles de informe proveedor con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(InformeProveedorDetalle.class);
        Criteria countCriteria = currentSession().createCriteria(InformeProveedorDetalle.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }
        if (params.containsKey("informeProveedor")) {
            criteria.createCriteria("informeProveedor").add(
                    Restrictions.idEq(params.get("informeProveedor")));
            countCriteria.createCriteria("informeProveedor").add(
                    Restrictions.idEq(params.get("informeProveedor")));
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
        if (!params.containsKey("informeProveedor")) {
            List<InformeEmpleadoDetalle> detalles = new ArrayList<>();
            params.put(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE, detalles);
            params.put("cantidad", new Long("0"));
            return params;
        }
        params.put(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public Map<String, Object> contrarecibo(Map<String, Object> params) {
        log.debug("Buscando lista de detalles de informe proveedor con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(InformeProveedorDetalle.class);
        Criteria countCriteria = currentSession().createCriteria(InformeProveedorDetalle.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }
        if (params.containsKey("contrarecibo")) {
            criteria.createCriteria("contrarecibo").add(
                    Restrictions.idEq(params.get("contrarecibo")));
            countCriteria.createCriteria("contrarecibo").add(
                    Restrictions.idEq(params.get("contrarecibo")));
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
        if (!params.containsKey("informeProveedor")) {
            List<InformeEmpleadoDetalle> detalles = new ArrayList<>();
            params.put(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE, detalles);
            params.put("cantidad", new Long("0"));
            return params;
        }
        params.put(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public Map<String, Object> revisar(Map<String, Object> params) {
        log.debug("Buscando lista de detalles de informe proveedor con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(InformeProveedorDetalle.class);
        Criteria countCriteria = currentSession().createCriteria(InformeProveedorDetalle.class);

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

        params.put(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public InformeProveedorDetalle obtiene(final Long id) {
        InformeProveedorDetalle proveedorDetalle = (InformeProveedorDetalle) currentSession().get(InformeProveedorDetalle.class, id);
        if (proveedorDetalle == null) {
            log.warn("uh oh, Informe Empleado Detalle with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(InformeProveedorDetalle.class, id);
        }
        return proveedorDetalle;
    }

    @Override
    public void crea(final InformeProveedorDetalle proveedorDetalle, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            proveedorDetalle.setEmpresa(usuario.getEmpresa());
        }
        currentSession().save(proveedorDetalle);
        currentSession().merge(proveedorDetalle);
        currentSession().flush();
    }

    @Override
    public void actualiza(final InformeProveedorDetalle proveedorDetalle, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            proveedorDetalle.setEmpresa(usuario.getEmpresa());
        }
        try {
            currentSession().update(proveedorDetalle);
        } catch (NonUniqueObjectException e) {
            try {
                currentSession().merge(proveedorDetalle);
            } catch (Exception ex) {
                log.error("No se pudo actualizar el informe detalle", ex);
                throw new RuntimeException("No se pudo actualizar el informe detalle",
                        ex);
            }
        } finally {
            currentSession().flush();
        }
    }

    @Override
    public String elimina(Long id) {
        log.debug("Eliminando prorroga con id {}", id);
        InformeProveedorDetalle detalle = obtiene(id);
        detalle.setStatus("I");
        currentSession().saveOrUpdate(detalle);
        currentSession().merge(detalle);
        currentSession().flush();
        String proveedor = detalle.getNombreProveedor();
        return proveedor;
    }
}
