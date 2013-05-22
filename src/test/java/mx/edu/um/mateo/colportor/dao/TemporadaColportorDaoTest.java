/*
 * TODO problemas con el constructor 
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.model.Colegio;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class TemporadaColportorDaoTest extends BaseDaoTest {

    @Autowired
    private TemporadaColportorDao instance;
    
    @Test
    public void listaPorEmpresa() {
        log.debug("Debiera mostrar lista Campanias por empresa");
        
        Usuario colportor = obtieneColportor();
        Usuario asociado = obtieneAsociado(colportor);
                
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Colegio colegio = new Colegio("UM", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        TemporadaColportor temporadacolportor = null;
        for (int i = 0; i < 20; i++) {
            temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "TEST", "TEST"+i);
            temporadacolportor.setColportor((Colportor) colportor);
            temporadacolportor.setAsociado((Asociado) asociado);
            temporadacolportor.setTemporada(temporada);
            temporadacolportor.setColegio(colegio);
            currentSession().save(temporadacolportor);
            assertNotNull(temporadacolportor.getId());
        }
        
        Map<String, Object> params = new TreeMap();
        params.put("empresa", asociado.getEmpresa().getId());
        Map result = instance.lista(params);
        
        assertNotNull(result.get(Constantes.TEMPORADACOLPORTOR_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TemporadaColportor>) result.get(Constantes.TEMPORADACOLPORTOR_LIST)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }
    
    @Test
    public void listaPorAsociado() {
        log.debug("Debiera mostrar lista Campanias por Asociado");
        
        Usuario colportor = obtieneColportor();
        Usuario asociado = obtieneAsociado(colportor);
                
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        
        Colegio colegio = new Colegio("UM", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        
        TemporadaColportor temporadacolportor = null;
        for (int i = 0; i < 20; i++) {
            temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "TEST", "TEST"+i);
            temporadacolportor.setColportor((Colportor) colportor);
            temporadacolportor.setAsociado((Asociado) asociado);
            temporadacolportor.setTemporada(temporada);
            temporadacolportor.setColegio(colegio);
            currentSession().save(temporadacolportor);
            assertNotNull(temporadacolportor.getId());
        }
        
        Map<String, Object> params = new TreeMap();
        params.put("asociado", asociado.getId());
        Map result = instance.lista(params);
        
        assertNotNull(result.get(Constantes.TEMPORADACOLPORTOR_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TemporadaColportor>) result.get(Constantes.TEMPORADACOLPORTOR_LIST)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void obtenerPorId() {
        log.debug("Debiera obtener Campania por Id");
        
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
        
        Long id = temporadaColportor.getId();
        TemporadaColportor result = instance.obtiene(id);
        assertNotNull(result.getId());
        
        assertEquals("TEST", result.getObservaciones());
    }

    @Test
    public void obtenerPorColportor() {
        log.debug("Debiera obtener una Campania por Colportor");
        
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
        
        TemporadaColportor result = instance.obtiene((Colportor) colportor);
        assertNotNull(result);
        assertNotNull(result.getId());

        assertEquals("TEST", result.getObservaciones());
    }

   @Test
    public void obtenerPorColportorAndTemporada() {
        log.debug("Debiera obtener Temporada Colportor por Colportor");
        
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
        currentSession().save(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
                
        TemporadaColportor temporadaColportor2 = new TemporadaColportor(Constantes.STATUS_ACTIVO, "TEST", "TEST");
        temporadaColportor2.setColportor((Colportor) colportor);
        temporadaColportor2.setAsociado((Asociado) asociado);
        temporadaColportor2.setTemporada(temporada2);
        temporadaColportor2.setColegio(colegio);
        currentSession().save(temporadaColportor2);
        assertNotNull(temporadaColportor2.getId());
        
        TemporadaColportor result = instance.obtiene((Colportor)colportor, temporada2);
        assertNotNull(result);
        assertEquals(result, temporadaColportor2);
    }
   
   @Test
    public void crear() {
        log.debug("Deberia crear Temporada Colportor");
        
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
        instance.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        TemporadaColportor result = instance.obtiene((Colportor) colportor);
        assertNotNull(result);
        assertNotNull(result.getId());

        assertEquals(temporadaColportor, result);
    }

    @Test
    public void actualiza() {
        log.debug("Deberia actualizar Temporada Colportor");
        
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
        instance.crea(temporadaColportor);
        assertNotNull(temporadaColportor.getId());
        
        temporadaColportor.setObservaciones("modificado");
        instance.actualiza(temporadaColportor);
        
        TemporadaColportor temporadaClp = instance.obtiene(temporadaColportor.getId());
        assertNotNull(temporadaClp);
        
        assertEquals(temporadaColportor, temporadaClp);
        
    }

    @Test
    public void elimina() throws UltimoException {
        log.debug("Debiera eliminar Temporada Colportor");
        
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

        String nombre2 = instance.elimina(temporadaColportor.getId());
        assertEquals(nombre2, temporadaColportor.getColportor().getClave());

        TemporadaColportor prueba = instance.obtiene(temporadaColportor.getId());
        if (!prueba.getStatus().equals(Constantes.STATUS_INACTIVO)) {
            fail("Fallo la prueba Eliminar");
        }
    }
}
