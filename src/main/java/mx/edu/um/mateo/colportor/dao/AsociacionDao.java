/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. 
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gibrandemetrioo
 */
@Repository
@Transactional

public class AsociacionDao {

    private static final Logger log = LoggerFactory.getLogger(AsociacionDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public AsociacionDao() {
        log.info("Nueva instancia de AsociacionDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de asociaciones con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Asociacion.class);
        Criteria countCriteria = currentSession().createCriteria(Asociacion.class);

        if (params.containsKey(Constantes.ADDATTRIBUTE_UNION)) {
            criteria.createCriteria(Constantes.ADDATTRIBUTE_UNION).add(Restrictions.idEq(params.get(Constantes.ADDATTRIBUTE_UNION)));
            countCriteria.createCriteria(Constantes.ADDATTRIBUTE_UNION).add(Restrictions.idEq(params.get(Constantes.ADDATTRIBUTE_UNION)));
        }

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike(Constantes.ADDATTRIBUTE_NOMBRE, filtro));
            propiedades.add(Restrictions.ilike("nombreCompleto", filtro));
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
        params.put(Constantes.CONTAINSKEY_ASOCIACIONES, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public Asociacion obtiene(Long id) {
        Asociacion asociacion = (Asociacion) currentSession().get(Asociacion.class, id);
        return asociacion;
    }

    public Asociacion crea(Asociacion asociacion) {
        return this.crea(asociacion, null);
    }

    public Asociacion crea(Asociacion asociacion, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            asociacion.setUnion(usuario.getAsociacion().getUnion());
        }
        session.save(asociacion);
        if (usuario != null) {
            usuario.setAsociacion(asociacion);
        }
        session.flush();
        return asociacion;
    }

    public Asociacion actualiza(Asociacion asociacion) {
        return this.actualiza(asociacion, null);
    }

    public Asociacion actualiza(Asociacion asociacion, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            asociacion.setUnion(usuario.getAsociacion().getUnion());
        }
        try {
            // Actualiza la asociacion
            log.debug("Actualizando asociacion");
            session.update(asociacion);
            session.flush();

        } catch (NonUniqueObjectException e) {
            try {
                session.merge(asociacion);
            } catch (Exception ex) {
                log.error("No se pudo actualizar la asociacion", ex);
                throw new RuntimeException("No se pudo actualizar la asociacion", ex);
            }
        }
        if (usuario != null) {
            usuario.setAsociacion(asociacion);
        }
        session.flush();
        return asociacion;
    }

    public String elimina(Long id, Long unionId) {
        log.debug("Eliminando asociación {}", id);
        Asociacion asociacion = obtiene(id);
        String nombre = asociacion.getNombre();
        asociacion.setStatus(Constantes.STATUS_INACTIVO);
        actualiza(asociacion);
        return nombre;
    }
}
