/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections; 
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gibrandemetrioo
 */
@Repository
@Transactional
public class TemporadaDao extends BaseDao {
    public TemporadaDao () {
        log.info("Se ha creado una nueva TemporadaDao");
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
        
        Criteria criteria = currentSession().createCriteria(Temporada.class)
                .add(Restrictions.eq("status", Constantes.STATUS_ACTIVO));
        Criteria countCriteria = currentSession().createCriteria(Temporada.class)
                .add(Restrictions.eq("status", Constantes.STATUS_ACTIVO));
        
        if (params.containsKey("organizacion")) {
            criteria.add(Restrictions.eq("organizacion.id",params.get("organizacion")));
            countCriteria.add(Restrictions.eq("organizacion.id",params.get("organizacion")));
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
        params.put(Constantes.TEMPORADA_LIST, criteria.list());
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
                
        try{
            currentSession().update(temporada);
        }catch(NonUniqueObjectException e){
            currentSession().merge(temporada);
            currentSession().flush();
            
        }
        
        return temporada;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando Temporada id {}", id);
        Temporada temporada = obtiene(id);
        temporada.setStatus(Constantes.STATUS_INACTIVO);
        temporada = actualiza(temporada);
        String nombre = temporada.getNombre();
        return nombre;
    }
    
  }


