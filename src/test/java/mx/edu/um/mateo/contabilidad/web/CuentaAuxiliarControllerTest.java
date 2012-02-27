/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.web;

import mx.edu.um.mateo.contabilidad.dao.CuentaAuxiliarDao;
import mx.edu.um.mateo.contabilidad.model.CuentaAuxiliar;
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
 * @author semdariobarbaamaya
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
public class CuentaAuxiliarControllerTest extends BaseTest{
    
    private static final Logger log = LoggerFactory.getLogger(CuentaAuxiliarControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CuentaAuxiliarDao ctaAuxiliarDao;

    public CuentaAuxiliarControllerTest() {
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
        this.mockMvc.perform(get("/contabilidad/auxiliar")).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/contabilidad/auxiliar/lista.jsp")).
                andExpect(model().attributeExists("auxiliares")).
                andExpect(model().attributeExists("paginacion")).
                andExpect(model().attributeExists("paginas")).
                andExpect(model().attributeExists("pagina"));
    }

    @Test
    public void debieraMostrarCtaAuxiliar() throws Exception {
        CuentaAuxiliar ctaAuxiliar = new CuentaAuxiliar("test", "test");
        ctaAuxiliar = ctaAuxiliarDao.crea(ctaAuxiliar);

        this.mockMvc.perform(get("/contabilidad/auxiliar/ver/" + ctaAuxiliar.getId())).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/contabilidad/auxiliar/ver.jsp")).
                andExpect(model().attributeExists("ctaAuxiliar"));
    }
    
    @Test
    public void debieraCrearCtaAuxiliar() throws Exception {
        log.debug("Debiera crear ctaAuxiliar");
        CuentaAuxiliar ctaAuxiliar = new CuentaAuxiliar("test", "test");
        ctaAuxiliar = ctaAuxiliarDao.crea(ctaAuxiliar);

        this.mockMvc.perform(post("/contabilidad/auxiliar/crea").
                param("nombre", "test").
                param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaAuxiliar.creada.message"));
    }
    
     @Test
    public void debieraActualizarCtaAuxiliar() throws Exception {
        log.debug("Debiera actualizar ctaAuxiliar");

        this.mockMvc.perform(post("/contabilidad/auxiliar/actualiza").param("nombre", "test1").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaAuxiliar.actualizada.message"));
    }

    @Test
    public void debieraEliminarCtaAuxiliar() throws Exception {
        log.debug("Debiera eliminar ctaAuxiliar");
        CuentaAuxiliar ctaAuxiliar = new CuentaAuxiliar("test", "test");
        ctaAuxiliarDao.crea(ctaAuxiliar);

        this.mockMvc.perform(post("/contabilidad/auxiliar/elimina").param("id", ctaAuxiliar.getId().toString())).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "ctaAuxiliar.eliminada.message"));
    }
}
