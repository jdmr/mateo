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
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.dao.EmpleadoPerDedDao;
import mx.edu.um.mateo.rh.model.EmpleadoPerDed;
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
 * @author Omar Soto <osoto@um.edu.mx>
 */
@Repository
@Transactional
public class EmpleadoPerDedDaoHibernate extends BaseDao implements EmpleadoPerDedDao {

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoPerDedDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de perded con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(EmpleadoPerDed.class)
                .add(Restrictions.eq("status", Constantes.STATUS_ACTIVO));
        Criteria countCriteria = currentSession().createCriteria(EmpleadoPerDed.class)
                .add(Restrictions.eq("status", Constantes.STATUS_ACTIVO));


        if (params.containsKey("empresa")) {
            criteria.createCriteria("empleado").createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empleado").createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
        }
        if (params.containsKey("empleado")) {
            criteria.createCriteria("empleado").add(Restrictions.idEq(params.get("empleadoId")));
            countCriteria.createCriteria("empleado").add(Restrictions.idEq(params.get("empleadoId")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("empleado.clave", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("perded.clave", filtro, MatchMode.ANYWHERE));
//            propiedades.add(Restrictions.ilike("status", filtro, MatchMode.ANYWHERE));
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
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.EMPLEADOPERDED_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        try {
            params.put("cantidad", (Long) countCriteria.list().get(0));
        } catch (IndexOutOfBoundsException e) {
            params.put("cantidad", 0L);
        }

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoPerDedDao#obtiene(java.lang.Long) 
     */
    @Override
    public EmpleadoPerDed obtiene(Long id) {
        EmpleadoPerDed perded = (EmpleadoPerDed) currentSession().get(EmpleadoPerDed.class, id);
        if (perded == null) {
            log.warn("uh oh, EmpleadoPD with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(EmpleadoPerDed.class, id);
        }

        return perded;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoPerDedDao#graba(mx.edu.um.mateo.rh.model.EmpleadoPerDed) 
     */
    @Override
    public EmpleadoPerDed graba(EmpleadoPerDed empleadoPD) {
        currentSession().saveOrUpdate(empleadoPD);
        currentSession().merge(empleadoPD);
        currentSession().flush();
        
        return empleadoPD;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoPerDedDao#elimina(java.lang.Long) 
     */
    @Override
    public String elimina(Long id) {
        log.debug("Eliminando Empleado perded {}", id);
        EmpleadoPerDed perded = this.obtiene(id);
        String nombre = perded.getPerDed().getNombre();
        currentSession().delete(perded);
        currentSession().flush();
        return nombre;
    }
    
}
