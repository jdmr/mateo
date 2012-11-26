/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.model.Colegio;
import mx.edu.um.mateo.rh.model.EstudiosEmpleado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ColegioDaoTest {

    @Autowired
    private ColegioDao colegioDao;
    private static final Logger log = LoggerFactory.getLogger(ColegioDaoTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public ColegioDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of lista method, of class ColegioDao.
     */
    @Test
    public void testLista() {
        log.debug("lista");
        for (int i = 0; i < 20; i++) {
            Colegio colegio = new Colegio();
            colegio.setNombre("Test");
            colegio.setStatus("A");
            colegioDao.grabaColegio(colegio);
            assertNotNull(colegio.getId());
        }
        Map<String, Object> params = null;
        Map result = colegioDao.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_COLEGIOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Colegio>) result.get(Constantes.CONTAINSKEY_COLEGIOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class ColegioDao.
     */
    @Test
    public void testObtiene() {
        log.debug("obtiene");
        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegioDao.grabaColegio(colegio);
        assertNotNull(colegio.getId());
        Colegio prueba = colegioDao.getColegio(colegio.getId());
        assertEquals(prueba.getId(), colegio.getId());
    }

    /**
     * Test of crea method, of class ColegioDao.
     */
    @Test
    public void testCrea() {
        log.debug("crea");
        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegioDao.grabaColegio(colegio);
        assertNotNull(colegio.getId());
    }

    /**
     * Test of actualiza method, of class ColegioDao.
     */
    @Test
    public void testActualiza() {
        log.debug("actualiza");
        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegioDao.grabaColegio(colegio);
        assertNotNull(colegio.getId());
        colegio.setNombre("prueba");
        colegioDao.grabaColegio(colegio);
        String prueba = "prueba";
        assertEquals(prueba, colegio.getNombre());

    }

    /**
     * Test of elimina method, of class ColegioDao.
     */
    @Test
    public void testElimina() throws Exception {
        log.debug("elimina");
        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegioDao.grabaColegio(colegio);
        assertNotNull(colegio.getId());
        colegioDao.removeColegio(colegio.getId());
        //assertEquals("I", colegio.getStatus());
    }
}
