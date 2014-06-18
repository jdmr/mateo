/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao;
import mx.edu.um.mateo.rh.model.VacacionesEmpleado;
import org.hibernate.Criteria;
import org.hibernate.Query;
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
public class VacacionesEmpleadoDaoHibernate extends BaseDao implements VacacionesEmpleadoDao {

    /**
     * @see mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de vacacionesEmpleados con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(VacacionesEmpleado.class);
        Criteria countCriteria = currentSession().createCriteria(VacacionesEmpleado.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("descripcion", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_VACACIONESEMPLEADO, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao#obtiene(java.lang.Long)
     */
    @Override
    public VacacionesEmpleado obtiene(Long id) {
        log.debug("Obtiene vacacionesEmpleado con id = {}", id);
        VacacionesEmpleado vacacionesEmpleado = (VacacionesEmpleado) currentSession().get(VacacionesEmpleado.class, id);
        if (vacacionesEmpleado == null) {
            log.warn("uh oh, la clave con el id" + id + "no se encontro...");
            throw new ObjectRetrievalFailureException(VacacionesEmpleado.class, id);

        }
        return vacacionesEmpleado;
    }

    /**
     * @param usuario
     * @see
     * mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao#graba(mx.edu.um.mateo.rh.model.VacacionesEmpleado,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void graba(final VacacionesEmpleado vacacionesEmpleado, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            vacacionesEmpleado.setEmpresa(usuario.getEmpresa());
        }
        currentSession().saveOrUpdate(vacacionesEmpleado);
        currentSession().merge(vacacionesEmpleado);
        currentSession().flush();

    }

    /**
     * @return el nombre del dia feriado
     * @see mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao#elimina(java.lang.Long)
     */
    @Override
    public String elimina(Long id) {
        log.debug("Eliminando vacacionesEmpleado con id {}", id);
        VacacionesEmpleado vacacionesEmpleado = obtiene(id);
        currentSession().delete(vacacionesEmpleado);
        currentSession().flush();
        String nombre = vacacionesEmpleado.getDescripcion();
        return nombre;
    }

    @Override
    public Integer totalDias() {
        int diasDisponibles = 0;
        Query query = currentSession().createQuery("select d from VacacionesEmpleado d");

        List<VacacionesEmpleado> vacacionesEmpleados = query.list();

        for (VacacionesEmpleado x : vacacionesEmpleados) {
            int dias = x.getNumDias();
            log.debug("vacaciones{}", x);
            if (x.getSigno().equals("+")) {
                diasDisponibles += dias;
                log.debug("dias+{}", diasDisponibles);
            } else {
                diasDisponibles -= dias;
                log.debug("dias-{}", diasDisponibles);
            }
        }
        return diasDisponibles;
    }
}
