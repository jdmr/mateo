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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import mx.edu.um.mateo.activos.dao.TipoActivoDao;
import mx.edu.um.mateo.activos.model.TipoActivo;
import mx.edu.um.mateo.contabilidad.model.CtaMayorPK;
import mx.edu.um.mateo.contabilidad.model.Cuenta;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
public class TipoActivoDaoHibernate extends BaseDao implements TipoActivoDao {
    
    @Autowired
    @Qualifier("dataSource2")
    private DataSource dataSource2;
    
    public TipoActivoDaoHibernate() {
        log.info("Se ha creado una nueva instancia de TipoActivoDao");
    }

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de tipos de activos con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(TipoActivo.class);
        Criteria countCriteria = currentSession().createCriteria(TipoActivo.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("descripcion", filtro, MatchMode.ANYWHERE));
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
        params.put("tiposDeActivo", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    @Override
    public TipoActivo obtiene(Long id) {
        return (TipoActivo) currentSession().get(TipoActivo.class, id);
    }

    @Override
    public TipoActivo crea(TipoActivo tipoActivo, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            tipoActivo.setEmpresa(usuario.getEmpresa());
        }
        tipoActivo.setCuenta((CuentaMayor) currentSession().load(CuentaMayor.class, tipoActivo.getCuenta().getId()));
        session.save(tipoActivo);
        session.flush();
        return tipoActivo;
    }

    @Override
    public TipoActivo crea(TipoActivo tipoActivo) {
        return this.crea(tipoActivo, null);
    }

    @Override
    public TipoActivo actualiza(TipoActivo tipoActivo) {
        return this.actualiza(tipoActivo, null);
    }

    @Override
    public TipoActivo actualiza(TipoActivo tipoActivo, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            tipoActivo.setEmpresa(usuario.getEmpresa());
        }
        tipoActivo.setCuenta((CuentaMayor) currentSession().load(Cuenta.class, tipoActivo.getCuenta().getId()));
        session.update(tipoActivo);
        session.flush();
        return tipoActivo;
    }

    @Override
    public String elimina(Long id) {
        TipoActivo tipoActivo = obtiene(id);
        String nombre = tipoActivo.getNombre();
        currentSession().delete(tipoActivo);
        currentSession().flush();
        return nombre;
    }
    
    @Override
    public void migrar(Usuario usuario) {
        log.debug("Migrando datos de tipo de activos");
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource2.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from mateo.cont_ctamayor where id_ctamayor like '1.3.01.0%' and id_ejercicio = '001-2012'");
            while (rs.next()) {
                String ejercicioId = rs.getString("id_ejercicio");
                String cuentaMayorId = rs.getString("id_ctamayor");
                String tipoCuenta = rs.getString("tipo_cuenta");
                EjercicioPK ejercicioPK = new EjercicioPK(ejercicioId, usuario.getEmpresa().getOrganizacion());
                Ejercicio ejercicio = (Ejercicio) currentSession().load(Ejercicio.class, ejercicioPK);
                CtaMayorPK cuentaMayorPK = new CtaMayorPK(ejercicio, cuentaMayorId, tipoCuenta);
                CuentaMayor cuentaMayor = (CuentaMayor) currentSession().load(CuentaMayor.class, cuentaMayorPK);
                TipoActivo tipoActivo = new TipoActivo();
                tipoActivo.setCuenta(cuentaMayor);
                tipoActivo.setDescripcion(rs.getString("nombrefiscal"));
                tipoActivo.setEmpresa(usuario.getEmpresa());
                tipoActivo.setNombre(rs.getString("nombre"));
                switch(cuentaMayorPK.getIdCtaMayor()) {
                    case "1.3.01.01":
                        tipoActivo.setPorciento(new BigDecimal("0.10"));
                        tipoActivo.setVidaUtil(120L);
                        break;
                    case "1.3.01.02":
                        tipoActivo.setPorciento(new BigDecimal("0.30"));
                        tipoActivo.setVidaUtil(40L);
                        break;
                    case "1.3.01.03":
                        tipoActivo.setPorciento(new BigDecimal("0.25"));
                        tipoActivo.setVidaUtil(48L);
                        break;
                    case "1.3.01.04":
                        tipoActivo.setPorciento(new BigDecimal("0.10"));
                        tipoActivo.setVidaUtil(120L);
                        break;
                    case "1.3.01.05":
                        tipoActivo.setPorciento(new BigDecimal("0.20"));
                        tipoActivo.setVidaUtil(60L);
                        break;
                    case "1.3.01.06":
                        tipoActivo.setPorciento(new BigDecimal("0.25"));
                        tipoActivo.setVidaUtil(48L);
                        break;
                }
                currentSession().save(tipoActivo);
            }
            currentSession().flush();
        } catch (SQLException e) {
            log.error("Hubo problemas con Oracle al intentar migrar datos de tipos de activo", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                log.error("Hubo problemas al intentar cerrar conexiones a Oracle", e);
            }
        }
    }
}
