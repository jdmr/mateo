/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.contabilidad.model.CuentaResultado;
import mx.edu.um.mateo.general.utils.UltimoException;
import org.slf4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional

public class CuentaResultadoDaoTest extends BaseTest{
    
    private static final Logger log = LoggerFactory.getLogger(CuentaResultadoDaoTest.class);
    @Autowired
    private CuentaResultadoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * Test of obtiene method, of class CuentaResultadoDao.
     */
    @Test
    public void deberiaMostrarListaDeCuentaResultado() {
        log.debug("Debiera mostrar lista de cuentaResultado");
        for (int i = 0; i < 20; i++) {
            CuentaResultado cuentaResultado = new CuentaResultado("test" + i, "test");
            currentSession().save(cuentaResultado);
            assertNotNull(cuentaResultado);
            log.debug("cuentaResultado>>" + cuentaResultado);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params); 
        assertNotNull(result.get(Constantes.CONTAINSKEY_RESULTADOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Empresa>) result.get(Constantes.CONTAINSKEY_RESULTADOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }
    
    @Test
    public void debieraObtenerCuentaResultado() {
        log.debug("Debiera obtener cuentaResultado");

        String nombre = "test";
        CuentaResultado cuentaResultado = new CuentaResultado("test", "test");
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

        CuentaResultado cuentaResultado = new CuentaResultado("test", "test");
        assertNotNull(cuentaResultado);

        CuentaResultado cuentaResultado2 = instance.crea(cuentaResultado);
        assertNotNull(cuentaResultado2);
        assertNotNull(cuentaResultado2.getId());

        assertEquals(cuentaResultado, cuentaResultado2);
    }

    @Test
    public void deberiaActualizarCuentaResultado() {
        log.debug("Deberia actualizar CuentaResultado");

        CuentaResultado cuentaResultado = new CuentaResultado("test", "test");
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
        CuentaResultado cuentaResultado = new CuentaResultado("test", "test");
        currentSession().save(cuentaResultado);
        assertNotNull(cuentaResultado);

        String nombre = instance.elimina(cuentaResultado.getId());
        assertEquals(nom, nombre);

        CuentaResultado prueba = instance.obtiene(cuentaResultado.getId());
        assertNull(prueba);
    }
}
