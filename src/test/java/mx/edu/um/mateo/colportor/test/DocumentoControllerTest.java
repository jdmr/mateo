/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * TODO problemas con nullpointerexception
 */
package mx.edu.um.mateo.colportor.test;

import java.util.ArrayList;
import java.util.Date;
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
import static org.junit.Assert.assertNotNull;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 * 
 * Para estas pruebas, se existen las siguientes pre-condiciones:
 * - El colportor ya esta logueado, y por lo tanto es facil obtenerlo del ambiente
 * - La captura de cada documento, traera como parametro de entrada la temporada.id
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
    public void debieraMostrarListaDeDocumentoDeColportor() throws Exception {
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
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_LISTA)
                .param("temporada.id", temporada.getId().toString()))
                .andExpect(model().attributeExists(Constantes.DOCUMENTOCOLPORTOR_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_LISTA+".jsp"));
    }

     
    @Test
    public void debieraMostrarListaDeDocumentoDeColportorQueNoEstaEnNingunaTemporada() throws Exception {
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
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_LISTA)
                .param("temporada.id", temporada.getId().toString()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_LISTA+".jsp"));
    }


    @Test
    public void debieraMostrarListaDeDocumentoDeColportorQueSeCambiaTemporada() throws Exception {
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
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_LISTA)
                .param("temporada.id", temporada.getId().toString()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_LISTA+".jsp"));
        log.debug("terminaPrimerLlamada");

        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_LISTA)
                .param("temporada.id", temporada2.getId().toString()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_LISTA+".jsp"));
        log.debug("terminaSegundaLlamada");

    }

     
    @Test
    public void debieraMostrarListaDeDocumentoDeColportorQueSeCambiaAUnaTemporadaVacia() throws Exception {
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
        
        TemporadaColportor temporadaColportor2 = new TemporadaColportor(Constantes.STATUS_INACTIVO, "TEST", "TEST");
        temporadaColportor2.setColportor((Colportor) colportor);
        temporadaColportor2.setAsociado((Asociado) asociado);
        temporadaColportor2.setTemporada(temporada2);
        temporadaColportor2.setColegio(colegio);
        temporadaColportor2.setObjetivo("2000");
        currentSession().save(temporadaColportor2);
        assertNotNull(temporadaColportor2.getId());

        authenticate(colportor, colportor.getPassword(), new ArrayList <GrantedAuthority> (colportor.getRoles()));
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_LISTA)
                .param("temporada.id", temporada.getId().toString()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_LISTA+".jsp"));
        log.debug("terminaPrimerLlamada");

        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_LISTA)
                .param("temporada.id", temporada2.getId().toString()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_LISTA+".jsp"));
        log.debug("terminaSegundaLlamada");
    }

     
    @Test
    public void debieraMostrarListaVaciaDeDocumentosDeColportorAlAsociado() throws Exception {
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
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_LISTA)
                .param("temporada.id", temporada.getId().toString())
                .sessionAttr("colportorTmp", colportor))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_LISTA+".jsp"));
    }

     
    @Test
    public void debieraMostrarListaDeDocumentosDeColportorAlAsociado() throws Exception {
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
        
        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }

        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_LISTA)
                .param("temporada.id", temporada.getId().toString())
                .sessionAttr("colportorTmp", colportor))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_LISTA+".jsp"))
                .andExpect(model().attributeExists(Constantes.DOCUMENTOCOLPORTOR_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

     
    @Test
    public void deberiaMostrarDocumentosDelaClaveDelColportorAlAsociado() throws Exception {
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
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_LISTA)
                .param("temporada.id", temporada.getId().toString())
                .param("clave", "5678")
                .sessionAttr("colportorTmp", colportor))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_LISTA+".jsp"))
                .andExpect(model().attributeExists(Constantes.DOCUMENTOCOLPORTOR_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
        
        
    }

/*     
    @Test
    public void deberiaMostrarDocumentosDelaClaveDeVariosColportoresAlAsociado() throws Exception {
        log.debug("Mostrando documentos de un colportor seleccionado por su clave");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(organizacion);
        currentSession().save(temporada);
        assertNotNull(temporada.getId());

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        Colportor colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        Documento documento = null;
        for (int i = 0; i < 8; i++) {
            documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }
        
        colportorTmp = new Colportor("test2@test.com", "test2", "test2", "test2",
                "test", "5678", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor(colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
       
        for (int i = 0; i < 5; i++) {
            documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }
        
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO)
                .param("clave", "5678"))
                .andExpect(request().sessionAttribute("temporadaColportorTmp", temporadaColportor))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_DOCUMENTOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
        log.debug("termino Primera llamada");


        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO)
                .param("clave", "1234"))
                .andExpect(request().sessionAttribute("temporadaColportorTmp", temporadaColportor))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_DOCUMENTOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
        log.debug("termino Segunda llamada");

    }
*/
     
    @Test
    public void debieraMostrarDocumento() throws Exception {
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

        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento);
        }
        
        authenticate(colportor, colportor.getPassword(), new ArrayList <GrantedAuthority> (colportor.getRoles()));
        
        this.mockMvc.perform(get(Constantes.DOCUMENTOCOLPORTOR_LISTA)
                .param("temporada.id", temporada.getId().toString()))
                .andExpect(model().attributeExists(Constantes.DOCUMENTOCOLPORTOR))
                .andExpect(forwardedUrl("/WEB-INF/jsp/"+Constantes.DOCUMENTOCOLPORTOR_VER+".jsp"));
    }

