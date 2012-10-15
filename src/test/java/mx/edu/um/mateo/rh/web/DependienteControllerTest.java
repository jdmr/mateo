/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import mx.edu.um.mateo.general.test.BaseTest;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
//    "classpath:mateo.xml",
//    "classpath:security.xml",
//    "classpath:dispatcher-servlet.xml"
//})
@Transactional
public class DependienteControllerTest extends BaseTest {
//
//    private static final Logger log = LoggerFactory.getLogger(DependienteControllerTest.class);
//    @Autowired
//    private WebApplicationContext wac;
//    private MockMvc mockMvc;
//    @Autowired
//    private DependienteDao dependienteDao;
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    private Session currentSession() {
//        return sessionFactory.getCurrentSession();
//    }
//    public DependienteControllerTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//    }
//
//    @AfterClass
//    public static void tearDownClass() throws Exception {
//    }
//
//    @Before
//    public void setUp() {
//        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
//    }
//
//    @After
//    public void tearDown() {
//    }
//
//    @Test
//    public void debieraMostrarListaDeDependientes() throws Exception {
//        log.debug("Debiera mostrar lista de dependientes");
//        for (int i = 0; i < 20; i++) {
//        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
//        currentSession().save(organizacion);
//        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
//        currentSession().save(empresa);
//        Empleado empleado = new Empleado("test001", "test", "test", "test", "M", "address", "A",
//                "curp", "rfc", "cuenta", "imss", 10, 100, BigDecimal.ZERO, "mo", "ife", "ra",
//                Boolean.TRUE, "padre", "madre", "Ca", "Conyugue", Boolean.TRUE, Boolean.TRUE,
//                "iglesia", "responsabilidad",empresa);
//        currentSession().save(empleado);
//        Dependiente dependiente = new Dependiente();
//        dependiente.setTipoDependiente(TipoDependiente.HIJO);
//        dependiente.setEmpleado(empleado);
//        dependiente.setStatus("A");
//        currentSession().save(dependiente);
//        assertNotNull(dependiente.getId());
//            
//        }
//
//        this.mockMvc.perform(get(Constantes.PATH_DEPENDIENTE)).
//                andExpect(status().isOk()).
//                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DEPENDIENTE_LISTA + ".jsp")).
//                andExpect(model().attributeExists(Constantes.CONTAINSKEY_DEPENDIENTES)).
//                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
//                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
//                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
//    }
//
//    @Test
//    public void debieraMostrarDependiente() throws Exception {
//        log.debug("Debiera mostrar dependiente");
//        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
//        currentSession().save(organizacion);
//        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
//        currentSession().save(empresa);
//        Empleado empleado = new Empleado("test001", "test", "test", "test", "M", "address", "A",
//                "curp", "rfc", "cuenta", "imss", 10, 100, BigDecimal.ZERO, "mo", "ife", "ra",
//                Boolean.TRUE, "padre", "madre", "Ca", "Conyugue", Boolean.TRUE, Boolean.TRUE,
//                "iglesia", "responsabilidad",empresa);
//        currentSession().save(empleado);
//        Dependiente dependiente = new Dependiente();
//        dependiente.setTipoDependiente(TipoDependiente.HIJO);
//        dependiente.setEmpleado(empleado);
//        dependiente.setStatus("A");
//        dependienteDao.graba(dependiente);
//        assertNotNull(dependiente.getId());
//        this.mockMvc.perform(get(Constantes.PATH_DEPENDIENTE_VER + "/" + dependiente.getId())).
//                andExpect(status().isOk())
//                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DEPENDIENTE_VER + ".jsp"))
//                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_DEPENDIENTE));
//    }
//
//    @Test
//    public void debieraCrearDependiente() throws Exception {
//        log.debug("Debiera crear dependiente");
//Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
//        currentSession().save(organizacion);
//        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
//        currentSession().save(empresa);
//        Empleado empleado = new Empleado("test001", "test", "test", "test", "M", "address", "A",
//                "curp", "rfc", "cuenta", "imss", 10, 100, BigDecimal.ZERO, "mo", "ife", "ra",
//                Boolean.TRUE, "padre", "madre", "Ca", "Conyugue", Boolean.TRUE, Boolean.TRUE,
//                "iglesia", "responsabilidad",empresa);
//        currentSession().save(empleado);
//        Dependiente dependiente = new Dependiente();
//        dependiente.setTipoDependiente(TipoDependiente.HIJO);
//        dependiente.setEmpleado(empleado);
//        dependiente.setStatus("A");        currentSession().save(dependiente);
//        assertNotNull(dependiente.getId());
//        this.mockMvc.perform(post(Constantes.PATH_DEPENDIENTE_CREA)
//                .param("tipoDependiente",dependiente.getTipoDependiente().toString()))
//                .andExpect(status().isOk())
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE));
//    }
//
//    @Test
//    public void debieraEditarDependiente() throws Exception {
//        log.debug("Debiera actualizar dependiente");
//        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
//        currentSession().save(organizacion);
//        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
//        currentSession().save(empresa);
//        Empleado empleado = new Empleado("test001", "test", "test", "test", "M", "address", "A",
//                "curp", "rfc", "cuenta", "imss", 10, 100, BigDecimal.ZERO, "mo", "ife", "ra",
//                Boolean.TRUE, "padre", "madre", "Ca", "Conyugue", Boolean.TRUE, Boolean.TRUE,
//                "iglesia", "responsabilidad",empresa);
//        currentSession().save(empleado);
//        Dependiente dependiente = new Dependiente();
//        dependiente.setTipoDependiente(TipoDependiente.HIJO);
//        dependiente.setEmpleado(empleado);
//        dependiente.setStatus("A");
//        currentSession().save(dependiente);
//        assertNotNull(dependiente.getId());
//
//        this.mockMvc.perform(post(Constantes.PATH_DEPENDIENTE_EDITA)
//                .param("id", dependiente.getId().toString())
//                .param("tipoDependiente", dependiente.getTipoDependiente().toString()))
//                .andExpect(status().isOk())
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "dependiente.editado.message"));
//
//    }
//
//    @Test
//    public void debieraEliminarDependiente() throws Exception {
//        log.debug("Debiera eliminar dependiente");
//        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
//        currentSession().save(organizacion);
//        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
//        currentSession().save(empresa);
//        Empleado empleado = new Empleado("test001", "test", "test", "test", "M", "address", "A",
//                "curp", "rfc", "cuenta", "imss", 10, 100, BigDecimal.ZERO, "mo", "ife", "ra",
//                Boolean.TRUE, "padre", "madre", "Ca", "Conyugue", Boolean.TRUE, Boolean.TRUE,
//                "iglesia", "responsabilidad",empresa);
//        currentSession().save(empleado);
//        Dependiente dependiente = new Dependiente();
//        dependiente.setTipoDependiente(TipoDependiente.HIJO);
//        dependiente.setEmpleado(empleado);
//        dependiente.setStatus("A");
//        currentSession().save(dependiente);
//        assertNotNull(dependiente.getId());
//
//        this.mockMvc.perform(post(Constantes.PATH_DEPENDIENTE_ELIMINA)
//                .param("id", dependiente.getId().toString()))
//                .andExpect(status().isOk())
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "dependiente.eliminado.message"));
//    }
}
