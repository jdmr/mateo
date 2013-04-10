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
import mx.edu.um.mateo.inscripciones.dao.PaqueteDao;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PaqueteControllerTest extends BaseControllerTest{
    
    private static final Logger log = LoggerFactory.getLogger(PaqueteControllerTest.class);
    
    
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public PaqueteControllerTest() {
    }
    
    /**
     * Prueba la lista de paquetes
     */
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de paquetes");
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
        assertNotNull(almacen);
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);        
        Long id = usuario.getId();
        assertNotNull(id);
        
        Paquete paquete = null;
        for (int i = 0; i < 20; i++) {
            paquete = new Paquete();
            paquete.setAcfe("a");
            paquete.setDescripcion("test");
            paquete.setEmpresa(empresa);
            paquete.setEnsenanza(new BigDecimal("80"));
            paquete.setInternado(new BigDecimal("80"));
            paquete.setMatricula(new BigDecimal("80"));
            paquete.setNombre("test");
            currentSession().save(paquete);
            assertNotNull(paquete.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_PAQUETE)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PAQUETE_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAQUETES)).
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
                
        this.mockMvc.perform(get(Constantes.PATH_PAQUETE_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PAQUETE_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PAQUETE));
    }
    
    /**
     * Prueba que se muestre el jsp Edita
     */
    @Test
    public void testEdita() throws Exception {
        log.debug("Test 'edita'");
        
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
        assertNotNull(almacen);
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);        
        Long id = usuario.getId();
        assertNotNull(id);
        
        Paquete paquete = new Paquete();
        paquete.setAcfe("a");
        paquete.setDescripcion("test");
        paquete.setEmpresa(empresa);
        paquete.setEnsenanza(new BigDecimal("80"));
        paquete.setInternado(new BigDecimal("80"));
        paquete.setMatricula(new BigDecimal("80"));
        paquete.setNombre("test");
        currentSession().save(paquete);
        assertNotNull(paquete);
                
        this.mockMvc.perform(get(Constantes.PATH_PAQUETE_EDITA+"/"+paquete.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PAQUETE_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PAQUETE))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_PAQUETE, paquete));
    }
    
    /**
     * Prueba que se muestre el jsp Ver
     */
    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar paquetes");
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
        assertNotNull(almacen);
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);        
        Long id = usuario.getId();
        assertNotNull(id);
        
        Paquete paquete = new Paquete();
        paquete.setAcfe("a");
        paquete.setDescripcion("test");
        paquete.setEmpresa(empresa);
        paquete.setEnsenanza(new BigDecimal("80"));
        paquete.setInternado(new BigDecimal("80"));
        paquete.setMatricula(new BigDecimal("80"));
        paquete.setNombre("test");
        currentSession().save(paquete);
        assertNotNull(paquete);
        
        this.mockMvc.perform(get(Constantes.PATH_PAQUETE_VER + "/" + paquete.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PAQUETE_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PAQUETE));
    }

    /**
     * Prueba que el proceso de Grabar un paquete
     */
    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear paquete");
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
        assertNotNull(almacen);
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);        
        Long id = usuario.getId();
        assertNotNull(id);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_PAQUETE_GRABA)
                .param("acfe", "a")
                .param("descripcion", "test")
                .param("ensenanza","80")
                .param("internado", "80")
                .param("matricula", "80")
                .param("nombre", "test"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "paquete.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PAQUETE_LISTA));
    }
    
    /**
     * Prueba el proceso de Actualizacion de un paquete
     */
    @Test
    public void testActualiza() throws Exception {
        log.debug("Test 'actualiza' un paquete");
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
        assertNotNull(almacen);
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);        
        Long id = usuario.getId();
        assertNotNull(id);
        
        Paquete paquete = new Paquete();
        paquete.setAcfe("a");
        paquete.setDescripcion("test");
        paquete.setEmpresa(empresa);
        paquete.setEnsenanza(new BigDecimal("80"));
        paquete.setInternado(new BigDecimal("80"));
        paquete.setMatricula(new BigDecimal("80"));
        paquete.setNombre("test");
        currentSession().save(paquete);
        assertNotNull(paquete);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_PAQUETE_ACTUALIZA)
                .param("version", paquete.getVersion().toString())
                .param("id", paquete.getId().toString())
                .param("acfe", "a")
                .param("descripcion", "test2")
                .param("ensenanza","80")
                .param("internado", "80")
                .param("matricula", "80")
                .param("nombre", "test"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "paquete.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PAQUETE_LISTA));
        
        currentSession().refresh(paquete);
        log.debug("{}",paquete);
        assertEquals("test2", paquete.getDescripcion());
    }

    /**
     * Prueba el proceso de Borrado de un paquete
     */
    @Test
    public void testElimina() throws Exception{
        log.debug("Test 'elimina' paquete");
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
        assertNotNull(almacen);
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);        
        Long id = usuario.getId();
        assertNotNull(id);
        
        Paquete paquete = new Paquete();
        paquete.setAcfe("a");
        paquete.setDescripcion("test");
        paquete.setEmpresa(empresa);
        paquete.setEnsenanza(new BigDecimal("80"));
        paquete.setInternado(new BigDecimal("80"));
        paquete.setMatricula(new BigDecimal("80"));
        paquete.setNombre("test");
        currentSession().save(paquete);
        assertNotNull(paquete);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_PAQUETE_ELIMINA)
                .param("id", paquete.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "paquete.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PAQUETE_LISTA));
    }
}
