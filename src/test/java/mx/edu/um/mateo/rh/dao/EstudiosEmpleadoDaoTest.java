/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.rh.model.EstudiosEmpleado;
import mx.edu.um.mateo.rh.model.NivelEstudios;
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
public class EstudiosEmpleadoDaoTest {

    @Autowired
    private EstudiosEmpleadoDao estudiosEmpleadoDao;
    private static final Logger log = LoggerFactory.getLogger(EstudiosEmpleadoDaoTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public EstudiosEmpleadoDaoTest() {
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
     * Test of lista method, of class EstudiosEmpleadoDao.
     */
    @Test
    public void testLista() {

        
        for(int i=0; i<20; i++){
        EstudiosEmpleado estudiosEmpleado = new EstudiosEmpleado();
        estudiosEmpleado.setFechaCaptura(new Date());
        estudiosEmpleado.setFechaTitulacion(new Date());
        estudiosEmpleado.setNivelEstudios(NivelEstudios.MAESTRIA);
        estudiosEmpleado.setNombreEstudios("maestria");
        estudiosEmpleado.setStatus("A");
        estudiosEmpleado.setTitulado(Short.MIN_VALUE);
        estudiosEmpleado.setVersion(1);
        currentSession().save(estudiosEmpleado);
        assertNotNull(estudiosEmpleado.getId());
        log.debug("nombre"+estudiosEmpleado.getNombreEstudios());
        }
        Map<String, Object> params = null;
        Map result = estudiosEmpleadoDao.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<EstudiosEmpleado>) result.get(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
//        log.debug("cantidad"+((List<EstudiosEmpleado>)result.get(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO)).size());
        
    }

    /**
     * Test of obtiene method, of class EstudiosEmpleadoDao.
     */
    @Test
    public void testObtiene() {
        log.debug("obtiene");
        EstudiosEmpleado instance = new EstudiosEmpleado();
        currentSession().save(instance);
        assertNotNull(instance.getId());
        EstudiosEmpleado result = estudiosEmpleadoDao.obtiene(instance.getId());
        assertNotNull(result.getId());
        assertEquals(instance, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of crea method, of class EstudiosEmpleadoDao.
     */
    @Test
    public void testCrea() {
        log.debug("crea");
        EstudiosEmpleado estudiosEmpleado = new EstudiosEmpleado();
        estudiosEmpleado.setFechaCaptura(new Date());
        estudiosEmpleado.setFechaTitulacion(new Date());
        estudiosEmpleado.setNivelEstudios(NivelEstudios.MAESTRIA);
        estudiosEmpleado.setNombreEstudios("maestria");
        estudiosEmpleado.setStatus("A");
        estudiosEmpleado.setTitulado(Short.MIN_VALUE);
        estudiosEmpleado.setVersion(1);
        currentSession().save(estudiosEmpleado);
        assertNotNull(estudiosEmpleado.getId());
        
        log.debug("nombre"+estudiosEmpleado.toString());
                
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of actualiza method, of class EstudiosEmpleadoDao.
     */
    @Test
    public void testActualiza() {
        log.debug("actualiza");
        EstudiosEmpleado estudiosEmpleado = new EstudiosEmpleado();
        estudiosEmpleado.setFechaCaptura(new Date());
        estudiosEmpleado.setFechaTitulacion(new Date());
        estudiosEmpleado.setNivelEstudios(NivelEstudios.MAESTRIA);
        estudiosEmpleado.setNombreEstudios("maestria");
        estudiosEmpleado.setStatus("A");
        estudiosEmpleado.setTitulado(Short.MIN_VALUE);
        estudiosEmpleado.setVersion(1);
        currentSession().save(estudiosEmpleado);
        assertNotNull(estudiosEmpleado.getId());
        log.debug("nombre"+estudiosEmpleado.getNombreEstudios());
        String nombre ="doctorado";
        estudiosEmpleado.setNombreEstudios(nombre);
        estudiosEmpleadoDao.actualiza(estudiosEmpleado);
        assertNotNull(estudiosEmpleado.getId());
        assertEquals(nombre, estudiosEmpleado.getNombreEstudios());
        
    }

    /**
     * Test of elimina method, of class EstudiosEmpleadoDao.
     */
    @Test
    public void testElimina() throws Exception {
        log.debug("elimina");
         EstudiosEmpleado estudiosEmpleado = new EstudiosEmpleado();
        estudiosEmpleado.setFechaCaptura(new Date());
        estudiosEmpleado.setFechaTitulacion(new Date());
        estudiosEmpleado.setNivelEstudios(NivelEstudios.MAESTRIA);
        estudiosEmpleado.setNombreEstudios("maestria");
        estudiosEmpleado.setStatus("A");
        estudiosEmpleado.setTitulado(Short.MIN_VALUE);
        estudiosEmpleado.setVersion(1);
        currentSession().save(estudiosEmpleado);
        assertNotNull(estudiosEmpleado.getId());
        log.debug("nombre"+estudiosEmpleado.getNombreEstudios());
        String tipo = estudiosEmpleadoDao.elimina(estudiosEmpleado.getId());
        assertEquals(tipo, estudiosEmpleado.getNombreEstudios());
        
                
    }
}
