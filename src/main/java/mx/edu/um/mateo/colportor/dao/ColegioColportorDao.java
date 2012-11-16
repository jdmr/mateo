/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.colportor.model.ColegioColportor;
import mx.edu.um.mateo.colportor.utils.UltimoException;
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
 * @author wilbert
 */
@Repository
@Transactional
public class ColegioColportorDao {
private static final Logger log = LoggerFactory.getLogger(ColegioColportorDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public ColegioColportorDao() {
        log.info("Nueva instancia de ColegioColportorDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de colegio con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(ColegioColportor.class);
        Criteria countCriteria = currentSession().createCriteria(ColegioColportor.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro));
            propiedades.add(Restrictions.ilike("status", filtro));
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
        params.put(Constantes.CONTAINSKEY_COLEGIOS, criteria.list());
//        log.debug("colegios***"+((List)params.get(Constantes.CONTAINSKEY_COLEGIOS)).size());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public ColegioColportor obtiene(Long id) {
        log.debug("Obtiene colegio con id = {}", id);
        ColegioColportor colegio = (ColegioColportor) currentSession().get(ColegioColportor.class, id);
        return colegio;
    }

    public ColegioColportor crea(ColegioColportor colegio) {
        log.debug("Creando colegio : {}", colegio);
        currentSession().save(colegio);
        currentSession().flush();
        return colegio;
    }

    public ColegioColportor actualiza(ColegioColportor colegio) {
        log.debug("Actualizando colegio {}", colegio);
        
        //trae el objeto de la DB 
        ColegioColportor nuevo = (ColegioColportor)currentSession().get(ColegioColportor.class, colegio.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(colegio, nuevo);
        //lo guarda en la BD
        currentSession().update(nuevo);
        currentSession().flush();
        return nuevo;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando colegio con id {}", id);
        ColegioColportor colegio = obtiene(id);
        currentSession().delete(colegio);
        currentSession().flush();
        String nombre = colegio.getNombre();
        return nombre;
    }
}