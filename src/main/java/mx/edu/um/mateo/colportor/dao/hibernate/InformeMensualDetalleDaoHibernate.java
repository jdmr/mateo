 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao.hibernate;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import mx.edu.um.mateo.colportor.dao.*;
import java.util.*;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;
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
public class InformeMensualDetalleDaoHibernate extends BaseDao implements InformeMensualDetalleDao {
    
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de informeMensualDetalle con params {}", params);
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
        
        Criteria criteria = currentSession().createCriteria(InformeMensualDetalle.class);
        Criteria countCriteria = currentSession().createCriteria(InformeMensualDetalle.class);
        
        if(params.get("informe") != null){
            criteria.createCriteria("informeMensual")
                    .add(Restrictions.idEq(params.get("informe")));
            countCriteria.createCriteria("informeMensual")
                    .add(Restrictions.idEq(params.get("informe")));
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
        params.put(Constantes.INFORMEMENSUAL_DETALLE_LIST, criteria.list());
        
        //Es necesario completar en este listado el mes completo
        List <InformeMensualDetalle> infs = (List<InformeMensualDetalle>)params.get(Constantes.INFORMEMENSUAL_DETALLE_LIST);
        Calendar gcFecha = new GregorianCalendar(TimeZone.getTimeZone("America/Monterrey"), Locale.US);
        if(infs.size() > 0){
            gcFecha.setTime(infs.get(0).getFecha());
            Map <Date, InformeMensualDetalle> informes = new TreeMap <>();
            
            //Llenar el map con todos los dias del mes
            Calendar tmpCalendar = new GregorianCalendar(TimeZone.getTimeZone("America/Monterrey"), Locale.US);
            Boolean inicioSemana = false;
            Integer diaSemana = 0;
            InformeMensualDetalle tmp = null;
            
            for(int i = gcFecha.getActualMinimum(Calendar.DATE); i <= gcFecha.getActualMaximum(Calendar.DATE ); i++){
                gcFecha.set(Calendar.DATE, i);
                
                //Si no se ha evaluado el inicio de semana
                if(!inicioSemana){
                    tmpCalendar.setTime(gcFecha.getTime());
                    diaSemana = tmpCalendar.get(Calendar.DAY_OF_WEEK);
                    log.debug("Dia de la semana {}", diaSemana);
                    
                    for(int j = 1; j < diaSemana; j++){
                        tmpCalendar.add(Calendar.DAY_OF_WEEK, j *-1);
                        log.debug("Fecha previa {}", tmpCalendar.getTime());
                        tmp = new InformeMensualDetalle();
                        tmp.setFecha(tmpCalendar.getTime());                    
                        informes.put(tmp.getFecha(), tmp);
                        tmpCalendar.setTime(gcFecha.getTime()); //Asignar la fecha original
                    }
                    inicioSemana = true;
                }
                tmp = new InformeMensualDetalle();
                tmp.setFecha(gcFecha.getTime());                    
                informes.put(tmp.getFecha(), tmp);
            }
            
            log.debug("Fecha inicial {}",gcFecha.getActualMinimum(Calendar.DATE));
            log.debug("Fecha final {}",gcFecha.getActualMaximum(Calendar.DATE));
            
            //recorrer el listado y guardarlo en el map
            tmp = null;
            for(InformeMensualDetalle imd : infs){
                gcFecha.setTime(imd.getFecha());
                tmp = informes.get(gcFecha.getTime());
                tmp.setBautizados(tmp.getBautizados() + imd.getBautizados());
                tmp.setCasasVisitadas(tmp.getCasasVisitadas() + imd.getCasasVisitadas());
                tmp.setContactosEstudiosBiblicos(tmp.getContactosEstudiosBiblicos() + imd.getContactosEstudiosBiblicos());
                tmp.setDiezmo(tmp.getDiezmo().add(imd.getDiezmo()));
                tmp.setHrsTrabajadas(tmp.getHrsTrabajadas()+imd.getHrsTrabajadas());
                tmp.setLiteraturaGratis(tmp.getLiteraturaGratis() + imd.getLiteraturaGratis());
                tmp.setLiteraturaVendida(tmp.getLiteraturaVendida() + imd.getLiteraturaVendida());
                tmp.setOracionesOfrecidas(tmp.getOracionesOfrecidas() + imd.getOracionesOfrecidas());
                tmp.setTotalPedidos(tmp.getTotalPedidos().add(imd.getTotalPedidos()));
                tmp.setTotalVentas(tmp.getTotalVentas().add(imd.getTotalVentas()));
                tmp.setFecha(imd.getFecha());
                tmp.setCapturo(imd.getCapturo());
                tmp.setFechaCaptura(imd.getFechaCaptura());
                tmp.setId(imd.getId());
                tmp.setInformeMensual(imd.getInformeMensual());
                tmp.setVersion(imd.getVersion());
            }
            
            params.put(Constantes.INFORMEMENSUAL_DETALLE_LIST, new ArrayList(informes.values()));
        }
        
        //Quitamos paginado de esta opcion para que se muestre el mes completo en el jsp
        params.put(Constantes.CONTAINSKEY_CANTIDAD, 0L);
       

        return params;
    }

    public InformeMensualDetalle obtiene(Long id) {
        log.debug("Obtiene informeMensualDetalle con id = {}", id);
        InformeMensualDetalle detalle = (InformeMensualDetalle) currentSession().get(InformeMensualDetalle.class, id);
        return detalle;
    }

    public InformeMensualDetalle crea(InformeMensualDetalle detalle) {
        log.debug("Creando informeMensualDetalle : {}", detalle);
        try{
            currentSession().saveOrUpdate(detalle);
        }catch(Exception e){
            currentSession().merge(detalle);
            currentSession().flush();
            
        }
        log.debug("Creando informeMensualDetalle : {}", detalle);
        return detalle;
    }
    
    public String elimina(Long id){
        log.debug("Eliminando informeMensualDetalle con id {}", id);
        InformeMensualDetalle detalle = obtiene(id);
        Date fecha = detalle.getFecha();
        currentSession().delete(detalle);
        currentSession().flush();
        SimpleDateFormat sdf = new SimpleDateFormat ("MMM", local);
        return "Detalle del mes "+sdf.format(fecha);
    }
    
    
}