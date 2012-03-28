package mx.edu.um.mateo.contabilidad.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.model.CuentaResultado;
import mx.edu.um.mateo.general.model.Organizacion;
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
 * @author nujev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class CuentaResultadoDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CuentaResultadoDaoTest.class);
    @Autowired
    private CuentaResultadoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class CuentaResultadoDao.
     */
    @Test
    public void deberiaMostrarListaDeCuentaResultado() {
        log.debug("Debiera mostrar lista de cuentaResultado");

        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        for (int i = 0; i < 20; i++) {
            CuentaResultado cuentaResultado = new CuentaResultado("test" + i, "test", "test", false, false, false, false, BigDecimal.ZERO);
            cuentaResultado.setOrganizacion(test);
            currentSession().save(cuentaResultado);
            assertNotNull(cuentaResultado);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_RESULTADOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<CuentaResultado>) result.get(Constantes.CONTAINSKEY_RESULTADOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerCuentaResultado() {
        log.debug("Debiera obtener cuentaResultado");

        String nombre = "test";
        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaResultado cuentaResultado = new CuentaResultado("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaResultado.setOrganizacion(test);
        currentSession().save(cuentaResultado);
        assertNotNull(cuentaResultado.getId());
        Long id = cuentaResultado.getId();

        CuentaResultado result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, cuentaResultado);
    }

    @Test
    public void deberiaCrearCuentaResultado() {
        log.debug("Deberia crear CuentaResultado");

        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaResultado cuentaResultado = new CuentaResultado("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaResultado.setOrganizacion(test);
        assertNotNull(cuentaResultado);

        CuentaResultado cuentaResultado2 = instance.crea(cuentaResultado);
        assertNotNull(cuentaResultado2);
        assertNotNull(cuentaResultado2.getId());

        assertEquals(cuentaResultado, cuentaResultado2);
    }

    @Test
    public void deberiaActualizarCuentaResultado() {
        log.debug("Deberia actualizar CuentaResultado");

        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaResultado cuentaResultado = new CuentaResultado("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaResultado.setOrganizacion(test);
        assertNotNull(cuentaResultado);
        currentSession().save(cuentaResultado);

        String nombre = "test1";
        cuentaResultado.setNombre(nombre);

        CuentaResultado cuentaResultado2 = instance.actualiza(cuentaResultado);
        assertNotNull(cuentaResultado2);
        assertEquals(nombre, cuentaResultado.getNombre());

        assertEquals(cuentaResultado, cuentaResultado2);
    }

    @Test
    public void deberiaEliminarCuentaResultado() throws UltimoException {
        log.debug("Debiera eliminar CuentaResultado");

        String nom = "test";
        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaResultado cuentaResultado = new CuentaResultado("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaResultado.setOrganizacion(test);
        currentSession().save(cuentaResultado);
        assertNotNull(cuentaResultado);

        String nombre = instance.elimina(cuentaResultado.getId());
        assertEquals(nom, nombre);

        CuentaResultado prueba = instance.obtiene(cuentaResultado.getId());
        assertNull(prueba);
    }
}
