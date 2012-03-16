/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.inventario.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.TipoProducto;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class TipoProductoDao {

    private static final Logger log = LoggerFactory.getLogger(TipoProductoDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de tipos de producto con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(TipoProducto.class);
        Criteria countCriteria = currentSession().createCriteria(TipoProducto.class);

        if (params.containsKey("almacen")) {
            criteria.createCriteria("almacen").add(Restrictions.idEq(params.get("almacen")));
            countCriteria.createCriteria("almacen").add(Restrictions.idEq(params.get("almacen")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("descripcion", filtro, MatchMode.ANYWHERE));
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
        params.put("tiposDeProducto", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public TipoProducto obtiene(Long id) {
        return (TipoProducto) currentSession().get(TipoProducto.class, id);
    }

    public TipoProducto crea(TipoProducto tipoProducto, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            tipoProducto.setAlmacen(usuario.getAlmacen());
        }
        session.save(tipoProducto);
        session.flush();
        return tipoProducto;
    }

    public TipoProducto crea(TipoProducto tipoProducto) {
        return this.crea(tipoProducto, null);
    }

    public TipoProducto actualiza(TipoProducto tipoProducto) {
        return this.actualiza(tipoProducto, null);
    }

    public TipoProducto actualiza(TipoProducto tipoProducto, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            tipoProducto.setAlmacen(usuario.getAlmacen());
        }
        session.update(tipoProducto);
        session.flush();
        return tipoProducto;
    }

    public String elimina(Long id) {
        TipoProducto tipoProducto = obtiene(id);
        String nombre = tipoProducto.getNombre();
        currentSession().delete(tipoProducto);
        currentSession().flush();
        return nombre;
    }
}
