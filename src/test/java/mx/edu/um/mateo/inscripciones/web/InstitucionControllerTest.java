package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.InstitucionDao;
import mx.edu.um.mateo.inscripciones.model.Institucion;
import mx.edu.um.mateo.inventario.model.Almacen;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

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
public class InstitucionControllerTest extends BaseControllerTest {

    
    @Autowired
    private InstitucionDao instance;



    @Test
    public void debieraMostrarListaDeInstitucion() throws Exception {
        log.debug("Debiera mostrar lista de institucion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Institucion institucion = null;
        for (int i = 0; i < 20; i++) {
            institucion = new Institucion();
            institucion.setNombre("Nombre-test");
            institucion.setPorcentaje(new BigDecimal("123"));
            institucion.setStatus("A");
            institucion.setOrganizacion(organizacion);
            instance.graba(institucion);
            assertNotNull(institucion.getId());
        }
        this.mockMvc.perform(
                get(Constantes.PATH_INSTITUCION))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_INSTITUCION_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_INSTITUCION))
                .andExpect(model().attributeExists("paginacion"))
                .andExpect(model().attributeExists("paginas"))
                .andExpect(model().attributeExists("pagina"));
    }


    @Test
    public void debieraMostrarInstitucion() throws Exception {
        log.debug("Debiera mostrar institucion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Institucion institucion = new Institucion();
        institucion.setOrganizacion(organizacion);
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        currentSession().save(institucion);
        Long id = institucion.getId();

        this.mockMvc.perform(get("/inscripciones/instituciones/ver/" + id))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/inscripciones/instituciones/ver.jsp"))
                .andExpect(model().attributeExists("institucion"));

    }

    @Test
    public void debieraCrearInstitucion() throws Exception {
        log.debug("Debiera crear institucion");
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

        this.mockMvc.perform(post(Constantes.PATH_INSTITUCION_GRABA)
                .param("nombre", "TEST-1")
                .param("porcentaje", "test-%")
                .param("status", "A"))
                .andExpect(status().isOk());
        // .andExpect(flash().attributeExists("message"))
        // .andExpect(flash().attribute("message", "institucion.creada.message"));
    }

    @Test
    public void debieraEliminarInstitucion() throws Exception {
        log.debug("Debiera eliminar institucion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Institucion institucion = new Institucion();
        institucion.setOrganizacion(organizacion);
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        currentSession().save(institucion);
        Long id = institucion.getId();
        this.mockMvc.perform(post("/inscripciones/instituciones/elimina")
                .param("id", institucion.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "institucion.eliminada.message"));
    }

    @Test
    public void debieraActualizarInstitucion() throws Exception {
        log.debug("Debiera actualizar institucion");

       Usuario usuario=obtieneUsuario();

        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        Institucion institucion = new Institucion();
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        instance.graba(institucion);
        assertNotNull(institucion.getId());
        assertEquals("Nombre-test", institucion.getNombre());

        this.mockMvc.perform(post(Constantes.PATH_INSTITUCION_GRABA)
                .param("nombre", "TEST-1")
                .param("status", "A")
                .param("porcentaje", "123")
                .param("id", institucion.getId().toString())
                .param("version", institucion.getVersion().toString()))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl(Constantes.PATH_INSTITUCION + "/"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "institucion.actualizada.message"));

    }

}
