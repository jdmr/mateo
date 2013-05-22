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
import mx.edu.um.mateo.general.dao.BaseDao;
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
public class TemporadaColportorDao extends BaseDao {

    
    public TemporadaColportorDao() {        
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
        
        Criteria criteria = currentSession().createCriteria(TemporadaColportor.class)
                .add(Restrictions.eq("status", Constantes.STATUS_ACTIVO));
        Criteria countCriteria = currentSession().createCriteria(TemporadaColportor.class)
                .add(Restrictions.eq("status", Constantes.STATUS_ACTIVO));
        
        if(params.get("empresa") != null){
            criteria.createCriteria("asociado").add(Restrictions.eq("empresa.id", (Long)params.get("empresa")));
            countCriteria.createCriteria("asociado").add(Restrictions.eq("empresa.id", (Long)params.get("empresa")));
        }
        if(params.get("asociado") != null){
            criteria.createCriteria("asociado").add(Restrictions.eq("id", (Long)params.get("asociado")));
            countCriteria.createCriteria("asociado").add(Restrictions.eq("id", (Long)params.get("asociado")));
        }
        
        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            
            //FALTA DEFINIR FILTROS

            //criteria.add(propiedades);
            //countCriteria.add(propiedades);
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
        params.put(Constantes.TEMPORADACOLPORTOR_LIST, criteria.list());
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public TemporadaColportor obtiene(Long id) {
        log.debug("Obtiene Temporada Colportor con id = {}", id);
        TemporadaColportor temporadaColportor = (TemporadaColportor) currentSession().get(TemporadaColportor.class, id);
        return temporadaColportor;
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
        
        Criteria sql = currentSession().createCriteria(TemporadaColportor.class);
        sql.add(Restrictions.eq("colportor", colportor));
        sql.add(Restrictions.eq("temporada", temporada));
        return (TemporadaColportor) sql.uniqueResult();
    }

    public TemporadaColportor crea(TemporadaColportor temporadaColportor) {
        log.debug("Creando Temporada Colportor : {}", temporadaColportor);
        currentSession().save(temporadaColportor);
        currentSession().flush();
        return temporadaColportor;
    }

    public TemporadaColportor actualiza(TemporadaColportor temporadaColportor) {
        log.debug("Actualizando Temporada Colportor {}", temporadaColportor);
        currentSession().update(temporadaColportor);
        currentSession().flush();
        return temporadaColportor;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando Temporada Colportor id {}", id);
        TemporadaColportor temporadaColportor = obtiene(id);
        String clave = temporadaColportor.getColportor().getClave();
        temporadaColportor.setStatus(Constantes.STATUS_INACTIVO);
        actualiza(temporadaColportor);
        return clave;
    }
}
