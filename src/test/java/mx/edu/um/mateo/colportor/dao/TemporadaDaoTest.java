/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import mx.edu.um.mateo.colportor.dao.TemporadaDao;
import mx.edu.um.mateo.colportor.dao.UnionDao;
import mx.edu.um.mateo.colportor.dao.AsociacionDao;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
public class TemporadaDaoTest {

    private static final Logger log = LoggerFactory.getLogger(TemporadaDao.class);
    @Autowired
    private TemporadaDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    @Autowired
    private UnionDao unionDao;
    @Autowired
    private AsociacionDao asociacionDao;
    /**
     * Test of lista method, of class TemporaDao.
     */
    @Test
    public void debieraMostrarListaDeTePpmorada() {
        log.debug("Debiera mostrar lista Temporada");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        for (int i = 0; i < 20; i++) {
            Temporada temporada = new Temporada("test" + i);
            temporada.setAsociacion(asociacion);
            currentSession().save(temporada);
            assertNotNull(temporada.getId());
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_TEMPORADAS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Temporada>) result.get(Constantes.CONTAINSKEY_TEMPORADAS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerTemporada() {
        log.debug("Debiera obtener Temporada");
        
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        String nombre = "test";
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);

        currentSession().save(temporada);
        assertNotNull(temporada.getId());
        Long id = temporada.getId();

        Temporada result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, temporada);
    }

    @Test
    public void deberiaCrearTemporada() {
        log.debug("Deberia crear Temporada");

        Union union = new Union("test");
        union = unionDao.crea(union);
        currentSession().save(union);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        asociacionDao.crea(asociacion);
        
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        assertNotNull(temporada);

        Temporada temporada2 = instance.crea(temporada);
        assertNotNull(temporada2);
        assertNotNull(temporada2.getId());

        assertEquals(temporada, temporada2);
    }

    @Test
    public void deberiaActualizarTemporada() {
        log.debug("Deberia actualizar Temporada");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);

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

        Union union = new Union("test");
        union = unionDao.crea(union);
        currentSession().save(union);
        
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        asociacionDao.crea(asociacion);
        
        String nom = "test";
        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
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
