/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.colportor.model.ColegioColportor;
import mx.edu.um.mateo.colportor.test.BaseTest;
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
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ColegioColportorDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ColegioColportorDaoTest.class);
    @Autowired
    private ColegioColportorDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class ColegioColportorDao.
     */
    @Test
    //PRUEBA PASO 100%
    public void deberiaMostrarListaDeColegioColportor() {
        log.debug("Debiera mostrar lista de colegio");

        for (int i = 0; i < 20; i++) {
            ColegioColportor colegio = new ColegioColportor(Constantes.NOMBRE + i, Constantes.STATUS_ACTIVO);
            currentSession().save(colegio);
            assertNotNull(colegio);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_COLEGIOS_COLPORTOR));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<ColegioColportor>) result.get(Constantes.CONTAINSKEY_COLEGIOS_COLPORTOR)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraObtenerColegioColportor() {
        log.debug("Debiera obtener colegio");

        String nombre = "test";
        ColegioColportor colegio = new ColegioColportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        assertNotNull(colegio.getId());
        Long id = colegio.getId();

        ColegioColportor result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, colegio);
    }

    @Test
    //PRUEBA PASO 100%
    public void deberiaCrearColegioColportor() {
        log.debug("Deberia crear ColegioColportor");

        ColegioColportor colegio = new ColegioColportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        assertNotNull(colegio);

        ColegioColportor colegio2 = instance.crea(colegio);
        assertNotNull(colegio2);
        assertNotNull(colegio2.getId());

        assertEquals(colegio, colegio2);
    }

    @Test
    //PRUEBA PASO 100%
    public void deberiaActualizarColegioColportor() {
        log.debug("Deberia actualizar ColegioColportor");

        ColegioColportor colegio = new ColegioColportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        assertNotNull(colegio);

        String nombre = "test1";
        colegio.setNombre(nombre);

        ColegioColportor colegio2 = instance.actualiza(colegio);
        assertNotNull(colegio2);
        assertEquals(nombre, colegio.getNombre());

        assertEquals(colegio, colegio2);
    }

    @Test
    //PRUEBA PASO 100%
    public void deberiaEliminarColegioColportor() throws UltimoException {
        log.debug("Debiera eliminar ColegioColportor");

        String nom = "test";
        ColegioColportor colegio = new ColegioColportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        assertNotNull(colegio);

        String nombre = instance.elimina(colegio.getId());
        assertEquals(nom, nombre);

        ColegioColportor prueba = instance.obtiene(colegio.getId());
        if (prueba != null) {
            fail("Fallo prueba Eliminar");
        }
    }
}