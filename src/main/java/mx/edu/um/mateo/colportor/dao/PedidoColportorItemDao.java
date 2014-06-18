/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.dao;

import java.util.Map;
import mx.edu.um.mateo.colportor.model.PedidoColportorItem;

/**
 *
 * @author osoto
 */
public interface PedidoColportorItemDao {
    public Map<String, Object> lista(Map<String, Object> params);

    public PedidoColportorItem obtiene(Long id);

    public PedidoColportorItem crea(PedidoColportorItem pedidoColportorItem);

    public PedidoColportorItem actualiza(PedidoColportorItem pedidoColportorItem);

    public String elimina(Long id);
}
