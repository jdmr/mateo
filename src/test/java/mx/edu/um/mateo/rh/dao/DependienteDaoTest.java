/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import mx.edu.um.mateo.rh.model.Dependiente;
import mx.edu.um.mateo.rh.model.TipoDependiente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public class DependienteDaoTest {

    @Autowired
    private DependienteDao dependienteDao;
    private static final Logger log = LoggerFactory.getLogger(DependienteDaoTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public DependienteDaoTest() {
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

    @Test
    public void deberiaCrearDependiente() {
        log.debug("Deberia crear Dependiente");
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        currentSession().save(dependiente);
        assertNotNull(dependiente.getId());

    }

    @Test
    public void deberiaActualizarDependiente() {
        log.debug("Deberia actulizar Dependiente");
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        dependienteDao.crea(dependiente);
        assertNotNull(dependiente.getId());
        dependiente.setTipoDependiente(TipoDependiente.ESPOSO);
        dependienteDao.actualiza(dependiente);
        Dependiente dependiente1 = dependienteDao.obtiene(dependiente.getId());
        assertNotNull(dependiente1);
        assertEquals(TipoDependiente.ESPOSO, dependiente1.getTipoDependiente());
    }
    @Test
    public void deberiaEliminarDependiente()throws Exception{
        log.debug("Deberia eliminar Dependiente");
         Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        dependienteDao.crea(dependiente);
        assertNotNull(dependiente.getId());
        String tipo=dependienteDao.elimina(dependiente.getId());
        assertNotNull(tipo);
        assertEquals(tipo, dependiente.getTipoDependiente().toString());
    }
}