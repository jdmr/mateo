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
import mx.edu.um.mateo.rh.dao.JefeSeccionDao;
import mx.edu.um.mateo.rh.model.JefeSeccion;
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
public class JefeSeccionDaoHibernate extends BaseDao implements JefeSeccionDao {

    /**
     * @see mx.edu.um.mateo.rh.dao.JefeSeccionDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de jefeSecciones con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(JefeSeccion.class);
        Criteria countCriteria = currentSession().createCriteria(JefeSeccion.class);

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
     * @see mx.edu.um.mateo.rh.dao.JefeSeccionDao#obtiene(java.lang.Long)
     */
    @Override
    public JefeSeccion obtiene(Long id) {
        log.debug("Obtiene jefeSeccion con id = {}", id);
        JefeSeccion jefeSeccion = (JefeSeccion) currentSession().get(JefeSeccion.class, id);
        if (jefeSeccion == null) {
            log.warn("uh oh, la clave con el id" + id + "no se encontro...");
            throw new ObjectRetrievalFailureException(JefeSeccion.class, id);

        }
        return jefeSeccion;
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.JefeSeccionDao#obtieneClaveActiva(java.lang.Long)
     */
    @Override
    public JefeSeccion obtieneClaveActiva(Long id) {
        log.debug("Buscando clave activa de usuario por id{}", id);
        Query query = currentSession().createQuery(
                "select c from JefeSeccion c where c.empleado.id = :id AND c.status=:status");
        query.setLong("id", id);
        query.setString("status", "A");
        JefeSeccion clave = (JefeSeccion) query.uniqueResult();
        return clave;
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.JefeSeccionDao#noExisteClave(java.lang.String)
     */
    @Override
    public Boolean noExisteClave(String clave) {
        log.debug("Buscando clave de usuario por  atributo clave{}", clave);
        Query query = currentSession().createQuery(
                "select c from JefeSeccion c where c.clave = :clave");
        query.setString("clave", clave);
        if (query == null) {
            return true;
        }
        return false;
    }

    /**
     * @param usuario
     * @see
     * mx.edu.um.mateo.rh.dao.JefeSeccionDao#graba(mx.edu.um.mateo.rh.model.JefeSeccion,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    public void graba(final JefeSeccion jefeSeccion, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            jefeSeccion.setEmpresa(usuario.getEmpresa());
        }
        currentSession().saveOrUpdate(jefeSeccion);
        currentSession().merge(jefeSeccion);
        currentSession().flush();

    }

    /**
     * @return @see
     * mx.edu.um.mateo.rh.dao.JefeSeccionDao#elimina(java.lang.Long)
     */
    @Override
    public String elimina(Long id) {
        log.debug("Eliminando jefeSeccion con id {}", id);
        JefeSeccion jefeSeccion = obtiene(id);
        jefeSeccion.setStatus("I");
        currentSession().delete(jefeSeccion);
        currentSession().flush();
        String nombre = jefeSeccion.getJefeSeccion().getNombre().toString();
        return nombre;
    }

}
