/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.CobroCampoDao;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import mx.edu.um.mateo.inscripciones.model.Institucion;
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
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
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
public class CobroCampoControllerTest {

    @Autowired
    private CobroCampoDao instance;

    public CobroCampoControllerTest() {
    }
    private static final Logger log = LoggerFactory.getLogger(CobroCampoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
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
     * Test of lista method, of class CobroCampoController.
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
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        currentSession().save(institucion);

        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        CobroCampo cobroCampo = null;
        for (int i = 0; i < 20; i++) {
            cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
            cobroCampo.setEmpresa(empresa);
            cobroCampo.setFechaAlta(new Date());
            cobroCampo.setFechaModificacion(new Date());
            cobroCampo.setStatus("A");
            cobroCampo.setUsuarioAlta(usuario);
            cobroCampo.setUsuarioModificacion(usuario);
            instance.graba(cobroCampo, usuario);
            assertNotNull(cobroCampo.getId());
        }
        this.mockMvc.perform(get(Constantes.PATH_COBROCAMPO)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COBROCAMPO_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_COBROSCAMPOS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    /**
     * Test of ver method, of class CobroCampoController.
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
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        currentSession().save(institucion);

        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(empresa);
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());
        this.mockMvc.perform(get(Constantes.PATH_COBROCAMPO_VER + "/" + cobroCampo.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COBROCAMPO_VER+ ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_COBROCAMPO));
    }

  

    /**
     * Test of elimina method, of class CobroCampoController.
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
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        currentSession().save(institucion);

        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(empresa);
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());
          this.mockMvc.perform(post(Constantes.PATH_COBROCAMPO_ELIMINA)
                .param("id", cobroCampo.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cobroCampo.elimina.message"));
    }
}
