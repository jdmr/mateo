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
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
        
        Criteria criteria = currentSession().createCriteria(Documento.class)
                .setFetchMode("temporadaColportor", FetchMode.SELECT);
        Criteria countCriteria = currentSession().createCriteria(Documento.class)
                .setFetchMode("temporadaColportor", FetchMode.SELECT);;

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";            
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("tipoDeDocumento", filtro));
            propiedades.add(Restrictions.ilike("folio", filtro));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }
        
        if(params.get("temporadaColportor")!=null){
            criteria.add(Restrictions.eq("temporadaColportor",params.get("temporadaColportor")));
        
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
             params.put(Constantes.CONTAINSKEY_DOCUMENTOS, criteria.list());
         }else{
              params.put(Constantes.CONTAINSKEY_DOCUMENTOS, new ArrayList());
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
    
    
}