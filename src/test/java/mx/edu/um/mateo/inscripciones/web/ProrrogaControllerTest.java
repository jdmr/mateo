/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.ProrrogaDao;
import mx.edu.um.mateo.inscripciones.model.Prorroga;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;

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
public class ProrrogaControllerTest extends BaseTest {

    @Autowired
    private ProrrogaDao prorrogaDao;
    private static final Logger log = LoggerFactory.getLogger(ProrrogaControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public ProrrogaControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of lista method, of class ProrrogaController.
     */
    @Test
    public void testLista() throws Exception {
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
        Long id = usuario.getId();
        assertNotNull(id);
        Prorroga prorroga = null;
        for (int i = 0; i < 20; i++) {
            prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("1203.5"), "a");
            prorroga.setObservaciones("prueba");
            prorrogaDao.graba(prorroga, usuario);
            assertNotNull(prorroga.getId());
        }
        this.mockMvc.perform(get(Constantes.PATH_PRORROGA)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PRORROGA_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PRORROGAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    /**
     * Test of ver method, of class ProrrogaController.
     */
    @Test
    public void testVer() throws Exception {
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
        Long id = usuario.getId();
        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("1203.5"), "a");
        prorroga.setObservaciones("prueba");
        prorrogaDao.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
        this.mockMvc.perform(get(Constantes.PATH_PRORROGA_VER + "/" + prorroga.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PRORROGA_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PRORROGA));
    }



    /**
     * Test of elimina method, of class ProrrogaController.
     */
    @Test
    public void testElimina() throws Exception{
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
        Long id = usuario.getId();
        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("1203.5"), "a");
        prorroga.setObservaciones("prueba");
        prorrogaDao.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
          this.mockMvc.perform(post(Constantes.PATH_PRORROGA_ELIMINA)
                .param("id", prorroga.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "prorroga.elimina.message"));
    }
}
