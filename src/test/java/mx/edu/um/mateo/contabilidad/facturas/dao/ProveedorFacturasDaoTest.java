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
        Usuario usuario = this.obtieneUsuario();
        ProveedorFacturas proveedorFacturas = null;
        for (int i = 0; i < 20; i++) {
            proveedorFacturas = new ProveedorFacturas("testA" + i, "TEST-01", "nombre", "appaterno", "apmaterno", "test" + i + "@prv.edu.mx",
                    "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01",
                    "TEST-01", "TEST-01", "TEST-01", "a", "TEST-01");
            proveedorFacturas.setAlmacen(usuario.getAlmacen());
            dao.crea(proveedorFacturas, usuario);
            assertNotNull(proveedorFacturas.getId());
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
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) obtieneProveedor();

        dao.crea(proveedorFacturas, proveedorFacturas);
        assertNotNull(proveedorFacturas.getId());
        ProveedorFacturas proveedorFacturas1 = dao.obtiene(proveedorFacturas.getId());
        assertEquals(proveedorFacturas.getBanco(), proveedorFacturas1.getBanco());
    }

    @Test
    public void testCreas() {
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) obtieneProveedor();

        dao.crea(proveedorFacturas, proveedorFacturas);
        assertNotNull(proveedorFacturas.getId());
        ProveedorFacturas proveedorFacturas1 = dao.obtiene(proveedorFacturas.getId());
        assertEquals(proveedorFacturas.getBanco(), proveedorFacturas1.getBanco());
    }

    @Test
    public void testActualiza() {
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) obtieneProveedor();

        dao.crea(proveedorFacturas, proveedorFacturas);
        assertNotNull(proveedorFacturas.getId());
        ProveedorFacturas proveedorFacturas1 = dao.obtiene(proveedorFacturas.getId());
        assertEquals(proveedorFacturas.getBanco(), proveedorFacturas1.getBanco());


        proveedorFacturas1.setBanco("banco1");
        dao.actualiza(proveedorFacturas1, proveedorFacturas1);

        currentSession().refresh(proveedorFacturas);
        assertEquals(proveedorFacturas.getBanco(), "banco1");

    }

    @Test
    public void testElimina() {
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) obtieneProveedor();

        dao.crea(proveedorFacturas, proveedorFacturas);
        assertNotNull(proveedorFacturas.getId());
        String rfc = dao.elimina(proveedorFacturas.getId());
        assertEquals(rfc, proveedorFacturas.getRfc());

        if ("A".equals(proveedorFacturas.getStatus())) {
            fail("Se encontro el proveedor con rfc " + rfc);
        } else {
            log.debug("Se elimino con exito el proveedor con rfc: {}", rfc);
        }

    }
}
