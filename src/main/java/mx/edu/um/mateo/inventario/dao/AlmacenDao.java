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
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.inventario.model.TipoProducto;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
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
public class AlmacenDao {

    private static final Logger log = LoggerFactory.getLogger(AlmacenDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public AlmacenDao() {
        log.info("Nueva instancia de AlmacenDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de almacenes con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Almacen.class);
        Criteria countCriteria = currentSession().createCriteria(Almacen.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombreCompleto", filtro, MatchMode.ANYWHERE));
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
        params.put("almacenes", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public Almacen obtiene(Long id) {
        Almacen almacen = (Almacen) currentSession().get(Almacen.class, id);
        return almacen;
    }

    public Almacen crea(Almacen almacen, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            almacen.setEmpresa(usuario.getEmpresa());
        }
        session.save(almacen);
        if (usuario != null) {
            usuario.setAlmacen(almacen);
        }
        TipoProducto tipoProducto = new TipoProducto("TIPO1", "TIPO1", almacen);
        session.save(tipoProducto);
        session.flush();
        return almacen;
    }

    public Almacen crea(Almacen almacen) {
        return this.crea(almacen, null);
    }

    public Almacen actualiza(Almacen almacen) {
        return this.actualiza(almacen, null);
    }

    public Almacen actualiza(Almacen almacen, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            almacen.setEmpresa(usuario.getEmpresa());
        }
        try {
            // Actualiza la almacen
            log.debug("Actualizando almacen");
            session.update(almacen);
            session.flush();

        } catch (NonUniqueObjectException e) {
            try {
                session.merge(almacen);
            } catch (Exception ex) {
                log.error("No se pudo actualizar la almacen", ex);
                throw new RuntimeException("No se pudo actualizar la almacen", ex);
            }
        }
        if (usuario != null) {
            usuario.setAlmacen(almacen);
        }
        session.flush();
        return almacen;
    }

    public String elimina(Long id, Long empresaId) throws UltimoException {
        Criteria criteria = currentSession().createCriteria(Almacen.class);
        criteria.createCriteria("empresa").add(Restrictions.idEq(empresaId));
        criteria.setProjection(Projections.rowCount());
        Long cantidad = (Long) criteria.list().get(0);
        if (cantidad > 1) {
            Almacen almacen = obtiene(id);
            String nombre = almacen.getNombre();
            currentSession().delete(almacen);
            return nombre;
        } else {
            throw new UltimoException("No se puede eliminar porque es el ultimo");
        }
    }
}
