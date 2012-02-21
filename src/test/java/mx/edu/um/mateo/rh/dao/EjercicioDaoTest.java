/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.rh.model.Ejercicio;
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
 * @author nujev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class EjercicioDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(EjercicioDaoTest.class);
    @Autowired
    private EjercicioDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class EjercicioDao.
     */
    @Test
    public void deberiaMostrarListaDeEjercicio() {
        log.debug("Debiera mostrar lista de Ejercicio");
        for (int i = 0; i < 20; i++) {
            Ejercicio ejercicio = new Ejercicio("test" + i, Constantes.STATUS_ACTIVO);
            currentSession().save(ejercicio);
            assertNotNull(ejercicio);
            log.debug("Ejercicio >>" + ejercicio);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get("ejercicio"));
        assertNotNull(result.get("cantidad"));

        assertEquals(10, ((List<Ejercicio>) result.get("ejercicio")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    @Test
    public void deberiaObtenerListaDeEjercicio() {
        log.debug("Debiera obtener Lista de Ejercicios");
        Ejercicio ejercicio = new Ejercicio("test", Constantes.STATUS_ACTIVO);
        currentSession().save(ejercicio);
        assertNotNull(ejercicio);
        Long id = ejercicio.getId();

        Ejercicio result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals("test", result.getNombre());
    }

    @Test
    public void deberiaCrearEjercicio() {
        log.debug("Deberia crear un Ejercicio");
        Ejercicio ejercicio = new Ejercicio("test", Constantes.STATUS_ACTIVO);
        assertNotNull(ejercicio);
        log.debug("ejercicio >> " + ejercicio);
        ejercicio = instance.crea(ejercicio);
        assertNotNull(ejercicio.getId());
    }
    
    @Test
    public void deberiaActualizarEjercicio() {
        log.debug("Deberia actualizar CtaMayor");
        Ejercicio ejercicio = new Ejercicio("test", Constantes.STATUS_ACTIVO);
        assertNotNull(ejercicio);
        currentSession().save(ejercicio);

        ejercicio.setNombre("test1");
        
        ejercicio = instance.actualiza(ejercicio);
        log.debug("ctaMayor >>" + ejercicio);
        assertEquals("test1", ejercicio.getNombre());
    }

    @Test
    public void deberiaEliminarEjercicio() throws UltimoException {
        log.debug("Debiera eliminar Ejercicio");

        Ejercicio ejercicio = new Ejercicio("test", Constantes.STATUS_ACTIVO);
        currentSession().save(ejercicio);
        assertNotNull(ejercicio);
        String nombre = instance.elimina(ejercicio.getId());
        assertEquals("test", nombre);

        Ejercicio prueba = instance.obtiene(ejercicio.getId());
        assertNull(prueba);



    }
}
