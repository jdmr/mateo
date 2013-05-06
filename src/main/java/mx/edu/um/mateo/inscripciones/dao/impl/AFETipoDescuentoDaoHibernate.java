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
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.inscripciones.dao.AFETipoDescuentoDao;
import mx.edu.um.mateo.inscripciones.model.AFETipoDescuento;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@Repository
@Transactional
public class AFETipoDescuentoDaoHibernate extends BaseDao implements AFETipoDescuentoDao{
     
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
        Criteria criteria = currentSession().createCriteria(AFETipoDescuento.class);
        Criteria countCriteria = currentSession().createCriteria(AFETipoDescuento.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("descripcion", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_TIPODESCUENTOS, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));


        return params;
    }

    @Override
    public AFETipoDescuento obtiene(final Long id) {
        AFETipoDescuento afeTipoDescuento = (AFETipoDescuento) currentSession().get(AFETipoDescuento.class, id);
        if (afeTipoDescuento == null) {
            log.warn("uh oh, el descuento con id '" + id + "' no fue encontrado...");
            try {
                throw new ObjectRetrievalFailureException(AFETipoDescuento.class, id);
            } catch (ObjectRetrievalFailureException ex) {
                Logger.getLogger(AFETipoDescuentoDaoHibernate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return afeTipoDescuento;
    }
    
    @Override
    public void graba(AFETipoDescuento afeTipoDescuento, Organizacion organizacion) {
        if (organizacion != null) {
            afeTipoDescuento.setOrganizacion(organizacion);
        }
        try{
        currentSession().saveOrUpdate(afeTipoDescuento);
                   
        }catch(NonUniqueObjectException e){
            currentSession().merge(afeTipoDescuento);
        
        }finally{
            currentSession().flush(); 
        }    
    }

    @Override
    public String elimina(final Long id) {
       log.debug("Eliminando el tipo de descuento {}", id);
        AFETipoDescuento afeTipoDescuento = this.obtiene(id);
        String nombre = afeTipoDescuento.getDescripcion();
        afeTipoDescuento.setStatus("I");
        currentSession().flush();

        return nombre;
    }
}
