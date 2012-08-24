/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import java.math.BigDecimal;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.rh.dao.DependienteDao;
import mx.edu.um.mateo.rh.model.Dependiente;
import mx.edu.um.mateo.rh.model.TipoDependiente;
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
 * @author AMDA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class DependienteControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DependienteControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private DependienteDao dependienteDao;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    public DependienteControllerTest() {
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
    public void debieraMostrarListaDeDependientes() throws Exception {
        log.debug("Debiera mostrar lista de dependientes");

        for (int i = 0; i < 20; i++) {
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        currentSession().save(dependiente);
        assertNotNull(dependiente.getId());
            
        }

        this.mockMvc.perform(get(Constantes.PATH_DEPENDIENTE)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DEPENDIENTE_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_DEPENDIENTES)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarDependiente() throws Exception {
        log.debug("Debiera mostrar dependiente");
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        dependienteDao.crea(dependiente);
        assertNotNull(dependiente.getId());
        this.mockMvc.perform(get(Constantes.PATH_DEPENDIENTE_VER + "/" + dependiente.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DEPENDIENTE_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_DEPENDIENTE));
    }

    @Test
    public void debieraCrearDependiente() throws Exception {
        log.debug("Debiera crear dependiente");
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        currentSession().save(dependiente);
        assertNotNull(dependiente.getId());
        this.mockMvc.perform(post(Constantes.PATH_DEPENDIENTE_CREA)
                .param("tipoDependiente",dependiente.getTipoDependiente().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE));
    }

    @Test
    public void debieraActualizarDependiente() throws Exception {
        log.debug("Debiera actualizar dependiente");
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        currentSession().save(dependiente);
        assertNotNull(dependiente.getId());

        this.mockMvc.perform(post(Constantes.PATH_DEPENDIENTE_ACTUALIZA)
                .param("id", dependiente.getId().toString())
                .param("tipoDependiente", dependiente.getTipoDependiente().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "dependiente.actualizado.message"));

    }

    @Test
    public void debieraEliminarDependiente() throws Exception {
        log.debug("Debiera eliminar dependiente");
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        currentSession().save(dependiente);
        assertNotNull(dependiente.getId());

        this.mockMvc.perform(post(Constantes.PATH_DEPENDIENTE_ELIMINA)
                .param("id", dependiente.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "dependiente.eliminado.message"));
    }
}
