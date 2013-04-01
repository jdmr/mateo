/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.TipoColportor;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.test.BaseTest;
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
 * @author 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class TipoColportorDaoTest extends BaseTest{
    @Autowired
    private TipoColportorDao instance;
    @Autowired
    private SessionFactory sessionFactory;
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public TipoColportorDaoTest() {
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
     * Test of lista method, of class TipoColportorDao.
     */
    @Test
    public void deberiaMostrarListaDeTipoColportor() {
        log.debug("Debiera mostrar lista de TipoColportor");
        for (int i = 0; i < 20; i++) {
            TipoColportor tipoColportor = new TipoColportor("test"+i,"A");
            currentSession().save(tipoColportor);
            assertNotNull(tipoColportor.getId());
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        
        assertNotNull(result.get(Constantes.CONTAINSKEY_TIPO_COLPORTOR));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TipoColportor>) result.get(Constantes.CONTAINSKEY_TIPO_COLPORTOR)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }
    /**
     * Test of obtiene method, of class TipoColportorDao.
     */
    @Test
    public void debieraObtenerTipoColportor() {
        log.debug("Debiera obtener TipoColportor");
        String nombre = "test";
        TipoColportor tipoColportor = new TipoColportor(nombre,"A");
        currentSession().save(tipoColportor);
        assertNotNull(tipoColportor.getId());
        
        Long id = tipoColportor.getId();
        TipoColportor result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getTipoColportor());
    }
    /**
     * Test of crea method, of class TipoColportorDao.
     */
    @Test
    public void deberiaCrearTipoColportor() {
        log.debug("Deberia crear TipoColportor");
        TipoColportor tipoColportor = new TipoColportor("test", "A");        
        TipoColportor tipoColportor2 = instance.crea(tipoColportor);
        assertNotNull(tipoColportor2.getId());
        
        assertEquals(tipoColportor, tipoColportor2);
    }
    /**
     * Test of actualiza method, of class TipoColportorDao.
     */
    @Test
    public void deberiaActualizarTipoColportor() {
        log.debug("Deberia actualizar TipoColportor");
        TipoColportor tipoColportor = new TipoColportor("test","A");        
        currentSession().save(tipoColportor);
        
        String nombre = "test1";
        tipoColportor.setTipoColportor(nombre);
        TipoColportor tipoColportor2 = instance.actualiza(tipoColportor);
        assertNotNull(tipoColportor2);
        assertEquals(nombre, tipoColportor2.getTipoColportor());
    }
    /**
     * Test of elimina method, of class TipoColportorDao.
     */
    @Test
    public void deberiaEliminarTipoColportor() throws UltimoException {
        log.debug("Debiera eliminar TipoColportor");
        String nom = "test";
        TipoColportor tipoColportor = new TipoColportor(nom,"A");
        currentSession().save(tipoColportor);
        assertNotNull(tipoColportor.getId());
        
        String nombre = instance.elimina(tipoColportor.getId());
        assertEquals(nom, nombre);
        
        TipoColportor prueba = instance.obtiene(tipoColportor.getId());
        if(prueba != null){
            fail("Fallo la prueba Eliminar");
        }
    }
}
