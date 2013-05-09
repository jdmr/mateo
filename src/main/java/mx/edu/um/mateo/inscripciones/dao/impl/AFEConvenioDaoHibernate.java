/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.AFEConvenioDao;
import mx.edu.um.mateo.inscripciones.dao.AlumnoDao;
import mx.edu.um.mateo.inscripciones.model.AFEConvenio;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@Transactional
@Repository
public class AFEConvenioDaoHibernate extends BaseDao implements AFEConvenioDao{

    @Autowired
    private AlumnoDao alDao;
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de Convenios con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(AFEConvenio.class);
        Criteria countCriteria = currentSession().createCriteria(AFEConvenio.class);

         if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            criteria.createAlias("tipoBeca", "tb");
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("matricula", filtro,
                    MatchMode.START));
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
        } else {
            criteria.addOrder(Order.asc("matricula"));
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_REPORTE)) {
            criteria.setFirstResult((Integer) params.get(Constantes.CONTAINSKEY_OFFSET));
            criteria.setMaxResults((Integer) params.get(Constantes.CONTAINSKEY_MAX));
        }
        List<AFEConvenio> lista= criteria.list();
        params.put(Constantes.CONTAINSKEY_AFECONVENIO, lista);
        Iterator<AFEConvenio> itr = lista.iterator();
        log.debug("Entrando al ciclo");
        while(itr.hasNext()) {
            
         AFEConvenio convenio = itr.next();
         log.debug("{}",convenio.getMatricula());
         convenio.setAlumno(alDao.obtiene(convenio.getMatricula()));
         
        
        }
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    
    }

    @Override
    public AFEConvenio obtiene(final Long id) {
        
       log.debug("Obtiene convenio con id = {}", id);
                
        AFEConvenio afeConvenio = (AFEConvenio) currentSession().get(AFEConvenio.class, id);
        if(afeConvenio != null){
        afeConvenio.setAlumno(alDao.obtiene(afeConvenio.getMatricula()));
        }
        return afeConvenio;
    }

    
    @Override
    public void graba(final AFEConvenio afeConvenio, Usuario usuario) {
       Session session = currentSession();
        if (usuario != null) {
            afeConvenio.setEmpresa(usuario.getEmpresa());
        }
        try {
            currentSession().saveOrUpdate(afeConvenio);
       } catch (NonUniqueObjectException e) {
           try {
                currentSession().merge(afeConvenio);  
                currentSession().flush();
            } catch (Exception ex) {
                log.error("No se pudo actualizar el afeConvenio", ex);
                throw new RuntimeException("No se pudo actualizar el afeConvenio",
                        ex);
            }
       }finally{
            
       }
        
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public String elimina(final Long id) {
        log.debug("Eliminando el convenio {}", id);
        AFEConvenio afeConvenio = this.obtiene(id);
        log.debug("Checandoo Matricula");
        String matricula= afeConvenio.getAlumno().getMatricula();
        log.debug("{}", matricula);
        currentSession().delete(afeConvenio);

        currentSession().flush();
        
        return matricula ;

        
    }

    
    
}