/*     
    @Test
    public void debieraCrearDocumento() throws Exception {
        log.debug("Debiera crear documento");

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());       

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(organizacion);
        currentSession().save(temporada);
        assertNotNull(temporada.getId());

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        Usuario colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        this.authenticate(colportorTmp, colportorTmp.getPassword(), new ArrayList(asociado.getAuthorities()));
        
        temporadaColportor = new TemporadaColportor((Colportor)colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_CREA).
                param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO).
                param("folio", Constantes.FOLIO).
                param("importe", "0.0").
                param("fecha", "05/05/2010").
                param("observaciones", Constantes.OBSERVACIONES)
                .sessionAttr("colportorTmp", colportorTmp))
                .andExpect(request().sessionAttribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
                .andExpect(status().isOk()).andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"));
                //.andExpect(redirectedUrl(Constantes.PATH_DOCUMENTO_VER));
    }

     
    @Test
    public void debieraActualizarDocumento() throws Exception {
        log.debug("Debiera actualizar documento");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());       

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(organizacion);
        currentSession().save(temporada);
        assertNotNull(temporada.getId());

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        Usuario colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        this.authenticate(colportorTmp, colportorTmp.getPassword(), new ArrayList(asociado.getAuthorities()));
        
        temporadaColportor = new TemporadaColportor((Colportor)colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_CREA).
                param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO).
                param("folio", Constantes.FOLIO).
                param("importe", "0.0").
                param("fecha", "05/05/2010").
                param("observaciones", Constantes.OBSERVACIONES)
                .sessionAttr("colportorTmp", colportorTmp))
                .andExpect(request().sessionAttribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
                .andExpect(status().isOk()).andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"))
                .andDo(print());
                //.andExpect(redirectedUrl(Constantes.PATH_DOCUMENTO_VER + "/1"));
        
//        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_ACTUALIZA).
//                param("id", "1").
//                param("version", "0").
//                param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO).
//                param("folio", Constantes.FOLIO).
//                param("importe", "0.0").
//                param("fecha", "05/05/2010").
//                param("observaciones", Constantes.OBSERVACIONES)
//                .sessionAttr("colportorTmp", colportorTmp))
//                //.andExpect(request().sessionAttribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
//                .andExpect(status().isOk()).andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.actualizado.message"))
//                .andDo(print());
                //.andExpect(redirectedUrl(Constantes.PATH_DOCUMENTO_VER + "/1"));

    }

     
    @Test
    public void debieraEliminarDocumento() throws Exception {
        log.debug("Debiera eliminar documento");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        
        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        
        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(organizacion);
        currentSession().save(temporada);
        assertNotNull(temporada.getId());

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        Usuario colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());
        
        temporadaColportor = new TemporadaColportor((Colportor)colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        Documento documento = null;
        for (int i = 0; i < 1; i++) {
            documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documento.setTemporadaColportor(temporadaColportor);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }

        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_ELIMINA)
                .param("id", documento.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.eliminado.message"));
    }
    
     
    @Test
    public void deberiaProbarTablaDeTotales() throws Exception{
        log.debug("Deberia probar tabla de resultados de acuerdo a los documentos creados");
        
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());

        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());

        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());

        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());

        Set<Rol> roles = new HashSet<>();
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        roles.add(rol);
        rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        roles.add(rol);

        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());

        Usuario asociado = new Asociado("test22@test.com", "test", "test", "test", "test",
                "1", Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        asociado.setRoles(roles);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());

        this.authenticate(asociado, asociado.getPassword(), new ArrayList(asociado.getAuthorities()));

        TemporadaColportor temporadaColportor = null;
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(organizacion);
        currentSession().save(temporada);
        assertNotNull(temporada.getId());

        ColegioColportor colegio = new ColegioColportor("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);

        Usuario colportorTmp = new Colportor("test@test.com", "test", "test", "test",
                "test", "1234", Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                "10706", "test", "test001", new Date());
        colportorTmp.setEmpresa(empresa);
        colportorTmp.setAlmacen(almacen);
        colportorTmp.setRoles(roles);
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);
        assertNotNull(colportorTmp.getId());

        temporadaColportor = new TemporadaColportor((Colportor) colportorTmp, asociacion, asociado, temporada, union, colegio);
        temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
        temporadaColportor.setObjetivo("11250");
        temporadaColportor.setObservaciones("Observaciones");
        temporadaColportor.setFecha(new Date());
        temporadaColportorDao.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

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
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA)
                .sessionAttr("colportorTmp", colportorTmp)) //                .andExpect(request().sessionAttribute("temporadaColportorPrueba", temporadaColportor.getId().toString()))
                .andExpect(model().attribute(Constantes.FIDELIDAD, new BigDecimal("100.000000").setScale(2, BigDecimal.ROUND_HALF_EVEN)))
                .andExpect(model().attribute(Constantes.ALCANZADO, new BigDecimal("88.888888").setScale(2, BigDecimal.ROUND_HALF_EVEN)))
                .andExpect(view().name(Constantes.PATH_DOCUMENTO_LISTA))
                .andExpect(status().isOk());
    }*/
}
