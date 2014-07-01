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
import javax.sql.DataSource;
import mx.edu.um.mateo.contabilidad.facturas.dao.PasarProveedoresFacturasDao;
import mx.edu.um.mateo.contabilidad.facturas.dao.ProveedorFacturasDao;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Repository
@Transactional
public class PasarProveedoresFacturasDaoHibernate extends BaseDao implements PasarProveedoresFacturasDao {

    @Autowired
    @Qualifier("dataSource2")
    private DataSource dataSource2;
    @Autowired
    private ProveedorFacturasDao dao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
//    @Transactional(readOnly = true)
    public void pasar(Usuario usuario) {

        //Trallendo de la base de datos.
        log.debug("Entrando a metodo de paso de proveedores");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ProveedorFacturas> proveedores = new ArrayList<>();
        try {
            log.debug("Obteniendo datos de Oracle");
            conn = dataSource2.getConnection();
            stmt = conn.prepareStatement("select prv.ID,prv.VERSION, prv.RAZON_SOCIAL, prv.TIPOPERSONA, prv.RFC, prv.ID_FISCAL,prv.CURP,"
                    + "prv.CALLE, prv.CODIGO_POSTAL_ID, prv.TELEFONO,prv.TIPO_TERCERO_ID,prv.STATUS,prv.CLABE,prv.BANCO,prv.EMAIL "
                    + "from MATEO.CAT_PROVEEDOR prv");
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
                if (email == null || email.isEmpty()) {
                    ProveedorFacturas facturas = new ProveedorFacturas("proveedor" + id + "@prv.edu.mx", "prueba", razon_social, razon_social, razon_social,
                            "proveedor" + id + "@prv.edu.mx", razon_social, rfc, id_fiscal, curp, calle, telefono, tipo_tercero_id, clabe, banco,
                            status, clabe);
                    facturas.setId(id);
                    facturas.setEjercicio(usuario.getEjercicio());
                    facturas.setVersion(version);
                    facturas.setTipoTercero(tipo_tercero_id);
                    proveedores.add(facturas);
                } else {
                    ProveedorFacturas facturas = new ProveedorFacturas(email, "prueba", razon_social, razon_social, razon_social,
                            email, razon_social, rfc, id_fiscal, curp, calle, telefono, tipo_tercero_id, clabe, banco,
                            status, clabe);
                    facturas.setId(id);
                    facturas.setEjercicio(usuario.getEjercicio());
                    facturas.setVersion(version);
                    facturas.setTipoTercero(tipo_tercero_id);
                    proveedores.add(facturas);
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

        log.debug("Pasando datos a tabla en postgres");
        for (ProveedorFacturas proveedorFacturas : proveedores) {
            log.debug("proveedorFacturas{}", proveedorFacturas.toString());
            log.debug("proveedorFacturasCorreo{}", proveedorFacturas.getCorreo().toString());
            if (dao.obtiene(proveedorFacturas.getRfc()) == null) {
                log.debug("****insertando nuevo proveedor*****");
                dao.crea(proveedorFacturas, usuario);
            } else {
                ProveedorFacturas proveedor = dao.obtiene(proveedorFacturas.getRfc());
                log.debug("Proveedor{}", proveedor);
                if (proveedor.getCorreo() != proveedorFacturas.getCorreo()) {
                    log.debug("*****actualizando datos proveedor***");
                    proveedor.setCorreo(proveedorFacturas.getCorreo());
                    proveedor.setUsername(proveedorFacturas.getCorreo());
                    proveedor.setPassword(passwordEncoder.encodePassword(
                            "prueba", proveedorFacturas.getUsername()));
                    dao.actualiza(proveedor, usuario);

                }

            }
        }

    }
}
