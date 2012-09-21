/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import java.math.BigDecimal;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.rh.dao.EmpleadoDao;
import mx.edu.um.mateo.rh.model.Empleado;
import static org.junit.Assert.assertNotNull;
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
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
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
        log.debug("Debiera mostrar lista de empleados");

        for (int i = 0; i < 20; i++) {
            Empleado empleado = new Empleado("clave", "nombre", "apPaterno", "apMaterno",
                "m", "direccion", "ac",
                "curp", "rfc", "cuenta", "imms", 12,
                123, BigDecimal.ZERO,
                "modalidad", "ife", "rango", true, "padre",
                "madre", "estadoCivil", "conyuge",
                true, true, "iglesia", "responsabilidad",
                123, 123);
            empleadoDao.crea(empleado);
            assertNotNull(empleado);
        }

        this.mockMvc.perform(get(Constantes.PATH_EMPLEADO)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_EMPLEADO_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_EMPLEADOS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarEmpleado() throws Exception {
        log.debug("Debiera mostrar empleado");
        Empleado empleado = new Empleado("clave", "nombre", "apPaterno", "apMaterno",
                "m", "direccion", "ac",
                "curp", "rfc", "cuenta", "imms", 12,
                123, BigDecimal.ZERO,
                "modalidad", "ife", "rango", true, "padre",
                "madre", "estadoCivil", "conyuge",
                true, true, "iglesia", "responsabilidad",
                123, 123);
        empleado = empleadoDao.crea(empleado);
        assertNotNull(empleado);

        this.mockMvc.perform(get(Constantes.PATH_EMPLEADO_VER + "/" + empleado.getId())).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_EMPLEADO_VER + ".jsp")).
                andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_EMPLEADO));
    }

    @Test
    public void debieraCrearEmpleado() throws Exception {
        log.debug("Debiera crear empleado");

        this.mockMvc.perform(post(Constantes.PATH_EMPLEADO_CREA)
                .param("clave", "test")
                .param("nombre", "test")
                .param("apPaterno", "test")
                .param("apMaterno", "test")
                .param("genero", "m")
                .param("direccion", "test")
                .param("status", "te"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE));
    }

    @Test
    public void debieraActualizarEmpleado() throws Exception {
        log.debug("Debiera actualizar empleado");
        Empleado empleado = new Empleado("clave", "nombre", "apPaterno", "apMaterno",
                "m", "direccion", "ac",
                "curp", "rfc", "cuenta", "imms", 12,
                123, BigDecimal.ZERO,
                "modalidad", "ife", "rango", true, "padre",
                "madre", "estadoCivil", "conyuge",
                true, true, "iglesia", "responsabilidad",
                123, 123);
        empleado = empleadoDao.crea(empleado);
        assertNotNull(empleado);

        this.mockMvc.perform(post(Constantes.PATH_EMPLEADO_ACTUALIZA)
                .param("id", empleado.getId().toString())
                .param("version", empleado.getVersion().toString())
                .param("nombre", "test1")
                .param("clave", empleado.getClave())
                .param("apPaterno", "test1")
                .param("apMaterno", "test1")
                .param("genero", "t")
                .param("direccion", "test1")
                .param("status", "ts"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "empleado.actualizado.message"));

    }

    @Test
    public void debieraEliminarEmpleado() throws Exception {
        log.debug("Debiera eliminar empleado");
        Empleado empleado = new Empleado();
        empleado.setClave("test");
        empleado.setNombre("test");
        empleado.setApPaterno("test");
        empleado.setApMaterno("test");
        empleado.setGenero("m");
        empleado.setDireccion("test");
        empleado.setStatus("te");
        empleadoDao.crea(empleado);

        this.mockMvc.perform(post(Constantes.PATH_EMPLEADO_ELIMINA).param("id", empleado.getId().toString())).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE)).
                andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "empleado.eliminado.message"));
    }
}
