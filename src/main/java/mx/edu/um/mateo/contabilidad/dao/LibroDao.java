/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.model.Libro;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.UltimoException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Repository
@Transactional
public class LibroDao {

    private static final Logger log = LoggerFactory.getLogger(LibroDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public LibroDao() {
        log.info("Nueva instancia de LibroDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de libro con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Libro.class);
        Criteria countCriteria = currentSession().createCriteria(Libro.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_LIBROS, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public Libro obtiene(Long id) {
        log.debug("Obtiene  libro con id = {}", id);
        Libro libro = (Libro) currentSession().get(Libro.class, id);
        return libro;
    }
//
//    public Libro crea(Libro libro) {
//        log.debug("Creando  libro : {}", libro);
//        currentSession().save(libro);
//        currentSession().flush();
//        return libro;
//    }
    
    public Libro crea(Libro libro) {
        return crea(libro, null);
    }

    public Libro crea(Libro libro, Usuario usuario) {
        log.debug("Creando cuenta de mayor : {}", libro);
        if (usuario != null) {
            libro.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        }
        currentSession().save(libro);
        currentSession().flush();
        return libro;
    }

    public Libro actualiza(Libro libro) {
        log.debug("Actualizando  libro {}", libro);
        
        //trae el objeto de la DB 
        Libro nueva = (Libro)currentSession().get(Libro.class, libro.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(libro, nueva);
        //lo guarda en la BD
        
        currentSession().update(nueva);
        currentSession().flush();
        return nueva;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando  libro con id {}", id);
        Libro libro = obtiene(id);
        currentSession().delete(libro);
        currentSession().flush();
        String nombre = libro.getNombre();
        return nombre;
    }
}
