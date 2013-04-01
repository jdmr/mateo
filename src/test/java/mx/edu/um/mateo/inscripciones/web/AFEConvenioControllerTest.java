/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
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
import mx.edu.um.mateo.inscripciones.dao.AFEConvenioDao;
import mx.edu.um.mateo.inscripciones.dao.AlumnoDao;
import mx.edu.um.mateo.inscripciones.model.AFEConvenio;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
public class AFEConvenioControllerTest extends BaseTest{
    
    @Autowired
    private AFEConvenioDao instance;
    private static final Logger log = LoggerFactory.getLogger(TiposBecasControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private AlumnoDao alDao;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
      public AFEConvenioControllerTest() {
    }
      
       @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testObtenerListaConvenios() throws Exception {
    log.debug("test obtener una lista de Convenios");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, empresa);
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        AFEConvenio afeConvenio = null;
        for (int i=0 ; i<20; i++){
            afeConvenio= new AFEConvenio("A",alumno,empresa,tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
            currentSession().save(afeConvenio);
            assertNotNull(afeConvenio.getId());
        }
        
         this.mockMvc.perform(get(Constantes.PATH_AFECONVENIO)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFECONVENIO_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_AFECONVENIO)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    
    @Test
    public void testCrearConvenio() throws Exception {
        log.debug("Test Crea un Convenio");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, empresa);
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        assertNotNull(alumno);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,empresa,tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        currentSession().save(afeConvenio);
        assertNotNull(afeConvenio.getId());
        assertEquals(alumno.getMatricula(), "1080506");
        instance.graba(afeConvenio, usuario);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_AFECONVENIO_GRABA)
                .param("matricula", "1080506")
                .param("tipoBeca", tipoBeca.getId().toString())
                .param("importe", "300")
                .param("numHoras", "320")
                .param("diezma", "0")
                .param("afeConvenio.id", afeConvenio.getId().toString())
                
                )
                
                .andExpect(status().isOk());
                
               // .andExpect(flash().attributeExists("message"))
              //  .andExpect(flash().attribute("message", "afeConvenio.creado.label"));
        
    }
    
    @Test
    public void testActualizarConvenio() throws Exception {
        log.debug("Test Crea un Convenio");
         Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, empresa);
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        assertNotNull(alumno);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,empresa,tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        currentSession().save(afeConvenio);
        assertNotNull(afeConvenio.getId());
        assertEquals(alumno.getMatricula(), "1080506");
        instance.graba(afeConvenio, usuario);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_AFECONVENIO_GRABA)
                .param("matricula", "1080506")
                .param("tipoBeca", tipoBeca.getId().toString())
                .param("importe", "300")
                .param("numHoras", "320")
                .param("diezma", "0")
                .param("afeConvenio.id", afeConvenio.getId().toString())
                
                )
                
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFECONVENIO_NUEVO + ".jsp"));
                // .andExpect(flash().attributeExists("message"))
              //  .andExpect(flash().attribute("message", "afeConvenio.creado.label"));
              
        
    
    }
    
    @Test
    public void testMuestraConvenio() throws Exception {
        log.debug("Test mostrar un Convenio");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, empresa);
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,empresa,tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        currentSession().save(afeConvenio);
        assertNotNull(afeConvenio.getId());
        assertEquals(alumno.getMatricula(), "1080506");
        this.mockMvc.perform(get(Constantes.PATH_AFECONVENIO_VER + "/" + afeConvenio.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFECONVENIO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_AFECONVENIO));
    }
    
    @Test
    public void testEliminaConvenio() throws Exception {
        log.debug("Test eliminar un Convenio");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, empresa);
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,empresa,tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        currentSession().save(afeConvenio);
        assertNotNull(afeConvenio.getId());
        assertEquals(alumno.getMatricula(), "1080506");
        this.mockMvc.perform(post(Constantes.PATH_AFECONVENIO_ELIMINA)
                .param("id", afeConvenio.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "afeConvenio.elimina.message"));
    
    }
}
