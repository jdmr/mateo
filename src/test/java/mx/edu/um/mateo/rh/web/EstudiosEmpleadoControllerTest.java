/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import mx.edu.um.mateo.general.test.BaseTest;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author AMDA
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
//    "classpath:mateo.xml",
//    "classpath:security.xml",
//    "classpath:dispatcher-servlet.xml"
//})
@Transactional
public class EstudiosEmpleadoControllerTest extends BaseTest {
    
//    @Autowired
//    @Qualifier
//    private NivelEstudios nivelEstudios;
//    
//    @Autowired
//    private EstudiosEmpleadoDao estudiosEmpleadoDao;
//    
//    private static final Logger log = LoggerFactory.getLogger(EstudiosEmpleadoControllerTest.class);
//    @Autowired
//    private WebApplicationContext wac;
//    private MockMvc mockMvc;
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    private Session currentSession() {
//        return sessionFactory.getCurrentSession();
//    }
//    public EstudiosEmpleadoControllerTest() {
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
//    public void debieraMostrarListaDeEstudiosEmpleado() throws Exception {
//        log.debug("Debiera mostrar lista de estudiosEmpleados");
//
//        for (int i = 0; i < 20; i++) {
//            EstudiosEmpleado estudiosEmpleado = new EstudiosEmpleado();
//            estudiosEmpleado.setFechaCaptura(new Date());
//            estudiosEmpleado.setFechaTitulacion(new Date());
//            estudiosEmpleado.setNivelEstudios(nivelEstudios.obtener(NivelEstudios.PREPARATORIA).getId());
//            estudiosEmpleado.setNombreEstudios("maestria");
//            estudiosEmpleado.setStatus("A");
//            estudiosEmpleado.setTitulado(Short.MIN_VALUE);
//            estudiosEmpleado.setVersion(1);
//            currentSession().save(estudiosEmpleado);
//            assertNotNull(estudiosEmpleado.getId());
//            log.debug("nombre" + estudiosEmpleado.getNombreEstudios());
//        }
//
//        this.mockMvc.perform(get(Constantes.PATH_ESTUDIOSEMPLEADO)).
//                andExpect(status().isOk()).
//                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ESTUDIOSEMPLEADO_LISTA + ".jsp")).
//                andExpect(model().attributeExists(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO)).
//                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
//                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
//                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
//    }
//
//    @Test
//    public void debieraMostrarEstudiosEmpleado() throws Exception {
//        log.debug("Debiera mostrar estudiosEmpleado");
//         EstudiosEmpleado estudiosEmpleado = new EstudiosEmpleado();
//        estudiosEmpleado.setFechaCaptura(new Date());
//        estudiosEmpleado.setFechaTitulacion(new Date());
//        estudiosEmpleado.setNivelEstudios(NivelEstudios.SECUNDARIA);
//        estudiosEmpleado.setNombreEstudios("maestria");
//        estudiosEmpleado.setStatus("A");
//        estudiosEmpleado.setTitulado(Short.MIN_VALUE);
//        estudiosEmpleado.setVersion(1);
//        currentSession().save(estudiosEmpleado);
//        assertNotNull(estudiosEmpleado.getId());
//        log.debug("nombre"+estudiosEmpleado.getNombreEstudios());
//        this.mockMvc.perform(get(Constantes.PATH_ESTUDIOSEMPLEADO_VER + "/" + estudiosEmpleado.getId())).
//                andExpect(status().isOk())
//                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ESTUDIOSEMPLEADO_VER + ".jsp"))
//                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_ESTUDIOSEMPLEADO));
//    }
//
//    @Test
//    public void debieraCrearEstudiosEmpleado() throws Exception {
//        log.debug("Debiera crear estudiosEmpleado");
//        EstudiosEmpleado estudiosEmpleado = new EstudiosEmpleado();
//        estudiosEmpleado.setFechaCaptura(new Date());
//        estudiosEmpleado.setFechaTitulacion(new Date());
//        estudiosEmpleado.setNivelEstudios(NivelEstudios.PREPARATORIA);
//        estudiosEmpleado.setNombreEstudios("maestria");
//        estudiosEmpleado.setStatus("A");
//        estudiosEmpleado.setTitulado(Short.MIN_VALUE);
//        estudiosEmpleado.setVersion(1);
//        estudiosEmpleadoDao.crea(estudiosEmpleado);
//        currentSession().save(estudiosEmpleado);
//        assertNotNull(estudiosEmpleado.getId());
//        
//        log.debug("nombre"+estudiosEmpleado.toString());
//        this.mockMvc.perform(post(Constantes.PATH_ESTUDIOSEMPLEADO_CREA)
//                .param("nombreEstudios", estudiosEmpleado.getNombreEstudios().toString()))
//                .andExpect(status().isOk())
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE));
//    }
//
//    @Test
//    public void debieraActualizarEstudiosEmpleado() throws Exception {
//        log.debug("Debiera actualizar estudiosEmpleado");
//        EstudiosEmpleado estudiosEmpleado = new EstudiosEmpleado();
//        estudiosEmpleado.setFechaCaptura(new Date());
//        estudiosEmpleado.setFechaTitulacion(new Date());
//        estudiosEmpleado.setNivelEstudios(NivelEstudios.MAESTRIA);
//        estudiosEmpleado.setNombreEstudios("maestria");
//        estudiosEmpleado.setStatus("A");
//        estudiosEmpleado.setTitulado(Short.MIN_VALUE);
//        estudiosEmpleado.setVersion(1);
//        currentSession().save(estudiosEmpleado);
//        assertNotNull(estudiosEmpleado.getId());
//        log.debug("nombre"+estudiosEmpleado.getNombreEstudios());
//
//        this.mockMvc.perform(post(Constantes.PATH_ESTUDIOSEMPLEADO_ACTUALIZA)
//                .param("id", estudiosEmpleado.getId().toString())
//                .param("nombreEstudios", estudiosEmpleado.getNombreEstudios().toString()))
//                .andExpect(status().isOk())
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "estudiosEmpleado.actualizado.message"));
//
//    }
//
//    @Test
//    public void debieraEliminarEstudiosEmpleado() throws Exception {
//        log.debug("Debiera eliminar estudiosEmpleado");
//        EstudiosEmpleado estudiosEmpleado = new EstudiosEmpleado();
//        estudiosEmpleado.setFechaCaptura(new Date());
//        estudiosEmpleado.setFechaTitulacion(new Date());
//        estudiosEmpleado.setNivelEstudios(NivelEstudios.DOCTORADO);
//        estudiosEmpleado.setNombreEstudios("maestria");
//        estudiosEmpleado.setStatus("A");
//        estudiosEmpleado.setTitulado(Short.MIN_VALUE);
//        estudiosEmpleado.setVersion(1);
//        currentSession().save(estudiosEmpleado);
//        assertNotNull(estudiosEmpleado.getId());
//        log.debug("nombre"+estudiosEmpleado.getNombreEstudios());
//
//        this.mockMvc.perform(post(Constantes.PATH_ESTUDIOSEMPLEADO_ELIMINA)
//                .param("id", estudiosEmpleado.getId().toString()))
//                .andExpect(status().isOk())
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "estudiosEmpleado.eliminado.message"));
//    }
//

}
