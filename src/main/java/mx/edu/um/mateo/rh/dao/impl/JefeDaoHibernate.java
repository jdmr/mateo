/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.dao.JefeDao;
import mx.edu.um.mateo.rh.model.Jefe;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Repository
public class JefeDaoHibernate extends BaseDao implements JefeDao {

    /**
     * @see mx.edu.um.mateo.rh.dao.JefeDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de jefes con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Jefe.class);
        Criteria countCriteria = currentSession().createCriteria(Jefe.class);

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
        params.put(Constantes.CONTAINSKEY_JEFES, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.JefeDao#obtiene(java.lang.Long)
     */
    @Override
    public Jefe obtiene(Long id) {
        log.debug("Obtiene jefe con id = {}", id);
        Jefe jefe = (Jefe) currentSession().get(Jefe.class, id);
        if (jefe == null) {
            log.warn("uh oh, el jefe con el id" + id + "no se encontro...");
            throw new ObjectRetrievalFailureException(Jefe.class, id);

        }
        return jefe;
    }

    /**
     * @param usuario
     * @see mx.edu.um.mateo.rh.dao.JefeDao#graba(mx.edu.um.mateo.rh.model.Jefe,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void graba(final Jefe jefe, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            jefe.setEmpresa(usuario.getEmpresa());
        }
        currentSession().saveOrUpdate(jefe);
        currentSession().merge(jefe);
        currentSession().flush();

    }

    /**
     * @return el nombre del dia feriado
     * @see mx.edu.um.mateo.rh.dao.JefeDao#elimina(java.lang.Long)
     */
    @Override
    public String elimina(Long id) {
        log.debug("Eliminando jefe con id {}", id);
        Jefe jefe = obtiene(id);
        currentSession().delete(jefe);
        currentSession().flush();
        String nombre = jefe.getJefe().getNombre();
        return nombre;
    }
}
