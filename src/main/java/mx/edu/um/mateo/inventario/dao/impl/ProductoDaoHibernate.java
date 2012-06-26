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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Imagen;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inventario.dao.ProductoDao;
import mx.edu.um.mateo.inventario.model.HistorialProducto;
import mx.edu.um.mateo.inventario.model.Producto;
import mx.edu.um.mateo.inventario.model.TipoProducto;
import mx.edu.um.mateo.inventario.model.XProducto;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
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
public class ProductoDaoHibernate extends BaseDao implements ProductoDao {

    public ProductoDaoHibernate() {
        log.info("Nueva instancia de ProductoDao");
    }

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de productos con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Producto.class);
        Criteria countCriteria = currentSession().createCriteria(Producto.class);

        if (params.containsKey("almacen")) {
            criteria.createCriteria("almacen").add(Restrictions.idEq(params.get("almacen")));
            countCriteria.createCriteria("almacen").add(Restrictions.idEq(params.get("almacen")));
        }
        
        if (params.containsKey("inactivo")) {
            criteria.add(Restrictions.eq("inactivo", true));
            countCriteria.add(Restrictions.eq("inactivo", true));
        } else {
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.eq("inactivo", Boolean.FALSE));
            propiedades.add(Restrictions.isNull("inactivo"));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("sku", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("descripcion", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("marca", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("modelo", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("ubicacion", filtro, MatchMode.ANYWHERE));
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
        params.put("productos", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public List<Producto> listaParaSalida(String filtro, Long almacenId) {
        Criteria criteria = currentSession().createCriteria(Producto.class);
        criteria.createCriteria("almacen").add(Restrictions.idEq(almacenId));
        filtro = "%" + filtro + "%";
        Disjunction propiedades = Restrictions.disjunction();
        propiedades.add(Restrictions.ilike("sku", filtro, MatchMode.ANYWHERE));
        propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
        propiedades.add(Restrictions.ilike("descripcion", filtro, MatchMode.ANYWHERE));
        propiedades.add(Restrictions.ilike("marca", filtro, MatchMode.ANYWHERE));
        propiedades.add(Restrictions.ilike("modelo", filtro, MatchMode.ANYWHERE));
        propiedades.add(Restrictions.ilike("ubicacion", filtro, MatchMode.ANYWHERE));
        criteria.add(propiedades);
        
        propiedades = Restrictions.disjunction();
        propiedades.add(Restrictions.eq("inactivo", Boolean.FALSE));
        propiedades.add(Restrictions.isNull("inactivo"));
        criteria.add(propiedades);

        criteria.add(Restrictions.gt("existencia", BigDecimal.ZERO));
        criteria.setMaxResults(10);
        return criteria.list();
    }

    @Override
    public Producto obtiene(Long id) {
        return (Producto) currentSession().get(Producto.class, id);
    }

    @Override
    public Producto crea(Producto producto, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            producto.setAlmacen(usuario.getAlmacen());
        }
        producto.setTipoProducto((TipoProducto) session.get(TipoProducto.class, producto.getTipoProducto().getId()));
        Date fecha = new Date();
        producto.setFechaCreacion(fecha);
        producto.setFechaModificacion(fecha);
        session.save(producto);
        audita(producto, usuario, Constantes.CREAR, fecha);
        session.flush();
        return producto;
    }

    @Override
    public Producto crea(Producto producto) {
        return this.crea(producto, null);
    }

    @Override
    public Producto actualiza(Producto producto) {
        return this.actualiza(producto, null);
    }

    @Override
    public Producto actualiza(Producto otro, Usuario usuario) {
        Date fecha = new Date();
        Session session = currentSession();
        Producto producto = (Producto) session.get(Producto.class, otro.getId());
        producto.setVersion(otro.getVersion());
        producto.setCodigo(otro.getCodigo());
        producto.setDescripcion(otro.getDescripcion());
        producto.setFraccion(otro.getFraccion());
        producto.setIva(otro.getIva());
        producto.setMarca(otro.getMarca());
        producto.setNombre(otro.getNombre());
        producto.setSku(otro.getSku());
        producto.setTiempoEntrega(otro.getTiempoEntrega());
        producto.setUnidadMedida(otro.getUnidadMedida());
        producto.setTipoProducto((TipoProducto) session.get(TipoProducto.class, otro.getTipoProducto().getId()));
        if (otro.getInactivo() && (producto.getInactivo() == null || !producto.getInactivo())) {
            producto.setInactivo(true);
            producto.setFechaInactivo(fecha);
        } else if (!otro.getInactivo() && producto.getInactivo() != null && producto.getInactivo()) {
            producto.setInactivo(false);
            producto.setFechaInactivo(null);
        }
        producto.setFechaModificacion(fecha);
        if (producto.getImagenes().size() > 0) {
            Imagen imagen = producto.getImagenes().get(0);
            producto.getImagenes().clear();
            producto.getImagenes().add(otro.getImagenes().get(0));
            session.delete(imagen);
        }
        session.update(producto);
        audita(producto, usuario, Constantes.ACTUALIZAR, fecha);
        session.flush();
        return producto;
    }

    @Override
    public String elimina(Long id) {
        return elimina(id, null);
    }

    @Override
    public String elimina(Long id, Usuario usuario) {
        Producto producto = obtiene(id);
        String nombre = producto.getNombre();
        audita(producto, usuario, Constantes.ELIMINAR, new Date());

        currentSession().delete(producto);
        currentSession().flush();
        return nombre;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> historial(Long id, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("select new map(");
        sb.append("p.actividad as actividad, p.creador as creador ");
        sb.append(", p.precioUnitario as precioUnitario, p.ultimoPrecio as ultimoPrecio ");
        sb.append(", p.existencia as existencia ");
        sb.append(", p.fechaCreacion as fecha ");
        sb.append(", p.entradaId as entradaId ");
        sb.append(", p.salidaId as salidaId ");
        sb.append(", p.cancelacionId as cancelacionId ");
        sb.append(", (select e.folio from Entrada e where e.id = p.entradaId) as folioEntrada ");
        sb.append(", (select s.folio from Salida s where s.id = p.salidaId) as folioSalida ");
        sb.append(", (select c.folio from Cancelacion c where c.id = p.cancelacionId) as folioCancelacion ");
        sb.append(") from XProducto p where p.productoId = :productoId order by p.id desc");
        Query query = currentSession().createQuery(sb.toString());
        query.setLong("productoId", id);
        if (params.containsKey("max")) {
            Integer max = (Integer) params.get("max");
            query.setMaxResults(max);
            params.put("max", max);
        } else {
            query.setMaxResults(10);
            params.put("max", 10);
        }
        if (params.containsKey("offset")) {
            Integer offset = (Integer) params.get("offset");
            query.setFirstResult(offset);
            params.put("offset", offset);
        } else {
            query.setFirstResult(0);
            params.put("offset", 0);
        }
        params.put("historial", query.list());

        sb = new StringBuilder();
        sb.append("select count(*) as cantidad from XProducto p where p.productoId = :productoId");
        query = currentSession().createQuery(sb.toString());
        query.setLong("productoId", id);
        params.put("cantidad", query.uniqueResult());

        return params;
    }

    @Override
    public void guardaHistorial(Date fecha) {
        log.debug("Buscando historial de productos de la fecha {}", fecha);
        StringBuilder sb = new StringBuilder();
        sb.append("select new HistorialProducto(p.id, p.almacen) from Producto p order by p.codigo");
        log.debug("Cargando lista de productos");
        List<HistorialProducto> productos = currentSession().createQuery(sb.toString()).list();
        List<HistorialProducto> resultado = new ArrayList<>();
        log.debug("Buscando historial por producto ({})", productos.size());
        int cont = 0;
        for (HistorialProducto hp : productos) {
            if (cont++ % 10 == 0) {
                log.debug("Leyendo {} / {} Productos", cont, productos.size());
            }
            sb = new StringBuilder();
            sb.append("select new map(p.existencia as existencia) from XProducto p where p.productoId = :productoId and p.fechaCreacion <= :fecha order by p.fechaCreacion desc");
            Query query = currentSession().createQuery(sb.toString());
            query.setLong("productoId", hp.getProductoId());
            query.setTimestamp("fecha", fecha);
            query.setMaxResults(1);
            Map<String, Object> existencia = (Map<String, Object>) query.uniqueResult();
            if (existencia != null && !existencia.isEmpty()) {
                sb = new StringBuilder();
                sb.append("select hp from HistorialProducto hp where fecha = :fecha and productoId = :productoId");
                Query query2 = currentSession().createQuery(sb.toString());
                query2.setDate("fecha", fecha);
                query2.setLong("productoId", hp.getProductoId());
                HistorialProducto otro = (HistorialProducto) query2.uniqueResult();
                if (otro != null) {
                    hp = otro;
                }
                hp.setExistencia((BigDecimal) existencia.get("existencia"));
                hp.setFecha(fecha);
                resultado.add(hp);
            }
        }
        log.debug("{} / {} Productos", cont, productos.size());
        log.debug("Guardando historial de productos ({})", resultado.size());
        cont = 0;
        for (HistorialProducto hp : resultado) {
            if (cont++ % 10 == 0) {
                log.debug("Guardando {} / {} Productos", cont, resultado.size());
            }
            currentSession().saveOrUpdate(hp);
        }
    }

    @Override
    public Map<String, Object> obtieneHistorial(Map<String, Object> params) {
        List<Producto> resultado = new ArrayList<>();
        if (params.containsKey("fecha") && params.containsKey("almacen")) {
            Date fecha = (Date) params.get("fecha");
            Long almacenId = (Long) params.get("almacen");
            log.debug("Buscando historial de productos de la fecha {}", fecha);
            StringBuilder sb = new StringBuilder();
            sb.append("select new HistorialProducto(p.id, p.almacen) from Producto p where p.almacen.id = :almacenId order by p.codigo");
            log.debug("Cargando lista de productos");
            Query query1 = currentSession().createQuery(sb.toString());
            query1.setLong("almacenId", almacenId);
            List<HistorialProducto> productos = query1.list();
            log.debug("Buscando historial por producto ({})", productos.size());
            int cont = 0;
            for (HistorialProducto hp : productos) {
                if (cont++ % 10 == 0) {
                    log.debug("Leyendo {} / {} Productos", cont, productos.size());
                }
                sb = new StringBuilder();
                sb.append("select new Producto(p.productoId, p.sku, p.nombre, p.descripcion, p.marca, p.modelo, p.ubicacion, p.existencia, p.unidadMedida, p.precioUnitario, p.fraccion, tp.nombre, a.nombre) from XProducto p, TipoProducto tp, Almacen a where p.tipoProductoId = tp.id and p.almacenId = a.id and p.productoId = :productoId and p.fechaCreacion <= :fecha order by p.fechaCreacion desc");
                Query query = currentSession().createQuery(sb.toString());
                query.setLong("productoId", hp.getProductoId());
                query.setTimestamp("fecha", fecha);
                query.setMaxResults(1);
                Producto producto = (Producto) query.uniqueResult();
                if (producto != null) {
                    resultado.add(producto);
                }
            }
            log.debug("{} / {} Productos", cont, productos.size());
            log.debug("Se encontro el historial de productos ({})", resultado.size());
            
            params.put("productos", resultado);
            params.put("cantidad", 1L);
            params.put("max", 1);
        }
        return params;
    }

    @Override
    public Map<String, Object> historialTodos(Map<String, Object> params) {

        return params;
    }

    private void audita(Producto producto, Usuario usuario, String actividad, Date fecha) {
        XProducto xproducto = new XProducto();
        BeanUtils.copyProperties(producto, xproducto, new String[] {"id", "version"});
        xproducto.setProductoId(producto.getId());
        xproducto.setTipoProductoId(producto.getTipoProducto().getId());
        xproducto.setAlmacenId(producto.getAlmacen().getId());
        xproducto.setFechaCreacion(fecha);
        xproducto.setActividad(actividad);
        xproducto.setCreador((usuario != null) ? usuario.getUsername() : "sistema");
        currentSession().save(xproducto);
    }

    @Override
    public void arreglaDescripciones() {
        log.debug("Arreglando descripciones");
        Date fecha = new Date();
        Query query = currentSession().createQuery("from Producto");
        List<Producto> productos = query.list();
        int cont = 0;
        for(Producto producto : productos) {
            if (StringUtils.isBlank(producto.getDescripcion())) {
                log.debug("Actualizando {}", producto);
                producto.setDescripcion(producto.getNombre());
                currentSession().update(producto);
                this.audita(producto, null, Constantes.ACTUALIZAR, fecha);
                cont++;
            }
        }
        currentSession().flush();
        log.debug("Se arreglaron {} de {}", cont, productos.size());
    }
}
