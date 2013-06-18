/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

import java.util.ArrayList;
import java.util.Date;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
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
public class InformeEmpleadoControllerTest extends BaseControllerTest {

    /**
     * Prueba la lista de paquetes
     */
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de paquetes");

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

        this.mockMvc.perform(get(Constantes.PATH_INFORMEEMPLEADO)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEEMPLEADO_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_INFORMESEMPLEADO)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    /**
     * Prueba que se muestre el jsp Nuevo
     */
    @Test
    public void testNuevo() throws Exception {
        log.debug("Test 'nuevo'");

        this.mockMvc.perform(get(Constantes.PATH_INFORMEEMPLEADO_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEEMPLEADO_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEEMPLEADO));
    }

    /**
     * Prueba que se muestre el jsp Edita
     */
    @Test
    public void testEdita() throws Exception {
        log.debug("Test 'edita'");
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
        this.mockMvc.perform(get(Constantes.PATH_INFORMEEMPLEADO_EDITA + "/" + informe.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEEMPLEADO_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEEMPLEADO))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_INFORMEEMPLEADO, informe));
    }

    /**
     * Prueba que se muestre el jsp Ver
     */
    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar paquetes");
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

        this.mockMvc.perform(get(Constantes.PATH_INFORMEEMPLEADO_VER + "/" + informe.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INFORMEEMPLEADO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_INFORMEEMPLEADO));
    }

    /**
     * Prueba que el proceso de Grabar un paquete
     */
    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear paquete");
        Usuario usuario = obtieneUsuario();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));


        this.mockMvc.perform(post(Constantes.PATH_INFORMEEMPLEADO_GRABA)
                .param("numNomina", "0575")
                .param("nombreEmpleado", "Sam")
                .param("fechaInforme", "4/06/2013")
                .param("dolares", "true")
                .param("informe", "true")
                .param("pesos", "true")
                .param("reembolso", "true")
                .param("status", "a"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "informe.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEEMPLEADO_LISTA));
    }

    /**
     * Test of elimina method, of class ProrrogaController.
     */
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

        this.mockMvc.perform(post(Constantes.PATH_INFORMEEMPLEADO_ELIMINA)
                .param("id", informe.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "informe.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_INFORMEEMPLEADO_LISTA));
    }
}
