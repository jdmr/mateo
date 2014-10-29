/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao.hibernate;

import java.util.HashMap;
import java.util.List;
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
        
        criteria.createAlias("colportor", "clp");
        criteria.createAlias("clp.empresa", "emp");
        criteria.createAlias("cliente", "cl");
        
        countCriteria.createAlias("colportor", "clp");
        countCriteria.createAlias("clp.empresa", "emp");
        countCriteria.createAlias("cliente", "cl");

        if (params.containsKey("colportor")) {
            criteria.add(Restrictions.eq("clp.id", params.get("colportor")));
            countCriteria.add(Restrictions.eq("clp.id",params.get("colportor")));
        }
        
        if (params.containsKey("empresa")) {
            criteria.add(Restrictions.eq("emp.id", params.get("empresa")));
            criteria.add(Restrictions.eq("emp.id", params.get("empresa")));
        }
        
        if(params.containsKey("proyecto")) {
            criteria.createCriteria("proyecto").add(Restrictions.idEq(params.get("proyecto")));
            countCriteria.createCriteria("proyecto").add(Restrictions.idEq(params.get("proyecto")));
        }
        
        if(params.containsKey("cliente")) {
            criteria.add(Restrictions.idEq(params.get("cliente")));
            countCriteria.add(Restrictions.idEq(params.get("cliente")));
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
    public PedidoColportor crea(PedidoColportor pedidoColportor) {
        //Determinar numero de pedido
        try{
            log.debug("Intentando generar el nuevo numero de pedido");
            
            //Obtener listado de pedidos del proyecto actual
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", pedidoColportor.getColportor().getEmpresa().getId());
            //params.put("colportor", pedidoColportor.getColportor().getId()); //Solo se evalua la empresa y el proyecto
            params.put("proyecto", pedidoColportor.getProyecto().getId());
            
            params.put("reporte", "reporte");
            Integer pedidos = ((List<PedidoColportor>)this.lista(params).get(Constantes.PEDIDO_COLPORTOR_LIST)).size();
            log.debug("#pedidos registrados {}", pedidos);
            
            pedidos += 1;

            StringBuilder str = new StringBuilder();
            //Se estandariza este numero a 5 posiciones
            for(int i = 5; i > pedidos.toString().length(); i--){
                str.append("0");
            }
            str.append(pedidos);
            
            pedidoColportor.setNumPedido(pedidoColportor.getProyecto().getCodigo()+"/"+str.toString());
            log.debug("NumPedido... {}", pedidoColportor.getNumPedido());
            
        }catch(Exception e){
            log.error("Error al intentar generar el numero de pedido");
        }
        
        getSession().save(pedidoColportor);
        getSession().flush();
        return pedidoColportor;
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
