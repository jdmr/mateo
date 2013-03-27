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
package mx.edu.um.mateo.inventario.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.dao.CancelacionDao;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.inventario.model.Cancelacion;
import mx.edu.um.mateo.inventario.model.Folio;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class CancelacionDaoHibernate extends BaseDao implements CancelacionDao {

    public CancelacionDaoHibernate() {
        log.info("Nueva instance de CancelacionDao");
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de cancelaciones con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Cancelacion.class);
        Criteria countCriteria = currentSession().createCriteria(
                Cancelacion.class);

        if (params.containsKey("almacen")) {
            criteria.createCriteria("almacen").add(
                    Restrictions.idEq(params.get("almacen")));
            countCriteria.createCriteria("almacen").add(
                    Restrictions.idEq(params.get("almacen")));
        }

        if (params.containsKey("fechaIniciado")) {
            log.debug("Buscando desde {}", params.get("fechaIniciado"));
            criteria.add(Restrictions.ge("fechaCreacion",
                    params.get("fechaIniciado")));
            countCriteria.add(Restrictions.ge("fechaCreacion",
                    params.get("fechaIniciado")));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 1);
            log.debug("Asignando busqueda desde {}", calendar.getTime());
            criteria.add(Restrictions.ge("fechaCreacion", calendar.getTime()));
            countCriteria.add(Restrictions.ge("fechaCreacion",
                    calendar.getTime()));
        }

        if (params.containsKey("fechaTerminado")) {
            log.debug("Buscando hasta {}", params.get("fechaTerminado"));
            criteria.add(Restrictions.le("fechaCreacion",
                    params.get("fechaTerminado")));
            countCriteria.add(Restrictions.le("fechaCreacion",
                    params.get("fechaTerminado")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("folio", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("comentarios", filtro,
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
        } else {
            criteria.addOrder(Order.desc("fechaCreacion"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("cancelaciones", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    @Transactional(readOnly = true)
    public Cancelacion obtiene(Long id) {
        return (Cancelacion) currentSession().get(Cancelacion.class, id);
    }

    @Override
    public Cancelacion crea(Cancelacion cancelacion) {
        return this.crea(cancelacion, null);
    }

    @Override
    public Cancelacion crea(Cancelacion cancelacion, Usuario usuario) {
        Date fecha = new Date();
        cancelacion.setCreador((usuario != null) ? usuario.getUsername()
                : "sistema");
        cancelacion.setFechaCreacion(fecha);
        currentSession().save(cancelacion);
        return cancelacion;
    }

    @Override
    public String getFolio(Almacen almacen) {
        Query query = currentSession()
                .createQuery(
                "select f from Folio f where f.nombre = :nombre and f.almacen.id = :almacenId");
        query.setString("nombre", "CANCELACION");
        query.setLong("almacenId", almacen.getId());
        query.setLockOptions(LockOptions.UPGRADE);
        Folio folio = (Folio) query.uniqueResult();
        if (folio == null) {
            folio = new Folio("CANCELACION");
            folio.setAlmacen(almacen);
            currentSession().save(folio);
            return getFolio(almacen);
        }
        folio.setValor(folio.getValor() + 1);
        java.text.NumberFormat nf = java.text.DecimalFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(9);
        nf.setMaximumIntegerDigits(9);
        nf.setMaximumFractionDigits(0);
        StringBuilder sb = new StringBuilder();
        sb.append("C-");
        sb.append(almacen.getEmpresa().getOrganizacion().getCodigo());
        sb.append(almacen.getEmpresa().getCodigo());
        sb.append(almacen.getCodigo());
        sb.append(nf.format(folio.getValor()));
        return sb.toString();
    }
}
