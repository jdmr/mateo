/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import mx.edu.um.mateo.contabilidad.facturas.dao.PasarProveedoresFacturasDao;
import mx.edu.um.mateo.contabilidad.facturas.dao.ProveedorFacturasDao;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import mx.edu.um.mateo.inscripciones.model.AlumnoAcademico;
import mx.edu.um.mateo.inscripciones.model.Modalidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
public class PasarProveedoresFacturasDaoHibernate extends BaseDao implements PasarProveedoresFacturasDao {

    @Autowired
    @Qualifier("dataSource2")
    private DataSource dataSource2;
    @Autowired
    private ProveedorFacturasDao dao;

    @Override
    @Transactional(readOnly = true)
    public void pasar(Usuario usuario) {

        //Trallendo de la base de datos.
        log.debug("Listado de Alumnos");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ProveedorFacturas> proveedores = new ArrayList<>();
        try {
            conn = dataSource2.getConnection();
            stmt = conn.prepareStatement("select prv.ID,prv.VERSION, prv.RAZON_SOCIAL, prv.TIPOPERSONA, prv.RFC, prv.ID_FISCAL,prv.CURP,"
                    + "prv.CALLE, prv.CODIGO_POSTAL_ID, prv.TELEFONO,prv.TIPO_TERCERO_ID,prv.STATUS,prv.CLABE,prv.BANCO,prv.EMAIL "
                    + "from MATEO.CAT_PROVEEDOR prv, ");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("ID");
                Integer version = rs.getInt("VERSION");
                String razon_social = rs.getString("RAZON_SOCIAL");
                String tipoPersona = rs.getString("TIPOPERSONA");
                String rfc = rs.getString("RFC");
                String id_fiscal = rs.getString("ID_FISCAL");
                String curp = rs.getString("CURP");
                String calle = rs.getString("CALLE");
                String codigo_postal_id = rs.getString("CODIGO_POSTAL_ID");
                String telefono = rs.getString("TELEFONO");
                String tipo_tercero_id = rs.getString("TIPO_TERCERO_ID");
                String status = rs.getString("STATUS");
                String clabe = rs.getString("CLABE");
                String email = rs.getString("EMAIL");
                String banco = rs.getString("BANCO");
                ProveedorFacturas facturas = new ProveedorFacturas(rfc, "prueba", razon_social, razon_social, razon_social,
                        rfc + "@proveedor.com", razon_social, rfc, id_fiscal, curp, calle, telefono, tipo_tercero_id, clabe, banco,
                        status, clabe);
                facturas.setId(id);
                facturas.setVersion(version);
                facturas.setTipoTercero(tipo_tercero_id);
                proveedores.add(facturas);




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
        for (ProveedorFacturas proveedorFacturas : proveedores) {
            dao.crea(proveedorFacturas, usuario);
        }

    }
}
