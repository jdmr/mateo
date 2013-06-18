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
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
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
public class InformeProveedorDetallesDaoTest extends BaseDaoTest {

    @Autowired
    private InformeProveedorDetallesDao dao;

    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
        InformeProveedorDetalle proveedorDetalle = null;
        for (int i = 0; i < 20; i++) {
            proveedorDetalle = new InformeProveedorDetalle();
            proveedorDetalle.setInformeProveedor(informeProveedor);
            proveedorDetalle.setFechaFactura(new Date());
            proveedorDetalle.setFolioFactura("1110475");
            proveedorDetalle.setIVA(new BigDecimal(".16"));
            proveedorDetalle.setNombreProveedor("Lala");
            proveedorDetalle.setPathPDF("prueba.pdf");
            proveedorDetalle.setPathXMl("prueba.xml");
            proveedorDetalle.setRFCProveedor("1147hgas40q");
            proveedorDetalle.setSubtotal(new BigDecimal("223"));
            proveedorDetalle.setTotal(new BigDecimal("250"));
            dao.crea(proveedorDetalle, usuario);
            assertNotNull(proveedorDetalle.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = dao.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<InformeEmpleado>) result.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
        //     \\\\\\//////         
        //////\\\\\\
        InformeProveedorDetalle proveedorDetalle = new InformeProveedorDetalle();
        proveedorDetalle.setInformeProveedor(informeProveedor);
        proveedorDetalle.setFechaFactura(new Date());
        proveedorDetalle.setFolioFactura("1110475");
        proveedorDetalle.setIVA(new BigDecimal(".16"));
        proveedorDetalle.setNombreProveedor("Lala");
        proveedorDetalle.setPathPDF("prueba.pdf");
        proveedorDetalle.setPathXMl("prueba.xml");
        proveedorDetalle.setRFCProveedor("1147hgas40q");
        proveedorDetalle.setSubtotal(new BigDecimal("223"));
        proveedorDetalle.setTotal(new BigDecimal("250"));
        dao.crea(proveedorDetalle, usuario);
        assertNotNull(proveedorDetalle.getId());

        InformeProveedorDetalle detalle1 = dao.obtiene(proveedorDetalle.getId());
        assertEquals(proveedorDetalle.getInformeProveedor(), detalle1.getInformeProveedor());
    }

    @Test
    public void testCrea() {
        Usuario usuario = obtieneUsuario();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
        //     \\\\\\//////         
        //////\\\\\\
        InformeProveedorDetalle proveedorDetalle = new InformeProveedorDetalle();
        proveedorDetalle.setInformeProveedor(informeProveedor);
        proveedorDetalle.setFechaFactura(new Date());
        proveedorDetalle.setFolioFactura("1110475");
        proveedorDetalle.setIVA(new BigDecimal(".16"));
        proveedorDetalle.setNombreProveedor("Lala");
        proveedorDetalle.setPathPDF("prueba.pdf");
        proveedorDetalle.setPathXMl("prueba.xml");
        proveedorDetalle.setRFCProveedor("1147hgas40q");
        proveedorDetalle.setSubtotal(new BigDecimal("223"));
        proveedorDetalle.setTotal(new BigDecimal("250"));
        dao.crea(proveedorDetalle, usuario);
        assertNotNull(proveedorDetalle.getId());

        InformeProveedorDetalle detalle1 = dao.obtiene(proveedorDetalle.getId());
        assertEquals(proveedorDetalle.getInformeProveedor(), detalle1.getInformeProveedor());
    }

    @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
        //     \\\\\\//////         
        //////\\\\\\
        InformeProveedorDetalle proveedorDetalle = new InformeProveedorDetalle();
        proveedorDetalle.setInformeProveedor(informeProveedor);
        proveedorDetalle.setFechaFactura(new Date());
        proveedorDetalle.setFolioFactura("1110475");
        proveedorDetalle.setIVA(new BigDecimal(".16"));
        proveedorDetalle.setNombreProveedor("Lala");
        proveedorDetalle.setPathPDF("prueba.pdf");
        proveedorDetalle.setPathXMl("prueba.xml");
        proveedorDetalle.setRFCProveedor("1147hgas40q");
        proveedorDetalle.setSubtotal(new BigDecimal("223"));
        proveedorDetalle.setTotal(new BigDecimal("250"));
        dao.crea(proveedorDetalle, usuario);
        assertNotNull(proveedorDetalle.getId());

        InformeProveedorDetalle detalle1 = dao.obtiene(proveedorDetalle.getId());
        assertEquals(proveedorDetalle.getInformeProveedor(), detalle1.getInformeProveedor());
        detalle1.setFolioFactura("1110476");
        dao.actualiza(detalle1, usuario);

        currentSession().refresh(proveedorDetalle);
        assertEquals("1110476", proveedorDetalle.getFolioFactura());
    }

    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
        //     \\\\\\//////         
        //////\\\\\\
        InformeProveedorDetalle proveedorDetalle = new InformeProveedorDetalle();
        proveedorDetalle.setInformeProveedor(informeProveedor);
        proveedorDetalle.setFechaFactura(new Date());
        proveedorDetalle.setFolioFactura("1110475");
        proveedorDetalle.setIVA(new BigDecimal(".16"));
        proveedorDetalle.setNombreProveedor("Lala");
        proveedorDetalle.setPathPDF("prueba.pdf");
        proveedorDetalle.setPathXMl("prueba.xml");
        proveedorDetalle.setRFCProveedor("1147hgas40q");
        proveedorDetalle.setSubtotal(new BigDecimal("223"));
        proveedorDetalle.setTotal(new BigDecimal("250"));
        dao.crea(proveedorDetalle, usuario);
        assertNotNull(proveedorDetalle.getId());

        String proveedor = dao.elimina(proveedorDetalle.getId());
        assertEquals(proveedor, proveedorDetalle.getNombreProveedor());
        InformeProveedorDetalle detalle1 = dao.obtiene(proveedorDetalle.getId());

        if ("A".equals(proveedorDetalle.getStatus())) {
            fail("Se encontro el informe " + detalle1);
        } else {
            log.debug("Se elimino con exito el informe con nomina: {}", proveedor);
        }
    }
}
