package mx.edu.um.mateo.inscripciones.web;

import java.util.ArrayList;
import java.util.Date;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Periodo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class PeriodoControllerTest extends BaseControllerTest {

    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de periodos");
        Usuario usuario = obtieneUsuario();
        
        Periodo periodo = null;
        for(int i=0; i<20; i++){
            periodo = new Periodo();
            periodo.setDescripcion("TEST-1");
            periodo.setStatus("A");
            periodo.setClave("clave");
            periodo.setFechaInicial(new Date());
            periodo.setFechaFinal(new Date());
            periodo.setOrganizacion(usuario.getEmpresa().getOrganizacion());
            currentSession().save(periodo);
            assertNotNull(periodo.getId());
        }
        
        this.mockMvc.perform(
                get(Constantes.PATH_PERIODOS))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PERIODOS_LISTA + ".jsp"))
                .andExpect(model().attributeExists("periodo"))
                .andExpect(model().attributeExists("paginacion"))
                .andExpect(model().attributeExists("paginas"))
                .andExpect(model().attributeExists("pagina"));
    }

    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar periodos");
        Usuario usuario = obtieneUsuario();
        Periodo periodo = new Periodo();
        periodo.setDescripcion("TEST-1");
        periodo.setStatus("A");
        periodo.setClave("clave");
        periodo.setFechaInicial(new Date());
        periodo.setFechaFinal(new Date());
        periodo.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(periodo);
        assertNotNull(periodo.getId());

        this.mockMvc.perform(get(Constantes.PATH_PERIODOS_VER + "/" + periodo.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PERIODOS_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PERIODOS));

    }

    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear periodo");
        Usuario usuario = obtieneUsuario();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_PERIODOS_GRABA)
                .param("descripcion", "TEST-1")
                .param("clave", "clave")
                .param("status", "A")
                .param("fechaInicial", "12/12/12")
                .param("fechaFinal", "12/12/12"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "periodo.creado.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PERIODOS));
    }

    @Test
    public void testEliminar() throws Exception {
        log.debug("Debiera eliminar periodo");
        Usuario usuario = obtieneUsuario();
        Periodo periodo = new Periodo();
        periodo.setDescripcion("TEST-1");
        periodo.setStatus("A");
        periodo.setClave("clave");
        periodo.setFechaInicial(new Date());
        periodo.setFechaFinal(new Date());
        periodo.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(periodo);
        assertNotNull(periodo);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_PERIODOS_ELIMINA)
                .param("id", periodo.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "periodo.eliminado.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PERIODOS));
    }

    @Test
    public void testActualizar() throws Exception {
        log.debug("Debiera actualizar periodo");
        Usuario usuario = obtieneUsuario();
        
        Periodo periodo = new Periodo();
        periodo.setDescripcion("TEST-1");
        periodo.setStatus("A");
        periodo.setClave("clave");
        periodo.setFechaInicial(new Date());
        periodo.setFechaFinal(new Date());
        periodo.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(periodo);
        assertNotNull(periodo);

        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_PERIODOS_GRABA)
                .param("descripcion", "TEST-1")
                .param("clave", "clave")
                .param("status", "A")
                .param("fechaInicial", "12/12/12")
                .param("fechaFinal", "12/12/12")
                .param("id", periodo.getId().toString())
                .param("version", periodo.getVersion().toString()))
                .andExpect(redirectedUrl(Constantes.PATH_PERIODOS))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "periodo.actualizado.message"));
        
        currentSession().refresh(periodo);
        log.debug("{}",periodo);
        assertEquals("TEST-1", periodo.getDescripcion());
    }

}
