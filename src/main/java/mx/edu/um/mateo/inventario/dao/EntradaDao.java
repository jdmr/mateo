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
package mx.edu.um.mateo.inventario.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ProductoNoSoportaFraccionException;
import mx.edu.um.mateo.inventario.model.*;
import org.hibernate.*;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class EntradaDao {

    private static final Logger log = LoggerFactory.getLogger(EntradaDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public EntradaDao() {
        log.info("Nueva instancia de EntradaDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

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

        if (params.containsKey("almacen")) {
            criteria.createCriteria("almacen").add(Restrictions.idEq(params.get("almacen")));
            countCriteria.createCriteria("almacen").add(Restrictions.idEq(params.get("almacen")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro));
            propiedades.add(Restrictions.ilike("descripcion", filtro));
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
        params.put("entradas", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public Entrada obtiene(Long id) {
        return (Entrada) currentSession().get(Entrada.class, id);
    }

    public Entrada carga(Long id) {
        return (Entrada) currentSession().load(Entrada.class, id);
    }

    public Entrada crea(Entrada entrada, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            entrada.setAlmacen(usuario.getAlmacen());
        }
        Query query = currentSession().createQuery("select e from Estatus e where e.nombre = :nombre");
        query.setString("nombre", Constantes.ABIERTA);
        Estatus estatus = (Estatus) query.uniqueResult();
        entrada.setEstatus(estatus);
        entrada.setFolio(getFolioTemporal(entrada.getAlmacen()));
        session.save(entrada);
        session.flush();
        return entrada;
    }

    public Entrada crea(Entrada entrada) {
        return this.crea(entrada, null);
    }

    public Entrada actualiza(Entrada entrada) {
        return this.actualiza(entrada, null);
    }

    public Entrada actualiza(Entrada otraEntrada, Usuario usuario) {
        Entrada entrada = (Entrada) currentSession().get(Entrada.class, otraEntrada.getId());
        switch (entrada.getEstatus().getNombre()) {
            case Constantes.ABIERTA:
                Session session = currentSession();
                if (otraEntrada.getVersion() != entrada.getVersion()) {
                    throw new RuntimeException("No es la ultima version de la entrada");
                }
                entrada.setFactura(otraEntrada.getFactura());
                entrada.setFechaFactura(otraEntrada.getFechaFactura());
                entrada.setComentarios(otraEntrada.getComentarios());
                entrada.setTipoCambio(otraEntrada.getTipoCambio());
                entrada.setDevolucion(otraEntrada.getDevolucion());
                entrada.setIva(otraEntrada.getIva());
                entrada.setTotal(otraEntrada.getTotal());
                entrada.setProveedor(otraEntrada.getProveedor());
                session.update(entrada);
                session.flush();
                return entrada;
            default:
                throw new RuntimeException("No se puede actualizar una entrada que no este abierta");
        }
    }

    public Entrada cierra(Entrada entrada, Usuario usuario) {
        switch (entrada.getEstatus().getNombre()) {
            case Constantes.ABIERTA:
                if (usuario != null) {
                    entrada.setAlmacen(usuario.getAlmacen());
                }
                break;
            case Constantes.PENDIENTE:
                Entrada pendiente = (Entrada) currentSession().get(Entrada.class, entrada.getId());
                if (entrada.getVersion() != pendiente.getVersion()) {
                    throw new RuntimeException("No es la ultima version de la entrada");
                }
                pendiente.setFactura(entrada.getFactura());
                pendiente.setFechaFactura(entrada.getFechaFactura());
                entrada = pendiente;
                break;
            default:
                throw new RuntimeException("No se puede actualizar una entrada que no este abierta");
        }
        for (LoteEntrada lote : entrada.getLotes()) {
            Producto producto = lote.getProducto();
            producto.setPrecioUnitario(costoPromedio(lote));
            if (!entrada.getDevolucion()) {
                producto.setUltimoPrecio(lote.getPrecioUnitario());
            }
            producto.setExistencia(producto.getExistencia().add(lote.getCantidad()));
            currentSession().update(producto);

            BigDecimal subtotal = lote.getPrecioUnitario().multiply(lote.getCantidad());
            entrada.setIva(entrada.getIva().add(lote.getIva()));
            entrada.setTotal(entrada.getTotal().add(subtotal.add(lote.getIva())));
        }
        Query query = currentSession().createQuery("select e from Estatus e where e.nombre = :nombre");
        query.setString("nombre", Constantes.CERRADA);
        Estatus estatus = (Estatus) query.uniqueResult();
        entrada.setEstatus(estatus);
        entrada.setFolio(getFolio(entrada.getAlmacen()));

        currentSession().update(entrada);
        currentSession().flush();
        return entrada;
    }

    public String elimina(Long id) {
        Entrada entrada = obtiene(id);
        if (entrada.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            String nombre = entrada.getFolio();
            currentSession().delete(entrada);
            currentSession().flush();
            return nombre;
        } else {
            throw new RuntimeException("No se puede eliminar una entrada que no este abierta");
        }
    }

    public LoteEntrada creaLote(LoteEntrada lote) throws ProductoNoSoportaFraccionException {
        if (!lote.getProducto().getFraccion()) {
            BigDecimal[] resultado = lote.getCantidad().divideAndRemainder(new BigDecimal("1"));
            if (resultado[1].doubleValue() > 0) {
                throw new ProductoNoSoportaFraccionException();
            }
        }

        BigDecimal subtotal = lote.getPrecioUnitario().multiply(lote.getCantidad());
        BigDecimal iva = subtotal.multiply(lote.getProducto().getIva()).setScale(2, RoundingMode.HALF_UP);
        lote.setIva(iva);

        currentSession().save(lote);

        return lote;
    }

    public Long eliminaLote(Long id) {
        log.debug("Eliminando lote {}", id);
        LoteEntrada lote = (LoteEntrada) currentSession().get(LoteEntrada.class, id);
        if (lote.getEntrada().getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            id = lote.getEntrada().getId();
            currentSession().delete(lote);
            currentSession().flush();
            return id;
        }
        throw new RuntimeException("No se pudo eliminar el lote " + id);
    }

    private String getFolioTemporal(Almacen almacen) {
        Query query = currentSession().createQuery("select f from Folio f where f.nombre = :nombre and f.almacen.id = :almacenId");
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
        Query query = currentSession().createQuery("select f from Folio f where f.nombre = :nombre and f.almacen.id = :almacenId");
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
        BigDecimal viejoBalance = producto.getPrecioUnitario().multiply(producto.getExistencia());
        BigDecimal nuevoBalance = lote.getPrecioUnitario().multiply(cantidad);

        BigDecimal balanceTotal = viejoBalance.add(nuevoBalance);
        BigDecimal articulos = cantidad.add(producto.getExistencia());
        return balanceTotal.divide(articulos).setScale(2, RoundingMode.HALF_UP);
    }
}
