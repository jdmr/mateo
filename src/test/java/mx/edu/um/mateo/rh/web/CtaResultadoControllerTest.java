/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.rh.dao.CtaResultadoDao;
import mx.edu.um.mateo.rh.dao.CtaResultadoDao;
import mx.edu.um.mateo.rh.model.CtaResultado;
import mx.edu.um.mateo.rh.model.CtaResultado;
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
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
public class CtaResultadoControllerTest {

    private static final Logger log = LoggerFactory.getLogger(CtaResultadoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CtaResultadoDao ctaResultadoDao;

    public CtaResultadoControllerTest() {
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
        log.debug("Debiera monstrar lista de ctaResultado");
        
        this.mockMvc.perform(get("/rh/ctaResultado")).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/rh/ctaResultado/lista.jsp")).
                andExpect(model().attributeExists("ctaResultados")).
                andExpect(model().attributeExists("paginacion")).
                andExpect(model().attributeExists("paginas")).
                andExpect(model().attributeExists("pagina"));
    }
 @Test
    public void debieraMostrarCtaResultado() throws Exception {
        log.debug("Debiera mostrar ctaMayor");
        CtaResultado ctaResultado = new CtaResultado("test", "test");
        ctaResultado = ctaResultadoDao.crea(ctaResultado);

        this.mockMvc.perform(get("/rh/ctaResultado/ver/" + ctaResultado.getId())).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/rh/ctaResultado/ver.jsp")).
                andExpect(model().attributeExists("ctaResultado"));
    }

    @Test
    public void debieraCrearCtaResultado() throws Exception {
        log.debug("Debiera crear ctaResultado");
        
        this.mockMvc.perform(post("/rh/ctaResultado/crea").param("nombre", "test").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaResultado.creada.message"));
    }

    @Test
    public void debieraActualizarCtaResultado() throws Exception {
        log.debug("Debiera actualizar ctaResultado");

        this.mockMvc.perform(post("/rh/ctaResultado/actualiza").param("nombre", "test1").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaResultado.actualizada.message"));
    }

    @Test
    public void debieraEliminarCtaResultado() throws Exception {
        log.debug("Debiera eliminar ctaResultado");
        CtaResultado ctaResultado = new CtaResultado("test", "test");
        ctaResultadoDao.crea(ctaResultado);

        this.mockMvc.perform(post("/rh/ctaResultado/elimina").param("id", ctaResultado.getId().toString())).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaResultado.eliminada.message"));
    }
}
