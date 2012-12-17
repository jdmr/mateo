/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.ColegioColportorDao;
import mx.edu.um.mateo.colportor.model.ColegioColportor;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
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
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class ColegioColportorControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ColegioColportorControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private ColegioColportorDao colegioDao;

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
    //PRUEBA PASO 100%
    public void debieraMostrarListaDeColegio() throws Exception {
        log.debug("Debiera monstrar lista de colegioes");

        for (int i = 0; i < 20; i++) {
            ColegioColportor colegio = new ColegioColportor(Constantes.NOMBRE + i, Constantes.STATUS_ACTIVO);
            colegioDao.crea(colegio);
            assertNotNull(colegio);
        }

        this.mockMvc.perform(get(Constantes.PATH_COLEGIO_COLPORTOR))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLEGIO_COLPORTOR_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_COLEGIOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraMostrarColegio() throws Exception {
        log.debug("Debiera mostrar colegio");
        ColegioColportor colegio = new ColegioColportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        colegio = colegioDao.crea(colegio);
        assertNotNull(colegio);

        this.mockMvc.perform(get(Constantes.PATH_COLEGIO_COLPORTOR_VER + "/" + colegio.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLEGIO_COLPORTOR_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_COLEGIO));
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraCrearColegio() throws Exception {
        log.debug("Debiera crear colegio");

        this.mockMvc.perform(post(Constantes.PATH_COLEGIO_COLPORTOR_CREA)
                .param("nombre", "test")
                .param("status", Constantes.STATUS_ACTIVO))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.creado.message"));
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraActualizarColegio() throws Exception {
        log.debug("Debiera actualizar colegio");
        ColegioColportor colegio = new ColegioColportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        colegio = colegioDao.crea(colegio);
        assertNotNull(colegio);

        this.mockMvc.perform(post(Constantes.PATH_COLEGIO_COLPORTOR_ACTUALIZA)
                .param("id", colegio.getId().toString())
                .param("version", colegio.getVersion().toString())
                .param("nombre", "test1")
                .param("status", colegio.getStatus()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.actualizado.message"));
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraEliminarColegio() throws Exception {
        log.debug("Debiera eliminar colegio");
        ColegioColportor colegio = new ColegioColportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        colegioDao.crea(colegio);
        assertNotNull(colegio);

        this.mockMvc.perform(post(Constantes.PATH_COLEGIO_COLPORTOR_ELIMINA)
                .param("id", colegio.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.eliminado.message"));
    }
}