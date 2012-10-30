/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.rh.dao.DependienteDao;
import mx.edu.um.mateo.rh.model.Dependiente;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@Repository
@Transactional
public class DependienteDaoHibernate extends BaseDao implements DependienteDao{
    
    /**
     * @see mx.edu.um.mateo.rh.dao.DependienteDao#lista(java.util.Map)   
     */
   @Override
   public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de dependiente con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_MAX)) {
            params.put(Constantes.CONTAINSKEY_MAX, 10);
        } else {
            params.put(Constantes.CONTAINSKEY_MAX, Math.min((Integer) params.get(Constantes.CONTAINSKEY_MAX), 100));
        }

        if (params.containsKey(Constantes.CONTAINSKEY_PAGINA)) {
            Long pagina = (Long) params.get(Constantes.CONTAINSKEY_PAGINA);
            Long offset = (pagina - 1) * (Integer) params.get(Constantes.CONTAINSKEY_MAX);
            params.put(Constantes.CONTAINSKEY_OFFSET, offset.intValue());
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_OFFSET)) {
            params.put(Constantes.CONTAINSKEY_OFFSET, 0);
        }
        Criteria criteria = currentSession().createCriteria(Dependiente.class);
        Criteria countCriteria = currentSession().createCriteria(Dependiente.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apPaterno", filtro, MatchMode.ANYWHERE));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey(Constantes.CONTAINSKEY_ORDER)) {
            String campo = (String) params.get(Constantes.CONTAINSKEY_ORDER);
            if (params.get(Constantes.CONTAINSKEY_SORT).equals(Constantes.CONTAINSKEY_DESC)) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_REPORTE)) {
            criteria.setFirstResult((Integer) params.get(Constantes.CONTAINSKEY_OFFSET));
            criteria.setMaxResults((Integer) params.get(Constantes.CONTAINSKEY_MAX));
        }
        params.put(Constantes.CONTAINSKEY_DEPENDIENTES, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

   /**
     * @see mx.edu.um.mateo.rh.dao.DependienteDao#obtiene(java.lang.Long) 
     */
    @Override
    public Dependiente obtiene(Long id) {
        log.debug("Obtiene dependiente con id = {}", id);
        Dependiente dependiente = (Dependiente) currentSession().get(Dependiente.class, id);
        return dependiente;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.DependienteDao#graba(mx.edu.um.mateo.rh.model.Dependiente)  
     */
    @Override
    public void graba(Dependiente dependiente) {
          Session session = currentSession();
        currentSession().saveOrUpdate(dependiente);
        currentSession().merge(dependiente);
        currentSession().flush();
      
    }
        

    /**
     * @see mx.edu.um.mateo.rh.dao.DependienteDao#elimina(java.lang.Long)  
     */
    @Override
    public String elimina(Long id)  {
        log.debug("Eliminando dependiente con id {}", id);
        Dependiente dependiente = obtiene(id);
        currentSession().delete(dependiente);
        currentSession().flush();
        String nombre = dependiente.getTipoDependiente().toString();
        return nombre;
    }
}
