/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.EmpresaDao;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.rh.model.CuentaMayor;
import org.hibernate.Criteria;
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
 * @author nujev
 */
@Repository
@Transactional
public class CuentaMayorDao {

    private static final Logger log = LoggerFactory.getLogger(EmpresaDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public CuentaMayorDao() {
        log.info("Nueva instancia de CtaMayorDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de ctaMayor con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(CuentaMayor.class);
        Criteria countCriteria = currentSession().createCriteria(CuentaMayor.class);

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
        params.put("ctaMayores", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public CuentaMayor obtiene(Long id) {
        CuentaMayor ctaMayor = (CuentaMayor) currentSession().get(CuentaMayor.class, id);
        return ctaMayor;
    }

//<<<<<<< HEAD:src/main/java/mx/edu/um/mateo/rh/dao/CtaMayorDao.java
//    public CtaMayor crea(CtaMayor ctaMayor) {
//    ctaMayor = new CtaMayor();
//=======
    public CuentaMayor crea(CuentaMayor ctaMayor) {
//>>>>>>> 05f517e388d96b64023614a89f3db30afef06e9f:src/main/java/mx/edu/um/mateo/rh/dao/CuentaMayorDao.java
        currentSession().save(ctaMayor);
        currentSession().flush();
        return ctaMayor;
    }

    public CuentaMayor actualiza(CuentaMayor ctaMayor) {
        currentSession().saveOrUpdate(ctaMayor);
        return ctaMayor;
    }

    public String elimina(Long id) throws UltimoException {
        CuentaMayor ctamayor = obtiene(id);
        currentSession().delete(ctamayor);
        String nombre = ctamayor.getNombre();
        return nombre;
    }
}
