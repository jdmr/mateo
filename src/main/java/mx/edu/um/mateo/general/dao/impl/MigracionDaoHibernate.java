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
package mx.edu.um.mateo.general.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import mx.edu.um.mateo.contabilidad.model.CCostoPK;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.contabilidad.model.CtaMayorPK;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.contabilidad.model.Libro;
import mx.edu.um.mateo.contabilidad.model.LibroPK;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.dao.MigracionDao;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
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
public class MigracionDaoHibernate extends BaseDao implements MigracionDao {

    @Autowired
    @Qualifier("dataSource2")
    private DataSource dataSource;

    @Override
    public void hazlo(Usuario usuario) {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement buscaLibros = null;
        PreparedStatement buscaCCosto = null;
        PreparedStatement buscaCtaMayor = null;
        PreparedStatement buscaCuentas = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Organizacion organizacion = usuario.getEmpresa().getOrganizacion();
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            buscaLibros = conn
                    .prepareStatement("select * from MATEO.CONT_LIBRO where id_ejercicio = ?");
            buscaCCosto = conn
                    .prepareStatement("select * from MATEO.CONT_CCOSTO where id_ejercicio = ?");
            buscaCtaMayor = conn
                    .prepareStatement("select * from MATEO.CONT_CTAMAYOR where id_ejercicio = ?");
            buscaCuentas = conn
                    .prepareStatement("select * from MATEO.CONT_RELACION where id_ejercicio = ?");

            log.debug("Pasando ejercicios");
            rs = stmt.executeQuery("select * from MATEO.CONT_EJERCICIO");
            while (rs.next()) {
                EjercicioPK pk = new EjercicioPK(rs.getString("ID_EJERCICIO"),
                        organizacion);
                Ejercicio ejercicio = new Ejercicio();
                ejercicio.setId(pk);
                ejercicio.setMascAuxiliar(rs.getString("MASC_AUXILIAR"));
                ejercicio.setMascBalance(rs.getString("MASC_BALANCE"));
                ejercicio.setMascCcosto(rs.getString("MASC_CCOSTO"));
                ejercicio.setMascResultado(rs.getString("MASC_RESULTADO"));
                ejercicio.setNivelContable(rs.getByte("NIVEL_CONTABLE"));
                ejercicio.setNivelTauxiliar(rs.getByte("NIVEL_TAUXILIAR"));
                ejercicio.setNombre(rs.getString("NOMBRE"));
                ejercicio.setStatus(rs.getString("STATUS"));
                log.debug("Creando {}", ejercicio);
                currentSession().save(ejercicio);

                buscaLibros.setString(1, pk.getIdEjercicio());
                rs2 = buscaLibros.executeQuery();
                while (rs2.next()) {
                    LibroPK libroPK = new LibroPK(ejercicio,
                            rs2.getString("ID_LIBRO"));
                    Libro libro = new Libro();
                    libro.setId(libroPK);
                    libro.setNombre(rs2.getString("NOMBRE"));
                    log.debug("Creando {}", libro);
                    currentSession().save(libro);
                }

                buscaCCosto.setString(1, pk.getIdEjercicio());
                rs2 = buscaCCosto.executeQuery();
                while (rs2.next()) {
                    CCostoPK cCostoPK = new CCostoPK(ejercicio,
                            rs2.getString("ID_CCOSTO"));
                    CentroCosto centroCosto = new CentroCosto();
                    centroCosto.setId(cCostoPK);
                    centroCosto.setDetalle(rs2.getString("DETALLE"));
                    centroCosto.setIniciales(rs2.getString("INICIALES"));
                    centroCosto.setNombre(rs2.getString("NOMBRE"));
                    log.debug("Creando {}", centroCosto);
                    currentSession().save(centroCosto);
                }

                buscaCtaMayor.setString(1, pk.getIdEjercicio());
                rs2 = buscaCtaMayor.executeQuery();
                while (rs2.next()) {
                    CtaMayorPK ctaMayorPK = new CtaMayorPK(ejercicio,
                            rs2.getString("ID_CTAMAYOR"),
                            rs2.getString("TIPO_CUENTA"));
                    CuentaMayor ctaMayor = new CuentaMayor();
                    ctaMayor.setId(ctaMayorPK);
                    ctaMayor.setNombre(rs2.getString("NOMBRE"));
                    ctaMayor.setNombreFiscal(rs2.getString("NOMBREFISCAL"));
                    ctaMayor.setDetalle(rs2.getString("DETALLE"));
                    ctaMayor.setAviso(rs2.getString("AVISO"));
                    ctaMayor.setAuxiliar(rs2.getString("AUXILIAR"));
                    ctaMayor.setIva(rs2.getString("IVA"));
                    ctaMayor.setPctIVA(rs2.getLong("PCTIVA"));
                    ctaMayor.setDetaller(rs2.getString("DETALLER"));
                    log.debug("Creando {}", ctaMayor);
                    currentSession().save(ctaMayor);
                }

                // buscaCuentas.setString(1, pk.getIdEjercicio());
                // rs2 = buscaCuentas.executeQuery();
                // while(rs2.next()) {
                // CuentaPK cuentaPK = new CuentaPK(ejercicio,
                // rs2.getString("ID_CTAMAYOR"), rs2.getString("TIPO_CUENTA"),
                // rs2.getString("ID_CCOSTO"), rs2.getString("ID_AUXILIAR"));
                // Cuenta cuenta = new Cuenta();
                // cuenta.setId(cuentaPK);
                // cuenta.setNaturaleza(rs2.getString("NATURALEZA"));
                // cuenta.setNombre(rs2.getString("NOMBRE"));
                // cuenta.setStatus(rs2.getString("STATUS"));
                // log.debug("Creando {}", cuenta);
                // currentSession().save(cuenta);
                // }
            }

            currentSession().flush();

        } catch (SQLException e) {
            log.error("Errores en la migracion", e);
        } finally {
            try {
                if (rs2 != null) {
                    rs2.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (buscaLibros != null) {
                    buscaLibros.close();
                }
                if (buscaCCosto != null) {
                    buscaCCosto.close();
                }
                if (buscaCtaMayor != null) {
                    buscaCtaMayor.close();
                }
                if (buscaCuentas != null) {
                    buscaCuentas.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("Problema al cerrar conexiones", e);
            }
        }
    }
}
