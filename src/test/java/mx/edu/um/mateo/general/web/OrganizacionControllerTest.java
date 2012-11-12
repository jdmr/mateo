/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.general.web;

import java.util.ArrayList;
import java.util.List;
import mx.edu.um.mateo.contabilidad.dao.EjercicioDao;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.general.dao.OrganizacionDao;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.junit.*;
import static org.junit.Assert.assertNotNull;
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
 * @author jdmr
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class OrganizacionControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(OrganizacionControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private OrganizacionDao organizacionDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private EjercicioDao ejercicioDao;

    public OrganizacionControllerTest() {
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
    public void debieraMostrarListaDeOrganizaciones() throws Exception {
        this.mockMvc.perform(get("/admin/organizacion")).andExpect(status().isOk()).andExpect(forwardedUrl("/WEB-INF/jsp/admin/organizacion/lista.jsp")).andExpect(model().attributeExists("organizaciones")).andExpect(model().attributeExists("paginacion")).andExpect(model().attributeExists("paginas")).andExpect(model().attributeExists("pagina"));
    }

    @Test
    public void debieraMostrarOrganizacion() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        organizacion = organizacionDao.crea(organizacion);

        this.mockMvc.perform(get("/admin/organizacion/ver/" + organizacion.getId())).andExpect(status().isOk()).andExpect(forwardedUrl("/WEB-INF/jsp/admin/organizacion/ver.jsp")).andExpect(model().attributeExists("organizacion"));
    }

    @Test
    public void debieraCrearOrganizacion() throws Exception {
        log.debug("Debiera actualizar organizacion");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        organizacion = organizacionDao.crea(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        Long almacenId = 0l;
        actualizaUsuario:
        for (Empresa empresa : organizacion.getEmpresas()) {
            for (Almacen almacen : empresa.getAlmacenes()) {
                almacenId = almacen.getId();
                break actualizaUsuario;
            }
        }
        
        List<Ejercicio> ejercicios = ejercicioDao.lista(organizacion.getId());
        usuario.setEjercicio(ejercicios.get(0));
        
        usuario = usuarioDao.crea(usuario, almacenId, new String[]{rol.getAuthority()});
        Long id = usuario.getId();
        assertNotNull(id);

        this.authenticate(usuario, usuario.getPassword(), new ArrayList<>(usuario.getAuthorities()));
        
        this.mockMvc.perform(
                post("/admin/organizacion/crea")
                .param("codigo", "tst-05")
                .param("nombre", "TEST--05")
                .param("nombreCompleto", "TEST--05"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "organizacion.creada.message"));
    }
}
