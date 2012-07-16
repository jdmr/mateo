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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import mx.edu.um.mateo.activos.dao.ActivoDao;
import mx.edu.um.mateo.activos.model.Activo;
import mx.edu.um.mateo.activos.model.BajaActivo;
import mx.edu.um.mateo.activos.model.FolioActivo;
import mx.edu.um.mateo.activos.model.ReubicacionActivo;
import mx.edu.um.mateo.activos.model.TipoActivo;
import mx.edu.um.mateo.activos.model.XActivo;
import mx.edu.um.mateo.activos.utils.ActivoNoCreadoException;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.contabilidad.model.Cuenta;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Proveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class ActivoDaoHibernate extends BaseDao implements ActivoDao {

    @Autowired
    @Qualifier("dataSource2")
    private DataSource dataSource2;

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
        Date fecha = new Date();
        if (usuario != null) {
            activo.setEmpresa(usuario.getEmpresa());
        }
        activo.setTipoActivo((TipoActivo) session.load(TipoActivo.class, activo.getTipoActivo().getId()));
        activo.setProveedor((Proveedor) session.load(Proveedor.class, activo.getProveedor().getId()));
        activo.setCentroCosto((CentroCosto) session.load(CentroCosto.class, activo.getCentroCosto().getId()));
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
    public Activo actualiza(Activo activo) {
        return this.actualiza(activo, null);
    }

    @Override
    public Activo actualiza(Activo activo, Usuario usuario) {
        Session session = currentSession();
        Date fecha = new Date();
        if (usuario != null) {
            activo.setEmpresa(usuario.getEmpresa());
        }
        activo.setTipoActivo((TipoActivo) session.load(TipoActivo.class, activo.getTipoActivo().getId()));
        activo.setFechaModificacion(fecha);
        session.update(activo);
        audita(activo, usuario, Constantes.ACTUALIZAR, fecha);
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
        Query query = currentSession().createQuery("select new Activo(a.id, a.version, a.moi, a.fechaCompra, a.tipoActivo.porciento, a.tipoActivo.vidaUtil, a.inactivo, a.fechaInactivo) from Activo a inner join a.tipoActivo where a.empresa.id = :empresaId and a.fechaCreacion <= :fecha");
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
    public void arreglaFechas() {
        Query query = currentSession().createQuery("from Activo order by fechaCompra");
        List<Activo> activos = query.list();
        int cont = 0;
        for (Activo activo : activos) {
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
    public List<Cuenta> cuentas(Long organizacionId) {
        Criteria criteria = currentSession().createCriteria(Cuenta.class);
        criteria.createCriteria("organizacion").add(Restrictions.idEq(organizacionId));
        criteria.addOrder(Order.asc("nombre"));
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
        Activo activo = reubicacion.getActivo();
        activo.setFechaModificacion(fecha);
        activo.setCentroCosto((CentroCosto) currentSession().load(CentroCosto.class, activo.getCentroCosto().getId()));
        currentSession().update(activo);
        if (usuario != null) {
            reubicacion.setCreador(usuario.getUsername());
        } else {
            reubicacion.setCreador("sistema");
        }
        reubicacion.setFechaCreacion(fecha);
        currentSession().save(reubicacion);
        this.audita(activo, usuario, Constantes.REUBICACION, fecha);
        currentSession().flush();
        return activo.getFolio();
    }

    @Override
    @Transactional(rollbackFor = ActivoNoCreadoException.class)
    public byte[] sube(byte[] datos, Usuario usuario) throws ActivoNoCreadoException {
        int idx = 5;
        int i = 0;
        try {
            String ejercicioId = "001-2012";
            EjercicioPK ejercicioPK = new EjercicioPK(ejercicioId, usuario.getEmpresa().getOrganizacion());
            Ejercicio ejercicio = (Ejercicio) currentSession().load(Ejercicio.class, ejercicioPK);
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

            POIFSFileSystem fs = new POIFSFileSystem(new ByteArrayInputStream(datos));
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            int numeroDePaginas = workbook.getNumberOfSheets();
            // inicia for para distintas paginas
            HSSFSheet sheet = workbook.getSheetAt(idx);
            int rows = sheet.getPhysicalNumberOfRows();
            for (i = 2; i < rows; i++) {
                log.debug("Leyendo pagina {} renglon {}", idx, i);
                HSSFRow row = sheet.getRow(i);
                String nombreGrupo = row.getCell(0).getStringCellValue().trim();
                TipoActivo tipoActivo = tipos.get(nombreGrupo);
                if (tipoActivo != null) {
                    String cuentaCCosto = row.getCell(2).getStringCellValue().trim();
                    if (cuentaCCosto != null) {
                        CentroCosto centroCosto = centrosDeCosto.get(cuentaCCosto);
                        if (centroCosto == null) {
                            Query ccostoQuery = currentSession().createQuery("select cc from CentroCosto cc "
                                    + "where cc.id.ejercicio.id.idEjercicio = :ejercicioId "
                                    + "and cc.id.ejercicio.id.organizacion.id = :organizacionId "
                                    + "and cc.id.idCosto like :idCosto");
                            ccostoQuery.setString("ejercicioId", ejercicioId);
                            ccostoQuery.setLong("organizacionId", usuario.getEmpresa().getOrganizacion().getId());
                            ccostoQuery.setString("idCosto", "%" + cuentaCCosto);
                            List<CentroCosto> listaCCosto = ccostoQuery.list();
                            log.debug("Regreso: {}", listaCCosto);
                            if (listaCCosto != null && listaCCosto.size() > 0) {
                                centroCosto = listaCCosto.get(0);
                            }
                            if (centroCosto == null) {
                                throw new RuntimeException("(" + idx + ":" + i + ") No se encontro el centro de costo " + cuentaCCosto);
                            }
                            centrosDeCosto.put(cuentaCCosto, centroCosto);
                        }
                        Date fechaCompra = row.getCell(4) != null ? row.getCell(4).getDateCellValue() : null;
                        Boolean seguro = false;
                        if (row.getCell(6) != null && StringUtils.isNotBlank(row.getCell(6).getStringCellValue())) {
                            seguro = true;
                        }
                        String poliza = null;
                        switch (row.getCell(4).getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                poliza = row.getCell(4).toString();
                                break;
                            case HSSFCell.CELL_TYPE_STRING:
                                poliza = row.getCell(4).getStringCellValue().trim();
                                break;
                        }
                        String codigo = row.getCell(8) != null ? row.getCell(8).getStringCellValue().trim() : null;
                        String descripcion = row.getCell(9) != null ? row.getCell(9).getStringCellValue().trim() : null;
                        String marca = row.getCell(10) != null ? row.getCell(10).getStringCellValue().trim() : null;
                        String modelo = null;
                        switch (row.getCell(11).getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                modelo = row.getCell(11).toString();
                                break;
                            case HSSFCell.CELL_TYPE_STRING:
                                modelo = row.getCell(11).getStringCellValue().trim();
                                break;
                        }
                        String serie = row.getCell(12) != null ? row.getCell(12).getStringCellValue().trim() : null;
                        String responsable = row.getCell(13) != null ? row.getCell(13).getStringCellValue().trim() : null;
                        String ubicacion = row.getCell(14) != null ? row.getCell(14).getStringCellValue().trim() : null;
                        BigDecimal costo = null;
                        switch (row.getCell(12).getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                costo = new BigDecimal(row.getCell(15).getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING:
                                costo = new BigDecimal(row.getCell(15).toString());
                                break;
                        }

                        Activo activo = new Activo(fechaCompra, seguro, poliza, codigo, descripcion, marca, modelo, serie, responsable, ubicacion, costo, tipoActivo, centroCosto, proveedor, usuario.getEmpresa());
                        this.crea(activo, usuario);
                    } else {
                        throw new ActivoNoCreadoException("La cuenta viene vacia (" + idx + ":" + i + ")");
                    }
                } else {
                    throw new ActivoNoCreadoException("No se encontro el tipo de activo (" + idx + ":" + i + ")");
                }
            }
            return workbook.getBytes();
        } catch (Exception e) {
            log.error("Hubo problemas al intentar pasar datos de archivo excel a BD (" + idx + ":" + i + ")", e);
            throw new ActivoNoCreadoException("Hubo problemas al intentar pasar datos de archivo excel a BD (" + idx + ":" + i + ")", e);
        }
    }
}
