/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.dao.SolicitudVacacionesEmpleadoDao;
import mx.edu.um.mateo.rh.model.SolicitudVacacionesEmpleado;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Repository
public class SolicitudVacacionesEmpleadoDaoHibernate extends BaseDao implements SolicitudVacacionesEmpleadoDao {

    /**
     * @see mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de vacacioness con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(SolicitudVacacionesEmpleado.class);
        Criteria countCriteria = currentSession().createCriteria(SolicitudVacacionesEmpleado.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("observaciones", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_SOLICITUDVACACIONESEMPLEADO, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao#obtiene(java.lang.Long)
     */
    @Override
    public SolicitudVacacionesEmpleado obtiene(Long id) {
        log.debug("Obtiene vacaciones con id = {}", id);
        SolicitudVacacionesEmpleado vacaciones = (SolicitudVacacionesEmpleado) currentSession().get(SolicitudVacacionesEmpleado.class, id);
        if (vacaciones == null) {
            log.warn("uh oh, las vacaciones con el id" + id + "no se encontro...");
            throw new ObjectRetrievalFailureException(SolicitudVacacionesEmpleado.class, id);

        }
        return vacaciones;
    }

    /**
     * @param usuario
     * @see
     * mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao#graba(mx.edu.um.mateo.rh.model.VacacionesEmpleado,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void graba(final SolicitudVacacionesEmpleado vacaciones, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            vacaciones.setEmpresa(usuario.getEmpresa());
        }
        currentSession().saveOrUpdate(vacaciones);
        currentSession().merge(vacaciones);
        currentSession().flush();

    }

    /**
     * @return el nombre del empleado
     * @see mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao#elimina(java.lang.Long)
     */
    @Override
    public String elimina(Long id) {
        log.debug("Eliminando vacaciones con id {}", id);
        SolicitudVacacionesEmpleado vacaciones = obtiene(id);
        currentSession().delete(vacaciones);
        currentSession().flush();
        String nombre = vacaciones.getEmpleado().getNombre();
        return nombre;
    }

}
