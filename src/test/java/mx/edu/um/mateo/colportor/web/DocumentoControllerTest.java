/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * TODO problemas con nullpointerexception
 */
package mx.edu.um.mateo.colportor.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.colportor.dao.AsociadoDao;
import mx.edu.um.mateo.colportor.dao.DocumentoDao;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.dao.TemporadaDao;
import mx.edu.um.mateo.colportor.dao.UnionDao;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.ColegioColportor;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Documento;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.contabilidad.dao.EjercicioDao;
import mx.edu.um.mateo.general.dao.*;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
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
import static org.springframework.test.web.server.result.MockMvcResultHandlers.*;
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
public class DocumentoControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DocumentoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private DocumentoDao documentoDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UnionDao unionDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private TemporadaColportorDao temporadaColportorDao;
    @Autowired
    private AsociadoDao asociadoDao;
    @Autowired
    private TemporadaDao temporadaDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private EjercicioDao ejercicioDao;
    @Autowired
    private OrganizacionDao organizacionDao;

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
    public void debieraMostrarListaDeDocumentoDeColportor() throws Exception {
        log.debug("Debiera monstrar lista de documentos");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_COL");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Colportor usuario = new Colportor("test1@test.com", "test", "test", "test", "test", "test", "1",
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        assertNotNull(usuario.getId());
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));

        Asociado asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());

        
        TemporadaColportor temporadaColportor = null;
        Temporada temporada=null;
        for(int i =0; i< 9; i++){
                temporada= new Temporada("test"+i);
                temporada.setAsociacion(asociacion);
                currentSession().save(temporada);
                assertNotNull(temporada.getId());
        }
        
        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        assertNotNull(colegio.getId());
        
        Colportor colportorTmp = (Colportor) usuario;
        colportorTmp.setAsociacion(asociacion);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setEmpresa(empresa);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        Documento documento = null;
        for (int i = 0; i < 9; i++) {
            documento = new Documento("test", "F"+i,
                    new Date(), new BigDecimal("1500"), "test", temporadaColportor);            
            currentSession().save(documento);
            assertNotNull(documento.getId());
        }
        
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA)
                .sessionAttr("colportorTmp", colportorTmp)) 
                .andExpect(status().isOk());
    }

     
    @Test
    public void debieraMostrarListaDeDocumentoDeColportorQueNoEstaEnNingunaTemporada() throws Exception {
        log.debug("Debiera monstrar lista de documentos de un colportor que no esta en ninguna temporada");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_COL");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Colportor usuario = new Colportor("test1@test.com", "test", "test", "test", "test", "test", "1",
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        assertNotNull(usuario.getId());
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));

        Asociado asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());

        
        TemporadaColportor temporadaColportor = null;
        Temporada temporada=null;
        for(int i =0; i< 9; i++){
                temporada= new Temporada("test"+i);
                temporada.setAsociacion(asociacion);
                currentSession().save(temporada);
                assertNotNull(temporada.getId());
        }
        
        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        assertNotNull(colegio.getId());
        
        Colportor colportorTmp = (Colportor) usuario;
        colportorTmp.setAsociacion(asociacion);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setEmpresa(empresa);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_INACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        Documento documento = null;
        for (int i = 0; i < 9; i++) {
            documento = new Documento("test", "F"+i,
                    new Date(), new BigDecimal("1500"), "test", temporadaColportor);            
            currentSession().save(documento);
            assertNotNull(documento.getId());
        }
        
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA)
                .param("temporadaId", temporada.getId().toString())
                .sessionAttr("colportorTmp", colportorTmp))
                .andExpect(model().attribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
                .andExpect(status().isOk());
    }

     
    @Test
    public void debieraMostrarListaDeDocumentoDeColportorQueSeCambiaTemporada() throws Exception {
        log.debug("Debiera monstrar lista de documentos de un colportor qeu no esta en ninguna temporada");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_COL");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Colportor usuario = new Colportor("test1@test.com", "test", "test", "test", "test", "test", "1",
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        assertNotNull(usuario.getId());
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));

        Asociado asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
               
        Temporada temporada = new Temporada("test1");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Temporada temporada2 = new Temporada("test2");
        temporada2.setAsociacion(asociacion);
        currentSession().save(temporada2);
        assertNotNull(temporada2.getId());
                
        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        assertNotNull(colegio.getId());
        
        Colportor colportorTmp = (Colportor) usuario;
        colportorTmp.setAsociacion(asociacion);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setEmpresa(empresa);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        TemporadaColportor temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        Documento documento = null;
        for (int i = 0; i < 9; i++) {
            documento = new Documento("test", "F"+i,
                    new Date(), new BigDecimal("1500"), "test", temporadaColportor);            
            currentSession().save(documento);
            assertNotNull(documento.getId());
        }
        
        TemporadaColportor temporadaColportor2 = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada2, union, colegio);
        temporadaColportor2.setStatus(Constantes.STATUS_INACTIVO);
        temporadaColportor2.setObjetivo("11250");
        temporadaColportor2.setObservaciones("Observaciones");
        temporadaColportor2.setFecha(new Date());
        currentSession().save(temporadaColportor2);
        assertNotNull(temporadaColportor2.getId());

        for (int i = 0; i < 9; i++) {
            documento = new Documento("test", "F"+i,
                    new Date(), new BigDecimal("1500"), "test", temporadaColportor2);            
            currentSession().save(documento);
            assertNotNull(documento.getId());
        }
        
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA)
                .sessionAttr("colportorTmp", colportorTmp))
                .andExpect(model().attribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
                .andExpect(status().isOk());
        log.debug("terminaPrimerLlamada");

        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA)
                .param("temporadaId", temporada2.getId().toString())
                .sessionAttr("colportorTmp", colportorTmp))
                .andExpect(model().attribute("temporadaColportorPrueba", temporadaColportor2.getId().toString()))
                .andExpect(status().isOk());
        log.debug("terminaSegundaLlamada");

    }

     
    @Test
    public void debieraMostrarListaDeDocumentoDeColportorQueSeCambiaAUnaTemporadaVacia() throws Exception {
        log.debug("Debiera monstrar lista de documentos de un colportor qeu no esta en ninguna temporada");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_COL");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Colportor usuario = new Colportor("test1@test.com", "test", "test", "test", "test", "test", "1",
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        assertNotNull(usuario.getId());
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));

        Asociado asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
               
        Temporada temporada = new Temporada("test1");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Temporada temporada2 = new Temporada("test2");
        temporada2.setAsociacion(asociacion);
        currentSession().save(temporada2);
        assertNotNull(temporada2.getId());
                
        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        assertNotNull(colegio.getId());
        
        Colportor colportorTmp = (Colportor) usuario;
        colportorTmp.setAsociacion(asociacion);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setEmpresa(empresa);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        TemporadaColportor temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        Documento documento = null;
        for (int i = 0; i < 9; i++) {
            documento = new Documento("test", "F"+i,
                    new Date(), new BigDecimal("1500"), "test", temporadaColportor);            
            currentSession().save(documento);
            assertNotNull(documento.getId());
        }
        
        TemporadaColportor temporadaColportor2 = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada2, union, colegio);
        temporadaColportor2.setStatus(Constantes.STATUS_INACTIVO);
        temporadaColportor2.setObjetivo("11250");
        temporadaColportor2.setObservaciones("Observaciones");
        temporadaColportor2.setFecha(new Date());
        currentSession().save(temporadaColportor2);
        assertNotNull(temporadaColportor2.getId());

        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA)
                .sessionAttr("colportorTmp", colportorTmp))
                .andExpect(model().attribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
                .andExpect(status().isOk());
        log.debug("terminaPrimerLlamada");

        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA)
                .param("temporadaId", temporada2.getId().toString())
                .sessionAttr("colportorTmp", colportorTmp)).andExpect(model()
                .attribute("temporadaColportorPrueba", temporadaColportor2.getId().toString()))
                .andExpect(status().isOk());
        log.debug("terminaSegundaLlamada");
    }

     
    @Test
    public void debieraMostrarListaVaciaDeDocumentosDeColportorAlAsociado() throws Exception {
        log.debug("Debiera monstrar lista de documentos de un colportor al asociado");
        log.debug("Debiera monstrar lista de documentos de un colportor al asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        Colportor colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor);
        log.debug("TemporadaColportor" + temporadaColportor);

        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"));
    }

     
    @Test
    public void debieraMostrarListaDeDocumentosDeColportorAlAsociado() throws Exception {
        log.debug("Debiera monstrar lista de documentos de un colportor al asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        Colportor colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor);
        log.debug("TemporadaColportor" + temporadaColportor);


        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }
        
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA)
                .sessionAttr("colportorTmp", colportorTmp))
                .andExpect(status().isOk()).andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_DOCUMENTOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

     
    @Test
    public void deberiaMostrarDocumentosDelaClaveDelColportorAlAsociado() throws Exception {
        log.debug("Mostrando documentos de un colportor seleccionado por su clave");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        Colportor colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        Documento documento = null;
        for (int i = 0; i < 8; i++) {
            documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }
        
        colportorTmp = new Colportor("test2@test.com", "test2", "test2", "test2",
                "test", "5678", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
       
        for (int i = 0; i < 5; i++) {
            documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }
        
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO)
                .param("clave", "5678"))
                .andExpect(request().sessionAttribute("temporadaColportorTmp", temporadaColportor))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_DOCUMENTOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

     
    @Test
    public void deberiaMostrarDocumentosDelaClaveDeVariosColportoresAlAsociado() throws Exception {
        log.debug("Mostrando documentos de un colportor seleccionado por su clave");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        Colportor colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        Documento documento = null;
        for (int i = 0; i < 8; i++) {
            documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }
        
        colportorTmp = new Colportor("test2@test.com", "test2", "test2", "test2",
                "test", "5678", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
       
        for (int i = 0; i < 5; i++) {
            documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }
        
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO)
                .param("clave", "5678"))
                .andExpect(request().sessionAttribute("temporadaColportorTmp", temporadaColportor))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_DOCUMENTOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
        log.debug("termino Primera llamada");


        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO)
                .param("clave", "1234"))
                .andExpect(request().sessionAttribute("temporadaColportorTmp", temporadaColportor))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_DOCUMENTOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
        log.debug("termino Segunda llamada");

    }

     
    @Test
    public void debieraMostrarDocumento() throws Exception {
        log.debug("Debiera mostrar documento");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        Usuario colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor((Colportor)colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        Documento documento = null;
        for (int i = 0; i < 1; i++) {
            documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }

        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_VER + "/" + documento.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_DOCUMENTO));
    }

     
    @Test
    public void debieraCrearDocumento() throws Exception {
        log.debug("Debiera crear documento");

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());       

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        Usuario colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        this.authenticate(colportorTmp, colportorTmp.getPassword(), new ArrayList(asociado.getAuthorities()));
        
        temporadaColportor = new TemporadaColportor((Colportor)colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_CREA).
                param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO).
                param("folio", Constantes.FOLIO).
                param("importe", "0.0").
                param("fecha", "05/05/2010").
                param("observaciones", Constantes.OBSERVACIONES)
                .sessionAttr("colportorTmp", colportorTmp))
                .andExpect(request().sessionAttribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
                .andExpect(status().isOk()).andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"));
                //.andExpect(redirectedUrl(Constantes.PATH_DOCUMENTO_VER));
    }

     
    @Test
    public void debieraActualizarDocumento() throws Exception {
        log.debug("Debiera actualizar documento");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());       

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        Usuario colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        this.authenticate(colportorTmp, colportorTmp.getPassword(), new ArrayList(asociado.getAuthorities()));
        
        temporadaColportor = new TemporadaColportor((Colportor)colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_CREA).
                param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO).
                param("folio", Constantes.FOLIO).
                param("importe", "0.0").
                param("fecha", "05/05/2010").
                param("observaciones", Constantes.OBSERVACIONES)
                .sessionAttr("colportorTmp", colportorTmp))
                .andExpect(request().sessionAttribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
                .andExpect(status().isOk()).andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"))
                .andDo(print());
                //.andExpect(redirectedUrl(Constantes.PATH_DOCUMENTO_VER + "/1"));
        
        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_ACTUALIZA).
                param("id", "1").
                param("version", "0").
                param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO).
                param("folio", Constantes.FOLIO).
                param("importe", "0.0").
                param("fecha", "05/05/2010").
                param("observaciones", Constantes.OBSERVACIONES)
                .sessionAttr("colportorTmp", colportorTmp))
                //.andExpect(request().sessionAttribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
                .andExpect(status().isOk()).andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.actualizado.message"))
                .andDo(print());
                //.andExpect(redirectedUrl(Constantes.PATH_DOCUMENTO_VER + "/1"));

    }

     
    @Test
    public void debieraEliminarDocumento() throws Exception {
        log.debug("Debiera eliminar documento");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        Usuario colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor((Colportor)colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        Documento documento = null;
        for (int i = 0; i < 1; i++) {
            documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }

        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_ELIMINA)
                .param("id", documento.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.eliminado.message"));
    }
    
     
    @Test
    public void deberiaProbarTablaDeTotales() throws Exception{
        log.debug("Deberia probar tabla de resultados de acuerdo a los documentos creados");
        
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());

        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());

        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());

        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());

        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);

        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());

        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());

        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);

        Usuario colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());

        temporadaColportor = new TemporadaColportor((Colportor) colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        Documento documento = null;
        for (int i = 0; i < 20; i++) {
            documento = new Documento(Constantes.BOLETIN, Constantes.FOLIO,
                    new Date(), new BigDecimal("500"), Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
        }

        for (int i = 0; i < 20; i++) {
            documento = new Documento(Constantes.DIEZMO, Constantes.FOLIO,
                    new Date(), new BigDecimal("50"), Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
        }

        for (int i = 0; i < 20; i++) {
            documento = new Documento(Constantes.DEPOSITO_CAJA, Constantes.FOLIO,
                    new Date(), new BigDecimal("100"), Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
        }

        for (int i = 0; i < 20; i++) {
            documento = new Documento(Constantes.DEPOSITO_BANCO, Constantes.FOLIO,
                    new Date(), new BigDecimal("100"), Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
        }

        for (int i = 0; i < 20; i++) {
            documento = new Documento(Constantes.NOTAS_DE_COMPRA, Constantes.FOLIO,
                    new Date(), new BigDecimal("100"), Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
        }
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA)
                .sessionAttr("colportorTmp", colportorTmp)) //                .andExpect(request().sessionAttribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
                .andExpect(model().attribute(Constantes.FIDELIDAD, new BigDecimal("100.000000").setScale(2, BigDecimal.ROUND_HALF_EVEN)))
                .andExpect(model().attribute(Constantes.ALCANZADO, new BigDecimal("88.888888").setScale(2, BigDecimal.ROUND_HALF_EVEN)))
                .andExpect(view().name(Constantes.PATH_DOCUMENTO_LISTA))
                .andExpect(status().isOk());
    }
}
