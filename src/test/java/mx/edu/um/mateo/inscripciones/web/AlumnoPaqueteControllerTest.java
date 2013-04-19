/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AlumnoPaquete;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inventario.model.Almacen;
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
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class AlumnoPaqueteControllerTest extends BaseControllerTest{
    
    @Test
    public void testListaDeAlumnoPaquete() throws Exception {
        log.debug("test mostrar lista de AlumnoPaquete");
        Usuario usuario = obtieneUsuario();
        Paquete paquete = null;
        for (int i = 0; i < 20; i++) {
            paquete = new Paquete();
            paquete.setAcfe("a");
            paquete.setDescripcion("test");
            paquete.setEmpresa(usuario.getEmpresa());
            paquete.setEnsenanza(new BigDecimal("80"));
            paquete.setInternado(new BigDecimal("80"));
            paquete.setMatricula(new BigDecimal("80"));
            paquete.setNombre("test");
            currentSession().save(paquete);
            assertNotNull(paquete.getId());
        }
        this.mockMvc.perform(
                get(Constantes.PATH_ALUMNOPAQUETE))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/inscripciones/alumnoPaquete/lista.jsp"))
                .andExpect(model().attributeExists("alumnoPaquetes"))
                .andExpect(model().attributeExists("paginacion"))
                .andExpect(model().attributeExists("paginas"))
                .andExpect(model().attributeExists("pagina"));
    }
    
    @Test
    public void testEditaAlumnoPaquete() throws Exception {
        Usuario usuario = obtieneUsuario();
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        currentSession().save(alumnoPaquete);
        assertNotNull(alumnoPaquete.getId());
        Long id = alumnoPaquete.getId();
        this.mockMvc.perform(
                get(Constantes.PATH_ALUMNOPAQUETE_EDITA + "/" + id))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ALUMNOPAQUETE_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAQUETES))
                .andExpect(model().attributeExists("alumnoPaquete"));
    }
    
    @Test
    public void testNuevoDeAlumnoPaquete() throws Exception {
        log.debug("test Nuevo de AlumnoPaquete");
        this.mockMvc.perform(
                get(Constantes.PATH_ALUMNOPAQUETE_NUEVO))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ALUMNOPAQUETE_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAQUETES))
                .andExpect(model().attributeExists("alumnoPaquete"));
    }
    
     @Test
    public void testVerAlumnoPaquete() throws Exception {
        log.debug("test ver alumnoPaquete");
        Usuario usuario = obtieneUsuario();
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        currentSession().save(alumnoPaquete);
        assertNotNull(alumnoPaquete.getId());
        Long id = alumnoPaquete.getId();
        
        this.mockMvc.perform(get("/" + Constantes.PATH_ALUMNOPAQUETE_VER + "/" + id))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/inscripciones/alumnoPaquete/ver.jsp"))
                .andExpect(model().attributeExists("alumnoPaquete"));
        
    }
    
    @Test
    public void testCreaAlumnoPaquete() throws Exception {
        Usuario usuario = obtieneUsuario();
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        currentSession().save(alumnoPaquete);
        assertNotNull(alumnoPaquete.getId());
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_ALUMNOPAQUETE_GRABA)
                .param("paquete.id", paquete.getId().toString())
                .param("matricula", "1110475")
                .param("status", "A"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "alumnoPaquete.creado.message"));
    }
    
     @Test
     public void testActualizaAlumnoPaquete() throws Exception {
        Usuario usuario = obtieneUsuario();
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        currentSession().save(alumnoPaquete);
        assertNotNull(alumnoPaquete.getId());
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_ALUMNOPAQUETE_GRABA)
                .param("paquete.id", paquete.getId().toString())
                .param("matricula", "1090687")
                .param("status", "A")
                .param("version", alumnoPaquete.getVersion().toString())
                .param("id", alumnoPaquete.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "alumnoPaquete.actualizado.message"));
        
        currentSession().refresh(alumnoPaquete);
        log.debug("{}",alumnoPaquete);
        assertEquals("1090687", alumnoPaquete.getMatricula());
    }
    
     @Test
    public void testEliminarAlumnoPaquete() throws Exception {
        log.debug("test eliminar alumnoPaquete");
       Usuario usuario = obtieneUsuario();
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        currentSession().save(alumnoPaquete);
        assertNotNull(alumnoPaquete.getId());

        this.mockMvc.perform(post(Constantes.PATH_ALUMNOPAQUETE_ELIMINA)
                .param("id", alumnoPaquete.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "alumnoPaquete.eliminado.message"))
                .andExpect(redirectedUrl("/"+Constantes.PATH_ALUMNOPAQUETE_LISTA));
   }
}
