/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * TODO problemas con type long: asociado
 */
package mx.edu.um.mateo.colportor.web;

import java.util.ArrayList;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.AsociadoDao;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import static org.junit.Assert.assertNotNull;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;

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
public class AsociadoControllerTest extends BaseControllerTest {
    
    @Autowired
    private AsociadoDao asociadoDao;
    
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera monstrar lista asociado");
        Usuario usuario = obtieneAsociado();
        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        for (int i = 0; i < 20; i++) {
            Asociado asociado = new Asociado("test" + i + "@test.com", "test", "test", "test", "test",
                    Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                    Constantes.MUNICIPIO);

            asociado.setEmpresa(usuario.getEmpresa());
            asociado.setAlmacen(usuario.getAlmacen());
            
            currentSession().save(asociado);
            assertNotNull(asociado.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_ASOCIADO))                
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIADO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ASOCIADO_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void testObtiene() throws Exception {
        log.debug("Debiera mostrar  asociado");
        Usuario usuario = obtieneAsociado();
        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);

        asociado.setEmpresa(usuario.getEmpresa());
        asociado.setAlmacen(usuario.getAlmacen());
            
        currentSession().save(asociado);
        assertNotNull(asociado.getId());


        this.mockMvc.perform(get(Constantes.PATH_ASOCIADO_VER + "/" + asociado.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIADO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_ASOCIADO));
    }

    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear asociado");
        Usuario usuario = obtieneAsociado();
        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        Asociado asociado = new Asociado("jalvaradol", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        
        asociado.setEmpresa(usuario.getEmpresa());
        asociado.setAlmacen(usuario.getAlmacen());
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        
        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_CREA)
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
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.creado.message"))
                .andExpect(redirectedUrl(Constantes.PATH_ASOCIADO_VER+"/"+asociadoDao.obtiene(asociado.getId()+1).getId()));
    }

    @Test
    public void testActualiza() throws Exception {
        log.debug("Debiera actualizar  asociado");
        Usuario usuario = obtieneAsociado();
        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        Asociado asociado = new Asociado("jalvaradol", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setEmpresa(usuario.getEmpresa());
        asociado.setAlmacen(usuario.getAlmacen());
        
        currentSession().save(asociado);
        assertNotNull(asociado.getId());

        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_ACTUALIZA)
            .param("id", asociado.getId().toString())
            .param("version", asociado.getVersion().toString())
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
            .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.actualizado.message"))
            .andExpect(redirectedUrl(Constantes.PATH_ASOCIADO_VER+"/"+asociado.getId()));
    }

    @Test
    public void testElimina() throws Exception {
        log.debug("Debiera eliminar  asociado");
        Usuario usuario = obtieneAsociado();
        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        Asociado asociado = new Asociado("jalvaradol", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setEmpresa(usuario.getEmpresa());
        asociado.setAlmacen(usuario.getAlmacen());
        currentSession().save(asociado);        
        assertNotNull(asociado.getId());
        
        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_ELIMINA)
                .param("id", asociado.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.eliminado.message"))
                .andExpect(redirectedUrl(Constantes.PATH_ASOCIADO));
    }
}
