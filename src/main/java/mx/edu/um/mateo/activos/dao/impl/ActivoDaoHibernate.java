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
package mx.edu.um.mateo.activos.dao.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.activos.dao.ActivoDao;
import mx.edu.um.mateo.activos.model.Activo;
import mx.edu.um.mateo.activos.model.FolioActivo;
import mx.edu.um.mateo.activos.model.TipoActivo;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
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
public class ActivoDaoHibernate extends BaseDao implements ActivoDao {

    public ActivoDaoHibernate() {
        log.info("Nueva instancia de Activo Dao creada.");
    }

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de activos con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Activo.class);
        Criteria countCriteria = currentSession().createCriteria(Activo.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("tipoActivoId")) {
            criteria.createCriteria("tipoActivo").add(Restrictions.idEq(params.get("tipoActivoId")));
            countCriteria.createCriteria("tipoActivo").add(Restrictions.idEq(params.get("tipoActivoId")));
        }

        if (params.containsKey("cuentaId")) {
            criteria.createCriteria("cuenta").add(Restrictions.idEq(params.get("cuentaId")));
            countCriteria.createCriteria("cuenta").add(Restrictions.idEq(params.get("cuentaId")));
        }
        
        if (params.containsKey("proveedorId")) {
            criteria.createCriteria("proveedor").add(Restrictions.idEq(params.get("proveedorId")));
            countCriteria.createCriteria("proveedor").add(Restrictions.idEq(params.get("proveedorId")));
        }
        
        if (params.containsKey("fechaIniciado")) {
            criteria.add(Restrictions.ge("fechaCompra", params.get("fechaIniciado")));
            countCriteria.add(Restrictions.ge("fechaCompra", params.get("fechaIniciado")));
        }
        
        if (params.containsKey("bajas")) {
            criteria.add(Restrictions.eq("inactivo", true));
        }
        
        if (params.containsKey("reubicaciones")) {
            criteria.createCriteria("reubicaciones").add(Restrictions.isNotNull("id"));
            countCriteria.createCriteria("reubicaciones").add(Restrictions.isNotNull("id"));
        }
        
        if (params.containsKey("fechaTerminado")) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date) params.get("fechaTerminado"));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            criteria.add(Restrictions.le("fechaCompra", cal.getTime()));
            countCriteria.add(Restrictions.le("fechaCompra", cal.getTime()));
        }
        
        if (params.containsKey("responsableNombre")) {
            criteria.add(Restrictions.ilike("responsable", (String)params.get("responsable"), MatchMode.ANYWHERE));
            countCriteria.add(Restrictions.ilike("responsable", (String)params.get("responsable"), MatchMode.ANYWHERE));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("folio", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("procedencia", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("factura", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("pedimento", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("poliza", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("codigo", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("descripcion", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("marca", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("modelo", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("ubicacion", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("responsable", filtro, MatchMode.ANYWHERE));
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
        criteria.addOrder(Order.desc("fechaModificacion"));

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("activos", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        ProjectionList list = Projections.projectionList();
        list.add(Projections.sum("depreciacionAnual"), "depreciacionAnual");
        list.add(Projections.sum("depreciacionMensual"), "depreciacionMensual");
        list.add(Projections.sum("depreciacionAcumulada"), "depreciacionAcumulada");
        list.add(Projections.sum("moi"), "moi");
        list.add(Projections.groupProperty("fechaDepreciacion"), "fechaDepreciacion");
        countCriteria.setProjection(list);

        List proyecciones = countCriteria.list();
        Iterator iterator = proyecciones.iterator();
        if (iterator.hasNext()) {
            Object[] obj = (Object[]) iterator.next();
            NumberFormat nf = DecimalFormat.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date fecha;
            if (obj[4] != null) {
                fecha = (Date) obj[4];
            } else {
                fecha = new Date();
            }
            params.put("resumen", new String[]{nf.format(obj[0]), nf.format(obj[1]), nf.format(obj[2]), nf.format(obj[3]), sdf.format(fecha)});
        }

        return params;
    }

    @Override
    public Activo obtiene(Long id) {
        Activo activo = (Activo) currentSession().get(Activo.class, id);
        return activo;
    }

    @Override
    public Activo crea(Activo activo, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            activo.setEmpresa(usuario.getEmpresa());
        }
        activo.setTipoActivo((TipoActivo) session.load(TipoActivo.class, activo.getTipoActivo().getId()));
        activo.setFolio(this.getFolio(activo.getEmpresa()));
        session.save(activo);
        session.flush();
        return activo;
    }

    @Override
    public Activo crea(Activo activo) {
        return this.crea(activo, null);
    }

    @Override
    public Activo actualiza(Activo activo) {
        return this.actualiza(activo, null);
    }

    @Override
    public Activo actualiza(Activo activo, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            activo.setEmpresa(usuario.getEmpresa());
        }
        activo.setTipoActivo((TipoActivo) session.load(TipoActivo.class, activo.getTipoActivo().getId()));
        session.update(activo);
        session.flush();
        return activo;
    }

    @Override
    public String elimina(Long id) {
        Activo activo = obtiene(id);
        String nombre = activo.getFolio();
        currentSession().delete(activo);
        currentSession().flush();
        return nombre;
    }

    public void depreciar(Date fecha, Empresa empresa) {
        Query query = currentSession().createQuery("select new Activo(a.id, a.version, a.moi, a.fechaCompra, a.tipoActivo.porciento, a.tipoActivo.vidaUtil, a.inactivo, a.fechaInactivo) from Activo a inner join a.tipoActivo where a.empresa.id = :empresaId and a.fechaCreacion <= :fecha");
        query.setLong("empresaId", empresa.getId());
        query.setDate("fecha", fecha);
        List<Activo> activos = query.list();
        for (Activo activo : activos) {
            // depreciacion anual
            BigDecimal porciento = activo.getPorciento();
            BigDecimal depreciacionAnual = activo.getMoi().multiply(porciento);

            // depreciacion mensual
            BigDecimal depreciacionMensual = BigDecimal.ZERO;
            Date fechaCompra = activo.getFechaCompra();
            if (fechaCompra.before(fecha) && days360(fechaCompra, fecha) / 30 < activo.getVidaUtil()) {
                depreciacionMensual = depreciacionAnual.divide(new BigDecimal("12"));
            }
            
            // depreciacion acumulada
            BigDecimal depreciacionAcumulada = BigDecimal.ZERO;
            Long meses = 0L;
            if ((fechaCompra.before(fecha) && !activo.getInactivo()) 
                    || (fechaCompra.before(fecha) && activo.getInactivo() && activo.getFechaInactivo().after(fecha))) {
                meses = days360(fechaCompra, fecha) / 30;
            } else if (fechaCompra.before(fecha) && activo.getInactivo() && activo.getFechaInactivo().before(fecha)) {
                meses = days360(fechaCompra, activo.getFechaInactivo()) / 30;
            }
            if (meses < activo.getVidaUtil()) {
                depreciacionAcumulada = depreciacionMensual.multiply(new BigDecimal(meses));
            }
            
            // valor neto
            BigDecimal valorNeto = activo.getMoi().subtract(depreciacionAcumulada);
            
            query = currentSession().createQuery("update Activo a set a.fechaDepreciacion = :fecha, a.depreciacionAnual = :depreciacionAnual, a.depreciacionMensual = :depreciacionMensual, a.depreciacionAcumulada = :depreciacionAcumulada, a.valorNeto = :valorNeto where a.id = :activoId");
            query.setDate("fecha", fecha);
            query.setBigDecimal("depreciacionAnual", depreciacionAnual);
            query.setBigDecimal("depreciacionMensual", depreciacionMensual);
            query.setBigDecimal("depreciacionAcumulada", depreciacionAcumulada);
            query.setBigDecimal("valorNeto", valorNeto);
            query.setLong("activoId", activo.getId());
            query.executeUpdate();
        }
    }

    /**
     * Calcula el número de días entre dos fechas basándose en un año de 360
     * días (doce meses de 30 días) que se utiliza en algunos cálculos
     * contables. Esta función facilita el cálculo de pagos si su sistema de
     * contabilidad se basa en 12 meses de 30 días.
     *
     * @param dateBegin	The purchase date
     * @param dateEnd	The depreciation date
     */
    private Long days360(Date inicio, Date fin) {
        Calendar dateBegin = Calendar.getInstance();
        Calendar dateEnd = Calendar.getInstance();
        dateBegin.setTime(inicio);
        dateEnd.setTime(fin);
        long dference;
        long yearsBetwen;
        long daysInMonths;
        long daysLastMonth;
        long serialBegin;
        long serialEnd;
        yearsBetwen = dateBegin.get(Calendar.YEAR) - 1900;
        daysInMonths = (dateBegin.get(Calendar.MONTH) + 1) * 30;
        daysLastMonth = dateBegin.get(Calendar.DAY_OF_MONTH);
        if (daysLastMonth == 31) {
            daysLastMonth = 30;
        }

        serialBegin = yearsBetwen * 360 + daysInMonths + daysLastMonth;

        yearsBetwen = dateEnd.get(Calendar.YEAR) - 1900;
        daysInMonths = (dateEnd.get(Calendar.MONTH) + 1) * 30;
        daysLastMonth = dateEnd.get(Calendar.DAY_OF_MONTH);
        if (daysLastMonth == 31) {
            if (dateBegin.get(Calendar.DAY_OF_MONTH) < 30) {
                daysInMonths += 30;
                daysLastMonth = 1;
            } else {
                daysLastMonth = 30;
            }
        }

        serialEnd = yearsBetwen * 360 + daysInMonths + daysLastMonth;

        dference = serialEnd - serialBegin;
        return dference;
    }

    private String getFolio(Empresa empresa) {
        Query query = currentSession().createQuery("select f from FolioActivo f where f.nombre = :nombre and f.organizacion.id = :almacenId");
        query.setString("nombre", "ACTIVO");
        query.setLong("organizacionId", empresa.getOrganizacion().getId());
        query.setLockOptions(LockOptions.UPGRADE);
        FolioActivo folio = (FolioActivo) query.uniqueResult();
        if (folio == null) {
            folio = new FolioActivo("ACTIVO");
            folio.setOrganizacion(empresa.getOrganizacion());
            currentSession().save(folio);
            return getFolio(empresa);
        }
        folio.setValor(folio.getValor() + 1);
        java.text.NumberFormat nf = java.text.DecimalFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(7);
        nf.setMaximumIntegerDigits(7);
        nf.setMaximumFractionDigits(0);
        StringBuilder sb = new StringBuilder();
        sb.append("A-");
        sb.append(empresa.getOrganizacion().getCodigo());
        sb.append(empresa.getCodigo());
        sb.append(nf.format(folio.getValor()));
        return sb.toString();
    }

    @Override
    public void arreglaFechas() {
        Query query = currentSession().createQuery("from Activo order by fechaCompra");
        List<Activo> activos = query.list();
        int cont = 0;
        for(Activo activo : activos) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(activo.getFechaCompra());
            if (cal1.get(Calendar.YEAR) < 10) {
                log.debug("Pasando al año 2000 {}", activo);
                cal1.add(Calendar.YEAR, 2000);
                activo.setFechaCompra(cal1.getTime());
                currentSession().update(activo);
            } else if (cal1.get(Calendar.YEAR) < 100) {
                log.debug("Pasando al año 1900 {}", activo);
                cal1.add(Calendar.YEAR, 1900);
                activo.setFechaCompra(cal1.getTime());
                currentSession().update(activo);
            } else if (cal1.get(Calendar.YEAR) > 1900) {
                currentSession().flush();
                break;
            }
            cont++;
        }
        log.debug("Termino actualizando {} de {}", cont, activos.size());
    }
}
