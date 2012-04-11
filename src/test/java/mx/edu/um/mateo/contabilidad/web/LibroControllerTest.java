package mx.edu.um.mateo.contabilidad.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.dao.LibroDao;
import mx.edu.um.mateo.contabilidad.dao.LibroDao;
import mx.edu.um.mateo.contabilidad.model.Libro;
import mx.edu.um.mateo.contabilidad.model.Libro;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import static org.junit.Assert.assertNotNull;
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
public class LibroControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(LibroControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private LibroDao libroDao;

    @Autowired
    private SessionFactory sessionFactory;
    
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }


    public LibroControllerTest() {
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
    public void debieraMostrarListaDeLibro() throws Exception {
        log.debug("Debiera monstrar lista de libros");
        
        this.mockMvc.perform(get(Constantes.PATH_CUENTA_LIBRO)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.PATH_CUENTA_LIBRO_LISTA+".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_LIBROS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarLibro() throws Exception {

  log.debug("Debiera mostrar cuenta de mayor");
        Libro libro = new Libro("test", "tes", "te", 0001);
        libro = libroDao.crea(libro);
        assertNotNull(libro);

        this.mockMvc.perform(get(Constantes.PATH_CUENTA_LIBRO_VER +"/"+ libro.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CUENTA_LIBRO_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_LIBRO));
    }

    @Test
    public void debieraCrearLibro() throws Exception {
        log.debug("Debiera crear libro");
        Organizacion organizacion =new Organizacion ("TEST01", "TEST01", "TEST01");
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
        this.mockMvc.perform(post(Constantes.PATH_CUENTA_LIBRO_CREA)
                .param("nombre", "test")
                .param("clave", "tes")
                .param("status","ts")
                .param("codigo","0000"))
                .andExpect(status().isOk())
                 .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE)).
                andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "libro.creado.message"));
    }

    @Test
    public void debieraActualizarLibro() throws Exception {
        log.debug("Debiera actualizar libro");
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
        Libro libro = new Libro("test", "tes", "te",0000 );
        libro.setOrganizacion(organizacion);
        libro =libroDao.crea(libro);
        assertNotNull(libro);

        this.mockMvc.perform(post(Constantes.PATH_CUENTA_LIBRO_ACTUALIZA)
                .param("id", libro.getId().toString())
                .param("nombre", "test")
                .param("clave", "tes")
                .param("status","ts")
                .param("codigo","0000"))
                .andExpect(status().isOk())
                 .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE)).
                andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "libro.actualizado.message"));
    }

  
    

    @Test
    public void debieraEliminarLibro() throws Exception {
        log.debug("Debiera eliminar libro");
        Libro libro = new Libro("test", "tes","te",0001);
        libroDao.crea(libro);

        this.mockMvc.perform(post(Constantes.PATH_CUENTA_LIBRO_ELIMINA).param("id", libro.getId().toString())).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE)).
                andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "libro.eliminado.message"));
    }
}
