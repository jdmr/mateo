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
import mx.edu.um.mateo.inscripciones.model.AFETipoDescuento;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
public class AFETipoDescuentoControllerTest extends BaseControllerTest{
   
    /**
     * Prueba la lista de tipos de descuentos
     */
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de paquetes");
        
        Usuario usuario = obtieneUsuario();
        AFETipoDescuento afeTipoDescuento;
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        for (int i = 0; i < 20; i++) {
            afeTipoDescuento= new AFETipoDescuento("tipoDescuento","A", organizacion);
            currentSession().save(afeTipoDescuento);
            assertNotNull(afeTipoDescuento.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_TIPODESCUENTO)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPODESCUENTO_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_TIPODESCUENTOS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    /**
     * Prueba que se muestre el jsp Nuevo
     */
    @Test
    public void testNuevo() throws Exception {
        log.debug("Test 'nuevo' tipo Descuento");
                
        this.mockMvc.perform(get(Constantes.PATH_TIPODESCUENTO_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPODESCUENTO_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_TIPODESCUENTO));
    }
    
    /**
     * Prueba que se muestre el jsp Edita
     */
    @Test
    public void testEdita() throws Exception {
        log.debug("Test 'edita'");
        Usuario usuario = obtieneUsuario();
        AFETipoDescuento afeTipoDescuento;
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        afeTipoDescuento= new AFETipoDescuento("tipoDescuento","A", organizacion);
        currentSession().save(afeTipoDescuento);
        assertNotNull(afeTipoDescuento);
                
        this.mockMvc.perform(get(Constantes.PATH_TIPODESCUENTO_EDITA+"/"+afeTipoDescuento.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPODESCUENTO_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_TIPODESCUENTO))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_TIPODESCUENTO, afeTipoDescuento));
    }
    
    
    /**
     * Prueba que se muestre el jsp Ver
     */
    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar paquetes");
        Usuario usuario = obtieneUsuario();        
        AFETipoDescuento afeTipoDescuento;
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        afeTipoDescuento= new AFETipoDescuento("tipoDescuento","A", organizacion);
        currentSession().save(afeTipoDescuento);
        assertNotNull(afeTipoDescuento);
        
        this.mockMvc.perform(get(Constantes.PATH_TIPODESCUENTO_VER + "/" + afeTipoDescuento.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPODESCUENTO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_TIPODESCUENTO));
    }

    /**
     * Prueba que el proceso de Grabar un tipo de descuento
     */
    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear un tipo de Descuento");
        Usuario usuario = obtieneUsuario();        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_TIPODESCUENTO_GRABA)
                .param("descripcion", "test")
                .param("status", "A"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "tipoDescuento.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_TIPODESCUENTO_LISTA+"/"));
    }
    
    /**
     * Prueba el proceso de Actualizacion de un tipo de descuento
     */
    @Test
    public void testActualiza() throws Exception {
        log.debug("Test 'actualiza' un paquete");
        Usuario usuario = obtieneUsuario();        
        AFETipoDescuento afeTipoDescuento;
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        afeTipoDescuento= new AFETipoDescuento("tipoDescuento","A", organizacion);
        currentSession().save(afeTipoDescuento);
        assertNotNull(afeTipoDescuento);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_TIPODESCUENTO_GRABA)
                .param("version", afeTipoDescuento.getVersion().toString())
                .param("id", afeTipoDescuento.getId().toString())
                .param("descripcion", "tipoDescuento222")
                .param("status", "A"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "tipoDescuento.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_TIPODESCUENTO_LISTA+"/"));
        
        currentSession().refresh(afeTipoDescuento);
        log.debug("{}",afeTipoDescuento);
        assertEquals("tipoDescuento222", afeTipoDescuento.getDescripcion());
    }
    /**
     * Prueba el proceso de Borrado de un tipo Descuento
     */
    @Test
    public void testElimina() throws Exception{
        log.debug("Test 'elimina' paquete");
        Usuario usuario = obtieneUsuario();        
        AFETipoDescuento afeTipoDescuento;
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        afeTipoDescuento= new AFETipoDescuento("tipoDescuento","A", organizacion);
        currentSession().save(afeTipoDescuento);
        assertNotNull(afeTipoDescuento);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_TIPODESCUENTO_ELIMINA)
                .param("id", afeTipoDescuento.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "tipoDescuento.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_TIPODESCUENTO_LISTA));
    }
}
