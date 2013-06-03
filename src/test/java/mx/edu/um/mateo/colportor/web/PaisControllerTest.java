/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.PaisDao;
import mx.edu.um.mateo.colportor.model.Pais;
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
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
/**
 *
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
public class PaisControllerTest {
    private static final Logger log = LoggerFactory.getLogger(PaisControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private PaisDao paisDao;
    
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
    public void debieraMostrarListaDePais() throws Exception {
        log.debug("Debiera monstrar lista Pais");
        for (int i = 0; i < 20; i++) {
            Pais pais = new Pais("test" + i);
            paisDao.crea(pais);
            assertNotNull(pais);
        }
        this.mockMvc.perform(get(Constantes.PATH_PAIS))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PAIS_LISTA+ ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAISES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void debieraMostrarPais() throws Exception {
        log.debug("Debiera mostrar  Pais");
        Pais pais = new Pais("test");
        pais = paisDao.crea(pais);
        assertNotNull(pais);

        this.mockMvc.perform(get(Constantes.PATH_PAIS_VER +"/"+ pais.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PAIS_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_PAIS));
    }
    @Test
    public void debieraCrearPais() throws Exception {
        log.debug("Debiera crear Pais");
        this.mockMvc.perform(post(Constantes.PATH_PAIS_CREA)
                .param("nombre", "test"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "pais.creada.message"));
    }
    @Test
    public void debieraActualizarPais() throws Exception {
        log.debug("Debiera actualizar  pais");
        Pais pais = new Pais("test");
        pais = paisDao.crea(pais);
        assertNotNull(pais);

        this.mockMvc.perform(post(Constantes.PATH_PAIS_ACTUALIZA)
                .param("id",pais.getId().toString())
                .param("version", pais.getVersion().toString())
                .param("nombre", "test1"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "pais.actualizada.message"));
    }
    @Test
    public void debieraEliminarPais() throws Exception {
        log.debug("Debiera eliminar  Pais");
        Pais pais = new Pais("test");
        paisDao.crea(pais);
        assertNotNull(pais);

        this.mockMvc.perform(post(Constantes.PATH_PAIS_ELIMINA)
                .param("id", pais.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "pais.eliminada.message"));
    }
}
