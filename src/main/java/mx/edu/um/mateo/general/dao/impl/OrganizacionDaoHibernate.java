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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.dao.EmpresaDao;
import mx.edu.um.mateo.general.dao.OrganizacionDao;
import mx.edu.um.mateo.general.dao.ReporteDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class OrganizacionDaoHibernate extends BaseDao implements OrganizacionDao {

    @Autowired
    private ReporteDao reporteDao;
    @Autowired
    private EmpresaDao empresaDao;

    public OrganizacionDaoHibernate() {
        log.info("Nueva instancia de OrganizacionDao");
    }

    @Override
    @Transactional(readOnly = true)
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
        Criteria countCriteria = currentSession().createCriteria(
                Organizacion.class);

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombreCompleto", filtro,
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
        params.put("organizaciones", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    @Transactional(readOnly = true)
    public Organizacion obtiene(Long id) {
        Organizacion organizacion = (Organizacion) currentSession().get(
                Organizacion.class, id);
        return organizacion;
    }

    @Override
    public Organizacion crea(Organizacion organizacion, Usuario usuario) {
        Session session = currentSession();
        session.save(organizacion);
        Calendar cal = Calendar.getInstance();
        StringBuilder idEjercicio = new StringBuilder();
        idEjercicio.append("001-");
        idEjercicio.append(cal.get(Calendar.YEAR));
        EjercicioPK ejercicioPK = new EjercicioPK(idEjercicio.toString(), organizacion);
        Byte x = new Byte("0");
        Ejercicio ejercicio = new Ejercicio(ejercicioPK, idEjercicio.toString(), "A", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, x, x);
        session.save(ejercicio);
        log.debug("Ejercicio creado {}",ejercicio);
        Empresa empresa = new Empresa("MTZ", "MATRIZ", "MATRIZ",
                "000000000001", organizacion);
        if (usuario != null) {
            usuario.setEmpresa(empresa);
        }
        empresaDao.crea(empresa, usuario);
        reporteDao.inicializaOrganizacion(organizacion);
        session.refresh(organizacion);
        session.flush();
        return organizacion;
    }

    @Override
    public Organizacion crea(Organizacion organizacion) {
        return this.crea(organizacion, null);
    }

    @Override
    public Organizacion actualiza(Organizacion organizacion) {
        return this.actualiza(organizacion, null);
    }

    @Override
    public Organizacion actualiza(Organizacion organizacion, Usuario usuario) {
        Session session = currentSession();
        log.debug("NombreCompleto: {}", organizacion.getNombreCompleto());
        session.update(organizacion);
        session.flush();
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

    @Override
    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando organizacion {}", id);
        Criteria criteria = currentSession().createCriteria(Organizacion.class);
        criteria.setProjection(Projections.rowCount());
        Long cantidad = (Long) criteria.list().get(0);
        if (cantidad > 1) {
            Organizacion organizacion = obtiene(id);
            Query query = currentSession()
                    .createQuery(
                    "select o from Organizacion o where o.id != :organizacionId");
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
                                for (Empresa otraEmpresa : otraOrganizacion
                                        .getEmpresas()) {
                                    for (Almacen otroAlmacen : otraEmpresa
                                            .getAlmacenes()) {
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
            throw new UltimoException(
                    "No se puede eliminar porque es el ultimo");
        }
    }
}
