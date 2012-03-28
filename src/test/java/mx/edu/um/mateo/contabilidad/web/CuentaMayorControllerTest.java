/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.dao.CuentaMayorDao;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertNotNull;
import org.junit.*;
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
 * @author nujev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class CuentaMayorControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CuentaMayorControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CuentaMayorDao cuentaMayorDao;
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void debieraMostrarListaDeCuentaMayor() throws Exception {
        log.debug("Debiera monstrar lista de cuentas de mayor");

        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        for (int i = 0; i < 20; i++) {
            CuentaMayor cuentaMayor = new CuentaMayor("test" + i, "test", "test", false, false, false, false, BigDecimal.ZERO);
            cuentaMayor.setOrganizacion(test);
            cuentaMayorDao.crea(cuentaMayor);
            assertNotNull(cuentaMayor);
        }

        this.mockMvc.perform(
                get(Constantes.PATH_CUENTA_MAYOR))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CUENTA_MAYOR_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_MAYORES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarCuentaMayor() throws Exception {
        log.debug("Debiera mostrar cuenta de mayor");
        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaMayor cuentaMayor = new CuentaMayor("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaMayor.setOrganizacion(test);
        cuentaMayor = cuentaMayorDao.crea(cuentaMayor);
        assertNotNull(cuentaMayor);

        this.mockMvc.perform(
                get(Constantes.PATH_CUENTA_MAYOR_VER + "/" + cuentaMayor.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CUENTA_MAYOR_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_MAYOR));
    }

    @Test
    public void debieraCrearCuentaMayor() throws Exception {
        log.debug("Debiera crear cuenta de mayor");
        
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Empresa otraEmpresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(otraEmpresa);
        Almacen almacen = new Almacen("TST", "TEST01",otraEmpresa);
        currentSession().save(almacen);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        usuario.setEmpresa(otraEmpresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));

        this.mockMvc.perform(
                post(Constantes.PATH_CUENTA_MAYOR_CREA)
                .param("nombre", "test")
                .param("nombreFiscal", "test")
                .param("clave", "test")
                .param("detalle", "false")
                .param("aviso", "false")
                .param("auxiliar", "false")
                .param("iva", "false")
                .param("pctIva", "0.0"))
                .andExpect(status().isOk())
                .andExpect(flash()
                .attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash()
                .attribute(Constantes.CONTAINSKEY_MESSAGE, "mayores.creada.message"));
    }

    @Test
    public void debieraActualizarCuentaMayor() throws Exception {
        log.debug("Debiera actualizar cuenta de mayor");
        
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Empresa otraEmpresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(otraEmpresa);
        Almacen almacen = new Almacen("TST", "TEST01",otraEmpresa);
        currentSession().save(almacen);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        usuario.setEmpresa(otraEmpresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));
        CuentaMayor cuentaMayor = new CuentaMayor("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaMayor.setOrganizacion(organizacion);
        cuentaMayor = cuentaMayorDao.crea(cuentaMayor);
        assertNotNull(cuentaMayor);

        this.mockMvc.perform(
                post(Constantes.PATH_CUENTA_MAYOR_ACTUALIZA)
                .param("id", cuentaMayor.getId().toString())
                .param("version", cuentaMayor.getVersion().toString())
                .param("nombre", "test1")
                .param("nombreFiscal", cuentaMayor.getNombreFiscal())
                .param("clave", cuentaMayor.getClave())
                .param("detalle", cuentaMayor.getDetalle().toString())
                .param("aviso", cuentaMayor.getAviso().toString())
                .param("auxiliar", cuentaMayor.getAuxiliar().toString())
                .param("iva", cuentaMayor.getIva().toString())
                .param("pctIva", cuentaMayor.getPorcentajeIva().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "mayores.actualizada.message"));
    }

    @Test
    public void debieraEliminarCtaMayor() throws Exception {
        log.debug("Debiera eliminar cuenta de mayor");
        
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Empresa otraEmpresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(otraEmpresa);
        Almacen almacen = new Almacen("TST", "TEST01",otraEmpresa);
        currentSession().save(almacen);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        usuario.setEmpresa(otraEmpresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));
        CuentaMayor cuentaMayor = new CuentaMayor("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaMayor.setOrganizacion(organizacion);
        cuentaMayorDao.crea(cuentaMayor);
        assertNotNull(cuentaMayor);

        this.mockMvc.perform(post(
                Constantes.PATH_CUENTA_MAYOR_ELIMINA)
                .param("id", cuentaMayor.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash()
                .attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash()
                .attribute(Constantes.CONTAINSKEY_MESSAGE, "mayores.eliminada.message"));
    }
}
