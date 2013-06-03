/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.dao.OrdenPagoDao;
import mx.edu.um.mateo.contabilidad.model.OrdenPago;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 */
@Repository
public class OrdenPagoDaoHibernate extends BaseDao implements OrdenPagoDao {

/**
 * @see mx.edu.um.mateo.contabilidad.dao.OrdenPagoDao#lista(java.util.Map) 
 */
 @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de ordenes de pago con params {}", params);
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
        
        Criteria criteria = currentSession().createCriteria(OrdenPago.class)
                .add(Restrictions.eq("statusInterno", Constants.STATUS_ACTIVO));
        Criteria countCriteria = currentSession().createCriteria(OrdenPago.class)
                .add(Restrictions.eq("statusInterno", Constants.STATUS_ACTIVO));

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("descripcion", filtro,
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
        params.put(Constantes.ORDENPAGO_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see
     * mx.edu.um.mateo.contabilidad.dao.OrdenPagoDao#obtiene(java.lang.Integer)
     */
    @Override
    public OrdenPago obtiene(final Long id) {
        Criteria criteria = currentSession().createCriteria(OrdenPago.class)
                .add(Restrictions.idEq(id))
                .add(Restrictions.eq("statusInterno", Constants.STATUS_ACTIVO));
        OrdenPago ordenPago = (OrdenPago)criteria.uniqueResult();
        //OrdenPago ordenPago = (OrdenPago) currentSession().get(OrdenPago.class, id);
        if (ordenPago == null) {
            log.warn("uh oh, orden de pago with id '" + id + "' not found...");
            //throw new ObjectRetrievalFailureException(OrdenPago.class, id);
        }

        return ordenPago;
    }

    /**
     * @see
     * mx.edu.um.mateo.contabilidad.dao.OrdenPagoDao#crea(mx.edu.um.mateo.contabilidad.model.OrdenPago,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    @Transactional
    public void crea(final OrdenPago ordenPago, Usuario usuario) {
        if (usuario != null) {
            ordenPago.setEmpresa(usuario.getEmpresa());
        }
        ordenPago.setFechaCaptura(new Date());
        currentSession().save(ordenPago);
        currentSession().flush();

    }

    /**
     * @see
     * mx.edu.um.mateo.contabilidad.dao.OrdenPagoDao#actualiza(mx.edu.um.mateo.contabilidad.model.OrdenPago,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    @Transactional
    public void actualiza(final OrdenPago ordenPago, Usuario usuario) {
        if (usuario != null) {
            ordenPago.setEmpresa(usuario.getEmpresa());
        }
        OrdenPago op1 = null, op2 = null;
        try {
            
            op1 = obtiene(ordenPago.getId());
            
            op2 = (OrdenPago)BeanUtils.cloneBean(op1);
            op2.setCheque(ordenPago.getCheque());
            op2.setDescripcion(ordenPago.getDescripcion());
            op2.setFechaPago(ordenPago.getFechaPago());
            op2.setStatus(ordenPago.getStatus());
            op2.setUserCaptura(ordenPago.getUserCaptura());
            op2.setId(null);
            op2.setVersion(null);
            
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            log.error("No fue posible clonar la orden de pago {}", ordenPago);
            throw new RuntimeException("No se pudo actualizar el ordenPago",
                    ex);
        }
        try {
            //Desactivar status de la orden original
            op1.setStatusInterno(Constantes.STATUS_INACTIVO);
            currentSession().update(op1);
            
            //Mandamos crear la nueva instancia de la orden de pago            
            crea(op2, usuario);
        } catch (NonUniqueObjectException e) {
            try {
                currentSession().merge(op1);
                //Mandamos crear la nueva instancia de la orden de pago            
                crea(op2, usuario);
            } catch (Exception ex) {
                log.error("No se pudo actualizar el ordenPago", ex);
                throw new RuntimeException("No se pudo actualizar el ordenPago",
                        ex);
            }
        } finally {
            currentSession().flush();
        }
    }

    /**
     * @see
     * mx.edu.um.mateo.contabilidad.dao.OrdenPagoDao#elimina(java.lang.Long) 
     */
    @Override
    public String elimina(final Long id) {
        OrdenPago ordenPago = this.obtiene(id);
        String descripcion = ordenPago.getDescripcion();
        ordenPago.setStatusInterno(Constantes.STATUS_INACTIVO);
        try{
            currentSession().update(ordenPago);
        } catch (NonUniqueObjectException e) {
            try {
                currentSession().merge(ordenPago);
            } catch (Exception ex) {
                log.error("No se pudo actualizar el ordenPago", ex);
                throw new RuntimeException("No se pudo actualizar el ordenPago",
                        ex);
            }
        } finally {
            currentSession().flush();
        }
        return descripcion;

    }
}
