/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import java.math.BigDecimal;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.rh.dao.CategoriaDao;
import mx.edu.um.mateo.rh.model.Categoria;
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
public class CategoriaControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CategoriaControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CategoriaDao categoriaDao;

    public CategoriaControllerTest() {
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
    public void debieraMostrarListaDeCategorias() throws Exception {
        log.debug("Debiera mostrar lista de categorias");

        for (int i = 0; i < 20; i++) {
            Categoria categoria = new Categoria();
            categoria.setNombre("test"+i);
            categoriaDao.saveCategoria(categoria);
            assertNotNull(categoria);
        }

        this.mockMvc.perform(get(Constantes.PATH_CATEGORIA)).
                andExpect(status().isOk()).
              andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CATEGORIA_LISTA + ".jsp")).
              andExpect(model().attributeExists(Constantes.CONTAINSKEY_CATEGORIAS)).
               andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
              andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarCategoria() throws Exception {
        log.debug("Debiera mostrar categoria");
        Categoria categoria = new Categoria();
        categoria.setNombre("test1");
        categoriaDao.saveCategoria(categoria);
        assertNotNull(categoria);

        this.mockMvc.perform(get(Constantes.PATH_CATEGORIA_VER + "/" + categoria.getId())).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CATEGORIA_VER + ".jsp")).
                andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_CATEGORIA));
    }

    @Test
    public void debieraCrearCategoria() throws Exception {
        log.debug("Debiera crear categoria");

        this.mockMvc.perform(post(Constantes.PATH_CATEGORIA_CREA)
                .param("nombre", "test")
                                        )
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE));
    }

    @Test
    public void debieraActualizarCategoria() throws Exception {
        log.debug("Debiera actualizar categoria");
        
        Categoria categoria = new Categoria();
        categoria.setNombre("test1");
        categoriaDao.saveCategoria(categoria);
        assertNotNull(categoria);

        this.mockMvc.perform(post(Constantes.PATH_CATEGORIA_ACTUALIZA)
                .param("id", categoria.getId().toString())
                .param("nombre", "test1")                )
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "categoria.saveCategoria.message"));

    }

    @Test
    public void debieraEliminarCategoria() throws Exception {
        log.debug("Debiera eliminar categoria");
        Categoria categoria = new Categoria();
        categoria.setNombre("test");
        categoriaDao.saveCategoria(categoria);

        this.mockMvc.perform(post(Constantes.PATH_CATEGORIA_ELIMINA).param("id", categoria.getId().toString())).
                andExpect(status().isOk()).
                andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE)).
                andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "categoria.removeCategoria.message"));
    }
}
