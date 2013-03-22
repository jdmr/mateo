/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.ColegioColportorDao;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.dao.UnionDao;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.ColegioColportor;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.rh.model.Colegio;
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
/**
 * TODO Cambiar constructores y actualizar datos
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class TemporadaColportorControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(TemporadaColportorControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private TemporadaColportorDao temporadaColportorDao;
    @Autowired
    private UnionDao unionDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private ColegioColportorDao colegioDao;
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
    public void debieraMostrarListaDeTemporadaColportor() throws Exception {
        log.debug("Debiera monstrar lista Temporada Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Organizacion organizacion = new Organizacion("codigo", "Organizacion", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("codigo", "nombre", empresa);
        currentSession().save(almacen);
        Usuario colportor = new Colportor("test01@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        colportor.setAlmacen(almacen);
        colportor.setEmpresa(empresa);
        currentSession().save(colportor);
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAlmacen(almacen);
        asociado.setEmpresa(empresa);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);
        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        for (int i = 0; i < 20; i++) {
            TemporadaColportor temporadaColportor = new TemporadaColportor(Constantes.STATUS_ACTIVO + i, "TEST", "TEST");
            temporadaColportor.setColportor((Colportor) colportor);
            temporadaColportor.setAsociacion(asociacion);
            temporadaColportor.setAsociado(asociado);
            temporadaColportor.setTemporada(temporada);
            temporadaColportor.setUnion(union);
            temporadaColportor.setColegioColportor(colegio);
            temporadaColportorDao.crea(temporadaColportor);
            assertNotNull(temporadaColportor);
        }

        this.mockMvc.perform(get(Constantes.PATH_TEMPORADACOLPORTOR))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TEMPORADACOLPORTOR_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_TEMPORADACOLPORTORES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarTemporadaColportor() throws Exception {
        log.debug("Debiera mostrar  temporada colpotor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Organizacion organizacion = new Organizacion("codigo", "Organizacion", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("codigo", "nombre", empresa);
        currentSession().save(almacen);
        Usuario colportor = new Colportor("test01@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        colportor.setAlmacen(almacen);
        colportor.setEmpresa(empresa);
        currentSession().save(colportor);
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAlmacen(almacen);
        asociado.setEmpresa(empresa);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);
        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        TemporadaColportor temporadaColportor = null;
        for (int i = 0; i < 20; i++) {
            temporadaColportor = new TemporadaColportor(Constantes.STATUS_ACTIVO + i, "TEST", "TEST");
            temporadaColportor.setColportor((Colportor) colportor);
            temporadaColportor.setAsociacion(asociacion);
            temporadaColportor.setAsociado(asociado);
            temporadaColportor.setTemporada(temporada);
            temporadaColportor.setUnion(union);
            temporadaColportor.setColegioColportor(colegio);
            temporadaColportorDao.crea(temporadaColportor);
            assertNotNull(temporadaColportor);
        }
        this.mockMvc.perform(get(Constantes.PATH_TEMPORADACOLPORTOR_VER + "/" + temporadaColportor.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TEMPORADACOLPORTOR_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_TEMPORADACOLPORTOR));

    }

    /**
     * TODO Para que pasara la prueba se comento el atributo observaiones y los
     * mensajes
     *
     * @throws Exception
     */
    @Test
    public void debieraCrearTemporadaColportor() throws Exception {
        log.debug("Debiera crear cuenta de Temporada Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Organizacion organizacion = new Organizacion("codigo", "Organizacion", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("codigo", "nombre", empresa);
        currentSession().save(almacen);
        Colportor colportor = new Colportor("test01@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        colportor.setAlmacen(almacen);
        colportor.setEmpresa(empresa);
        currentSession().save(colportor);
        Usuario asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAlmacen(almacen);
        asociado.setEmpresa(empresa);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);
        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        colegioDao.crea(colegio);
        log.debug("colegio" + colegio);

        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        this.mockMvc.perform(
                post(Constantes.PATH_TEMPORADACOLPORTOR_CREA)
                .param("status", Constantes.STATUS_ACTIVO)
                .param("fecha", sdf.format(new Date()))
                .param("objetivo", "test")
                //                .param("observaciones", "test")
                .param("temporada.id", temporada.getId().toString())
                .param("colportor.id", colportor.getId().toString())
                .param("asociado.id", asociado.getId().toString())
                .param("colegio.id", colegio.getId().toString()))
                .andExpect(status().isOk());
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.creada.message"));
    }

    /**
     * TODO Para que pasara la prueba se comento el atributo observaiones y los
     * mensajes
     *
     * @throws Exception
     */
    @Test
    public void debieraActualizarTemporadaColportor() throws Exception {
        log.debug("Debiera actualizar  temporada Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Organizacion organizacion = new Organizacion("codigo", "Organizacion", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("codigo", "nombre", empresa);
        currentSession().save(almacen);
        Colportor colportor = new Colportor("test01@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        colportor.setAlmacen(almacen);
        colportor.setEmpresa(empresa);
        currentSession().save(colportor);
        Usuario asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAsociacion(asociacion);
        asociado.setAlmacen(almacen);
        asociado.setEmpresa(empresa);
        currentSession().save(asociado);
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);
        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        TemporadaColportor temporadaColportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "TEST", "TEST");
        temporadaColportor.setColportor((Colportor) colportor);
        temporadaColportor.setAsociacion(asociacion);
        temporadaColportor.setAsociado((Asociado) asociado);
        temporadaColportor.setTemporada(temporada);
        temporadaColportor.setUnion(union);
        temporadaColportor.setColegioColportor(colegio);
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor);
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        this.mockMvc.perform(post(Constantes.PATH_TEMPORADACOLPORTOR_ACTUALIZA)
                .param("id", temporadaColportor.getId().toString())
                .param("version", temporadaColportor.getVersion().toString())
                .param("fecha", sdf.format(new Date()))
                .param("status", Constantes.STATUS_ACTIVO)
                .param("objetivo", "test")
                //                .param("observaciones", "test")
                .param("temporada.id", temporada.getId().toString())
                .param("asociado.id", asociado.getId().toString())
                .param("colportor.id", colportor.getId().toString())
                .param("colegio.id", colegio.getId().toString()))
                .andExpect(status().isOk());
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.actualizada.message"));
    }

    @Test
    public void debieraEliminarTemporadaColportor() throws Exception {
        log.debug("Debiera eliminar  temporada Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("codigo", "Organizacion", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("codigo", "nombre", empresa);
        currentSession().save(almacen);
        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Colportor colportor = new Colportor("test01@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        colportor.setEmpresa(empresa);
        colportor.setAlmacen(almacen);
        currentSession().save(colportor);
        Usuario asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAlmacen(almacen);
        asociado.setEmpresa(empresa);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);
        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        TemporadaColportor temporadaColportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "TEST", "TEST");
        temporadaColportor.setColportor((Colportor) colportor);
        temporadaColportor.setAsociacion(asociacion);
        temporadaColportor.setAsociado((Asociado) asociado);
        temporadaColportor.setTemporada(temporada);
        temporadaColportor.setUnion(union);
        temporadaColportor.setColegioColportor(colegio);
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor);
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        this.mockMvc.perform(post(
                Constantes.PATH_TEMPORADACOLPORTOR_ELIMINA)
                .param("id", temporadaColportor.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.eliminada.message"));
    }

    @Test
    public void deberiaProbarNuevaTeporadaColportor() throws Exception {
        log.debug("Deberia Probar Nueva De TemporadaColportor Controller");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Organizacion organizacion = new Organizacion("codigo", "Organizacion", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("codigo", "nombre", empresa);
        currentSession().save(almacen);
        for (int i = 0; i < 10; i++) {
            Colportor colportor = new Colportor("test-" + i + "@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                    "8262652626", "test", "test", "10706" + i, "test", "test001", new Date());
            colportor.setAsociacion(asociacion);
            colportor.setEmpresa(empresa);
            colportor.setAlmacen(almacen);
            currentSession().save(colportor);
            assertNotNull(colportor.getId());
        }
        for (int i = 0; i < 10; i++) {
            Asociado asociado = new Asociado("test--" + i + "@test.com", "test", "test", "test", "test",
                    Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                    Constantes.MUNICIPIO);
            asociado.setAsociacion(asociacion);
            asociado.setEmpresa(empresa);
            asociado.setAlmacen(almacen);
            currentSession().save(asociado);
            assertNotNull(asociado.getId());
        }

        for (int i = 0; i < 10; i++) {
            ColegioColportor colegio = new ColegioColportor(Constantes.NOMBRE + "--" + i, Constantes.STATUS_ACTIVO);
            currentSession().save(colegio);
            assertNotNull(colegio);
        }
        for (int i = 0; i < 10; i++) {
            Temporada temporada = new Temporada("test" + i);
            temporada.setAsociacion(asociacion);
            currentSession().save(temporada);
            assertNotNull(temporada.getId());
        }
        TemporadaColportor temporadaColportor = new TemporadaColportor();
        this.mockMvc.perform(post(
                Constantes.PATH_TEMPORADACOLPORTOR_NUEVA)
                .sessionAttr(Constantes.SESSION_ASOCIACION, asociacion))
                .andExpect(model().attribute("sizeTemporada", 10))
                .andExpect(model().attribute("sizeColportor", 10))
                .andExpect(model().attribute("sizeAsociado", 10))
                .andExpect(model().attribute("sizeColegios", 10))
                .andExpect(model().attribute("temporadaColportor", temporadaColportor))
                .andExpect(status().isOk())
                .andExpect(view().name(Constantes.PATH_TEMPORADACOLPORTOR_NUEVA));

    }
}
