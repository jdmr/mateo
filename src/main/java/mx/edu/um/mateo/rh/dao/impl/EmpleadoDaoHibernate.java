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
package mx.edu.um.mateo.rh.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.rh.dao.ClaveEmpleadoDao;
import mx.edu.um.mateo.rh.dao.EmpleadoDao;
import mx.edu.um.mateo.rh.model.ClaveEmpleado;
import mx.edu.um.mateo.rh.model.Empleado;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Omar Soto <osoto@um.edu.mx>
 */
@Repository
@Transactional
public class EmpleadoDaoHibernate extends BaseDao implements EmpleadoDao {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClaveEmpleadoDao claveDao;

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de empleados con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Empleado.class);
        Criteria countCriteria = currentSession().createCriteria(Empleado.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("clave", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombre", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apMaterno", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apPaterno", filtro,
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
        params.put(Constantes.EMPLEADO_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        log.debug("Elementos en lista de empleados {}", params.get(Constantes.EMPLEADO_LIST));

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#obtiene(java.lang.Long)
     */
    @Override
    public Empleado obtiene(Long id) {
        Empleado empleado = (Empleado) currentSession().get(Empleado.class, id);
        return empleado;
    }

    @Override
    public Empleado graba(Empleado empleado, Usuario usuario, ClaveEmpleado clave) {
        Session session = currentSession();

        if (usuario != null) {
            empleado.setEmpresa(usuario.getEmpresa());
            empleado.setAlmacen(usuario.getAlmacen());
        }
        empleado.setPassword(passwordEncoder.encodePassword(
                empleado.getPassword(), empleado.getUsername()));
        session.saveOrUpdate(empleado);
//        session.merge(empleado);
        session.flush();
        return empleado;
    }

    @Override
    public Empleado graba(Empleado empleado) {
        return this.graba(empleado, null, null);
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.EmpleadoDao#getEmpleadosBday(mx.edu.um.mateo.rh.model.Empleado)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List getEmpleadosBday(Empleado empleado) {
        List lista = new ArrayList();

        if (empleado != null) {
            if (empleado.getClave() != null && empleado.getFechaNacimiento() != null) {

                Criteria sql = getSession().createCriteria(Empleado.class);
                sql.add(Restrictions.like("clave", empleado.getClave() + "%"));

                Locale locale = new Locale("es", "MX", "Traditional_WIN");

                Calendar gc = new GregorianCalendar(locale);
                Calendar tmp = new GregorianCalendar(locale);

                tmp.setTime(new Date());

                gc.setTime(empleado.getFechaNacimiento());
                gc.set(Calendar.YEAR, tmp.get(Calendar.YEAR) - 100);

                tmp = null;

                sql.add(this.getQueryByMonth(gc));

                sql.addOrder(Order.asc("clave"));

                lista = sql.list();
            }

        }

        return lista;
    }

    private Criterion getQueryByMonth(Calendar gc) {
        Criterion cr = null;

        gc.add(Calendar.YEAR, 1);

        gc.set(Calendar.DAY_OF_MONTH, 1);
        Date fechaI = gc.getTime();

        gc.set(Calendar.DAY_OF_MONTH, gc.getMaximum(Calendar.DAY_OF_MONTH));
        Date fechaF = gc.getTime();

        cr = Restrictions.between("fechaNacimiento", fechaI, fechaF);

        Calendar tmp = (Calendar) gc.clone();
        tmp.clear();
        tmp.setTime(new Date());
        tmp.add(Calendar.YEAR, -17);

        if (gc.compareTo(tmp) <= 0) {
            return Restrictions.or(cr, getQueryByMonth(gc));
        } else {
            return cr;
        }
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#getEmpleado(Empleado empleado)
     */
    @Override
    @Transactional(readOnly = true)
    public Empleado getEmpleado(final Empleado empleado) {
        Empleado emp = new Empleado();

        if (empleado != null) {
            Criteria sql = getSession().createCriteria(Empleado.class);

            // Buscar por id
            if (empleado.getId() != null) {
                sql.add(Restrictions.idEq(empleado.getId()));
            } // Buscar por clave
            else if (empleado.getClave() != null
                    && !"".equals(empleado.getClave())) {
                sql.add(Restrictions.eq("clave", empleado.getClave()));
            }
            emp = (Empleado) sql.uniqueResult();

        }
        if (emp == null) {
            log.warn("uh oh, empleado with id '" + empleado.getId()
                    + "' not found...");
            throw new ObjectRetrievalFailureException(Empleado.class,
                    empleado.getId());
        }
        return emp;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#saveEmpleado(Empleado empleado)
     */
    @Override
    public void saveEmpleado(final Empleado empleado) {
        this.saveEmpleado(empleado, null, null);
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.EmpleadoDao#saveEmpleado(mx.edu.um.mateo.rh.model.Empleado,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    @Override
    @Transactional
    public void saveEmpleado(final Empleado empleado, Usuario usuario, ClaveEmpleado ce) {
        if (usuario != null) {
            empleado.setEmpresa(usuario.getEmpresa());
        }
        empleado.setPassword(passwordEncoder.encodePassword(
                usuario.getPassword(), usuario.getUsername()));
        currentSession().saveOrUpdate(empleado);
        ce.setEmpleado(empleado);
        claveDao.graba(ce, usuario);
    }


    /**
     * @see
     * mx.edu.um.mateo.rh.dao.EmpleadoDao#searchEmpleado(mx.edu.um.mateo.rh.model.Empleado)
     */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Empleado> searchEmpleado(Empleado empleado) {
        Criteria criteria = null;
        List<Empleado> empleados = null;

        criteria = getSession().createCriteria(Empleado.class);

        if (empleado.getClave() != null && !"".equals(empleado.getClave())) {
            criteria.add(Restrictions.ilike("clave", empleado.getClave() + "%"));
        }

        if (empleado.getApPaterno() != null && !"".equals(empleado.getApPaterno())) {
            criteria.add(Restrictions.ilike("apPaterno", empleado.getApPaterno() + "%"));
        }
        if (empleado.getApMaterno() != null && !"".equals(empleado.getApMaterno())) {
            criteria.add(Restrictions.ilike("apMaterno", empleado.getApMaterno() + "%"));
        }
        if (empleado.getNombre() != null && !"".equals(empleado.getNombre())) {
            criteria.add(Restrictions.ilike("nombre", empleado.getNombre() + "%"));
        }

        criteria.addOrder(Order.asc("clave"));
        empleados = criteria.list();

        return empleados;

    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.EmpleadoDao#searchEmpleadoByClaveOrApPaterno(mx.edu.um.mateo.rh.model.Empleado)
     */
    @Override
    @Transactional(readOnly = true)
    public List<Empleado> searchEmpleadoByClaveOrApPaterno(Empleado empleado) {
        if (empleado.getClave() != null
                && !"".equals(empleado.getClave())
                && empleado
                .getClave()
                .substring(0,
                        (empleado.getClave().length() > 2) ? 2 : 1)
                .equals("98".substring(0,
                                (empleado.getClave().length() > 2) ? 2 : 1))) {
            empleado.setApPaterno("");
        } else if (empleado.getApPaterno() != null
                && !"".equals(empleado.getApPaterno())) {
            empleado.setClave("");
        }

        return this.searchEmpleado(empleado);

    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.EmpleadoDao#getEmpleadoClave(mx.edu.um.mateo.rh.model.Empleado)
     */
    @Override
    @Transactional(readOnly = true)
    public Empleado getEmpleadoClave(Empleado empleado) {
        Empleado emp = (Empleado) getSession()
                .createCriteria(Empleado.class)
                .add(org.hibernate.criterion.Restrictions.eq("clave",
                                empleado.getClave())).uniqueResult();

        if (emp == null) {
            log.warn("uh oh, empleado with clave '" + empleado.getClave()
                    + "' not found...");
            throw new ObjectRetrievalFailureException(Empleado.class,
                    empleado.getClave());
        }

        return emp;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#getEmpleado(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public Empleado getEmpleado(final String clave) {
        Empleado emp = (Empleado) getSession().createCriteria(Empleado.class)
                .add(org.hibernate.criterion.Restrictions.eq("clave", clave))
                .uniqueResult();

        if (emp == null) {
            log.warn("uh oh, empleado with clave '" + clave + "' not found...");
            throw new ObjectRetrievalFailureException(Empleado.class, clave);
        }

        return emp;
    }

    @Override
    public void actualiza(Empleado empleado) {
        currentSession().update(empleado);
        currentSession().flush();
    }
}
