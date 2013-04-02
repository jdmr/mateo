/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author develop
 */
public class CobroCampoControllerTest {
    
    public CobroCampoControllerTest() {
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
     * Test of lista method, of class CobroCampoController.
     */
    @Test
    public void testLista() {
        System.out.println("lista");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        String filtro = "";
        Long pagina = null;
        String tipo = "";
        String correo = "";
        String order = "";
        String sort = "";
        Usuario usuario = null;
        Errors errors = null;
        Model modelo = null;
        CobroCampoController instance = new CobroCampoController();
        String expResult = "";
        String result = instance.lista(request, response, filtro, pagina, tipo, correo, order, sort, usuario, errors, modelo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ver method, of class CobroCampoController.
     */
    @Test
    public void testVer() {
        System.out.println("ver");
        Long id = null;
        Model modelo = null;
        CobroCampoController instance = new CobroCampoController();
        String expResult = "";
        String result = instance.ver(id, modelo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of nueva method, of class CobroCampoController.
     */
    @Test
    public void testNueva() {
        System.out.println("nueva");
        HttpServletRequest request = null;
        Model modelo = null;
        CobroCampoController instance = new CobroCampoController();
        String expResult = "";
        String result = instance.nueva(request, modelo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of graba method, of class CobroCampoController.
     */
    @Test
    public void testGraba() {
        System.out.println("graba");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        CobroCampo cobroCampo = null;
        BindingResult bindingResult = null;
        Errors errors = null;
        Model modelo = null;
        RedirectAttributes redirectAttributes = null;
        CobroCampoController instance = new CobroCampoController();
        String expResult = "";
        String result = instance.graba(request, response, cobroCampo, bindingResult, errors, modelo, redirectAttributes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edita method, of class CobroCampoController.
     */
    @Test
    public void testEdita() {
        System.out.println("edita");
        Long id = null;
        Model modelo = null;
        CobroCampoController instance = new CobroCampoController();
        String expResult = "";
        String result = instance.edita(id, modelo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of elimina method, of class CobroCampoController.
     */
    @Test
    public void testElimina() {
        System.out.println("elimina");
        HttpServletRequest request = null;
        Long id = null;
        Model modelo = null;
        CobroCampo cobroCampo = null;
        BindingResult bindingResult = null;
        RedirectAttributes redirectAttributes = null;
        CobroCampoController instance = new CobroCampoController();
        String expResult = "";
        String result = instance.elimina(request, id, modelo, cobroCampo, bindingResult, redirectAttributes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
