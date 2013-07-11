/*
 * TODO problemas con el constructor
 * 
 */
package mx.edu.um.mateo.colportor.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Documento;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.model.Colegio;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 *
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class DocumentoDaoTest extends BaseDaoTest {
    public static final String TIPO_DOCUMENTO = "A";
    public static final String FOLIO = "test";
    public static final BigDecimal IMPORTE = new BigDecimal("0.0");
    public static final String OBSERVACIONES = "test teste";

    @Autowired
    private DocumentoDao instance;
    
    @Test
    public void deberiaMostrarListaDeDocumento() {
        log.debug("Debiera mostrar lista de documento");

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
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento);
        }

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("temporadaColportor", temporadaColportor.getId());
        Map result = instance.lista(params);
        
        assertNotNull(result.get(Constantes.DOCUMENTOCOLPORTOR_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Documento>) result.get(Constantes.DOCUMENTOCOLPORTOR_LIST)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void deberiaTraerListaVacia() {
        log.debug("Debiera traer lista Vacia");

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
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                    temporadaColportor);
            currentSession().save(documento);
            assertNotNull(documento);
        }

        Map<String, Object> params = new TreeMap<String, Object>();
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.DOCUMENTOCOLPORTOR_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(0, ((List<Documento>) result.get(Constantes.DOCUMENTOCOLPORTOR_LIST)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerDocumento() {
        log.debug("Debiera obtener documento");

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
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                temporadaColportor);
        currentSession().save(documento);
        assertNotNull(documento);
        Long id = documento.getId();

        Documento result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(FOLIO, result.getFolio());

        assertEquals(result, documento);
    }

    @Test
    public void deberiaCrearDocumento() {
        log.debug("Deberia crear Documento");

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
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                temporadaColportor);
        currentSession().save(documento);
        assertNotNull(documento.getId());

    }

    @Test
    public void deberiaActualizarDocumento() {
        log.debug("Deberia actualizar Documento");

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
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                temporadaColportor);
        currentSession().save(documento);
        assertNotNull(documento.getId());

        String folio = "test1";
        documento.setFolio(folio);

        Documento documento2 = instance.actualiza(documento);
        assertNotNull(documento2);
        assertEquals(folio, documento.getFolio());

        assertEquals(documento, documento2);
    }

    @Test
    public void deberiaEliminaralDocumento() throws UltimoException {
        log.debug("Debiera eliminar Documento");
        
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
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());

        Documento documento = new Documento(TIPO_DOCUMENTO, FOLIO, new Date(), IMPORTE, OBSERVACIONES, 
                temporadaColportor);
        currentSession().save(documento);
        assertNotNull(documento.getId());
        
        String folio = instance.elimina(documento.getId());

        Documento prueba = instance.obtiene(documento.getId());
        if (prueba != null) {
            fail("Fallo prueba Eliminar");
        }
    }
}
