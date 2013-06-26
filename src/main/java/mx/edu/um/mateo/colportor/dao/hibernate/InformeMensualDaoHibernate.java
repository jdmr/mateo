 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao.hibernate;
import java.text.SimpleDateFormat;
import mx.edu.um.mateo.colportor.dao.*;
import java.util.*;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 */
@Repository
@Transactional
public class InformeMensualDaoHibernate extends BaseDao implements InformeMensualDao {
    
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de informeMensual con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }
        log.debug("paginado {}", params.containsKey(Constantes.CONTAINSKEY_MAX));
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
        
        Criteria criteria = currentSession().createCriteria(InformeMensual.class);
        Criteria countCriteria = currentSession().createCriteria(InformeMensual.class);
        
        if(params.get("empresa")!=null){
            criteria.createCriteria("colportor")
                .add(Restrictions.idEq(params.get("empresa")));
            
            countCriteria.createCriteria("colportor")
                .add(Restrictions.idEq(params.get("empresa")));
        
        }

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";            
            Disjunction propiedades = Restrictions.disjunction();
            //propiedades.add(Restrictions.ilike("tipoDeInformeMensual", filtro));
            //propiedades.add(Restrictions.ilike("folio", filtro));
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
        
         if(params.get("empresa") != null){
            params.put(Constantes.INFORMEMENSUAL_LIST, criteria.list());
            countCriteria.setProjection(Projections.rowCount());
            params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));
         }else{
            params.put(Constantes.INFORMEMENSUAL_LIST, new ArrayList());
            params.put(Constantes.CONTAINSKEY_CANTIDAD, 0L);
         }
       

        return params;
    }

    public InformeMensual obtiene(Long id) {
        log.debug("Obtiene informeMensual con id = {}", id);
        InformeMensual informeMensual = (InformeMensual) currentSession().get(InformeMensual.class, id);
        return informeMensual;
    }

    public InformeMensual crea(InformeMensual informeMensual) {
        log.debug("Creando informeMensual : {}", informeMensual);
        try{
            currentSession().saveOrUpdate(informeMensual);
        }catch(Exception e){
            currentSession().merge(informeMensual);
            currentSession().flush();
            
        }
        return informeMensual;
    }
    
    public String elimina(Long id){
        log.debug("Eliminando informeMensual con id {}", id);
        InformeMensual informeMensual = obtiene(id);
        Date fecha = informeMensual.getFecha();
        currentSession().delete(informeMensual);
        currentSession().flush();
        SimpleDateFormat sdf = new SimpleDateFormat ("MMM", local);
        return "Informe del mes "+sdf.format(fecha);
    }
    
    
}