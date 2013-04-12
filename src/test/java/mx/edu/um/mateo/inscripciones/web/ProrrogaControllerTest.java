/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.ProrrogaDao;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.Prorroga;
import mx.edu.um.mateo.inventario.model.Almacen;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;

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
public class ProrrogaControllerTest extends BaseControllerTest {

    @Autowired
    private ProrrogaDao prorrogaDao;

    /**
     * Test of lista method, of class ProrrogaController.
     */
    @Test
    public void testLista() throws Exception {
        Usuario usuario = obtieneUsuario();
        Prorroga prorroga = null;
        for (int i = 0; i < 20; i++) {
            prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("1203.5"), "a");
            prorroga.setObservaciones("prueba");
            prorrogaDao.graba(prorroga, usuario);
            assertNotNull(prorroga.getId());
        }
        this.mockMvc.perform(get(Constantes.PATH_PRORROGA)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PRORROGA_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PRORROGAS)).
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

        this.mockMvc.perform(get(Constantes.PATH_PRORROGA_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PRORROGA_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PRORROGA));
    }

    /**
     * Test of edita method, of class ProrrogaController.
     */
    @Test
    public void testEdita() throws Exception {

        Usuario usuario = obtieneUsuario();
        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("1203.5"), "a");
        prorroga.setObservaciones("prueba");
        prorrogaDao.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());

        this.mockMvc.perform(get(Constantes.PATH_PRORROGA_EDITA + "/" + prorroga.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PRORROGA_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PRORROGA))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_PRORROGA, prorroga));
    }

    /**
     * Test of ver method, of class ProrrogaController.
     */
    @Test
    public void testVer() throws Exception {

        Usuario usuario = obtieneUsuario();
        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("1203.5"), "a");
        prorroga.setObservaciones("prueba");
        prorrogaDao.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
        this.mockMvc.perform(get(Constantes.PATH_PRORROGA_VER + "/" + prorroga.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PRORROGA_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PRORROGA));
    }

    /**
     * Prueba que el proceso de Grabar un paquete
     */
    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear paquete");
        Usuario usuario = obtieneUsuario();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_PRORROGA_GRABA)
                .param("matricula", "1110475")
                .param("fecha", "test")
                .param("fechaCompromiso", "2013-04-12")
                .param("descripcion", "2013-04-12")
                .param("saldo", "80")
                .param("userName", usuario.getUsername())
                .param("status", "A")
                .param("observaciones", "Test-Observaciones")
                .param("usuario", usuario.toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "prorroga.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PRORROGA_LISTA));
    }

    /**
     * Prueba el proceso de Actualizacion de un paquete
     */
    @Test
    public void testActualiza() throws Exception {
        log.debug("Test 'actualiza' un prorroga");
        Usuario usuario = obtieneUsuario();
        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("1203.5"), "a");
        prorroga.setObservaciones("prueba");
        prorrogaDao.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_PRORROGA_ACTUALIZA)
                .param("version", prorroga.getVersion().toString())
                .param("id", prorroga.getId().toString())
                .param("matricula", "1110475")
                .param("fecha", "2013-04-12")
                .param("fechaCompromiso", "2013-04-12")
                .param("descripcion", "text")
                .param("saldo", "80")
                .param("userName", usuario.getUsername())
                .param("status", "A")
                .param("observaciones", "Test-Observaciones")
                .param("usuario", usuario.toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "paquete.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PAQUETE_LISTA));

        currentSession().refresh(prorroga);
        log.debug("{}", prorroga);
        assertEquals("text", prorroga.getDescripcion());
    }

    /**
     * Test of elimina method, of class ProrrogaController.
     */
    @Test
    public void testElimina() throws Exception {
        Usuario usuario = obtieneUsuario();
        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("1203.5"), "a");
        prorroga.setObservaciones("prueba");
        prorrogaDao.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());

        this.mockMvc.perform(post(Constantes.PATH_PRORROGA_ELIMINA)
                .param("id", prorroga.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "prorroga.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PRORROGA_LISTA));
    }
}
