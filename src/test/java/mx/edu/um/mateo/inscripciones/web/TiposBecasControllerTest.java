/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.TiposBecasDao;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class TiposBecasControllerTest extends BaseTest {

    @Autowired
    private TiposBecasDao tipoBecasDao;
    private static final Logger log = LoggerFactory.getLogger(TiposBecasControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public TiposBecasControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of lista method, of class TiposBecasController.
     */
    @Test
    public void debieraMostrarListaDeTiposBecas() throws Exception {
        log.debug("Debiera mostrar lista de tipos Becas");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        for (int i = 0; i < 20; i++) {
            TiposBecas tiposBecas = new TiposBecas();
            tiposBecas.setDescripcion("test" + i);
            tiposBecas.setDiezma(true);
            tiposBecas.setNumHoras(320);
            tiposBecas.setPerteneceAlumno(true);
            tiposBecas.setPorcentaje(new BigDecimal(12.3));
            tiposBecas.setSoloPostgrado(false);
            tiposBecas.setStatus("a");
            tiposBecas.setTope(new BigDecimal(320));
            tiposBecas.setEmpresa(empresa);
            currentSession().save(tiposBecas);
        }

        this.mockMvc.perform(get(Constantes.PATH_TIPOSBECAS)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPOSBECAS_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_TIPOSBECAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    /**
     * Test of ver method, of class TiposBecasController.
     */
    @Test
    public void debieraMostrarTipoBeca() throws Exception {
        log.debug("Debiera mostrar un tipo de beca");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(true);
        tiposBecas.setPorcentaje(new BigDecimal(12.3));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(320));
        tiposBecas.setEmpresa(empresa);
        currentSession().save(tiposBecas);
        assertNotNull(tiposBecas.getId());
        this.mockMvc.perform(get(Constantes.PATH_TIPOSBECAS_VER + "/" + tiposBecas.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPOSBECAS_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_TIPOSBECAS));
    }

    /**
     * Test of elimina method, of class TiposBecasController.
     */
    @Test
    public void testElimina() throws Exception {
        log.debug("Debiera mostrar un tipo de beca");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(true);
        tiposBecas.setPorcentaje(new BigDecimal(12.3));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(320));
        tiposBecas.setEmpresa(empresa);
        currentSession().save(tiposBecas);
        assertNotNull(tiposBecas.getId());
        this.mockMvc.perform(post(Constantes.PATH_TIPOSBECAS_ELIMINA)
                .param("id", tiposBecas.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "tiposBecas.elimina.message"));
    }
}
