/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.CobroCampoDao;
import mx.edu.um.mateo.inscripciones.dao.InstitucionDao;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import mx.edu.um.mateo.inscripciones.model.Institucion;
import mx.edu.um.mateo.inscripciones.service.CobroCampoManager;
import mx.edu.um.mateo.inscripciones.service.InstitucionManager;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
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
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class CobroCampoControllerTest extends BaseControllerTest {

    @Autowired
    private CobroCampoManager instance;
    @Autowired
    private InstitucionDao institucionManager;

    /**
     * Test of lista method, of class CobroCampoController.
     */
    @Test
    public void testLista() throws Exception {
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(institucion);
        assertNotNull(institucion.getId());

        CobroCampo cobroCampo = null;
        for (int i = 0; i < 20; i++) {
            cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
            cobroCampo.setEmpresa(usuario.getEmpresa());
            cobroCampo.setFechaAlta(new Date());
            cobroCampo.setFechaModificacion(new Date());
            cobroCampo.setStatus("A");
            cobroCampo.setUsuarioAlta(usuario);
            cobroCampo.setUsuarioModificacion(usuario);
            instance.graba(cobroCampo, usuario);
            assertNotNull(cobroCampo.getId());
        }
        this.mockMvc.perform(get(Constantes.PATH_COBROCAMPO)).
                andExpect(status().isOk()).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COBROCAMPO_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_COBROSCAMPOS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    /**
     * Prueba que se muestre el jsp Nuevo
     */
    @Test
    public void testNuevo() throws Exception {
        log.debug("Test 'nuevo'");
        Usuario usuario = obtieneUsuario();
        for (int i = 0; i < 20; i++) {
            Institucion institucion = new Institucion();
            institucion.setNombre("Nombre-test");
            institucion.setPorcentaje(new BigDecimal("123"));
            institucion.setStatus("A");
            institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
            currentSession().save(institucion);
            assertNotNull(institucion.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_COBROCAMPO_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COBROCAMPO_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_INSTITUCION))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_COBROCAMPO));
    }

    /**
     * Prueba que se muestre el jsp Edita
     */
    @Test
    public void testEdita() throws Exception {
        log.debug("Test 'edita'");
        Usuario usuario = obtieneUsuario();
        for (int i = 0; i < 20; i++) {
            Institucion institucion = new Institucion();
            institucion.setNombre("Nombre-test");
            institucion.setPorcentaje(new BigDecimal("123"));
            institucion.setStatus("A");
            institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
            currentSession().save(institucion);
            assertNotNull(institucion.getId());
        }
        Institucion institucion1 = new Institucion();
        institucion1.setNombre("Nombre-test");
        institucion1.setPorcentaje(new BigDecimal("123"));
        institucion1.setStatus("A");
        institucion1.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(institucion1);
        assertNotNull(institucion1.getId());
        CobroCampo cobroCampo = new CobroCampo("1110475", institucion1, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(usuario.getEmpresa());
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());

        this.mockMvc.perform(get(Constantes.PATH_COBROCAMPO_EDITA + "/" + cobroCampo.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COBROCAMPO_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_COBROCAMPO))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_INSTITUCION))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_COBROCAMPO, cobroCampo));
    }

    /**
     * Test of ver method, of class CobroCampoController.
     */
    @Test
    public void testVer() throws Exception {
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(institucion);
        assertNotNull(institucion.getId());

        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(usuario.getEmpresa());
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());
        this.mockMvc.perform(get(Constantes.PATH_COBROCAMPO_VER + "/" + cobroCampo.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COBROCAMPO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_COBROCAMPO));
    }

    /**
     * Prueba que el proceso de Grabar un paquete
     */
    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear paquete");
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(institucion);
        assertNotNull(institucion.getId());
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        this.mockMvc.perform(post(Constantes.PATH_COBROCAMPO_GRABA)
                .param("matricula", "1110475")
                .param("institucion.id", institucion.getId().toString())
                .param("importeMatricula", "80")
                .param("importeEnsenanza", "80")
                .param("importeInternado", "80")
                .param("status", "A"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "cobroCampo.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_COBROCAMPO_LISTA + "/"));
    }

    /**
     * Test of elimina method, of class CobroCampoController.
     */
    @Test
    public void testElimina() throws Exception {
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(institucion);
        assertNotNull(institucion.getId());

        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(usuario.getEmpresa());
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());
        this.mockMvc.perform(post(Constantes.PATH_COBROCAMPO_ELIMINA)
                .param("id", cobroCampo.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cobroCampo.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_COBROCAMPO_LISTA));
    }
}
