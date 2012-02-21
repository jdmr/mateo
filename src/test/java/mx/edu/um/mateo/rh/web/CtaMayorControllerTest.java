/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.rh.dao.CtaMayorDao;
import mx.edu.um.mateo.rh.model.CtaMayor;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author nujev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
public class CtaMayorControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CtaMayorControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CtaMayorDao ctaMayorDao;

    public CtaMayorControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void debieraMostrarListaDeCtaMayor() throws Exception {
        CtaMayor ctaMayor = new CtaMayor("tst-01", "test-01");
        ctaMayor = ctaMayorDao.crea(ctaMayor);

        this.mockMvc.perform(get("/rh/ctaMayor")).andExpect(status().isOk()).andExpect(forwardedUrl("/WEB-INF/jsp/rh/ctaMayor/lista.jsp")).andExpect(model().attributeExists("ctaMayor")).andExpect(model().attributeExists("paginacion")).andExpect(model().attributeExists("paginas")).andExpect(model().attributeExists("pagina"));
    }

    @Test
    public void debieraMostrarCtaMayor() throws Exception {
        CtaMayor ctaMayor = new CtaMayor("tst-01", "test-01");
        ctaMayor = ctaMayorDao.crea(ctaMayor);

        this.mockMvc.perform(get("/rh/ctaMayor/ver/" + ctaMayor.getId())).andExpect(status().isOk()).andExpect(forwardedUrl("/WEB-INF/jsp/rh/ctaMayor/ver.jsp")).andExpect(model().attributeExists("ctaMayor"));
    }
//    /**
//     * Test of lista method, of class CtaMayorController.
//     */
//    @Test
//    public void testLista() {
//        System.out.println("lista");
//        HttpServletRequest request = null;
//        HttpServletResponse response = null;
//        String filtro = "";
//        Long pagina = null;
//        String tipo = "";
//        String correo = "";
//        String order = "";
//        String sort = "";
//        Model modelo = null;
//        CtaMayorController instance = new CtaMayorController();
//        String expResult = "";
//        String result = instance.lista(request, response, filtro, pagina, tipo, correo, order, sort, modelo);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of ver method, of class CtaMayorController.
//     */
//    @Test
//    public void testVer() {
//        System.out.println("ver");
//        Long id = null;
//        Model modelo = null;
//        CtaMayorController instance = new CtaMayorController();
//        String expResult = "";
//        String result = instance.ver(id, modelo);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of nueva method, of class CtaMayorController.
//     */
//    @Test
//    public void testNueva() {
//        System.out.println("nueva");
//        Model modelo = null;
//        CtaMayorController instance = new CtaMayorController();
//        String expResult = "";
//        String result = instance.nueva(modelo);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of crea method, of class CtaMayorController.
//     */
//    @Test
//    public void testCrea() {
//        System.out.println("crea");
//        HttpServletRequest request = null;
//        HttpServletResponse response = null;
//        CtaMayor CtaMayor = null;
//        BindingResult bindingResult = null;
//        Errors errors = null;
//        Model modelo = null;
//        RedirectAttributes redirectAttributes = null;
//        CtaMayorController instance = new CtaMayorController();
//        String expResult = "";
//        String result = instance.crea(request, response, CtaMayor, bindingResult, errors, modelo, redirectAttributes);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of edita method, of class CtaMayorController.
//     */
//    @Test
//    public void testEdita() {
//        System.out.println("edita");
//        Long id = null;
//        Model modelo = null;
//        CtaMayorController instance = new CtaMayorController();
//        String expResult = "";
//        String result = instance.edita(id, modelo);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of actualiza method, of class CtaMayorController.
//     */
//    @Test
//    public void testActualiza() {
//        System.out.println("actualiza");
//        HttpServletRequest request = null;
//        CtaMayor CtaMayor = null;
//        BindingResult bindingResult = null;
//        Errors errors = null;
//        Model modelo = null;
//        RedirectAttributes redirectAttributes = null;
//        CtaMayorController instance = new CtaMayorController();
//        String expResult = "";
//        String result = instance.actualiza(request, CtaMayor, bindingResult, errors, modelo, redirectAttributes);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of elimina method, of class CtaMayorController.
//     */
//    @Test
//    public void testElimina() {
//        System.out.println("elimina");
//        HttpServletRequest request = null;
//        Long id = null;
//        Model modelo = null;
//        CtaMayor CtaMayor = null;
//        BindingResult bindingResult = null;
//        RedirectAttributes redirectAttributes = null;
//        CtaMayorController instance = new CtaMayorController();
//        String expResult = "";
//        String result = instance.elimina(request, id, modelo, CtaMayor, bindingResult, redirectAttributes);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
