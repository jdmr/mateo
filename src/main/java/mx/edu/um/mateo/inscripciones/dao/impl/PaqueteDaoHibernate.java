/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao.impl;

import mx.edu.um.mateo.inscripciones.dao.PaqueteDao;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
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
 * @author develop
 */
@Repository
@Transactional
public class PaqueteDaoHibernate extends BaseDao implements PaqueteDao{
      /**
     * @see mx.edu.um.afe.dao.TipoAFEBecaDao#getTiposBeca(mx.edu.um.afe.model.TipoAFEBeca)
     */

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
        Criteria criteria = currentSession().createCriteria(Paquete.class);
        Criteria countCriteria = currentSession().createCriteria(Paquete.class);

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
            propiedades.add(Restrictions.ilike("nombre", filtro,
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
            criteria.addOrder(Order.asc("descripcion"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("paquetes", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.afe.dao.TipoAFEBecaDao#getTipoBeca(Integer id)
     */
    @Override
    public Paquete obtiene(final Long id) {
        Paquete paquete =  (Paquete) currentSession().get(Paquete.class, id);
        if (paquete == null) {
            log.warn("uh oh, tipoBeca with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Paquete.class, id);
        }

        return paquete;
    }

    /**
     * @see mx.edu.um.afe.dao.TipoAFEBecaDao#saveTipoBeca(TipoAFEBeca tipoBeca)
     */    
   public void crea(final Paquete paquete, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            paquete.setEmpresa(usuario.getEmpresa());
        }
        currentSession().saveOrUpdate(paquete);
        currentSession().merge(paquete);
        currentSession().flush();

    }
    /**
     * @see mx.edu.um.afe.dao.TipoAFEBecaDao#removeTipoBeca(Integer id)
     */
    @Override
    public String elimina(final Long id) {
       Paquete paquete = this.obtiene(id);
       String descripcion = paquete.getDescripcion(); 
       currentSession().delete(paquete);
        currentSession().flush();
        return descripcion;
        
    }

}
