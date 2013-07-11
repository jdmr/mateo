/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;
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
public class InformeMensualDetalleControllerTest extends BaseControllerTest{
   
    @Autowired
    private InformeMensualDetalleDao instance;
    
    /**
     * Prueba la lista de informeMensuals
     */
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de informeMensuales");
        
        Usuario colportor = obtieneColportor();
        
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
                
        InformeMensualDetalle detalle = null;
        for (int i = 0; i < 20; i++) {
            //InformeMensual informe, Date fecha, Double hrsTrabajadas, Integer librosRegalados, BigDecimal totalPedidos, BigDecimal totalVentas, Integer literaturaGratis, Integer oracionesOfrecidas, Integer casasVisitadas, Integer contactosEstudiosBiblicos, Integer bautizados, Usuario capturo, Date cuando

            detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
            currentSession().save(detalle);
            assertNotNull(detalle.getId());

        }
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));

        this.mockMvc.perform(get(Constantes.INFORMEMENSUAL_DETALLE_PATH_LISTA)
                .param("id", informe.getId().toString())).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.INFORMEMENSUAL_DETALLE_PATH_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.INFORMEMENSUAL_DETALLE_LIST)).
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
        
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));
                
        this.mockMvc.perform(get(Constantes.INFORMEMENSUAL_DETALLE_PATH_NUEVO)
                 .sessionAttr(Constantes.INFORMEMENSUAL, informe))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.INFORMEMENSUAL_DETALLE_PATH_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.INFORMEMENSUAL_DETALLE));
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
        
        InformeMensualDetalle detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));
                
        this.mockMvc.perform(get(Constantes.INFORMEMENSUAL_DETALLE_PATH_EDITA+"/"+detalle.getId())
                .sessionAttr(Constantes.INFORMEMENSUAL, informe))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.INFORMEMENSUAL_DETALLE_PATH_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.INFORMEMENSUAL_DETALLE))
                .andExpect(model().attribute(Constantes.INFORMEMENSUAL_DETALLE, detalle));
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
        
        InformeMensualDetalle detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));
        
        this.mockMvc.perform(get(Constantes.INFORMEMENSUAL_DETALLE_PATH_VER + "/" + detalle.getId())
                .sessionAttr(Constantes.INFORMEMENSUAL, informe))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.INFORMEMENSUAL_DETALLE_PATH_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.INFORMEMENSUAL_DETALLE));
    }

    /**
     * Prueba que el proceso de Grabar un informeMensual
     */
    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear informeMensual");
        
        Usuario colportor = obtieneColportor();        
        
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        
        InformeMensualDetalle detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));        
        
        this.mockMvc.perform(post(Constantes.INFORMEMENSUAL_DETALLE_PATH_CREA)
                .param("hrsTrabajadas", "3.2")
                .param("literaturaVendida", "3")
                .param("totalPedidos", "365.21")
                .param("totalVentas", "165.21")
                .param("literaturaGratis", "21")
                .param("oracionesOfrecidas", "21")
                .param("casasVisitadas", "21")
                .param("contactosEstudiosBiblicos", "21")
                .param("bautizados", "21")
                .param("fecha", "01/01/2012")
                .param("informeMensual.id", informe.getId().toString())
                .sessionAttr(Constantes.INFORMEMENSUAL, informe))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "informeMensualDetalle.creado.message"))
                .andExpect(redirectedUrl(Constantes.INFORMEMENSUAL_DETALLE_PATH_LISTA));
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
        
        InformeMensualDetalle detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));
        
        this.mockMvc.perform(post(Constantes.INFORMEMENSUAL_DETALLE_PATH_ACTUALIZA)
                .param("id", detalle.getId().toString())
                .param("version", detalle.getVersion().toString())
                .param("hrsTrabajadas", "3.2")
                .param("hrsTrabajadas", "3.2")
                .param("literaturaVendida", "3")
                .param("totalPedidos", "365.21")
                .param("totalVentas", "165.21")
                .param("literaturaGratis", "21")
                .param("oracionesOfrecidas", "21")
                .param("casasVisitadas", "21")
                .param("contactosEstudiosBiblicos", "21")
                .param("bautizados", "21")
                .param("fecha", "01/01/2012")
                .param("informeMensual.id", informe.getId().toString())
                .sessionAttr(Constantes.INFORMEMENSUAL, informe))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "informeMensualDetalle.actualizado.message"))
                .andExpect(redirectedUrl(Constantes.INFORMEMENSUAL_DETALLE_PATH_LISTA));
        
        detalle = instance.obtiene(detalle.getId());
        log.debug("{}",detalle);
        Calendar gc = new GregorianCalendar(2012, 0, 01);
        assertEquals(gc.getTime(), detalle.getFecha());
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
        assertNotNull(informe.getId());
        
        InformeMensualDetalle detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());
        
        this.authenticate(colportor, colportor.getPassword(), new ArrayList<GrantedAuthority>(colportor.getRoles()));
        
        this.mockMvc.perform(post(Constantes.INFORMEMENSUAL_DETALLE_PATH_ELIMINA)
                .param("id", detalle.getId().toString())
                .sessionAttr(Constantes.INFORMEMENSUAL, informe))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "informeMensualDetalle.eliminado.message"))
                .andExpect(redirectedUrl(Constantes.INFORMEMENSUAL_DETALLE_PATH_LISTA));
    }
}
