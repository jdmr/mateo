package mx.edu.um.mateo.contabilidad.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.UltimoException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

    private static final Logger log = LoggerFactory.getLogger(CuentaMayorDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public CuentaMayorDao() {
        log.info("Nueva instancia de CuentaMayorDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de cuenta de mayor con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(CuentaMayor.class);
        Criteria countCriteria = currentSession().createCriteria(CuentaMayor.class);
        
        if (params.containsKey(Constantes.CONTAINSKEY_ORGANIZACION)) {
            criteria.createCriteria(Constantes.CONTAINSKEY_ORGANIZACION).add(Restrictions.idEq(params.get(Constantes.CONTAINSKEY_ORGANIZACION)));
            countCriteria.createCriteria(Constantes.CONTAINSKEY_ORGANIZACION).add(Restrictions.idEq(params.get(Constantes.CONTAINSKEY_ORGANIZACION)));
        }
        
        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombreFiscal", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_MAYORES, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public CuentaMayor obtiene(Long id) {
        log.debug("Obtiene cuenta de mayor con id = {}", id);
        CuentaMayor cuentaMayor = (CuentaMayor) currentSession().get(CuentaMayor.class, id);
        return cuentaMayor;
    }

    public CuentaMayor crea(CuentaMayor cuentaMayor) {
        return crea(cuentaMayor, null);
    }

    public CuentaMayor crea(CuentaMayor cuentaMayor, Usuario usuario) {
        log.debug("Creando cuenta de mayor : {}", cuentaMayor);
        if (usuario != null) {
            cuentaMayor.getId().getEjercicio().getId().setOrganizacion(usuario.getEmpresa().getOrganizacion());
        }
        currentSession().save(cuentaMayor);
        currentSession().flush();
        return cuentaMayor;
    }

    public CuentaMayor actualiza(CuentaMayor cuentaMayor) {
        return actualiza(cuentaMayor, null);
    }

    public CuentaMayor actualiza(CuentaMayor cuentaMayor, Usuario usuario) {
        log.debug("Actualizando cuenta de mayor {}", cuentaMayor);
        
        CuentaMayor nueva = (CuentaMayor)currentSession().get(CuentaMayor.class, cuentaMayor.getId());
        BeanUtils.copyProperties(cuentaMayor, nueva, new String[] {"id","version"});
        
        if (usuario != null) {
            nueva.getId().getEjercicio().getId().setOrganizacion(usuario.getEmpresa().getOrganizacion());
        }
        currentSession().update(nueva);
        currentSession().flush();
        return nueva;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando cuenta de mayor con id {}", id);
        CuentaMayor cuentaMayor = obtiene(id);
        currentSession().delete(cuentaMayor);
        currentSession().flush();
        String nombre = cuentaMayor.getNombre();
        return nombre;
    }
}
