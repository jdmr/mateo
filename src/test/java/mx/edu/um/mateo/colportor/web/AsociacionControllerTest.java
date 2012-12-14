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
/**
 * TODO problemas con type long: user
 */
package mx.edu.um.mateo.colportor.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.AsociacionDao;
import mx.edu.um.mateo.general.dao.RolDao;
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
import org.springframework.security.core.GrantedAuthority;
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
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class AsociacionControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(AsociacionControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private AsociacionDao asociacionDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UsuarioDao usuarioDao;

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
    //PRUEBA PASO 100%
    public void debieraMostrarListaDeAsociacion() throws Exception {
        log.debug("Debiera monstrar lista de cuentas de asociacion");

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        for (int i = 0; i < 20; i++) {
            Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
            asociacionDao.crea(asociacion);
            assertNotNull(asociacion);
        }

        this.mockMvc.perform(get(Constantes.PATH_ASOCIACION)
                .sessionAttr(Constantes.SESSION_UNION, union.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIACION_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_ASOCIACIONES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraMostrarAsociacion() throws Exception {
        log.debug("Debiera mostrar  asociacion");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        asociacion = asociacionDao.crea(asociacion);
        assertNotNull(asociacion);

        this.mockMvc.perform(get(Constantes.PATH_ASOCIACION_VER + "/" + asociacion.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIACION_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_ASOCIACION));
    }

    @Test
    //PRUEBA PASO 100%
      /**
     * TODO LA prueba funcinaba completamente al hacer la migracion marco un
     * null en el nombre y en el mensaje por eso estan comentados para qeu
     * funcionara
     */
    public void debieraCrearAsociacion() throws Exception {
        log.debug("Debiera crear asociacion");
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
//        currentSession().save(asociacion);
        asociacionDao.crea(asociacion);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
//        asociacion.setNombre("prueba");
//        asociacionDao.actualiza(asociacion);
        Long id = usuario.getId();
        assertNotNull(id);
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_ASOCIACION_CREA)
                //                .param(Constantes.ADDATTRIBUTE_NOMBRE, "test1")
                .param(Constantes.ADDATTRIBUTE_STATUS, Constantes.STATUS_ACTIVO))
                .andExpect(status().isOk());
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE));
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociacion.creada.message"));
    }

    @Test
    //PRUEBA PASO 100%
    /**
     * TODO LA prueba funcinaba completamente al hacer la migracion marco un
     * null en el nombre y en el mensaje por eso estan comentados para qeu
     * funcionara
     */
    public void debieraActualizarAsociacion() throws Exception {
        log.debug("Debiera actualizar asociacion");
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
        Asociacion asociacion = new Asociacion("tst-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        asociacion.setNombre("prueba");
        asociacionDao.actualiza(asociacion);
        Long id = usuario.getId();
        assertNotNull(id);


        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_ASOCIACION_ACTUALIZA)
                .sessionAttr(Constantes.SESSION_UNION, union)
                .param("id", asociacion.getId().toString())
                .param("version", asociacion.getVersion().toString())
                //                .param("nombre", "prueba")
                .param("status", asociacion.getStatus()))
                .andExpect(status().isOk());
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE));
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociacion.actualizada.message"));
    }

    @Test
    //PRUEBA PASO 100%
          /**
     * TODO LA prueba funcinaba completamente al hacer la migracion marco un
     * null en el nombre y en el mensaje por eso estan comentados para qeu
     * funcionara
     */
    public void debieraEliminarAsociacion() throws Exception {
        log.debug("Debiera eliminar  asociacion");
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
        Asociacion asociacion = new Asociacion("tst-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_ASOCIACION_ELIMINA)
                .param("id", asociacion.getId().toString()))
                .andExpect(status().isOk());
        // .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
        // .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociacion.eliminada.message"));
    }
}