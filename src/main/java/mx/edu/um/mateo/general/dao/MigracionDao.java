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
package mx.edu.um.mateo.general.dao;

import java.sql.*;
import javax.sql.DataSource;
import org.hibernate.HibernateException;
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
public class MigracionDao {

    private static final Logger log = LoggerFactory.getLogger(MigracionDao.class);
    @Autowired
    private DataSource dataSource;
        
    public void hazlo() {
        Connection conn = null;
        Connection conn2 = null;
        Statement stmt = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql:old_mateo","tomcat","tomcat00");
            stmt = conn.createStatement();
            conn2 = dataSource.getConnection();
            conn2.setAutoCommit(false);
            
            log.debug("Migrando organizaciones");
            int max = 0;
            ps = conn2.prepareStatement("insert into organizaciones(id,version,codigo,nombre,nombre_completo) values(?,?,?,?,?)");
            rs = stmt.executeQuery("select * from organizaciones");
            while(rs.next()) {
                ps.setLong(1, rs.getLong("id"));
                ps.setInt(2, rs.getInt("version"));
                ps.setString(3, rs.getString("codigo"));
                ps.setString(4, rs.getString("nombre"));
                ps.setString(5, rs.getString("nombre_completo"));
                max = rs.getInt("id");
                ps.addBatch();
            }
            ps.executeBatch();
            ps = conn2.prepareStatement("select setval('organizaciones_id_seq',?)");
            ps.setLong(1, max);
            ps.executeQuery();
            
            log.debug("Migrando empresas");
            max = 0;
            ps = conn2.prepareStatement("insert into empresas(id,version,codigo,nombre,nombre_completo,rfc,organizacion_id) values(?,?,?,?,?,?,?)");
            rs = stmt.executeQuery("select * from empresas");
            while(rs.next()) {
                ps.setLong(1, rs.getLong("id"));
                ps.setInt(2, rs.getInt("version"));
                ps.setString(3, rs.getString("codigo"));
                ps.setString(4, rs.getString("nombre"));
                ps.setString(5, rs.getString("nombre_completo"));
                ps.setString(6, rs.getString("rfc"));
                ps.setLong(7, rs.getLong("organizacion_id"));
                max = rs.getInt("id");
                ps.addBatch();
            }
            ps.executeBatch();
            ps = conn2.prepareStatement("select setval('empresas_id_seq',?)");
            ps.setLong(1, max);
            ps.executeQuery();
            
            log.debug("Migrando proveedores");
            max = 0;
            ps = conn2.prepareStatement("insert into proveedores(id,version,nombre,nombre_completo,rfc,curp,direccion,telefoo,fax,contacto,correo,base,empresa_id");
            rs = stmt.executeQuery("select * from proveedores");
            while(rs.next()) {
                ps.setLong(1, rs.getLong("id"));
                ps.setInt(2, rs.getInt("version"));
            }
            
            conn2.commit();
        } catch(ClassNotFoundException | SQLException | HibernateException e) {
            log.error("Errores en la migracion", e );
            try {
                conn2.rollback();
            } catch (SQLException ex) {
                log.error("No se pudo hacer rollback", e);
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                if (ps != null) ps.close();
                if (conn2 != null) conn2.close();
            } catch(SQLException e) {
                log.error("Problema al cerrar conexiones", e);
            }
        }
    }
}
