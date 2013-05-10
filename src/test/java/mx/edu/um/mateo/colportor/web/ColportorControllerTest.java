/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

/**
 * TODO
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.inventario.model.Almacen;
import static org.junit.Assert.assertNotNull;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

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
public class ColportorControllerTest extends BaseControllerTest {
    
    @Autowired
    private ColportorDao colportorDao;
    
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de colportores");
        Usuario usuario = obtieneAsociado();
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        for (int i = 0; i < 20; i++) {
            Colportor colportor = new Colportor("username" + i, "test", "test", "apPaterno", "apMaterno", "test", Constantes.STATUS_ACTIVO,
                    "8262652626", "test", "test", "10706" + i, "test", "test001", new Date());
            colportor.setAlmacen(usuario.getAlmacen());
            colportor.setEmpresa(usuario.getEmpresa());

            currentSession().save(colportor);
            assertNotNull(colportor.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_COLPORTOR)
                .param("filtro", "test2"))
                                
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLPORTOR_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.COLPORTOR_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));

    }

    @Test
    public void testObtiene() throws Exception {
        log.debug("Debiera mostrar colportor");
        Usuario usuario = obtieneAsociado();        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        Colportor colportor = new Colportor("username" , "test", "test", "apPaterno", "apMaterno", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAlmacen(usuario.getAlmacen());
        colportor.setEmpresa(usuario.getEmpresa());
        currentSession().save(colportor);
        assertNotNull(colportor.getId());
        
        this.mockMvc.perform(get(Constantes.PATH_COLPORTOR_VER + "/" + colportor.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLPORTOR_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_COLPORTOR));

    }
    
    @Test
    public void testGraba() throws Exception {
        log.debug("Test Graba");
        
        Usuario usuario = obtieneAsociado();        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_COLPORTOR_CREA)
                .param("username", "testC1")
                .param("correo", "test@test.com")
                .param("password", "test")
                .param("nombre", "testC1")
                .param("apPaterno", "test")
                .param("apMaterno", "test")
                .param("clave", Constantes.CLAVE)
                .param("status", Constantes.STATUS_ACTIVO)
                .param("telefono", Constantes.TELEFONO)
                .param("calle", Constantes.CALLE)
                .param("colonia", Constantes.COLONIA)
                .param("municipio", Constantes.MUNICIPIO)
                .param("tipoDeColportor", Constantes.TIPO_COLPORTOR)
                .param("matricula", Constantes.MATRICULA)
                .param("fechaDeNacimiento", "05/05/2010"))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.creado.message"));
    }

    @Test
    public void testActualiza() throws Exception {
        log.debug("Debiera actualizar colportor");
        
        Usuario usuario = obtieneAsociado();        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        Colportor colportor = new Colportor("username" , "testC2", "test", "apPaterno", "apMaterno", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAlmacen(usuario.getAlmacen());
        colportor.setEmpresa(usuario.getEmpresa());

        currentSession().save(colportor);
        assertNotNull(colportor.getId());

        this.mockMvc.perform(post(Constantes.PATH_COLPORTOR_ACTUALIZA)
                .param("id", colportor.getId().toString())
                .param("version", colportor.getVersion().toString())
                .param("roles", "ROLE_COL")
                .param("roles", Constantes.ROLE_COL)
                .param("username", "testC2")
                .param("correo", "test@test.com")
                .param("password", "test")
                .param("nombre", "test")
                .param("apPaterno", "test")
                .param("apMaterno", "test")
                .param("clave", Constantes.CLAVE)
                .param("status", Constantes.STATUS_ACTIVO)
                .param("telefono", Constantes.TELEFONO)
                .param("calle", Constantes.CALLE)
                .param("colonia", Constantes.COLONIA)
                .param("municipio", Constantes.MUNICIPIO)
                .param("tipoDeColportor", Constantes.TIPO_COLPORTOR)
                .param("matricula", Constantes.MATRICULA)
                .param("fechaDeNacimiento", "05/05/2010"))                
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.actualizado.message"));
    }

    @Test
    public void testElimina() throws Exception {
        log.debug("Debiera eliminar colportor");
        Usuario usuario = obtieneAsociado();        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        Colportor colportor = new Colportor("username" , "test", "test", "apPaterno", "apMaterno", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());        
        colportor.setAlmacen(usuario.getAlmacen());
        colportor.setEmpresa(usuario.getEmpresa());

        currentSession().save(colportor);
        assertNotNull(colportor.getId());

        this.mockMvc.perform(post(Constantes.PATH_COLPORTOR_ELIMINA)
                .param("id", colportor.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.eliminado.message"))
                .andExpect(redirectedUrl(Constantes.PATH_COLPORTOR));

    }
}