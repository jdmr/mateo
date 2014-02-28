/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.colportor.model.Colportor;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
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
        
        Criteria criteria = currentSession().createCriteria(TemporadaColportor.class);
        Criteria countCriteria = currentSession().createCriteria(TemporadaColportor.class);
        
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
            
            criteria.createAlias("temporada", "temp");
            criteria.createAlias("colportor", "clp");
            criteria.add(Restrictions.or(Restrictions.ilike("temp.nombre", filtro), 
                    Restrictions.ilike("clp.clave", filtro), 
                    Restrictions.ilike("clp.nombre", filtro), 
                    Restrictions.ilike("clp.apMaterno", filtro),
                    Restrictions.ilike("clp.apPaterno", filtro)));
            
            countCriteria.createAlias("temporada", "temp");
            countCriteria.createAlias("colportor", "clp");
            countCriteria.add(Restrictions.or(Restrictions.ilike("temp.nombre", filtro), 
                    Restrictions.ilike("clp.clave", filtro), 
                    Restrictions.ilike("clp.nombre", filtro), 
                    Restrictions.ilike("clp.apMaterno", filtro),
                    Restrictions.ilike("clp.apPaterno", filtro)));
            
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
    
    public Map<String, Object> listadoTemporadasPorColportor(Map<String, Object> params) {
        log.debug("listadoTemporadasPorColportor");
        Query criteria = currentSession()
                .createQuery("select tc from TemporadaColportor tc where tc.colportor.clave = :filtro and tc.colportor.empresa.id = :empresaId and tc.asociado.id = :asociadoId");
        criteria.setString("filtro", (String)params.get("filtro"));
        criteria.setLong("empresaId", (Long)params.get("empresa"));
        criteria.setLong("asociadoId", (Long)params.get("asociado"));
        
        params.put(Constantes.TEMPORADACOLPORTOR_LIST, criteria.list());
        return params;
    }

    public TemporadaColportor obtiene(Long id) {
        log.debug("Obtiene Temporada Colportor con id = {}", id);
        TemporadaColportor temporadaColportor = (TemporadaColportor) currentSession().get(TemporadaColportor.class, id);
        return temporadaColportor;
    }

    public TemporadaColportor obtiene(Colportor colportor) {
        log.debug("Obtiene Temporada Colportor con Colportor = {}", colportor.getId());
        Query sql = currentSession().createQuery("select tc from TemporadaColportor as tc inner join tc.colportor as clp where clp.id = :clpId and tc.status = :status ");
        sql.setLong("clpId", colportor.getId());
        sql.setString("status", Constantes.STATUS_ACTIVO);
        
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
        try{
            currentSession().update(temporadaColportor);
        }catch(NonUniqueObjectException e){
            currentSession().merge(temporadaColportor);
            currentSession().flush();
            
        }
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
