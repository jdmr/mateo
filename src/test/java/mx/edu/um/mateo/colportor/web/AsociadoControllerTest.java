/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * TODO problemas con type long: asociado
 */
package mx.edu.um.mateo.colportor.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.AsociadoDao;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.general.model.*;
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
import org.springframework.security.core.GrantedAuthority;
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
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class AsociadoControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(AsociadoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private AsociadoDao asociadoDao;
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
    public void debieraMostrarListaDeAsociado() throws Exception {
        log.debug("Debiera monstrar lista asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        for (int i = 0; i < 20; i++) {
            Asociado asociado = new Asociado("test" + i + "@test.com", "test", "test", "test", "test",
                    Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                    Constantes.MUNICIPIO);

            asociado.setEmpresa(empresa);
            asociado.setAlmacen(almacen);
            asociado.setAsociacion(asociacion);
            currentSession().save(asociado);
            assertNotNull(asociado.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_ASOCIADO)
                .sessionAttr(Constantes.SESSION_ASOCIACION, asociacion))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIADO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_ASOCIADOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarAsociado() throws Exception {
        log.debug("Debiera mostrar  asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);

        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());


        this.mockMvc.perform(get(Constantes.PATH_ASOCIADO_VER + "/" + asociado.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIADO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_ASOCIADO));
    }

    @Test
    public void debieraCrearAsociado() throws Exception {
        log.debug("Debiera crear asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Asociado asociado = new Asociado("jalvaradol", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        this.authenticate(asociado, asociado.getPassword(), new ArrayList<GrantedAuthority>(asociado.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_CREA)
                .sessionAttr("asociacionId", asociacion.getId())
                //                .sessionAttr("alamcen", almacen)
                .param("username", "jalvaradol")
                .param("nombre", "Jose")
                .param("apPaterno", "Alvarado")
                .param("apMaterno", "Lopez")
                .param("correo", "jalvaradol52@gmail.com")
                .param(Constantes.ROLES, Constantes.ROLE_ASO))
                //                .param("username", "jalvaradol52@gmail.com"))
                .andExpect(status().isOk());
        //.andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_ASOCIADO));
    }

    @Test
    public void debieraActualizarAsociado() throws Exception {
        log.debug("Debiera actualizar  asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Asociado asociado = new Asociado("jalvaradol", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());

        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_ACTUALIZA)
                .param("id", asociado.getId().toString()))
                .andExpect(status().isOk());


    }

    @Test
    public void debieraEliminarAsociacion() throws Exception {
        log.debug("Debiera eliminar  asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Asociado asociado = new Asociado("jalvaradol", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_ELIMINA)
                .param("id", asociado.getId().toString()))
                .andExpect(status().isOk()).andExpect(flash()
                .attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.eliminado.message"));
    }
}
