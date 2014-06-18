/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.dao.hibernate;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.dao.PedidoColportorItemDao;
import mx.edu.um.mateo.colportor.model.PedidoColportorItem;
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
public class PedidoColportorItemDaoHibernate extends BaseDao implements PedidoColportorItemDao {
        public PedidoColportorItemDaoHibernate() {
        log.info("Se ha creado una nueva instancia de PedidoColportorItemDao");
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de items de pedidos de colportor con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(PedidoColportorItem.class);
        Criteria countCriteria = currentSession().createCriteria(PedidoColportorItem.class);

        criteria.createCriteria("pedido").add(Restrictions.idEq(params.get("pedido")));
        countCriteria.createCriteria("pedido").add(Restrictions.idEq(params.get("pedido")));

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("item", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("observaciones", filtro,
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
        params.put(Constantes.PEDIDO_COLPORTOR_ITEM_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoColportorItem obtiene(Long id) {
        PedidoColportorItem pedidoColportorItem = (PedidoColportorItem) currentSession().get(PedidoColportorItem.class, id);
        return pedidoColportorItem;
    }

    @Override
    public PedidoColportorItem crea(PedidoColportorItem pedidoColportorItem) {
        getSession().save(pedidoColportorItem);
        getSession().flush();
        return pedidoColportorItem;
    }

    @Override
    public PedidoColportorItem actualiza(PedidoColportorItem pedidoColportorItem) {
        getSession().update(pedidoColportorItem);
        getSession().flush();
        return pedidoColportorItem;
    }

    @Override
    public String elimina(Long id) {
        PedidoColportorItem pedidoColportorItem = obtiene(id);
        String nombre = pedidoColportorItem.getItem();
        currentSession().delete(pedidoColportorItem);
        currentSession().flush();
        return nombre;
    }
}
