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
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.model.UsuarioRol;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
public class UsuarioDao {

    private static final Logger log = LoggerFactory.getLogger(UsuarioDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public UsuarioDao() {
        log.info("Se ha creado una nueva instancia de UsuarioDao");
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de usuarios con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }
        
        if (!params.containsKey("max")) {
            params.put("max",10);
        } else {
            params.put("max",Math.min((Integer)params.get("max"), 100));
        }
        if (!params.containsKey("offset")) {
            params.put("offset",0);
        }
        Criteria criteria = currentSession().createCriteria(Usuario.class);
        Criteria countCriteria = currentSession().createCriteria(Usuario.class);
        
        criteria.setFirstResult((Integer)params.get("offset"));
        criteria.setMaxResults((Integer)params.get("max"));
        params.put("usuarios", criteria.list());
        
        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long)countCriteria.list().get(0));
        
        return params;
    }
    
    public Usuario obtiene(Long id) {
        Usuario usuario = (Usuario) currentSession().get(Usuario.class, id);
        return usuario;
    }
    
    public Usuario obtiene(String username) {
        Query query = currentSession().createQuery("select u from Usuario u where u.username = :username");
        query.setString("username", username);
        return (Usuario) query.uniqueResult();
    }
    
    public Usuario crea(Usuario usuario, Long almacenId, String[] nombreDeRoles) {
        Almacen almacen = (Almacen) currentSession().get(Almacen.class, almacenId);
        usuario.setAlmacen(almacen);
        usuario.setEmpresa(almacen.getEmpresa());

        Query query = currentSession().createQuery("select r from Rol r where r.authority = :nombre");
        for(String nombre : nombreDeRoles) {
            query.setString("nombre", nombre);
            Rol rol = (Rol) query.uniqueResult();
            UsuarioRol usuarioRol = new UsuarioRol(usuario, rol);
            log.debug("Guardando la relacion {}", usuarioRol);
            usuario.getAuthorities().add(usuarioRol);
        }
        log.debug("Roles del usuario {}",usuario.getAuthorities());
        
        currentSession().save(usuario);
        currentSession().flush();
        return usuario;
    }
    
    public Usuario actualiza(Usuario usuario, String[] nombreDeRoles) {
        usuario.getAuthorities().clear();
        Query query = currentSession().createQuery("select r from Rol r where r.authority = :nombre");
        for(String nombre : nombreDeRoles) {
            query.setString("nombre", nombre);
            Rol rol = (Rol) query.uniqueResult();
            usuario.getAuthorities().add(new UsuarioRol(usuario, rol));
        }
        log.debug("Roles del usuario {}",usuario.getAuthorities());
        currentSession().update(usuario);
        return usuario;
    }
    
    public String elimina(Long id) throws UltimoException {
        Usuario usuario = obtiene(id);
        Criteria criteria = currentSession().createCriteria(Usuario.class);
        criteria.setProjection(Projections.rowCount());
        List resultados = criteria.createCriteria("empresa").add(Restrictions.eq("id", usuario.getEmpresa().getId())).list();
        Long cantidad = (Long)resultados.get(0);
        if (cantidad > 1) {
            String nombre = usuario.getUsername();
            currentSession().delete(usuario);
            return nombre;
        } else {
            throw new UltimoException("No se puede eliminar el ultimo Usuario");
        }
    }
    
    public List<Rol> roles() {
        Query query = currentSession().createQuery("select r from Rol r");
        return query.list();
    }
    
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

}
