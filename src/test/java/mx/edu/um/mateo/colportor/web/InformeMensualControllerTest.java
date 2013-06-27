/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import mx.edu.um.mateo.colportor.dao.InformeMensualDao;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import org.junit.Test;
import static org.junit.Assert.*;
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
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class InformeMensualControllerTest extends BaseControllerTest{
   
    @Autowired
    private InformeMensualDao instance;
    
    /**
     * Prueba la lista de informeMensuals
     */
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de informeMensuales");
        
        Usuario colportor = obtieneColportor();
                
        InformeMensual informe = null;
        for (int i = 0; i < 20; i++) {
            informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
            currentSession().save(informe);
            assertNotNull(informe.getId());

        }
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));

        this.mockMvc.perform(get(Constantes.INFORMEMENSUAL_PATH)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.INFORMEMENSUAL_PATH_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.INFORMEMENSUAL_LIST)).
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
        
        Usuario colportor = obtieneColportor();
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));
                
        this.mockMvc.perform(get(Constantes.INFORMEMENSUAL_PATH_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.INFORMEMENSUAL_PATH_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.INFORMEMENSUAL));
    }
    
    /**
     * Prueba que se muestre el jsp Edita
     */
    @Test
    public void testEdita() throws Exception {
        log.debug("Test 'edita'");
        
        Usuario colportor = obtieneColportor();
                
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));
                
        this.mockMvc.perform(get(Constantes.INFORMEMENSUAL_PATH_EDITA+"/"+informe.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.INFORMEMENSUAL_PATH_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.INFORMEMENSUAL))
                .andExpect(model().attribute(Constantes.INFORMEMENSUAL, informe));
    }
    
    /**
     * Prueba que se muestre el jsp Ver
     */
    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar informeMensuals");
        
        Usuario colportor = obtieneColportor();
        
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));
        
        this.mockMvc.perform(get(Constantes.INFORMEMENSUAL_PATH_VER + "/" + informe.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.INFORMEMENSUAL_PATH_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.INFORMEMENSUAL));
    }

    /**
     * Prueba que el proceso de Grabar un informeMensual
     */
    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear informeMensual");
        
        Usuario usuario = obtieneColportor();        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        
        this.mockMvc.perform(post(Constantes.INFORMEMENSUAL_PATH_CREA)
                .param("fecha", "01/01/2012"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "informeMensual.creada.message"))
                .andExpect(redirectedUrl(Constantes.INFORMEMENSUAL_PATH_LISTA));
    }
    
    /**
     * Prueba el proceso de Actualizacion de un informeMensual
     */
    @Test
    public void testActualiza() throws Exception {
        log.debug("Test 'actualiza' un informeMensual");
        
        Usuario colportor = obtieneColportor();
        
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));
        
        this.mockMvc.perform(post(Constantes.INFORMEMENSUAL_PATH_ACTUALIZA)
                .param("id", informe.getId().toString())
                .param("version", informe.getVersion().toString())
                .param("status", informe.getStatus())
                .param("colportor.id", colportor.getId().toString())
                .param("fecha", "01/01/2012"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "informeMensual.actualizada.message"))
                .andExpect(redirectedUrl(Constantes.INFORMEMENSUAL_PATH_LISTA));
        
        informe = instance.obtiene(informe.getId());
        log.debug("{}",informe);
        Calendar gc = new GregorianCalendar(2012, 0, 01);
        assertEquals(gc.getTime(), informe.getFecha());
    }

    /**
     * Prueba el proceso de Borrado de un informeMensual
     */
    @Test
    public void testElimina() throws Exception{
        log.debug("Test 'elimina' informeMensual");
        
        Usuario colportor = obtieneColportor();
        
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());;
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));
        
        this.mockMvc.perform(post(Constantes.INFORMEMENSUAL_PATH_ELIMINA)
                .param("id", informe.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "informeMensual.eliminada.message"))
                .andExpect(redirectedUrl(Constantes.INFORMEMENSUAL_PATH_LISTA));
    }
}
