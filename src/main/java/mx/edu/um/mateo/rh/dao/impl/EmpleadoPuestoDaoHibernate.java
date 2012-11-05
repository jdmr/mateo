/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.EmpleadoPuestoDao;
import mx.edu.um.mateo.rh.model.EmpleadoPuesto;
import org.hibernate.Criteria;
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
 * @author semdariobarbaamaya
 */
@Repository
@Transactional
public class EmpleadoPuestoDaoHibernate extends BaseDao implements EmpleadoPuestoDao{
    
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de empleadopuesto con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(EmpleadoPuesto.class).add(Restrictions.eq("status",Constantes.STATUS_ACTIVO));
        Criteria countCriteria = currentSession().createCriteria(EmpleadoPuesto.class).add(Restrictions.eq("status",Constantes.STATUS_ACTIVO));
        

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("status", filtro, MatchMode.ANYWHERE));
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
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONTAINSKEY_EMPLEADOPUESTO, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }
    
    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoPuestoManager#obtiene(java.lang.String) 
     */
    @Override
    public EmpleadoPuesto obtiene(final Long id) {
        EmpleadoPuesto empleadopuesto = (EmpleadoPuesto) currentSession().get(EmpleadoPuesto.class, id);
        if (empleadopuesto == null) {
            log.warn("uh oh, empleadopuesto with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(EmpleadoPuesto.class, id);
        }
        return empleadopuesto;
    }
    
    /**
     *  @see mx.edu.um.mateo.rh.service.PerDedManager#graba(mx.edu.um.mateo.rh.model.PerDed, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void graba(final EmpleadoPuesto empleadopuesto, Usuario usuario) {
        Session session = currentSession();
//        if (usuario != null) {
//            empleadopuesto.setEmpresa(usuario.getEmpresa());
//        }
        currentSession().saveOrUpdate(empleadopuesto);
        currentSession().merge(empleadopuesto);
        currentSession().flush();
    }
     /**
     *  @see mx.edu.um.mateo.rh.service.PerDedManager#elimina(java.lang.String) 
     */
    @Override
    public String elimina(final Long id) {
        log.debug("Eliminando la perded {}", id);
        EmpleadoPuesto empleadopuesto = this.obtiene(id);
        String nombre = empleadopuesto.getPuesto().getDescripcion();
        currentSession().delete(empleadopuesto);

        currentSession().flush();

        return nombre;
    }
}
