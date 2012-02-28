/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import mx.edu.um.mateo.rh.model.Empleado;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author develop
 */
@Repository("empleadoDao")
@Transactional
public class EmpleadoDao {

    private static final Logger log = LoggerFactory.getLogger(EmpleadoDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public EmpleadoDao() {
        log.info("Nueva instancia de EmpleadoDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de empleados con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Empleado.class);
        Criteria countCriteria = currentSession().createCriteria(Empleado.class);

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro));
            propiedades.add(Restrictions.ilike("nombreFiscal", filtro));
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
        params.put("empleado", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public Empleado obtiene(Long id) {
        Empleado empleado = (Empleado) currentSession().get(Empleado.class, id);
        return empleado;
    }

    public Empleado crea(Empleado empleado) {
        empleado = new Empleado();
        currentSession().save(empleado);
        return empleado;
    }

    public Empleado actualiza(Empleado Empleado) {
        currentSession().saveOrUpdate(Empleado);
        return Empleado;
    }
    public String elimina(Long id) throws UltimoException {
        Empleado ctamayor = obtiene(id);
        currentSession().delete(ctamayor);
        String nombre = ctamayor.getNombre();
        return nombre;
    }
//private EmpleadoLaborales empleadoLaborales;
//private EmpleadoPersonales empleadoPersonales;
//private Nacionalidades nacionalidad;
}