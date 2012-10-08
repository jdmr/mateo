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
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.dao.ConceptoDao;
import mx.edu.um.mateo.rh.model.Concepto;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author AMDA
 */
@Repository
@Transactional
public class ConceptoDaoHibernate extends BaseDao implements ConceptoDao {

   /**
    * @see mx.edu.um.mateo.rh.dao.ConceptoDao#lista(java.util.Map) 
    */
    @Override
    public Map<String, Object> lista(Map<String, Object> params){
        
        log.debug("Buscando lista de proveedores con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey("max")) {
            params.put("max", 10);
        } else {
            params.put("max", Math.min((Integer) params.get("max"), 100));
        }

        if (params.containsKey("pagina")) {
            Long pagina = (Long) params.get("pagina");
            Long offset = (pagina - 1) * (Integer) params.get("max");
            params.put("offset", offset.intValue());
        }

        if (!params.containsKey("offset")) {
            params.put("offset", 0);
        }
        Criteria criteria = currentSession().createCriteria(Concepto.class);
        Criteria countCriteria = currentSession().createCriteria(Concepto.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("descripcion", filtro, MatchMode.ANYWHERE));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey("order")) {
            String campo = (String) params.get("order");
            if (params.get("sort").equals("desc")) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONCEPTO_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
        
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.ConceptoDao#obtiene(java.lang.Integer) 
     */
    public Concepto obtiene (final Long id) throws ObjectRetrievalFailureException {
        Concepto concepto = (Concepto) currentSession().get(Concepto.class, id);
        if (concepto == null) {
            log.warn("uh oh, concepto with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Concepto.class, id);
        }

        return concepto;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.ConceptoDao#graba(mx.edu.um.mateo.rh.model.Concepto)  
     */    
    @Override
    public void graba(final Concepto concepto, Usuario usuario) {
        Session session = currentSession();
		if (usuario != null) {
			concepto.setEmpresa(usuario.getEmpresa());
		}
        currentSession().saveOrUpdate(concepto);
//        currentSession().merge(concepto);
        currentSession().flush();
        log.debug("concepto{}",concepto);
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.ConceptoDao#elimina(java.lang.Integer) 
     */
    @Override
    public String elimina(final Long id) throws ObjectRetrievalFailureException {
        Concepto concepto = this.obtiene(id); 
        String nombre = concepto.getNombre();
        currentSession().delete(concepto);
        currentSession().flush();
        return nombre;
    }
    
  
}

