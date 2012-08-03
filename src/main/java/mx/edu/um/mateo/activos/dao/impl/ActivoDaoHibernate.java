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

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.activos.dao.ActivoDao;
import mx.edu.um.mateo.activos.model.Activo;
import mx.edu.um.mateo.activos.model.BajaActivo;
import mx.edu.um.mateo.activos.model.FolioActivo;
import mx.edu.um.mateo.activos.model.ReubicacionActivo;
import mx.edu.um.mateo.activos.model.TipoActivo;
import mx.edu.um.mateo.activos.model.XActivo;
import mx.edu.um.mateo.contabilidad.model.CCostoPK;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Proveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
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

        Date fechaIniciado = null;
        if (params.containsKey("fechaIniciado")) {
            fechaIniciado = (Date) params.get("fechaIniciado");
            criteria.add(Restrictions.ge("fechaCompra", fechaIniciado));
            countCriteria.add(Restrictions.ge("fechaCompra", fechaIniciado));
        }

        Date fechaTerminado = null;
        if (params.containsKey("fechaTerminado")) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date) params.get("fechaTerminado"));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            fechaTerminado = cal.getTime();
            criteria.add(Restrictions.le("fechaCompra", fechaTerminado));
            countCriteria.add(Restrictions.le("fechaCompra", fechaTerminado));
        }

        if (params.containsKey("bajas")) {
            if (fechaIniciado != null) {
                criteria.add(Restrictions.eq("inactivo", true));
                countCriteria.add(Restrictions.eq("inactivo", true));
                criteria.add(Restrictions.ge("fechaInactivo", fechaIniciado));
                countCriteria.add(Restrictions.ge("fechaInactivo", fechaIniciado));
                if (fechaTerminado != null) {
                    criteria.add(Restrictions.le("fechaInactivo", fechaTerminado));
                    countCriteria.add(Restrictions.le("fechaInactivo", fechaTerminado));
                }
            } else if (fechaTerminado != null) {
                criteria.add(Restrictions.eq("inactivo", true));
                countCriteria.add(Restrictions.eq("inactivo", true));
                criteria.add(Restrictions.le("fechaInactivo", fechaTerminado));
                countCriteria.add(Restrictions.le("fechaInactivo", fechaTerminado));
            } else {
                criteria.add(Restrictions.eq("inactivo", true));
                countCriteria.add(Restrictions.eq("inactivo", true));
            }
        } else {
            criteria.add(Restrictions.eq("inactivo", false));
            countCriteria.add(Restrictions.eq("inactivo", false));
        }

        if (params.containsKey("reubicaciones")) {
            if (fechaIniciado != null) {
                criteria.add(Restrictions.isNotNull("fechaReubicado"));
                countCriteria.add(Restrictions.isNotNull("fechaReubicado"));
                criteria.add(Restrictions.ge("fechaReubicado", fechaIniciado));
                countCriteria.add(Restrictions.ge("fechaReubicado", fechaIniciado));
                if (fechaTerminado != null) {
                    criteria.add(Restrictions.le("fechaReubicado", fechaIniciado));
                    countCriteria.add(Restrictions.le("fechaReubicado", fechaIniciado));
                }
            } else if (fechaTerminado != null) {
                criteria.add(Restrictions.isNotNull("fechaReubicado"));
                countCriteria.add(Restrictions.isNotNull("fechaReubicado"));
                criteria.add(Restrictions.le("fechaReubicado", fechaIniciado));
                countCriteria.add(Restrictions.le("fechaReubicado", fechaIniciado));
            } else {
                criteria.add(Restrictions.isNotNull("fechaReubicado"));
                countCriteria.add(Restrictions.isNotNull("fechaReubicado"));
            }
        }

        if (params.containsKey("responsableNombre")) {
            criteria.add(Restrictions.ilike("responsable", (String) params.get("responsable"), MatchMode.ANYWHERE));
            countCriteria.add(Restrictions.ilike("responsable", (String) params.get("responsable"), MatchMode.ANYWHERE));
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
        } else {
            criteria.addOrder(Order.desc("folio"));
        }

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
        Date fecha = new Date();
        if (usuario != null) {
            activo.setEmpresa(usuario.getEmpresa());
        }
        CCostoPK pk = new CCostoPK(usuario.getEjercicio(), activo.getCentroCosto().getId().getIdCosto());
        activo.setCentroCosto((CentroCosto) session.load(CentroCosto.class, pk));
        activo.setTipoActivo((TipoActivo) session.load(TipoActivo.class, activo.getTipoActivo().getId()));
        activo.setProveedor((Proveedor) session.load(Proveedor.class, activo.getProveedor().getId()));
        activo.setFolio(this.getFolio(activo.getEmpresa()));
        activo.setFechaCreacion(fecha);
        activo.setFechaModificacion(fecha);
        session.save(activo);
        audita(activo, usuario, Constantes.CREAR, fecha);
        session.flush();
        return activo;
    }

    @Override
    public Activo crea(Activo activo) {
        return this.crea(activo, null);
    }

    @Override
    public void depreciar(Date fecha, Long empresaId) {
        Date fechaModificacion = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        fecha = cal.getTime();
        Query query = currentSession().createQuery("select new Activo(a.id, a.version, a.moi, a.fechaCompra, a.tipoActivo.porciento, a.tipoActivo.vidaUtil, a.inactivo, a.fechaInactivo) from Activo a inner join a.tipoActivo where a.empresa.id = :empresaId and a.fechaCompra <= :fecha");
        query.setLong("empresaId", empresaId);
        query.setDate("fecha", fecha);
        List<Activo> activos = query.list();
        int cont = 0;
        int total = activos.size();
        for (Activo activo : activos) {
            if (++cont % 1000 == 0) {
                log.debug("Depreciando activo {} ({} / {})", new Object[]{activo.getId(), cont, total});
            }

            activo = deprecia(activo, fecha);

            query = currentSession().createQuery("update Activo a set a.fechaDepreciacion = :fecha, a.depreciacionAnual = :depreciacionAnual, a.depreciacionMensual = :depreciacionMensual, a.depreciacionAcumulada = :depreciacionAcumulada, a.valorNeto = :valorNeto, a.fechaModificacion = :fechaModificacion where a.id = :activoId");
            query.setDate("fecha", fecha);
            query.setBigDecimal("depreciacionAnual", activo.getDepreciacionAnual());
            query.setBigDecimal("depreciacionMensual", activo.getDepreciacionMensual());
            query.setBigDecimal("depreciacionAcumulada", activo.getDepreciacionAcumulada());
            query.setBigDecimal("valorNeto", activo.getValorNeto());
            query.setTimestamp("fechaModificacion", fechaModificacion);
            query.setLong("activoId", activo.getId());
            query.executeUpdate();
        }
        currentSession().flush();
        log.info("Se han depreciado los activos de la empresa {} para la fecha de {}", empresaId, fecha);
    }

    private Activo deprecia(Activo activo, Date fecha) {
        // depreciacion anual
        BigDecimal porciento = activo.getPorciento();
        log.trace("Activo - MOI - Porciento: {} - {} - {}", new Object[]{activo.getId(), activo.getMoi(), porciento});
        BigDecimal depreciacionAnual = activo.getMoi().multiply(porciento);
        log.trace("DepreciacionAnual: {}", depreciacionAnual);

        // depreciacion mensual
        BigDecimal depreciacionMensual = BigDecimal.ZERO;
        Date fechaCompra = activo.getFechaCompra();
        if (fechaCompra.before(fecha) && days360(fechaCompra, fecha) / 30 < activo.getVidaUtil()) {
            depreciacionMensual = depreciacionAnual.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        }
        log.trace("DepreciacionMensual: {}", depreciacionMensual);

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
        log.trace("DepreciacionAcumulada: {}", depreciacionAcumulada);

        // valor neto
        BigDecimal valorNeto = activo.getMoi().subtract(depreciacionAcumulada);
        log.trace("ValorNeto: {}", valorNeto);

        activo.setFechaDepreciacion(fecha);
        activo.setDepreciacionAnual(depreciacionAnual);
        activo.setDepreciacionMensual(depreciacionMensual);
        activo.setDepreciacionAcumulada(depreciacionAcumulada);
        activo.setValorNeto(valorNeto);

        return activo;
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
        Query query = currentSession().createQuery("select f from FolioActivo f where f.nombre = :nombre and f.organizacion.id = :organizacionId");
        query.setString("nombre", "ACTIVOS");
        query.setLong("organizacionId", empresa.getOrganizacion().getId());
        query.setLockOptions(LockOptions.UPGRADE);
        FolioActivo folio = (FolioActivo) query.uniqueResult();
        if (folio == null) {
            folio = new FolioActivo("ACTIVOS");
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
    public void arreglaFechas(OutputStream out) {
        log.debug("Arreglando fechas");
        Date inicio = new Date();
        XSSFWorkbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
        XSSFSheet fechas = wb.createSheet("FECHAS-ANTERIORES");
        int fechasRow = 0;
        XSSFSheet fechas2 = wb.createSheet("FECHAS-POSTERIORES");
        int fechas2Row = 0;
        Transaction tx = null;
        try {
            tx = currentSession().beginTransaction();
            Query update = currentSession().createQuery("update Activo set fechaCompra = :fechaCompra where id = :id");
            Query query = currentSession().createQuery("select new Activo(a.id, a.descripcion, a.fechaCompra, a.tipoActivo.cuenta.id.idCtaMayor, a.centroCosto.id.idCosto, a.codigo) from Activo a where a.fechaCompra < :fechaCompra order by a.tipoActivo.cuenta.id.idCtaMayor, a.centroCosto.id.idCosto, a.codigo");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            query.setDate("fechaCompra", sdf.parse("01/01/1970"));
            List<Activo> activos = query.list();
            int cont = 0;
            for (Activo activo : activos) {
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(activo.getFechaCompra());
                if (cal1.get(Calendar.YEAR) < 10) {
                    log.debug("Pasando al año 2000 {} - {}", activo.getDescripcion(), activo.getFechaCompra());
                    cal1.add(Calendar.YEAR, 2000);
                    update.setDate("fechaCompra", cal1.getTime());
                    update.setLong("id", activo.getId());
                    update.executeUpdate();
                    XSSFRow renglon = fechas.createRow(fechasRow++);
                    renglon.createCell(0).setCellValue(activo.getTipoActivoCuenta());
                    renglon.createCell(1).setCellValue(activo.getCentroCostoCuenta());
                    renglon.createCell(2).setCellValue(activo.getCodigo());
                    renglon.createCell(3).setCellValue(activo.getDescripcion());
                    renglon.createCell(4).setCellValue(sdf.format(activo.getFechaCompra()));
                    Cell cell = renglon.createCell(5);
                    cell.setCellValue(cal1.getTime());
                    cell.setCellStyle(cellStyle);
                } else if (cal1.get(Calendar.YEAR) < 100) {
                    log.debug("Pasando al año 1900 {} - {}", activo.getDescripcion(), activo.getFechaCompra());
                    cal1.add(Calendar.YEAR, 1900);
                    update.setDate("fechaCompra", cal1.getTime());
                    update.setLong("id", activo.getId());
                    update.executeUpdate();
                    XSSFRow renglon = fechas.createRow(fechasRow++);
                    renglon.createCell(0).setCellValue(activo.getTipoActivoCuenta());
                    renglon.createCell(1).setCellValue(activo.getCentroCostoCuenta());
                    renglon.createCell(2).setCellValue(activo.getCodigo());
                    renglon.createCell(3).setCellValue(activo.getDescripcion());
                    renglon.createCell(4).setCellValue(sdf.format(activo.getFechaCompra()));
                    Cell cell = renglon.createCell(5);
                    cell.setCellValue(cal1.getTime());
                    cell.setCellStyle(cellStyle);
                } else if (cal1.get(Calendar.YEAR) >= 1900 && cal1.get(Calendar.YEAR) <= 1912) {
                    log.debug("Pasando al año 2000 {} - {}", activo.getDescripcion(), activo.getFechaCompra());
                    cal1.add(Calendar.YEAR, 100);
                    update.setDate("fechaCompra", cal1.getTime());
                    update.setLong("id", activo.getId());
                    update.executeUpdate();
                    XSSFRow renglon = fechas.createRow(fechasRow++);
                    renglon.createCell(0).setCellValue(activo.getTipoActivoCuenta());
                    renglon.createCell(1).setCellValue(activo.getCentroCostoCuenta());
                    renglon.createCell(2).setCellValue(activo.getCodigo());
                    renglon.createCell(3).setCellValue(activo.getDescripcion());
                    renglon.createCell(4).setCellValue(sdf.format(activo.getFechaCompra()));
                    Cell cell = renglon.createCell(5);
                    cell.setCellValue(cal1.getTime());
                    cell.setCellStyle(cellStyle);
                }
                cont++;
            }
            currentSession().flush();


            query = currentSession().createQuery("select new Activo(a.id, a.descripcion, a.fechaCompra, a.tipoActivo.cuenta.id.idCtaMayor, a.centroCosto.id.idCosto, a.codigo) from Activo a where a.fechaCompra > :fechaCompra order by a.tipoActivo.cuenta.id.idCtaMayor, a.centroCosto.id.idCosto, a.codigo");
            query.setDate("fechaCompra", new Date());
            activos = query.list();
            for (Activo activo : activos) {
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(activo.getFechaCompra());
                if (cal1.get(Calendar.YEAR) < 2020) {
                    log.debug("Quitandole 10 anios {} - {}", activo.getDescripcion(), activo.getFechaCompra());
                    cal1.add(Calendar.YEAR, -10);
                    update.setDate("fechaCompra", cal1.getTime());
                    update.setLong("id", activo.getId());
                    update.executeUpdate();
                    XSSFRow renglon = fechas2.createRow(fechas2Row++);
                    renglon.createCell(0).setCellValue(activo.getTipoActivoCuenta());
                    renglon.createCell(1).setCellValue(activo.getCentroCostoCuenta());
                    renglon.createCell(2).setCellValue(activo.getCodigo());
                    renglon.createCell(3).setCellValue(activo.getDescripcion());
                    renglon.createCell(4).setCellValue(sdf.format(activo.getFechaCompra()));
                    Cell cell = renglon.createCell(5);
                    cell.setCellValue(cal1.getTime());
                    cell.setCellStyle(cellStyle);
                } else if (cal1.get(Calendar.YEAR) >= 2020) {
                    log.debug("Pasando al año 1900 {} - {}", activo.getDescripcion(), activo.getFechaCompra());
                    cal1.add(Calendar.YEAR, -100);
                    update.setDate("fechaCompra", cal1.getTime());
                    update.setLong("id", activo.getId());
                    update.executeUpdate();
                    XSSFRow renglon = fechas2.createRow(fechas2Row++);
                    renglon.createCell(0).setCellValue(activo.getTipoActivoCuenta());
                    renglon.createCell(1).setCellValue(activo.getCentroCostoCuenta());
                    renglon.createCell(2).setCellValue(activo.getCodigo());
                    renglon.createCell(3).setCellValue(activo.getDescripcion());
                    renglon.createCell(4).setCellValue(sdf.format(activo.getFechaCompra()));
                    Cell cell = renglon.createCell(5);
                    cell.setCellValue(cal1.getTime());
                    cell.setCellStyle(cellStyle);
                }
                cont++;
            }
            currentSession().flush();

            tx.commit();
            log.debug("Termino actualizando {} de {} en {}", new Object[]{cont, activos.size(), ((new Date().getTime() - inicio.getTime()) / 1000)});
            wb.write(out);
        } catch (Exception e) {
            log.error("No se pudieron arreglar las fechas de los activos", e);
            tx.rollback();
            throw new RuntimeException("No se pudieron arreglar las fechas de los actios", e);
        }
    }

    @Override
    public String baja(BajaActivo bajaActivo, Usuario usuario) {
        log.debug("Dando de baja a {}", bajaActivo);
        Date fecha = new Date();
        Activo activo = bajaActivo.getActivo();
        currentSession().refresh(activo);
        if (usuario != null) {
            bajaActivo.setCreador(usuario.getUsername());
        } else {
            bajaActivo.setCreador("sistema");
        }
        bajaActivo.setFechaCreacion(fecha);
        currentSession().save(bajaActivo);
        activo.setInactivo(Boolean.TRUE);
        activo.setFechaInactivo(bajaActivo.getFecha());
        activo.setFechaModificacion(fecha);
        currentSession().update(activo);
        audita(activo, usuario, Constantes.BAJA, fecha);
        currentSession().flush();
        return activo.getFolio();
    }

    @Override
    public Activo carga(Long id) {
        return (Activo) currentSession().load(Activo.class, id);
    }

    private void audita(Activo activo, Usuario usuario, String actividad, Date fecha) {
        XActivo xactivo = new XActivo();
        BeanUtils.copyProperties(activo, xactivo, new String[]{"id", "version"});
        xactivo.setActivoId(activo.getId());
        xactivo.setEmpresaId(activo.getEmpresa().getId());
        xactivo.setProveedorId(activo.getProveedor().getId());
        xactivo.setTipoActivoId(activo.getTipoActivo().getId());
        xactivo.setEjercicioId(activo.getCentroCosto().getId().getEjercicio().getId().getIdEjercicio());
        xactivo.setOrganizacionId(activo.getEmpresa().getOrganizacion().getId());
        xactivo.setIdCosto(activo.getCentroCosto().getId().getIdCosto());
        xactivo.setFechaCreacion(fecha);
        xactivo.setActividad(actividad);
        xactivo.setCreador((usuario != null) ? usuario.getUsername() : "sistema");
        log.debug("Depreciacion fecha: {} - {}", activo.getFechaCompra(), xactivo.getFechaCompra());
        currentSession().save(xactivo);
    }

    @Override
    public List<CentroCosto> centrosDeCosto(Usuario usuario) {
        Criteria criteria = currentSession().createCriteria(CentroCosto.class);
        criteria.add(Restrictions.eq("id.ejercicio.id.organizacion.id", usuario.getEjercicio().getId().getOrganizacion().getId()));
        criteria.add(Restrictions.eq("id.ejercicio.id.idEjercicio", usuario.getEjercicio().getId().getIdEjercicio()));
        criteria.addOrder(Order.asc("id.idCosto"));
        return criteria.list();
    }

    @Override
    public void subeImagen(Activo activo, Usuario usuario) {
        log.debug("Subiendo imagen");
        Date fecha = new Date();
        activo.setFechaModificacion(fecha);
        currentSession().update(activo);
        this.audita(activo, usuario, Constantes.IMAGEN, fecha);
        currentSession().flush();
    }

    @Override
    public String reubica(ReubicacionActivo reubicacion, Usuario usuario) {
        log.debug("Reubicando activo {}", reubicacion.getActivo());
        Date fecha = new Date();

        CCostoPK pk = new CCostoPK(usuario.getEjercicio(), reubicacion.getCentroCosto().getId().getIdCosto());
        CentroCosto centroCosto = (CentroCosto) currentSession().load(CentroCosto.class, pk);

        Activo activo = reubicacion.getActivo();
        currentSession().refresh(activo);
        activo.setCentroCosto(centroCosto);
        activo.setFechaModificacion(fecha);
        activo.setFechaReubicado(reubicacion.getFecha());
        currentSession().update(activo);

        reubicacion.setCentroCosto(centroCosto);
        reubicacion.setFechaCreacion(fecha);
        reubicacion.setCreador(usuario.getUsername());
        reubicacion.setEmpresa(usuario.getEmpresa());
        currentSession().save(reubicacion);

        this.audita(activo, usuario, Constantes.REUBICACION, fecha);

        currentSession().flush();

        return activo.getFolio();
    }

    @Override
    public void sube(byte[] datos, Usuario usuario, OutputStream out, Integer codigoInicial) {
        Date inicio = new Date();
        int idx = 5;
        int i = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yy");

        MathContext mc = new MathContext(16, RoundingMode.HALF_UP);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(0);
        nf.setMinimumIntegerDigits(5);

        Transaction tx = null;
        try {
            String ejercicioId = "001-2012";
            Map<String, CentroCosto> centrosDeCosto = new HashMap<>();
            Map<String, TipoActivo> tipos = new HashMap<>();
            Query tipoActivoQuery = currentSession().createQuery("select ta from TipoActivo ta "
                    + "where ta.empresa.id = :empresaId "
                    + "and ta.cuenta.id.ejercicio.id.idEjercicio = :ejercicioId "
                    + "and ta.cuenta.id.ejercicio.id.organizacion.id = :organizacionId");
            tipoActivoQuery.setLong("empresaId", usuario.getEmpresa().getId());
            tipoActivoQuery.setString("ejercicioId", ejercicioId);
            tipoActivoQuery.setLong("organizacionId", usuario.getEmpresa().getOrganizacion().getId());
            List<TipoActivo> listaTipos = tipoActivoQuery.list();
            for (TipoActivo tipoActivo : listaTipos) {
                tipos.put(tipoActivo.getCuenta().getId().getIdCtaMayor(), tipoActivo);
            }

            Query proveedorQuery = currentSession().createQuery("select p from Proveedor p where p.empresa.id = :empresaId and p.nombre = :nombreEmpresa");
            proveedorQuery.setLong("empresaId", usuario.getEmpresa().getId());
            proveedorQuery.setString("nombreEmpresa", usuario.getEmpresa().getNombre());
            Proveedor proveedor = (Proveedor) proveedorQuery.uniqueResult();

            Query codigoDuplicadoQuery = currentSession().createQuery("select a from Activo a where a.empresa.id = :empresaId and a.codigo = :codigo");

            XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(datos));
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet ccostoFantasma = wb.createSheet("CCOSTO-FANTASMAS");
            int ccostoFantasmaRow = 0;
            XSSFSheet sinCCosto = wb.createSheet("SIN-CCOSTO");
            int sinCCostoRow = 0;
            XSSFSheet codigoAsignado = wb.createSheet("CODIGO-ASIGNADO");
            int codigoAsignadoRow = 0;
            XSSFSheet fechaInvalida = wb.createSheet("FECHA-INVALIDA");
            int fechaInvalidaRow = 0;
            XSSFSheet sinCosto = wb.createSheet("SIN-COSTO");
            int sinCostoRow = 0;

            tx = currentSession().beginTransaction();
            for (idx = 0; idx <= 5; idx++) {
                XSSFSheet sheet = workbook.getSheetAt(idx);

                int rows = sheet.getPhysicalNumberOfRows();
                for (i = 2; i < rows; i++) {
                    log.debug("Leyendo pagina {} renglon {}", idx, i);
                    XSSFRow row = sheet.getRow(i);
                    if (row.getCell(0) == null) {
                        break;
                    }
                    String nombreGrupo = row.getCell(0).getStringCellValue().trim();
                    TipoActivo tipoActivo = tipos.get(nombreGrupo);
                    if (tipoActivo != null) {
                        String cuentaCCosto = row.getCell(2).toString().trim();
                        if (StringUtils.isNotBlank(cuentaCCosto)) {
                            CentroCosto centroCosto = centrosDeCosto.get(cuentaCCosto);
                            if (centroCosto == null) {
                                Query ccostoQuery = currentSession().createQuery("select cc from CentroCosto cc "
                                        + "where cc.id.ejercicio.id.idEjercicio = :ejercicioId "
                                        + "and cc.id.ejercicio.id.organizacion.id = :organizacionId "
                                        + "and cc.id.idCosto like :idCosto");
                                ccostoQuery.setString("ejercicioId", ejercicioId);
                                ccostoQuery.setLong("organizacionId", usuario.getEmpresa().getOrganizacion().getId());
                                ccostoQuery.setString("idCosto", "%" + cuentaCCosto);
                                ccostoQuery.setMaxResults(1);
                                List<CentroCosto> listaCCosto = ccostoQuery.list();
                                if (listaCCosto != null && listaCCosto.size() > 0) {
                                    centroCosto = listaCCosto.get(0);
                                }
                                if (centroCosto == null) {
                                    XSSFRow renglon = ccostoFantasma.createRow(ccostoFantasmaRow++);
                                    renglon.createCell(0).setCellValue(sheet.getSheetName() + ":" + (i + 1));
                                    renglon.createCell(1).setCellValue(row.getCell(0).toString());
                                    renglon.createCell(2).setCellValue(row.getCell(1).toString());
                                    renglon.createCell(3).setCellValue(row.getCell(2).toString());
                                    renglon.createCell(4).setCellValue(row.getCell(3).toString());
                                    renglon.createCell(5).setCellValue(row.getCell(4).toString());
                                    renglon.createCell(6).setCellValue(row.getCell(5).toString());
                                    renglon.createCell(7).setCellValue(row.getCell(6).toString());
                                    renglon.createCell(8).setCellValue(row.getCell(7).toString());
                                    renglon.createCell(9).setCellValue(row.getCell(8).toString());
                                    renglon.createCell(10).setCellValue(row.getCell(9).toString());
                                    renglon.createCell(11).setCellValue(row.getCell(10).toString());
                                    renglon.createCell(12).setCellValue(row.getCell(11).toString());
                                    renglon.createCell(13).setCellValue(row.getCell(12).toString());
                                    renglon.createCell(14).setCellValue(row.getCell(13).toString());
                                    renglon.createCell(15).setCellValue(row.getCell(14).toString());
                                    renglon.createCell(16).setCellValue(row.getCell(15).toString());
                                    continue;
                                }
                                centrosDeCosto.put(cuentaCCosto, centroCosto);
                            }
                            String poliza = null;
                            switch (row.getCell(4).getCellType()) {
                                case XSSFCell.CELL_TYPE_NUMERIC:
                                    poliza = row.getCell(4).toString();
                                    poliza = StringUtils.removeEnd(poliza, ".0");
                                    log.debug("POLIZA-N: {}", poliza);
                                    break;
                                case XSSFCell.CELL_TYPE_STRING:
                                    poliza = row.getCell(4).getStringCellValue().trim();
                                    log.debug("POLIZA-S: {}", poliza);
                                    break;
                            }
                            Boolean seguro = false;
                            if (row.getCell(5) != null && StringUtils.isNotBlank(row.getCell(5).toString())) {
                                seguro = true;
                            }
                            Boolean garantia = false;
                            if (row.getCell(6) != null && StringUtils.isNotBlank(row.getCell(6).toString())) {
                                garantia = true;
                            }
                            Date fechaCompra = null;
                            if (row.getCell(7) != null) {
                                log.debug("VALIDANDO FECHA");
                                XSSFCell cell = row.getCell(7);
                                switch (cell.getCellType()) {
                                    case Cell.CELL_TYPE_NUMERIC:
                                        log.debug("ES NUMERIC");
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            log.debug("ES FECHA");
                                            fechaCompra = cell.getDateCellValue();
                                        } else if (DateUtil.isCellInternalDateFormatted(cell)) {
                                            log.debug("ES FECHA INTERNAL");
                                            fechaCompra = cell.getDateCellValue();
                                        } else {
                                            BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                                            bd = stripTrailingZeros(bd);

                                            log.debug("CONVIRTIENDO DOUBLE {} - {}", DateUtil.isValidExcelDate(bd.doubleValue()), bd);
                                            fechaCompra = HSSFDateUtil.getJavaDate(bd.longValue(), true);
                                            log.debug("Cal: {}", fechaCompra);
                                        }
                                        break;
                                    case Cell.CELL_TYPE_FORMULA:
                                        log.debug("ES FORMULA");
                                        CellValue cellValue = evaluator.evaluate(cell);
                                        switch (cellValue.getCellType()) {
                                            case Cell.CELL_TYPE_NUMERIC:
                                                if (DateUtil.isCellDateFormatted(cell)) {
                                                    fechaCompra = DateUtil.getJavaDate(cellValue.getNumberValue(), true);
                                                }
                                        }
                                }
                            }
                            if (row.getCell(7) != null && fechaCompra == null) {
                                String fechaCompraString;
                                if (row.getCell(7).getCellType() == Cell.CELL_TYPE_STRING) {
                                    fechaCompraString = row.getCell(7).getStringCellValue();
                                } else {
                                    fechaCompraString = row.getCell(7).toString().trim();
                                }
                                try {
                                    fechaCompra = sdf.parse(fechaCompraString);
                                } catch (ParseException e) {
                                    try {
                                        fechaCompra = sdf2.parse(fechaCompraString);
                                    } catch (ParseException e2) {
                                        try {
                                            fechaCompra = sdf3.parse(fechaCompraString);
                                        } catch (ParseException e3) {
                                            // no se pudo convertir
                                        }
                                    }
                                }
                            }

                            if (fechaCompra == null) {
                                XSSFRow renglon = fechaInvalida.createRow(fechaInvalidaRow++);
                                renglon.createCell(0).setCellValue(sheet.getSheetName() + ":" + (i + 1));
                                renglon.createCell(1).setCellValue(row.getCell(0).toString());
                                renglon.createCell(2).setCellValue(row.getCell(1).toString());
                                renglon.createCell(3).setCellValue(row.getCell(2).toString());
                                renglon.createCell(4).setCellValue(row.getCell(3).toString());
                                renglon.createCell(5).setCellValue(row.getCell(4).toString());
                                renglon.createCell(6).setCellValue(row.getCell(5).toString());
                                renglon.createCell(7).setCellValue(row.getCell(6).toString());
                                renglon.createCell(8).setCellValue(row.getCell(7).toString());
                                renglon.createCell(9).setCellValue(row.getCell(8).toString());
                                renglon.createCell(10).setCellValue(row.getCell(9).toString());
                                renglon.createCell(11).setCellValue(row.getCell(10).toString());
                                renglon.createCell(12).setCellValue(row.getCell(11).toString());
                                renglon.createCell(13).setCellValue(row.getCell(12).toString());
                                renglon.createCell(14).setCellValue(row.getCell(13).toString());
                                renglon.createCell(15).setCellValue(row.getCell(14).toString());
                                renglon.createCell(16).setCellValue(row.getCell(15).toString());
                                continue;
                            }

                            String codigo = null;
                            switch (row.getCell(8).getCellType()) {
                                case XSSFCell.CELL_TYPE_NUMERIC:
                                    codigo = row.getCell(8).toString();
                                    break;
                                case XSSFCell.CELL_TYPE_STRING:
                                    codigo = row.getCell(8).getStringCellValue().trim();
                                    break;
                            }
                            if (StringUtils.isBlank(codigo)) {
                                codigo = nf.format(codigoInicial);
                                XSSFRow renglon = codigoAsignado.createRow(codigoAsignadoRow++);
                                renglon.createCell(0).setCellValue(sheet.getSheetName() + ":" + (i + 1));
                                renglon.createCell(1).setCellValue(row.getCell(0).toString());
                                renglon.createCell(2).setCellValue(row.getCell(1).toString());
                                renglon.createCell(3).setCellValue(row.getCell(2).toString());
                                renglon.createCell(4).setCellValue(row.getCell(3).toString());
                                renglon.createCell(5).setCellValue(row.getCell(4).toString());
                                renglon.createCell(6).setCellValue(row.getCell(5).toString());
                                renglon.createCell(7).setCellValue(row.getCell(6).toString());
                                renglon.createCell(8).setCellValue(row.getCell(7).toString());
                                renglon.createCell(9).setCellValue(codigoInicial);
                                renglon.createCell(10).setCellValue(row.getCell(9).toString());
                                renglon.createCell(11).setCellValue(row.getCell(10).toString());
                                renglon.createCell(12).setCellValue(row.getCell(11).toString());
                                renglon.createCell(13).setCellValue(row.getCell(12).toString());
                                renglon.createCell(14).setCellValue(row.getCell(13).toString());
                                renglon.createCell(15).setCellValue(row.getCell(14).toString());
                                renglon.createCell(16).setCellValue(row.getCell(15).toString());
                                codigoInicial++;
                            } else {
                                // busca codigo duplicado
                                if (codigo.contains(".")) {
                                    codigo = codigo.substring(0, codigo.lastIndexOf("."));
                                    log.debug("CODIGO: {}", codigo);
                                }

                                codigoDuplicadoQuery.setLong("empresaId", usuario.getEmpresa().getId());
                                codigoDuplicadoQuery.setString("codigo", codigo);
                                Activo activo = (Activo) codigoDuplicadoQuery.uniqueResult();
                                if (activo != null) {
                                    XSSFRow renglon = codigoAsignado.createRow(codigoAsignadoRow++);
                                    renglon.createCell(0).setCellValue(sheet.getSheetName() + ":" + (i + 1));
                                    renglon.createCell(1).setCellValue(row.getCell(0).toString());
                                    renglon.createCell(2).setCellValue(row.getCell(1).toString());
                                    renglon.createCell(3).setCellValue(row.getCell(2).toString());
                                    renglon.createCell(4).setCellValue(row.getCell(3).toString());
                                    renglon.createCell(5).setCellValue(row.getCell(4).toString());
                                    renglon.createCell(6).setCellValue(row.getCell(5).toString());
                                    renglon.createCell(7).setCellValue(row.getCell(6).toString());
                                    renglon.createCell(8).setCellValue(row.getCell(7).toString());
                                    renglon.createCell(9).setCellValue(codigo + "-" + nf.format(codigoInicial));
                                    renglon.createCell(10).setCellValue(row.getCell(9).toString());
                                    renglon.createCell(11).setCellValue(row.getCell(10).toString());
                                    renglon.createCell(12).setCellValue(row.getCell(11).toString());
                                    renglon.createCell(13).setCellValue(row.getCell(12).toString());
                                    renglon.createCell(14).setCellValue(row.getCell(13).toString());
                                    renglon.createCell(15).setCellValue(row.getCell(14).toString());
                                    renglon.createCell(16).setCellValue(row.getCell(15).toString());
                                    codigo = nf.format(codigoInicial);
                                    codigoInicial++;
                                }
                            }
                            String descripcion = null;
                            if (row.getCell(9) != null) {
                                switch (row.getCell(9).getCellType()) {
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        descripcion = row.getCell(9).toString();
                                        descripcion = StringUtils.removeEnd(descripcion, ".0");
                                        break;
                                    case XSSFCell.CELL_TYPE_STRING:
                                        descripcion = row.getCell(9).getStringCellValue().trim();
                                        break;
                                    default:
                                        descripcion = row.getCell(9).toString().trim();
                                }
                            }
                            String marca = null;
                            if (row.getCell(10) != null) {
                                switch (row.getCell(10).getCellType()) {
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        marca = row.getCell(10).toString();
                                        marca = StringUtils.removeEnd(marca, ".0");
                                        break;
                                    case XSSFCell.CELL_TYPE_STRING:
                                        marca = row.getCell(10).getStringCellValue().trim();
                                        break;
                                    default:
                                        marca = row.getCell(10).toString().trim();
                                }
                            }
                            String modelo = null;
                            if (row.getCell(11) != null) {
                                switch (row.getCell(11).getCellType()) {
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        modelo = row.getCell(11).toString();
                                        modelo = StringUtils.removeEnd(modelo, ".0");
                                        break;
                                    case XSSFCell.CELL_TYPE_STRING:
                                        modelo = row.getCell(11).getStringCellValue().trim();
                                        break;
                                    default:
                                        modelo = row.getCell(11).toString().trim();
                                }
                            }
                            String serie = null;
                            if (row.getCell(12) != null) {
                                switch (row.getCell(12).getCellType()) {
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        serie = row.getCell(12).toString();
                                        serie = StringUtils.removeEnd(serie, ".0");
                                        break;
                                    case XSSFCell.CELL_TYPE_STRING:
                                        serie = row.getCell(12).getStringCellValue().trim();
                                        break;
                                    default:
                                        serie = row.getCell(12).toString().trim();
                                }
                            }
                            String responsable = null;
                            if (row.getCell(13) != null) {
                                switch (row.getCell(13).getCellType()) {
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        responsable = row.getCell(13).toString();
                                        responsable = StringUtils.removeEnd(responsable, ".0");
                                        break;
                                    case XSSFCell.CELL_TYPE_STRING:
                                        responsable = row.getCell(13).getStringCellValue().trim();
                                        break;
                                    default:
                                        responsable = row.getCell(13).toString().trim();
                                }
                            }

                            String ubicacion = null;
                            if (row.getCell(14) != null) {
                                switch (row.getCell(14).getCellType()) {
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        ubicacion = row.getCell(14).toString();
                                        ubicacion = StringUtils.removeEnd(ubicacion, ".0");
                                        break;
                                    case XSSFCell.CELL_TYPE_STRING:
                                        ubicacion = row.getCell(14).getStringCellValue().trim();
                                        break;
                                    default:
                                        ubicacion = row.getCell(14).toString().trim();
                                }
                            }
                            BigDecimal costo = null;
                            switch (row.getCell(15).getCellType()) {
                                case XSSFCell.CELL_TYPE_NUMERIC:
                                    costo = new BigDecimal(row.getCell(15).getNumericCellValue(), mc);
                                    log.debug("COSTO-N: {} - {}", costo, row.getCell(15).getNumericCellValue());
                                    break;
                                case XSSFCell.CELL_TYPE_STRING:
                                    costo = new BigDecimal(row.getCell(15).toString(), mc);
                                    log.debug("COSTO-S: {} - {}", costo, row.getCell(15).toString());
                                    break;
                                case XSSFCell.CELL_TYPE_FORMULA:
                                    costo = new BigDecimal(evaluator.evaluateInCell(row.getCell(15)).getNumericCellValue(), mc);
                                    log.debug("COSTO-F: {}", costo);
                            }
                            if (costo == null) {
                                XSSFRow renglon = sinCosto.createRow(sinCostoRow++);
                                renglon.createCell(0).setCellValue(sheet.getSheetName() + ":" + (i + 1));
                                renglon.createCell(1).setCellValue(row.getCell(0).toString());
                                renglon.createCell(2).setCellValue(row.getCell(1).toString());
                                renglon.createCell(3).setCellValue(row.getCell(2).toString());
                                renglon.createCell(4).setCellValue(row.getCell(3).toString());
                                renglon.createCell(5).setCellValue(row.getCell(4).toString());
                                renglon.createCell(6).setCellValue(row.getCell(5).toString());
                                renglon.createCell(7).setCellValue(row.getCell(6).toString());
                                renglon.createCell(8).setCellValue(row.getCell(7).toString());
                                renglon.createCell(9).setCellValue(row.getCell(8).toString());
                                renglon.createCell(10).setCellValue(row.getCell(9).toString());
                                renglon.createCell(11).setCellValue(row.getCell(10).toString());
                                renglon.createCell(12).setCellValue(row.getCell(11).toString());
                                renglon.createCell(13).setCellValue(row.getCell(12).toString());
                                renglon.createCell(14).setCellValue(row.getCell(13).toString());
                                renglon.createCell(15).setCellValue(row.getCell(14).toString());
                                renglon.createCell(16).setCellValue(row.getCell(15).toString());
                                continue;
                            }

                            Activo activo = new Activo(fechaCompra, seguro, garantia, poliza, codigo, descripcion, marca, modelo, serie, responsable, ubicacion, costo, tipoActivo, centroCosto, proveedor, usuario.getEmpresa());
                            this.crea(activo, usuario);

                        } else {
                            XSSFRow renglon = sinCCosto.createRow(sinCCostoRow++);
                            renglon.createCell(0).setCellValue(sheet.getSheetName() + ":" + (i + 1));
                            renglon.createCell(1).setCellValue(row.getCell(0).toString());
                            renglon.createCell(2).setCellValue(row.getCell(1).toString());
                            renglon.createCell(3).setCellValue(row.getCell(2).toString());
                            renglon.createCell(4).setCellValue(row.getCell(3).toString());
                            renglon.createCell(5).setCellValue(row.getCell(4).toString());
                            renglon.createCell(6).setCellValue(row.getCell(5).toString());
                            renglon.createCell(7).setCellValue(row.getCell(6).toString());
                            renglon.createCell(8).setCellValue(row.getCell(7).toString());
                            renglon.createCell(9).setCellValue(row.getCell(8).toString());
                            renglon.createCell(10).setCellValue(row.getCell(9).toString());
                            renglon.createCell(11).setCellValue(row.getCell(10).toString());
                            renglon.createCell(12).setCellValue(row.getCell(11).toString());
                            renglon.createCell(13).setCellValue(row.getCell(12).toString());
                            renglon.createCell(14).setCellValue(row.getCell(13).toString());
                            renglon.createCell(15).setCellValue(row.getCell(14).toString());
                            renglon.createCell(16).setCellValue(row.getCell(15).toString());
                            continue;
                        }
                    } else {
                        throw new RuntimeException("(" + idx + ":" + i + ") No se encontro el tipo de activo " + nombreGrupo);
                    }
                }
            }
            tx.commit();
            log.debug("################################################");
            log.debug("################################################");
            log.debug("TERMINO EN {} MINS", (new Date().getTime() - inicio.getTime()) / (1000 * 60));
            log.debug("################################################");
            log.debug("################################################");

            wb.write(out);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            log.error("Hubo problemas al intentar pasar datos de archivo excel a BD (" + idx + ":" + i + ")", e);
            throw new RuntimeException("Hubo problemas al intentar pasar datos de archivo excel a BD (" + idx + ":" + i + ")", e);
        }
    }

    private BigDecimal stripTrailingZeros(BigDecimal value) {
        if (value.scale() <= 0) {
            return value;
        }

        String valueAsString = String.valueOf(value);
        int idx = valueAsString.indexOf(".");
        if (idx == -1) {
            return value;
        }

        for (int i = valueAsString.length() - 1; i > idx; i--) {
            if (valueAsString.charAt(i) == '0') {
                valueAsString = valueAsString.substring(0, i);
            } else if (valueAsString.charAt(i) == '.') {
                valueAsString = valueAsString.substring(0, i);
                // Stop when decimal point is reached
                break;
            } else {
                break;
            }
        }
        BigDecimal result = new BigDecimal(valueAsString);
        return result;
    }

    @Override
    public Map<String, Object> depreciacionAcumuladaPorCentroDeCosto(Map<String, Object> params) {
        Usuario usuario = (Usuario) params.get("usuario");
        Query tiposDeActivoQuery = currentSession().createQuery("select new map(ta.nombre as nombre, ta.cuenta.id.idCtaMayor as cuenta, ta.id as id) from TipoActivo ta where ta.cuenta.id.ejercicio.id.idEjercicio = :ejercicioId and ta.cuenta.id.ejercicio.id.organizacion.id = :organizacionId order by ta.cuenta.id.idCtaMayor");
        tiposDeActivoQuery.setString("ejercicioId", usuario.getEjercicio().getId().getIdEjercicio());
        tiposDeActivoQuery.setLong("organizacionId", usuario.getEjercicio().getId().getOrganizacion().getId());
        List<Map<String, Object>> tiposDeActivo = tiposDeActivoQuery.list();
        Map<String, Map<String, Object>> tiposDeActivoMap = new HashMap<>();
        for (Map<String, Object> tipoActivo : tiposDeActivo) {
            tipoActivo.put("total", BigDecimal.ZERO);
            tiposDeActivoMap.put((String) tipoActivo.get("cuenta"), tipoActivo);
        }
        Map<String, Object> totalCC = new HashMap<>();
        totalCC.put("nombre", "");
        totalCC.put("cuenta", "TOTAL");
        totalCC.put("id", 0);
        totalCC.put("total", BigDecimal.ZERO);
        tiposDeActivo.add(totalCC);
        tiposDeActivoMap.put("TOTAL", totalCC);
        params.put("tiposDeActivo", tiposDeActivo);

        Date fecha = (Date) params.get("fecha");

        MathContext mc = new MathContext(16, RoundingMode.HALF_UP);
        Map<String, Map<String, Object>> mapa1 = new TreeMap<>();
        Query query = currentSession().createQuery("select new Activo(a.id, a.version, a.moi, a.fechaCompra, a.tipoActivo.porciento, a.tipoActivo.vidaUtil, a.inactivo, a.fechaInactivo, a.fechaReubicado, a.tipoActivo.cuenta.id.idCtaMayor, a.centroCosto.id.idCosto, a.centroCosto.nombre) from Activo a inner join a.tipoActivo where a.empresa.id = :empresaId and a.fechaCompra <= :fecha");
        query.setLong("empresaId", usuario.getEmpresa().getId());
        query.setDate("fecha", fecha);
        List<Activo> activos = query.list();
        for (Activo activo : activos) {
            log.trace("Depreciando activo {}", activo.getId());
            activo = this.deprecia(activo, fecha);

            Map<String, Object> mapa2 = mapa1.get(activo.getCentroCostoCuenta());
            Map<String, BigDecimal> mapa3;
            if (mapa2 == null) {
                mapa2 = new HashMap<>();
                mapa3 = new HashMap<>();
                for (Map<String, Object> tipoActivo : tiposDeActivo) {
                    mapa3.put((String) tipoActivo.get("cuenta"), BigDecimal.ZERO);
                }
                mapa3.put("TOTAL", BigDecimal.ZERO);
                mapa2.put("totales", mapa3);
                mapa2.put("cuenta", activo.getCentroCostoCuenta());
                mapa2.put("nombre", activo.getCentroCostoNombre());
                mapa1.put(activo.getCentroCostoCuenta(), mapa2);
            }

            mapa3 = (Map<String, BigDecimal>) mapa2.get("totales");

            BigDecimal cantidad = mapa3.get(activo.getTipoActivoCuenta());
            cantidad = cantidad.add(activo.getDepreciacionAcumulada(), mc);
            mapa3.put(activo.getTipoActivoCuenta(), cantidad);

            Map<String, Object> tipoActivo = (Map<String, Object>) tiposDeActivoMap.get(activo.getTipoActivoCuenta());
            BigDecimal total = (BigDecimal) tipoActivo.get("total");
            total = total.add(activo.getDepreciacionAcumulada(), mc);
            tipoActivo.put("total", total);

            // Totales
            cantidad = mapa3.get("TOTAL");
            cantidad = cantidad.add(activo.getDepreciacionAcumulada(), mc);
            mapa3.put("TOTAL", cantidad);

            tipoActivo = (Map<String, Object>) tiposDeActivoMap.get("TOTAL");
            total = (BigDecimal) tipoActivo.get("total");
            total = total.add(activo.getDepreciacionAcumulada(), mc);
            tipoActivo.put("total", total);

        }

        if (log.isTraceEnabled()) {
            for (Map<String, Object> centroCosto : mapa1.values()) {
                log.trace("CentroCosto: {} : {}", centroCosto.get("nombre"), centroCosto.get("totales"));
            }
        }

        params.put("centrosDeCosto", mapa1.values());
        return params;
    }

    @Override
    public Map<String, Object> depreciacionAcumuladaPorCentroDeCostoDetalle(Map<String, Object> params) {
        MathContext mc = new MathContext(16, RoundingMode.HALF_UP);
        Map<String, BigDecimal> totales = new HashMap<>();
        totales.put("ACUMULADA", BigDecimal.ZERO);
        totales.put("MENSUAL", BigDecimal.ZERO);
        totales.put("ANUAL", BigDecimal.ZERO);
        totales.put("NETO", BigDecimal.ZERO);
        Usuario usuario = (Usuario) params.get("usuario");
        String centroCostoId = (String) params.get("centroCostoId");
        Date fecha = (Date) params.get("fecha");
        CCostoPK ccostoPK = new CCostoPK(usuario.getEjercicio(), centroCostoId);
        CentroCosto centroCosto = (CentroCosto) currentSession().get(CentroCosto.class, ccostoPK);
        params.put("centroCosto", centroCosto);
        Criteria criteria = currentSession().createCriteria(Activo.class);
        criteria.add(Restrictions.eq("empresa.id", usuario.getEmpresa().getId()));
        criteria.add(Restrictions.eq("centroCosto.id.ejercicio.id.idEjercicio", usuario.getEjercicio().getId().getIdEjercicio()));
        criteria.add(Restrictions.eq("centroCosto.id.ejercicio.id.organizacion.id", usuario.getEjercicio().getId().getOrganizacion().getId()));
        criteria.add(Restrictions.eq("centroCosto.id.idCosto", centroCostoId));
        criteria.add(Restrictions.le("fechaCompra", fecha));
        List<Activo> activos = criteria.list();
        for (Activo activo : activos) {
            log.trace("Depreciando activo {}", activo.getId());
            activo = this.deprecia(activo, fecha);

            BigDecimal total = totales.get("ACUMULADA");
            total = total.add(activo.getDepreciacionAcumulada(), mc);
            totales.put("ACUMULADA", total);

            total = totales.get("MENSUAL");
            total = total.add(activo.getDepreciacionMensual(), mc);
            totales.put("MENSUAL", total);

            total = totales.get("ANUAL");
            total = total.add(activo.getDepreciacionAnual(), mc);
            totales.put("ANUAL", total);

            total = totales.get("NETO");
            total = total.add(activo.getValorNeto(), mc);
            totales.put("NETO", total);
        }
        params.put("activos", activos);
        params.put("totales", totales);
        return params;
    }

    @Override
    public Map<String, Object> depreciacionMensualPorCentroDeCosto(Map<String, Object> params) {
        Usuario usuario = (Usuario) params.get("usuario");
        Query tiposDeActivoQuery = currentSession().createQuery("select new map(ta.nombre as nombre, ta.cuenta.id.idCtaMayor as cuenta, ta.id as id) from TipoActivo ta where ta.cuenta.id.ejercicio.id.idEjercicio = :ejercicioId and ta.cuenta.id.ejercicio.id.organizacion.id = :organizacionId order by ta.cuenta.id.idCtaMayor");
        tiposDeActivoQuery.setString("ejercicioId", usuario.getEjercicio().getId().getIdEjercicio());
        tiposDeActivoQuery.setLong("organizacionId", usuario.getEjercicio().getId().getOrganizacion().getId());
        List<Map<String, Object>> tiposDeActivo = tiposDeActivoQuery.list();
        Map<String, Map<String, Object>> tiposDeActivoMap = new HashMap<>();
        for (Map<String, Object> tipoActivo : tiposDeActivo) {
            tipoActivo.put("total", BigDecimal.ZERO);
            tiposDeActivoMap.put((String) tipoActivo.get("cuenta"), tipoActivo);
        }
        Map<String, Object> totalCC = new HashMap<>();
        totalCC.put("nombre", "");
        totalCC.put("cuenta", "TOTAL");
        totalCC.put("id", 0);
        totalCC.put("total", BigDecimal.ZERO);
        tiposDeActivo.add(totalCC);
        tiposDeActivoMap.put("TOTAL", totalCC);
        params.put("tiposDeActivo", tiposDeActivo);

        Date fecha = (Date) params.get("fecha");

        MathContext mc = new MathContext(16, RoundingMode.HALF_UP);
        Map<String, Map<String, Object>> mapa1 = new TreeMap<>();
        Query query = currentSession().createQuery("select new Activo(a.id, a.version, a.moi, a.fechaCompra, a.tipoActivo.porciento, a.tipoActivo.vidaUtil, a.inactivo, a.fechaInactivo, a.fechaReubicado, a.tipoActivo.cuenta.id.idCtaMayor, a.centroCosto.id.idCosto, a.centroCosto.nombre) from Activo a inner join a.tipoActivo where a.empresa.id = :empresaId and a.fechaCompra <= :fecha");
        query.setLong("empresaId", usuario.getEmpresa().getId());
        query.setDate("fecha", fecha);
        List<Activo> activos = query.list();
        for (Activo activo : activos) {
            log.trace("Depreciando activo {}", activo.getId());
            activo = this.deprecia(activo, fecha);

            Map<String, Object> mapa2 = mapa1.get(activo.getCentroCostoCuenta());
            Map<String, BigDecimal> mapa3;
            if (mapa2 == null) {
                mapa2 = new HashMap<>();
                mapa3 = new HashMap<>();
                for (Map<String, Object> tipoActivo : tiposDeActivo) {
                    mapa3.put((String) tipoActivo.get("cuenta"), BigDecimal.ZERO);
                }
                mapa3.put("TOTAL", BigDecimal.ZERO);
                mapa2.put("totales", mapa3);
                mapa2.put("cuenta", activo.getCentroCostoCuenta());
                mapa2.put("nombre", activo.getCentroCostoNombre());
                mapa1.put(activo.getCentroCostoCuenta(), mapa2);
            }

            mapa3 = (Map<String, BigDecimal>) mapa2.get("totales");

            BigDecimal cantidad = mapa3.get(activo.getTipoActivoCuenta());
            cantidad = cantidad.add(activo.getDepreciacionMensual(), mc);
            mapa3.put(activo.getTipoActivoCuenta(), cantidad);

            Map<String, Object> tipoActivo = (Map<String, Object>) tiposDeActivoMap.get(activo.getTipoActivoCuenta());
            BigDecimal total = (BigDecimal) tipoActivo.get("total");
            total = total.add(activo.getDepreciacionMensual(), mc);
            tipoActivo.put("total", total);

            // Totales
            cantidad = mapa3.get("TOTAL");
            cantidad = cantidad.add(activo.getDepreciacionMensual(), mc);
            mapa3.put("TOTAL", cantidad);

            tipoActivo = (Map<String, Object>) tiposDeActivoMap.get("TOTAL");
            total = (BigDecimal) tipoActivo.get("total");
            total = total.add(activo.getDepreciacionMensual(), mc);
            tipoActivo.put("total", total);

        }

        if (log.isTraceEnabled()) {
            for (Map<String, Object> centroCosto : mapa1.values()) {
                log.trace("CentroCosto: {} : {}", centroCosto.get("nombre"), centroCosto.get("totales"));
            }
        }

        params.put("centrosDeCosto", mapa1.values());
        return params;
    }

    @Override
    public Map<String, Object> depreciacionMensualPorCentroDeCostoDetalle(Map<String, Object> params) {
        MathContext mc = new MathContext(16, RoundingMode.HALF_UP);
        Map<String, BigDecimal> totales = new HashMap<>();
        totales.put("ACUMULADA", BigDecimal.ZERO);
        totales.put("MENSUAL", BigDecimal.ZERO);
        totales.put("ANUAL", BigDecimal.ZERO);
        totales.put("NETO", BigDecimal.ZERO);
        Usuario usuario = (Usuario) params.get("usuario");
        String centroCostoId = (String) params.get("centroCostoId");
        Date fecha = (Date) params.get("fecha");
        CCostoPK ccostoPK = new CCostoPK(usuario.getEjercicio(), centroCostoId);
        CentroCosto centroCosto = (CentroCosto) currentSession().get(CentroCosto.class, ccostoPK);
        params.put("centroCosto", centroCosto);
        Criteria criteria = currentSession().createCriteria(Activo.class);
        criteria.add(Restrictions.eq("empresa.id", usuario.getEmpresa().getId()));
        criteria.add(Restrictions.eq("centroCosto.id.ejercicio.id.idEjercicio", usuario.getEjercicio().getId().getIdEjercicio()));
        criteria.add(Restrictions.eq("centroCosto.id.ejercicio.id.organizacion.id", usuario.getEjercicio().getId().getOrganizacion().getId()));
        criteria.add(Restrictions.eq("centroCosto.id.idCosto", centroCostoId));
        criteria.add(Restrictions.le("fechaCompra", fecha));
        List<Activo> activos = criteria.list();
        for (Activo activo : activos) {
            log.trace("Depreciando activo {}", activo.getId());
            activo = this.deprecia(activo, fecha);

            BigDecimal total = totales.get("ACUMULADA");
            total = total.add(activo.getDepreciacionAcumulada(), mc);
            totales.put("ACUMULADA", total);

            total = totales.get("MENSUAL");
            total = total.add(activo.getDepreciacionMensual(), mc);
            totales.put("MENSUAL", total);

            total = totales.get("ANUAL");
            total = total.add(activo.getDepreciacionAnual(), mc);
            totales.put("ANUAL", total);

            total = totales.get("NETO");
            total = total.add(activo.getValorNeto(), mc);
            totales.put("NETO", total);
        }
        params.put("activos", activos);
        params.put("totales", totales);
        return params;
    }

    @Override
    public Map<String, Object> depreciacionAcumuladaPorTipoActivo(Map<String, Object> params) {
        Usuario usuario = (Usuario) params.get("usuario");
        Query tiposDeActivoQuery = currentSession().createQuery("select new map(ta.nombre as nombre, ta.cuenta.id.idCtaMayor as cuenta, ta.id as id) from TipoActivo ta where ta.cuenta.id.ejercicio.id.idEjercicio = :ejercicioId and ta.cuenta.id.ejercicio.id.organizacion.id = :organizacionId order by ta.cuenta.id.idCtaMayor");
        tiposDeActivoQuery.setString("ejercicioId", usuario.getEjercicio().getId().getIdEjercicio());
        tiposDeActivoQuery.setLong("organizacionId", usuario.getEjercicio().getId().getOrganizacion().getId());
        List<Map<String, Object>> tiposDeActivo = tiposDeActivoQuery.list();
        Map<String, Map<String, Object>> tiposDeActivoMap = new HashMap<>();
        for (Map<String, Object> tipoActivo : tiposDeActivo) {
            tipoActivo.put("ACUMULADA", BigDecimal.ZERO);
            tipoActivo.put("MENSUAL", BigDecimal.ZERO);
            tiposDeActivoMap.put((String) tipoActivo.get("cuenta"), tipoActivo);
        }
        log.trace("TiposDeActivoMap: {}", tiposDeActivoMap);

        params.put("tiposDeActivo", tiposDeActivo);

        Map<String, BigDecimal> totales = new HashMap<>();
        totales.put("ACUMULADA", BigDecimal.ZERO);
        totales.put("MENSUAL", BigDecimal.ZERO);

        Date fecha = (Date) params.get("fecha");

        MathContext mc = new MathContext(16, RoundingMode.HALF_UP);
        Query query = currentSession().createQuery("select new Activo(a.id, a.version, a.moi, a.fechaCompra, a.tipoActivo.porciento, a.tipoActivo.vidaUtil, a.inactivo, a.fechaInactivo, a.fechaReubicado, a.tipoActivo.cuenta.id.idCtaMayor, a.centroCosto.id.idCosto, a.centroCosto.nombre) from Activo a inner join a.tipoActivo where a.empresa.id = :empresaId and a.fechaCompra <= :fecha");
        query.setLong("empresaId", usuario.getEmpresa().getId());
        query.setDate("fecha", fecha);
        List<Activo> activos = query.list();
        for (Activo activo : activos) {
            log.trace("Depreciando activo {}", activo.getId());
            activo = this.deprecia(activo, fecha);

            Map<String, Object> tipoActivo = (Map<String, Object>) tiposDeActivoMap.get(activo.getTipoActivoCuenta());
            BigDecimal acumulada = (BigDecimal) tipoActivo.get("ACUMULADA");
            acumulada = acumulada.add(activo.getDepreciacionAcumulada(), mc);
            tipoActivo.put("ACUMULADA", acumulada);

            BigDecimal totalAcumulada = (BigDecimal) totales.get("ACUMULADA");
            totalAcumulada = totalAcumulada.add(activo.getDepreciacionAcumulada(), mc);
            totales.put("ACUMULADA", totalAcumulada);

            BigDecimal mensual = (BigDecimal) tipoActivo.get("MENSUAL");
            mensual = mensual.add(activo.getDepreciacionMensual(), mc);
            tipoActivo.put("MENSUAL", mensual);

            BigDecimal totalMensual = (BigDecimal) totales.get("MENSUAL");
            totalMensual = totalMensual.add(activo.getDepreciacionMensual(), mc);
            totales.put("MENSUAL", totalMensual);
        }

        if (log.isTraceEnabled()) {
            for (Map<String, Object> tipoActivo : tiposDeActivo) {
                log.trace("TipoActivo: {} : {} : {}", new Object[]{tipoActivo.get("nombre"), tipoActivo.get("ACUMULADA"), tipoActivo.get("MENSUAL")});
            }

            log.trace("Totales: {}", totales);
        }

        params.put("totales", totales);

        return params;
    }

    @Override
    public Map<String, Object> depreciacionAcumuladaPorTipoActivoDetalle(Map<String, Object> params) {
        MathContext mc = new MathContext(16, RoundingMode.HALF_UP);
        Map<String, BigDecimal> totales = new HashMap<>();
        totales.put("ACUMULADA", BigDecimal.ZERO);
        totales.put("MENSUAL", BigDecimal.ZERO);
        totales.put("ANUAL", BigDecimal.ZERO);
        totales.put("NETO", BigDecimal.ZERO);
        Usuario usuario = (Usuario) params.get("usuario");
        String tipoActivoId = (String) params.get("tipoActivoId");
        Date fecha = (Date) params.get("fecha");
        Query query = currentSession().createQuery("select ta from TipoActivo ta where ta.cuenta.id.idCtaMayor = :tipoActivoId and ta.cuenta.id.ejercicio.id.idEjercicio = :ejercicioId and ta.cuenta.id.ejercicio.id.organizacion.id = :organizacionId");
        query.setString("tipoActivoId", tipoActivoId);
        query.setString("ejercicioId", usuario.getEjercicio().getId().getIdEjercicio());
        query.setLong("organizacionId", usuario.getEjercicio().getId().getOrganizacion().getId());
        TipoActivo tipoActivo = (TipoActivo) query.uniqueResult();
        if (tipoActivo != null) {
            params.put("tipoActivo", tipoActivo);
            Criteria criteria = currentSession().createCriteria(Activo.class);
            criteria.add(Restrictions.eq("empresa.id", usuario.getEmpresa().getId()));
            criteria.add(Restrictions.eq("tipoActivo.id", tipoActivo.getId()));
            criteria.add(Restrictions.le("fechaCompra", fecha));
            List<Activo> activos = criteria.list();
            for (Activo activo : activos) {
                log.trace("Depreciando activo {}", activo.getId());
                activo = this.deprecia(activo, fecha);

                BigDecimal total = totales.get("ACUMULADA");
                total = total.add(activo.getDepreciacionAcumulada(), mc);
                totales.put("ACUMULADA", total);

                total = totales.get("MENSUAL");
                total = total.add(activo.getDepreciacionMensual(), mc);
                totales.put("MENSUAL", total);

                total = totales.get("ANUAL");
                total = total.add(activo.getDepreciacionAnual(), mc);
                totales.put("ANUAL", total);

                total = totales.get("NETO");
                total = total.add(activo.getValorNeto(), mc);
                totales.put("NETO", total);
            }
            params.put("activos", activos);
            params.put("totales", totales);

        }
        return params;
    }

    @Override
    public Map<String, Object> reporteDIA(Integer anio, Usuario usuario) {
        Map<String, Object> resultado = new HashMap<>();
        Calendar fecha = Calendar.getInstance();
        fecha.setTimeInMillis(0);
        fecha.set(Calendar.DAY_OF_YEAR, 1);
        fecha.set(Calendar.YEAR, anio);
        fecha.set(Calendar.HOUR_OF_DAY, 0);

        Calendar fecha2 = Calendar.getInstance();
        fecha2.setTime(fecha.getTime());
        fecha2.add(Calendar.YEAR, 1);
        log.debug("Armando reporte dia de {} a {}", fecha, fecha2);

        MathContext mc = new MathContext(16, RoundingMode.HALF_UP);
        Map<String, Map<String, Object>> grupos = new TreeMap<>();
        BigDecimal totalCosto = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalCompras = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalBajas = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal costoFinal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalDepreciacionAcumulada = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalComprasAcumuladas = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalBajasAcumuladas = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalDepreciacionFinal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorNeto = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        Criteria criteria = currentSession().createCriteria(TipoActivo.class);
        criteria.add(Restrictions.eq("empresa.id", usuario.getEmpresa().getId()));
        List<TipoActivo> tiposDeActivo = criteria.list();
        for (TipoActivo tipoActivo : tiposDeActivo) {
            Map<String, Object> ta = new HashMap<>();
            ta.put("cuenta", tipoActivo.getCuenta().getId().getIdCtaMayor());
            ta.put("nombre", tipoActivo.getNombre());
            ta.put("costo", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            ta.put("compras", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            ta.put("bajas", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            ta.put("costoFinal", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            ta.put("depreciacionAcumulada", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            ta.put("comprasAcumuladas", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            ta.put("bajasAcumuladas", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            ta.put("depreciacionFinal", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            ta.put("valorNeto", BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            grupos.put(tipoActivo.getCuenta().getId().getIdCtaMayor(), ta);
        }

        log.debug("Activos");
        Query query = currentSession().createQuery("select new Activo(a.id, a.version, a.moi, a.fechaCompra, a.tipoActivo.porciento, a.tipoActivo.vidaUtil, a.inactivo, a.fechaInactivo, a.fechaReubicado, a.tipoActivo.cuenta.id.idCtaMayor, a.centroCosto.id.idCosto, a.centroCosto.nombre) "
                + "from Activo a inner join a.tipoActivo "
                + "where a.empresa.id = :empresaId "
                + "and a.fechaCompra <= :fecha "
                + "and (a.inactivo = false or a.fechaInactivo > :fecha)");
        query.setLong("empresaId", usuario.getEmpresa().getId());
        query.setDate("fecha", fecha.getTime());
        for (Activo activo : (List<Activo>) query.list()) {
            activo = this.deprecia(activo, fecha.getTime());
            Map<String, Object> ta = grupos.get(activo.getTipoActivoCuenta());
            BigDecimal costo = (BigDecimal) ta.get("costo");
            costo = costo.add(activo.getMoi(), mc);
            ta.put("costo", costo);

            BigDecimal depreciacionAcumulada = (BigDecimal) ta.get("depreciacionAcumulada");
            depreciacionAcumulada = depreciacionAcumulada.add(activo.getDepreciacionAcumulada(), mc);
            ta.put("depreciacionAcumulada", depreciacionAcumulada);

            totalCosto = totalCosto.add(activo.getMoi());
            totalDepreciacionAcumulada = totalDepreciacionAcumulada.add(activo.getDepreciacionAcumulada(), mc);

            activo = this.deprecia(activo, fecha2.getTime());
            BigDecimal depreciacionFinal = (BigDecimal) ta.get("depreciacionFinal");
            depreciacionFinal = depreciacionFinal.add(activo.getDepreciacionAcumulada(), mc);
            ta.put("depreciacionFinal", depreciacionFinal);

            totalDepreciacionFinal = totalDepreciacionFinal.add(activo.getDepreciacionAcumulada(), mc);
        }

        // Compras
        query = currentSession().createQuery("select new Activo(a.id, a.version, a.moi, a.fechaCompra, a.tipoActivo.porciento, a.tipoActivo.vidaUtil, a.inactivo, a.fechaInactivo, a.fechaReubicado, a.tipoActivo.cuenta.id.idCtaMayor, a.centroCosto.id.idCosto, a.centroCosto.nombre) "
                + "from Activo a inner join a.tipoActivo "
                + "where a.empresa.id = :empresaId "
                + "and a.fechaCompra between :fecha and :fecha2 "
                + "and (a.inactivo = false or a.fechaInactivo > :fecha)");
        query.setLong("empresaId", usuario.getEmpresa().getId());
        query.setDate("fecha", fecha.getTime());
        query.setDate("fecha2", fecha2.getTime());
        for (Activo activo : (List<Activo>) query.list()) {
            activo = this.deprecia(activo, fecha.getTime());
            Map<String, Object> ta = grupos.get(activo.getTipoActivoCuenta());
            BigDecimal compras = (BigDecimal) ta.get("compras");
            compras = compras.add(activo.getMoi(), mc);
            ta.put("compras", compras);
            totalCompras = totalCompras.add(activo.getMoi(), mc);

            activo = this.deprecia(activo, fecha2.getTime());
            BigDecimal comprasAcumuladas = (BigDecimal) ta.get("comprasAcumuladas");
            comprasAcumuladas = comprasAcumuladas.add(activo.getDepreciacionAcumulada(), mc);
            ta.put("comprasAcumuladas", comprasAcumuladas);

            totalComprasAcumuladas = totalComprasAcumuladas.add(activo.getDepreciacionAcumulada(), mc);
            totalDepreciacionFinal = totalDepreciacionFinal.add(activo.getDepreciacionAcumulada(), mc);
        }

        // Bajas
        query = currentSession().createQuery("select new Activo(a.id, a.version, a.moi, a.fechaCompra, a.tipoActivo.porciento, a.tipoActivo.vidaUtil, a.inactivo, a.fechaInactivo, a.fechaReubicado, a.tipoActivo.cuenta.id.idCtaMayor, a.centroCosto.id.idCosto, a.centroCosto.nombre) "
                + "from Activo a inner join a.tipoActivo "
                + "where a.empresa.id = :empresaId "
                + "and a.inactivo = true and a.fechaInactivo between :fecha and :fecha2");
        query.setLong("empresaId", usuario.getEmpresa().getId());
        query.setDate("fecha", fecha.getTime());
        query.setDate("fecha2", fecha2.getTime());
        for (Activo activo : (List<Activo>) query.list()) {
            activo = this.deprecia(activo, fecha.getTime());
            Map<String, Object> ta = grupos.get(activo.getTipoActivoCuenta());
            BigDecimal bajas = (BigDecimal) ta.get("bajas");
            bajas = bajas.add(activo.getMoi(), mc);
            ta.put("bajas", bajas);

            BigDecimal bajasAcumuladas = (BigDecimal) ta.get("bajasAcumuladas");
            bajasAcumuladas = bajasAcumuladas.add(activo.getDepreciacionAcumulada(), mc);
            ta.put("bajasAcumuladas", bajasAcumuladas);

            totalBajas = totalBajas.add(activo.getMoi(), mc);
            totalBajasAcumuladas = totalBajasAcumuladas.add(activo.getDepreciacionAcumulada(), mc);
        }

        for (TipoActivo tipoActivo : tiposDeActivo) {
            Map<String, Object> grupo = grupos.get(tipoActivo.getCuenta().getId().getIdCtaMayor());
            BigDecimal costo = (BigDecimal) grupo.get("costo");
            BigDecimal compras = (BigDecimal) grupo.get("compras");
            BigDecimal bajas = (BigDecimal) grupo.get("bajas");
            BigDecimal grupoCostoFinal = costo.add(compras.subtract(bajas, mc), mc);
            grupo.put("costoFinal", grupoCostoFinal);
            costoFinal = costoFinal.add(grupoCostoFinal, mc);
            
            BigDecimal depreciacionFinal = (BigDecimal) grupo.get("depreciacionFinal");
            BigDecimal depreciacionAcumulada = (BigDecimal) grupo.get("depreciacionAcumulada");
            grupo.put("comprasAcumuladas", depreciacionFinal.subtract(depreciacionAcumulada, mc));
            totalComprasAcumuladas = totalDepreciacionFinal.subtract(totalDepreciacionAcumulada, mc);
            
            
            BigDecimal grupoValorNeto = costoFinal.subtract(depreciacionFinal, mc);
            grupo.put("valorNeto", grupoValorNeto);
            valorNeto = valorNeto.add(grupoValorNeto, mc);
        }

        resultado.put("lista", grupos.values());
        resultado.put("totalCosto", totalCosto);
        resultado.put("totalCompras", totalCompras);
        resultado.put("totalBajas", totalBajas);
        resultado.put("costoFinal", costoFinal);
        resultado.put("totalDepreciacionAcumulada", totalDepreciacionAcumulada);
        resultado.put("totalComprasAcumuladas", totalComprasAcumuladas);
        resultado.put("totalBajasAcumuladas", totalBajasAcumuladas);
        resultado.put("totalDepreciacionFinal", totalDepreciacionFinal);
        resultado.put("valorNeto", valorNeto);
        return resultado;
    }
}
