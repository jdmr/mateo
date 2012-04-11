package mx.edu.um.mateo.contabilidad.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.model.CuentaResultado;
import mx.edu.um.mateo.general.dao.EmpresaDao;
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
public class CuentaResultadoDao {

    private static final Logger log = LoggerFactory.getLogger(CuentaResultadoDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public CuentaResultadoDao() {
        log.info("Nueva instancia de CuentaResultadoDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de cuenta de resultado con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(CuentaResultado.class);
        Criteria countCriteria = currentSession().createCriteria(CuentaResultado.class);
        
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
        params.put(Constantes.CONTAINSKEY_RESULTADOS, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public CuentaResultado obtiene(Long id) {
        log.debug("Obtiene cuenta de resultado con id = {}", id);
        CuentaResultado cuentaResultado = (CuentaResultado) currentSession().get(CuentaResultado.class, id);
        return cuentaResultado;
    }

    public CuentaResultado crea(CuentaResultado cuentaResultado) {
        return crea(cuentaResultado, null);
    }

    public CuentaResultado crea(CuentaResultado cuentaResultado, Usuario usuario) {
        log.debug("Creando cuenta de resultado : {}", cuentaResultado);
        if (usuario != null) {
            cuentaResultado.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        }
        currentSession().save(cuentaResultado);
        currentSession().flush();
        return cuentaResultado;
    }

    public CuentaResultado actualiza(CuentaResultado cuentaResultado) {
        return actualiza(cuentaResultado, null);
    }

    public CuentaResultado actualiza(CuentaResultado cuentaResultado, Usuario usuario) {
        log.debug("Actualizando cuenta de resultado {}", cuentaResultado);
        
        CuentaResultado nueva = (CuentaResultado)currentSession().get(CuentaResultado.class, cuentaResultado.getId());
        BeanUtils.copyProperties(cuentaResultado, nueva);
        
        if (usuario != null) {
            nueva.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        }
        currentSession().update(nueva);
        currentSession().flush();
        return nueva;
    }


    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando cuenta de resultado con id {}", id);
        CuentaResultado cuentaResultado = obtiene(id);
        currentSession().delete(cuentaResultado);
        currentSession().flush();
        String nombre = cuentaResultado.getNombre();
        return nombre;
    }
}
