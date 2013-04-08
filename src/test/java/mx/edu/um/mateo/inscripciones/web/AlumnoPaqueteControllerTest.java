/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.web.ProveedorControllerTest;
import mx.edu.um.mateo.inscripciones.model.AlumnoPaquete;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
public class AlumnoPaqueteControllerTest extends BaseTest{
    private static final Logger log = LoggerFactory.getLogger(ProveedorControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SessionFactory sessionFactory;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }
    
    @Test
    public void debieraMostrarListaDeAlumnoPaquete() throws Exception {
        log.debug("Debiera mostrar lista de AlumnoPaquete");
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
    public void debieraMostrarJspEditaDeAlumnoPaquete() throws Exception {
        log.debug("Debiera mostrar JspEdita de AlumnoPaquete");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Paquete paquete = new Paquete("Test","Test1","1110475", new Double(12), new Double(12),"1", usuario.getEmpresa());
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
    public void debieraMostrarJspNuevoDeAlumnoPaquete() throws Exception {
        log.debug("Debiera mostrar JspNuevo de AlumnoPaquete");
        this.mockMvc.perform(
                get(Constantes.PATH_ALUMNOPAQUETE_NUEVO))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ALUMNOPAQUETE_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAQUETES))
                .andExpect(model().attributeExists("alumnoPaquete"));
    }
    
     @Test
    public void debieraMostrarAlumnoPaquete() throws Exception {
        log.debug("Debiera mostrar alumnoPaquete");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Paquete paquete = new Paquete("Test","Test1","1110475", new Double(12), new Double(12),"1", usuario.getEmpresa());
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
    public void debieraCrearAlumnoPaquete() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Paquete paquete = new Paquete("Test","Test1","1110475", new Double(12), new Double(12),"1", usuario.getEmpresa());
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
     public void debieraModificarAlumnoPaquete() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Paquete paquete = new Paquete("Test","Test1","1110475", new Double(12), new Double(12),"1", usuario.getEmpresa());
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
    }
    
     @Test
    public void debieraEliminarAlumnoPaquete() throws Exception {
        log.debug("Debiera eliminar alumnoPaquete");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Paquete paquete = new Paquete("Test","Test1","1110475", new Double(12), new Double(12),"1", usuario.getEmpresa());
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        currentSession().save(alumnoPaquete);
        assertNotNull(alumnoPaquete.getId());

        this.mockMvc.perform(post(Constantes.PATH_ALUMNOPAQUETE_ELIMINA)
                .param("id", alumnoPaquete.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "alumnoPaquete.eliminado.message"));
   }
     
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}
