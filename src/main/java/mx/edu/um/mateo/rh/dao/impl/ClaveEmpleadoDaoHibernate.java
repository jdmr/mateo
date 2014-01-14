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
import mx.edu.um.mateo.rh.dao.ClaveEmpleadoDao;
import mx.edu.um.mateo.rh.model.ClaveEmpleado;
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
@Repository
@Transactional
public class ClaveEmpleadoDaoHibernate extends BaseDao implements ClaveEmpleadoDao {

    /**
     * @see mx.edu.um.mateo.rh.dao.ClaveEmpleadoDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de claveEmpleadoes con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(ClaveEmpleado.class);
        Criteria countCriteria = currentSession().createCriteria(ClaveEmpleado.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("clave", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_CLAVEEMPLEADO, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.ClaveEmpleadoDao#obtiene(java.lang.Long)
     */
    @Override
    public ClaveEmpleado obtiene(Long id) {
        log.debug("Obtiene claveEmpleado con id = {}", id);
        ClaveEmpleado claveEmpleado = (ClaveEmpleado) currentSession().get(ClaveEmpleado.class, id);
        if (claveEmpleado == null) {
            log.warn("uh oh, la clave con el id" + id + "no se encontro...");
            throw new ObjectRetrievalFailureException(ClaveEmpleado.class, id);

        }
        return claveEmpleado;
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.ClaveEmpleadoDao#obtieneClaveActiva(java.lang.Long)
     */
    @Override
    public ClaveEmpleado obtieneClaveActiva(Long id) {
        log.debug("Buscando clave activa de usuario por id{}", id);
        Query query = currentSession().createQuery(
                "select c from ClaveEmpleado c where c.empleado.id = :id AND c.status=:status");
        query.setLong("id", id);
        query.setString("status", "A");
        ClaveEmpleado clave = (ClaveEmpleado) query.uniqueResult();
        return clave;
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.ClaveEmpleadoDao#graba(mx.edu.um.mateo.rh.model.ClaveEmpleado,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void graba(final ClaveEmpleado claveEmpleado, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            claveEmpleado.setEmpresa(usuario.getEmpresa());
        }
        currentSession().saveOrUpdate(claveEmpleado);
        currentSession().merge(claveEmpleado);
        currentSession().flush();

    }

    /**
     * @see mx.edu.um.mateo.rh.dao.ClaveEmpleadoDao#elimina(java.lang.Long)
     */
    @Override
    public String elimina(Long id) {
        log.debug("Eliminando claveEmpleado con id {}", id);
        ClaveEmpleado claveEmpleado = obtiene(id);
        claveEmpleado.setStatus("I");
        currentSession().delete(claveEmpleado);
        currentSession().flush();
        String nombre = claveEmpleado.getClave().toString();
        return nombre;
    }

}
