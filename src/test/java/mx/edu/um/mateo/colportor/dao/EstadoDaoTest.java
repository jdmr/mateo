/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.Estado;
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
public class EstadoDaoTest {

    private static final Logger log = LoggerFactory.getLogger(EstadoDaoTest.class);
    @Autowired
    private EstadoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public EstadoDaoTest() {
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
     * Test of lista method, of class EstadoDao.
     */
    @Test
    public void deberiaMostrarListaDeEstado() {
        log.debug("Debiera mostrar lista de Estados");
        Pais pais = new Pais(Constantes.NOMBRE);
        currentSession().save(pais);
        for (int i = 0; i < 20; i++) {
            Estado estado = new Estado(Constantes.NOMBRE + i);
            estado.setPais(pais);
            currentSession().save(estado);
            assertNotNull(estado);
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_ESTADOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Estado>) result.get(Constantes.CONTAINSKEY_ESTADOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class EstadoDao.
     */
    @Test
    public void debieraObtenerEstado() {
        log.debug("Debiera obtener Estados");
        String nombre = "test";
        Pais pais = new Pais(Constantes.NOMBRE);
        currentSession().save(pais);
        Estado estado = new Estado(Constantes.NOMBRE);
        estado.setPais(pais);
        currentSession().save(estado);
        assertNotNull(estado.getId());
        Long id = estado.getId();
        Estado result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());
        assertEquals(result, estado);
    }

    /**
     * Test of crea method, of class EstadoDao.
     */
    @Test
    public void deberiaCrearEstado() {
        log.debug("Deberia crear Estado");
        Pais pais = new Pais(Constantes.NOMBRE);
        currentSession().save(pais);
        Estado estado = new Estado(Constantes.NOMBRE);
        estado.setPais(pais);
        assertNotNull(estado);
        Estado estado2 = instance.crea(estado);
        assertNotNull(estado2);
        assertNotNull(estado2.getId());
        assertEquals(estado, estado2);
    }

    /**
     * Test of actualiza method, of class EstadoDao.
     */
    @Test
    public void deberiaActualizarEstado() {
        log.debug("Deberia actualizar Estados");
        Pais pais = new Pais(Constantes.NOMBRE);
        currentSession().save(pais);
        Estado estado = new Estado("test");
        estado.setPais(pais);
        assertNotNull(estado);
        currentSession().save(estado);
        String nombre = "test1";
        estado.setNombre(nombre);
        Estado estado2 = instance.actualiza(estado);
        assertNotNull(estado2);
        assertEquals(nombre, estado.getNombre());
        assertEquals(estado, estado2);
    }

    /**
     * Test of elimina method, of class EstadoDao.
     */
    @Test
    public void deberiaEliminarEstado() throws UltimoException {
        log.debug("Debiera eliminar Estado");
        String nom = "test";
        Pais pais = new Pais(Constantes.NOMBRE);
        currentSession().save(pais);
        Estado estado = new Estado(Constantes.NOMBRE);
        estado.setPais(pais);
        currentSession().save(estado);
        assertNotNull(estado);
        String nombre = instance.elimina(estado.getId());
        assertEquals(nom, nombre);
        Estado prueba = instance.obtiene(estado.getId());

        if (prueba != null) {
            fail("Fallo la prueba Eliminar");
        }
    }
}
