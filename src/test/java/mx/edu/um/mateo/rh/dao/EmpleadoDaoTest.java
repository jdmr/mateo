/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import mx.edu.um.mateo.rh.model.Empleado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import mx.edu.um.mateo.general.model.Empresa;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class EmpleadoDaoTest {

    private static final Logger log = LoggerFactory.getLogger(EmpleadoDaoTest.class);
    @Autowired
    private EmpleadoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public EmpleadoDaoTest() {
    }

    /**
     * Test of obtiene method, of class EmpleadoDao.
     */
    @Test
    public void testObtiene() {
        System.out.println("obtiene");
        Long id = 1L;
        List<Empleado> lista = insertaEmpleados(1);

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get("empleado"));
        assertNotNull(result.get("cantidad"));

        assertEquals(1, ((List<Empresa>) result.get("empleado")).size());
        assertEquals(1, ((Long) result.get("cantidad")).intValue());

    }

    /**
     * Test of crea method, of class EmpleadoDao.
     */
    @Test
    public void testCrea() {
        System.out.println("crea");
        List<Empleado> lista = insertaEmpleados(1);
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get("empleado"));
        assertNotNull(result.get("cantidad"));

        assertEquals(1, ((List<Empresa>) result.get("empleado")).size());
        assertEquals(1, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of crea method, of class EmpleadoDao.
     */
    @Test
    public void testActualiza() {
        log.debug("Deberia actualizar empleado");
        List<Empleado> lista = insertaEmpleados(1);
        log.debug("lista"+lista.size());
        Empleado empleado = lista.get(0);
        assertNotNull(empleado);
        empleado.setNombre("Juan1");

        empleado = instance.actualiza(empleado);
        log.debug("empleado >>" + empleado);
        assertEquals("Juan1", empleado.getNombre());
    }

    private List<Empleado> insertaEmpleados(Integer i) {
        log.debug("Insertar empleado");
        List<Empleado> lista = new ArrayList();
        Empleado empleado = null;
        for (int j = 0; j < i; j++) {
            empleado = new Empleado();
            empleado.setNombre("Juan" + j);
            empleado.setApPaterno("Perez" + j);
            empleado.setApMaterno("Dominguez" + j);
            empleado.setClave("980001" + j);
            empleado.setDireccion("Libertad 1300 pte");
            empleado.setGenero("M");
            empleado.setStatus("A");
            currentSession().save(empleado);
            log.debug(empleado.toString());
            lista.add(empleado);
        }
        return lista;
    }
}
