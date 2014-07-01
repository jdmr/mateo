/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.dao.RolDao;
import mx.edu.um.mateo.contabilidad.facturas.dao.ProveedorFacturasDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Repository
@Transactional
public class ProveedorFacturasDaoHibernate extends BaseDao implements ProveedorFacturasDao {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RolDao rolDao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de Proveedores con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey("max")) {
            params.put("max", 10);
        } else {
            params.put("max", Math.min((Integer) params.get("max"), 100));
        }

        if (params.containsKey("pagina")) {
            log.debug("entrando a paginacion");
            Long pagina = (Long) params.get("pagina");
            Long offset = (pagina - 1) * (Integer) params.get("max");
            params.put("offset", offset.intValue());
        }

        if (!params.containsKey("offset")) {
            params.put("offset", 0);
        }
        Criteria criteria = currentSession().createCriteria(ProveedorFacturas.class);
        Criteria countCriteria = currentSession().createCriteria(ProveedorFacturas.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("rfc", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("razonSocial", filtro,
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
            criteria.addOrder(Order.asc("rfc"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONTAINSKEY_PROVEEDORESFACTURAS, criteria.list());

        log.debug("listaObjetos{}", criteria.list());
        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));
        return params;
    }

    @Override
    public ProveedorFacturas obtiene(final Long id) {
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) currentSession().get(ProveedorFacturas.class, id);
        if (proveedorFacturas == null) {
            log.warn("uh oh, Proveedor with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(ProveedorFacturas.class, id);
        }

        return proveedorFacturas;
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorFacturas obtiene(String rfc) {
        log.debug("obteniendo prveedor por rfc");
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) getSession()
                .createCriteria(ProveedorFacturas.class)
                .add(org.hibernate.criterion.Restrictions.eq("rfc", rfc)).uniqueResult();

        if (proveedorFacturas == null) {
            log.warn("uh oh, Proveedor with id '" + rfc + "' not found...");
            throw new ObjectRetrievalFailureException(ProveedorFacturas.class, rfc);
        }

        return proveedorFacturas;
    }

    @Override
    public void crea(final ProveedorFacturas proveedorFacturas, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            log.debug("usuarioAmbiente++-+{}", usuario.toString());
            log.debug("empresa--/-{}", usuario.getEmpresa().toString());
            proveedorFacturas.setEmpresa(usuario.getEmpresa());

            proveedorFacturas.setAlmacen(usuario.getAlmacen());
        }
        log.debug("usuario logeado... {}" + usuario.getEjercicio());
        log.debug("creando proveedor facturas {}" + proveedorFacturas);
        proveedorFacturas.setPassword(passwordEncoder.encodePassword(
                proveedorFacturas.getPassword(), proveedorFacturas.getUsername()));
        log.debug("password" + proveedorFacturas.getPassword());
        proveedorFacturas.addRol(rolDao.obtiene("ROLE_PRV"));
        log.debug("rol del proveedor{}" + proveedorFacturas.getRoles());
        currentSession().save(proveedorFacturas);
        currentSession().merge(proveedorFacturas);
        currentSession().flush();
        log.debug("usuario logeado grabado{}" + usuario.getEjercicio());

    }

    @Override
    public void actualiza(final ProveedorFacturas proveedorFacturas, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            proveedorFacturas.setEmpresa(usuario.getEmpresa());
            proveedorFacturas.setAlmacen(usuario.getAlmacen());
        }
        try {
            currentSession().update(proveedorFacturas);
            currentSession().flush();
        } catch (NonUniqueObjectException e) {
            try {
                currentSession().merge(proveedorFacturas);
                currentSession().flush();
            } catch (Exception ex) {
                log.error("No se pudo actualizar el proveedor", ex);
                throw new RuntimeException("No se pudo actualizar el proveedor",
                        ex);
            }
        }

    }

    @Override
    public String elimina(Long id) {
        log.debug("Eliminando proveedor con id {}", id);
        ProveedorFacturas proveedorFacturas = obtiene(id);
        proveedorFacturas.setStatus("I");
        currentSession().saveOrUpdate(proveedorFacturas);
        currentSession().merge(proveedorFacturas);
        currentSession().flush();
        String rfc = proveedorFacturas.getRfc();
        return rfc;
    }
}
