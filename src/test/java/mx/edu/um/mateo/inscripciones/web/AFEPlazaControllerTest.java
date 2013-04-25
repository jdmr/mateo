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
import mx.edu.um.mateo.inscripciones.model.AFEPlaza;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;

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
public class AFEPlazaControllerTest extends BaseControllerTest {

    /**
     * Prueba la lista de AFE Convenios
     */
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de paquetes");

        Usuario usuario = obtieneUsuario();
        AFEPlaza afePlaza = null;
        for (int i = 0; i < 20; i++) {
            afePlaza = new AFEPlaza();
            afePlaza.setClave("1110475");
            afePlaza.setDias("23");
            afePlaza.setEmail("samuel.9401@gmail.com");
            afePlaza.setEmpresa(usuario.getEmpresa());
            afePlaza.setFechaAlta(new Date());
            afePlaza.setFechaModificacion(new Date());
            afePlaza.setPrimerIngreso(false);
            afePlaza.setRequisitos("Estudiante");
            afePlaza.setTipoPlaza("Ayudante General");
            afePlaza.setTurno("Matutino");
            afePlaza.setIndustrial(true);
            afePlaza.setObservaciones("prueba");
            afePlaza.setUsuarioAlta(usuario);
            afePlaza.setUsuarioModificacion(usuario);
            currentSession().save(afePlaza);
            assertNotNull(afePlaza.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_AFEPLAZA)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFEPLAZA_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_AFEPLAZAS)).
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

        this.mockMvc.perform(get(Constantes.PATH_AFEPLAZA_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFEPLAZA_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_AFEPLAZA));
    }

    /**
     * Prueba que se muestre el jsp Edita
     */
    @Test
    public void testEdita() throws Exception {
        log.debug("Test 'edita'");
        Usuario usuario = obtieneUsuario();
        AFEPlaza afePlaza = new AFEPlaza();
        afePlaza.setClave("1110475");
        afePlaza.setDias("23");
        afePlaza.setEmail("samuel.9401@gmail.com");
        afePlaza.setEmpresa(usuario.getEmpresa());
        afePlaza.setFechaAlta(new Date());
        afePlaza.setFechaModificacion(new Date());
        afePlaza.setPrimerIngreso(false);
        afePlaza.setRequisitos("Estudiante");
        afePlaza.setTipoPlaza("Ayudante General");
        afePlaza.setTurno("Matutino");
        afePlaza.setIndustrial(true);
        afePlaza.setObservaciones("prueba");
        afePlaza.setUsuarioAlta(usuario);
        afePlaza.setUsuarioModificacion(usuario);
        currentSession().save(afePlaza);
        assertNotNull(afePlaza.getId());

        this.mockMvc.perform(get(Constantes.PATH_AFEPLAZA_EDITA + "/" + afePlaza.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFEPLAZA_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_AFEPLAZA))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_AFEPLAZA, afePlaza));
    }

    /**
     * Prueba que se muestre el jsp Ver
     */
    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar paquetes");
        Usuario usuario = obtieneUsuario();
        AFEPlaza afePlaza = new AFEPlaza();
        afePlaza.setClave("1110475");
        afePlaza.setDias("23");
        afePlaza.setEmail("samuel.9401@gmail.com");
        afePlaza.setEmpresa(usuario.getEmpresa());
        afePlaza.setFechaAlta(new Date());
        afePlaza.setFechaModificacion(new Date());
        afePlaza.setPrimerIngreso(false);
        afePlaza.setRequisitos("Estudiante");
        afePlaza.setTipoPlaza("Ayudante General");
        afePlaza.setTurno("Matutino");
        afePlaza.setIndustrial(true);
        afePlaza.setObservaciones("prueba");
        afePlaza.setUsuarioAlta(usuario);
        afePlaza.setUsuarioModificacion(usuario);
        currentSession().save(afePlaza);
        assertNotNull(afePlaza.getId());

        this.mockMvc.perform(get(Constantes.PATH_AFEPLAZA_VER + "/" + afePlaza.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFEPLAZA_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_AFEPLAZA));
    }

    /**
     * Prueba que el proceso de Grabar un paquete
     */
    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear paquete");
        Usuario usuario = obtieneUsuario();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_AFEPLAZA_GRABA)
                .param("clave", "1110475")
                .param("email", "samuel.9401@gmail.com")
                .param("primerIngreso", "true")
                .param("industrial", "true")
                .param("observaciones", "prueba")
                .param("requisitos", "colportor")
                .param("tipoPlaza", "ayudanteGeneral")
                .param("turno", "matutino"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "afePlaza.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_AFEPLAZA_LISTA + "/"));
    }

    /**
     * Prueba el proceso de Actualizacion de un paquete
     */
    @Test
    public void testActualiza() throws Exception {
        log.debug("Test 'actualiza' un paquete");
        Usuario usuario = obtieneUsuario();
        AFEPlaza afePlaza = new AFEPlaza();
        afePlaza.setClave("1110475");
        afePlaza.setDias("23");
        afePlaza.setEmail("samuel.9401@gmail.com");
        afePlaza.setEmpresa(usuario.getEmpresa());
        afePlaza.setFechaAlta(new Date());
        afePlaza.setFechaModificacion(new Date());
        afePlaza.setPrimerIngreso(false);
        afePlaza.setRequisitos("Estudiante");
        afePlaza.setTipoPlaza("Ayudante General");
        afePlaza.setTurno("Matutino");
        afePlaza.setIndustrial(true);
        afePlaza.setObservaciones("prueba");
        afePlaza.setUsuarioAlta(usuario);
        afePlaza.setUsuarioModificacion(usuario);
        currentSession().save(afePlaza);
        assertNotNull(afePlaza.getId());

        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_AFEPLAZA_ACTUALIZA)
                .param("version", afePlaza.getVersion().toString())
                .param("id", afePlaza.getId().toString())
                .param("clave", "1110476")
                .param("email", "samuel.9401@gmail.com")
                .param("primerIngreso", "true")
                .param("industrial", "true")
                .param("observaciones", "prueba2")
                .param("requisitos", "colportor")
                .param("tipoPlaza", "ayudanteGeneral")
                .param("turno", "matutino"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "afePlaza.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_AFEPLAZA_LISTA));

        currentSession().refresh(afePlaza);
        log.debug("{}", afePlaza);
        assertEquals("1110476", afePlaza.getClave());
    }

    /**
     * Prueba el proceso de Borrado de un paquete
     */
    @Test
    public void testElimina() throws Exception {
        log.debug("Test 'elimina' paquete");
        Usuario usuario = obtieneUsuario();
        AFEPlaza afePlaza = new AFEPlaza();
        afePlaza.setClave("1110475");
        afePlaza.setDias("23");
        afePlaza.setEmail("samuel.9401@gmail.com");
        afePlaza.setEmpresa(usuario.getEmpresa());
        afePlaza.setFechaAlta(new Date());
        afePlaza.setFechaModificacion(new Date());
        afePlaza.setPrimerIngreso(false);
        afePlaza.setRequisitos("Estudiante");
        afePlaza.setTipoPlaza("Ayudante General");
        afePlaza.setTurno("Matutino");
        afePlaza.setIndustrial(true);
        afePlaza.setObservaciones("prueba");
        afePlaza.setUsuarioAlta(usuario);
        afePlaza.setUsuarioModificacion(usuario);
        currentSession().save(afePlaza);
        assertNotNull(afePlaza.getId());

        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_AFEPLAZA_ELIMINA)
                .param("id", afePlaza.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "afePlaza.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_AFEPLAZA_LISTA));
    }
}
