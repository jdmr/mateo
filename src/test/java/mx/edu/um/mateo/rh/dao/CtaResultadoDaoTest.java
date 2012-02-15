/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import mx.edu.um.mateo.rh.model.CtaResultado;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author semdariobarbaamaya
 */
public class CtaResultadoDaoTest {
    
    public CtaResultadoDaoTest() {
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
     * Test of obtiene method, of class CtaResultadoDao.
     */
    @Test
    public void testObtiene() {
        System.out.println("obtiene");
        Long id = null;
        CtaResultadoDao instance = new CtaResultadoDao();
        CtaResultado expResult = null;
        CtaResultado result = instance.obtiene(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of crea method, of class CtaResultadoDao.
     */
    @Test
    public void testCrea() {
        System.out.println("crea");
        CtaResultado ctaresultado = null;
        CtaResultadoDao instance = new CtaResultadoDao();
        CtaResultado expResult = null;
        CtaResultado result = instance.crea(ctaresultado);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
