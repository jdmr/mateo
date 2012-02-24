/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.rh.dao.EmpleadoDao;
import mx.edu.um.mateo.rh.model.Empleado;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 *
 * @author AMDA
 */
    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
    })
    public class EmpleadoControllerTest extends BaseTest {
    
    private static final Logger log = LoggerFactory.getLogger(EmpleadoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private EmpleadoDao empleadoDao;

    public EmpleadoControllerTest() {
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
    public void debieraMostrarListaDeEmpleados() throws Exception {
        log.debug("Debiera monstrar lista de empleados");
        
        this.mockMvc.perform(get("/rh/empleado")).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/rh/empleado/lista.jsp")).
                andExpect(model().attributeExists("empleados")).
                andExpect(model().attributeExists("paginacion")).
                andExpect(model().attributeExists("paginas")).
                andExpect(model().attributeExists("pagina"));
    }
    
    @Test
    public void debieraMostrarEmpleado() throws Exception {
        log.debug("Debiera mostrar empleado");
        Empleado empleado = new Empleado("test", "test", "test", "test");
        empleado = empleadoDao.crea(empleado);

        this.mockMvc.perform(get("/rh/empleado/ver/" + empleado.getId())).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/rh/empleado/ver.jsp")).
                andExpect(model().attributeExists("empleado"));
    }
    
    @Test
    public void debieraCrearEmpleado() throws Exception {
        log.debug("Debiera crear empleado");
        
        this.mockMvc.perform(post("/rh/empleado/crea").param("nombre", "test").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "empleado.creado.message"));
    }
    
    @Test
    public void debieraActualizarEmpleado() throws Exception {
        log.debug("Debiera actualizar empleado");

        this.mockMvc.perform(post("/rh/empleado/actualiza").param("nombre", "test1").param("nombreFiscal", "test")).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "empleado.actualizado.message"));
    }
    
    @Test
    public void debieraEliminarEmpleado() throws Exception {
        log.debug("Debiera eliminar empleado");
        Empleado empleado = new Empleado("test", "test", "test", "test");
        empleadoDao.crea(empleado);

        this.mockMvc.perform(post("/rh/empleado/elimina").param("id", empleado.getId().toString())).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists("message")).
                andExpect(flash().attribute("message", "empleado.eliminado.message"));
    }
    
}
