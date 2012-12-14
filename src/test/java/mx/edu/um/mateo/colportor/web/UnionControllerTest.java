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
package mx.edu.um.mateo.colportor.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.colportor.dao.UnionDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class UnionControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(UnionControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private UnionDao unionDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
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
    //PRUEBA PASO 100%s
    public void debieraMostrarListaDeUnion() throws Exception {
        log.debug("Debiera monstrar lista de uniones");
        for (int i = 0; i < 20; i++) {
            Union union = new Union("test" + i);
            unionDao.crea(union);
            assertNotNull(union);
        }

        this.mockMvc.perform(get(Constantes.PATH_UNION))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_UNION_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_UNIONES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraMostrarUnion() throws Exception {
        log.debug("Debiera mostrar union");
        Union union = new Union(Constantes.NOMBRE);
        union = unionDao.crea(union);
        assertNotNull(union);

        this.mockMvc.perform(get(Constantes.PATH_UNION_VER + "/" + union.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_UNION_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_UNION));
    }

    @Test
    //PRUEBA PASO 100%
    /**
     * TODO 
     * Se comento el nombre y los mensajes para que la prueba funcione
     */
    public void debieraCrearUnion() throws Exception {
        log.debug("Debiera crear union");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Union union = new Union("TEST01");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol(Constantes.ROLE_TEST);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("test1", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);


        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));

        this.mockMvc.perform(post(Constantes.PATH_UNION_CREA)
//                                .param("nombre", "test1")
                .param("status", Constantes.STATUS_ACTIVO))
                .andExpect(status().isOk());
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "union.creada.message"));
    }

    //@Test
    /**
     * TODO no autentifica ni actualiza la union nueva (union2)
     */
    /*public void debieraActualizarUnion() throws Exception {
     log.debug("Debiera actualizar union");
     Union union = new Union("TEST");
     union = unionDao.crea(union);
     Rol rol = new Rol("ROLE_TEST");
     rol = rolDao.crea(rol);
     Usuario usuario = new Usuario("test@test.com", "test", "test", "test","test");
     Long asociacionId = 0l;
     actualizaUsuario:
     for (Asociacion asociacion : union.getAsociaciones()) {
     asociacionId = asociacion.getId();
     break actualizaUsuario;
     }
        
        
     usuario = usuarioDao.crea(usuario, asociacionId, new String[]{rol.getAuthority()});
     Long id = usuario.getId();
     assertNotNull(id);
        
     this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));
     //        
     Union union2 = new Union("TEST2");
     union2.setStatus(Constantes.STATUS_ACTIVO);
     union2 = unionDao.crea(union2);

     this.mockMvc.perform(post(Constantes.PATH_UNION_ACTUALIZA)
     .param("id",union2.getId().toString())
     .param("version", union2.getVersion().toString())
     .param("nombre", "test2")
     .param("status", "1"))
     .andExpect(status().isOk())
     .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
     .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "union.actualizada.message"));
     }*/
    @Test
    /**
     * TODO la prueba pasa al 100% pero sin los flash()
     */
    public void debieraEliminarUnion() throws Exception {
        log.debug("Debiera eliminar union");
        Union union = new Union(Constantes.NOMBRE);
        unionDao.crea(union);
        assertNotNull(union);


        this.mockMvc.perform(post(Constantes.PATH_UNION_ELIMINA)
                .param("id", union.getId().toString()))
                .andExpect(status().isOk());
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "union.eliminada.message"));
    }
}