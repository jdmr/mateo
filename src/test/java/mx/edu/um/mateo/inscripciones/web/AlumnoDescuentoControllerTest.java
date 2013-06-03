/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.util.ArrayList;
import java.util.Date;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AlumnoDescuento;
import mx.edu.um.mateo.inscripciones.model.Descuento;
import mx.edu.um.mateo.inscripciones.service.AlumnoDescuentoManager;
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
public class AlumnoDescuentoControllerTest extends BaseControllerTest{
    @Autowired
    AlumnoDescuentoManager instance;
    
    
    /**
     * Prueba que se muestre la Lista
     */
    
     @Test
    public void testListaAlumnoDescuentos() throws Exception {
        log.debug("Debiera mostrar lista de Descuentos de alumnos");
        
            Usuario usuario= obtieneUsuario();
            Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
            Descuento descuento= new Descuento("Descuento","A","S", organizacion);
            currentSession().save(descuento);
            AlumnoDescuento alumnoDescuento;
        for (int i = 0; i < 20; i++) {
            alumnoDescuento= new AlumnoDescuento("1080506",descuento,new Date(), usuario,"S","A");
                instance.graba(alumnoDescuento, usuario);
                assertNotNull(alumnoDescuento.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_ALUMNODESCUENTO)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ALUMNODESCUENTO_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_ALUMNODESCUENTOS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
     
     /**
     * Prueba que se muestre el jsp Nuevo
     */
    
     @Test
    public void testNuevoAlumnoDescuento() throws Exception {
        log.debug("Test 'nuevo'");
                
        this.mockMvc.perform(get(Constantes.PATH_ALUMNODESCUENTO_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ALUMNODESCUENTO_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_ALUMNODESCUENTO));
    }
    
     /**
     * Prueba que se muestre el jsp Edita
     */
    @Test
    public void testEditaAlumnoDescuento() throws Exception {
        Usuario usuario= obtieneUsuario();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        Descuento descuento= new Descuento("Descuento","A","S", organizacion);
        currentSession().save(descuento);
        AlumnoDescuento alumnoDescuento= new AlumnoDescuento("1080506",descuento,new Date(), usuario,"S","A");
        instance.graba(alumnoDescuento, usuario);
        assertNotNull(alumnoDescuento.getId());
                
        this.mockMvc.perform(get(Constantes.PATH_ALUMNODESCUENTO_EDITA+"/"+alumnoDescuento.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ALUMNODESCUENTO_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_ALUMNODESCUENTO))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_ALUMNODESCUENTO, alumnoDescuento));
    }
    
    
    @Test
    public void testVerAlumnoDescuento() throws Exception {
        log.debug("Debiera mostrar Descuentos de Alumnos");
        Usuario usuario= obtieneUsuario();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        Descuento descuento= new Descuento("Descuento","A","S", organizacion);
        currentSession().save(descuento);
        AlumnoDescuento alumnoDescuento= new AlumnoDescuento("1080506",descuento,new Date(), usuario,"S","A");
        instance.graba(alumnoDescuento, usuario);
        assertNotNull(alumnoDescuento.getId());
        
        this.mockMvc.perform(get(Constantes.PATH_ALUMNODESCUENTO_VER + "/" + alumnoDescuento.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ALUMNODESCUENTO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_ALUMNODESCUENTO));
    }
    
    
       /**
     * Prueba que el proceso de Grabar un descuento de alumno
     */
    @Test
    public void testGrabaAlumnodDescuento() throws Exception {
        log.debug("Debiera crear Descuento de Alumno");
        Usuario usuario = obtieneUsuario();      
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        Descuento descuento= new Descuento("Descuento","A","S", organizacion);
        currentSession().save(descuento);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_ALUMNODESCUENTO_GRABA)
                .param("matri1cula", "1080506")
                .param("contabiliza","S")
                .param("status", "A")
                .param("fecha", "12/10/2012")
                .param("descuento.id", descuento.getId().toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "alumnoDescuento.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_ALUMNODESCUENTO_LISTA+"/"));
    }
       
    
    /**
     * Prueba el proceso de Borrado de un descuento de un Alumno
     */
    @Test
    public void testEliminaAlumnoDescuento() throws Exception{
        log.debug("Test 'elimina' descuento de Alumno");
        Usuario usuario= obtieneUsuario();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        Descuento descuento= new Descuento("Descuento","A","S", organizacion);
        currentSession().save(descuento);
        AlumnoDescuento alumnoDescuento= new AlumnoDescuento("1080506",descuento,new Date(), usuario,"S","A");
        instance.graba(alumnoDescuento, usuario);
        assertNotNull(alumnoDescuento.getId());
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_ALUMNODESCUENTO_ELIMINA)
                .param("id", alumnoDescuento.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "alumnoDescuento.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_ALUMNODESCUENTO_LISTA));
    }
    
    /**
     * Prueba el proceso de Actualizacion de un descuento de Alumno
     */
    @Test
    public void testActualizaAlumnoDescuento() throws Exception {
        log.debug("Test 'actualiza' un descuento de Alumno");
        Usuario usuario= obtieneUsuario();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        Descuento descuento= new Descuento("Descuento","A","S", organizacion);
        currentSession().save(descuento);
        AlumnoDescuento alumnoDescuento= new AlumnoDescuento("1080506",descuento,new Date(), usuario,"S","A");
        instance.graba(alumnoDescuento, usuario);
        assertNotNull(alumnoDescuento.getId());
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_ALUMNODESCUENTO_GRABA)
                .param("id", alumnoDescuento.getId().toString())
                .param("version", alumnoDescuento.getVersion().toString())
                .param("matricula", "1080506")
                .param("contabiliza","N")
                .param("status", "I")
                .param("fecha", alumnoDescuento.getFecha().toString())
                .param("descuento.id", descuento.getId().toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "alumnoDescuento.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_ALUMNODESCUENTO_LISTA+"/"));
        
        currentSession().refresh(alumnoDescuento);
        log.debug("{}",alumnoDescuento);
        assertEquals("I", alumnoDescuento.getStatus());
    }
}
