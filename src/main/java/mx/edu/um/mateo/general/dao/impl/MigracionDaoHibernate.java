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
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.dao.MigracionDao;
import org.springframework.beans.factory.annotation.Autowired;
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
    private DataSource dataSource;
        
    @Override
    public void hazlo() {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("insert into facturas_almacen_entradas(factura_id, entrada_id) values(?,?)");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select id, factura_almacen_id from entradas");
            while (rs.next()) {
                Long entradaId = rs.getLong("id");
                Long facturaId = rs.getLong("factura_almacen_id");
                if (facturaId > 0) {
                    ps.setLong(1, facturaId);
                    ps.setLong(2, entradaId);
                    ps.executeUpdate();
                }
            }
            
            ps = conn.prepareStatement("insert into facturas_almacen_salidas(factura_id, salida_id) values(?,?)");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select id, factura_almacen_id from salidas");
            while (rs.next()) {
                Long salidaId = rs.getLong("id");
                Long facturaId = rs.getLong("factura_almacen_id");
                if (facturaId > 0) {
                    ps.setLong(1, facturaId);
                    ps.setLong(2, salidaId);
                    ps.executeUpdate();
                }
            }
            conn.commit();
        } catch(SQLException e) {
            log.error("Errores en la migracion", e );
            try {
                conn.rollback();
            } catch (SQLException ex) {
                log.error("No se pudo hacer rollback", e);
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                if (ps != null) ps.close();
            } catch(SQLException e) {
                log.error("Problema al cerrar conexiones", e);
            }
        }
    }
}
