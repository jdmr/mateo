/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.Estado;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gibrandemetrioo
 */
@Repository
@Transactional
public class EstadoDao {
    private static final Logger log = LoggerFactory.getLogger(EstadoDao.class);
    @Autowired
    private SessionFactory sessionFactory;
    public EstadoDao() {
        log.info("Nueva instancia de EstadoDao");
    }
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de Estado con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Estado.class);
        Criteria countCriteria = currentSession().createCriteria(Estado.class);
        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro));
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
        params.put(Constantes.CONTAINSKEY_ESTADOS, criteria.list());
        log.debug("Rows returned {}",((List)params.get(Constantes.CONTAINSKEY_ESTADOS)).size());
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));
        return params;
    }
        public Estado obtiene(Long id) {
        log.debug("Obtiene Estado con id = {}", id);
        Estado estado = (Estado) currentSession().get(Estado.class, id);
        return estado;
    }
        public Estado crea(Estado estado) {
        log.debug("Creando Estado : {}", estado);
        currentSession().save(estado);
        currentSession().flush();
        return estado;
    }
        public Estado actualiza(Estado estado) {
        log.debug("Actualizando Estado {}", estado);
        //trae el objeto de la DB 
        Estado nuevo = (Estado) currentSession().get(Estado.class, estado.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(estado, nuevo);
        //lo guarda en la BD
        currentSession().update(nuevo);
        currentSession().flush();
        return nuevo;
    }
        public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando Estado con id {}", id);
        Estado estado = obtiene(id);
        currentSession().delete(estado);
        currentSession().flush();
        String nombre = estado.getNombre();
        return nombre;
    }
}
    
