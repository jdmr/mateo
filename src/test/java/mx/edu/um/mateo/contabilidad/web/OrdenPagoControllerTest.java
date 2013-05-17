
package mx.edu.um.mateo.contabilidad.web;

import java.util.ArrayList;
import java.util.Date;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.contabilidad.dao.OrdenPagoDao;
import mx.edu.um.mateo.contabilidad.model.OrdenPago;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
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
 * @author osoto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class OrdenPagoControllerTest extends BaseControllerTest {
    
    @Autowired
    private OrdenPagoDao instance;
    
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera monstrar lista ordenes de pago");
        Usuario usuario = this.obtieneUsuario();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        OrdenPago orden = null;        
        for (int i = 0; i < 20; i++) {
            orden = new OrdenPago("TEST", true, new Date(), usuario.getEmpresa(), Constantes.STATUS_ACTIVO, 
                    usuario, new Date(), Constantes.STATUS_ACTIVO);

            currentSession().save(orden);
            assertNotNull(orden.getId());
        }

        this.mockMvc.perform(get(Constantes.ORDENPAGO_PATH)
                .sessionAttr("empresaId", usuario.getEmpresa().getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.ORDENPAGO_PATH_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ORDENPAGO_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar una Orden de Pago");
        
        Usuario usuario = this.obtieneUsuario();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        OrdenPago orden = new OrdenPago("TEST", true, new Date(), usuario.getEmpresa(), Constantes.STATUS_ACTIVO, 
                    usuario, new Date(), Constantes.STATUS_ACTIVO);

        currentSession().save(orden);
        assertNotNull(orden.getId());        
        
        this.mockMvc.perform(get(Constantes.ORDENPAGO_PATH_VER + "/" + orden.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.ORDENPAGO_PATH_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ORDENPAGO));
    }

    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera grabar una Orden de Pago nueva");
        
        Usuario usuario = this.obtieneUsuario();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        OrdenPago orden = new OrdenPago("TEST", true, new Date(), usuario.getEmpresa(), Constantes.STATUS_ACTIVO, 
                    usuario, new Date(), Constantes.STATUS_ACTIVO);

        currentSession().save(orden);
        assertNotNull(orden.getId());    
        
        this.mockMvc.perform(post(Constantes.ORDENPAGO_PATH_GRABA)
                .param("descripcion", "test")
                .param("cheque", "false")
                .param("fechaPago", "01/01/2012"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "ordenPago.graba.message"))
                .andExpect(redirectedUrl(Constantes.ORDENPAGO_PATH));
    }

    @Test
    public void testActualiza() throws Exception {
        log.debug("Debiera modificar una Orden de Pago nueva");
        
        Usuario usuario = this.obtieneUsuario();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        OrdenPago orden = new OrdenPago("TEST", true, new Date(), usuario.getEmpresa(), Constantes.STATUS_ACTIVO, 
                    usuario, new Date(), Constantes.STATUS_ACTIVO);

        currentSession().save(orden);
        assertNotNull(orden.getId());    
        
        this.mockMvc.perform(post(Constantes.ORDENPAGO_PATH_ACTUALIZA)
                .param("id", orden.getId().toString())
                .param("version", orden.getVersion().toString())
                .param("descripcion", "test1")
                .param("cheque", "false")
                .param("fechaPago", "01/01/2012")
                .param("status", orden.getStatus())
                .param("statusInterno", orden.getStatusInterno()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "ordenPago.actualiza.message"))
                .andExpect(redirectedUrl(Constantes.ORDENPAGO_PATH));
        
    }

    @Test
    public void testElimina() throws Exception {
        log.debug("Debiera eliminar una Orden de Pago ");
        
        Usuario usuario = this.obtieneUsuario();
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        OrdenPago orden = new OrdenPago("TEST", true, new Date(), usuario.getEmpresa(), Constantes.STATUS_ACTIVO, 
                    usuario, new Date(), Constantes.STATUS_ACTIVO);

        currentSession().save(orden);
        assertNotNull(orden.getId());    
        
        this.mockMvc.perform(post(Constantes.ORDENPAGO_PATH_ELIMINA)
                .param("id", orden.getId().toString()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "ordenPago.elimina.message"))
                .andExpect(redirectedUrl(Constantes.ORDENPAGO_PATH));
        
        orden = instance.obtiene(orden.getId());
        if(orden != null){
            fail("La orden de pago todavia existe!!!");
        }
    }
}
