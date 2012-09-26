/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.rh.model.Nacionalidad;
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
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class NacionalidadDaoTest {

    @Autowired
    private NacionalidadDao instance;
    private static final Logger log = LoggerFactory.getLogger(ColegioDaoTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public NacionalidadDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of lista method, of class NacionalidadDao.
     */
    @Test
    public void testLista() {
        Organizacion organizacion= new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        for (int i = 0; i < 20; i++) {
            Nacionalidad nacionalidad = new Nacionalidad();
            nacionalidad.setNombre("mexicana " + i);
            nacionalidad.setStatus("A");
            nacionalidad.setEmpresa(empresa);
            instance.graba(nacionalidad, null);

        }
        Map<String, Object> params = null;
        Map <String, Object>  result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_NACIONALIDADES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Nacionalidad>) result.get(Constantes.CONTAINSKEY_NACIONALIDADES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());


    }

    /**
     * Test of obtiene method, of class NacionalidadDao.
     */
    
    @Test
    public void testObtiene() {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        Nacionalidad nacionalidad = new Nacionalidad();
        nacionalidad.setNombre("mexicana");
        nacionalidad.setStatus("A");
        nacionalidad.setEmpresa(empresa);
        instance.graba(nacionalidad, null);
        assertNotNull(nacionalidad.getId());
        Nacionalidad prueba = instance.obtiene(nacionalidad.getId());
        assertEquals(prueba.getNombre(), nacionalidad.getNombre());
    }

    /**
     * Test of crea method, of class NacionalidadDao.
     */
    
    @Test
    public void testCrea() {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        Nacionalidad nacionalidad = new Nacionalidad();
        nacionalidad.setNombre("mexicana");
        nacionalidad.setStatus("A");
        nacionalidad.setEmpresa(empresa);
        instance.graba(nacionalidad,null);
        assertNotNull(nacionalidad.getId());
    }

    /**
     * Test of actualiza method, of class NacionalidadDao.
     */
    
    @Test
    public void testActualiza() {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        Nacionalidad nacionalidad = new Nacionalidad();
        nacionalidad.setNombre("mexicana");
        nacionalidad.setStatus("A");
        nacionalidad.setEmpresa(empresa);
        instance.graba(nacionalidad,null);
        assertNotNull(nacionalidad.getId());
        String nombre = "prueba";
        nacionalidad.setNombre(nombre);
        Nacionalidad prueba = instance.graba(nacionalidad,null);
        assertEquals(prueba.getNombre(), nacionalidad.getNombre());
    }

    /**
     * Test of elimina method, of class NacionalidadDao.
     */
    
    @Test
    public void testElimina() throws Exception {
       
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        Nacionalidad nacionalidad = new Nacionalidad();
        nacionalidad.setNombre("mexicana");
        nacionalidad.setStatus("A");
        nacionalidad.setEmpresa(empresa);
        instance.graba(nacionalidad,null);
        assertNotNull(nacionalidad.getId());
        String prueba = instance.elimina(nacionalidad.getId());
        assertEquals(prueba, nacionalidad.getNombre());
    }

    
}
