/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.Pais;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.utils.Constantes;
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
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class PaisDaoTest {

    private static final Logger log = LoggerFactory.getLogger(PaisDaoTest.class);
    @Autowired
    private PaisDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public PaisDaoTest() {
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
     * Test of lista method, of class PaisDao.
     */
    @Test
    public void deberiaMostrarListaDePais() {
        log.debug("Debiera mostrar lista de Pais");
        for (int i = 0; i < 20; i++) {
            Pais pais = new Pais(Constantes.NOMBRE + i);
            currentSession().save(pais);
            assertNotNull(pais);
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.PAIS_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Pais>) result.get(Constantes.PAIS_LIST)).size());
        //assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class PaisDao.
     */
    @Test
    public void debieraObtenerPais() {
        log.debug("Debiera obtener Pais");
        String nombre = "test";
        Pais pais = new Pais(Constantes.NOMBRE);
        currentSession().save(pais);
        assertNotNull(pais.getId());
        Long id = pais.getId();
        Pais result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());
        assertEquals(result, pais);
    }

    /**
     * Test of crea method, of class PaisDao.
     */
    @Test
    public void deberiaCrearPais() {
        log.debug("Deberia crear Pais");
        Pais pais = new Pais(Constantes.NOMBRE);
        assertNotNull(pais);
        Pais pais2 = instance.crea(pais);
        assertNotNull(pais2);
        assertNotNull(pais2.getId());
        assertEquals(pais, pais2);
    }

    /**
     * Test of actualiza method, of class PaisDao.
     */
    @Test
    public void deberiaActualizarPais() {
        log.debug("Deberia actualizar Pais");
        Pais pais = new Pais("test");
        assertNotNull(pais);
        currentSession().save(pais);
        String nombre = "test1";
        pais.setNombre(nombre);
        Pais pais2 = instance.actualiza(pais);
        assertNotNull(pais2);
        assertEquals(nombre, pais.getNombre());
        assertEquals(pais, pais2);
    }

    /**
     * Test of elimina method, of class PaisDao.
     */
    @Test
    public void deberiaEliminarPais() throws UltimoException {
        log.debug("Debiera eliminar Pais");
        String nom = "test";
        Pais pais = new Pais(Constantes.NOMBRE);
        currentSession().save(pais);
        assertNotNull(pais);
        String nombre = instance.elimina(pais.getId());
        assertEquals(nom, nombre);
        Pais prueba = instance.obtiene(pais.getId());

        if (prueba != null) {
            fail("Fallo la prueba Eliminar");
        }
    }
}
