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
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;
import org.springframework.security.core.GrantedAuthority;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;

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
public class InformeEmpleadoDetalleControllerTest extends BaseControllerTest {

    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de paquetes");

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
//     \\\\  ////
//      \\\\////
        ////\\\\
        ////  \\\\
        InformeEmpleadoDetalle detalle = null;
        for (int i = 0; i < 20; i++) {
            detalle = new InformeEmpleadoDetalle();
            detalle.setInformeEmpleado(informe);
            detalle.setFechaFactura(new Date());
            detalle.setFolioFactura("1110475");
            detalle.setIVA(new BigDecimal(".16"));
            detalle.setNombreProveedor("Lala");
            detalle.setCcp("990236");
            detalle.setPathPDF("prueba.pdf");
            detalle.setPathXMl("prueba.xml");
            detalle.setRFCProveedor("1147hgas40q");
            detalle.setSubtotal(new BigDecimal("223"));
            detalle.setTotal(new BigDecimal("250"));
            detalle.setEmpresa(usuario.getEmpresa());
            currentSession().save(detalle);
            assertNotNull(detalle.getId());
        }


        this.mockMvc.perform(get(Constantes.PATH_INFORMEEMPLEADODETALLE)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEEMPLEADODETALLE_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_INFORMESDETALLES)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void testNuevo() throws Exception {
        log.debug("Test 'nuevo'");
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = null;
        for (int i = 0; i < 20; i++) {
            informe = new InformeEmpleado();
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
        }

        this.mockMvc.perform(get(Constantes.PATH_INFORMEEMPLEADODETALLE_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEEMPLEADODETALLE_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_INFORMESEMPLEADO))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEEMPLEADODETALLE));
    }

    @Test
    public void testEdita() throws Exception {
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
//      \\\\////
        ////\\\\
        InformeEmpleadoDetalle detalle = new InformeEmpleadoDetalle();
        detalle.setInformeEmpleado(informe);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setCcp("990236");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setTotal(new BigDecimal("250"));
        detalle.setEmpresa(usuario.getEmpresa());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());


        this.mockMvc.perform(get(Constantes.PATH_INFORMEEMPLEADODETALLE_EDITA + "/" + detalle.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEEMPLEADODETALLE_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_INFORMESEMPLEADO))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEEMPLEADODETALLE))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_INFORMEEMPLEADODETALLE, detalle));
    }

    @Test
    public void testVer() throws Exception {
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
//      \\\\////
        ////\\\\
        InformeEmpleadoDetalle detalle = new InformeEmpleadoDetalle();
        detalle.setInformeEmpleado(informe);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setCcp("990236");
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setTotal(new BigDecimal("250"));
        detalle.setEmpresa(usuario.getEmpresa());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());


        this.mockMvc.perform(get(Constantes.PATH_INFORMEEMPLEADODETALLE_VER + "/" + detalle.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEEMPLEADODETALLE_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEEMPLEADODETALLE));
    }

    @Test
    public void testGraba() throws Exception {
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
//      \\\\////
        ////\\\\
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_INFORMEEMPLEADODETALLE_GRABA)
                .param("fechaFactura", "21/03/2013")
                .param("folioFactura", "1110475")
                .param("iva", ".16")
                .param("ccp", "990236")
                .param("nombreProveedor", "Lala")
                .param("pathPDF", "prueba.pdf")
                .param("pathXML", "prueba.xml")
                .param("rfcProveedor", "1110475xml")
                .param("subtotal", "780.16")
                .param("total", "780.16")
                .param("informeEmpleado.Id", informe.getId().toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "detalle.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEEMPLEADODETALLE_LISTA));
    }

    @Test
    public void testActualiza() throws Exception {
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
//      \\\\////
        ////\\\\
        InformeEmpleadoDetalle detalle = new InformeEmpleadoDetalle();
        detalle.setInformeEmpleado(informe);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setCcp("990236");
        detalle.setPathXMl("prueba.xml");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setTotal(new BigDecimal("250"));
        detalle.setEmpresa(usuario.getEmpresa());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_INFORMEEMPLEADODETALLE_ACTUALIZA)
                .param("version", detalle.getVersion().toString())
                .param("id", detalle.getId().toString())
                .param("fechaFactura", "21/03/2013")
                .param("folioFactura", "1110476")
                .param("iva", ".16")
                .param("ccp", "990236")
                .param("nombreProveedor", "Lala")
                .param("pathPDF", "prueba.pdf")
                .param("pathXML", "prueba.xml")
                .param("rfcProveedor", "1110475xml")
                .param("subtotal", "780.16")
                .param("total", "780.16")
                .param("informeEmpleado.Id", informe.getId().toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "detalle.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEEMPLEADODETALLE_LISTA));
        currentSession().refresh(detalle);
        log.debug("{}", informe);
        assertEquals("1110476", detalle.getFolioFactura());
    }

    @Test
    public void testElimina() throws Exception {
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
//      \\\\////
        ////\\\\
        InformeEmpleadoDetalle detalle = new InformeEmpleadoDetalle();
        detalle.setInformeEmpleado(informe);
        detalle.setFechaFactura(new Date());
        detalle.setFolioFactura("1110475");
        detalle.setIVA(new BigDecimal(".16"));
        detalle.setNombreProveedor("Lala");
        detalle.setPathPDF("prueba.pdf");
        detalle.setPathXMl("prueba.xml");
        detalle.setCcp("990236");
        detalle.setRFCProveedor("1147hgas40q");
        detalle.setSubtotal(new BigDecimal("223"));
        detalle.setEmpresa(usuario.getEmpresa());
        detalle.setTotal(new BigDecimal("250"));
        currentSession().save(detalle);
        assertNotNull(detalle.getId());
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_INFORMEEMPLEADODETALLE_ELIMINA)
                .param("id", detalle.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "detalle.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEEMPLEADODETALLE_LISTA));
    }
}
