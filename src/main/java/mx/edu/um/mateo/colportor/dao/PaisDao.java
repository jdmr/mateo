/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.Pais;
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
public class PaisDao {

    private static final Logger log = LoggerFactory.getLogger(PaisDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public PaisDao() {
        log.info("Nueva instancia de PaisDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de Pais con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Pais.class);
        Criteria countCriteria = currentSession().createCriteria(Pais.class);
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
        params.put(Constantes.PAIS_LIST, criteria.list());
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));
        return params;
    }

    public Pais obtiene(Long id) {
        log.debug("Obtiene Pais con id = {}", id);
        Pais pais = (Pais) currentSession().get(Pais.class, id);
        return pais;
    }

    public Pais crea(Pais pais) {
        log.debug("Creando Pais : {}", pais);
        currentSession().save(pais);
        currentSession().flush();
        return pais;
    }

    public Pais actualiza(Pais pais) {
        log.debug("Actualizando pais {}", pais);
        //trae el objeto de la DB 
        Pais nuevo = (Pais) currentSession().get(Pais.class, pais.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(pais, nuevo);
        //lo guarda en la BD
        currentSession().update(nuevo);
        currentSession().flush();
        return nuevo;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando pais con id {}", id);
        Pais pais = obtiene(id);
        currentSession().delete(pais);
        currentSession().flush();
        String nombre = pais.getNombre();
        return nombre;
    }
}
