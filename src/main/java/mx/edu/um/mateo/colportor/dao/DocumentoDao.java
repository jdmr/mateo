 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;
import java.util.*;
import mx.edu.um.mateo.colportor.model.Documento;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author wilbert
 */
@Repository
@Transactional
public class DocumentoDao {
    private static final Logger log = LoggerFactory.getLogger(DocumentoDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public DocumentoDao() {
        log.info("Nueva instancia de DocumentoDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de documento con params {}", params);
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
        
        Criteria criteria = currentSession().createCriteria(Documento.class);
        Criteria countCriteria = currentSession().createCriteria(Documento.class);
        
        if(params.get("temporadaColportor")!=null){
            criteria.add(Restrictions.eq("temporadaColportor.id",params.get("temporadaColportor")));
        
        }

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";            
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("tipoDeDocumento", filtro));
            propiedades.add(Restrictions.ilike("folio", filtro));
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
        
         if(params.get("temporadaColportor")!=null){
             params.put(Constantes.DOCUMENTOCOLPORTOR_LIST, criteria.list());
         }else{
              params.put(Constantes.DOCUMENTOCOLPORTOR_LIST, new ArrayList());
         }
       
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public Documento obtiene(Long id) {
        log.debug("Obtiene documento con id = {}", id);
        Documento documento = (Documento) currentSession().get(Documento.class, id);
        return documento;
    }

    public Documento crea(Documento documento) {
        log.debug("Creando documento : {}", documento);
        currentSession().save(documento);
        currentSession().flush();
        return documento;
    }

    public Documento actualiza(Documento documento) {
        log.debug("Actualizando documento {}", documento);
        
        //trae el objeto de la DB 
        Documento nuevo = (Documento)currentSession().get(Documento.class, documento.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(documento, nuevo);
        //lo guarda en la BD
        
        currentSession().update(nuevo);
        currentSession().flush();
        return nuevo;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando documento con id {}", id);
        Documento documento = obtiene(id);
        Date fecha = new Date();
        documento.setFecha(fecha);
        currentSession().delete(documento);
        currentSession().flush();
        String folio = documento.getFolio();
        return folio;
    }
    
    /**
     * Regresa un listado del numero de ventas que cada colportor ha hecho en las temporadas activas en el rango de fechas
     * @return
     * @throws Exception 
     */
    public List <Documento> obtieneVentasPorMesTemporadaActual(Date fecha1, Date fecha2) throws Exception{
        Criteria sql = currentSession().createCriteria(Documento.class)
                .createAlias("tc", "temporadaColportor");
        ProjectionList projections = Projections.projectionList();
        projections.add(Projections.groupProperty("tc.colportor.id"));
        sql.setProjection(projections);
        
        return sql.list();
    }
    
    /**
     * Regresa un map, agrupado por colportor, con su numero de compras mensuales
     * @param params, se espera que dentro de params venga un Documento (Constantes.DOCUMENTOCOLPORTOR), con una fecha especificada,
     * de donde se tomar&aacute; el a&ntilde;o
     * @return
     * @throws Exception 
     */
    public Map <String, Object> numeroComprasPorMes(Map <String, Object> params) throws Exception{
        Date fecha = ((Documento)params.get(Constantes.DOCUMENTOCOLPORTOR)).getFecha();
        
        Calendar gcFecha = new GregorianCalendar(Locale.getDefault());
        gcFecha.setTime(fecha);
        gcFecha.set(Calendar.DATE, 1);
        gcFecha.set(Calendar.MONTH, Calendar.JANUARY);
        gcFecha.set(Calendar.HOUR, 0);
        gcFecha.set(Calendar.MINUTE, 0);
        gcFecha.set(Calendar.SECOND, 0);
        Date fechaInicial = gcFecha.getTime();
        gcFecha.set(Calendar.DATE, 31);
        gcFecha.set(Calendar.MONTH, Calendar.DECEMBER);
        gcFecha.set(Calendar.HOUR, 0);
        gcFecha.set(Calendar.MINUTE, 0);
        gcFecha.set(Calendar.SECOND, 0);
        Date fechaFinal = gcFecha.getTime();
        
        StringBuffer str = new StringBuffer(" select u.clave, abc.mon as month, ventas ");
        str.append("from ");
        str.append("( ");
        str.append("select temporadacolportor_id, to_char(fecha,'Mon') as mon, count(*) as ventas ");
        str.append("from documentos ");
        str.append("where fecha between :fechaI and :fechaF   ");
        str.append("and tipodedocumento = 'Boletin'  ");
        str.append("group by 1,2 ");
        str.append(") as abc, ");
        str.append("temporada_colportor tc, usuarios u ");
        str.append("where u.id = tc.colportor_id ");
        str.append("and tc.id = abc.temporadacolportor_id ");
        
        SQLQuery sql = currentSession().createSQLQuery(str.toString());
        sql.setDate("fechaI", fechaInicial);
        sql.setDate("fechaF", fechaFinal);
        sql.addScalar("clave", StringType.INSTANCE);
        sql.addScalar("month", StringType.INSTANCE);
        sql.addScalar("ventas", IntegerType.INSTANCE);
        
        Map <String, List <Object[]>> mapa = new TreeMap<>();
        
        Object [] objs = null;
        List <Object[]> lobjs = null;
                
        Iterator <Object[]> it = sql.list().iterator();
        while(it.hasNext()){
            objs = it.next();
            if(mapa.containsKey(objs[0])){
                lobjs = mapa.get(objs[0]);
            }
            else{
                lobjs = new ArrayList<>();
                mapa.put((String)objs[0], lobjs);
            }
            lobjs.add(objs);
        }
        
        params.put(Constantes.DOCUMENTOCOLPORTOR_LIST, mapa);
        return params;

    }
    
    
}