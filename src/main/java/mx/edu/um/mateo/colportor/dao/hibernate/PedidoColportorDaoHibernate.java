/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao.hibernate;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.dao.PedidoColportorDao;
import mx.edu.um.mateo.colportor.model.PedidoColportor;
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
public class PedidoColportorDaoHibernate extends BaseDao implements PedidoColportorDao{

    public PedidoColportorDaoHibernate() {
        log.info("Se ha creado una nueva instancia de ClienteColportorDao");
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de pedidoColportores con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(PedidoColportor.class);
        Criteria countCriteria = currentSession().createCriteria(PedidoColportor.class);

        if (params.containsKey("colportor")) {
            criteria.createCriteria("colportor").add(Restrictions.idEq(params.get("colportor")));
            countCriteria.createCriteria("colportor").add(Restrictions.idEq(params.get("colportor")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("numPedido", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("razonSocial", filtro,
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
        params.put(Constantes.PEDIDO_COLPORTOR_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoColportor obtiene(Long id) {
        PedidoColportor PedidoColportor = (PedidoColportor) currentSession().get(PedidoColportor.class, id);
        return PedidoColportor;
    }

    @Override
    public PedidoColportor crea(PedidoColportor PedidoColportor) {
        getSession().save(PedidoColportor);
        getSession().flush();
        return PedidoColportor;
    }

    @Override
    public PedidoColportor actualiza(PedidoColportor PedidoColportor) {
        getSession().update(PedidoColportor);
        getSession().flush();
        return PedidoColportor;
    }

    @Override
    public String elimina(Long id) {
        PedidoColportor PedidoColportor = obtiene(id);
        String nombre = PedidoColportor.getNumPedido();
        currentSession().delete(PedidoColportor);
        currentSession().flush();
        return nombre;
    }
}
