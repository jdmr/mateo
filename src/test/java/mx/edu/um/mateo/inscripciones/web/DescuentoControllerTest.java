/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.util.ArrayList;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Descuento;
import mx.edu.um.mateo.inscripciones.service.DescuentoManager;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class DescuentoControllerTest extends BaseControllerTest{
    /**
     * Prueba la lista de Descuentos
     */
    @Autowired
    DescuentoManager instance;
    
    @Test
    public void testListaDescuentos() throws Exception {
        log.debug("Debiera mostrar lista de Descuentos");
        
        Usuario usuario = obtieneUsuario();
        Descuento descuento = null;
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        for (int i = 0; i < 20; i++) {
            descuento = new Descuento();
            descuento.setDescripcion("test");
            descuento.setContabiliza("S");
            descuento.setStatus("A");
            
            descuento.setOrganizacion(organizacion);
            
            instance.graba(descuento, organizacion);
            assertNotNull(descuento.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_DESCUENTO)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DESCUENTO_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_DESCUENTOS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void testNuevoDescuento() throws Exception {
        log.debug("Test 'nuevo'");
                
        this.mockMvc.perform(get(Constantes.PATH_DESCUENTO_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DESCUENTO_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_DESCUENTO));
    }
    
    /**
     * Prueba que se muestre el jsp Edita
     */
    @Test
    public void testEditaDescuento() throws Exception {
        log.debug("Test 'edita'");
        Usuario usuario = obtieneUsuario();
        Descuento descuento = new Descuento();
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        descuento.setDescripcion("test");
        descuento.setContabiliza("S");
        descuento.setStatus("A");
        descuento.setOrganizacion(organizacion);
        instance.graba(descuento, organizacion);
        assertNotNull(descuento.getId());
                
        this.mockMvc.perform(get(Constantes.PATH_DESCUENTO_EDITA+"/"+descuento.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DESCUENTO_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_DESCUENTO))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_DESCUENTO, descuento));
    }
    
       @Test
    public void testVerDescuento() throws Exception {
        log.debug("Debiera mostrar Descuentos");
        Usuario usuario = obtieneUsuario();
        Descuento descuento = new Descuento();
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        descuento.setDescripcion("test");
        descuento.setContabiliza("S");
        descuento.setStatus("A");
        descuento.setOrganizacion(organizacion);
        instance.graba(descuento, organizacion);
        assertNotNull(descuento.getId());
        
        this.mockMvc.perform(get(Constantes.PATH_DESCUENTO_VER + "/" + descuento.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DESCUENTO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_DESCUENTO));
    }
       
        /**
     * Prueba que el proceso de Grabar un descuento
     */
    @Test
    public void testGrabaDescuento() throws Exception {
        log.debug("Debiera crear Descuento");
        Usuario usuario = obtieneUsuario();        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_DESCUENTO_GRABA)
                .param("descripcion", "test")
                .param("contabiliza","S")
                .param("status", "A")
                .param("organizacion.id", usuario.getEmpresa().getOrganizacion().getId().toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "descuento.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_DESCUENTO_LISTA+"/"));
    }
    
   /**
     * Prueba el proceso de Borrado de un descuento
     */
    @Test
    public void testEliminaDescuento() throws Exception{
        log.debug("Test 'elimina' descuento");
        Usuario usuario = obtieneUsuario();
        Descuento descuento = new Descuento();
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        descuento.setDescripcion("test");
        descuento.setContabiliza("S");
        descuento.setStatus("A");
        descuento.setOrganizacion(organizacion);
        instance.graba(descuento, organizacion);
        assertNotNull(descuento.getId());
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_DESCUENTO_ELIMINA)
                .param("id", descuento.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "descuento.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_DESCUENTO_LISTA));
    }

     /**
     * Prueba el proceso de Actualizacion de un descuento
     */
    @Test
    public void testActualizaDescuento() throws Exception {
        log.debug("Test 'actualiza' un descuento");
         Usuario usuario = obtieneUsuario();
        Descuento descuento = new Descuento();
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        descuento.setDescripcion("test");
        descuento.setContabiliza("S");
        descuento.setStatus("A");
        descuento.setOrganizacion(organizacion);
        instance.graba(descuento, organizacion);
        assertNotNull(descuento);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_DESCUENTO_GRABA)
                .param("descripcion", "test")
                .param("contabiliza","S")
                .param("status", "A")
                .param("organizacion.id", usuario.getEmpresa().getOrganizacion().getId().toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "descuento.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_DESCUENTO_LISTA+"/"));
        
        currentSession().refresh(descuento);
        log.debug("{}",descuento);
        assertEquals("test", descuento.getDescripcion());
    }
}

