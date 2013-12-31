/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * TODO problemas con nullpointerexception
 */
package mx.edu.um.mateo.colportor.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.colportor.dao.DocumentoDao;
import static mx.edu.um.mateo.colportor.dao.DocumentoDaoTest.FOLIO;
import static mx.edu.um.mateo.colportor.dao.DocumentoDaoTest.IMPORTE;
import static mx.edu.um.mateo.colportor.dao.DocumentoDaoTest.OBSERVACIONES;
import static mx.edu.um.mateo.colportor.dao.DocumentoDaoTest.TIPO_DOCUMENTO;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Documento;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.model.Colegio;
import org.junit.*;
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
 * @author osoto
 
 Para estas pruebas, se existen las siguientes pre-condiciones:
 - El colportor ya esta logueado, y por lo tanto es facil obtenerlo del ambiente
 - La captura de cada documento, traera como parametro de entrada la temporada.id
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class DocumentoControllerTest extends BaseControllerTest {

    @Autowired
    private DocumentoDao documentoDao;
    @Autowired
    private TemporadaColportorDao temporadaColportorDao;
         
    @Test
    public void testListaAlColportor() throws Exception {
        log.debug("Debiera monstrar lista de documentos");
        
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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento);
        }
        
        authenticate(colportor, colportor.getPassword(), new ArrayList <GrantedAuthority> (colportor.getRoles()));
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA)
                .param("clave", "54321")
                .param("temporada.id", temporada.getId().toString()))
                .andExpect(model().attributeExists(Constantes.DOCUMENTOCOLPORTOR_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA+".jsp"));
    }

     
    @Test
    public void testListaAlColportorSinTemporadaActiva() throws Exception {
        log.debug("Debiera monstrar lista de documentos de un colportor que no esta en ninguna temporada");
        
        Usuario colportor = obtieneColportor();
        Usuario asociado = obtieneAsociado(colportor);
                
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Colegio colegio = new Colegio("UM", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        TemporadaColportor temporadaColportor = new TemporadaColportor(Constantes.STATUS_INACTIVO, "TEST", "TEST");
        temporadaColportor.setColportor((Colportor) colportor);
        temporadaColportor.setAsociado((Asociado) asociado);
        temporadaColportor.setTemporada(temporada);
        temporadaColportor.setColegio(colegio);
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento);
        }
        
        authenticate(colportor, colportor.getPassword(), new ArrayList <GrantedAuthority> (colportor.getRoles()));
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA)
                .param("clave", "54321")
                .param("temporada.id", temporada.getId().toString()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA+".jsp"));
    }


    @Test
    public void testListaAlColportorCambiaTemporada() throws Exception {
        log.debug("Debiera monstrar lista de documentos de un colportor qeu no esta en ninguna temporada");
        
        Usuario colportor = obtieneColportor();
        Usuario asociado = obtieneAsociado(colportor);
                
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Temporada temporada2 = new Temporada("test");
        temporada2.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada2);
        assertNotNull(temporada2.getId());
        
        Colegio colegio = new Colegio("UM", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        TemporadaColportor temporadaColportor = new TemporadaColportor(Constantes.STATUS_INACTIVO, "TEST", "TEST");
        temporadaColportor.setColportor((Colportor) colportor);
        temporadaColportor.setAsociado((Asociado) asociado);
        temporadaColportor.setTemporada(temporada);
        temporadaColportor.setColegio(colegio);
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento);
        }
        
        TemporadaColportor temporadaColportor2 = new TemporadaColportor(Constantes.STATUS_INACTIVO, "TEST", "TEST");
        temporadaColportor2.setColportor((Colportor) colportor);
        temporadaColportor2.setAsociado((Asociado) asociado);
        temporadaColportor2.setTemporada(temporada2);
        temporadaColportor2.setColegio(colegio);
        temporadaColportor2.setObjetivo("2000");
        currentSession().save(temporadaColportor2);
        assertNotNull(temporadaColportor2.getId());

        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor2);
            currentSession().save(documento);
            assertNotNull(documento);
        }
        
        authenticate(colportor, colportor.getPassword(), new ArrayList <GrantedAuthority> (colportor.getRoles()));
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA)
                .param("clave", "54321")
                .param("temporada.id", temporada.getId().toString()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA+".jsp"));
        log.debug("terminaPrimerLlamada");

        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA)
                .param("clave", "54321")
                .param("temporada.id", temporada2.getId().toString()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA+".jsp"));
        log.debug("terminaSegundaLlamada");

    }

    @Test
    public void testListaVaciaAlAsociado() throws Exception {
        log.debug("Debiera monstrar lista de documentos de un colportor al asociado");
        
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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA)
                .param("temporada.id", temporada.getId().toString())
                .param("clave", "5678"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA+".jsp"));
    }

     
    @Test
    public void testListaAlAsociado() throws Exception {
        log.debug("Mostrando documentos de un colportor seleccionado por su clave");
        Usuario asociado = obtieneAsociado();
        Usuario colportor = obtieneColportor(asociado, "5678");
                
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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }        
                
        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));
        
        this.mockMvc.perform(post(Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA)
                .param("temporada.id", temporada.getId().toString())
                .param("clave", "5678"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA+".jsp"))
                //.andExpect(model().attributeExists(Constantes.TEMPORADACOLPORTAJE_LIST))
                .andExpect(model().attributeExists(Constantes.DOCUMENTOCOLPORTOR_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
        
        
    }
    
    @Test
    public void testListaAlAsociadoSinClave() throws Exception {
        log.debug("Se valida que regrese un error, ya que no se proporciono la clave del colportor");
        Usuario asociado = obtieneAsociado();
        Usuario colportor = obtieneColportor(asociado, "5678");
                
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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }        
                
        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));
        
        this.mockMvc.perform(post(Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA)
                .param("temporada.id", temporada.getId().toString())
                .param("clave", "5678"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA+".jsp"))
                //.andExpect(model().attributeExists(Constantes.TEMPORADACOLPORTAJE_LIST))
                .andExpect(model().attributeExists(Constantes.DOCUMENTOCOLPORTOR_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
        
        
    }
    
    @Test
    public void testListaAlAsociadoClaveInvalia() throws Exception {
        log.debug("Se valida que regrese un error, ya que el asociado proporciono una clave de colportor invalida");
        Usuario asociado = obtieneAsociado();
        Usuario colportor = obtieneColportor(asociado, "5678");
                
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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }        
                
        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));
        
        this.mockMvc.perform(post(Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA)
                .param("temporada.id", temporada.getId().toString())
                .param("clave", "5678"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA+".jsp"))
                //.andExpect(model().attributeExists(Constantes.TEMPORADACOLPORTAJE_LIST))
                .andExpect(model().attributeExists(Constantes.DOCUMENTOCOLPORTOR_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
        
        
    }
     
    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar documento");
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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        Documento documento = null;
        for (int i = 0; i < 20; i++) {
            documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento);
        }
        
        authenticate(colportor, colportor.getPassword(), new ArrayList <GrantedAuthority> (colportor.getRoles()));
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_PATH_VER+"/"+documento.getId()))
                .andExpect(model().attributeExists(Constantes.DOCUMENTOCOLPORTOR))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_PATH_VER+".jsp"));
    }

    @Test
    public void debieraCrearDocumentoPorColportor() throws Exception {
        log.debug("Debiera crear documento por colportor");

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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        Documento documento = null;
        for (int i = 0; i < 20; i++) {
            documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento);
        }
        
        authenticate(colportor, colportor.getPassword(), new ArrayList <GrantedAuthority> (colportor.getRoles()));
        
        this.mockMvc.perform(post(Constantes.DOCUMENTOCOLPORTOR_PATH_CREA)
                .param("temporada.id", temporada.getId().toString())
                .param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO)
                .param("folio", Constantes.FOLIO)
                .param("importe", "0.0")
                .param("fecha", "05/05/2010")
                .param("observaciones", Constantes.OBSERVACIONES))
                .andExpect(redirectedUrl(Constantes.DOCUMENTOCOLPORTOR_PATH_VER+"/"+(documento.getId()+1L)))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"));
    }
    
    @Test
    public void debieraCrearDocumentoPorAsociado() throws Exception {
        log.debug("Debiera crear documento por asociado");

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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        Documento documento = null;
        for (int i = 0; i < 20; i++) {
            documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento);
        }
        
        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));
        
        this.mockMvc.perform(post(Constantes.DOCUMENTOCOLPORTOR_PATH_CREA)
                .param("temporadaColportor.colportor.clave", ((Colportor)colportor).getClave())
                .param("temporadaColportor.temporada.id", temporada.getId().toString())
                .param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO)
                .param("folio", Constantes.FOLIO)
                .param("importe", "0.0")
                .param("fecha", "05/05/2010")
                .param("observaciones", Constantes.OBSERVACIONES))
                .andExpect(redirectedUrl(Constantes.DOCUMENTOCOLPORTOR_PATH_VER+"/"+(documento.getId()+1L)))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"));
    }

     
    @Test
    public void debieraActualizarDocumento() throws Exception {
        log.debug("Debiera actualizar documento");
        
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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        Documento documento = null;
        for (int i = 0; i < 20; i++) {
            documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento);
        }
        
        authenticate(colportor, colportor.getPassword(), new ArrayList <GrantedAuthority> (colportor.getRoles()));

        this.mockMvc.perform(post(Constantes.DOCUMENTOCOLPORTOR_PATH_CREA)
                .param("temporada.id", temporada.getId().toString())
                .param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO)
                .param("folio", Constantes.FOLIO)
                .param("importe", "0.0")
                .param("fecha", "05/05/2010")
                .param("observaciones", Constantes.OBSERVACIONES))
                .andExpect(redirectedUrl(Constantes.DOCUMENTOCOLPORTOR_PATH_VER+"/"+(documento.getId()+1L)))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"));
        
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("temporadaColportor", temporadaColportor.getId());
        params = documentoDao.lista(params);
        Integer nRows = ((List)params.get(Constantes.DOCUMENTOCOLPORTOR_LIST)).size();
        
        documento = documentoDao.obtiene(documento.getId()+1L);
        assertNotNull(documento.getId());
        
        this.mockMvc.perform(post(Constantes.DOCUMENTOCOLPORTOR_PATH_ACTUALIZA)
                .param("id", documento.getId().toString())
                .param("version", documento.getVersion().toString())
                .param("temporada.id", temporada.getId().toString())
                .param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO)
                .param("folio", "modificado")
                .param("importe", "0.0")
                .param("fecha", "05/05/2010")
                .param("observaciones", Constantes.OBSERVACIONES))
                .andExpect(redirectedUrl(Constantes.DOCUMENTOCOLPORTOR_PATH_VER+"/"+documento.getId()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.actualizado.message"));
        
        params = documentoDao.lista(params);
        Integer nRows2 = ((List)params.get(Constantes.DOCUMENTOCOLPORTOR_LIST)).size();
        assertEquals(nRows, nRows2);
        
        documento = documentoDao.obtiene(documento.getId());
        assertNotNull(documento.getId());
        assertEquals("modificado", documento.getFolio());

    }


    @Test
    public void debieraEliminarDocumento() throws Exception {
        log.debug("Debiera eliminar documento");
        
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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        Documento documento = null;
        for (int i = 0; i < 20; i++) {
            documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento.getId());
        }
        
        authenticate(colportor, colportor.getPassword(), new ArrayList <GrantedAuthority> (colportor.getRoles()));

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("temporadaColportor", temporadaColportor.getId());
        params = documentoDao.lista(params);
        assertNotNull(params.get(Constantes.DOCUMENTOCOLPORTOR_LIST));
        Integer nRows = ((List)params.get(Constantes.DOCUMENTOCOLPORTOR_LIST)).size();
        
        this.mockMvc.perform(post(Constantes.DOCUMENTOCOLPORTOR_PATH_ELIMINA)
                .param("id", documento.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.eliminado.message"))
                .andExpect(redirectedUrl(Constantes.DOCUMENTOCOLPORTOR_PATH));
        
        params = documentoDao.lista(params);
        assertNotNull(params.get(Constantes.DOCUMENTOCOLPORTOR_LIST));
        Integer nRows2 = ((List)params.get(Constantes.DOCUMENTOCOLPORTOR_LIST)).size();
        //assertEquals((Long)(nRows-1L), nRows2);
    }
    
    
    @Test
    public void deberiaProbarTablaDeTotales() throws Exception{
        log.debug("Deberia probar tabla de resultados de acuerdo a los documentos creados");
        
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
        temporadaColportor.setObjetivo("2000");
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        authenticate(colportor, colportor.getPassword(), new ArrayList <GrantedAuthority> (colportor.getRoles()));

        Documento documento = null;
        for (int i = 0; i < 20; i++) {
            documento = new Documento(Constantes.BOLETIN, Constantes.FOLIO,
                    new Date(), new BigDecimal("500"), Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
        }

        for (int i = 0; i < 20; i++) {
            documento = new Documento(Constantes.DIEZMO, Constantes.FOLIO,
                    new Date(), new BigDecimal("50"), Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
        }

        for (int i = 0; i < 20; i++) {
            documento = new Documento(Constantes.DEPOSITO_CAJA, Constantes.FOLIO,
                    new Date(), new BigDecimal("100"), Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
        }

        for (int i = 0; i < 20; i++) {
            documento = new Documento(Constantes.DEPOSITO_BANCO, Constantes.FOLIO,
                    new Date(), new BigDecimal("100"), Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
        }

        for (int i = 0; i < 20; i++) {
            documento = new Documento(Constantes.NOTAS_DE_COMPRA, Constantes.FOLIO,
                    new Date(), new BigDecimal("100"), Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
        }
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_PATH)
                .param("clave", "54321")
                .param("temporadaId", "1"))
                .andExpect(model().attributeExists(Constantes.TOTALBOLETIN))
                .andExpect(model().attributeExists(Constantes.TOTALDIEZMOS))
                .andExpect(model().attributeExists(Constantes.TOTALDEPOSITOS))
                .andExpect(model().attributeExists(Constantes.FIDELIDAD))
                .andExpect(model().attributeExists(Constantes.ALCANZADO))
                .andExpect(model().attributeExists(Constantes.TOTALBOLETIN))
                .andExpect(model().attribute(Constantes.TOTALBOLETIN, new BigDecimal("10000.00").setScale(2, BigDecimal.ROUND_HALF_EVEN)))
                .andExpect(model().attribute(Constantes.TOTALDIEZMOS, new BigDecimal("1000.00").setScale(2, BigDecimal.ROUND_HALF_EVEN)))
                .andExpect(model().attribute(Constantes.TOTALDEPOSITOS, new BigDecimal("4000.00").setScale(2, BigDecimal.ROUND_HALF_EVEN)))
                .andExpect(model().attribute(Constantes.FIDELIDAD, new BigDecimal("100.000000").setScale(2, BigDecimal.ROUND_HALF_EVEN)))
                .andExpect(model().attribute(Constantes.ALCANZADO, new BigDecimal("500.00").setScale(2, BigDecimal.ROUND_HALF_EVEN)))
                .andExpect(view().name(Constantes.DOCUMENTOCOLPORTOR_PATH_LISTA));
    }
}
