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
package mx.edu.um.mateo.general.dao.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.SpringSecurityUtils;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class UsuarioDaoHibernate extends BaseDao implements UsuarioDao {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SpringSecurityUtils springSecurityUtils;

    public UsuarioDaoHibernate() {
        log.info("Se ha creado una nueva instancia de UsuarioDao");
    }

    @Override
    @Transactional(readOnly = true)
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

        Criteria criteria = currentSession().createCriteria(Usuario.class);
        Criteria countCriteria = currentSession().createCriteria(Usuario.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("username", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombre", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apellido", filtro,
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
        params.put("usuarios", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtiene(Long id) {
        Usuario usuario = (Usuario) currentSession().get(Usuario.class, id);
        return usuario;
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtiene(String username) {
        Query query = currentSession().createQuery(
                "select u from Usuario u where u.username = :username");
        query.setString("username", username);
        return (Usuario) query.uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtienePorOpenId(String openId) {
        log.debug("Buscando usuario por openId {}", openId);
        Query query = currentSession().createQuery(
                "select u from Usuario u where u.openId = :openId");
        query.setString("openId", openId);
        return (Usuario) query.uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtienePorCorreo(String correo) {
        log.debug("Buscando usuario por correo {}", correo);
        Query query = currentSession().createQuery(
                "select u from Usuario u where u.correo = :correo");
        query.setString("correo", correo);
        return (Usuario) query.uniqueResult();
    }

    @Override
    public Usuario crea(Usuario usuario, Long almacenId, String[] nombreDeRoles) {
        Almacen almacen = (Almacen) currentSession().get(Almacen.class,
                almacenId);
        usuario.setAlmacen(almacen);
        usuario.setEmpresa(almacen.getEmpresa());
        usuario.setPassword(passwordEncoder.encodePassword(
                usuario.getPassword(), usuario.getUsername()));

        if (usuario.getRoles() != null) {
            usuario.getRoles().clear();
        } else {
            usuario.setRoles(new HashSet<Rol>());
        }
        Query query = currentSession().createQuery(
                "select r from Rol r where r.authority = :nombre");
        for (String nombre : nombreDeRoles) {
            query.setString("nombre", nombre);
            Rol rol = (Rol) query.uniqueResult();
            usuario.addRol(rol);
        }
        log.debug("Roles del usuario {}", usuario.getRoles());
        log.debug("Ejercicio {}", usuario.getEjercicio().getId().getIdEjercicio());
        log.debug("Organizacion {}", almacen.getEmpresa().getOrganizacion());

        EjercicioPK pk = new EjercicioPK(usuario.getEjercicio().getId()
                .getIdEjercicio(), almacen.getEmpresa().getOrganizacion());
        Ejercicio ejercicio = (Ejercicio) currentSession().get(Ejercicio.class,
                pk);
        if (ejercicio != null) {
            usuario.setEjercicio(ejercicio);
        }

        currentSession().save(usuario);
        currentSession().flush();

        return usuario;
    }

    @Override
    public Usuario actualiza(Usuario usuario, Long almacenId,
            String[] nombreDeRoles) {
        Usuario nuevoUsuario = (Usuario) currentSession().get(Usuario.class,
                usuario.getId());
        nuevoUsuario.setVersion(usuario.getVersion());
        nuevoUsuario.setUsername(usuario.getUsername());
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setApPaterno(usuario.getApPaterno());
        nuevoUsuario.setApMaterno(usuario.getApMaterno());
        nuevoUsuario.setCorreo(usuario.getCorreo());

        nuevoUsuario.getRoles().clear();
        Query query = currentSession().createQuery(
                "select r from Rol r where r.authority = :nombre");
        for (String nombre : nombreDeRoles) {
            query.setString("nombre", nombre);
            Rol rol = (Rol) query.uniqueResult();
            nuevoUsuario.addRol(rol);
        }
        try {
            currentSession().update(nuevoUsuario);
            currentSession().flush();
        } catch (NonUniqueObjectException e) {
            log.warn(
                    "Ya hay un objeto previamente cargado, intentando hacer merge",
                    e);
            currentSession().merge(nuevoUsuario);
            currentSession().flush();
        }
        return nuevoUsuario;
    }

    @Override
    public Usuario actualiza(Usuario usuario, Long almacenId,
            String[] nombreDeRoles, String[] centrosDeCostoIds) {
        log.debug("Copiando propiedades");
        Usuario nuevoUsuario = (Usuario) currentSession().get(Usuario.class,
                usuario.getId());
        nuevoUsuario.setVersion(usuario.getVersion());
        nuevoUsuario.setUsername(usuario.getUsername());
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setApPaterno(usuario.getApPaterno());
        nuevoUsuario.setApMaterno(usuario.getApMaterno());
        nuevoUsuario.setCorreo(usuario.getCorreo());

        log.debug("Asignando roles {}", nombreDeRoles);
        nuevoUsuario.getRoles().clear();
        Query query = currentSession().createQuery(
                "select r from Rol r where r.authority = :nombre");
        for (String nombre : nombreDeRoles) {
            query.setString("nombre", nombre);
            Rol rol = (Rol) query.uniqueResult();
            nuevoUsuario.addRol(rol);
        }

        log.debug("Asignando centros de costo {}", centrosDeCostoIds);
        nuevoUsuario.getCentrosDeCosto().clear();
        Set<CentroCosto> centrosDeCosto = obtieneCentrosDeCosto(nuevoUsuario.getEjercicio(), centrosDeCostoIds);
        log.debug("CentrosDeCosto: {}", centrosDeCosto);
        nuevoUsuario.setCentrosDeCosto(centrosDeCosto);

        try {
            currentSession().update(nuevoUsuario);
            currentSession().flush();
        } catch (NonUniqueObjectException e) {
            log.warn(
                    "Ya hay un objeto previamente cargado, intentando hacer merge",
                    e);
            currentSession().merge(nuevoUsuario);
            currentSession().flush();
        }
        return nuevoUsuario;
    }

    @Override
    public void actualiza(Usuario usuario) {
        currentSession().update(usuario);
        currentSession().flush();
    }

    @Override
    @Transactional(rollbackFor = {UltimoException.class})
    public String elimina(Long id) throws UltimoException {
        Usuario usuario = obtiene(id);
        Criteria criteria = currentSession().createCriteria(Usuario.class);
        criteria.setProjection(Projections.rowCount());
        @SuppressWarnings("rawtypes")
        List resultados = criteria.createCriteria("empresa")
                .add(Restrictions.eq("id", usuario.getEmpresa().getId()))
                .list();
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

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<Rol> roles() {
        log.debug("Obteniendo lista de roles");
        Query query = currentSession().createQuery("select r from Rol r order by r.prioridad");
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<Almacen> obtieneAlmacenes() {
        List<Almacen> almacenes;
        if (springSecurityUtils.ifAnyGranted("ROLE_ADMIN")) {
            Query query = currentSession()
                    .createQuery(
                    "select a from Almacen a order by a.empresa.organizacion, a.empresa, a.nombre");
            almacenes = query.list();
        } else if (springSecurityUtils.ifAnyGranted("ROLE_ORG")) {
            Usuario usuario = springSecurityUtils.obtieneUsuario();
            Query query = currentSession()
                    .createQuery(
                    "select a from Almacen a where a.empresa.organizacion.id = :organizacionId order by a.empresa, a.nombre");
            query.setLong("organizacionId", usuario.getEmpresa()
                    .getOrganizacion().getId());
            almacenes = query.list();
        } else {
            Usuario usuario = springSecurityUtils.obtieneUsuario();
            Query query = currentSession()
                    .createQuery(
                    "select a from Almacen a where a.empresa.id = :empresaId order by a.nombre");
            query.setLong("empresaId", usuario.getEmpresa().getId());
            almacenes = query.list();
        }
        return almacenes;
    }

    @Override
    public void asignaAlmacen(Usuario usuario, Long almacenId,
            String ejercicioId) {
        Almacen almacen = (Almacen) currentSession().get(Almacen.class,
                almacenId);
        if (almacen != null) {
            log.debug("Asignando {} a usuario {}", almacen, usuario);
            String password = usuario.getPassword();
            currentSession().refresh(usuario);
            if (!password.equals(usuario.getPassword())) {
                usuario.setPassword(passwordEncoder.encodePassword(password,
                        usuario.getUsername()));
            }
            usuario.setAlmacen(almacen);
            usuario.setEmpresa(almacen.getEmpresa());
            if (ejercicioId != null) {
                EjercicioPK pk = new EjercicioPK(ejercicioId, almacen
                        .getEmpresa().getOrganizacion());
                Ejercicio ejercicio = (Ejercicio) currentSession().get(
                        Ejercicio.class, pk);
                if (ejercicio != null) {
                    usuario.setEjercicio(ejercicio);
                }
            }

            currentSession().update(usuario);
            currentSession().flush();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<Ejercicio> obtieneEjercicios(Long organizacionId) {
        Query query = currentSession()
                .createQuery(
                "select e from Ejercicio e where e.id.organizacion.id = :organizacionId order by e.id.idEjercicio desc");
        query.setLong("organizacionId", organizacionId);
        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroCosto> obtieneCentrosDeCosto(Ejercicio ejercicio) {
        Query query = currentSession().createQuery(
                "select cc from CentroCosto cc "
                + "where cc.id.ejercicio.id.organizacion.id = :organizacionId "
                + "and cc.id.ejercicio.id.idEjercicio = :ejercicioId "
                + "order by cc.id.idCosto");
        query.setLong("organizacionId", ejercicio.getId().getOrganizacion().getId());
        query.setString("ejercicioId", ejercicio.getId().getIdEjercicio());
        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<CentroCosto> obtieneCentrosDeCosto(Ejercicio ejercicio, String[] centrosDeCostoIds) {
        Criteria criteria = currentSession().createCriteria(CentroCosto.class);
        criteria.add(Restrictions.eq("id.ejercicio", ejercicio));
        criteria.add(Restrictions.in("id.idCosto", centrosDeCostoIds));
        return new HashSet(criteria.list());
    }
}
