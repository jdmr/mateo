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
import mx.edu.um.mateo.inventario.dao.SalidaDao;
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
import mx.edu.um.mateo.inventario.utils.NoEstaAbiertaException;
import mx.edu.um.mateo.inventario.utils.NoEstaCerradaException;
import mx.edu.um.mateo.inventario.utils.NoHayExistenciasSuficientes;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarException;
import mx.edu.um.mateo.inventario.utils.ProductoNoSoportaFraccionException;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
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
public class SalidaDaoHibernate extends BaseDao implements SalidaDao {

    @Autowired
    private CancelacionDao cancelacionDao;

    public SalidaDaoHibernate() {
        log.info("Nueva instancia de SalidaDao");
    }

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de salidas con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Salida.class);
        Criteria countCriteria = currentSession().createCriteria(Salida.class);

        if (params.containsKey("almacen")) {
            criteria.createCriteria("almacen").add(
                    Restrictions.idEq(params.get("almacen")));
            countCriteria.createCriteria("almacen").add(
                    Restrictions.idEq(params.get("almacen")));
        }

        if (params.containsKey("clienteId")) {
            criteria.createCriteria("cliente").add(
                    Restrictions.idEq(params.get("clienteId")));
            countCriteria.createCriteria("cliente").add(
                    Restrictions.idEq(params.get("clienteId")));
        }

        if (params.containsKey("estatusId")) {
            criteria.createCriteria("estatus").add(
                    Restrictions.idEq(params.get("estatusId")));
            countCriteria.createCriteria("estatus").add(
                    Restrictions.idEq(params.get("estatusId")));
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
            propiedades.add(Restrictions.ilike("reporte", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("empleado", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("departamento", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("atendio", filtro,
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
        } else if (!params.containsKey("estatusId")) {
            criteria.createCriteria("estatus").addOrder(Order.asc("prioridad"));
        }
        criteria.addOrder(Order.desc("fechaModificacion"));

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("salidas", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Salida> buscaSalidasParaFactura(Map<String, Object> params) {
        log.debug("Buscando lista de salidas con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Salida.class);

        if (params.containsKey("almacen")) {
            criteria.createCriteria("almacen").add(
                    Restrictions.idEq(params.get("almacen")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("folio", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("reporte", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("empleado", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("departamento", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("atendio", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("comentarios", filtro,
                    MatchMode.ANYWHERE));
            criteria.add(propiedades);
        }

        if (params.containsKey("facturaId")) {
            Query query = currentSession().createQuery("select s.id from FacturaAlmacen f inner join f.salidas as s where f.id = :facturaId");
            query.setLong("facturaId", (Long) params.get("facturaId"));
            List<Long> idsDeSalidas = query.list();
            log.debug("idsDeSalidas: {}", idsDeSalidas);
            if (idsDeSalidas != null && idsDeSalidas.size() > 0) {
                criteria.add(Restrictions.not(Restrictions.in("id", idsDeSalidas)));
            }
        }

        criteria.createCriteria("estatus").add(
                Restrictions.eq("nombre", Constantes.CERRADA));
        criteria.addOrder(Order.desc("fechaModificacion"));

        criteria.setFirstResult((Integer) params.get("offset"));
        criteria.setMaxResults((Integer) params.get("max"));

        return criteria.list();
    }

    @Override
    public Salida obtiene(Long id) {
        return (Salida) currentSession().get(Salida.class, id);
    }

    @Override
    public Salida carga(Long id) {
        return (Salida) currentSession().load(Salida.class, id);
    }

    @Override
    public Salida crea(Salida salida, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            salida.setAlmacen(usuario.getAlmacen());
            salida.setAtendio(usuario.getUsername());
        }
        Query query = currentSession().createQuery(
                "select e from Estatus e where e.nombre = :nombre");
        query.setString("nombre", Constantes.ABIERTA);
        Estatus estatus = (Estatus) query.uniqueResult();
        salida.setEstatus(estatus);
        salida.setFolio(getFolioTemporal(salida.getAlmacen()));
        Date fecha = new Date();
        salida.setFechaCreacion(fecha);
        salida.setFechaModificacion(fecha);
        session.save(salida);

        audita(salida, usuario, Constantes.CREAR, fecha, false);

        session.flush();
        return salida;
    }

    @Override
    public Salida crea(Salida salida) {
        return this.crea(salida, null);
    }

    @Override
    public Salida actualiza(Salida salida) throws NoEstaAbiertaException {
        return this.actualiza(salida, null);
    }

    @Override
    public Salida actualiza(Salida otraSalida, Usuario usuario)
            throws NoEstaAbiertaException {
        Salida salida = (Salida) currentSession().get(Salida.class,
                otraSalida.getId());
        switch (salida.getEstatus().getNombre()) {
            case Constantes.ABIERTA:
                Session session = currentSession();
                salida.setVersion(otraSalida.getVersion());
                salida.setReporte(otraSalida.getReporte());
                salida.setEmpleado(otraSalida.getEmpleado());
                salida.setComentarios(otraSalida.getComentarios());
                if (usuario != null) {
                    salida.setAtendio(usuario.getUsername());
                } else {
                    salida.setAtendio(otraSalida.getAtendio());
                }
                salida.setDepartamento(otraSalida.getDepartamento());
                salida.setCliente(otraSalida.getCliente());
                Date fecha = new Date();
                salida.setFechaModificacion(fecha);
                session.update(salida);

                audita(salida, usuario, Constantes.ACTUALIZAR, fecha, false);

                session.flush();
                return salida;
            default:
                throw new NoEstaAbiertaException(
                        "No se puede actualizar una salida que no este abierta");
        }
    }

    @Override
    public String cierra(Long salidaId, Usuario usuario)
            throws NoSePuedeCerrarException, NoHayExistenciasSuficientes,
            NoEstaAbiertaException {
        Salida salida = (Salida) currentSession().get(Salida.class, salidaId);
        salida = cierra(salida, usuario);
        return salida.getFolio();
    }

    @Override
    public Salida cierra(Salida salida, Usuario usuario)
            throws NoSePuedeCerrarException, NoHayExistenciasSuficientes,
            NoEstaAbiertaException {
        if (salida != null) {
            if (salida.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
                if (usuario != null) {
                    salida.setAlmacen(usuario.getAlmacen());
                }

                salida.setIva(BigDecimal.ZERO);
                salida.setTotal(BigDecimal.ZERO);
                Date fecha = new Date();
                for (LoteSalida lote : salida.getLotes()) {
                    Producto producto = lote.getProducto();
                    if (producto.getExistencia().subtract(lote.getCantidad())
                            .compareTo(BigDecimal.ZERO) < 0) {
                        throw new NoHayExistenciasSuficientes(
                                "No existen existencias suficientes de "
                                + producto.getNombre(), producto);
                    }
                    lote.setPrecioUnitario(producto.getPrecioUnitario());
                    currentSession().update(lote);
                    producto.setExistencia(producto.getExistencia().subtract(
                            lote.getCantidad()));
                    producto.setFechaModificacion(fecha);
                    currentSession().update(producto);
                    auditaProducto(producto, usuario, Constantes.ACTUALIZAR,
                            salida.getId(), null, fecha);

                    BigDecimal subtotal = lote.getPrecioUnitario().multiply(
                            lote.getCantidad());
                    salida.setIva(salida.getIva().add(lote.getIva()));
                    salida.setTotal(salida.getTotal().add(
                            subtotal.add(lote.getIva())));
                }

                Query query = currentSession().createQuery(
                        "select e from Estatus e where e.nombre = :nombre");
                query.setString("nombre", Constantes.CERRADA);
                Estatus estatus = (Estatus) query.uniqueResult();
                salida.setEstatus(estatus);
                salida.setFolio(getFolio(salida.getAlmacen()));
                salida.setAtendio(usuario.getApellido() + ", "
                        + usuario.getNombre());
                salida.setFechaModificacion(fecha);

                currentSession().update(salida);

                audita(salida, usuario, Constantes.ACTUALIZAR, fecha, true);

                currentSession().flush();
                return salida;
            } else {
                throw new NoEstaAbiertaException(
                        "No se puede actualizar una salida que no este abierta");
            }
        } else {
            throw new NoSePuedeCerrarException(
                    "No se puede cerrar la salida pues no existe");
        }
    }

    @Override
    public String elimina(Long id) throws NoEstaAbiertaException {
        Salida salida = obtiene(id);
        if (salida.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            String nombre = salida.getFolio();
            currentSession().delete(salida);

            audita(salida, null, Constantes.ELIMINAR, new Date(), false);

            currentSession().flush();
            return nombre;
        } else {
            throw new NoEstaAbiertaException(
                    "No se puede eliminar una salida que no este abierta");
        }
    }

    @Override
    public LoteSalida creaLote(LoteSalida lote)
            throws ProductoNoSoportaFraccionException, NoEstaAbiertaException {
        if (lote.getSalida().getEstatus().getNombre()
                .equals(Constantes.ABIERTA)) {
            if (!lote.getProducto().getFraccion()) {
                BigDecimal[] resultado = lote.getCantidad().divideAndRemainder(
                        new BigDecimal("1"));
                if (resultado[1].doubleValue() > 0) {
                    throw new ProductoNoSoportaFraccionException();
                }
            }

            lote.setPrecioUnitario(lote.getProducto().getPrecioUnitario());

            BigDecimal subtotal = lote.getPrecioUnitario().multiply(
                    lote.getCantidad());
            BigDecimal iva = subtotal.multiply(lote.getProducto().getIva())
                    .setScale(2, RoundingMode.HALF_UP);
            lote.setIva(iva);
            BigDecimal total = subtotal.add(iva).setScale(2,
                    RoundingMode.HALF_UP);
            lote.setFechaCreacion(new Date());
            currentSession().save(lote);

            Salida salida = lote.getSalida();
            salida.setIva(salida.getIva().add(iva));
            salida.setTotal(salida.getTotal().add(total));
            currentSession().save(salida);

            currentSession().flush();

            return lote;
        } else {
            throw new NoEstaAbiertaException(
                    "No se puede crear un lote en una salida que no este abierta");
        }
    }

    @Override
    public Long eliminaLote(Long id) throws NoEstaAbiertaException {
        log.debug("Eliminando lote {}", id);
        LoteSalida lote = (LoteSalida) currentSession().get(LoteSalida.class,
                id);
        if (lote.getSalida().getEstatus().getNombre()
                .equals(Constantes.ABIERTA)) {
            id = lote.getSalida().getId();
            Salida salida = lote.getSalida();
            salida.setIva(salida.getIva().subtract(lote.getIva()));
            salida.setTotal(salida.getTotal().subtract(lote.getTotal()));
            currentSession().save(salida);
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
        query.setString("nombre", "SALIDA-TEMPORAL");
        query.setLong("almacenId", almacen.getId());
        query.setLockOptions(LockOptions.UPGRADE);
        Folio folio = (Folio) query.uniqueResult();
        if (folio == null) {
            folio = new Folio("SALIDA-TEMPORAL");
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
        sb.append("TS-");
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
        query.setString("nombre", "SALIDA");
        query.setLong("almacenId", almacen.getId());
        query.setLockOptions(LockOptions.UPGRADE);
        Folio folio = (Folio) query.uniqueResult();
        if (folio == null) {
            folio = new Folio("SALIDA");
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
        sb.append("S-");
        sb.append(almacen.getEmpresa().getOrganizacion().getCodigo());
        sb.append(almacen.getEmpresa().getCodigo());
        sb.append(almacen.getCodigo());
        sb.append(nf.format(folio.getValor()));
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> preCancelacion(Long id, Usuario usuario)
            throws NoEstaCerradaException {
        log.info("{} mando llamar precancelacion de salida {}", usuario, id);
        Salida salida = (Salida) currentSession().get(Salida.class, id);
        if (salida.getEstatus().getNombre().equals(Constantes.CERRADA)
                || salida.getEstatus().getNombre().equals(Constantes.FACTURADA)) {
            Set<Producto> productos = new HashSet<>();
            for (LoteSalida lote : salida.getLotes()) {
                productos.add(lote.getProducto());
            }

            log.debug(
                    "Buscando entradas que contengan los productos {} despues de la fecha {}",
                    productos, salida.getFechaModificacion());
            Query query = currentSession()
                    .createQuery(
                    "select e from Entrada e inner join e.lotes le inner join e.estatus es "
                    + "where(es.nombre = 'CERRADA' or es.nombre = 'PENDIENTE') "
                    + "and le.producto in (:productos) "
                    + "and e.fechaModificacion > :fecha");
            query.setParameterList("productos", productos);
            query.setTimestamp("fecha", salida.getFechaModificacion());
            List<Entrada> entradas = (List<Entrada>) query.list();
            for (Entrada entrada : entradas) {
                log.debug("ENTRADA: {}", entrada);
                for (LoteEntrada lote : entrada.getLotes()) {
                    productos.add(lote.getProducto());
                }
            }

            query = currentSession().createQuery(
                    "select s from Salida s inner join s.lotes ls inner join s.estatus es "
                    + "where es.nombre = 'CERRADA' "
                    + "and ls.producto in (:productos) "
                    + "and s.fechaModificacion > :fecha");
            query.setParameterList("productos", productos);
            query.setTimestamp("fecha", salida.getFechaModificacion());
            List<Salida> salidas = (List<Salida>) query.list();
            for (Salida otra : salidas) {
                log.debug("SALIDA: {}", otra);
                for (LoteSalida lote : otra.getLotes()) {
                    productos.add(lote.getProducto());
                }
            }
            salidas.add(salida);

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
                        + "and (xp.salidaId is null or xp.salidaId != :salidaId) "
                        + "order by xp.fechaCreacion desc");
                query.setLong("productoId", producto.getId());
                query.setTimestamp("fecha", salida.getFechaModificacion());
                query.setLong("salidaId", salida.getId());
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
            resultado.put("salida", salida);
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
                    "La salida no se puede cancelar porque no esta cerrada o facturada",
                    salida);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Cancelacion cancelar(Long id, Usuario usuario, String comentarios)
            throws NoEstaCerradaException {
        log.info("{} esta cancelando salida {}", usuario, id);
        Salida salida = (Salida) currentSession().get(Salida.class, id);
        if (salida.getEstatus().getNombre().equals(Constantes.CERRADA)
                || salida.getEstatus().getNombre().equals(Constantes.FACTURADA)) {
            Set<Producto> productos = new HashSet<>();
            for (LoteSalida lote : salida.getLotes()) {
                productos.add(lote.getProducto());
            }

            log.debug(
                    "Buscando entradas que contengan los productos {} despues de la fecha {}",
                    productos, salida.getFechaModificacion());
            Query query = currentSession()
                    .createQuery(
                    "select e from Entrada e inner join e.lotes le inner join e.estatus es "
                    + "where(es.nombre = 'CERRADA' or es.nombre = 'PENDIENTE') "
                    + "and le.producto in (:productos) "
                    + "and e.fechaModificacion > :fecha");
            query.setParameterList("productos", productos);
            query.setTimestamp("fecha", salida.getFechaModificacion());
            List<Entrada> entradas = (List<Entrada>) query.list();
            for (Entrada entrada : entradas) {
                log.debug("ENTRADA: {}", entrada);
                for (LoteEntrada lote : entrada.getLotes()) {
                    productos.add(lote.getProducto());
                }
            }

            query = currentSession().createQuery(
                    "select s from Salida s inner join s.lotes ls inner join s.estatus es "
                    + "where es.nombre = 'CERRADA' "
                    + "and ls.producto in (:productos) "
                    + "and s.fechaModificacion > :fecha");
            query.setParameterList("productos", productos);
            query.setTimestamp("fecha", salida.getFechaModificacion());
            List<Salida> salidas = (List<Salida>) query.list();
            for (Salida otra : salidas) {
                log.debug("SALIDA: {}", otra);
                for (LoteSalida lote : otra.getLotes()) {
                    productos.add(lote.getProducto());
                }
            }
            salidas.add(salida);

            Date fecha = new Date();
            for (Producto producto : productos) {
                log.debug("Buscando historial de {}", producto);
                query = currentSession()
                        .createQuery(
                        "select xp from XProducto xp "
                        + "where xp.productoId = :productoId "
                        + "and (xp.actividad = 'CREAR' or actividad = 'ACTUALIZAR') "
                        + "and xp.fechaCreacion < :fecha "
                        + "and (xp.salidaId is null or xp.salidaId != :salidaId) "
                        + "order by xp.fechaCreacion desc");
                query.setLong("productoId", producto.getId());
                query.setTimestamp("fecha", salida.getFechaModificacion());
                query.setLong("salidaId", salida.getId());
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
            for (Entrada entrada : entradas) {
                entrada.setFactura(entrada.getFactura() + "C" + fechaString);
                entrada.setEstatus(cancelada);
                entrada.setFechaModificacion(fecha);
                currentSession().update(entrada);

                auditaEntrada(entrada, usuario, Constantes.CANCELAR, fecha,
                        true);
            }

            for (Salida s : salidas) {
                s.setReporte(s.getReporte() + "C" + fechaString);
                s.setEstatus(cancelada);
                s.setFechaModificacion(fecha);
                currentSession().update(s);

                audita(s, usuario, Constantes.CANCELAR, fecha, true);
            }

            // Crear cancelacion
            Cancelacion cancelacion = new Cancelacion();
            cancelacion.setFolio(cancelacionDao.getFolio(salida.getAlmacen()));
            cancelacion.setComentarios(comentarios);
            cancelacion.setSalida(salida);
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
                    "La salida no se puede cancelar porque no esta cerrada o facturada",
                    salida);
        }
    }

    private void auditaProducto(Producto producto, Usuario usuario,
            String actividad, Long salidaId, Long cancelacionId, Date fecha) {
        XProducto xproducto = new XProducto();
        BeanUtils.copyProperties(producto, xproducto);
        xproducto.setId(null);
        xproducto.setProductoId(producto.getId());
        xproducto.setSalidaId(salidaId);
        xproducto.setCancelacionId(cancelacionId);
        xproducto.setTipoProductoId(producto.getTipoProducto().getId());
        xproducto.setAlmacenId(producto.getAlmacen().getId());
        xproducto.setFechaCreacion(fecha);
        xproducto.setActividad(actividad);
        xproducto.setCreador((usuario != null) ? usuario.getUsername()
                : "sistema");
        currentSession().save(xproducto);
    }

    private void auditaEntrada(Entrada entrada, Usuario usuario,
            String actividad, Date fecha, boolean conLotes) {
        XEntrada xentrada = new XEntrada();
        BeanUtils.copyProperties(entrada, xentrada);
        xentrada.setId(null);
        xentrada.setEntradaId(entrada.getId());
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

    private void audita(Salida salida, Usuario usuario, String actividad,
            Date fecha, boolean conLotes) {
        XSalida xsalida = new XSalida();
        BeanUtils.copyProperties(salida, xsalida);
        xsalida.setId(null);
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
