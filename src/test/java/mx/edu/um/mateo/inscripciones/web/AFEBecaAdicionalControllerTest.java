/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AFEBecaAdicional;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
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
public class AFEBecaAdicionalControllerTest extends BaseControllerTest {

    /**
     * Prueba la lista de paquetes
     */
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de paquetes");
        Usuario usuario = obtieneUsuario();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tiposBecas);

        AFEBecaAdicional becaAdicional = null;

        for (int i = 0; i < 20; i++) {
            becaAdicional = new AFEBecaAdicional();
            becaAdicional.setEmpresa(usuario.getEmpresa());
            becaAdicional.setFechaAlta(new Date());
            becaAdicional.setImporte(new BigDecimal("20.8"));
            becaAdicional.setMatricula("111047" + i);
            becaAdicional.setStatus("A");
            becaAdicional.setUsuarioAlta(usuario);
            becaAdicional.setTiposBecas(tiposBecas);
            currentSession().save(becaAdicional);
            assertNotNull(becaAdicional.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_AFE_BECAADICIONAL)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFE_BECAADICIONAL_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_BECASADICIONALES)).
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
        Usuario usuario = obtieneUsuario();
        TiposBecas tiposBecas = null;
        for (int i = 0; i < 20; i++) {
            tiposBecas = new TiposBecas();
            tiposBecas.setDescripcion("test");
            tiposBecas.setDiezma(true);
            tiposBecas.setNumHoras(320);
            tiposBecas.setPerteneceAlumno(false);
            tiposBecas.setPorcentaje(new BigDecimal(320));
            tiposBecas.setSoloPostgrado(false);
            tiposBecas.setStatus("a");
            tiposBecas.setTope(new BigDecimal(350));
            tiposBecas.setEmpresa(usuario.getEmpresa());
            currentSession().save(tiposBecas);
        }
        this.mockMvc.perform(get(Constantes.PATH_AFE_BECAADICIONAL_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFE_BECAADICIONAL_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_TIPOSBECAS))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_BECAADICIONAL));
    }

    /**
     * Prueba que se muestre el jsp Edita
     */
    @Test
    public void testEdita() throws Exception {
        log.debug("Test 'edita'");
        Usuario usuario = obtieneUsuario();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tiposBecas);

        AFEBecaAdicional becaAdicional = new AFEBecaAdicional();
        becaAdicional.setEmpresa(usuario.getEmpresa());
        becaAdicional.setFechaAlta(new Date());
        becaAdicional.setImporte(new BigDecimal("20.8"));
        becaAdicional.setMatricula("1110475");
        becaAdicional.setStatus("A");
        becaAdicional.setUsuarioAlta(usuario);
        becaAdicional.setTiposBecas(tiposBecas);
        currentSession().save(becaAdicional);
        assertNotNull(becaAdicional.getId());
        this.mockMvc.perform(get(Constantes.PATH_AFE_BECAADICIONAL_EDITA + "/" + becaAdicional.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFE_BECAADICIONAL_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_TIPOSBECAS))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_BECAADICIONAL))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_BECAADICIONAL, becaAdicional));
    }

    /**
     * Prueba que se muestre el jsp Ver
     */
    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar paquetes");
        Usuario usuario = obtieneUsuario();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tiposBecas);

        AFEBecaAdicional becaAdicional = new AFEBecaAdicional();
        becaAdicional.setEmpresa(usuario.getEmpresa());
        becaAdicional.setFechaAlta(new Date());
        becaAdicional.setImporte(new BigDecimal("20.8"));
        becaAdicional.setMatricula("1110475");
        becaAdicional.setStatus("A");
        becaAdicional.setUsuarioAlta(usuario);
        becaAdicional.setTiposBecas(tiposBecas);
        currentSession().save(becaAdicional);
        assertNotNull(becaAdicional.getId());

        this.mockMvc.perform(get(Constantes.PATH_AFE_BECAADICIONAL_VER + "/" + becaAdicional.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFE_BECAADICIONAL_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_BECAADICIONAL));
    }

    /**
     * Prueba que el proceso de Grabar un paquete
     */
    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear paquete");
        Usuario usuario = obtieneUsuario();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tiposBecas);
        this.mockMvc.perform(post(Constantes.PATH_AFE_BECAADICIONAL_GRABA)
                .param("matricula", "1110475")
                .param("importe", "89.6")
                .param("status", "A")
                .param("tiposBecas", tiposBecas.getId().toString())
                .param("usuarioAlta", usuario.getId().toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "becaAdicional.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_AFE_BECAADICIONAL_LISTA));
    }

    /**
     * Prueba el proceso de Actualizacion de un paquete
     */
    @Test
    public void testActualiza() throws Exception {
        log.debug("Test 'actualiza' un paquete");
        Usuario usuario = obtieneUsuario();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tiposBecas);

        AFEBecaAdicional becaAdicional = new AFEBecaAdicional();
        becaAdicional.setEmpresa(usuario.getEmpresa());
        becaAdicional.setFechaAlta(new Date());
        becaAdicional.setImporte(new BigDecimal("20.8"));
        becaAdicional.setMatricula("1110475");
        becaAdicional.setStatus("A");
        becaAdicional.setUsuarioAlta(usuario);
        becaAdicional.setTiposBecas(tiposBecas);
        currentSession().save(becaAdicional);
        assertNotNull(becaAdicional.getId());

        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_PAQUETE_ACTUALIZA)
                .param("version", becaAdicional.getVersion().toString())
                .param("id", becaAdicional.getId().toString())
                .param("matricula", "1110476")
                .param("importe", "89.6")
                .param("status", "A")
                .param("tiposBecas", tiposBecas.getId().toString())
                .param("usuarioAlta", usuario.getId().toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "paquete.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PAQUETE_LISTA));

        currentSession().refresh(becaAdicional);
        log.debug("{}", becaAdicional);
        assertEquals("1110476", becaAdicional.getMatricula());
    }

    /**
     * Prueba el proceso de Borrado de un paquete
     */
    @Test
    public void testElimina() throws Exception {
        log.debug("Test 'elimina' paquete");
        Usuario usuario = obtieneUsuario();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tiposBecas);

        AFEBecaAdicional becaAdicional = new AFEBecaAdicional();
        becaAdicional.setEmpresa(usuario.getEmpresa());
        becaAdicional.setFechaAlta(new Date());
        becaAdicional.setImporte(new BigDecimal("20.8"));
        becaAdicional.setMatricula("1110475");
        becaAdicional.setStatus("A");
        becaAdicional.setUsuarioAlta(usuario);
        becaAdicional.setTiposBecas(tiposBecas);
        currentSession().save(becaAdicional);
        assertNotNull(becaAdicional.getId());

        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_AFE_BECAADICIONAL_ELIMINA)
                .param("id", becaAdicional.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "paquete.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_AFE_BECAADICIONAL_LISTA));
    }
}
