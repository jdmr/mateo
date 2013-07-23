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
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
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
public class InformeProveedorDaoHibernate extends BaseDao implements InformeProveedorDao {

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de Informes del proveedor con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(InformeProveedor.class);
        Criteria countCriteria = currentSession().createCriteria(InformeProveedor.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }
        if (params.containsKey("proveedorFacturas")) {
            criteria.createCriteria("proveedorFacturas").add(
                    Restrictions.idEq(params.get("proveedorFacturas")));
            countCriteria.createCriteria("proveedorFacturas").add(
                    Restrictions.idEq(params.get("proveedorFacturas")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombreProveedor", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("fechaInforme", filtro,
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
            criteria.addOrder(Order.asc("fechaInforme"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        if (!params.containsKey("proveedorFacturas")) {
            List<InformeEmpleado> encabezados = new ArrayList<>();
            params.put(Constantes.CONTAINSKEY_INFORMESPROVEEDOR, encabezados);
            params.put("cantidad", new Long("0"));
            return params;
        }
        params.put(Constantes.CONTAINSKEY_INFORMESPROVEEDOR, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public InformeProveedor obtiene(final Long id) {
        InformeProveedor informeProveedor = (InformeProveedor) currentSession().get(InformeProveedor.class, id);
        if (informeProveedor == null) {
            log.warn("uh oh, Informe Proveedor with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(InformeProveedor.class, id);
        }
        return informeProveedor;
    }

    @Override
    public void crea(final InformeProveedor informeProveedor, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            informeProveedor.setEmpresa(usuario.getEmpresa());
        }
        currentSession().save(informeProveedor);
        currentSession().merge(informeProveedor);
        currentSession().flush();
    }

    @Override
    public void actualiza(final InformeProveedor informeProveedor, Usuario usuario) {
        Session session = currentSession();
        log.debug("informe...**dao entrando{}", informeProveedor);
        if (usuario != null) {
            informeProveedor.setEmpresa(usuario.getEmpresa());
        }
        try {
            currentSession().update(informeProveedor);
            currentSession().flush();
        } catch (NonUniqueObjectException e) {
            try {
                currentSession().merge(informeProveedor);
                currentSession().flush();

            } catch (Exception ex) {
                log.error("No se pudo actualizar el informe", ex);
                throw new RuntimeException("No se pudo actualizar el informe",
                        ex);
            }
        }
        log.debug("informe...**dao saliendo{}", informeProveedor);
    }

    @Override
    public String elimina(Long id) {
        log.debug("Eliminando informe proveedor con id {}", id);
        InformeProveedor informeProveedor = obtiene(id);
        informeProveedor.setStatus("I");
        currentSession().saveOrUpdate(informeProveedor);
        currentSession().merge(informeProveedor);
        currentSession().flush();
        String proveedor = informeProveedor.getNombreProveedor();
        return proveedor;
    }
}
