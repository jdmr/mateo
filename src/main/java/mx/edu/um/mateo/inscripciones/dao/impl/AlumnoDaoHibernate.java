/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.inscripciones.Alumno;
import mx.edu.um.mateo.inscripciones.dao.AlumnoDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
public class AlumnoDaoHibernate extends BaseDao implements AlumnoDao{

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de Alumnos con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Alumno.class);
        Criteria countCriteria = currentSession().createCriteria(Alumno.class);

       if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("telefono", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("email", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("codigo_personal", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apellido_materno", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("f_nacimiento", filtro,
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
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("alumnos", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

   @Override
    @Transactional(readOnly = true)
    public Alumno obtiene(Long id) {
        Alumno  alumno = (Alumno) currentSession().get(Alumno.class, id);
        return alumno;
    }
   
}
