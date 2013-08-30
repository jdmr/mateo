/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.rh.dao.ColegioDao;
import mx.edu.um.mateo.rh.model.Colegio;
import static org.junit.Assert.assertNotNull;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
/**
 * TODO Cambiar constructores y actualizar datos
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class TemporadaColportorControllerTest extends BaseControllerTest {
    
    @Autowired
    private TemporadaColportorDao temporadaColportorDao;    
    @Autowired
    private ColegioDao colegioDao;
    
    @Test
    public void lista() throws Exception {
        log.debug("Debiera monstrar lista Temporada Colportor");
        
        Usuario colportor = obtieneColportor();
        Usuario asociado = obtieneAsociado(colportor);
                
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Colegio colegio = new Colegio("UM", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        TemporadaColportor temporadaColportor = null;
        for (int i = 0; i < 20; i++) {
            temporadaColportor = new TemporadaColportor(Constantes.STATUS_ACTIVO + i, "TEST", "TEST"+i);
            temporadaColportor.setColportor((Colportor) colportor);
            temporadaColportor.setAsociado((Asociado)asociado);
            temporadaColportor.setTemporada(temporada);
            temporadaColportor.setColegio(colegio);
            temporadaColportorDao.crea(temporadaColportor);
            assertNotNull(temporadaColportor.getId());
        }
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        this.mockMvc.perform(get(Constantes.TEMPORADACOLPORTOR_PATH))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.TEMPORADACOLPORTOR_PATH_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.TEMPORADACOLPORTOR_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void obtiene() throws Exception {
        log.debug("Debiera mostrar  temporada colpotor");
        
        Usuario colportor = obtieneColportor();
        Usuario asociado = obtieneAsociado(colportor);
                
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Colegio colegio = new Colegio("UM", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        TemporadaColportor temporadaColportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "TEST", "TEST");
        temporadaColportor.setColportor((Colportor) colportor);
        temporadaColportor.setAsociado((Asociado)asociado);
        temporadaColportor.setTemporada(temporada);
        temporadaColportor.setColegio(colegio);
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));
        
        this.mockMvc.perform(get(Constantes.TEMPORADACOLPORTOR_PATH_VER + "/" + temporadaColportor.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.TEMPORADACOLPORTOR_PATH_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.TEMPORADACOLPORTOR));

    }

    @Test
    public void crea() throws Exception {
        log.debug("Debiera crear cuenta de Temporada Colportor");
        
        Usuario colportor = obtieneColportor();
        Usuario asociado = obtieneAsociado(colportor);
                
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Colegio colegio = new Colegio("UM", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);

        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));
        
        TemporadaColportor temporadaColportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "TEST", "TEST");
        temporadaColportor.setColportor((Colportor) colportor);
        temporadaColportor.setAsociado((Asociado)asociado);
        temporadaColportor.setTemporada(temporada);
        temporadaColportor.setColegio(colegio);
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        this.mockMvc.perform(
                post(Constantes.TEMPORADACOLPORTOR_PATH_CREA)
                .param("objetivo", "test")
                .param("observaciones", "test")
                .param("temporada.id", temporada.getId().toString())
                .param("colportor.id", colportor.getId().toString())
                .param("colegio.id", colegio.getId().toString()))
                .andExpect(redirectedUrl(Constantes.TEMPORADACOLPORTOR_PATH_VER + "/" + (1L+temporadaColportor.getId())))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.graba.message"));
        
        TemporadaColportor tmpClp = temporadaColportorDao.obtiene(temporadaColportor.getId()+1L);
        assertNotNull(tmpClp);
        assertNotNull(tmpClp.getAsociado());
        log.debug("Asociado {}",tmpClp.getAsociado());
    }

    @Test
    public void actualiza() throws Exception {
        log.debug("Debiera actualizar  temporada Colportor");
        
        Usuario colportor = obtieneColportor();
        Usuario asociado = obtieneAsociado(colportor);
                
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Colegio colegio = new Colegio("UM", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        TemporadaColportor temporadaColportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "TEST", "TEST");
        temporadaColportor.setColportor((Colportor) colportor);
        temporadaColportor.setAsociado((Asociado)asociado);
        temporadaColportor.setTemporada(temporada);
        temporadaColportor.setColegio(colegio);
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));
        
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        this.mockMvc.perform(post(Constantes.TEMPORADACOLPORTOR_PATH_ACTUALIZA)
                .param("id", temporadaColportor.getId().toString())
                .param("version", temporadaColportor.getVersion().toString())
                .param("fecha", sdf.format(new Date()))
                .param("status", Constantes.STATUS_ACTIVO)
                .param("objetivo", "test")
                .param("observaciones", "modificado")
                .param("temporada.id", temporada.getId().toString())
                .param("colportor.id", colportor.getId().toString())
                .param("colegio.id", colegio.getId().toString()))
                .andExpect(redirectedUrl(Constantes.TEMPORADACOLPORTOR_PATH_VER + "/" + temporadaColportor.getId()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.actualizada.message"));
    }

    @Test
    public void elimina() throws Exception {
        log.debug("Debiera eliminar  temporada Colportor");
        
        Usuario colportor = obtieneColportor();
        Usuario asociado = obtieneAsociado(colportor);
                
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Colegio colegio = new Colegio("UM", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        TemporadaColportor temporadaColportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "TEST", "TEST");
        temporadaColportor.setColportor((Colportor) colportor);
        temporadaColportor.setAsociado((Asociado) asociado);
        temporadaColportor.setTemporada(temporada);
        temporadaColportor.setColegio(colegio);
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor);
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));
        
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        this.mockMvc.perform(post(
                Constantes.TEMPORADACOLPORTOR_PATH_ELIMINA)
                .param("id", temporadaColportor.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.eliminada.message"));
    }

    @Test
    public void nuevaTemporada() throws Exception {
        log.debug("Deberia Probar Nueva De TemporadaColportor Controller");
        
        Usuario colportor = obtieneColportor();
        Usuario asociado = obtieneAsociado(colportor);
        
        Colportor clp = null;
        for (int i = 0; i < 10; i++) {
            clp = new Colportor("test-" + i + "@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                    "8262652626", "test", "test", "10706" + i, "test", "test001", new Date());
            clp.setEmpresa(colportor.getEmpresa());
            clp.setAlmacen(colportor.getAlmacen());
            clp.setRoles(colportor.getRoles());
            currentSession().save(clp);
            assertNotNull(clp.getId());
        }
        Asociado asoc = null;
        for (int i = 0; i < 10; i++) {
            asoc = new Asociado("test--" + i + "@test.com", "test", "test", "test", "test",
                    Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                    Constantes.MUNICIPIO);
            asoc.setEmpresa(asociado.getEmpresa());
            asoc.setAlmacen(asociado.getAlmacen());
            asoc.setRoles(asociado.getRoles());
            currentSession().save(asoc);
            assertNotNull(asoc.getId());
        }
        Colegio colegio = null;
        for (int i = 0; i < 10; i++) {
            colegio = new Colegio(Constantes.NOMBRE + "--" + i, Constantes.STATUS_ACTIVO);
            currentSession().save(colegio);
            assertNotNull(colegio);
        }
        Temporada temporada = null;
        for (int i = 0; i < 10; i++) {
            temporada = new Temporada("test"+i);
            temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
            currentSession().save(temporada);
            assertNotNull(temporada.getId());
        }
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));
        
        this.mockMvc.perform(post(Constantes.TEMPORADACOLPORTOR_PATH_NUEVA))
                .andExpect(view().name(Constantes.TEMPORADACOLPORTOR_PATH_NUEVA));

    }
}
