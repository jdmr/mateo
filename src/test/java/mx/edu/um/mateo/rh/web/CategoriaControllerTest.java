/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.rh.dao.CategoriaDao;
import mx.edu.um.mateo.rh.model.Categoria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;

/**
 *
 * @author zorch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class CategoriaControllerTest extends BaseTest{
    
    @Autowired
    private CategoriaDao categoriaDao;
    
    private static final Logger log = LoggerFactory.getLogger(CategoriaControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
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
    public void testListaCategorias() throws Exception {
        log.debug("Test Lista de Categorias");
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Organizacion organizacion = new Organizacion("codigo", "nombre", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("test", "alamcen", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        for (int i = 0; i < 20; i++) {
            Categoria categoria = new Categoria();
            categoria.setNombre("Test");
            categoria.setStatus("A");
            categoriaDao.graba(categoria, usuario);
            assertNotNull(categoria.getId());
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
    public void testGrabaCategoria() throws Exception {
        log.debug("Test Crear Categoria");
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Organizacion organizacion = new Organizacion("codigo", "nombre", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("test", "alamcen", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        Categoria categoria = new Categoria();
        categoria.setNombre("Test");
        categoria.setStatus("A");
        categoriaDao.graba(categoria, usuario);
        assertNotNull(categoria.getId());
        
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_CATEGORIA_GRABA)
                .param("nombre", "Test")
                .param("status", "A")
                )
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "categoria.graba.message"));
        
     }
    @Test
    public void testVerCategoria() throws Exception {
        log.debug("Test mostrar categoria");
       Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Organizacion organizacion = new Organizacion("codigo", "nombre", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("test", "alamcen", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        Categoria categoria = new Categoria();
        categoria.setNombre("Test");
        categoria.setStatus("A");
        categoriaDao.graba(categoria, usuario);
        assertNotNull(categoria.getId());
        
        this.mockMvc.perform(get("/rh/categoria/ver/" + categoria.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.PATH_CATEGORIA_VER+".jsp"))
                .andExpect(model().attributeExists("categoria"));
        
    }
      
      
    
    @Test
    public void testActualizarCategoria() throws Exception {
        log.debug("Test actualizar Categoria");
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Organizacion organizacion = new Organizacion("codigo", "nombre", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("test", "alamcen", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        Categoria categoria = new Categoria();
        categoria.setNombre("prueba");
        categoria.setStatus("A");
        categoriaDao.graba(categoria, usuario);
        assertNotNull(categoria.getId());
        
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        
        
        this.mockMvc.perform(post("/rh/categoria/graba")
                .param("id", categoria.getId().toString())
                .param("version", categoria.getVersion().toString())
                .param("nombre", "TEST")
                .param("status", categoria.getStatus())
                )
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "categoria.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_CATEGORIA_LISTA+"/"));
        
        currentSession().refresh(categoria);
        assertEquals("TEST", categoria.getNombre());
    }
    
     @Test
      public void testEditaCategoria() throws Exception {
         log.debug("Test eliminar Categoria");
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Organizacion organizacion = new Organizacion("codigo", "nombre", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("test", "alamcen", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        Categoria categoria = new Categoria();
        categoria.setNombre("prueba");
        categoria.setStatus("A");
        categoriaDao.graba(categoria, usuario);
        assertNotNull(categoria.getId());
        
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
         
         this.mockMvc.perform(get("/rh/categoria/edita/"+categoria.getId())
                )
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.PATH_CATEGORIA_EDITA+".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_CATEGORIA));
     }
     
}
