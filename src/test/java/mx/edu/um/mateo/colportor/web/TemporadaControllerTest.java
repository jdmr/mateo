/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.AsociacionDao;
import mx.edu.um.mateo.colportor.dao.TemporadaDao;
import mx.edu.um.mateo.colportor.dao.UnionDao;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.Union;
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
public class TemporadaControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(TemporadaControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private TemporadaDao temporadaDao;
    @Autowired
    private UnionDao unionDao;
    @Autowired
    private AsociacionDao asociacionDao;
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

    /**
     * 
     * @throws Exception 
     */
    @Test
    public void debieraMostrarListaDeTemporada() throws Exception {
        log.debug("Debiera monstrar lista TEmporada");

        Union union = new Union("test");
        union = unionDao.crea(union);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        asociacionDao.crea(asociacion);

        for (int i = 0; i < 20; i++) {
            Temporada temporada = new Temporada("test" + i);
            temporada.setAsociacion(asociacion);
            currentSession().save(temporada);
            assertNotNull(temporada.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_TEMPORADA)
                .sessionAttr(Constantes.SESSION_ASOCIACION, asociacion))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TEMPORADA_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_TEMPORADAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA))
                .andExpect(model().attribute("SizeTemporadas", 10));

    }


    @Test
    public void debieraMostrarTemporada() throws Exception {
        log.debug("Debiera mostrar  temporada");
        Union union = new Union("test");
        union = unionDao.crea(union);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        temporada = temporadaDao.crea(temporada);
        assertNotNull(temporada);

        this.mockMvc.perform(get(Constantes.PATH_TEMPORADA_VER + "/" + temporada.getId())
                .sessionAttr(Constantes.SESSION_ASOCIACION, asociacion))
                .andExpect(status().isOk())
                .andExpect(view().name(Constantes.PATH_TEMPORADA_VER));
    }

    @Test
    public void debieraCrearTemporada() throws Exception {
        log.debug("Debiera crear temporada");
        Union union = new Union("test");
        union = unionDao.crea(union);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        asociacionDao.crea(asociacion);
        
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        this.mockMvc.perform(post(Constantes.PATH_TEMPORADA_CREA)
                .param("nombre", "test")
                .param("fechaInicio", sdf.format(new Date()))
                .param("fechaFinal", sdf.format(new Date()))
                .sessionAttr(Constantes.SESSION_ASOCIACION, asociacion))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.creada.message"));
    }

    @Test
    public void debieraActualizarTemporada() throws Exception {
        log.debug("Debiera actualizar  temporada");
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        Union union = new Union("test");
        union = unionDao.crea(union);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        temporada = temporadaDao.crea(temporada);
        assertNotNull(temporada);

        this.mockMvc.perform(post(Constantes.PATH_TEMPORADA_ACTUALIZA)
                .param("id", temporada.getId().toString())
                .param("version", temporada.getVersion().toString())
                .param("nombre", "test1")
                .param("fechaInicio", sdf.format(new Date()))
                .param("fechaFinal", sdf.format(new Date()))
                .sessionAttr(Constantes.SESSION_ASOCIACION, asociacion))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.actualizada.message"));
    }

    @Test
    public void debieraEliminarTemporada() throws Exception {
        log.debug("Debiera eliminar  temporada");
        Union union = new Union("test");
        union = unionDao.crea(union);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        asociacionDao.crea(asociacion);
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        temporadaDao.crea(temporada);
        assertNotNull(temporada);

        this.mockMvc.perform(post(Constantes.PATH_TEMPORADA_ELIMINA)
                .param("id", temporada.getId().toString())
                .sessionAttr(Constantes.SESSION_ASOCIACION, asociacion))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.eliminada.message"));
    }
}
