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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inventario.dao.FacturaAlmacenDao;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.inventario.model.Entrada;
import mx.edu.um.mateo.inventario.model.Estatus;
import mx.edu.um.mateo.inventario.model.FacturaAlmacen;
import mx.edu.um.mateo.inventario.model.Folio;
import mx.edu.um.mateo.inventario.model.Salida;
import mx.edu.um.mateo.inventario.model.XEntrada;
import mx.edu.um.mateo.inventario.model.XFacturaAlmacen;
import mx.edu.um.mateo.inventario.model.XSalida;
import mx.edu.um.mateo.inventario.utils.NoEstaAbiertaException;
import mx.edu.um.mateo.inventario.utils.NoEstaCerradaException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCancelarException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarEnCeroException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarException;
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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class FacturaAlmacenDaoHibernate extends BaseDao implements
        FacturaAlmacenDao {

    public FacturaAlmacenDaoHibernate() {
        log.info("Nueva instancia de FacturaAlmacenDao");
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de facturas con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(
                FacturaAlmacen.class);
        Criteria countCriteria = currentSession().createCriteria(
                FacturaAlmacen.class);

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
            criteria.createCriteria("estatus").addOrder(Order.asc("prioridad"));
        }
        criteria.addOrder(Order.desc("fechaModificacion"));

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("facturas", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaAlmacen obtiene(Long id) {
        return (FacturaAlmacen) currentSession().get(FacturaAlmacen.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaAlmacen carga(Long id) {
        return (FacturaAlmacen) currentSession().load(FacturaAlmacen.class, id);
    }

    @Override
    public FacturaAlmacen crea(FacturaAlmacen factura, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            factura.setAlmacen(usuario.getAlmacen());
        }
        Query query = currentSession().createQuery(
                "select e from Estatus e where e.nombre = :nombre");
        query.setString("nombre", Constantes.ABIERTA);
        Estatus estatus = (Estatus) query.uniqueResult();
        factura.setEstatus(estatus);
        factura.setFolio(getFolioTemporal(factura.getAlmacen()));
        Date fecha = new Date();
        factura.setFechaCreacion(fecha);
        factura.setFechaModificacion(fecha);
        session.save(factura);

        audita(factura, usuario, Constantes.CREAR, fecha);

        session.flush();
        return factura;
    }

    @Override
    public FacturaAlmacen crea(FacturaAlmacen factura) {
        return this.crea(factura, null);
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public FacturaAlmacen actualiza(FacturaAlmacen factura)
            throws NoEstaAbiertaException {
        return this.actualiza(factura, null);
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public FacturaAlmacen actualiza(FacturaAlmacen otraFactura, Usuario usuario)
            throws NoEstaAbiertaException {
        FacturaAlmacen factura = (FacturaAlmacen) currentSession().get(
                FacturaAlmacen.class, otraFactura.getId());
        switch (factura.getEstatus().getNombre()) {
            case Constantes.ABIERTA:
                Session session = currentSession();
                factura.setVersion(otraFactura.getVersion());
                factura.setFecha(otraFactura.getFecha());
                factura.setComentarios(otraFactura.getComentarios());
                factura.setIva(otraFactura.getIva());
                factura.setTotal(otraFactura.getTotal());
                factura.setCliente(otraFactura.getCliente());
                Date fecha = new Date();
                factura.setFechaModificacion(fecha);
                session.update(factura);

                audita(factura, usuario, Constantes.ACTUALIZAR, fecha);

                session.flush();
                return factura;
            default:
                throw new NoEstaAbiertaException(
                        "No se puede actualizar una factura que no este abierta");
        }
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class, NoSePuedeCerrarException.class, NoSePuedeCerrarEnCeroException.class})
    public String cierra(Long facturaId, Usuario usuario)
            throws NoSePuedeCerrarException, NoSePuedeCerrarEnCeroException,
            NoEstaAbiertaException {
        FacturaAlmacen factura = (FacturaAlmacen) currentSession().get(
                FacturaAlmacen.class, facturaId);
        factura = cierra(factura, usuario);
        return factura.getFolio();
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class, NoSePuedeCerrarException.class, NoSePuedeCerrarEnCeroException.class})
    public FacturaAlmacen cierra(FacturaAlmacen factura, Usuario usuario)
            throws NoSePuedeCerrarException, NoSePuedeCerrarEnCeroException,
            NoEstaAbiertaException {
        if (factura != null) {
            if (factura.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
                if (usuario != null) {
                    factura.setAlmacen(usuario.getAlmacen());
                }

                Date fecha = new Date();
                factura.setIva(BigDecimal.ZERO);
                factura.setTotal(BigDecimal.ZERO);
                Query query = currentSession().createQuery(
                        "select e from Estatus e where e.nombre = :nombre");
                query.setString("nombre", Constantes.FACTURADA);
                Estatus facturada = (Estatus) query.uniqueResult();
                for (Salida salida : factura.getSalidas()) {
                    salida.setEstatus(facturada);
                    salida.setFechaModificacion(fecha);
                    currentSession().update(salida);
                    audita(salida, usuario, Constantes.FACTURADA, fecha);
                    factura.setIva(factura.getIva().add(salida.getIva()));
                    factura.setTotal(factura.getTotal().add(salida.getTotal()));
                }

                for (Entrada entrada : factura.getEntradas()) {
                    entrada.setEstatus(facturada);
                    entrada.setFechaModificacion(fecha);
                    currentSession().update(entrada);
                    audita(entrada, usuario, Constantes.FACTURADA, fecha);
                    factura.setIva(factura.getIva().subtract(entrada.getIva()));
                    factura.setTotal(factura.getTotal().subtract(
                            entrada.getTotal()));
                }

                query.setString("nombre", Constantes.CERRADA);
                Estatus estatus = (Estatus) query.uniqueResult();
                factura.setEstatus(estatus);
                factura.setFolio(getFolio(factura.getAlmacen()));
                factura.setFechaModificacion(fecha);

                currentSession().update(factura);

                audita(factura, usuario, Constantes.ACTUALIZAR, fecha);

                currentSession().flush();
                return factura;
            } else {
                throw new NoEstaAbiertaException(
                        "No se puede actualizar una factura que no este abierta");
            }
        } else {
            throw new NoSePuedeCerrarException(
                    "No se puede cerrar la factura pues no existe");
        }
    }

    @Override
    @Transactional(rollbackFor = {NoSePuedeCancelarException.class, NoEstaCerradaException.class})
    public FacturaAlmacen cancelar(Long id, Usuario usuario)
            throws NoEstaCerradaException, NoSePuedeCancelarException {
        FacturaAlmacen factura = (FacturaAlmacen) currentSession().get(
                FacturaAlmacen.class, id);
        if (factura != null) {
            if (factura.getEstatus().getNombre().equals(Constantes.CERRADA)) {
                Query query = currentSession().createQuery(
                        "select e from Estatus e where e.nombre = :nombre");
                query.setString("nombre", Constantes.CERRADA);
                Estatus cerrada = (Estatus) query.uniqueResult();

                Date fecha = new Date();
                for (Entrada entrada : factura.getEntradas()) {
                    entrada.setEstatus(cerrada);
                    entrada.setFechaModificacion(fecha);
                    currentSession().update(entrada);

                    audita(entrada, usuario, Constantes.ACTUALIZAR, fecha);
                }

                for (Salida salida : factura.getSalidas()) {
                    salida.setEstatus(cerrada);
                    salida.setFechaModificacion(fecha);
                    currentSession().update(salida);

                    audita(salida, usuario, Constantes.ACTUALIZAR, fecha);
                }

                query.setString("nombre", Constantes.CANCELADA);
                Estatus cancelada = (Estatus) query.uniqueResult();
                factura.setFechaModificacion(new Date());
                factura.setEstatus(cancelada);
                currentSession().update(factura);
                currentSession().flush();
                return factura;
            } else {
                throw new NoEstaCerradaException(
                        "No se puede actualizar una factura que no este cerrada",
                        factura);
            }
        } else {
            throw new NoSePuedeCancelarException(
                    "No se puede cancelar la factura porque no existe", factura);
        }
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
        FacturaAlmacen factura = obtiene(id);
        if (factura.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            String nombre = factura.getFolio();
            currentSession().delete(factura);
            audita(factura, usuario, Constantes.ELIMINAR, new Date());

            currentSession().flush();
            return nombre;
        } else {
            throw new NoEstaAbiertaException(
                    "No se puede eliminar una factura que no este abierta");
        }
    }

    private String getFolioTemporal(Almacen almacen) {
        Query query = currentSession()
                .createQuery(
                "select f from Folio f where f.nombre = :nombre and f.almacen.id = :almacenId");
        query.setString("nombre", "FACTURA-TEMPORAL");
        query.setLong("almacenId", almacen.getId());
        query.setLockOptions(LockOptions.UPGRADE);
        Folio folio = (Folio) query.uniqueResult();
        if (folio == null) {
            folio = new Folio("FACTURA-TEMPORAL");
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
        sb.append("TF-");
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
        query.setString("nombre", "FACTURA");
        query.setLong("almacenId", almacen.getId());
        query.setLockOptions(LockOptions.UPGRADE);
        Folio folio = (Folio) query.uniqueResult();
        if (folio == null) {
            folio = new Folio("FACTURA");
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
        sb.append("FA-");
        sb.append(almacen.getEmpresa().getOrganizacion().getCodigo());
        sb.append(almacen.getEmpresa().getCodigo());
        sb.append(almacen.getCodigo());
        sb.append(nf.format(folio.getValor()));
        return sb.toString();
    }

    private void audita(FacturaAlmacen factura, Usuario usuario,
            String actividad, Date fecha) {
        XFacturaAlmacen xfactura = new XFacturaAlmacen();
        BeanUtils.copyProperties(factura, xfactura);
        xfactura.setId(null);
        xfactura.setFacturaAlmacenId(factura.getId());
        xfactura.setAlmacenId(factura.getAlmacen().getId());
        xfactura.setClienteId(factura.getCliente().getId());
        xfactura.setEstatusId(factura.getEstatus().getId());
        xfactura.setFechaCreacion(fecha);
        xfactura.setActividad(actividad);
        xfactura.setCreador((usuario != null) ? usuario.getUsername()
                : "sistema");
        currentSession().save(xfactura);
    }

    private void audita(Salida salida, Usuario usuario, String actividad,
            Date fecha) {
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
    }

    private void audita(Entrada entrada, Usuario usuario, String actividad,
            Date fecha) {
        XEntrada xentrada = new XEntrada();
        BeanUtils.copyProperties(entrada, xentrada);
        xentrada.setId(null);
        xentrada.setEntradaId(entrada.getId());
        xentrada.setProveedorId(entrada.getProveedor().getId());
        xentrada.setEstatusId(entrada.getEstatus().getId());
        xentrada.setAlmacenId(entrada.getAlmacen().getId());
        xentrada.setFechaCreacion(fecha);
        xentrada.setActividad(actividad);
        xentrada.setCreador((usuario != null) ? usuario.getUsername()
                : "sistema");
        currentSession().save(xentrada);
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public FacturaAlmacen agregaSalida(Long facturaId, Long salidaId) throws NoEstaAbiertaException {
        FacturaAlmacen factura = (FacturaAlmacen) currentSession().get(
                FacturaAlmacen.class, facturaId);
        if (factura.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            Salida salida = (Salida) currentSession().get(Salida.class, salidaId);
            factura.getSalidas().add(salida);
            factura.setTotal(factura.getTotal().add(salida.getTotal()));
            factura.setIva(factura.getIva().add(salida.getIva()));
            factura.setFechaModificacion(new Date());
            currentSession().save(factura);
            currentSession().flush();
            return factura;
        } else {
            throw new NoEstaAbiertaException("La factura no esta abierta, no se pueden agregar la salida");
        }
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public FacturaAlmacen agregaEntrada(Long facturaId, Long entradaId) throws NoEstaAbiertaException {
        FacturaAlmacen factura = (FacturaAlmacen) currentSession().get(
                FacturaAlmacen.class, facturaId);
        if (factura.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            Entrada entrada = (Entrada) currentSession().load(Entrada.class,
                    entradaId);
            factura.getEntradas().add(entrada);
            factura.setTotal(factura.getTotal().subtract(entrada.getTotal()));
            factura.setIva(factura.getIva().subtract(entrada.getIva()));
            factura.setFechaModificacion(new Date());
            currentSession().save(factura);
            currentSession().flush();
            return factura;
        } else {
            throw new NoEstaAbiertaException("La factura no esta abierta, no se puede agregar la entrada");
        }
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public FacturaAlmacen eliminaSalida(Long facturaId, Long salidaId) throws NoEstaAbiertaException {
        log.debug("Eliminando salida {} de factura {}", salidaId, facturaId);
        FacturaAlmacen factura = (FacturaAlmacen) currentSession().get(
                FacturaAlmacen.class, facturaId);
        if (factura.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            Salida salida = (Salida) currentSession().load(Salida.class, salidaId);
            factura.setTotal(factura.getTotal().subtract(salida.getTotal()));
            factura.setIva(factura.getIva().subtract(salida.getIva()));
            log.trace("SalidasA: {}", factura.getSalidas());
            factura.getSalidas().remove(salida);
            log.trace("SalidasB: {}", factura.getSalidas());
            factura.setFechaModificacion(new Date());
            currentSession().save(factura);
            currentSession().flush();
            return factura;
        } else {
            throw new NoEstaAbiertaException("La factura no esta abierta, no se puede eliminar la salida");
        }
    }

    @Override
    @Transactional(rollbackFor = {NoEstaAbiertaException.class})
    public FacturaAlmacen eliminaEntrada(Long facturaId, Long entradaId) throws NoEstaAbiertaException {
        log.debug("Eliminando entrada {} de factura {}", entradaId, facturaId);
        FacturaAlmacen factura = (FacturaAlmacen) currentSession().get(
                FacturaAlmacen.class, facturaId);
        if (factura.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            Entrada entrada = (Entrada) currentSession().load(Entrada.class,
                    entradaId);
            factura.setTotal(factura.getTotal().add(entrada.getTotal()));
            factura.setIva(factura.getIva().add(entrada.getIva()));
            factura.getEntradas().remove(entrada);
            factura.setFechaModificacion(new Date());
            currentSession().save(factura);
            currentSession().flush();
            return factura;
        } else {
            throw new NoEstaAbiertaException("La factura no esta abierta, no se puede eliminar la entrada");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<Salida> salidas(Long facturaId) {
        Query query = currentSession()
                .createQuery(
                "select s from Salida s inner join s.facturaAlmacen fa where fa.id = :facturaId");
        query.setLong("facturaId", facturaId);
        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entrada> entradas(Long facturaId) {
        Query query = currentSession()
                .createQuery(
                "select e from Entrada e inner join e.facturaAlmacen fa where fa.id = :facturaId");
        query.setLong("facturaId", facturaId);
        return query.list();
    }
}
