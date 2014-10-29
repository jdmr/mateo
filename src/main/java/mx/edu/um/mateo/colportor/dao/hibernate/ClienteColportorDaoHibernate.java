/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.dao.hibernate;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.dao.ClienteColportorDao;
import mx.edu.um.mateo.colportor.model.ClienteColportor;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 */
@Repository
@Transactional
public class ClienteColportorDaoHibernate extends BaseDao implements ClienteColportorDao {

    public ClienteColportorDaoHibernate() {
        log.info("Se ha creado una nueva instancia de ClienteColportorDao");
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de clienteColportores con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(ClienteColportor.class);
        Criteria countCriteria = currentSession().createCriteria(ClienteColportor.class);
        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
        }
        if (params.containsKey("usuario")) {
            criteria.createCriteria("usuario").add(Restrictions.idEq(params.get("usuario")));
            countCriteria.createCriteria("usuario").add(Restrictions.idEq(params.get("usuario")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("email", filtro,
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
        params.put(Constantes.CLIENTE_COLPORTOR_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteColportor obtiene(Long id) {
        ClienteColportor clienteColportor = (ClienteColportor) currentSession().get(ClienteColportor.class, id);
        return clienteColportor;
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClienteColportor obtiene(Map<String, Object> params) {
        log.debug("Buscando cliente {}", params.get("cliente"));
        
        String arr[] = ((String)params.get("cliente")).split("\\s+");
        log.debug("En el nombre {} se encontraron {} palabras ", params.get("cliente"), arr.length);
        
        String nombre = "";
        String apPaterno = "";
        if(arr.length == 1){
            nombre = arr[0];
            apPaterno = "%";
        }
        else{
            nombre = arr[0];
            apPaterno = arr[1];
        }
        log.debug("Nombre {}, apPaterno {}", nombre, apPaterno);
        
        Criteria sql = getSession().createCriteria(ClienteColportor.class);
        sql.add(Restrictions.ilike("nombre", "%"+nombre+"%")); 
        sql.add(Restrictions.ilike("apPaterno", "%"+apPaterno+"%"));
        return (ClienteColportor)sql.uniqueResult();
    }
    
    @Override
    public ClienteColportor crea(ClienteColportor clienteColportor) {
        getSession().save(clienteColportor);
        getSession().flush();
        return clienteColportor;
    }

    @Override
    public ClienteColportor actualiza(ClienteColportor clienteColportor) {
        getSession().update(clienteColportor);
        getSession().flush();
        return clienteColportor;
    }

    @Override
    public String elimina(Long id) {
        ClienteColportor clienteColportor = obtiene(id);
        String nombre = clienteColportor.getNombre();
        currentSession().delete(clienteColportor);
        currentSession().flush();
        return nombre;
    }
}
