/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.general.model.Proveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.redirectedUrl;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class InformeProveedorDetalleControllerTest extends BaseControllerTest {

    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de paquetes");

        ProveedorFacturas usuario = (ProveedorFacturas) obtieneProveedor();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        informeProveedor.setProveedorFacturas(usuario);
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
//     \\\\  ////
//      \\\\////
        ////\\\\
        ////  \\\\
        InformeProveedorDetalle detalle = null;
        for (int i = 0; i < 20; i++) {
            detalle = new InformeProveedorDetalle();
            detalle.setInformeProveedor(informeProveedor);
            detalle.setFechaFactura(new Date());
            detalle.setFolioFactura("1110475");
            detalle.setIVA(new BigDecimal(".16"));
            detalle.setNombreProveedor("Lala");
            detalle.setPathPDF("prueba.pdf");
            detalle.setPathXMl("prueba.xml");
            detalle.setRFCProveedor("1147hgas40q");
            detalle.setSubtotal(new BigDecimal("223"));
            detalle.setTotal(new BigDecimal("250"));
            detalle.setEmpresa(usuario.getEmpresa());
            currentSession().save(detalle);
            assertNotNull(detalle.getId());
        }


        this.mockMvc.perform(get(Constantes.PATH_INFORMEPROVEEDOR_DETALLE)
                .sessionAttr("informeId", informeProveedor)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void testContrerecibo() throws Exception {
        log.debug("Debiera mostrar lista de paquetes");

        ProveedorFacturas usuario = (ProveedorFacturas) obtieneProveedor();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        informeProveedor.setProveedorFacturas(usuario);
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
//     \\\\  ////
//      \\\\////
        ////\\\\
        ////  \\\\
        InformeProveedorDetalle detalle = null;
        for (int i = 0; i < 20; i++) {
            detalle = new InformeProveedorDetalle();
            detalle.setInformeProveedor(informeProveedor);
            detalle.setFechaFactura(new Date());
            detalle.setFolioFactura("1110475");
            detalle.setIVA(new BigDecimal(".16"));
            detalle.setNombreProveedor("Lala");
            detalle.setPathPDF("prueba.pdf");
            detalle.setPathXMl("prueba.xml");
            detalle.setRFCProveedor("1147hgas40q");
            detalle.setSubtotal(new BigDecimal("223"));
            detalle.setTotal(new BigDecimal("250"));
            detalle.setEmpresa(usuario.getEmpresa());
            currentSession().save(detalle);
            assertNotNull(detalle.getId());
        }


        this.mockMvc.perform(get(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_CONTRARECIBO)
                .sessionAttr("informeId", informeProveedor)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_CONTRARECIBO + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void testNuevo() throws Exception {
        log.debug("Test 'nuevo'");
        ProveedorFacturas usuario = (ProveedorFacturas) obtieneProveedor();
        InformeProveedor informeProveedor = null;
        for (int i = 0; i < 20; i++) {
            informeProveedor = new InformeProveedor();
            informeProveedor.setEmpresa(usuario.getEmpresa());
            informeProveedor.setFechaInforme(new Date());
            informeProveedor.setNombreProveedor("LAla");
            informeProveedor.setStatus("A");
            informeProveedor.setProveedorFacturas(usuario);
            currentSession().save(informeProveedor);
            assertNotNull(informeProveedor.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_NUEVO)
                .sessionAttr(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informeProveedor))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE));
    }

    @Test
    public void testEdita() throws Exception {
        ProveedorFacturas usuario = (ProveedorFacturas) obtieneProveedor();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        informeProveedor.setProveedorFacturas(usuario);
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
//      \\\\////
        ////\\\\
        InformeProveedorDetalle detalle = new InformeProveedorDetalle();
        detalle.setInformeProveedor(informeProveedor);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setTotal(new BigDecimal("250"));
        detalle.setEmpresa(usuario.getEmpresa());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());


        this.mockMvc.perform(get(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_EDITA + "/" + detalle.getId())
                .sessionAttr(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informeProveedor))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_INFORMESPROVEEDOR))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE, detalle));
    }

    @Test
    public void testVer() throws Exception {
        ProveedorFacturas usuario = (ProveedorFacturas) obtieneProveedor();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        informeProveedor.setProveedorFacturas(usuario);
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
//      \\\\////
        ////\\\\
        InformeProveedorDetalle detalle = new InformeProveedorDetalle();
        detalle.setInformeProveedor(informeProveedor);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setTotal(new BigDecimal("250"));
        detalle.setEmpresa(usuario.getEmpresa());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());


        this.mockMvc.perform(get(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_VER + "/" + detalle.getId())
                .sessionAttr(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informeProveedor))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE));
    }

    @Test
    public void testGraba() throws Exception {
        ProveedorFacturas usuario = (ProveedorFacturas) obtieneProveedor();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        informeProveedor.setProveedorFacturas(usuario);
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
        //      \\\\////
        ////\\\\
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_GRABA)
                .sessionAttr("informeId", informeProveedor)
                .param("fechaFactura", "21/03/2013")
                .param("folioFactura", "1110475")
                .param("iva", ".16")
                .param("nombreProveedor", "Lala")
                .param("pathPDF", "prueba.pdf")
                .param("pathXML", "prueba.xml")
                .param("rfcProveedor", "1110475xml")
                .param("subtotal", "780.16")
                .param("total", "780.16"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "detalle.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTA));
    }

    @Test
    public void testActualiza() throws Exception {
        ProveedorFacturas usuario = (ProveedorFacturas) obtieneProveedor();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        informeProveedor.setProveedorFacturas(usuario);
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
//      \\\\////
        ////\\\\
        InformeProveedorDetalle detalle = new InformeProveedorDetalle();
        detalle.setInformeProveedor(informeProveedor);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setTotal(new BigDecimal("250"));
        detalle.setEmpresa(usuario.getEmpresa());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_ACTUALIZA)
                .sessionAttr("informeId", informeProveedor)
                .param("version", detalle.getVersion().toString())
                .param("id", detalle.getId().toString())
                .param("fechaFactura", "21/03/2013")
                .param("folioFactura", "1110476")
                .param("iva", ".16")
                .param("nombreProveedor", "Lala")
                .param("pathPDF", "prueba.pdf")
                .param("pathXML", "prueba.xml")
                .param("rfcProveedor", "1110475xml")
                .param("subtotal", "780.16")
                .param("total", "780.16"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "detalle.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTA));
        currentSession().refresh(detalle);
        log.debug("{}", informeProveedor);
        assertEquals("1110476", detalle.getFolioFactura());
    }

    @Test
    public void testElimina() throws Exception {
        ProveedorFacturas usuario = (ProveedorFacturas) obtieneProveedor();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        informeProveedor.setProveedorFacturas(usuario);
        currentSession().save(informeProveedor);
        assertNotNull(informeProveedor.getId());
//      \\\\////
        ////\\\\
        InformeProveedorDetalle detalle = new InformeProveedorDetalle();
        detalle.setInformeProveedor(informeProveedor);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setEmpresa(usuario.getEmpresa());
        detalle.setTotal(new BigDecimal("250"));
        currentSession().save(detalle);
        assertNotNull(detalle.getId());
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_ELIMINA)
                .sessionAttr(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informeProveedor)
                .param("id", detalle.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "detalle.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTA));
    }
}
