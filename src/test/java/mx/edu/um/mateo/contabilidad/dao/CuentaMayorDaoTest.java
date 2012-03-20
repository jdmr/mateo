package mx.edu.um.mateo.contabilidad.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
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
public class CuentaMayorDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CuentaMayorDaoTest.class);
    @Autowired
    private CuentaMayorDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class CuentaMayorDao.
     */
    @Test
    public void deberiaMostrarListaDeCuentaMayor() {
        log.debug("Debiera mostrar lista de cuentaMayor");

        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        for (int i = 0; i < 20; i++) {
            CuentaMayor cuentaMayor = new CuentaMayor("test" + i, "test", "test", false, false, false, false, BigDecimal.ZERO);
            cuentaMayor.setOrganizacion(test);
            currentSession().save(cuentaMayor);
            assertNotNull(cuentaMayor);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_MAYORES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<CuentaMayor>) result.get(Constantes.CONTAINSKEY_MAYORES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerCuentaMayor() {
        log.debug("Debiera obtener cuentaMayor");

        String nombre = "test";
        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaMayor cuentaMayor = new CuentaMayor("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaMayor.setOrganizacion(test);
        currentSession().save(cuentaMayor);
        assertNotNull(cuentaMayor.getId());
        Long id = cuentaMayor.getId();

        CuentaMayor result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, cuentaMayor);
    }

    @Test
    public void deberiaCrearCuentaMayor() {
        log.debug("Deberia crear CuentaMayor");

        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaMayor cuentaMayor = new CuentaMayor("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaMayor.setOrganizacion(test);
        assertNotNull(cuentaMayor);

        CuentaMayor cuentaMayor2 = instance.crea(cuentaMayor);
        assertNotNull(cuentaMayor2);
        assertNotNull(cuentaMayor2.getId());

        assertEquals(cuentaMayor, cuentaMayor2);
    }

    @Test
    public void deberiaActualizarCuentaMayor() {
        log.debug("Deberia actualizar CuentaMayor");

        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaMayor cuentaMayor = new CuentaMayor("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaMayor.setOrganizacion(test);
        assertNotNull(cuentaMayor);
        currentSession().save(cuentaMayor);

        String nombre = "test1";
        cuentaMayor.setNombre(nombre);

        CuentaMayor cuentaMayor2 = instance.actualiza(cuentaMayor);
        assertNotNull(cuentaMayor2);
        assertEquals(nombre, cuentaMayor.getNombre());

        assertEquals(cuentaMayor, cuentaMayor2);
    }

    @Test
    public void deberiaEliminarCuentaMayor() throws UltimoException {
        log.debug("Debiera eliminar CuentaMayor");

        String nom = "test";
        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaMayor cuentaMayor = new CuentaMayor("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaMayor.setOrganizacion(test);
        currentSession().save(cuentaMayor);
        assertNotNull(cuentaMayor);

        String nombre = instance.elimina(cuentaMayor.getId());
        assertEquals(nom, nombre);

        CuentaMayor prueba = instance.obtiene(cuentaMayor.getId());
        assertNull(prueba);
    }
}
