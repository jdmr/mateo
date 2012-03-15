/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.web;

import java.math.BigDecimal;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.dao.CuentaAuxiliarDao;
import mx.edu.um.mateo.contabilidad.model.CuentaAuxiliar;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
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
 * @author nujev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class CuentaAuxiliarControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CuentaAuxiliarControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CuentaAuxiliarDao cuentaAuxiliarDao;

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
    public void debieraMostrarListaDeCuentaAuxiliar() throws Exception {
        log.debug("Debiera monstrar lista de cuentas de auxiliar");
        
        for (int i = 0; i < 20; i++) {
            CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar("test" + i, "test", "test",false,false,false,false,BigDecimal.ZERO);
            cuentaAuxiliarDao.crea(cuentaAuxiliar);
            assertNotNull(cuentaAuxiliar);
        }

        this.mockMvc.perform(get(Constantes.PATH_CUENTA_AUXILIAR))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CUENTA_AUXILIAR_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_AUXILIARES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarCuentaAuxiliar() throws Exception {
        log.debug("Debiera mostrar cuenta de auxiliar");
        CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar("test", "test", "test",false,false,false,false,BigDecimal.ZERO);
        cuentaAuxiliar = cuentaAuxiliarDao.crea(cuentaAuxiliar);
        assertNotNull(cuentaAuxiliar);

        this.mockMvc.perform(get(Constantes.PATH_CUENTA_AUXILIAR_VER +"/"+ cuentaAuxiliar.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CUENTA_AUXILIAR_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_AUXILIAR));
    }

    @Test
    public void debieraCrearCuentaAuxiliar() throws Exception {
        log.debug("Debiera crear cuenta de auxiliar");

        this.mockMvc.perform(post(Constantes.PATH_CUENTA_AUXILIAR_CREA)
                .param("nombre", "test")
                .param("nombreFiscal", "test")
                .param("clave","test")
                .param("detalle", "false")
                .param("aviso", "false")
                .param("auxiliar", "false")
                .param("iva", "false")
                .param("pctIva", "0.0"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaAuxiliar.creada.message"));
    }

    @Test
    public void debieraActualizarCuentaAuxiliar() throws Exception {
        log.debug("Debiera actualizar cuenta de auxiliar");
        CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar("test", "test", "test",false,false,false,false,BigDecimal.ZERO);
        cuentaAuxiliar = cuentaAuxiliarDao.crea(cuentaAuxiliar);
        assertNotNull(cuentaAuxiliar);

        this.mockMvc.perform(post(Constantes.PATH_CUENTA_AUXILIAR_ACTUALIZA)
                .param("id",cuentaAuxiliar.getId().toString())
                .param("version", cuentaAuxiliar.getVersion().toString())
                .param("nombre", "test1")
                .param("nombreFiscal", cuentaAuxiliar.getNombreFiscal())
                .param("clave",cuentaAuxiliar.getClave())
                .param("detalle", cuentaAuxiliar.getDetalle().toString())
                .param("aviso", cuentaAuxiliar.getAviso().toString())
                .param("auxiliar", cuentaAuxiliar.getAuxiliar().toString())
                .param("iva", cuentaAuxiliar.getIva().toString())
                .param("pctIva", cuentaAuxiliar.getPorcentajeIva().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaAuxiliar.actualizada.message"));
    }

    @Test
    public void debieraEliminarCtaAuxiliar() throws Exception {
        log.debug("Debiera eliminar cuenta de auxiliar");
        CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar("test", "test", "test",false,false,false,false,BigDecimal.ZERO);
        cuentaAuxiliarDao.crea(cuentaAuxiliar);
        assertNotNull(cuentaAuxiliar);

        this.mockMvc.perform(post(Constantes.PATH_CUENTA_AUXILIAR_ELIMINA)
                .param("id", cuentaAuxiliar.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaAuxiliar.eliminada.message"));
    }
}
