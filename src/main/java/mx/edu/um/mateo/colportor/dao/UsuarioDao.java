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
package mx.edu.um.mateo.colportor.dao;

import java.util.*;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.SpringSecurityUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author jdmr
 */
@Repository("userDao")
@Transactional
public class UsuarioDao {

    private static final Logger log = LoggerFactory.getLogger(UsuarioDao.class);
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SpringSecurityUtils springSecurityUtils;

    public UsuarioDao() {
        log.info("Se ha creado una nueva instancia de UsuarioDao");
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de usuarios con params {}", params);
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

        if (!params.containsKey(Constantes.ADDATTRIBUTE_ASOCIACION)) {
            params.put("usuarios", new ArrayList());
            params.put("cantidad", 0L);

            return params;
        }
        
        Criteria criteria = currentSession().createCriteria(Usuario.class);
        Criteria countCriteria = currentSession().createCriteria(Usuario.class);

        
        if (params.containsKey(Constantes.ADDATTRIBUTE_ASOCIACION)) {
            log.debug("valor de asociacion"+params.get("asociacion"));
            criteria.createCriteria(Constantes.ADDATTRIBUTE_ASOCIACION).add(Restrictions.eq("id",((Asociacion)params.get(Constantes.ADDATTRIBUTE_ASOCIACION)).getId()));
            countCriteria.createCriteria(Constantes.ADDATTRIBUTE_ASOCIACION).add(Restrictions.eq("id",((Asociacion)params.get(Constantes.ADDATTRIBUTE_ASOCIACION)).getId()));
        }

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("username", Constantes.CONTAINSKEY_FILTRO, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombre", Constantes.CONTAINSKEY_FILTRO, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apellido", Constantes.CONTAINSKEY_FILTRO, MatchMode.ANYWHERE));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey(Constantes.CONTAINSKEY_ORDER)) {
            String campo = (String) params.get(Constantes.CONTAINSKEY_ORDER);
            if (params.get(Constantes.CONTAINSKEY_SORT).equals(Constantes.CONTAINSKEY_DESC)) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_REPORTE)) {
            criteria.setFirstResult((Integer) params.get(Constantes.CONTAINSKEY_OFFSET));
            criteria.setMaxResults((Integer) params.get(Constantes.CONTAINSKEY_MAX));
        }
        params.put(Constantes.CONTAINSKEY_USUARIOS, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

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

    public Usuario obtienePorOpenId(String openId) {
        log.debug("Buscando usuario por openId {}", openId);
        Query query = currentSession().createQuery("select u from Usuario u where u.openId = :openId");
        query.setString(Constantes.OPEN_ID, openId);
        return (Usuario) query.uniqueResult();
    }

    public Usuario crea(Usuario usuario, Long asociacionId, String[] nombreDeRoles) {
        Asociacion asociacion = (Asociacion) currentSession().get(Asociacion.class, asociacionId);
        usuario.setAsociacion(asociacion);
        usuario.setPassword(passwordEncoder.encodePassword(usuario.getPassword(), usuario.getUsername()));

        if (usuario.getRoles() != null) {
            usuario.getRoles().clear();
        } else {
            usuario.setRoles(new HashSet<Rol>());
        }
        Query query = currentSession().createQuery("select r from Rol r where r.authority = :nombre");
        for (String nombre : nombreDeRoles) {
            log.debug("Nombre del rol"+nombre);
            query.setString("nombre", nombre);
            Rol rol = (Rol) query.uniqueResult();
            usuario.addRol(rol);
        }
        log.debug("Roles del usuario {}", usuario.getRoles());

        currentSession().save(usuario);
        currentSession().flush();

        return usuario;
    }

    public Usuario actualiza(Usuario usuario, Long asociacionId, String[] nombreDeRoles) {
        Usuario nuevoUsuario = (Usuario) currentSession().get(Usuario.class, usuario.getId());
        nuevoUsuario.setVersion(usuario.getVersion());
        nuevoUsuario.setUsername(usuario.getUsername());
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setApPaterno(usuario.getApPaterno());
        nuevoUsuario.setApMaterno(usuario.getApMaterno());

        nuevoUsuario.getRoles().clear();
        Query query = currentSession().createQuery("select r from Rol r where r.authority = :nombre");
        for (String nombre : nombreDeRoles) {
            query.setString("nombre", nombre);
            Rol rol = (Rol) query.uniqueResult();
            nuevoUsuario.addRol(rol);
        }
        try {
            currentSession().update(nuevoUsuario);
            currentSession().flush();
        } catch (NonUniqueObjectException e) {
            log.warn("Ya hay un objeto previamente cargado, intentando hacer merge", e);
            currentSession().merge(nuevoUsuario);
            currentSession().flush();
        }
        return nuevoUsuario;
    }

    public void actualiza(Usuario usuario) {
        currentSession().update(usuario);
        currentSession().flush();
    }

    public String elimina(Long id) throws UltimoException {
        Usuario usuario = obtiene(id);
        Criteria criteria = currentSession().createCriteria(Usuario.class);
        criteria.setProjection(Projections.rowCount());
        List resultados = criteria.createCriteria(Constantes.ADDATTRIBUTE_ASOCIACION).add(Restrictions.eq("id", usuario.getAsociacion().getId())).list();
        Long cantidad = (Long) resultados.get(0);
        if (cantidad > 1) {
            String nombre = usuario.getUsername();
            currentSession().delete(usuario);
            currentSession().flush();
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

    public List<Asociacion> obtieneAsociaciones() {
        List<Asociacion> asociaciones;
        if (springSecurityUtils.ifAnyGranted(Constantes.ROL_ADMINISTRADOR)) {
            //Query query = currentSession().createQuery("select a from Almacen a order by a.empresa.organizacion, a.empresa, a.nombre");
            Query query = currentSession().createQuery("select a from Asociacion a order by a.union, a.nombre");
            asociaciones = query.list();
        } else if (springSecurityUtils.ifAnyGranted("ROLE_ORG")) {
            Usuario usuario = springSecurityUtils.obtieneUsuario();
            Query query = currentSession().createQuery("select a from Asociaciones a where a.union.id = :unionId order by a.asociacion, a.nombre");
            query.setLong(Constantes.UNION_ID, usuario.getAsociacion().getUnion().getId());
            asociaciones = query.list();
        } else {
            Usuario usuario = springSecurityUtils.obtieneUsuario();
            Query query = currentSession().createQuery("select a from Asociaciones a where a.asociacion.id = :asociacionId order by a.nombre");
            query.setLong(Constantes.ASOCIACION_ID, usuario.getAsociacion().getId());
            asociaciones = query.list();
        }
        return asociaciones;
    }

    public void asignaAsociacion(Usuario usuario, Long asociacionId) {
        Asociacion asociacion = (Asociacion) currentSession().get(Asociacion.class, asociacionId);
        if (asociacion != null) {
            log.debug("Asignando {} a usuario {}", asociacion, usuario);
            String password = usuario.getPassword();
            currentSession().refresh(usuario);
            if (!password.equals(usuario.getPassword())) {
                usuario.setPassword(passwordEncoder.encodePassword(password, usuario.getUsername()));
            }
            usuario.setAsociacion(asociacion);
            currentSession().update(usuario);
            currentSession().flush();
        }
    }
}