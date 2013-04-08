/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.service.PaqueteManager;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
public class PaqueteControllerTest {

    @Autowired
    private PaqueteManager paqueteMgr;
    private static final Logger log = LoggerFactory.getLogger(PaqueteControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public PaqueteControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of lista method, of class PaqueteController.
     */
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de tipos Becas");
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
            paqueteMgr.crea(paquete, usuario);
        }

        this.mockMvc.perform(get(Constantes.PATH_PAQUETE)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PAQUETE_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAQUETES)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    /**
     * Test of ver method, of class PaqueteController.
     */
    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar lista de tipos Becas");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Paquete paquete = new Paquete();
        paquete.setAcfe("a");
        paquete.setDescripcion("test");
        paquete.setEmpresa(empresa);
        paquete.setEnsenanza(new BigDecimal("80"));
        paquete.setInternado(new BigDecimal("80"));
        paquete.setMatricula(new BigDecimal("80"));
        paquete.setNombre("test");
        currentSession().save(paquete);
        this.mockMvc.perform(get(Constantes.PATH_PAQUETE_VER + "/" + paquete.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PAQUETE_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PAQUETE));
    }

    /**
     * Test of graba method, of class PaqueteController.
     */
    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera mostrar lista de tipos Becas");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Paquete paquete = new Paquete();
        paquete.setAcfe("a");
        paquete.setDescripcion("test");
        paquete.setEmpresa(empresa);
        paquete.setEnsenanza(new BigDecimal("80"));
        paquete.setInternado(new BigDecimal("80"));
        paquete.setMatricula(new BigDecimal("80"));
        paquete.setNombre("test");
        currentSession().save(paquete);
        this.mockMvc.perform(get(Constantes.PATH_PAQUETE_VER + "/" + paquete.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PAQUETE_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PAQUETE));
    }

    /**
     * Test of elimina method, of class PaqueteController.
     */
    @Test
    public void testElimina() throws Exception{
        log.debug("Debiera mostrar lista de tipos Becas");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Paquete paquete = new Paquete();
        paquete.setAcfe("a");
        paquete.setDescripcion("test");
        paquete.setEmpresa(empresa);
        paquete.setEnsenanza(new BigDecimal("80"));
        paquete.setInternado(new BigDecimal("80"));
        paquete.setMatricula(new BigDecimal("80"));
        paquete.setNombre("test");
        currentSession().save(paquete);
        this.mockMvc.perform(post(Constantes.PATH_PAQUETE_ELIMINA)
                .param("id", paquete.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "paquete.elimina.message"));
    }
}
