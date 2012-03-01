/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.model.CuentaAuxiliar;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.utils.UltimoException;
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
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class CuentaAuxiliarDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CuentaAuxiliarDaoTest.class);
    @Autowired
    private CuentaAuxiliarDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Test
    public void deberiaMostrarListaDeCtaAuxiliar() {
        log.debug("Debiera mostrar lista de ctaAuxiliar");
        for (int i = 0; i < 20; i++) {
            CuentaAuxiliar ctaAuxiliar = new CuentaAuxiliar("test" + i, "test");
            currentSession().save(ctaAuxiliar);
            assertNotNull(ctaAuxiliar);
            log.debug("ctaAuxiliar>>" + ctaAuxiliar);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get("auxiliares"));
        assertNotNull(result.get("cantidad"));

        assertEquals(10, ((List<CuentaAuxiliar>) result.get("auxiliares")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    @Test
    public void debieraObtenerCtaAuxiliar() {
        log.debug("Debiera obtener ctaAuxiliar");
        CuentaAuxiliar ctaAuxiliar = new CuentaAuxiliar("test", "test");
        currentSession().save(ctaAuxiliar);
        assertNotNull(ctaAuxiliar.getId());
        Long id = ctaAuxiliar.getId();

        CuentaAuxiliar result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals("test", result.getNombre());
    }

    @Test
    public void deberiaCrearCtaAuxiliar() {
        log.debug("Deberia crear CtaAuxiliar");
        CuentaAuxiliar ctaAuxiliar = new CuentaAuxiliar("test", "test");
        assertNotNull(ctaAuxiliar);
        log.debug("ctaAuxiliar >> " + ctaAuxiliar);
        ctaAuxiliar = instance.crea(ctaAuxiliar);
        assertNotNull(ctaAuxiliar.getId());
    }

    @Test
    public void deberiaActualizarCtaAuxiliar() {
        log.debug("Deberia actualizar CtaAuxiliar");
        CuentaAuxiliar ctaAuxiliar = new CuentaAuxiliar("test", "test");
        assertNotNull(ctaAuxiliar);
        currentSession().save(ctaAuxiliar);

        ctaAuxiliar.setNombre("test1");

        ctaAuxiliar = instance.actualiza(ctaAuxiliar);
        log.debug("ctaAuxiliar >>" + ctaAuxiliar);
        assertEquals("test1", ctaAuxiliar.getNombre());
    }

    @Test
    public void deberiaEliminarCtaAuxiliar() throws UltimoException {
        log.debug("Debiera eliminar Ejercicio");

        CuentaAuxiliar ctaAuxiliar = new CuentaAuxiliar("test", "A");
        currentSession().save(ctaAuxiliar);
        assertNotNull(ctaAuxiliar);
        String nombre = instance.elimina(ctaAuxiliar.getId());
        assertEquals("test", nombre);

        CuentaAuxiliar prueba = instance.obtiene(ctaAuxiliar.getId());
        assertNull(prueba);
    }
}
