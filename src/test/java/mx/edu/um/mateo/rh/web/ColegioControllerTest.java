/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.dao.ColegioDao;
import mx.edu.um.mateo.rh.model.Colegio;
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
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author AMDA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class ColegioControllerTest extends BaseControllerTest {

    @Autowired
    private ColegioDao colegioDao;
    
    private static final Logger log = LoggerFactory.getLogger(ColegioControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    public ColegioControllerTest() {
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
    public void debieraMostrarListaDeColegio() throws Exception {
        log.debug("Debiera mostrar lista de colegios");

        for (int i = 0; i < 20; i++) {
            Colegio colegio = new Colegio();
            colegio.setNombre("Test");
            colegio.setStatus("A");
            colegioDao.grabaColegio(colegio);
            assertNotNull(colegio.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_COLEGIO)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLEGIO_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_COLEGIOS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarColegio() throws Exception {
        log.debug("Debiera mostrar colegio");
        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegioDao.grabaColegio(colegio);
        assertNotNull(colegio.getId());
        this.mockMvc.perform(get(Constantes.PATH_COLEGIO_VER + "/" + colegio.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLEGIO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_COLEGIO));
    }

    @Test
    public void debieraCrearColegio() throws Exception {
        log.debug("Debiera crear colegio");
        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegioDao.grabaColegio(colegio);
        assertNotNull(colegio.getId());
        log.debug("nombre"+colegio.toString());
//        this.mockMvc.perform(post(Constantes.PATH_COLEGIO_CREA)
//                .param("nombre", colegio.getNombre().toString()))
//                .andExpect(status().isOk())
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE));
    }

    @Test
    public void debieraActualizarColegio() throws Exception {
        log.debug("Debiera actualizar colegio");
        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegioDao.grabaColegio(colegio);
        assertNotNull(colegio.getId());

//        this.mockMvc.perform(post(Constantes.PATH_COLEGIO_ACTUALIZA)
//               // .param("id", colegio.getId().toString())
//                .param("nombre", colegio.getNombre().toString()))
//                .andExpect(status().isOk())
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.actualizado.message"));
    }

    @Test
    public void debieraEliminarColegio() throws Exception {
        log.debug("Debiera eliminar colegio");
        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegioDao.grabaColegio(colegio);
        assertNotNull(colegio.getId());

//        this.mockMvc.perform(post(Constantes.PATH_COLEGIO_ELIMINA)
//                .param("id", colegio.getId().toString()))
//                .andExpect(status().isOk())
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.eliminado.message"));
   }
}
