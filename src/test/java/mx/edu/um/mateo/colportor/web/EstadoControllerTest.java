/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import mx.edu.um.mateo.colportor.dao.EstadoDao;
import mx.edu.um.mateo.colportor.model.Estado;
import mx.edu.um.mateo.colportor.model.Pais;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.dao.*;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
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
 * @author gibrandemetrioo
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class EstadoControllerTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(EstadoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private EstadoDao estadoDao;
    @Autowired
    private SessionFactory sessionFactory;
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
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
    public void debieraMostrarListaDeEstado() throws Exception {
        log.debug("Debiera monstrar lista estado");
        Pais pais = new Pais("test3");
        currentSession().save(pais);
        for (int i = 0; i < 20; i++) {
            Estado estado = new Estado(Constantes.NOMBRE+i);
            estadoDao.crea(estado);
            assertNotNull(estado);
        }

        this.mockMvc.perform(get(Constantes.PATH_ESTADO))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ESTADO_LISTA+ ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_ESTADOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void debieraMostrarEstado() throws Exception {
        log.debug("Debiera mostrar  Estado");
        Pais pais = new Pais("test3");
        currentSession().save(pais);
        Estado estado = new Estado(Constantes.NOMBRE);
        estado.setPais(pais);
        estado = estadoDao.crea(estado);
        assertNotNull(estado);

        this.mockMvc.perform(get(Constantes.PATH_ESTADO_VER +"/"+ estado.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ESTADO_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_ESTADO));
    }
    @Test
    public void debieraCrearEstado() throws Exception {
        log.debug("Debiera crear Estado");
        Pais pais = new Pais("test3");
        currentSession().save(pais);
        this.mockMvc.perform(
                post(Constantes.PATH_ESTADO_CREA)
                .param("id", "t")
                .param("version", "id")
                .param("nombre", Constantes.NOMBRE)
                .param("pais", pais.getId().toString()))
                .andExpect(status().isOk());
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "estado.creada.message"));
    }
    @Test
    public void debieraActualizarPais() throws Exception {
        log.debug("Debiera actualizar  pais");
        Pais pais = new Pais("test3");
        currentSession().save(pais);
        Estado estado = new Estado(Constantes.NOMBRE);
        estado.setPais(pais);
        estado = estadoDao.crea(estado);
        assertNotNull(estado);
   
        this.mockMvc.perform(post(Constantes.PATH_ESTADO_ACTUALIZA)
                .param("id","t")
                .param("version","id")
                .param("nombre", Constantes.NOMBRE)
                .param("pais", pais.getId().toString()))
                .andExpect(status().isOk());
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "estado.actualizada.message"));
}
    @Test
    public void debieraEliminarEstado() throws Exception {
        log.debug("Debiera eliminar Estado");
        Estado estado = new Estado("test");
        estadoDao.crea(estado);
        assertNotNull(estado);

        this.mockMvc.perform(post(Constantes.PATH_ESTADO_ELIMINA)
                .param("id", estado.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "estado.eliminada.message"));
    }
}
