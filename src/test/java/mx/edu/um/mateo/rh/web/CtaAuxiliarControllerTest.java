/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.rh.dao.CtaAuxiliarDao;
import mx.edu.um.mateo.rh.model.CtaAuxiliar;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;

/**
 *
 * @author semdariobarbaamaya
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
public class CtaAuxiliarControllerTest extends BaseTest{
    
    private static final Logger log = LoggerFactory.getLogger(CtaAuxiliarControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CtaAuxiliarDao ctaAuxiliarDao;

    public CtaAuxiliarControllerTest() {
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
    public void debieraMostrarListaDeCtaAuxiliar() throws Exception {
        log.debug("Debiera mostrar lista de ctaAuxiliar");
        this.mockMvc.perform(get("/rh/ctaAuxiliar")).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/rh/ctaAuxiliar/lista.jsp")).
                andExpect(model().attributeExists("ctaAuxiliares")).
                andExpect(model().attributeExists("paginacion")).
                andExpect(model().attributeExists("paginas")).
                andExpect(model().attributeExists("pagina"));
    }

    @Test
    public void debieraMostrarCtaAuxiliar() throws Exception {
        CtaAuxiliar ctaAuxiliar = new CtaAuxiliar("test", "test");
        ctaAuxiliar = ctaAuxiliarDao.crea(ctaAuxiliar);

        this.mockMvc.perform(get("/rh/ctaAuxiliar/ver/" + ctaAuxiliar.getId())).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/rh/ctaAuxiliar/ver.jsp")).
                andExpect(model().attributeExists("ctaAuxiliar"));
    }
    
    @Test
    public void debieraCrearCtaAuxiliar() throws Exception {
        log.debug("Debiera crear ctaAuxiliar");
        CtaAuxiliar ctaAuxiliar = new CtaAuxiliar("test", "test");
        ctaAuxiliar = ctaAuxiliarDao.crea(ctaAuxiliar);

        this.mockMvc.perform(post("/rh/ctaAuxiliar/crea").
                param("nombre", "test").
                param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaAuxiliar.creada.message"));
    }
    
     @Test
    public void debieraActualizarCtaAuxiliar() throws Exception {
        log.debug("Debiera actualizar ctaAuxiliar");

        this.mockMvc.perform(post("/rh/ctaAuxiliar/actualiza").param("nombre", "test1").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaAuxiliar.actualizada.message"));
    }

    @Test
    public void debieraEliminarCtaAuxiliar() throws Exception {
        log.debug("Debiera eliminar ctaAuxiliar");
        CtaAuxiliar ctaAuxiliar = new CtaAuxiliar("test", "test");
        ctaAuxiliarDao.crea(ctaAuxiliar);

        this.mockMvc.perform(post("/rh/ctaAuxiliar/elimina").param("id", ctaAuxiliar.getId().toString())).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaAuxiliar.eliminada.message"));
    }
}
