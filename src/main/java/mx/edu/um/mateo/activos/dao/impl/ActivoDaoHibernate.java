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

import java.util.HashMap;
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

        if (params.containsKey("tipoActivo")) {
            criteria.createCriteria("tipoActivo").add(Restrictions.idEq(params.get("tipoActivo")));
            countCriteria.createCriteria("tipoActivo").add(Restrictions.idEq(params.get("tipoActivo")));
        }

        if (params.containsKey("cuenta")) {
            criteria.createCriteria("cuenta").add(Restrictions.idEq(params.get("cuenta")));
            countCriteria.createCriteria("cuenta").add(Restrictions.idEq(params.get("cuenta")));
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

        return params;
    }

    public Activo obtiene(Long id) {
        Activo activo = (Activo) currentSession().get(Activo.class, id);
        return activo;
    }

    public Activo crea(Activo activo, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            activo.setEmpresa(usuario.getEmpresa());
        }
        activo.setTipoActivo((TipoActivo)session.load(TipoActivo.class, activo.getTipoActivo().getId()));
        activo.setFolio(this.getFolio(activo.getEmpresa()));
        session.save(activo);
        session.flush();
        return activo;
    }

    public Activo crea(Activo activo) {
        return this.crea(activo, null);
    }

    public Activo actualiza(Activo activo) {
        return this.actualiza(activo, null);
    }

    public Activo actualiza(Activo activo, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            activo.setEmpresa(usuario.getEmpresa());
        }
        activo.setTipoActivo((TipoActivo)session.load(TipoActivo.class, activo.getTipoActivo().getId()));
        session.update(activo);
        session.flush();
        return activo;
    }

    public String elimina(Long id) {
        Activo activo = obtiene(id);
        String nombre = activo.getFolio();
        currentSession().delete(activo);
        currentSession().flush();
        return nombre;
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
}
