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
package mx.edu.um.mateo.general.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.inventario.model.TipoProducto;
import org.hibernate.*;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
public class EmpresaDao {

    private static final Logger log = LoggerFactory.getLogger(EmpresaDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public EmpresaDao() {
        log.info("Nueva instancia de EmpresaDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de empresas con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Empresa.class);
        Criteria countCriteria = currentSession().createCriteria(Empresa.class);

        if (params.containsKey("organizacion")) {
            criteria.createCriteria("organizacion").add(Restrictions.idEq(params.get("organizacion")));
            countCriteria.createCriteria("organizacion").add(Restrictions.idEq(params.get("organizacion")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro));
            propiedades.add(Restrictions.ilike("nombreCompleto", filtro));
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
        params.put("empresas", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public Empresa obtiene(Long id) {
        Empresa empresa = (Empresa) currentSession().get(Empresa.class, id);
        return empresa;
    }

    public Empresa crea(Empresa empresa, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            empresa.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        }
        session.save(empresa);
        Almacen almacen = new Almacen("CENTRAL", empresa);
        session.save(almacen);
        if (usuario != null) {
            usuario.setEmpresa(empresa);
            usuario.setAlmacen(almacen);
            session.update(usuario);
        }
        Proveedor proveedor = new Proveedor(empresa.getNombre(), empresa.getNombreCompleto(), empresa.getRfc(), true, empresa);
        session.save(proveedor);
        TipoCliente tipoCliente = new TipoCliente("TIPO1", "TIPO1", empresa);
        session.save(tipoCliente);
        Cliente cliente = new Cliente(empresa.getNombre(), empresa.getNombreCompleto(), empresa.getRfc(), tipoCliente, true, empresa);
        TipoProducto tipoProducto = new TipoProducto("TIPO1", "TIPO1", almacen);
        session.save(tipoProducto);
        session.save(cliente);
        session.refresh(empresa);
        return empresa;
    }

    public Empresa crea(Empresa empresa) {
        return this.crea(empresa, null);
    }

    public Empresa actualiza(Empresa empresa) {
        return this.actualiza(empresa, null);
    }

    public Empresa actualiza(Empresa empresa, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            empresa.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        }
        try {
            // Actualiza la empresa
            log.debug("Actualizando empresa");
            session.update(empresa);
            session.flush();

            // Actualiza proveedor
            log.debug("Actualizando proveedor");
            Query query = session.createQuery("select p from Proveedor p where p.empresa.id = :empresaId and p.base is true");
            query.setLong("empresaId", empresa.getId());
            Proveedor proveedor = (Proveedor) query.uniqueResult();
            log.debug("{}", proveedor);
            proveedor.setNombre(empresa.getNombre());
            proveedor.setNombreCompleto(empresa.getNombreCompleto());
            proveedor.setRfc(empresa.getRfc());
            session.update(proveedor);

            // Actualiza cliente
            log.debug("Actualizando cliente");
            query = session.createQuery("select c from Cliente c where c.empresa.id = :empresaId and c.base is true");
            query.setLong("empresaId", empresa.getId());
            Cliente cliente = (Cliente) query.uniqueResult();
            cliente.setNombre(empresa.getNombre());
            cliente.setNombreCompleto(empresa.getNombreCompleto());
            cliente.setRfc(empresa.getRfc());
            session.update(cliente);
        } catch (NonUniqueObjectException e) {
            try {
                session.merge(empresa);
            } catch (Exception ex) {
                log.error("No se pudo actualizar la empresa", ex);
                throw new RuntimeException("No se pudo actualizar la empresa", ex);
            }
        }
        if (usuario != null) {
            actualizaUsuario:
            for (Almacen almacen : empresa.getAlmacenes()) {
                usuario.setEmpresa(empresa);
                usuario.setAlmacen(almacen);
                session.update(usuario);
                break actualizaUsuario;
            }
        }
        session.flush();
        return empresa;
    }

    public String elimina(Long id) throws UltimoException {
        Criteria criteria = currentSession().createCriteria(Empresa.class);
        criteria.setProjection(Projections.rowCount());
        Long cantidad = (Long) criteria.list().get(0);
        if (cantidad > 1) {
            Empresa empresa = obtiene(id);
            String nombre = empresa.getNombre();
            currentSession().delete(empresa);
            return nombre;
        } else {
            throw new UltimoException("No se puede eliminar porque es el ultimo");
        }
    }
}
