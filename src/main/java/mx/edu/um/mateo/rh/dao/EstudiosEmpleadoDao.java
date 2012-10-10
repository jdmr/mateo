/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.rh.model.EstudiosEmpleado;
import mx.edu.um.mateo.rh.model.EstudiosEmpleado;
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
 * @author nujev
 */
@Repository
@Transactional
public class EstudiosEmpleadoDao {

    private static final Logger log = LoggerFactory.getLogger(EstudiosEmpleadoDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public EstudiosEmpleadoDao() {
        log.info("Nueva instancia de EstudiosEmpleadoDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de EstudiosEmpleado con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(EstudiosEmpleado.class);
        Criteria countCriteria = currentSession().createCriteria(EstudiosEmpleado.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apPaterno", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public EstudiosEmpleado obtiene(Long id) {
        log.debug("Obtiene estudiosEmpleado con id = {}", id);
        EstudiosEmpleado estudiosEmpleado = (EstudiosEmpleado) currentSession().get(EstudiosEmpleado.class, id);
        return estudiosEmpleado;
    }

    public EstudiosEmpleado crea(EstudiosEmpleado estudiosEmpleado) {
        log.debug("Creando estudiosEmpleado : {}", estudiosEmpleado);
        currentSession().save(estudiosEmpleado);
        currentSession().flush();
        return estudiosEmpleado;
    }

    public EstudiosEmpleado actualiza(EstudiosEmpleado estudiosEmpleado) {
        log.debug("Actualizando estudiosEmpleado {}", estudiosEmpleado);

        //trae el objeto de la DB 
        EstudiosEmpleado nueva = (EstudiosEmpleado) currentSession().get(EstudiosEmpleado.class, estudiosEmpleado.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(estudiosEmpleado, nueva);
        //lo guarda en la BD

        currentSession().update(nueva);
        currentSession().flush();
        return nueva;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando estudiosEmpleado con id {}", id);
        EstudiosEmpleado estudiosEmpleado = obtiene(id);
        currentSession().delete(estudiosEmpleado);
        currentSession().flush();
        String nombre = estudiosEmpleado.getNombreEstudios().toString();
        return nombre;
    }
}