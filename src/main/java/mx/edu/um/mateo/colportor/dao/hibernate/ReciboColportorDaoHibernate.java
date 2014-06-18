/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao.hibernate;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.dao.ReciboColportorDao;
import mx.edu.um.mateo.colportor.model.ReciboColportor;
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
public class ReciboColportorDaoHibernate extends BaseDao implements ReciboColportorDao{

    public ReciboColportorDaoHibernate() {
        log.info("Se ha creado una nueva instancia de ReciboColportorDao");
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de recibos colportore con params {}", params);
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
        
        Criteria criteria = currentSession().createCriteria(ReciboColportor.class);
        Criteria countCriteria = currentSession().createCriteria(ReciboColportor.class);
        criteria.createCriteria("pedido").add(Restrictions.idEq(params.get("pedido")));
        countCriteria.createCriteria("pedido").add(Restrictions.idEq(params.get("pedido")));

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("numRecibo", filtro,
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
        
//        //Los recibos hacen un redireccionamiento incorrecto en la paginacion
//        if (!params.containsKey("reporte")) {
//            criteria.setFirstResult((Integer) params.get("offset"));men
//            criteria.setMaxResults((Integer) params.get("max"));
//        }
        params.put(Constantes.RECIBO_COLPORTOR_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", new Long(0));

        return params;
    }

    @Override
    @Transactional(readOnly = true)
    public ReciboColportor obtiene(Long id) {
        ReciboColportor reciboColportor = (ReciboColportor) currentSession().get(ReciboColportor.class, id);
        return reciboColportor;
    }

    @Override
    public ReciboColportor crea(ReciboColportor reciboColportor) {
        getSession().save(reciboColportor);
        getSession().flush();
        return reciboColportor;
    }

    @Override
    public ReciboColportor actualiza(ReciboColportor reciboColportor) {
        getSession().update(reciboColportor);
        getSession().flush();
        return reciboColportor;
    }

    @Override
    public String elimina(Long id) {
        ReciboColportor reciboColportor = obtiene(id);
        String nombre = reciboColportor.getNumRecibo().toString();
        currentSession().delete(reciboColportor);
        currentSession().flush();
        return nombre;
    }
}
