/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.web;

import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.dao.CuentaResultadoDao;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
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
public class CuentaResultadoControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CuentaResultadoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CuentaResultadoDao cuentaResultadoDao;

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
        log.debug("Debiera monstrar lista de cuentaResultados");
        
        this.mockMvc.perform(get(Constantes.PATH_CUENTA_RESULTADO)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.PATH_CUENTA_RESULTADO_LISTA+".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_RESULTADOS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarCtaResultado() throws Exception {

  log.debug("Debiera mostrar cuenta de mayor");
        CuentaResultado cuentaResultado = new CuentaResultado("test", "test");
        cuentaResultado = cuentaResultadoDao.crea(cuentaResultado);
        assertNotNull(cuentaResultado);

        this.mockMvc.perform(get(Constantes.PATH_CUENTA_RESULTADO_VER +"/"+ cuentaResultado.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CUENTA_RESULTADO_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_RESULTADO));
    }

    @Test
    public void debieraCrearCtaResultado() throws Exception {
        log.debug("Debiera crear cuentaResultado");
        
        this.mockMvc.perform(post(Constantes.PATH_CUENTA_RESULTADO_CREA).param("nombre", "test").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE)).
                andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaResultado.creada.message"));
    }

    @Test
    public void debieraActualizarCtaResultado() throws Exception {
        log.debug("Debiera actualizar cuentaResultado");

        this.mockMvc.perform(post(Constantes.PATH_CUENTA_RESULTADO_ACTUALIZA).param("nombre", "test1").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE)).
                andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaResultado.actualizada.message"));
    }

    @Test
    public void debieraEliminarCtaResultado() throws Exception {
        log.debug("Debiera eliminar cuentaResultado");
        CuentaResultado cuentaResultado = new CuentaResultado("test", "test");
        cuentaResultadoDao.crea(cuentaResultado);

        this.mockMvc.perform(post(Constantes.PATH_CUENTA_RESULTADO_ELIMINA).param("id", cuentaResultado.getId().toString())).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE)).
                andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaResultado.eliminada.message"));
    }
}
