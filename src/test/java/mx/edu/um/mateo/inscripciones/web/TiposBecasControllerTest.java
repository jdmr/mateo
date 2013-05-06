/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.TiposBecasDao;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import static org.junit.Assert.*;
import org.junit.Test;
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
public class TiposBecasControllerTest extends BaseControllerTest {

    @Autowired
    private TiposBecasDao instance;
    
    /**
     * Test of lista method, of class TiposBecasController.
     */
    @Test
    public void testListaTiposBecas() throws Exception {
        log.debug("Debiera mostrar lista de tipos Becas");
        Usuario usuario = obtieneUsuario();
        for (int i = 0; i < 20; i++) {
            TiposBecas tiposBecas = new TiposBecas();
            tiposBecas.setDescripcion("test" + i);
            tiposBecas.setDiezma(true);
            tiposBecas.setNumHoras(320);
            tiposBecas.setPerteneceAlumno(true);
            tiposBecas.setPorcentaje(new BigDecimal(12.3));
            tiposBecas.setSoloPostgrado(false);
            tiposBecas.setStatus("a");
            tiposBecas.setTope(new BigDecimal(320));
            tiposBecas.setEmpresa(usuario.getEmpresa());
            currentSession().save(tiposBecas);
        }

        this.mockMvc.perform(get(Constantes.PATH_TIPOSBECAS)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPOSBECAS_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_TIPOSBECAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    /**
     * Prueba que se muestre el jsp Nuevo
     */
    @Test
    public void testNuevoTipoBeca() throws Exception {
        log.debug("Test 'nuevo' tipoBeca");
                
        this.mockMvc.perform(get(Constantes.PATH_TIPOSBECAS_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPOSBECAS_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_TIPOSBECAS));
    }
    
    /**
     * Test of ver method, of class TiposBecasController.
     */
    @Test
    public void testVerTipoBeca() throws Exception {
        log.debug("Debiera mostrar un tipo de beca");
        Usuario usuario = obtieneUsuario();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(true);
        tiposBecas.setPorcentaje(new BigDecimal(12.3));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(320));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tiposBecas);
        assertNotNull(tiposBecas.getId());
        this.mockMvc.perform(get(Constantes.PATH_TIPOSBECAS_VER + "/" + tiposBecas.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPOSBECAS_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_TIPOSBECAS));
    }

     /**
     * Prueba que se muestre el jsp Edita
     */
    @Test
    public void testEditaTipoBeca() throws Exception {
        log.debug("Test 'edita'");
        Usuario usuario = obtieneUsuario();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(true);
        tiposBecas.setPorcentaje(new BigDecimal(12.3));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(320));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tiposBecas);
        assertNotNull(tiposBecas.getId());
                
        this.mockMvc.perform(get(Constantes.PATH_TIPOSBECAS_EDITA+"/"+tiposBecas.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TIPOSBECAS_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_TIPOSBECAS))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_TIPOSBECAS, tiposBecas));
    }
    
     /**
     * Prueba que el proceso de Grabar un tipoBeca
     */
    @Test
    public void testGrabaTipoBeca() throws Exception {
        log.debug("Debiera crear tipoBeca");
        Usuario usuario = obtieneUsuario();        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_TIPOSBECAS_GRABA)
                .param("diezma", "1")
                .param("descripcion", "test")
                .param("numHoras","320")
                .param("perteneceAlumno", "1")
                .param("porcentaje", "80")
                .param("status", "a")
                .param("soloPostgrado", "0")
                .param("tope", "3200"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "tiposBecas.graba.message"))
                .andExpect(redirectedUrl(Constantes.PATH_TIPOSBECAS_LISTA+"/"));
    }
    
    /**
     * Prueba el proceso de Actualizacion de un tipoBeca
     */
    @Test
    public void testActualizaTipoBeca() throws Exception {
        log.debug("Test 'actualiza' un paquete");
        Usuario usuario = obtieneUsuario();        
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(true);
        tiposBecas.setPorcentaje(new BigDecimal(12.3));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(320));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tiposBecas);
        assertNotNull(tiposBecas.getId());
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_TIPOSBECAS_GRABA)
                .param("version", tiposBecas.getVersion().toString())
                .param("id", tiposBecas.getId().toString())
                .param("diezma", tiposBecas.getDiezma().toString())
                .param("descripcion","prueba")
                .param("numHoras",tiposBecas.getNumHoras().toString())
                .param("perteneceAlumno", tiposBecas.getPerteneceAlumno().toString())
                .param("porcentaje", tiposBecas.getPorcentaje().toString())
                .param("status", tiposBecas.getStatus())
                .param("tope", tiposBecas.getTope().toString())
                .param("soloPostgrado", tiposBecas.getSoloPostgrado().toString())
                .param("empresa.id", usuario.getEmpresa().getId().toString()))
                .andExpect(redirectedUrl(Constantes.PATH_TIPOSBECAS_LISTA+"/"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "tiposBecas.graba.message"));
        
        currentSession().refresh(tiposBecas);
        log.debug("{}",tiposBecas);
        assertEquals("prueba", tiposBecas.getDescripcion());
    }
    /**
     * Test of elimina method, of class TiposBecasController.
     */
    @Test
    public void testElimina() throws Exception {
        log.debug("Debiera mostrar un tipo de beca");
        Usuario usuario = obtieneUsuario();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(true);
        tiposBecas.setPorcentaje(new BigDecimal(12.3));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(320));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tiposBecas);
        assertNotNull(tiposBecas.getId());
        this.mockMvc.perform(post(Constantes.PATH_TIPOSBECAS_ELIMINA)
                .param("id", tiposBecas.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "tiposBecas.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_TIPOSBECAS_LISTA));
    }
}
