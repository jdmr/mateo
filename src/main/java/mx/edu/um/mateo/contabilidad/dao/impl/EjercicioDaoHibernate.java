/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
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
package mx.edu.um.mateo.contabilidad.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.edu.um.mateo.contabilidad.dao.EjercicioDao;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class EjercicioDaoHibernate extends BaseDao implements EjercicioDao {

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de ejercicios con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey("max")) {
            params.put("max", 10);
        } else {
            params.put("max", Math.min((Integer) params.get("max"), 100));
        }

        if (params.containsKey("pagina")) {
            Long pagina = (Long) params.get("pagina");
            Long offset = (pagina - 1) * (Integer) params.get("max");
            params.put("offset", offset.intValue());
        }

        if (!params.containsKey("offset")) {
            params.put("offset", 0);
        }
        Criteria criteria = currentSession().createCriteria(Ejercicio.class);
        Criteria countCriteria = currentSession().createCriteria(
                Ejercicio.class);

        if (params.containsKey("organizacion")) {
            criteria.add(Restrictions.eq("id.organizacion.id",
                    params.get("organizacion")));
            countCriteria.add(Restrictions.eq("id.organizacion.id",
                    params.get("organizacion")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("id.idEjercicio", filtro,
                    MatchMode.ANYWHERE));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey("order")) {
            String campo = (String) params.get("order");
            if (params.get("sort").equals("desc")) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        } else {
            criteria.addOrder(Order.desc("id.idEjercicio"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("ejercicios", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public Ejercicio obtiene(EjercicioPK id) {
        Ejercicio ejercicio = (Ejercicio) currentSession().get(Ejercicio.class,
                id);
        return ejercicio;
    }

    @Override
    public Ejercicio crea(Ejercicio ejercicio, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            ejercicio.getId().setOrganizacion(
                    usuario.getEmpresa().getOrganizacion());
        }
        session.save(ejercicio);
        session.flush();
        /**
         * TODO Crear cuentas del ejercicio
         */
        return ejercicio;
    }

    @Override
    public Ejercicio crea(Ejercicio ejercicio) {
        return this.crea(ejercicio, null);
    }

    @Override
    public Ejercicio actualiza(Ejercicio ejercicio) {
        return this.actualiza(ejercicio, null);
    }

    @Override
    public Ejercicio actualiza(Ejercicio ejercicio, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            ejercicio.getId().setOrganizacion(
                    usuario.getEmpresa().getOrganizacion());
        }
        try {
            // Actualiza la ejercicio
            log.debug("Actualizando ejercicio");
            session.update(ejercicio);
            session.flush();
        } catch (NonUniqueObjectException e) {
            try {
                session.merge(ejercicio);
                session.flush();
            } catch (Exception ex) {
                log.error("No se pudo actualizar la ejercicio", ex);
                throw new RuntimeException(
                        "No se pudo actualizar la ejercicio", ex);
            }
        }
        return ejercicio;
    }

    @Override
    public String elimina(EjercicioPK id) {
        Ejercicio ejercicio = obtiene(id);
        String nombre = ejercicio.getNombre();
        currentSession().delete(ejercicio);
        return nombre;
    }

    @Override
    public List<Ejercicio> lista(Long organizacionId) {
        Query query = currentSession().createQuery("select e from Ejercicio e where e.id.organizacion.id = :organizacionId");
        query.setLong("organizacionId", organizacionId);
        return query.list();
    }
}
