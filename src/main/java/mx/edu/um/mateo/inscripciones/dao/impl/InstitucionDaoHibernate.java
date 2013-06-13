package mx.edu.um.mateo.inscripciones.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.InstitucionDao;
import mx.edu.um.mateo.inscripciones.model.Institucion;
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
 * @author semdariobarbaamaya
 */
@Repository
@Transactional
public class InstitucionDaoHibernate extends BaseDao implements InstitucionDao{
    
        @Override
        public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de institucion con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Institucion.class);
        Criteria countCriteria = currentSession().createCriteria(Institucion.class);
        

        if (params.containsKey("organizacion")) {
            criteria.createCriteria("organizacion").add(Restrictions.idEq(params.get("organizacion")));
            countCriteria.createCriteria("organizacion").add(Restrictions.idEq(params.get("organizacion")));
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
        params.put(Constantes.CONTAINSKEY_INSTITUCION, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }
     
     /**
     * @see mx.edu.um.mateo.inscripciones.service.InstitucionManager#obtiene(java.lang.String) 
     */
    @Override
    public Institucion obtiene(final Long id) {
        Institucion institucion = (Institucion) currentSession().get(Institucion.class, id);
        if (institucion == null) {
            log.warn("uh oh, institucion with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Institucion.class, id);
        }

        return institucion;
    }
    
    @Override
    public void graba(Institucion institucion) {
        Session session = currentSession();
       
        currentSession().saveOrUpdate(institucion);
        currentSession().merge(institucion);
        currentSession().flush();
        }
    
     @Override
     public Institucion actualiza(Institucion institucion) {
        Session session = currentSession();
        
        session.update(institucion);
        session.flush();
        return institucion;
    }
  
    @Override
    public String elimina(Long id) {
        Institucion institucion = obtiene(id);
        String descripcion = institucion.getNombre();
        currentSession().delete(institucion);
        currentSession().flush();
        return descripcion;
    }
}
