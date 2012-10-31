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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inventario.dao.CancelacionDao;
import mx.edu.um.mateo.inventario.dao.EntradaDao;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.inventario.model.Cancelacion;
import mx.edu.um.mateo.inventario.model.Entrada;
import mx.edu.um.mateo.inventario.model.Estatus;
import mx.edu.um.mateo.inventario.model.Folio;
import mx.edu.um.mateo.inventario.model.LoteEntrada;
import mx.edu.um.mateo.inventario.model.LoteSalida;
import mx.edu.um.mateo.inventario.model.Producto;
import mx.edu.um.mateo.inventario.model.Salida;
import mx.edu.um.mateo.inventario.model.XEntrada;
import mx.edu.um.mateo.inventario.model.XLoteEntrada;
import mx.edu.um.mateo.inventario.model.XLoteSalida;
import mx.edu.um.mateo.inventario.model.XProducto;
import mx.edu.um.mateo.inventario.model.XSalida;
import mx.edu.um.mateo.inventario.utils.NoCuadraException;
import mx.edu.um.mateo.inventario.utils.NoEstaAbiertaException;
import mx.edu.um.mateo.inventario.utils.NoEstaCerradaException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarEnCeroException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarException;
import mx.edu.um.mateo.inventario.utils.ProductoNoSoportaFraccionException;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class EntradaDaoHibernate extends BaseDao implements EntradaDao {

    @Autowired
    private CancelacionDao cancelacionDao;

    public EntradaDaoHibernate() {
        log.info("Nueva instancia de EntradaDao");
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de entradas con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Entrada.class);
        Criteria countCriteria = currentSession().createCriteria(Entrada.class);
        criteria.createAlias("estatus", "est");
        countCriteria.createAlias("estatus", "est");

        if (params.containsKey("almacen")) {
            criteria.createCriteria("almacen").add(
                    Restrictions.idEq(params.get("almacen")));
            countCriteria.createCriteria("almacen").add(
                    Restrictions.idEq(params.get("almacen")));
        }

        if (params.containsKey("proveedorId")) {
            criteria.createCriteria("proveedor").add(
                    Restrictions.idEq(params.get("proveedorId")));
            countCriteria.createCriteria("proveedor").add(
                    Restrictions.idEq(params.get("proveedorId")));
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

        if (params.containsKey(Constantes.ABIERTA)
                || params.containsKey(Constantes.CERRADA)
                || params.containsKey(Constantes.PENDIENTE)
                || params.containsKey(Constantes.FACTURADA)
                || params.containsKey(Constantes.CANCELADA)
                || params.containsKey(Constantes.DEVOLUCION)) {
            Disjunction propiedades = Restrictions.disjunction();
            if (params.containsKey(Constantes.ABIERTA)) {
                propiedades.add(Restrictions.eq("est.nombre", Constantes.ABIERTA));
            }
            if (params.containsKey(Constantes.CERRADA)) {
                propiedades.add(Restrictions.eq("est.nombre", Constantes.CERRADA));
            }
            if (params.containsKey(Constantes.PENDIENTE)) {
                propiedades.add(Restrictions.eq("est.nombre", Constantes.PENDIENTE));
            }
            if (params.containsKey(Constantes.FACTURADA)) {
                propiedades.add(Restrictions.eq("est.nombre", Constantes.FACTURADA));
            }
            if (params.containsKey(Constantes.CANCELADA)) {
                propiedades.add(Restrictions.eq("est.nombre", Constantes.CANCELADA));
            }
            criteria.add(propiedades);
            countCriteria.add(propiedades);

            if (params.containsKey(Constantes.DEVOLUCION)) {
                criteria.add(Restrictions.eq("devolucion", Boolean.TRUE));
                countCriteria.add(Restrictions.eq("devolucion", Boolean.TRUE));
            }
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("folio", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("factura", filtro,
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
            criteria.addOrder(Order.asc("est.prioridad"));
            criteria.addOrder(Order.desc("fechaModificacion"));
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("entradas", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<Entrada> buscaEntradasParaFactura(Map<String, Object> params) {
        log.debug("Buscando lista de entradas con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Entrada.class);

        if (params.containsKey("almacen")) {
            criteria.createCriteria("almacen").add(
                    Restrictions.idEq(params.get("almacen")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("folio", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("factura", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("comentarios", filtro,
                    MatchMode.ANYWHERE));
            criteria.add(propiedades);
        }

        if (params.containsKey("facturaId")) {
            Query query = currentSession().createQuery("select e.id from FacturaAlmacen f inner join f.entradas as e where f.id = :facturaId");
            query.setLong("facturaId", (Long) params.get("facturaId"));
            List<Long> idsDeEntradas = query.list();
            log.debug("idsDeEntradas: {}", idsDeEntradas);
            if (idsDeEntradas != null && idsDeEntradas.size() > 0) {
                criteria.add(Restrictions.not(Restrictions.in("id", idsDeEntradas)));
            }
        }

        criteria.add(Restrictions.eq("devolucion", Boolean.TRUE));
        criteria.createCriteria("estatus").add(
                Restrictions.eq("nombre", Constantes.CERRADA));
        criteria.addOrder(Order.desc("fechaModificacion"));

        criteria.setFirstResult((Integer) params.get("offset"));
        criteria.setMaxResults((Integer) params.get("max"));

        return criteria.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Entrada obtiene(Long id) {
        return (Entrada) currentSession().get(Entrada.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public Entrada carga(Long id) {
        return (Entrada) currentSession().load(Entrada.class, id);
    }

    @Override
    public Entrada crea(Entrada entrada, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            entrada.setAlmacen(usuario.getAlmacen());
        }
        Query query = currentSession().createQuery(
                "select e from Estatus e where e.nombre = :nombre");
        query.setString("nombre", Constantes.ABIERTA);
        Estatus estatus = (Estatus) query.uniqueResult();
        entrada.setEstatus(estatus);
        entrada.setFolio(getFolioTemporal(entrada.getAlmacen()));
        Date fecha = new Date();
        entrada.setFechaCreacion(fecha);
        entrada.setFechaModificacion(fecha);
        session.save(entrada);

        audita(entrada, usuario, Constantes.CREAR, fecha, false);

        session.flush();
        return entrada;
    }

    @Override
    public Entrada crea(Entrada entrada) {
        return this.crea(entrada, null);
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public Entrada actualiza(Entrada entrada) throws NoEstaAbiertaException {
        return this.actualiza(entrada, null);
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public Entrada actualiza(Entrada otraEntrada, Usuario usuario)
            throws NoEstaAbiertaException {
        Entrada entrada = (Entrada) currentSession().get(Entrada.class,
                otraEntrada.getId());
        switch (entrada.getEstatus().getNombre()) {
            case Constantes.ABIERTA:
                Session session = currentSession();
                entrada.setVersion(otraEntrada.getVersion());
                entrada.setFactura(otraEntrada.getFactura());
                entrada.setFechaFactura(otraEntrada.getFechaFactura());
                entrada.setComentarios(otraEntrada.getComentarios());
                entrada.setTipoCambio(otraEntrada.getTipoCambio());
                entrada.setDevolucion(otraEntrada.getDevolucion());
                entrada.setIva(otraEntrada.getIva());
                entrada.setTotal(otraEntrada.getTotal());
                entrada.setProveedor(otraEntrada.getProveedor());
                Date fecha = new Date();
                entrada.setFechaModificacion(fecha);
                session.update(entrada);

                audita(entrada, usuario, Constantes.ACTUALIZAR, fecha, false);

                session.flush();
                return entrada;
            default:
                throw new NoEstaAbiertaException(
                        "No se puede actualizar una entrada que no este abierta");
        }
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class, NoSePuedeCerrarException.class, NoCuadraException.class, NoSePuedeCerrarEnCeroException.class})
    public String pendiente(Long entradaId, Usuario usuario)
            throws NoSePuedeCerrarException, NoCuadraException,
            NoSePuedeCerrarEnCeroException, NoEstaAbiertaException {
        Entrada entrada = (Entrada) currentSession().get(Entrada.class,
                entradaId);
        if (entrada != null) {
            if (entrada.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
                if (usuario != null) {
                    entrada.setAlmacen(usuario.getAlmacen());
                }

                Date fecha = new Date();
                entrada = preparaParaCerrar(entrada, usuario, fecha);

                Query query = currentSession().createQuery(
                        "select e from Estatus e where e.nombre = :nombre");
                query.setString("nombre", Constantes.PENDIENTE);
                Estatus estatus = (Estatus) query.uniqueResult();
                entrada.setEstatus(estatus);
                entrada.setFolio(getFolio(entrada.getAlmacen()));
                entrada.setFechaModificacion(fecha);

                currentSession().update(entrada);

                audita(entrada, usuario, Constantes.ACTUALIZAR, fecha, true);

                currentSession().flush();
                return entrada.getFolio();
            } else {
                throw new NoEstaAbiertaException(
                        "No se puede actualizar una entrada que no este abierta");
            }
        } else {
            throw new NoSePuedeCerrarException(
                    "No se puede cerrar la entrada pues no existe");
        }

    }

    @Override
    @Transactional(rollbackFor = {NoSePuedeCerrarException.class, NoCuadraException.class, NoSePuedeCerrarEnCeroException.class, NoEstaAbiertaException.class})
    public String cierra(Long entradaId, Usuario usuario)
            throws NoSePuedeCerrarException, NoCuadraException,
            NoSePuedeCerrarEnCeroException, NoEstaAbiertaException {
        Entrada entrada = (Entrada) currentSession().get(Entrada.class,
                entradaId);
        entrada = cierra(entrada, usuario);
        return entrada.getFolio();
    }

    @Override
    @Transactional(rollbackFor = {NoSePuedeCerrarException.class, NoCuadraException.class, NoSePuedeCerrarEnCeroException.class, NoEstaAbiertaException.class})
    public Entrada cierra(Entrada entrada, Usuario usuario)
            throws NoSePuedeCerrarException, NoCuadraException,
            NoSePuedeCerrarEnCeroException, NoEstaAbiertaException {
        if (entrada != null) {
            if (entrada.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
                if (usuario != null) {
                    entrada.setAlmacen(usuario.getAlmacen());
                }

                Date fecha = new Date();
                entrada = preparaParaCerrar(entrada, usuario, fecha);
                Query query = currentSession().createQuery(
                        "select e from Estatus e where e.nombre = :nombre");
                query.setString("nombre", Constantes.CERRADA);
                Estatus estatus = (Estatus) query.uniqueResult();
                entrada.setEstatus(estatus);
                entrada.setFolio(getFolio(entrada.getAlmacen()));
                entrada.setFechaModificacion(fecha);

                currentSession().update(entrada);

                audita(entrada, usuario, Constantes.ACTUALIZAR, fecha, true);

                currentSession().flush();
                return entrada;
            } else {
                throw new NoEstaAbiertaException(
                        "No se puede actualizar una entrada que no este abierta");
            }
        } else {
            throw new NoSePuedeCerrarException(
                    "No se puede cerrar la entrada pues no existe");
        }
    }

    @Override
    @Transactional(rollbackFor = {NoSePuedeCerrarException.class})
    public Entrada cierraPendiente(Entrada entrada, Usuario usuario)
            throws NoSePuedeCerrarException {
        Entrada pendiente = (Entrada) currentSession().get(Entrada.class,
                entrada.getId());
        if (entrada.getVersion() != pendiente.getVersion()) {
            throw new NoSePuedeCerrarException(
                    "No es la ultima version de la entrada");
        }
        pendiente.setFactura(entrada.getFactura());
        pendiente.setFechaFactura(entrada.getFechaFactura());
        pendiente.setComentarios(entrada.getComentarios());
        entrada = pendiente;

        Query query = currentSession().createQuery(
                "select e from Estatus e where e.nombre = :nombre");
        query.setString("nombre", Constantes.CERRADA);
        Estatus estatus = (Estatus) query.uniqueResult();
        entrada.setEstatus(estatus);
        Date fecha = new Date();
        entrada.setFechaModificacion(fecha);

        currentSession().update(entrada);

        audita(entrada, usuario, Constantes.ACTUALIZAR, fecha, false);

        currentSession().flush();
        return entrada;
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public String elimina(Long id) throws NoEstaAbiertaException {
        return this.elimina(id, null);
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public String elimina(Long id, Usuario usuario)
            throws NoEstaAbiertaException {
        Entrada entrada = obtiene(id);
        if (entrada.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            String nombre = entrada.getFolio();
            currentSession().delete(entrada);
            audita(entrada, usuario, Constantes.ELIMINAR, new Date(), false);

            currentSession().flush();
            return nombre;
        } else {
            throw new NoEstaAbiertaException(
                    "No se puede eliminar una entrada que no este abierta");
        }
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class, ProductoNoSoportaFraccionException.class})
    public LoteEntrada creaLote(LoteEntrada lote)
            throws ProductoNoSoportaFraccionException, NoEstaAbiertaException {
        log.debug("Creando lote {}", lote);
        lote.setProducto((Producto) currentSession().get(Producto.class, lote.getProducto().getId()));
        lote.setEntrada((Entrada) currentSession().get(Entrada.class, lote.getEntrada().getId()));
        if (lote.getEntrada().getEstatus().getNombre()
                .equals(Constantes.ABIERTA)) {
            if (!lote.getProducto().getFraccion()) {
                BigDecimal[] resultado = lote.getCantidad().divideAndRemainder(
                        new BigDecimal("1"));
                if (resultado[1].doubleValue() > 0) {
                    throw new ProductoNoSoportaFraccionException();
                }
            }

            BigDecimal subtotal = lote.getPrecioUnitario().multiply(
                    lote.getCantidad());
            BigDecimal iva = subtotal.multiply(lote.getProducto().getIva())
                    .setScale(2, RoundingMode.HALF_UP);
            lote.setIva(iva);
            lote.setFechaCreacion(new Date());

            currentSession().save(lote);

            return lote;
        } else {
            throw new NoEstaAbiertaException(
                    "No se puede crear un lote en una entrada que no este abierta");
        }
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public Long eliminaLote(Long id) throws NoEstaAbiertaException {
        log.debug("Eliminando lote {}", id);
        LoteEntrada lote = (LoteEntrada) currentSession().get(
                LoteEntrada.class, id);
        if (lote.getEntrada().getEstatus().getNombre()
                .equals(Constantes.ABIERTA)) {
            id = lote.getEntrada().getId();
            currentSession().delete(lote);
            currentSession().flush();
            return id;
        }
        throw new NoEstaAbiertaException("No se pudo eliminar el lote " + id);
    }

    private String getFolioTemporal(Almacen almacen) {
        Query query = currentSession()
                .createQuery(
                "select f from Folio f where f.nombre = :nombre and f.almacen.id = :almacenId");
        query.setString("nombre", "ENTRADA-TEMPORAL");
        query.setLong("almacenId", almacen.getId());
        query.setLockOptions(LockOptions.UPGRADE);
        Folio folio = (Folio) query.uniqueResult();
        if (folio == null) {
            folio = new Folio("ENTRADA-TEMPORAL");
            folio.setAlmacen(almacen);
            currentSession().save(folio);
            currentSession().flush();
            return getFolioTemporal(almacen);
        }
        folio.setValor(folio.getValor() + 1);
        java.text.NumberFormat nf = java.text.DecimalFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(9);
        nf.setMaximumIntegerDigits(9);
        nf.setMaximumFractionDigits(0);
        StringBuilder sb = new StringBuilder();
        sb.append("TE-");
        sb.append(almacen.getEmpresa().getOrganizacion().getCodigo());
        sb.append(almacen.getEmpresa().getCodigo());
        sb.append(almacen.getCodigo());
        sb.append(nf.format(folio.getValor()));
        return sb.toString();
    }

    private String getFolio(Almacen almacen) {
        Query query = currentSession()
                .createQuery(
                "select f from Folio f where f.nombre = :nombre and f.almacen.id = :almacenId");
        query.setString("nombre", "ENTRADA");
        query.setLong("almacenId", almacen.getId());
        query.setLockOptions(LockOptions.UPGRADE);
        Folio folio = (Folio) query.uniqueResult();
        if (folio == null) {
            folio = new Folio("ENTRADA");
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
        sb.append("E-");
        sb.append(almacen.getEmpresa().getOrganizacion().getCodigo());
        sb.append(almacen.getEmpresa().getCodigo());
        sb.append(almacen.getCodigo());
        sb.append(nf.format(folio.getValor()));
        return sb.toString();
    }

    private BigDecimal costoPromedio(LoteEntrada lote) {
        Producto producto = lote.getProducto();

        BigDecimal cantidad = lote.getCantidad();
        BigDecimal viejoBalance = producto.getPrecioUnitario().multiply(
                producto.getExistencia());
        BigDecimal nuevoBalance = lote.getPrecioUnitario().multiply(cantidad);

        BigDecimal balanceTotal = viejoBalance.add(nuevoBalance);
        BigDecimal articulos = cantidad.add(producto.getExistencia());
        return balanceTotal.divide(articulos, 10, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Entrada preparaParaCerrar(Entrada entrada, Usuario usuario,
            Date fecha) throws NoCuadraException,
            NoSePuedeCerrarEnCeroException {
        BigDecimal iva = entrada.getIva();
        BigDecimal total = entrada.getTotal();
        entrada.setIva(BigDecimal.ZERO);
        entrada.setTotal(BigDecimal.ZERO);
        for (LoteEntrada lote : entrada.getLotes()) {
            Producto producto = lote.getProducto();
            producto.setPrecioUnitario(costoPromedio(lote));
            if (!entrada.getDevolucion()) {
                producto.setUltimoPrecio(lote.getPrecioUnitario());
            }
            producto.setExistencia(producto.getExistencia().add(
                    lote.getCantidad()));
            producto.setFechaModificacion(fecha);
            currentSession().update(producto);
            auditaProducto(producto, usuario, Constantes.ACTUALIZAR,
                    entrada.getId(), null, fecha);

            BigDecimal subtotal = lote.getPrecioUnitario().multiply(
                    lote.getCantidad());
            entrada.setIva(entrada.getIva().add(lote.getIva()));
            entrada.setTotal(entrada.getTotal()
                    .add(subtotal.add(lote.getIva())));
        }
        if (total.equals(BigDecimal.ZERO)) {
            throw new NoSePuedeCerrarEnCeroException(
                    "No se puede cerrar la entrada en cero");
        }
        // Si tanto el iva o el total difieren mas de un 5% del valor que
        // viene en la factura lanzar excepcion
        if (iva.compareTo(entrada.getIva()) != 0
                || total.compareTo(entrada.getTotal()) != 0) {
            BigDecimal variacion = new BigDecimal("0.05");
            BigDecimal topeIva = entrada.getIva().multiply(variacion);
            BigDecimal topeTotal = entrada.getTotal().multiply(variacion);
            if (iva.compareTo(entrada.getIva()) < 0
                    || total.compareTo(entrada.getTotal()) < 0) {
                if (iva.compareTo(entrada.getIva().subtract(topeIva)) >= 0
                        && total.compareTo(entrada.getTotal().subtract(
                        topeTotal)) >= 0) {
                    // todavia puede pasar
                } else {
                    throw new NoCuadraException(
                            "No se puede cerrar porque no cuadran los totales");
                }
            } else {
                if (iva.compareTo(entrada.getIva().add(topeIva)) <= 0
                        && total.compareTo(entrada.getTotal().add(topeTotal)) <= 0) {
                    // todavia puede pasar
                } else {
                    throw new NoCuadraException(
                            "No se puede cerrar porque no cuadran los totales");
                }
            }
        }

        return entrada;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> preCancelacion(Long id, Usuario usuario)
            throws NoEstaCerradaException {
        log.info("{} mando llamar precancelacion de entrada {}", usuario, id);
        Entrada entrada = (Entrada) currentSession().get(Entrada.class, id);
        if (entrada.getEstatus().getNombre().equals(Constantes.CERRADA)
                || entrada.getEstatus().getNombre()
                .equals(Constantes.FACTURADA)) {
            Set<Producto> productos = new HashSet<>();
            for (LoteEntrada lote : entrada.getLotes()) {
                productos.add(lote.getProducto());
            }

            log.debug(
                    "Buscando entradas que contengan los productos {} despues de la fecha {}",
                    productos, entrada.getFechaModificacion());
            Query query = currentSession()
                    .createQuery(
                    "select e from Entrada e inner join e.lotes le inner join e.estatus es "
                    + "where(es.nombre = 'CERRADA' or es.nombre = 'PENDIENTE') "
                    + "and le.producto in (:productos) "
                    + "and e.fechaModificacion > :fecha");
            query.setParameterList("productos", productos);
            query.setTimestamp("fecha", entrada.getFechaModificacion());
            List<Entrada> entradas = (List<Entrada>) query.list();
            for (Entrada e : entradas) {
                log.debug("ENTRADA: {}", e);
                for (LoteEntrada lote : e.getLotes()) {
                    productos.add(lote.getProducto());
                }
            }
            entradas.add(entrada);

            query = currentSession().createQuery(
                    "select s from Salida s inner join s.lotes ls inner join s.estatus es "
                    + "where es.nombre = 'CERRADA' "
                    + "and ls.producto in (:productos) "
                    + "and s.fechaModificacion > :fecha");
            query.setParameterList("productos", productos);
            query.setTimestamp("fecha", entrada.getFechaModificacion());
            List<Salida> salidas = (List<Salida>) query.list();
            for (Salida salida : salidas) {
                log.debug("SALIDA: {}", salida);
                for (LoteSalida lote : salida.getLotes()) {
                    productos.add(lote.getProducto());
                }
            }

            Map<Long, Producto> productosCancelados = new HashMap<>();
            Map<Long, Producto> productosSinHistoria = new HashMap<>();
            for (Producto producto : productos) {
                log.debug("Buscando historial de {}", producto);
                query = currentSession()
                        .createQuery(
                        "select xp from XProducto xp "
                        + "where xp.productoId = :productoId "
                        + "and (xp.actividad = 'CREAR' or actividad = 'ACTUALIZAR') "
                        + "and xp.fechaCreacion < :fecha "
                        + "and (xp.entradaId is null or xp.entradaId != :entradaId) "
                        + "order by xp.fechaCreacion desc");
                query.setLong("productoId", producto.getId());
                query.setTimestamp("fecha", entrada.getFechaModificacion());
                query.setLong("entradaId", entrada.getId());
                query.setMaxResults(1);
                List<XProducto> xproductos = (List<XProducto>) query.list();
                if (xproductos != null && xproductos.get(0) != null) {
                    XProducto xproducto = xproductos.get(0);
                    log.debug("Encontre historia del producto {}", xproducto);
                    Producto p = new Producto();
                    BeanUtils.copyProperties(xproducto, p);
                    p.setTipoProducto(producto.getTipoProducto());
                    p.setAlmacen(producto.getAlmacen());
                    productosCancelados.put(producto.getId(), p);
                } else {
                    log.debug("No encontre historia del producto {}", producto);
                    Producto p = new Producto();
                    BeanUtils.copyProperties(producto, p);
                    p.setPrecioUnitario(BigDecimal.ZERO);
                    p.setUltimoPrecio(BigDecimal.ZERO);
                    p.setExistencia(BigDecimal.ZERO);
                    productosSinHistoria.put(producto.getId(), p);
                }
            }

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("entrada", entrada);
            resultado.put("productos", productos);
            if (entradas != null && entradas.size() > 0) {
                resultado.put("entradas", entradas);
            }
            if (salidas != null && salidas.size() > 0) {
                resultado.put("salidas", salidas);
            }
            if (productosCancelados.size() > 0) {
                resultado.put("productosCancelados",
                        productosCancelados.values());
            }
            if (productosSinHistoria.size() > 0) {
                resultado.put("productosSinHistoria",
                        productosSinHistoria.values());
            }
            return resultado;
        } else {
            throw new NoEstaCerradaException(
                    "La entrada no se puede cancelar porque no esta cerrada o facturada",
                    entrada);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {NoEstaCerradaException.class})
    public Cancelacion cancelar(Long id, Usuario usuario, String comentarios)
            throws NoEstaCerradaException {
        log.info("{} esta cancelando entrada {}", usuario, id);
        Entrada entrada = (Entrada) currentSession().get(Entrada.class, id);
        if (entrada.getEstatus().getNombre().equals(Constantes.CERRADA)
                || entrada.getEstatus().getNombre()
                .equals(Constantes.FACTURADA)) {
            Set<Producto> productos = new HashSet<>();
            for (LoteEntrada lote : entrada.getLotes()) {
                productos.add(lote.getProducto());
            }

            log.debug(
                    "Buscando entradas que contengan los productos {} despues de la fecha {}",
                    productos, entrada.getFechaModificacion());
            Query query = currentSession()
                    .createQuery(
                    "select e from Entrada e inner join e.lotes le inner join e.estatus es "
                    + "where(es.nombre = 'CERRADA' or es.nombre = 'PENDIENTE') "
                    + "and le.producto in (:productos) "
                    + "and e.fechaModificacion > :fecha");
            query.setParameterList("productos", productos);
            query.setTimestamp("fecha", entrada.getFechaModificacion());
            List<Entrada> entradas = (List<Entrada>) query.list();
            for (Entrada e : entradas) {
                log.debug("ENTRADA: {}", e);
                for (LoteEntrada lote : e.getLotes()) {
                    productos.add(lote.getProducto());
                }
            }
            entradas.add(entrada);

            query = currentSession().createQuery(
                    "select s from Salida s inner join s.lotes ls inner join s.estatus es "
                    + "where es.nombre = 'CERRADA' "
                    + "and ls.producto in (:productos) "
                    + "and s.fechaModificacion > :fecha");
            query.setParameterList("productos", productos);
            query.setTimestamp("fecha", entrada.getFechaModificacion());
            List<Salida> salidas = (List<Salida>) query.list();
            for (Salida s : salidas) {
                log.debug("SALIDA: {}", s);
                for (LoteSalida lote : s.getLotes()) {
                    productos.add(lote.getProducto());
                }
            }

            Date fecha = new Date();
            for (Producto producto : productos) {
                log.debug("Buscando historial de {}", producto);
                query = currentSession()
                        .createQuery(
                        "select xp from XProducto xp "
                        + "where xp.productoId = :productoId "
                        + "and (xp.actividad = 'CREAR' or actividad = 'ACTUALIZAR') "
                        + "and xp.fechaCreacion < :fecha "
                        + "and (xp.entradaId is null or xp.entradaId != :entradaId) "
                        + "order by xp.fechaCreacion desc");
                query.setLong("productoId", producto.getId());
                query.setTimestamp("fecha", entrada.getFechaModificacion());
                query.setLong("entradaId", entrada.getId());
                query.setMaxResults(1);
                List<XProducto> xproductos = (List<XProducto>) query.list();
                if (xproductos != null && xproductos.get(0) != null) {
                    XProducto xproducto = xproductos.get(0);
                    log.debug("Encontre historia del producto {}", xproducto);
                    producto.setPrecioUnitario(xproducto.getPrecioUnitario());
                    producto.setUltimoPrecio(xproducto.getUltimoPrecio());
                    producto.setExistencia(xproducto.getExistencia());
                    producto.setFechaModificacion(fecha);
                } else {
                    log.debug("No encontre historia del producto {}", producto);
                    producto.setPrecioUnitario(BigDecimal.ZERO);
                    producto.setUltimoPrecio(BigDecimal.ZERO);
                    producto.setExistencia(BigDecimal.ZERO);
                    producto.setFechaModificacion(fecha);
                }
                currentSession().update(producto);
            }

            query = currentSession().createQuery(
                    "select e from Estatus e where e.nombre = :nombre");
            query.setString("nombre", Constantes.CANCELADA);
            Estatus cancelada = (Estatus) query.uniqueResult();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fechaString = sdf.format(fecha);
            for (Entrada e : entradas) {
                e.setFactura(e.getFactura() + "C" + fechaString);
                e.setEstatus(cancelada);
                e.setFechaModificacion(fecha);
                currentSession().update(e);

                audita(e, usuario, Constantes.CANCELAR, fecha, true);
            }

            for (Salida s : salidas) {
                s.setReporte(s.getReporte() + "C" + fechaString);
                s.setEstatus(cancelada);
                s.setFechaModificacion(fecha);
                currentSession().update(s);

                auditaSalida(s, usuario, Constantes.CANCELAR, fecha, true);
            }

            // Crear cancelacion
            Cancelacion cancelacion = new Cancelacion();
            cancelacion.setFolio(cancelacionDao.getFolio(entrada.getAlmacen()));
            cancelacion.setComentarios(comentarios);
            cancelacion.setEntrada(entrada);
            cancelacion.setProductos(productos);
            if (entradas != null && entradas.size() > 0) {
                cancelacion.setEntradas(entradas);
            }
            if (salidas != null && salidas.size() > 0) {
                cancelacion.setSalidas(salidas);
            }
            cancelacion = cancelacionDao.crea(cancelacion, usuario);
            currentSession().flush();
            for (Producto producto : productos) {
                auditaProducto(producto, usuario, Constantes.CANCELAR, null,
                        cancelacion.getId(), fecha);
            }

            return cancelacion;
        } else {
            throw new NoEstaCerradaException(
                    "La entrada no se puede cancelar porque no esta cerrada o facturada",
                    entrada);
        }
    }

    private void auditaProducto(Producto producto, Usuario usuario,
            String actividad, Long entradaId, Long cancelacionId, Date fecha) {
        XProducto xproducto = new XProducto();
        BeanUtils.copyProperties(producto, xproducto, new String[]{"id",
                    "version"});
        xproducto.setId(null);
        xproducto.setProductoId(producto.getId());
        xproducto.setEntradaId(entradaId);
        xproducto.setCancelacionId(cancelacionId);
        xproducto.setTipoProductoId(producto.getTipoProducto().getId());
        xproducto.setAlmacenId(producto.getAlmacen().getId());
        xproducto.setFechaCreacion(fecha);
        xproducto.setActividad(actividad);
        xproducto.setCreador((usuario != null) ? usuario.getUsername()
                : "sistema");
        currentSession().save(xproducto);
    }

    private void audita(Entrada entrada, Usuario usuario, String actividad,
            Date fecha, Boolean conLotes) {
        XEntrada xentrada = new XEntrada();
        BeanUtils.copyProperties(entrada, xentrada, new String[]{"id",
                    "version"});
        xentrada.setEntradaId(entrada.getId());
        xentrada.setAlmacenId(entrada.getAlmacen().getId());
        xentrada.setProveedorId(entrada.getProveedor().getId());
        xentrada.setEstatusId(entrada.getEstatus().getId());
        xentrada.setFechaCreacion(fecha);
        xentrada.setActividad(actividad);
        xentrada.setCreador((usuario != null) ? usuario.getUsername()
                : "sistema");
        currentSession().save(xentrada);
        if (conLotes) {
            for (LoteEntrada lote : entrada.getLotes()) {
                XLoteEntrada xlote = new XLoteEntrada();
                BeanUtils.copyProperties(lote, xlote, new String[]{"id",
                            "version"});
                xlote.setLoteEntradaId(lote.getId());
                xlote.setEntradaId(entrada.getId());
                xlote.setProductoId(lote.getProducto().getId());
                xlote.setActividad(actividad);
                xlote.setCreador((usuario != null) ? usuario.getUsername()
                        : "sistema");
                xlote.setFechaCreacion(fecha);
                currentSession().save(xlote);
            }
        }
    }

    private void auditaSalida(Salida salida, Usuario usuario, String actividad,
            Date fecha, Boolean conLotes) {
        XSalida xsalida = new XSalida();
        BeanUtils.copyProperties(salida, xsalida, new String[]{"id",
                    "version"});
        xsalida.setSalidaId(salida.getId());
        xsalida.setAlmacenId(salida.getAlmacen().getId());
        xsalida.setClienteId(salida.getCliente().getId());
        xsalida.setEstatusId(salida.getEstatus().getId());
        xsalida.setFechaCreacion(fecha);
        xsalida.setActividad(actividad);
        xsalida.setCreador((usuario != null) ? usuario.getUsername()
                : "sistema");
        currentSession().save(xsalida);
        if (conLotes) {
            for (LoteSalida lote : salida.getLotes()) {
                XLoteSalida xlote = new XLoteSalida();
                BeanUtils.copyProperties(lote, xlote, new String[]{"id",
                            "version"});
                xlote.setLoteSalidaId(lote.getId());
                xlote.setSalidaId(salida.getId());
                xlote.setProductoId(lote.getProducto().getId());
                xlote.setActividad(actividad);
                xlote.setCreador((usuario != null) ? usuario.getUsername()
                        : "sistema");
                xlote.setFechaCreacion(fecha);
                currentSession().save(xlote);
            }
        }
    }
}
