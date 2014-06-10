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
import mx.edu.um.mateo.colportor.model.InformeMensualDetalleVO;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 */
@Repository
@Transactional
public class InformeMensualDetalleDaoHibernate extends BaseDao implements InformeMensualDetalleDao {
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#lista(java.util.Map) 
     */
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
                        
                        if(tmpCalendar.get(Calendar.MONTH) != gcFecha.get(Calendar.MONTH)){
                            tmp.setInformeMensual(new InformeMensual("@@@"));//para identificar este tipo de registros
                        }
                        
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
                try {
                    tmp.setDiezmo(tmp.getDiezmo().add(imd.getDiezmo()));
                } catch (NullPointerException e) {
                    ;
                }
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
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#obtiene(java.lang.Long) 
     */
    public InformeMensualDetalle obtiene(Long id) {
        log.debug("Obtiene informeMensualDetalle con id = {}", id);
        InformeMensualDetalle detalle = (InformeMensualDetalle) currentSession().get(InformeMensualDetalle.class, id);
        return detalle;
    }
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#obtiene(mx.edu.um.mateo.colportor.model.InformeMensual, java.lang.Long) 
     */
    public InformeMensualDetalle obtiene(InformeMensual informe, Long id) {
        log.debug("Obtiene informeMensualDetalle con id = {}", id);
        Criteria sql = getSession().createCriteria(InformeMensualDetalle.class);
        sql.add(Restrictions.idEq(id));
        sql.createCriteria("informe").add(Restrictions.idEq(informe.getId()));
        return (InformeMensualDetalle)sql.uniqueResult();
    }
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#crea(mx.edu.um.mateo.colportor.model.InformeMensualDetalle) 
     */
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
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#crear(mx.edu.um.mateo.colportor.model.InformeMensualDetalle) 
     */
    public InformeMensualDetalle crear(InformeMensualDetalle detalle) throws ConstraintViolationException {
        log.debug("Creando informeMensualDetalle : {}", detalle);
        currentSession().save(detalle);
        return detalle;
    }
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#elimina(java.lang.Long) 
     */
    public String elimina(Long id){
        log.debug("Eliminando informeMensualDetalle con id {}", id);
        InformeMensualDetalle detalle = obtiene(id);
        Date fecha = detalle.getFecha();
        currentSession().delete(detalle);
        currentSession().flush();
        SimpleDateFormat sdf = new SimpleDateFormat ("MMM", local);
        return "Detalle del mes "+sdf.format(fecha);
    }
    
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#listaInformes(java.util.Map) 
     */
    public Map<String, Object> listaInformes(Map<String, Object> params){
        StringBuilder query = new StringBuilder();
        query.append("select im.colportor_id, coalesce(sum(horas_trabajadas),0) as horasTrabajadas, coalesce(sum(total_pedidos),0.0) as pedidos, coalesce(sum(libros_ventas),0.0) as ventas,  ");
        query.append("coalesce(sum(literatura_gratis),0) as literaturaGratis, coalesce(sum(oraciones_ofrecidas),0) as oraciones, coalesce(sum(casas_visitadas),0) as casasVisitadas, ");
        query.append("coalesce(sum(estudios_biblicos),0) as estudiosBiblicos, coalesce(sum(bautizados),0) as bautizados, coalesce(sum(diezmo),0.0) as diezmos, ");
        query.append("coalesce(sum(libros_vendidos),0) as librosVendidos ");
        query.append("from ");
        query.append("( ");
        query.append("select * ");
        query.append("from informe_mensual_detalle ");
        query.append(") imd, ");
        query.append("( ");
        query.append("select * ");
        query.append("from informe_mensual ");
        query.append("where mes_informe between :fechaInicio and :fechaFinal ");
        query.append(") im ");
        query.append("where imd.informemensual_id = im.id ");
        query.append("and im.colportor_id in ");
        query.append("( ");
        query.append("select colportor_id ");
        query.append("from temporada_colportor ");
        query.append("where asociado_id = :asociadoId ");
        query.append(") ");
        query.append("group by im.colportor_id ");
        
        SQLQuery sql = getSession().createSQLQuery(query.toString());
        sql.setDate("fechaInicio", (Date)params.get("fechaInicio"));
        sql.setDate("fechaFinal", (Date)params.get("fechaFinal"));
        sql.setLong("asociadoId", (Long)params.get("asociado"));
        
        sql.addScalar("colportor_id", StandardBasicTypes.LONG);
        sql.addScalar("horasTrabajadas", StandardBasicTypes.DOUBLE);
        sql.addScalar("pedidos", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("ventas", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("literaturaGratis", StandardBasicTypes.INTEGER);
        sql.addScalar("oraciones", StandardBasicTypes.INTEGER);
        sql.addScalar("casasVisitadas", StandardBasicTypes.INTEGER);
        sql.addScalar("estudiosBiblicos", StandardBasicTypes.INTEGER);
        sql.addScalar("bautizados", StandardBasicTypes.INTEGER);
        sql.addScalar("diezmos", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("librosVendidos", StandardBasicTypes.INTEGER);
        
        Object [] objs = null;
        List <Object[]>lista = sql.list();
        InformeMensualDetalle detalle = null;
        List <InformeMensualDetalle> detalles = new ArrayList <> ();
        
        Iterator <Object[]> it = lista.iterator();
        while(it.hasNext()){
            objs = it.next();
            detalle = new InformeMensualDetalle();
            detalle.setInformeMensual(new InformeMensual());
            
            System.out.println(objs[0]);
            detalle.getInformeMensual().getColportor().setId((Long)objs[0]);
            System.out.println(objs[1]);
            try{
                detalle.setHrsTrabajadas((Double)objs[1]);
            }catch(NullPointerException e){
                detalle.setHrsTrabajadas(0.0);
            }
            System.out.println(objs[2]);
            try{
                detalle.setTotalPedidos((BigDecimal)objs[2]);
            }catch(NullPointerException e){
                detalle.setTotalPedidos(BigDecimal.ZERO);
            }
            System.out.println(objs[3]);
            try{
                detalle.setTotalVentas((BigDecimal)objs[3]);
            }catch(NullPointerException e){
                detalle.setTotalVentas(BigDecimal.ZERO);
            }
            System.out.println(objs[4]);
            try{
                detalle.setLiteraturaGratis((Integer)objs[4]);
            }catch(NullPointerException e){
                detalle.setLiteraturaGratis(0);
            }
            System.out.println(objs[5]);
            try{
                detalle.setOracionesOfrecidas((Integer)objs[5]);
            }catch(NullPointerException e){
                detalle.setOracionesOfrecidas(0);
            }
            System.out.println(objs[6]);
            try{
                detalle.setCasasVisitadas((Integer)objs[6]);
            }catch(NullPointerException e){
                detalle.setCasasVisitadas(0);
            }
            System.out.println(objs[7]);
            try{
                detalle.setContactosEstudiosBiblicos((Integer)objs[7]);
            }catch(NullPointerException e){
                detalle.setContactosEstudiosBiblicos(0);
            }
            System.out.println(objs[8]);
            try{
                detalle.setBautizados((Integer)objs[8]);
            }catch(NullPointerException e){
                detalle.setBautizados((Integer)objs[8]);
            }
            System.out.println(objs[9]);
            try{
                detalle.setDiezmo((BigDecimal)objs[9]);
            }catch(NullPointerException e){
                detalle.setDiezmo(BigDecimal.ZERO);
            }
            System.out.println(objs[10]);
            try{
                detalle.setLiteraturaVendida((Integer)objs[10]);
            }catch(NullPointerException e){
                detalle.setLiteraturaVendida(0);
            }
            
            detalles.add(detalle);
        }
        params.put(Constantes.INFORMEMENSUAL_DETALLE_LIST, detalles);
        
        return params;
    }
    
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#listaInformesConcentradoAsociadosAsociacion(java.util.Map) 
     */
    public Map<String, Object> listaInformesConcentradoAsociadosAsociacion(Map<String, Object> params){
        StringBuilder query = new StringBuilder();
        query.append("select tc.asociado_id, coalesce(sum(horas_trabajadas),0) as horasTrabajadas, coalesce(sum(total_pedidos),0.0) as pedidos, coalesce(sum(libros_ventas),0.0) as ventas,  ");
        query.append("coalesce(sum(literatura_gratis),0) as literaturaGratis, coalesce(sum(oraciones_ofrecidas),0) as oraciones, coalesce(sum(casas_visitadas),0) as casasVisitadas, ");
        query.append("coalesce(sum(estudios_biblicos),0) as estudiosBiblicos, coalesce(sum(bautizados),0) as bautizados, coalesce(sum(diezmo),0.0) as diezmos, ");
        query.append("coalesce(sum(libros_vendidos),0) as librosVendidos ");
        query.append("from ");
        query.append("( ");
        query.append("select * ");
        query.append("from informe_mensual_detalle ");
        query.append(") imd, ");
        query.append("( ");
        query.append("select * ");
        query.append("from informe_mensual ");
        query.append("where mes_informe between :fechaInicio and :fechaFinal ");
        query.append(") im, ");
        query.append("( ");
        query.append("select asociado_id, colportor_id ");
        query.append("from temporada_colportor ");
        query.append("where asociado_id in ");
        query.append("(select id from usuarios where empresa_id = :empresaId) ");
        query.append(")tc ");
        query.append("where imd.informemensual_id = im.id ");
        query.append("and tc.colportor_id = im.colportor_id ");
        query.append("group by tc.asociado_id ");
        
        SQLQuery sql = getSession().createSQLQuery(query.toString());
        sql.setDate("fechaInicio", (Date)params.get("fechaInicio"));
        sql.setDate("fechaFinal", (Date)params.get("fechaFinal"));
        sql.setLong("empresaId", (Long)params.get("empresa"));
        
        sql.addScalar("asociado_id", StandardBasicTypes.LONG);
        sql.addScalar("horasTrabajadas", StandardBasicTypes.DOUBLE);
        sql.addScalar("pedidos", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("ventas", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("literaturaGratis", StandardBasicTypes.INTEGER);
        sql.addScalar("oraciones", StandardBasicTypes.INTEGER);
        sql.addScalar("casasVisitadas", StandardBasicTypes.INTEGER);
        sql.addScalar("estudiosBiblicos", StandardBasicTypes.INTEGER);
        sql.addScalar("bautizados", StandardBasicTypes.INTEGER);
        sql.addScalar("diezmos", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("librosVendidos", StandardBasicTypes.INTEGER);
        
        Object [] objs = null;
        List <Object[]>lista = sql.list();
        InformeMensualDetalle detalle = null;
        List <InformeMensualDetalle> detalles = new ArrayList <> ();
        
        Iterator <Object[]> it = lista.iterator();
        while(it.hasNext()){
            objs = it.next();
            detalle = new InformeMensualDetalle();
            detalle.setInformeMensual(new InformeMensual());
            
            System.out.println(objs[0]);
            detalle.getInformeMensual().getColportor().setId((Long)objs[0]);
            System.out.println(objs[1]);
            try{
                detalle.setHrsTrabajadas((Double)objs[1]);
            }catch(NullPointerException e){
                detalle.setHrsTrabajadas(0.0);
            }
            System.out.println(objs[2]);
            try{
                detalle.setTotalPedidos((BigDecimal)objs[2]);
            }catch(NullPointerException e){
                detalle.setTotalPedidos(BigDecimal.ZERO);
            }
            System.out.println(objs[3]);
            try{
                detalle.setTotalVentas((BigDecimal)objs[3]);
            }catch(NullPointerException e){
                detalle.setTotalVentas(BigDecimal.ZERO);
            }
            System.out.println(objs[4]);
            try{
                detalle.setLiteraturaGratis((Integer)objs[4]);
            }catch(NullPointerException e){
                detalle.setLiteraturaGratis(0);
            }
            System.out.println(objs[5]);
            try{
                detalle.setOracionesOfrecidas((Integer)objs[5]);
            }catch(NullPointerException e){
                detalle.setOracionesOfrecidas(0);
            }
            System.out.println(objs[6]);
            try{
                detalle.setCasasVisitadas((Integer)objs[6]);
            }catch(NullPointerException e){
                detalle.setCasasVisitadas(0);
            }
            System.out.println(objs[7]);
            try{
                detalle.setContactosEstudiosBiblicos((Integer)objs[7]);
            }catch(NullPointerException e){
                detalle.setContactosEstudiosBiblicos(0);
            }
            System.out.println(objs[8]);
            try{
                detalle.setBautizados((Integer)objs[8]);
            }catch(NullPointerException e){
                detalle.setBautizados((Integer)objs[8]);
            }
            System.out.println(objs[9]);
            try{
                detalle.setDiezmo((BigDecimal)objs[9]);
            }catch(NullPointerException e){
                detalle.setDiezmo(BigDecimal.ZERO);
            }
            System.out.println(objs[10]);
            try{
                detalle.setLiteraturaVendida((Integer)objs[10]);
            }catch(NullPointerException e){
                detalle.setLiteraturaVendida(0);
            }
            
            detalles.add(detalle);
        }
        params.put(Constantes.INFORMEMENSUAL_DETALLE_LIST, detalles);
        
        return params;
    }
    
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#listaInformesConcentradoPorColportor(java.util.Map) 
     */
    public Map<String, Object> listaInformesConcentradoPorColportor(Map<String, Object> params){
        StringBuilder query = new StringBuilder();
        query.append("select im.colportor_id, to_char(im.mes_informe,'MM/yyyy') as mes, coalesce(sum(horas_trabajadas),0) as horasTrabajadas, coalesce(sum(total_pedidos),0.0) as pedidos, coalesce(sum(libros_ventas),0.0) as ventas,  ");
        query.append("coalesce(sum(literatura_gratis),0) as literaturaGratis, coalesce(sum(oraciones_ofrecidas),0) as oraciones, coalesce(sum(casas_visitadas),0) as casasVisitadas, ");
        query.append("coalesce(sum(estudios_biblicos),0) as estudiosBiblicos, coalesce(sum(bautizados),0) as bautizados, coalesce(sum(diezmo),0.0) as diezmos, ");
        query.append("coalesce(sum(libros_vendidos),0) as librosVendidos ");
        query.append("from ");
        query.append("( ");
        query.append("select * ");
        query.append("from informe_mensual_detalle ");
        query.append(") imd, ");
        query.append("( ");
        query.append("select * ");
        query.append("from informe_mensual ");
        query.append("where mes_informe between :fechaInicio and :fechaFinal ");
        query.append("and colportor_id = :colportorId ");
        query.append(") im ");
        query.append("where imd.informemensual_id = im.id  ");
        
        if(params.containsKey("asociado") && params.get("asociado") != null){
            query.append("and im.colportor_id in ");
            query.append("( ");
            query.append("select colportor_id  ");
            query.append("from temporada_colportor ");
            query.append("where asociado_id = :asociadoId ");
            query.append(") ");
        }
        
        query.append("group by im.colportor_id, to_char(im.mes_informe,'MM/yyyy') ");
        query.append("order by 1,2 ");
        
        SQLQuery sql = getSession().createSQLQuery(query.toString());
        sql.setDate("fechaInicio", (Date)params.get("fechaInicio"));
        sql.setDate("fechaFinal", (Date)params.get("fechaFinal"));
        
        if(params.containsKey("asociado") && params.get("asociado") != null){
            sql.setLong("asociadoId", (Long)params.get("asociado"));
        }
        
        sql.setLong("colportorId", (Long)params.get("colportor"));
        
        sql.addScalar("colportor_id", StandardBasicTypes.LONG);
        sql.addScalar("mes", StandardBasicTypes.STRING);
        sql.addScalar("horasTrabajadas", StandardBasicTypes.DOUBLE);
        sql.addScalar("pedidos", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("ventas", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("literaturaGratis", StandardBasicTypes.INTEGER);
        sql.addScalar("oraciones", StandardBasicTypes.INTEGER);
        sql.addScalar("casasVisitadas", StandardBasicTypes.INTEGER);
        sql.addScalar("estudiosBiblicos", StandardBasicTypes.INTEGER);
        sql.addScalar("bautizados", StandardBasicTypes.INTEGER);
        sql.addScalar("diezmos", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("librosVendidos", StandardBasicTypes.INTEGER);
        
        Object [] objs = null;
        List <Object[]>lista = sql.list();
        InformeMensualDetalleVO detalle = null;
        List <InformeMensualDetalleVO> detalles = new ArrayList <> ();
        
        Iterator <Object[]> it = lista.iterator();
        while(it.hasNext()){
            objs = it.next();
            detalle = new InformeMensualDetalleVO();
            detalle.setInformeMensual(new InformeMensual());
            
            System.out.println(objs[0]);
            detalle.getInformeMensual().getColportor().setId((Long)objs[0]);
            detalle.setMesInforme((String)objs[1]);
            
            System.out.println(objs[2]);
            try{
                detalle.setHrsTrabajadas((Double)objs[2]);
            }catch(NullPointerException e){
                detalle.setHrsTrabajadas(0.0);
            }
            System.out.println(objs[3]);
            try{
                detalle.setTotalPedidos((BigDecimal)objs[3]);
            }catch(NullPointerException e){
                detalle.setTotalPedidos(BigDecimal.ZERO);
            }
            System.out.println(objs[4]);
            try{
                detalle.setTotalVentas((BigDecimal)objs[4]);
            }catch(NullPointerException e){
                detalle.setTotalVentas(BigDecimal.ZERO);
            }
            System.out.println(objs[5]);
            try{
                detalle.setLiteraturaGratis((Integer)objs[5]);
            }catch(NullPointerException e){
                detalle.setLiteraturaGratis(0);
            }
            System.out.println(objs[6]);
            try{
                detalle.setOracionesOfrecidas((Integer)objs[6]);
            }catch(NullPointerException e){
                detalle.setOracionesOfrecidas(0);
            }
            System.out.println(objs[7]);
            try{
                detalle.setCasasVisitadas((Integer)objs[7]);
            }catch(NullPointerException e){
                detalle.setCasasVisitadas(0);
            }
            System.out.println(objs[8]);
            try{
                detalle.setContactosEstudiosBiblicos((Integer)objs[8]);
            }catch(NullPointerException e){
                detalle.setContactosEstudiosBiblicos(0);
            }
            System.out.println(objs[9]);
            try{
                detalle.setBautizados((Integer)objs[9]);
            }catch(NullPointerException e){
                detalle.setBautizados((Integer)objs[9]);
            }
            System.out.println(objs[10]);
            try{
                detalle.setDiezmo((BigDecimal)objs[10]);
            }catch(NullPointerException e){
                detalle.setDiezmo(BigDecimal.ZERO);
            }
            System.out.println(objs[11]);
            try{
                detalle.setLiteraturaVendida((Integer)objs[11]);
            }catch(NullPointerException e){
                detalle.setLiteraturaVendida(0);
            }
            
            detalles.add(detalle);
        }
        params.put(Constantes.INFORMEMENSUAL_DETALLE_LIST, detalles);
        
        return params;
    }
    
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#listaInformesConcentradoAnualPorColportor(java.util.Map) 
     */
    public Map<String, Object> listaInformesConcentradoAnualPorColportor(Map<String, Object> params){
        StringBuilder query = new StringBuilder();
        query.append("select im.colportor_id, to_char(im.mes_informe,'yyyy') as mes, coalesce(sum(horas_trabajadas),0) as horasTrabajadas, coalesce(sum(total_pedidos),0.0) as pedidos, coalesce(sum(libros_ventas),0.0) as ventas,  ");
        query.append("coalesce(sum(literatura_gratis),0) as literaturaGratis, coalesce(sum(oraciones_ofrecidas),0) as oraciones, coalesce(sum(casas_visitadas),0) as casasVisitadas, ");
        query.append("coalesce(sum(estudios_biblicos),0) as estudiosBiblicos, coalesce(sum(bautizados),0) as bautizados, coalesce(sum(diezmo),0.0) as diezmos, ");
        query.append("coalesce(sum(libros_vendidos),0) as librosVendidos ");
        query.append("from ");
        query.append("( ");
        query.append("select * ");
        query.append("from informe_mensual_detalle ");
        query.append(") imd, ");
        query.append("( ");
        query.append("select * ");
        query.append("from informe_mensual ");
        query.append("where colportor_id = :colportorId ");
        query.append(") im ");
        query.append("where imd.informemensual_id = im.id  ");
        
        if(params.containsKey("asociado") && params.get("asociado") != null){
            query.append("and im.colportor_id in ");
            query.append("( ");
            query.append("select colportor_id  ");
            query.append("from temporada_colportor ");
            query.append("where asociado_id = :asociadoId ");
            query.append(") ");
        }
        
        query.append("group by im.colportor_id, to_char(im.mes_informe,'yyyy') ");
        query.append("order by 1,2 ");
        
        SQLQuery sql = getSession().createSQLQuery(query.toString());
        
        if(params.containsKey("asociado") && params.get("asociado") != null){
            sql.setLong("asociadoId", (Long)params.get("asociado"));
        }
        
        sql.setLong("colportorId", (Long)params.get("colportor"));
        
        sql.addScalar("colportor_id", StandardBasicTypes.LONG);
        sql.addScalar("mes", StandardBasicTypes.STRING);
        sql.addScalar("horasTrabajadas", StandardBasicTypes.DOUBLE);
        sql.addScalar("pedidos", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("ventas", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("literaturaGratis", StandardBasicTypes.INTEGER);
        sql.addScalar("oraciones", StandardBasicTypes.INTEGER);
        sql.addScalar("casasVisitadas", StandardBasicTypes.INTEGER);
        sql.addScalar("estudiosBiblicos", StandardBasicTypes.INTEGER);
        sql.addScalar("bautizados", StandardBasicTypes.INTEGER);
        sql.addScalar("diezmos", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("librosVendidos", StandardBasicTypes.INTEGER);
        
        Object [] objs = null;
        List <Object[]>lista = sql.list();
        InformeMensualDetalleVO detalle = null;
        List <InformeMensualDetalleVO> detalles = new ArrayList <> ();
        
        Iterator <Object[]> it = lista.iterator();
        while(it.hasNext()){
            objs = it.next();
            detalle = new InformeMensualDetalleVO();
            detalle.setInformeMensual(new InformeMensual());
            
            System.out.println(objs[0]);
            detalle.getInformeMensual().getColportor().setId((Long)objs[0]);
            detalle.setMesInforme((String)objs[1]);
            
            System.out.println(objs[2]);
            try{
                detalle.setHrsTrabajadas((Double)objs[2]);
            }catch(NullPointerException e){
                detalle.setHrsTrabajadas(0.0);
            }
            System.out.println(objs[3]);
            try{
                detalle.setTotalPedidos((BigDecimal)objs[3]);
            }catch(NullPointerException e){
                detalle.setTotalPedidos(BigDecimal.ZERO);
            }
            System.out.println(objs[4]);
            try{
                detalle.setTotalVentas((BigDecimal)objs[4]);
            }catch(NullPointerException e){
                detalle.setTotalVentas(BigDecimal.ZERO);
            }
            System.out.println(objs[5]);
            try{
                detalle.setLiteraturaGratis((Integer)objs[5]);
            }catch(NullPointerException e){
                detalle.setLiteraturaGratis(0);
            }
            System.out.println(objs[6]);
            try{
                detalle.setOracionesOfrecidas((Integer)objs[6]);
            }catch(NullPointerException e){
                detalle.setOracionesOfrecidas(0);
            }
            System.out.println(objs[7]);
            try{
                detalle.setCasasVisitadas((Integer)objs[7]);
            }catch(NullPointerException e){
                detalle.setCasasVisitadas(0);
            }
            System.out.println(objs[8]);
            try{
                detalle.setContactosEstudiosBiblicos((Integer)objs[8]);
            }catch(NullPointerException e){
                detalle.setContactosEstudiosBiblicos(0);
            }
            System.out.println(objs[9]);
            try{
                detalle.setBautizados((Integer)objs[9]);
            }catch(NullPointerException e){
                detalle.setBautizados((Integer)objs[9]);
            }
            System.out.println(objs[10]);
            try{
                detalle.setDiezmo((BigDecimal)objs[10]);
            }catch(NullPointerException e){
                detalle.setDiezmo(BigDecimal.ZERO);
            }
            System.out.println(objs[11]);
            try{
                detalle.setLiteraturaVendida((Integer)objs[11]);
            }catch(NullPointerException e){
                detalle.setLiteraturaVendida(0);
            }
            
            detalles.add(detalle);
        }
        params.put(Constantes.INFORMEMENSUAL_DETALLE_LIST, detalles);
        
        return params;
    }
    /**
     * @see mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao#listaInformesConcentradoSemanalPorColportor(java.util.Map) 
     */
    public Map<String, Object> listaInformesConcentradoSemanalPorColportor(Map<String, Object> params){
        log.debug("listaInformesConcentradoSemanalPorColportor {}", params);
        StringBuilder query = new StringBuilder();
        query.append("select im.colportor_id, date_trunc('week', im.mes_informe) AS \"Week\", coalesce(sum(horas_trabajadas),0) as horasTrabajadas, coalesce(sum(total_pedidos),0.0) as pedidos, coalesce(sum(libros_ventas),0.0) as ventas,  ");
        query.append("coalesce(sum(literatura_gratis),0) as literaturaGratis, coalesce(sum(oraciones_ofrecidas),0) as oraciones, coalesce(sum(casas_visitadas),0) as casasVisitadas, ");
        query.append("coalesce(sum(estudios_biblicos),0) as estudiosBiblicos, coalesce(sum(bautizados),0) as bautizados, coalesce(sum(diezmo),0.0) as diezmos, ");
        query.append("coalesce(sum(libros_vendidos),0) as librosVendidos ");
        query.append("from ");
        query.append("( ");
        query.append("select * ");
        query.append("from informe_mensual_detalle ");
        query.append(") imd, ");
        query.append("( ");
        query.append("select * ");
        query.append("from informe_mensual ");
        query.append("where mes_informe between :fechaInicio and :fechaFinal ");
        query.append("and colportor_id = :colportorId ");
        query.append(") im ");
        query.append("where imd.informemensual_id = im.id  ");
        
        if(params.containsKey("asociado") && params.get("asociado") != null){
            query.append("and im.colportor_id in ");
            query.append("( ");
            query.append("select colportor_id  ");
            query.append("from temporada_colportor ");
            query.append("where asociado_id = :asociadoId ");
            query.append(") ");
        }
        
        query.append("group by im.colportor_id, \"Week\" ");
        query.append("order by 1,2 ");
        
        SQLQuery sql = getSession().createSQLQuery(query.toString());
        sql.setDate("fechaInicio", (Date)params.get("fechaInicio"));
        sql.setDate("fechaFinal", (Date)params.get("fechaFinal"));
        
        if(params.containsKey("asociado") && params.get("asociado") != null){
            sql.setLong("asociadoId", (Long)params.get("asociado"));
        }
        
        sql.setLong("colportorId", (Long)params.get("colportor"));
        
        sql.addScalar("colportor_id", StandardBasicTypes.LONG);
        sql.addScalar("week", StandardBasicTypes.STRING);
        sql.addScalar("horasTrabajadas", StandardBasicTypes.DOUBLE);
        sql.addScalar("pedidos", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("ventas", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("literaturaGratis", StandardBasicTypes.INTEGER);
        sql.addScalar("oraciones", StandardBasicTypes.INTEGER);
        sql.addScalar("casasVisitadas", StandardBasicTypes.INTEGER);
        sql.addScalar("estudiosBiblicos", StandardBasicTypes.INTEGER);
        sql.addScalar("bautizados", StandardBasicTypes.INTEGER);
        sql.addScalar("diezmos", StandardBasicTypes.BIG_DECIMAL);
        sql.addScalar("librosVendidos", StandardBasicTypes.INTEGER);
        
        Object [] objs = null;
        List <Object[]>lista = sql.list();
        InformeMensualDetalleVO detalle = null;
        List <InformeMensualDetalleVO> detalles = new ArrayList <> ();
        
        Iterator <Object[]> it = lista.iterator();
        while(it.hasNext()){
            objs = it.next();
            detalle = new InformeMensualDetalleVO();
            detalle.setInformeMensual(new InformeMensual());
            
            System.out.println(objs[0]);
            detalle.getInformeMensual().getColportor().setId((Long)objs[0]);
            detalle.setMesInforme((String)objs[1]);
            
            System.out.println(objs[2]);
            try{
                detalle.setHrsTrabajadas((Double)objs[2]);
            }catch(NullPointerException e){
                detalle.setHrsTrabajadas(0.0);
            }
            System.out.println(objs[3]);
            try{
                detalle.setTotalPedidos((BigDecimal)objs[3]);
            }catch(NullPointerException e){
                detalle.setTotalPedidos(BigDecimal.ZERO);
            }
            System.out.println(objs[4]);
            try{
                detalle.setTotalVentas((BigDecimal)objs[4]);
            }catch(NullPointerException e){
                detalle.setTotalVentas(BigDecimal.ZERO);
            }
            System.out.println(objs[5]);
            try{
                detalle.setLiteraturaGratis((Integer)objs[5]);
            }catch(NullPointerException e){
                detalle.setLiteraturaGratis(0);
            }
            System.out.println(objs[6]);
            try{
                detalle.setOracionesOfrecidas((Integer)objs[6]);
            }catch(NullPointerException e){
                detalle.setOracionesOfrecidas(0);
            }
            System.out.println(objs[7]);
            try{
                detalle.setCasasVisitadas((Integer)objs[7]);
            }catch(NullPointerException e){
                detalle.setCasasVisitadas(0);
            }
            System.out.println(objs[8]);
            try{
                detalle.setContactosEstudiosBiblicos((Integer)objs[8]);
            }catch(NullPointerException e){
                detalle.setContactosEstudiosBiblicos(0);
            }
            System.out.println(objs[9]);
            try{
                detalle.setBautizados((Integer)objs[9]);
            }catch(NullPointerException e){
                detalle.setBautizados((Integer)objs[9]);
            }
            System.out.println(objs[10]);
            try{
                detalle.setDiezmo((BigDecimal)objs[10]);
            }catch(NullPointerException e){
                detalle.setDiezmo(BigDecimal.ZERO);
            }
            System.out.println(objs[11]);
            try{
                detalle.setLiteraturaVendida((Integer)objs[11]);
            }catch(NullPointerException e){
                detalle.setLiteraturaVendida(0);
            }
            
            detalles.add(detalle);
        }
        params.put(Constantes.INFORMEMENSUAL_DETALLE_LIST, detalles);
        
        return params;
    }
}