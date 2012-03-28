/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.model.CuentaAuxiliar;
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
public class CuentaAuxiliarDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CuentaAuxiliarDaoTest.class);
    @Autowired
    private CuentaAuxiliarDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class CuentaAuxiliarDao.
     */
    @Test
    public void deberiaMostrarListaDeCuentaAuxiliar() {
        log.debug("Debiera mostrar lista de cuentaAuxiliar");

        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        for (int i = 0; i < 20; i++) {
            CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar("test" + i, "test", "test", false, false, false, false, BigDecimal.ZERO);
            cuentaAuxiliar.setOrganizacion(test);
            currentSession().save(cuentaAuxiliar);
            assertNotNull(cuentaAuxiliar);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_AUXILIARES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<CuentaAuxiliar>) result.get(Constantes.CONTAINSKEY_AUXILIARES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerCuentaAuxiliar() {
        log.debug("Debiera obtener cuentaAuxiliar");

        String nombre = "test";
        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaAuxiliar.setOrganizacion(test);
        currentSession().save(cuentaAuxiliar);
        assertNotNull(cuentaAuxiliar.getId());
        Long id = cuentaAuxiliar.getId();

        CuentaAuxiliar result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, cuentaAuxiliar);
    }

    @Test
    public void deberiaCrearCuentaAuxiliar() {
        log.debug("Deberia crear CuentaAuxiliar");

        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaAuxiliar.setOrganizacion(test);
        assertNotNull(cuentaAuxiliar);

        CuentaAuxiliar cuentaAuxiliar2 = instance.crea(cuentaAuxiliar);
        assertNotNull(cuentaAuxiliar2);
        assertNotNull(cuentaAuxiliar2.getId());

        assertEquals(cuentaAuxiliar, cuentaAuxiliar2);
    }

    @Test
    public void deberiaActualizarCuentaAuxiliar() {
        log.debug("Deberia actualizar CuentaAuxiliar");

        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaAuxiliar.setOrganizacion(test);
        assertNotNull(cuentaAuxiliar);
        currentSession().save(cuentaAuxiliar);

        String nombre = "test1";
        cuentaAuxiliar.setNombre(nombre);

        CuentaAuxiliar cuentaAuxiliar2 = instance.actualiza(cuentaAuxiliar);
        assertNotNull(cuentaAuxiliar2);
        assertEquals(nombre, cuentaAuxiliar.getNombre());

        assertEquals(cuentaAuxiliar, cuentaAuxiliar2);
    }

    @Test
    public void deberiaEliminarCuentaAuxiliar() throws UltimoException {
        log.debug("Debiera eliminar CuentaAuxiliar");

        String nom = "test";
        Organizacion test = new Organizacion("TST-01", "TEST--01", "TEST--01");
        currentSession().save(test);
        CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar("test", "test", "test", false, false, false, false, BigDecimal.ZERO);
        cuentaAuxiliar.setOrganizacion(test);
        currentSession().save(cuentaAuxiliar);
        assertNotNull(cuentaAuxiliar);

        String nombre = instance.elimina(cuentaAuxiliar.getId());
        assertEquals(nom, nombre);

        CuentaAuxiliar prueba = instance.obtiene(cuentaAuxiliar.getId());
        assertNull(prueba);
    }
}
