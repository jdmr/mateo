/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Temporada;
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
 * @author gibrandemetrioo
 */
@Repository
@Transactional
public class TemporadaDao {
    private static final Logger log = LoggerFactory.getLogger(TemporadaDao.class);
    @Autowired
    private SessionFactory sessionFactory;
    
    public TemporadaDao () {
        log.info("Se ha creado una nueva TemporadaDao");
    }
    
    
    
    private Session currentSession () {
        return sessionFactory.getCurrentSession();
    }
    
    
    /**
     * Regresa lista de temporadas por asociacion<br>
     * En params va la asociacion como objeto<br>
     * Si la asociacion no esta presente en params regresa 
     * @param params
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de Temporada con params {}", params);
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
        
        Criteria criteria = currentSession().createCriteria(Temporada.class);
        Criteria countCriteria = currentSession().createCriteria(Temporada.class);
        
        if (params.containsKey(Constantes.ADDATTRIBUTE_ASOCIACION)) {
//            criteria.createCriteria("asociacion").add(Restrictions.eq("id",((Asociacion)params.get(Constantes.ADDATTRIBUTE_ASOCIACION)).getId()));
            criteria.add(Restrictions.eq("asociacion",((Asociacion)params.get(Constantes.ADDATTRIBUTE_ASOCIACION))));
            countCriteria.add(Restrictions.eq("id",((Asociacion)params.get(Constantes.ADDATTRIBUTE_ASOCIACION)).getId()));
        }
        
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
        params.put(Constantes.CONTAINSKEY_TEMPORADAS, criteria.list());
//        log.debug("Temporadas***"+((List)params.get(Constantes.CONTAINSKEY_TEMPORADAS)).size());
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));
        
        return params;
    }

    public Temporada obtiene(Long id) {
        log.debug("Obtiene Temporada con id = {}", id);
        Temporada temporada = (Temporada) currentSession().get(Temporada.class, id);
        return temporada;
    }

    public Temporada crea(Temporada temporada) {
        log.debug("Creando Temporada : {}", temporada);
        
        currentSession().save(temporada);
        currentSession().flush();
        return temporada;
    }

     public Temporada actualiza(Temporada temporada) {
        log.debug("Actualizando Temporada {}", temporada);
        
        //trae el objeto de la DB 
        Temporada nueva = (Temporada)currentSession().get(Temporada.class, temporada.getId());
        
        //actualiza el objeto
        BeanUtils.copyProperties(temporada, nueva);
        //lo guarda en la BD
        
        currentSession().update(nueva);
        currentSession().flush();
        return nueva;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando Temporada id {}", id);
        Temporada temporada = obtiene(id);
        Date fecha = new Date();
        temporada.setFechaInicio(fecha);
        temporada.setFechaFinal(fecha);
        currentSession().delete(temporada);
        currentSession().flush();
        String nombre = temporada.getNombre();
        return nombre;
    }
    
  }


