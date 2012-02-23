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
import mx.edu.um.mateo.inventario.model.*;
import mx.edu.um.mateo.inventario.utils.NoEstaAbiertaException;
import mx.edu.um.mateo.inventario.utils.NoHayExistenciasSuficientes;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarException;
import mx.edu.um.mateo.inventario.utils.ProductoNoSoportaFraccionException;
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
public class SalidaDao {

    private static final Logger log = LoggerFactory.getLogger(SalidaDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public SalidaDao() {
        log.info("Nueva instancia de SalidaDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

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
            criteria.createCriteria("almacen").add(Restrictions.idEq(params.get("almacen")));
            countCriteria.createCriteria("almacen").add(Restrictions.idEq(params.get("almacen")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("folio", filtro));
            propiedades.add(Restrictions.ilike("reporte", filtro));
            propiedades.add(Restrictions.ilike("empleado", filtro));
            propiedades.add(Restrictions.ilike("departamento", filtro));
            propiedades.add(Restrictions.ilike("atendio", filtro));
            propiedades.add(Restrictions.ilike("comentarios", filtro));
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

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("salidas", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public Salida obtiene(Long id) {
        return (Salida) currentSession().get(Salida.class, id);
    }

    public Salida carga(Long id) {
        return (Salida) currentSession().load(Salida.class, id);
    }

    public Salida crea(Salida salida, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            salida.setAlmacen(usuario.getAlmacen());
        }
        Query query = currentSession().createQuery("select e from Estatus e where e.nombre = :nombre");
        query.setString("nombre", Constantes.ABIERTA);
        Estatus estatus = (Estatus) query.uniqueResult();
        salida.setEstatus(estatus);
        salida.setFolio(getFolioTemporal(salida.getAlmacen()));
        session.save(salida);
        session.flush();
        return salida;
    }

    public Salida crea(Salida salida) {
        return this.crea(salida, null);
    }

    public Salida actualiza(Salida salida) throws NoEstaAbiertaException {
        return this.actualiza(salida, null);
    }

    public Salida actualiza(Salida otraSalida, Usuario usuario) throws NoEstaAbiertaException {
        Salida salida = (Salida) currentSession().get(Salida.class, otraSalida.getId());
        switch (salida.getEstatus().getNombre()) {
            case Constantes.ABIERTA:
                Session session = currentSession();
                salida.setReporte(otraSalida.getReporte());
                salida.setEmpleado(otraSalida.getEmpleado());
                salida.setComentarios(otraSalida.getComentarios());
                salida.setAtendio(otraSalida.getAtendio());
                salida.setDepartamento(otraSalida.getDepartamento());
                salida.setCliente(otraSalida.getCliente());
                session.update(salida);
                session.flush();
                return salida;
            default:
                throw new NoEstaAbiertaException("No se puede actualizar una salida que no este abierta");
        }
    }

    public String cierra(Long salidaId, Usuario usuario) throws NoSePuedeCerrarException, NoHayExistenciasSuficientes, NoEstaAbiertaException {
        Salida salida = (Salida) currentSession().get(Salida.class, salidaId);
        salida = cierra(salida, usuario);
        return salida.getFolio();
    }

    public Salida cierra(Salida salida, Usuario usuario) throws NoSePuedeCerrarException, NoHayExistenciasSuficientes, NoEstaAbiertaException {
        if (salida != null) {
            if (salida.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
                if (usuario != null) {
                    salida.setAlmacen(usuario.getAlmacen());
                }

                salida.setIva(BigDecimal.ZERO);
                salida.setTotal(BigDecimal.ZERO);
                for (LoteSalida lote : salida.getLotes()) {
                    Producto producto = lote.getProducto();
                    if (producto.getExistencia().subtract(lote.getCantidad()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new NoHayExistenciasSuficientes("No existen existencias suficientes de " + producto.getNombre(), producto);
                    }
                    lote.setPrecioUnitario(producto.getPrecioUnitario());
                    producto.setExistencia(producto.getExistencia().subtract(lote.getCantidad()));
                    currentSession().update(producto);

                    BigDecimal subtotal = lote.getPrecioUnitario().multiply(lote.getCantidad());
                    salida.setIva(salida.getIva().add(lote.getIva()));
                    salida.setTotal(salida.getTotal().add(subtotal.add(lote.getIva())));
                }

                Query query = currentSession().createQuery("select e from Estatus e where e.nombre = :nombre");
                query.setString("nombre", Constantes.CERRADA);
                Estatus estatus = (Estatus) query.uniqueResult();
                salida.setEstatus(estatus);
                salida.setFolio(getFolio(salida.getAlmacen()));

                currentSession().update(salida);
                currentSession().flush();
                return salida;
            } else {
                throw new NoEstaAbiertaException("No se puede actualizar una salida que no este abierta");
            }
        } else {
            throw new NoSePuedeCerrarException("No se puede cerrar la salida pues no existe");
        }
    }

    public String elimina(Long id) throws NoEstaAbiertaException {
        Salida salida = obtiene(id);
        if (salida.getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            String nombre = salida.getFolio();
            currentSession().delete(salida);
            currentSession().flush();
            return nombre;
        } else {
            throw new NoEstaAbiertaException("No se puede eliminar una salida que no este abierta");
        }
    }

    public LoteSalida creaLote(LoteSalida lote) throws ProductoNoSoportaFraccionException, NoEstaAbiertaException {
        if (lote.getSalida().getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            if (!lote.getProducto().getFraccion()) {
                BigDecimal[] resultado = lote.getCantidad().divideAndRemainder(new BigDecimal("1"));
                if (resultado[1].doubleValue() > 0) {
                    throw new ProductoNoSoportaFraccionException();
                }
            }

            lote.setPrecioUnitario(lote.getProducto().getPrecioUnitario());

            BigDecimal subtotal = lote.getPrecioUnitario().multiply(lote.getCantidad());
            BigDecimal iva = subtotal.multiply(lote.getProducto().getIva()).setScale(2, RoundingMode.HALF_UP);
            lote.setIva(iva);

            currentSession().save(lote);

            return lote;
        } else {
            throw new NoEstaAbiertaException("No se puede crear un lote en una salida que no este abierta");
        }
    }

    public Long eliminaLote(Long id) throws NoEstaAbiertaException {
        log.debug("Eliminando lote {}", id);
        LoteSalida lote = (LoteSalida) currentSession().get(LoteSalida.class, id);
        if (lote.getSalida().getEstatus().getNombre().equals(Constantes.ABIERTA)) {
            id = lote.getSalida().getId();
            currentSession().delete(lote);
            currentSession().flush();
            return id;
        }
        throw new NoEstaAbiertaException("No se pudo eliminar el lote " + id);
    }

    private String getFolioTemporal(Almacen almacen) {
        Query query = currentSession().createQuery("select f from Folio f where f.nombre = :nombre and f.almacen.id = :almacenId");
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
        Query query = currentSession().createQuery("select f from Folio f where f.nombre = :nombre and f.almacen.id = :almacenId");
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
}
