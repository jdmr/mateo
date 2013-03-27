package mx.edu.um.mateo.inscripciones.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.PeriodoDao;
import mx.edu.um.mateo.inscripciones.model.Periodo;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class PeriodoControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(PeriodoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private PeriodoDao instance;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }

    @Test
    public void debieraMostrarListaDePeriodo() throws Exception {
        log.debug("Debiera mostrar lista de periodo");
        this.mockMvc.perform(
                get(Constantes.PATH_PERIODOS))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PERIODOS_LISTA + ".jsp"))
                .andExpect(model().attributeExists("periodo"))
                .andExpect(model().attributeExists("paginacion"))
                .andExpect(model().attributeExists("paginas"))
                .andExpect(model().attributeExists("pagina"));
    }

    @Test
    public void debieraMostrarPeriodo() throws Exception {
        log.debug("Debiera mostrar periodo");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Periodo periodo = new Periodo("test", "A", "clave", new Date(), new Date());
        periodo.setOrganizacion(organizacion);
        currentSession().save(periodo);
        Long id = periodo.getId();

        this.mockMvc.perform(get("/inscripciones/periodos/ver/" + id))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/inscripciones/periodos/ver.jsp"))
                .andExpect(model().attributeExists("periodo"));

    }

    @Test
    public void debieraCrearPeriodo() throws Exception {
        log.debug("Debiera crear periodo");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_PERIODOS_GRABA)
                .param("descripcion", "TEST-1")
                .param("clave", "clave")
                .param("status", "A")
                .param("fechaInicial", "12/12/12")
                .param("fechaFinal", "12/12/12"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "periodo.creado.message"));
    }

    @Test
    public void debieraeliminarPeriodo() throws Exception {
        log.debug("Debiera eliminar periodo");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Periodo periodo = new Periodo("test", "A", "clave", new Date(), new Date());
        periodo.setOrganizacion(organizacion);
        currentSession().save(periodo);
        Long id = periodo.getId();
        this.mockMvc.perform(post("/inscripciones/periodos/elimina")
                .param("id", periodo.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "periodo.eliminado.message"));
    }

    @Test
    public void debieraActualizarPeriodo() throws Exception {
        log.debug("Debiera actualizar periodo");

        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        Periodo periodo = new Periodo("test", "A", "clave", new Date(), new Date());
        periodo.setOrganizacion(organizacion);
        instance.graba(periodo);
        assertNotNull(periodo);
        assertNotNull(periodo.getId());
        assertEquals("test", periodo.getDescripcion());

        this.mockMvc.perform(post(Constantes.PATH_PERIODOS_GRABA)
                .param("descripcion", "TEST-1")
                .param("clave", "clave")
                .param("status", "A")
                .param("fechaInicial", "12/12/12")
                .param("fechaFinal", "12/12/12")
                .param("id", periodo.getId().toString())
                .param("version", periodo.getVersion().toString()))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl(Constantes.PATH_PERIODOS + "/"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "periodo.actualizado.message"));

    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}
