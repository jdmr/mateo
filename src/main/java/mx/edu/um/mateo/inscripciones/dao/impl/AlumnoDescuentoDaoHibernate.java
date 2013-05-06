/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.AlumnoDescuentoDao;
import mx.edu.um.mateo.inscripciones.model.AlumnoDescuento;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@Repository
@Transactional
public class AlumnoDescuentoDaoHibernate extends BaseDao implements AlumnoDescuentoDao{
    
    
    @Override
   public Map<String, Object> lista(Map<String, Object> params) {

        log.debug("Buscando lista de descuentos con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(AlumnoDescuento.class);
        Criteria countCriteria = currentSession().createCriteria(AlumnoDescuento.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            log.debug("AQUIII");
            criteria.createAlias("descuento", "dc");
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            criteria.add(Restrictions.disjunction()
                    .add(Restrictions.like("matricula", filtro))
                    .add(Restrictions.ilike("dc.descripcion", filtro)));

            countCriteria.createAlias("descuento", "dc");
            countCriteria.add(Restrictions.disjunction()
                    .add(Restrictions.like("matricula", filtro))
                    .add(Restrictions.like("dc.descripcion", filtro)));
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
        params.put(Constantes.CONTAINSKEY_ALUMNODESCUENTOS, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));


        return params;
    }
    
    @Override
    public AlumnoDescuento obtiene(final Long id) {
        AlumnoDescuento alumnoDescuento = (AlumnoDescuento) currentSession().get(AlumnoDescuento.class, id);
        if (alumnoDescuento == null) {
            log.warn("uh oh, el descuento del Alumno con id '" + id + "' no fue encontrado...");
            try {
                throw new ObjectRetrievalFailureException(AlumnoDescuento.class, id);
            } catch (ObjectRetrievalFailureException ex) {
                Logger.getLogger(AlumnoDescuentoDaoHibernate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return alumnoDescuento;
    }
    
    @Override
    public void graba(AlumnoDescuento alumnoDescuento, Usuario usuario) {
        if (usuario != null) {
            alumnoDescuento.setUsuario(usuario);
        }
        try{
        currentSession().saveOrUpdate(alumnoDescuento);
                   
        }catch(NonUniqueObjectException e){
            currentSession().merge(alumnoDescuento);
        
        }finally{
            currentSession().flush(); 
        }    
    }
    
     @Override
    public String elimina(final Long id) {
       log.debug("Eliminando el descuento de Alumno {}", id);
        AlumnoDescuento alumnoDescuento = this.obtiene(id);
        String nombre = alumnoDescuento.getMatricula();
        currentSession().delete(alumnoDescuento);
        currentSession().flush();

        return nombre;
    }
}
