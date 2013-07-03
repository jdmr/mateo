/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ProveedorFacturasDaoTest extends BaseDaoTest {

    @Autowired
    private ProveedorFacturasDao dao;

    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        ProveedorFacturas proveedorFacturas = null;
        for (int i = 0; i < 20; i++) {
            proveedorFacturas = new ProveedorFacturas();
            proveedorFacturas.setBanco("banco");
            proveedorFacturas.setCURP("curp");
            proveedorFacturas.setClabe("clabe");
            proveedorFacturas.setCuentaCheque("cuentaCheque");
            proveedorFacturas.setDireccion("direccion");
            proveedorFacturas.setCorreo("proveedor@proveedor.com");
            proveedorFacturas.setIdFiscal("idfiscal");
            proveedorFacturas.setTipoTercero("tipotercero");
            proveedorFacturas.setTelefono("8261368265");
            proveedorFacturas.setStatus("a");
            proveedorFacturas.setRfc("rfc");
            proveedorFacturas.setRazonSocial("razonSocial");
            dao.crea(proveedorFacturas, usuario);
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = dao.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_PROVEEDORESFACTURAS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<InformeEmpleado>) result.get(Constantes.CONTAINSKEY_PROVEEDORESFACTURAS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        ProveedorFacturas proveedorFacturas = new ProveedorFacturas();
        proveedorFacturas.setBanco("banco");
        proveedorFacturas.setCURP("curp");
        proveedorFacturas.setClabe("clabe");
        proveedorFacturas.setCuentaCheque("cuentaCheque");
        proveedorFacturas.setDireccion("direccion");
        proveedorFacturas.setCorreo("proveedor@proveedor.com");
        proveedorFacturas.setIdFiscal("idfiscal");
        proveedorFacturas.setTipoTercero("tipotercero");
        proveedorFacturas.setTelefono("8261368265");
        proveedorFacturas.setStatus("a");
        proveedorFacturas.setRfc("rfc");
        proveedorFacturas.setRazonSocial("razonSocial");
        dao.crea(proveedorFacturas, usuario);
        ProveedorFacturas proveedorFacturas1 = dao.obtiene(proveedorFacturas.getId());
        assertEquals(proveedorFacturas.getBanco(), proveedorFacturas1.getBanco());
    }

    public void testCrea() {
        Usuario usuario = obtieneUsuario();
        ProveedorFacturas proveedorFacturas = new ProveedorFacturas();
        proveedorFacturas.setBanco("banco");
        proveedorFacturas.setCURP("curp");
        proveedorFacturas.setClabe("clabe");
        proveedorFacturas.setCuentaCheque("cuentaCheque");
        proveedorFacturas.setDireccion("direccion");
        proveedorFacturas.setCorreo("proveedor@proveedor.com");
        proveedorFacturas.setIdFiscal("idfiscal");
        proveedorFacturas.setTipoTercero("tipotercero");
        proveedorFacturas.setTelefono("8261368265");
        proveedorFacturas.setStatus("a");
        proveedorFacturas.setRfc("rfc");
        proveedorFacturas.setRazonSocial("razonSocial");
        dao.crea(proveedorFacturas, usuario);

        ProveedorFacturas proveedorFacturas1 = dao.obtiene(proveedorFacturas.getId());
        assertEquals(proveedorFacturas.getBanco(), proveedorFacturas1.getBanco());
        proveedorFacturas1.setBanco("banco1");
        dao.actualiza(proveedorFacturas1, usuario);

        currentSession().refresh(proveedorFacturas);
        assertEquals(proveedorFacturas.getBanco(), "banco1");

    }

    public void testElimina() {
        Usuario usuario = obtieneUsuario();
        ProveedorFacturas proveedorFacturas = new ProveedorFacturas();
        proveedorFacturas.setBanco("banco");
        proveedorFacturas.setCURP("curp");
        proveedorFacturas.setClabe("clabe");
        proveedorFacturas.setCuentaCheque("cuentaCheque");
        proveedorFacturas.setDireccion("direccion");
        proveedorFacturas.setCorreo("proveedor@proveedor.com");
        proveedorFacturas.setIdFiscal("idfiscal");
        proveedorFacturas.setTipoTercero("tipotercero");
        proveedorFacturas.setTelefono("8261368265");
        proveedorFacturas.setStatus("A");
        proveedorFacturas.setRfc("rfc");
        proveedorFacturas.setRazonSocial("razonSocial");
        dao.crea(proveedorFacturas, usuario);

        String rfc = dao.elimina(proveedorFacturas.getId());
        assertEquals(rfc, proveedorFacturas.getRfc());

        if ("A".equals(proveedorFacturas.getStatus())) {
            fail("Se encontro el proveedor con rfc " + rfc);
        } else {
            log.debug("Se elimino con exito el proveedor con rfc: {}", rfc);
        }

    }
}
