 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao.hibernate;
import java.text.SimpleDateFormat;
import mx.edu.um.mateo.colportor.dao.*;
import java.util.*;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 */
@Repository
@Transactional
public class InformeMensualDaoHibernate extends BaseDao implements InformeMensualDao {
    
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDao#lista(java.util.Map) 
    */
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
        
        log.debug("empresa {}", params.get("empresa"));
        if(params.get("empresa")!=null){
            criteria.createAlias("colportor","clp");
            criteria.createCriteria("clp.empresa")
                    .add(Restrictions.idEq(params.get("empresa")));
            
            countCriteria.createAlias("colportor","clp");
            countCriteria.createCriteria("clp.empresa")
                    .add(Restrictions.idEq(params.get("empresa")));
        
        }
        
        log.debug("Clave {}", params.get("clave"));
        if(params.get("clave")!=null){
            criteria.add(Restrictions.eq("clp.clave", (String)params.get("clave")));
            countCriteria.add(Restrictions.eq("clp.clave", (String)params.get("clave")));
        }
        else{
            //No hay clave del colportor
            log.debug("No se proporciono clave de colportor");
            params.put(Constantes.INFORMEMENSUAL_LIST, new ArrayList());
            params.put(Constantes.CONTAINSKEY_CANTIDAD, 0L);
            return params;
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
            log.debug("Se encontro una empresa");
            params.put(Constantes.INFORMEMENSUAL_LIST, criteria.list());
            countCriteria.setProjection(Projections.rowCount());
            params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));
         }else{
             log.debug("No se encontro una empresa");
            params.put(Constantes.INFORMEMENSUAL_LIST, new ArrayList());
            params.put(Constantes.CONTAINSKEY_CANTIDAD, 0L);
         }
       
         log.debug("Lista de informes {}", params.get(Constantes.INFORMEMENSUAL_LIST));

        return params;
    }
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDao#obtiene(java.lang.Long) 
     */
    public InformeMensual obtiene(Long id) {
        log.debug("Obtiene informeMensual con id = {}", id);
        InformeMensual informeMensual = (InformeMensual) currentSession().get(InformeMensual.class, id);
        return informeMensual;
    }
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDao#obtiene(mx.edu.um.mateo.colportor.model.Colportor, java.lang.Long) 
     */
    public InformeMensual obtiene(Colportor clp,Long id) {
        log.debug("Obtiene informeMensual con id = {}", id);
        Criteria sql = getSession().createCriteria(InformeMensual.class);
        sql.createCriteria("colportor")
                .add(Restrictions.eq("id", clp.getId()));
        sql.add(Restrictions.idEq(id));
        return (InformeMensual)sql.uniqueResult();
    }
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDao#obtiene(mx.edu.um.mateo.colportor.model.Colportor, java.util.Date) 
     */
    public InformeMensual obtiene(Colportor clp, Date fecha) {
        log.debug("Obtiene informeMensual con date = {}", fecha);
        
        Calendar cal = new GregorianCalendar(Locale.forLanguageTag("es"));
        cal.setTime(fecha);
        
        Calendar calTmp1 = Calendar.getInstance();
        calTmp1.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        calTmp1.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        calTmp1.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        
        Calendar calTmp2 = Calendar.getInstance();
        calTmp2.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        calTmp2.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        calTmp2.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        
        log.debug("cal1 {}, cal2 {}", calTmp1.getTime(), calTmp2.getTime());
        
        Criteria sql = getSession().createCriteria(InformeMensual.class);
        sql.createCriteria("colportor")
                .add(Restrictions.eq("id", clp.getId()));
        sql.add(Restrictions.between("fecha", calTmp1.getTime(), calTmp2.getTime()));
        
        return (InformeMensual)sql.uniqueResult(); 
    }
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDao#crea(mx.edu.um.mateo.colportor.model.InformeMensual) 
     */
    public InformeMensual crea(InformeMensual informeMensual) {
        log.debug("Creando informeMensual : {}", informeMensual);
        try{
            currentSession().saveOrUpdate(informeMensual);
            currentSession().merge(informeMensual);
            currentSession().flush();
        }catch(Exception e){
            log.error("Error al intentar crear/actualizar el informe {}",e);
            currentSession().merge(informeMensual);
            
        }
        log.debug("informeMensual creado : {}", informeMensual);
        return informeMensual;
    }
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDao#elimina(mx.edu.um.mateo.colportor.model.Colportor, java.lang.Long) 
     */
    public String elimina(Colportor clp, Long id){
        log.debug("Eliminando informeMensual con id {}", id);
        InformeMensual informeMensual = obtiene(clp, id);
        Date fecha = informeMensual.getFecha();
        currentSession().delete(informeMensual);
        currentSession().flush();
        SimpleDateFormat sdf = new SimpleDateFormat ("MMM", local);
        return "Informe del mes "+sdf.format(fecha);
    }
    
}