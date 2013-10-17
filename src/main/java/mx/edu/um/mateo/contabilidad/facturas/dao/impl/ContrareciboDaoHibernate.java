/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.ContrareciboDao;
import mx.edu.um.mateo.contabilidad.facturas.model.Contrarecibo;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
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
public class ContrareciboDaoHibernate extends BaseDao implements ContrareciboDao {

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de contrarecibos con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Contrarecibo.class);
        Criteria countCriteria = currentSession().createCriteria(Contrarecibo.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }
        criteria.add(Restrictions.ilike("status", "A"));
        countCriteria.add(Restrictions.ilike("status", "A"));

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            criteria.createAlias("proveedorFacturas", "pf");
            countCriteria.createAlias("proveedorFacturas", "pf");
            propiedades.add(Restrictions.ilike("pf.nombre", filtro,
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
            criteria.addOrder(Order.asc("fechaPago"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONTAINSKEY_CONTRARECIBOS, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public void crea(Contrarecibo contrarecibo, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            contrarecibo.setEmpresa(usuario.getEmpresa());
        }
        currentSession().save(contrarecibo);
        currentSession().merge(contrarecibo);
        currentSession().flush();

    }

    @Override
    public Contrarecibo obtiene(Long id) {
        Contrarecibo contrarecibo = (Contrarecibo) currentSession().get(Contrarecibo.class, id);
        List<InformeProveedorDetalle> detalles = currentSession().createCriteria(InformeProveedorDetalle.class).createCriteria("contrarecibo").add(Restrictions.idEq(id)).list();
        for (InformeProveedorDetalle x : detalles) {
            log.debug("Detelles***-{}", x);
        }
        contrarecibo.setDetalles(detalles);
        if (contrarecibo == null) {
            log.warn("uh oh, Informe Proveedor with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(InformeProveedor.class, id);
        }
        return contrarecibo;
    }

    @Override
    public void actualiza(Contrarecibo contrarecibo, Usuario usuario) {
        Session session = currentSession();
        log.debug("informe...**dao entrando{}", contrarecibo);
        if (usuario != null) {
            contrarecibo.setEmpresa(usuario.getEmpresa());
        }
        try {
            currentSession().update(contrarecibo);
            currentSession().flush();
        } catch (NonUniqueObjectException e) {
            try {
                currentSession().merge(contrarecibo);
                currentSession().flush();

            } catch (Exception ex) {
                log.error("No se pudo actualizar el informe", ex);
                throw new RuntimeException("No se pudo actualizar el informe",
                        ex);
            }
        }
        log.debug("informe...**dao saliendo{}", contrarecibo);
    }

    @Override
    public String elimina(Long id) {
        Contrarecibo contrarecibo = obtiene(id);
        contrarecibo.setStatus("I");
        String nombre = contrarecibo.getId().toString();
        currentSession().saveOrUpdate(contrarecibo);
        currentSession().merge(contrarecibo);
        currentSession().flush();
        return nombre;
    }
}
