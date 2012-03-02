/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.web;

import mx.edu.um.mateo.contabilidad.dao.CuentaResultadoDao;
import mx.edu.um.mateo.contabilidad.model.CuentaResultado;
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
public class CuentaResultadoControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CuentaResultadoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CuentaResultadoDao ctaResultadoDao;

    public CuentaResultadoControllerTest() {
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
    public void debieraMostrarListaDeCtaResultado() throws Exception {
        log.debug("Debiera monstrar lista de ctaResultados");
        
        this.mockMvc.perform(get("/contabilidad/resultado")).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/contabilidad/resultado/lista.jsp")).
                andExpect(model().attributeExists("ctaResultados")).
                andExpect(model().attributeExists("paginacion")).
                andExpect(model().attributeExists("paginas")).
                andExpect(model().attributeExists("pagina"));
    }

    @Test
    public void debieraMostrarCtaResultado() throws Exception {
        log.debug("Debiera mostrar ctaResultado");
        CuentaResultado ctaResultado = new CuentaResultado("test", "test");
        ctaResultado = ctaResultadoDao.crea(ctaResultado);

        this.mockMvc.perform(get("/contabilidad/resultado/ver/" + ctaResultado.getId())).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/contabilidad/resultado/ver.jsp")).
                andExpect(model().attributeExists("ctaResultado"));
    }

    @Test
    public void debieraCrearCtaResultado() throws Exception {
        log.debug("Debiera crear ctaResultado");
        
        this.mockMvc.perform(post("/contabilidad/resultado/crea").param("nombre", "test").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaResultado.creada.message"));
    }

    @Test
    public void debieraActualizarCtaResultado() throws Exception {
        log.debug("Debiera actualizar ctaResultado");

        this.mockMvc.perform(post("/contabilidad/resultado/actualiza").param("nombre", "test1").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaResultado.actualizada.message"));
    }

    @Test
    public void debieraEliminarCtaResultado() throws Exception {
        log.debug("Debiera eliminar ctaResultado");
        CuentaResultado ctaResultado = new CuentaResultado("test", "test");
        ctaResultadoDao.crea(ctaResultado);

        this.mockMvc.perform(post("/contabilidad/resultado/elimina").param("id", ctaResultado.getId().toString())).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaResultado.eliminada.message"));
    }
}
