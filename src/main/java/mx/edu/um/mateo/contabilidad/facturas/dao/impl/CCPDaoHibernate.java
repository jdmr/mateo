/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import mx.edu.um.mateo.contabilidad.facturas.dao.CCPDao;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import mx.edu.um.mateo.inscripciones.model.AlumnoAcademico;
import mx.edu.um.mateo.inscripciones.model.Modalidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Repository
public class CCPDaoHibernate extends BaseDao implements CCPDao {

    @Autowired
    @Qualifier("dataSource2")
    private DataSource dataSource2;

    @Override
    @Transactional(readOnly = true)
    public boolean obtiene(String folio) {
        log.debug("Obteniendo un folio");
        Boolean existe = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource2.getConnection();
            log.debug("Creando query");
            stmt = conn.prepareStatement(" select count (ap.id_autorizacion) as nreg from mateo.ccp_autorizacion ap where ap.id_autorizacion  =  ? ");
            stmt.setString(1, folio);
            log.debug("Provando conexion");
            rs = stmt.executeQuery();
            log.debug("Ejecutando Query");
            if (rs.next()) {
                log.debug("Entrando al Metodo");
                int n = rs.getInt("nreg");
                if (n == 0) {
                    existe = false;
                }
                if (n > 0) {
                    existe = true;
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
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
        return existe;
    }
}
