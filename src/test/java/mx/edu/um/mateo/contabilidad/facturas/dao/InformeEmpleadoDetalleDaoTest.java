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
public class InformeEmpleadoDetalleDaoTest extends BaseDaoTest {

    @Autowired
    private InformeEmpleadoDetalleDao instance;

    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = new InformeEmpleado();
        informe.setEmpresa(usuario.getEmpresa());
        informe.setNumNomina("054");
        informe.setNombreEmpleado("Sam");
        informe.setFechaInforme(new Date());
        informe.setDolares(Boolean.TRUE);
        informe.setInforme(Boolean.TRUE);
        informe.setPesos(Boolean.TRUE);
        informe.setReembolso(Boolean.TRUE);
        informe.setStatus("a");
        currentSession().save(informe);
        assertNotNull(informe.getId());
        InformeEmpleadoDetalle detalle = null;
        for (int i = 0; i < 20; i++) {
            detalle = new InformeEmpleadoDetalle();
            detalle.setInformeEmpleado(informe);
            detalle.setFechaFactura(new Date());
            detalle.setFolioFactura("1110475");
            detalle.setIVA(new BigDecimal(".16"));
            detalle.setNombreProveedor("Lala");
            detalle.setPathPDF("prueba.pdf");
            detalle.setPathXMl("prueba.xml");
            detalle.setRFCProveedor("1147hgas40q");
            detalle.setSubtotal(new BigDecimal("223"));
            detalle.setTotal(new BigDecimal("250"));
            instance.crea(detalle, usuario);
            assertNotNull(detalle.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_INFORMESDETALLES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<InformeEmpleado>) result.get(Constantes.CONTAINSKEY_INFORMESDETALLES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = new InformeEmpleado();
        informe.setEmpresa(usuario.getEmpresa());
        informe.setNumNomina("054");
        informe.setNombreEmpleado("Sam");
        informe.setFechaInforme(new Date());
        informe.setDolares(Boolean.TRUE);
        informe.setInforme(Boolean.TRUE);
        informe.setPesos(Boolean.TRUE);
        informe.setReembolso(Boolean.TRUE);
        informe.setStatus("a");
        currentSession().save(informe);
        //     \\\\\\//////         
        //////\\\\\\
        InformeEmpleadoDetalle detalle = new InformeEmpleadoDetalle();
        detalle.setInformeEmpleado(informe);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setTotal(new BigDecimal("250"));
        instance.crea(detalle, usuario);
        assertNotNull(detalle.getId());

        InformeEmpleadoDetalle detalle1 = instance.obtiene(detalle.getId());
        assertEquals(detalle.getInformeEmpleado(), detalle1.getInformeEmpleado());
    }

    /**
     * Test of crea method, of class InformeEmpleadoDao.
     */
    @Test
    public void testCrea() {
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = new InformeEmpleado();
        informe.setEmpresa(usuario.getEmpresa());
        informe.setNumNomina("054");
        informe.setNombreEmpleado("Sam");
        informe.setFechaInforme(new Date());
        informe.setDolares(Boolean.TRUE);
        informe.setInforme(Boolean.TRUE);
        informe.setPesos(Boolean.TRUE);
        informe.setReembolso(Boolean.TRUE);
        informe.setStatus("a");
        currentSession().save(informe);
        //     \\\\\\//////         
        //////\\\\\\
        InformeEmpleadoDetalle detalle = new InformeEmpleadoDetalle();
        detalle.setInformeEmpleado(informe);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setTotal(new BigDecimal("250"));
        instance.crea(detalle, usuario);
        assertNotNull(detalle.getId());

        InformeEmpleadoDetalle detalle1 = instance.obtiene(detalle.getId());
        assertEquals(detalle.getInformeEmpleado(), detalle1.getInformeEmpleado());
    }

    /**
     * Test of actualiza method, of class InformeEmpleadoDao.
     */
    @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = new InformeEmpleado();
        informe.setEmpresa(usuario.getEmpresa());
        informe.setNumNomina("054");
        informe.setNombreEmpleado("Sam");
        informe.setFechaInforme(new Date());
        informe.setDolares(Boolean.TRUE);
        informe.setInforme(Boolean.TRUE);
        informe.setPesos(Boolean.TRUE);
        informe.setReembolso(Boolean.TRUE);
        informe.setStatus("a");
        currentSession().save(informe);
        //     \\\\\\//////         
        //////\\\\\\
        InformeEmpleadoDetalle detalle = new InformeEmpleadoDetalle();
        detalle.setInformeEmpleado(informe);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setTotal(new BigDecimal("250"));
        instance.crea(detalle, usuario);
        assertNotNull(detalle.getId());


        InformeEmpleadoDetalle detalle1 = instance.obtiene(detalle.getId());
        assertEquals(detalle.getInformeEmpleado(), detalle1.getInformeEmpleado());

        detalle1.setFolioFactura("1110476");
        instance.actualiza(detalle1, usuario);

        currentSession().refresh(detalle);
        assertEquals("1110476", detalle.getFolioFactura());
    }

    /**
     * Test of elimina method, of class InformeEmpleadoDao.
     */
    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = new InformeEmpleado();
        informe.setEmpresa(usuario.getEmpresa());
        informe.setNumNomina("054");
        informe.setNombreEmpleado("Sam");
        informe.setFechaInforme(new Date());
        informe.setDolares(Boolean.TRUE);
        informe.setInforme(Boolean.TRUE);
        informe.setPesos(Boolean.TRUE);
        informe.setReembolso(Boolean.TRUE);
        informe.setStatus("a");
        currentSession().save(informe);
        //     \\\\\\//////         
        //////\\\\\\
        InformeEmpleadoDetalle detalle = new InformeEmpleadoDetalle();
        detalle.setInformeEmpleado(informe);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setTotal(new BigDecimal("250"));
        instance.crea(detalle, usuario);
        assertNotNull(detalle.getId());


        String proveedor = instance.elimina(detalle.getId());
        assertEquals(proveedor, detalle.getNombreProveedor());
        InformeEmpleadoDetalle detalle1 = instance.obtiene(detalle.getId());

        if ("A".equals(detalle.getStatus())) {
            fail("Se encontro el informe " + detalle1);
        } else {
            log.debug("Se elimino con exito el informe con nomina: {}", proveedor);
        }
    }
}
