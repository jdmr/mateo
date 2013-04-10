/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.inscripciones.dao.DescuentoDao;
import mx.edu.um.mateo.inscripciones.model.Descuento;
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
public class DescuentoDaoHibernate extends BaseDao implements DescuentoDao{
    
    
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
        Criteria criteria = currentSession().createCriteria(Descuento.class);
        Criteria countCriteria = currentSession().createCriteria(Descuento.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("descripcion", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_DESCUENTO, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));


        return params;
    }

    @Override
    public Descuento obtiene(final Long id) {
        Descuento descuento = (Descuento) currentSession().get(Descuento.class, id);
        if (descuento == null) {
            log.warn("uh oh, categoria with id '" + id + "' not found...");
            try {
                throw new ObjectRetrievalFailureException(Descuento.class, id);
            } catch (ObjectRetrievalFailureException ex) {
                Logger.getLogger(DescuentoDaoHibernate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return descuento;
    }
    

    @Override
    public void graba(Descuento descuento, Organizacion organizacion) {
        if (organizacion != null) {
            descuento.setOrganizacion(organizacion);
        }
        Session session = currentSession();
        currentSession().saveOrUpdate(descuento);
        currentSession().merge(descuento);
        currentSession().flush();
    }

    @Override
    public String elimina(final Long id) {
       log.debug("Eliminando el descuento {}", id);
        Descuento descuento = this.obtiene(id);
        String nombre = descuento.getDescripcion();
        currentSession().delete(descuento);
        currentSession().flush();

        return nombre;
    }

}
