/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.dao.EmpleadoDao;
import mx.edu.um.mateo.rh.dao.TraspasoDatosEmpleadoDao;
import mx.edu.um.mateo.rh.model.ClaveEmpleado;
import mx.edu.um.mateo.rh.model.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Repository
@Transactional
public class TraspasoDatosEmpleadoDaoHibernate extends BaseDao implements TraspasoDatosEmpleadoDao {

    @Autowired
    @Qualifier("dataSource2")
    private DataSource dataSource2;
    @Autowired
    private EmpleadoDao dao;

    public void traspaso(Usuario usuario) {

        String COMANDO = "select "
                + "e.id, e.clave, e.nombre, e.appaterno, e.apmaterno, e.fechanacimiento, e.direccion, e.genero,e.status, "
                + "e.nacionalidad, ep.estadocivil, ep.conyuge, ep.fechamatrimonio, ep.madre, ep.padre, ep.finado_padre, ep.finado_madre, "
                + "ep.iglesia, ep.responsabilidad, el.cuenta, el.curp, el.escalafon, el.imms, el.rfc, el.turno, el.fecha_baja, "
                + "el.antiguedad_base,el.fecha_antiguedad_base, el.fecha_alta, el.antiguedad_fiscal, el.modalidad, "
                + "el.experiencia_fuera_um, el.ife, el.rango, el.adventista, ap.username, ap.email "
                + "from "
                + "aron.empleado e, aron.empleadopersonales ep, aron.empleadolaborales el, noe.app_user ap, noe.user_relacion ur  "
                + "where e.id=ep. id "
                + "and e.id=el.id "
                + "and e.id=ur.empleado_id "
                + "and ap.id=ur.id ";
        log.debug("Entrando a metodo de paso de proveedores");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Empleado> empleados = new ArrayList<Empleado>();
        try {
            conn = dataSource2.getConnection();
            stmt = conn.prepareStatement(COMANDO);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Empleado empleado = new Empleado();
                try {
                    if (!rs.getString("adventista").isEmpty() && rs.getString("adventista").equals("Y")) {
                        empleado.setAdventista(true);
                    }
                } catch (NullPointerException e) {
                    empleado.setAdventista(false);

                }
                try {
                    if (rs.getInt("finado_padre") == 1) {
                        empleado.setFinadoMadre(true);
                    }
                } catch (NullPointerException e) {
                    empleado.setFinadoMadre(false);

                }
                try {
                    if (rs.getInt("finado_madre") == 1) {
                        empleado.setFinadoPadre(true);
                    }
                } catch (NullPointerException e) {
                    empleado.setFinadoPadre(false);
                }
                try {
                    if (rs.getString("modalidad").isEmpty() || rs.getString("modalidad") == null) {
                        empleado.setModalidad("D");
                        log.debug("asignando modalidad vasia");
                    } else {
                        empleado.setModalidad(rs.getString("modalidad"));
                    }
                } catch (NullPointerException e) {
                    log.debug("asignando modalidad {}", rs.getString("modalidad"));
                    empleado.setModalidad("D");
                }
                try {
                    if (rs.getString("rango").isEmpty() || rs.getString("rango") == null) {
                        log.debug("asignando rango vasio");
                        empleado.setRango("A");
                    } else {
                        empleado.setRango(rs.getString("rango"));
                    }
                } catch (NullPointerException e) {
                    log.debug("asignando rango {}", rs.getString("rango"));
                    empleado.setRango("A");
                }
                try {
                    if (rs.getString("imms").isEmpty() || rs.getString("imms") == null) {
                        log.debug("asignando rango vasio");
                        empleado.setImms("imms");
                    } else {
                        empleado.setImms(rs.getString("imms"));
                    }
                } catch (NullPointerException e) {
                    log.debug("asignando rango {}", rs.getString("rango"));
                    empleado.setImms("imms");
                }
                empleado.setApMaterno(rs.getString("appaterno"));
                empleado.setApPaterno(rs.getString("apmaterno"));
                empleado.setClave(rs.getString("clave"));
                empleado.setConyuge(rs.getString("conyuge"));
                empleado.setCorreo(rs.getString("email"));
                empleado.setUsername(rs.getString("email"));
                empleado.setCuenta(rs.getString("cuenta"));
                empleado.setCurp(rs.getString("curp"));
                empleado.setDireccion(rs.getString("direccion"));
                empleado.setEscalafon(rs.getInt("escalafon"));
                empleado.setEstadoCivil(rs.getString("estadocivil"));
                empleado.setExperienciaFueraUm(new BigDecimal(rs.getInt("experiencia_fuera_um")));
                empleado.setFechaAlta(rs.getDate("fecha_alta"));
                empleado.setFechaBaja(rs.getDate("fecha_baja"));
                empleado.setFechaMatrimonio(rs.getDate("fechamatrimonio"));
                empleado.setFechaNacimiento(rs.getDate("fechanacimiento"));

                empleado.setGenero(rs.getString("genero"));
                empleado.setId(rs.getLong("id"));
                empleado.setIfe(rs.getString("ife"));
                empleado.setIglesia(rs.getString("iglesia"));
                empleado.setMadre(rs.getString("madre"));

                empleado.setNombre(rs.getString("nombre"));
                empleado.setPadre(rs.getString("padre"));

                empleado.setResponsabilidad(rs.getString("responsabilidad"));
                empleado.setRfc(rs.getString("rfc"));
                empleado.setStatus(rs.getString("status"));
                empleado.setTipoEmpleado(null);
                empleado.setTurno(rs.getInt("turno"));
                empleado.setPassword(rs.getString("nombre"));
                empleados.add(empleado);

            }
        } catch (SQLException ex) {
            Logger.getLogger(TraspasoDatosEmpleadoDaoHibernate.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
                rs.close();
                conn.close();
            } catch (SQLException ex) {
                log.error("{}", ex);
                stmt = null;
                conn = null;
                rs = null;
            }
        }

        for (Empleado e : empleados) {
            log.debug("proveedorFacturas{}", e.toString());
            log.debug("proveedorFacturasCorreo{}", e.getCorreo().toString());
            ClaveEmpleado clave = new ClaveEmpleado();
            clave.setClave(e.getClave());
            clave.setEmpleado(e);
            clave.setEmpresa(usuario.getEmpresa());
            clave.setFecha(new Date());
            clave.setFechaAlta(new Date());
            clave.setObservaciones("clave de empleado");
            clave.setStatus(Constantes.STATUS_ACTIVO);
            clave.setUsuarioAlta(usuario);
            dao.saveEmpleado(e, usuario, clave);
        }

    }
}
