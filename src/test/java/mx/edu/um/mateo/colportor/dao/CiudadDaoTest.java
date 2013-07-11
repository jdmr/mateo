/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.Ciudad;
import mx.edu.um.mateo.colportor.model.Estado;
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
public class CiudadDaoTest {

    private static final Logger log = LoggerFactory.getLogger(CiudadDaoTest.class);
    @Autowired
    private CiudadDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public CiudadDaoTest() {
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
     * Test of lista method, of class CiudadDao.
     */
    @Test
    public void deberiaMostrarListaDeCiudad() {
        log.debug("Debiera mostrar lista de Ciudads");
        Estado estado = new Estado(Constantes.NOMBRE);
        currentSession().save(estado);
        for (int i = 0; i < 20; i++) {
            Ciudad ciudad = new Ciudad(Constantes.NOMBRE + i);
            ciudad.setEstado(estado);
            currentSession().save(ciudad);
            assertNotNull(ciudad);
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CIUDAD_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Ciudad>) result.get(Constantes.CIUDAD_LIST)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class CiudadDao.
     */
    @Test
    public void debieraObtenerCiudad() {
        log.debug("Debiera obtener Ciudads");
        Estado estado = new Estado(Constantes.NOMBRE);
        currentSession().save(estado);
        String nombre = "test";
        Ciudad ciudad = new Ciudad(Constantes.NOMBRE);
        ciudad.setEstado(estado);
        currentSession().save(ciudad);
        assertNotNull(ciudad.getId());
        Long id = ciudad.getId();
        Ciudad result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());
        assertEquals(result, ciudad);
    }

    /**
     * Test of crea method, of class CiudadDao.
     */
    @Test
    public void deberiaCrearCiudad() {
        log.debug("Deberia crear Ciudad");
        Estado estado = new Estado(Constantes.NOMBRE);
        currentSession().save(estado);
        Ciudad ciudad = new Ciudad(Constantes.NOMBRE);
        ciudad.setEstado(estado);
        assertNotNull(ciudad);
        Ciudad ciudad2 = instance.crea(ciudad);
        assertNotNull(ciudad2);
        assertNotNull(ciudad2.getId());
        assertEquals(ciudad, ciudad2);
    }

    /**
     * Test of actualiza method, of class CiudadDao.
     */
    @Test
    public void deberiaActualizarCiudad() {
        log.debug("Deberia actualizar Ciudads");
        Estado estado = new Estado(Constantes.NOMBRE);
        currentSession().save(estado);
        Ciudad ciudad = new Ciudad("test");
        assertNotNull(ciudad);
        ciudad.setEstado(estado);
        currentSession().save(ciudad);
        String nombre = "test1";
        ciudad.setNombre(nombre);
        Ciudad ciudad2 = instance.actualiza(ciudad);
        assertNotNull(ciudad2);
        assertEquals(nombre, ciudad.getNombre());
        assertEquals(ciudad, ciudad2);
    }

    /**
     * Test of elimina method, of class CiudadDao.
     */
    @Test
    public void deberiaEliminarCiudad() throws UltimoException {
        log.debug("Debiera eliminar Ciudad");
        Estado estado = new Estado(Constantes.NOMBRE);
        currentSession().save(estado);
        String nom = "test";
        Ciudad ciudad = new Ciudad(Constantes.NOMBRE);
        ciudad.setEstado(estado);
        currentSession().save(ciudad);
        assertNotNull(ciudad);
        String nombre = instance.elimina(ciudad.getId());
        assertEquals(nom, nombre);
        Ciudad prueba = instance.obtiene(ciudad.getId());
        if (prueba != null) {
            fail("Fallo prueba Eliminar");
        }
    }
}
