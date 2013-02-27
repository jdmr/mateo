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
import mx.edu.um.mateo.inscripciones.dao.ProrrogaDao;
import mx.edu.um.mateo.inscripciones.model.Prorroga;
import org.hibernate.Criteria;
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
public class ProrrogaDaoHibernate extends BaseDao implements ProrrogaDao{
    
    /**
     *@see mx.edu.um.mateo.rh.dao.ProrrogaDao#lista(java.util.Map)  
     */
    
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
          log.debug("Buscando lista de prorrogas con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Prorroga.class);
        Criteria countCriteria = currentSession().createCriteria(Prorroga.class);

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
            propiedades.add(Restrictions.ilike("matricula", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.eq("fecha_comp", filtro));
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
            criteria.addOrder(Order.asc("descripcion"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONTAINSKEY_PRORROGAS, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.ProrrogaDao#obtiene(java.lang.Long) 
     */
    @Override
    public Prorroga obtiene(Integer id) {
        log.debug("Obtiene prorroga con id = {}", id);
        Prorroga prorroga = (Prorroga) currentSession().get(Prorroga.class, id);
        if(prorroga==null){
            log.warn("uh oh, la prorroga con el id"+id+"no se encontro...");
            throw new ObjectRetrievalFailureException(Prorroga.class, id);
            
        }
        return prorroga;
    }

   
    /**
     *@see mx.edu.um.mateo.rh.dao.ProrrogaDao#graba(mx.edu.um.mateo.rh.model.Prorroga, mx.edu.um.mateo.general.model.Usuario)   
     */
    @Override
    public void graba(final Prorroga prorroga, Usuario usuario) {
        if (usuario != null) {
            prorroga.setEmpresa(usuario.getEmpresa());
            prorroga.setUsuario(usuario);
            prorroga.setUserName(usuario.getUsername());
        }
        currentSession().saveOrUpdate(prorroga);
        currentSession().merge(prorroga);
        currentSession().flush();
      
        
    }

    
/**
 *@see mx.edu.um.mateo.rh.dao.ProrrogaDao#elimina(java.lang.Long)  
 */
    @Override
    public String elimina( Integer id)  {
        log.debug("Eliminando prorroga con id {}", id);
        Prorroga prorroga = obtiene(id);
        prorroga.setStatus("I");
        currentSession().delete(prorroga);
        currentSession().flush();
        String descripcion = prorroga.getDescripcion();
        return descripcion;
    }
    
}
