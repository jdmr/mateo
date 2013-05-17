/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TemporadaDaoTest extends BaseDaoTest{

    private static final Logger log = LoggerFactory.getLogger(TemporadaDao.class);
    @Autowired
    private TemporadaDao instance;
    
    /**
     * Test of lista method, of class TemporaDao.
     */
    @Test
    public void testLista() {
        log.debug("Debiera mostrar lista Temporada"); 
        Usuario asociado = this.obtieneAsociado();
        for (int i = 0; i < 20; i++) {
            Temporada temporada = new Temporada("test" + i);
            temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
            currentSession().save(temporada);
            assertNotNull(temporada.getId());
        }
        Map<String, Object> params = new TreeMap<>();
        params.put("organizacion", asociado.getEmpresa().getOrganizacion());
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.TEMPORADA_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Temporada>) result.get(Constantes.TEMPORADA_LIST)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void testObtener() {
        log.debug("Debiera obtener Temporada");
        
        Usuario asociado = this.obtieneAsociado();

        String nombre = "test";
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        Long id = temporada.getId();

        Temporada result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, temporada);
    }

    @Test
    public void testCrear() {
        log.debug("Deberia crear Temporada");

        Usuario asociado = this.obtieneAsociado();
        
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        temporada.setStatus(Constantes.STATUS_ACTIVO);
        instance.crea(temporada);
        assertNotNull(temporada.getId());
    }

    @Test
    public void testActualiza() {
        log.debug("Deberia actualizar Temporada");
        
        Usuario asociado = this.obtieneAsociado();
        
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());

        assertNotNull(temporada);
        currentSession().save(temporada);

        String nombre = "test1";
        temporada.setNombre(nombre);

        Temporada temporada2 = instance.actualiza(temporada);
        assertNotNull(temporada2);
        assertEquals(nombre, temporada.getNombre());

        assertEquals(temporada, temporada2);
    }

    @Test
    public void deberiaEliminarTemporada() throws UltimoException {
        log.debug("Debiera eliminar Temporada");

        Usuario asociado = this.obtieneAsociado();
        
        String nom = "test";
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(temporada);
        assertNotNull(temporada);

        String nombre = instance.elimina(temporada.getId());
        assertEquals(nom, nombre);

        Temporada prueba = instance.obtiene(temporada.getId());

        if (prueba != null) {
            fail("Fallo la prueba Eliminar");

        }
    }
}
