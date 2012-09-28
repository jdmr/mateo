/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.NacionalidadDao;
import mx.edu.um.mateo.rh.model.Nacionalidad;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Zorch
 */
@Repository
@Transactional
public class NacionalidadDaoHibernate extends BaseDao implements NacionalidadDao{

    
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de nacionalidades con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Nacionalidad.class);
        Criteria countCriteria = currentSession().createCriteria(Nacionalidad.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_NACIONALIDADES, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public Nacionalidad obtiene(Long id) {
        log.debug("Obtiene nacionalidad con id = {}", id);
        Nacionalidad nacionalidad = (Nacionalidad) currentSession().get(Nacionalidad.class, id);
        if(nacionalidad==null){
            log.warn("uh oh, la categoria con el id"+id+"no se encontro...");
            throw new ObjectRetrievalFailureException(Nacionalidad.class, id);
            
        }
        return nacionalidad;
    }

   
    
    @Override
    public void graba(final Nacionalidad nacionalidad, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            nacionalidad.setEmpresa(usuario.getEmpresa());
        }
        currentSession().saveOrUpdate(nacionalidad);
        currentSession().merge(nacionalidad);
        currentSession().flush();
      
        
    }

    

    @Override
    public String elimina( Long id)  {
        log.debug("Eliminando nacionalidad con id {}", id);
        Nacionalidad nacionalidad = obtiene(id);
        nacionalidad.setStatus("I");
        currentSession().delete(nacionalidad);
        currentSession().flush();
        String nombre = nacionalidad.getNombre().toString();
        return nombre;
    }

   
}