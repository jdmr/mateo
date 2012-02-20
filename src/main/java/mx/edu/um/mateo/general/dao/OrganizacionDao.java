/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
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
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
 * @author jdmr
 */
@Repository
@Transactional
public class OrganizacionDao {

    private static final Logger log = LoggerFactory.getLogger(OrganizacionDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public OrganizacionDao() {
        log.info("Nueva instancia de OrganizacionDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de organizaciones con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Organizacion.class);
        Criteria countCriteria = currentSession().createCriteria(Organizacion.class);

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
        params.put("organizaciones", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public Organizacion obtiene(Long id) {
        Organizacion organizacion = (Organizacion) currentSession().get(Organizacion.class, id);
        return organizacion;
    }

    public Organizacion crea(Organizacion organizacion, Usuario usuario) {
        Session session = currentSession();
        session.save(organizacion);
        Empresa empresa = new Empresa("MTZ", "MATRIZ", "MATRIZ", "000000000001", organizacion);
        session.save(empresa);
        Almacen almacen = new Almacen("CT","CENTRAL", empresa);
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
        session.save(cliente);
        TipoProducto tipoProducto = new TipoProducto("TIPO1", "TIPO1", almacen);
        session.save(tipoProducto);
        session.refresh(empresa);
        session.refresh(organizacion);
        session.flush();
        return organizacion;
    }

    public Organizacion crea(Organizacion organizacion) {
        return this.crea(organizacion, null);
    }

    public Organizacion actualiza(Organizacion organizacion) {
        return this.actualiza(organizacion, null);
    }

    public Organizacion actualiza(Organizacion organizacion, Usuario usuario) {
        Session session = currentSession();
        session.update(organizacion);
        if (usuario != null) {
            session.refresh(organizacion);
            actualizaUsuario:
            for (Empresa empresa : organizacion.getEmpresas()) {
                for (Almacen almacen : empresa.getAlmacenes()) {
                    usuario.setEmpresa(empresa);
                    usuario.setAlmacen(almacen);
                    session.update(usuario);
                    break actualizaUsuario;
                }
            }
        }
        session.flush();
        return organizacion;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando organizacion {}", id);
        Criteria criteria = currentSession().createCriteria(Organizacion.class);
        criteria.setProjection(Projections.rowCount());
        Long cantidad = (Long) criteria.list().get(0);
        if (cantidad > 1) {
            Organizacion organizacion = obtiene(id);
            Query query = currentSession().createQuery("select o from Organizacion o where o.id != :organizacionId");
            query.setLong("organizacionId", id);
            query.setMaxResults(1);
            Organizacion otraOrganizacion = (Organizacion) query.uniqueResult();
            boolean encontreAdministrador = false;
            for (Empresa empresa : organizacion.getEmpresas()) {
                for (Almacen almacen : empresa.getAlmacenes()) {
                    currentSession().refresh(almacen);
                    for (Usuario usuario : almacen.getUsuarios()) {
                        for (Rol rol : usuario.getRoles()) {
                            if (rol.getAuthority().equals("ROLE_ADMIN")) {
                                encontreAlmacen:
                                for (Empresa otraEmpresa : otraOrganizacion.getEmpresas()) {
                                    for (Almacen otroAlmacen : otraEmpresa.getAlmacenes()) {
                                        usuario.setEmpresa(otraEmpresa);
                                        usuario.setAlmacen(otroAlmacen);
                                        currentSession().update(usuario);
                                        currentSession().flush();
                                        encontreAdministrador = true;
                                        break encontreAlmacen;
                                    }
                                }
                            }
                        }
                    }
                    if (encontreAdministrador) {
                        currentSession().refresh(almacen);
                    }
                }
            }
            String nombre = organizacion.getNombre();
            currentSession().delete(organizacion);
            currentSession().flush();
            return nombre;
        } else {
            throw new UltimoException("No se puede eliminar porque es el ultimo");
        }
    }
}
