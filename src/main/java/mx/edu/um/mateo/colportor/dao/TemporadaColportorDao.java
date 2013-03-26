/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.colportor.model.Colportor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
public class TemporadaColportorDao {

    private static final Logger log = LoggerFactory.getLogger(TemporadaColportorDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public TemporadaColportorDao() {
        log.info("Se ha creado una nueva Temporada ColportorDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de Temporada Colportor con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(TemporadaColportor.class);
        Criteria countCriteria = currentSession().createCriteria(TemporadaColportor.class);
        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike(Constantes.ADDATTRIBUTE_STATUS, filtro));

            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }
        if (params.containsKey(Constantes.CONTAINSKEY_ORDER)) {
            String campo = (String) params.get(Constantes.CONTAINSKEY_ORDER);
            if (params.get(Constantes.CONTAINSKEY_SORT).equals(Constantes.CONTAINSKEY_SORT)) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }

        }
        if (!params.containsKey(Constantes.CONTAINSKEY_REPORTE)) {
            criteria.setFirstResult((Integer) params.get(Constantes.CONTAINSKEY_OFFSET));
            criteria.setMaxResults((Integer) params.get(Constantes.CONTAINSKEY_MAX));
        }
        params.put(Constantes.CONTAINSKEY_TEMPORADACOLPORTORES, criteria.list());
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public TemporadaColportor obtiene(Long id) {
        log.debug("Obtiene Temporada Colportor con id = {}", id);
        TemporadaColportor temporadacolportor = (TemporadaColportor) currentSession().get(TemporadaColportor.class, id);
        return temporadacolportor;
    }

    public TemporadaColportor obtiene(Colportor colportor) {
        log.debug("Obtiene Temporada Colportor con Colportor = {}", colportor.getId());
        Criteria sql = currentSession().createCriteria(TemporadaColportor.class);
        sql.add(Restrictions.eq("colportor", colportor));
        sql.add(Restrictions.eq("status", Constantes.STATUS_ACTIVO));
        return (TemporadaColportor) sql.uniqueResult();

    }

    public TemporadaColportor obtiene(Colportor colportor, Temporada temporada) {
        log.debug("Obtiene Temporada Colportor con Colportor  {}", colportor.getId());
        log.debug("Obtiene Temporada Colportor con Temporada  {}", temporada.getId());
        
        Criteria sql = currentSession().createCriteria(TemporadaColportor.class);
        sql.add(Restrictions.eq("colportor", colportor));
        sql.add(Restrictions.eq("temporada", temporada));
        return (TemporadaColportor) sql.uniqueResult();

    }

    public TemporadaColportor crea(TemporadaColportor temporadacolportor) {
        log.debug("Creando Temporada Colportor : {}", temporadacolportor);
        currentSession().save(temporadacolportor);
        currentSession().flush();
        return temporadacolportor;
    }

    public TemporadaColportor actualiza(TemporadaColportor temporadacolportor) {
        log.debug("Actualizando Temporada Colportor {}", temporadacolportor);
        //trae el objeto de la DB 
        TemporadaColportor nueva = (TemporadaColportor) currentSession().get(TemporadaColportor.class, temporadacolportor.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(temporadacolportor, nueva);
        //lo guarda en la BD
        currentSession().update(nueva);
        currentSession().flush();
        return nueva;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando Temporada Colportor id {}", id);
        TemporadaColportor temporadacolportor = obtiene(id);
        Date fecha = new Date();
        temporadacolportor.setFecha(fecha);
        currentSession().delete(temporadacolportor);
        currentSession().flush();
        String status = temporadacolportor.getStatus();
        return status;
    }
}
