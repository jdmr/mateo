/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.Ciudad;
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
public class CiudadDao {
    private static final Logger log = LoggerFactory.getLogger(CiudadDao.class);
    @Autowired
    private SessionFactory sessionFactory;
    public CiudadDao() {
        log.info("Nueva instancia de CiudadDao");
    }
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de Ciudad con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Ciudad.class);
        Criteria countCriteria = currentSession().createCriteria(Ciudad.class);
        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike(Constantes.ADDATTRIBUTE_NOMBRE, filtro));
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
        params.put(Constantes.CONTAINSKEY_CIUDADES, criteria.list());
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));
        return params;
    }
        public Ciudad obtiene(Long id) {
        log.debug("Obtiene Ciudad con id = {}", id);
        Ciudad ciudad = (Ciudad) currentSession().get(Ciudad.class, id);
        return ciudad;
    }
        public Ciudad crea(Ciudad ciudad) {
        log.debug("Creando Ciudad : {}", ciudad);
        currentSession().save(ciudad);
        currentSession().flush();
        return ciudad;
    }
        public Ciudad actualiza(Ciudad ciudad) {
        log.debug("Actualizando Ciudad {}", ciudad);
        //trae el objeto de la DB 
        Ciudad nuevo = (Ciudad) currentSession().get(Ciudad.class, ciudad.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(ciudad, nuevo);
        //lo guarda en la BD
        currentSession().update(nuevo);
        currentSession().flush();
        return nuevo;
    }
        public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando Ciudad con id {}", id);
        Ciudad ciudad = obtiene(id);
        currentSession().delete(ciudad);
        currentSession().flush();
        String nombre = ciudad.getNombre();
        return nombre;
    }
}
