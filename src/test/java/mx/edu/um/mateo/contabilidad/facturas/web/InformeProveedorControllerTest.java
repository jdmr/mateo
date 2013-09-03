/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorManager;
import mx.edu.um.mateo.general.model.Proveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
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
public class InformeProveedorControllerTest extends BaseControllerTest {

    @Autowired
    private InformeProveedorManager manager;

    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de informes de proveedor");

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
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(get(Constantes.PATH_INFORMEPROVEEDOR)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEPROVEEDOR_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_INFORMESPROVEEDOR)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void testNuevo() throws Exception {
        log.debug("Test 'nuevo'");
        ProveedorFacturas usuario = (ProveedorFacturas) obtieneProveedor();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(get(Constantes.PATH_INFORMEPROVEEDOR_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEPROVEEDOR_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR));
    }

    @Test
    public void testEdita() throws Exception {
        log.debug("Test 'edita'");
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
        this.mockMvc.perform(get(Constantes.PATH_INFORMEPROVEEDOR_EDITA + "/" + informeProveedor.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEPROVEEDOR_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informeProveedor));
    }

    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar paquetes");
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
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(get(Constantes.PATH_INFORMEPROVEEDOR_VER + "/" + informeProveedor.getId())
                .sessionAttr("proveedorFacturas", usuario))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTA))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR));
    }

    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear paquete");
        ProveedorFacturas usuario = (ProveedorFacturas) obtieneProveedor();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));


        this.mockMvc.perform(post(Constantes.PATH_INFORMEPROVEEDOR_GRABA)
                .param("nombreProveedor", "0575")
                .param("fechaInforme", "4/06/2013")
                .param("proveedorFacturasId", usuario.getId().toString())
                .param("status", "a")
                .param("formaPago", "T")
                .param("cuentaCheque", "145272")
                .param("banco", "banamex")
                .param("moneda", "p")
                .param("ccp", "990236")
                .param("clabe", "145272"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "informeProveedor.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEPROVEEDOR_LISTA));
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

        this.mockMvc.perform(post(Constantes.PATH_INFORMEPROVEEDOR_ELIMINA)
                .param("id", informeProveedor.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "informeProveedor.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEPROVEEDOR_LISTA));
    }

    @Test
    public void testFinaliza() throws Exception {
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
        InformeProveedorDetalle detalle = null;
        for (int i = 0; i < 4; i++) {
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
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(get(Constantes.PATH_INFORMEPROVEEDOR_FINALIZA)
                .param("id", informeProveedor.getId().toString())
                .sessionAttr("informeId", informeProveedor)
                .sessionAttr("proveedor", informeProveedor))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "informeProveedor.finaliza.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEPROVEEDOR_LISTA));
        currentSession().refresh(informeProveedor);
        InformeProveedor informeProveedor1 = manager.obtiene(informeProveedor.getId());
        log.debug("informe...**{}", informeProveedor1);
        assertEquals(Constantes.STATUS_FINALIZADO, informeProveedor1.getStatus());
    }
}
