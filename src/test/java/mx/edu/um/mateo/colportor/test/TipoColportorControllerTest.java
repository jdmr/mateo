/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.test;

import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.TipoColportorDao;
import mx.edu.um.mateo.colportor.model.TipoColportor;
import mx.edu.um.mateo.colportor.test.BaseTest;
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
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class TipoColportorControllerTest extends BaseTest{
    private static final Logger log = LoggerFactory.getLogger(TipoColportorControllerTest.class);
    
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    
    @Autowired
    private TipoColportorDao tipoColportorDao;
    
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
    public void debieraMostrarListaDeTipoColportor() throws Exception {
        log.debug("Debiera monstrar lista TipoColportor");
        for (int i = 0; i < 20; i++) {
            TipoColportor tipoColportor = new TipoColportor("test" + i,"A");
            tipoColportorDao.crea(tipoColportor);
            assertNotNull(tipoColportor.getId());
        }
        
        this.mockMvc.perform(get(Constantes.PATH_TIPO_COLPORTOR))
                .andExpect(status().isOk())                
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPO_COLPORTOR_LISTA+ ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_TIPO_COLPORTOR))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void debieraMostrarTipoColportor() throws Exception {
        log.debug("Debiera mostrar  TipoColportor");
        TipoColportor tipoColportor = new TipoColportor("test","A");
        tipoColportor = tipoColportorDao.crea(tipoColportor);
        assertNotNull(tipoColportor.getId());

        this.mockMvc.perform(get(Constantes.PATH_TIPO_COLPORTOR_VER +"/"+ tipoColportor.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPO_COLPORTOR_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_TIPO_COLPORTOR));
    }
    @Test
    public void debieraCrearTipoColportor() throws Exception {
        log.debug("Debiera crear TipoColportor");
        this.mockMvc.perform(post(Constantes.PATH_TIPO_COLPORTOR_CREA)
                .param("tipoColportor", "test")
                .param("status", "A"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "tipoColportor.creado.message"));
    }
    @Test
    public void debieraActualizarTipoColportor() throws Exception {
        log.debug("Debiera actualizar  tipoColportor");
        TipoColportor tipoColportor = new TipoColportor("test2","A");
        tipoColportor = tipoColportorDao.crea(tipoColportor);
        assertNotNull(tipoColportor.getId());

        this.mockMvc.perform(post(Constantes.PATH_TIPO_COLPORTOR_ACTUALIZA)
                .param("id",tipoColportor.getId().toString())
                .param("version", tipoColportor.getVersion().toString())
                .param("tipoColportor", "test1")
                .param("status", "A"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "tipoColportor.actualizado.message"));
    }
    @Test
    public void debieraEliminarTipoColportor() throws Exception {
        log.debug("Debiera eliminar  TipoColportor");
        TipoColportor tipoColportor = new TipoColportor("test3","A");
        tipoColportorDao.crea(tipoColportor);
        assertNotNull(tipoColportor.getId());

        this.mockMvc.perform(post(Constantes.PATH_TIPO_COLPORTOR_ELIMINA)
                .param("id", tipoColportor.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "tipoColportor.eliminado.message"));
    }
}
