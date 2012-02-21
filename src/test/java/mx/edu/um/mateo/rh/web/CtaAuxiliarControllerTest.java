/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

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
public class CtaAuxiliarControllerTest {
    
    public CtaAuxiliarControllerTest() {
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
     * Test of index method, of class CtaAuxiliarController.
     */
    @Test
    public void testIndex() {
        System.out.println("index");
        CtaAuxiliarController instance = new CtaAuxiliarController();
        String expResult = "";
        String result = instance.index();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
