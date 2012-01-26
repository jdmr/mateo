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
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
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
        if (!params.containsKey("offset")) {
            params.put("offset", 0);
        }
        Criteria criteria = currentSession().createCriteria(Organizacion.class);
        Criteria countCriteria = currentSession().createCriteria(Organizacion.class);

        criteria.setFirstResult((Integer) params.get("offset"));
        criteria.setMaxResults((Integer) params.get("max"));
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
        Empresa empresa = new Empresa("MTZ", "MATRIZ", "MATRIZ", organizacion);
        session.save(empresa);
        Almacen almacen = new Almacen("CENTRAL", empresa);
        session.save(almacen);
        if (usuario != null) {
            usuario.setEmpresa(empresa);
            usuario.setAlmacen(almacen);
            session.update(usuario);
        }
        session.refresh(empresa);
        session.refresh(organizacion);
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
            actualizaUsuario:
            for(Empresa empresa : organizacion.getEmpresas()) {
                for(Almacen almacen : empresa.getAlmacenes()) {
                    usuario.setEmpresa(empresa);
                    usuario.setAlmacen(almacen);
                    session.update(usuario);
                    break actualizaUsuario;
                }
            }
        }
        return organizacion;
    }

    public String elimina(Long id) throws UltimoException {
        Criteria criteria = currentSession().createCriteria(Organizacion.class);
        criteria.setProjection(Projections.rowCount());
        Long cantidad = (Long)criteria.list().get(0);
        if (cantidad > 1) {
            Organizacion organizacion = obtiene(id);
            String nombre = organizacion.getNombre();
            currentSession().delete(organizacion);
            return nombre;
        } else {
            throw new UltimoException("No se puede eliminar porque es el ultimo");
        }
    }
}
