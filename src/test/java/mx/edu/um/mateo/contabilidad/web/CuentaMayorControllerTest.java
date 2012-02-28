/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.web;

import mx.edu.um.mateo.contabilidad.dao.CuentaMayorDao;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
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
public class CuentaMayorControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CuentaMayorControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CuentaMayorDao ctaMayorDao;

    public CuentaMayorControllerTest() {
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
    public void debieraMostrarListaDeCtaMayor() throws Exception {
        log.debug("Debiera monstrar lista de ctaMayor");
        
        this.mockMvc.perform(get("/contabilidad/mayor")).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/contabilidad/mayor/lista.jsp")).
                andExpect(model().attributeExists("ctaMayores")).
                andExpect(model().attributeExists("paginacion")).
                andExpect(model().attributeExists("paginas")).
                andExpect(model().attributeExists("pagina"));
    }

    @Test
    public void debieraMostrarCtaMayor() throws Exception {
        log.debug("Debiera mostrar ctaMayor");
        CuentaMayor ctaMayor = new CuentaMayor("test", "test");
        ctaMayor = ctaMayorDao.crea(ctaMayor);

        this.mockMvc.perform(get("/contabilidad/mayor/ver/" + ctaMayor.getId())).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/contabilidad/mayor/ver.jsp")).
                andExpect(model().attributeExists("ctaMayor"));
    }

    @Test
    public void debieraCrearCtaMayor() throws Exception {
        log.debug("Debiera crear ctaMayor");
        
        this.mockMvc.perform(post("/contabilidad/mayor/crea").param("nombre", "test").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaMayor.creada.message"));
    }

    @Test
    public void debieraActualizarCtaMayor() throws Exception {
        log.debug("Debiera actualizar ctaMayor");

        this.mockMvc.perform(post("/contabilidad/mayor/actualiza").param("nombre", "test1").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaMayor.actualizada.message"));
    }

    @Test
    public void debieraEliminarCtaMayor() throws Exception {
        log.debug("Debiera eliminar ctaMayor");
        CuentaMayor ctaMayor = new CuentaMayor("test", "test");
        ctaMayorDao.crea(ctaMayor);

        this.mockMvc.perform(post("/contabilidad/mayor/elimina").param("id", ctaMayor.getId().toString())).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaMayor.eliminada.message"));
    }
}
