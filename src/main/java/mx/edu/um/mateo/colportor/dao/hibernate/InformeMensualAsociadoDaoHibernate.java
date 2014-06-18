/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import mx.edu.um.mateo.colportor.dao.InformeMensualAsociadoDao;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.InformeMensualAsociado;
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
public class InformeMensualAsociadoDaoHibernate extends BaseDao implements InformeMensualAsociadoDao {

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de informes del asociado con params {}", params);
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
        
        Criteria criteria = currentSession().createCriteria(InformeMensualAsociado.class);
        Criteria countCriteria = currentSession().createCriteria(InformeMensualAsociado.class);
        
        log.debug("empresa {}", params.get("empresa"));
        criteria.createAlias("asociado","asoc");
        criteria.createCriteria("empresa")
                .add(Restrictions.idEq(params.get("empresa")));

        countCriteria.createAlias("asociado","asc");
        countCriteria.createCriteria("empresa")
                .add(Restrictions.idEq(params.get("empresa")));
        
        if(params.get("asociado")!=null){
            criteria.add(Restrictions.eq("asoc.id", (Long)params.get("asociado")));
            countCriteria.add(Restrictions.eq("asoc.id", (Long)params.get("asociado")));
        }
        else{
            //No hay id del asociado
            log.debug("No se proporciono id del asociado");
            params.put(Constantes.INFORMEMENSUAL_LIST, new ArrayList());
            params.put(Constantes.CONTAINSKEY_CANTIDAD, 0L);
            return params;
        }
        
        if(params.get("fechaInicial") != null){
            criteria.add(Restrictions.ge("fechaRegistro", (Date)params.get("fechaInicial")));
            countCriteria.add(Restrictions.ge("fechaRegistro", (Long)params.get("fechaInicial")));
        }
        
        if(params.get("fechaFinal") != null){
            criteria.add(Restrictions.ge("fechaRegistro", (Date)params.get("fechaFinal")));
            countCriteria.add(Restrictions.ge("fechaRegistro", (Long)params.get("fechaFinal")));
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

    @Override
    public InformeMensualAsociado obtiene(Long id) {
        log.debug("Obtiene informeMensualAsociado con id = {}", id);
        InformeMensualAsociado informeMensual = (InformeMensualAsociado) currentSession().get(InformeMensualAsociado.class, id);
        return informeMensual;
    }

    @Override
    public InformeMensualAsociado obtiene(Asociado asoc, Long id) {
        log.debug("Obtiene informeMensualAsociado con id = {}", id);
        Criteria sql = getSession().createCriteria(InformeMensualAsociado.class);
        sql.createCriteria("asociado")
                .add(Restrictions.eq("id", asoc.getId()));
        sql.add(Restrictions.idEq(id));
        return (InformeMensualAsociado)sql.uniqueResult();
    }

    @Override
    public InformeMensualAsociado obtiene(Asociado asoc, Date fecha) {
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
        
        Criteria sql = getSession().createCriteria(InformeMensualAsociado.class);
        sql.createCriteria("asociado")
                .add(Restrictions.eq("id", asoc.getId()));
        sql.add(Restrictions.between("fecha", calTmp1.getTime(), calTmp2.getTime()));
        
        return (InformeMensualAsociado)sql.uniqueResult(); 
    }

    @Override
    public InformeMensualAsociado crea(InformeMensualAsociado informeMensual) {
        log.debug("Creando informeMensualAsociado : {}", informeMensual);
        try{
            currentSession().saveOrUpdate(informeMensual);
            currentSession().merge(informeMensual);
            currentSession().flush();
        }catch(Exception e){
            log.error("Error al intentar crear/actualizar el informe {}",e);
            currentSession().merge(informeMensual);
            
        }
        log.debug("informeMensualAsociado creado : {}", informeMensual);
        return informeMensual;
    }

    @Override
    public String elimina(Asociado asoc, Long id) {
        log.debug("Eliminando informeMensualAsociado con id {}", id);
        InformeMensualAsociado informeMensual = obtiene(asoc, id);
        Date fecha = informeMensual.getFechaRegistro();
        currentSession().delete(informeMensual);
        currentSession().flush();
        SimpleDateFormat sdf = new SimpleDateFormat ("MMM", local);
        return "Informe del mes "+sdf.format(fecha);
    }
    
}
