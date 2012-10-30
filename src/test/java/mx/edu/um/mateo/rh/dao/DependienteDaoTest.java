/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.rh.model.Dependiente;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.model.Nacionalidad;
import mx.edu.um.mateo.rh.model.TipoDependiente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class DependienteDaoTest {

    @Autowired
    private DependienteDao dependienteDao;
    private static final Logger log = LoggerFactory.getLogger(DependienteDaoTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Test
    public void TestObtenerListaDependiente() {
        log.debug("Deberia obtener una lista de Dependientes");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Empleado empleado = new Empleado("test001", "test", "test", "test", "M", "address", "A",
                "curp", "rfc", "cuenta", "imss", 10, 100, BigDecimal.ZERO, "mo", "ife", "ra",
                Boolean.TRUE, "padre", "madre", "Ca", "Conyugue", Boolean.TRUE, Boolean.TRUE,
                "iglesia", "responsabilidad",empresa);
        currentSession().save(empleado);
        assertNotNull(empleado.getId());
        for(int i=0; i<20; i++){
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJA);
        dependiente.setEmpleado(empleado);
        dependiente.setStatus("A");
        dependiente.setNombre("test");
        currentSession().save(dependiente);
        assertNotNull(dependiente.getId());
        
        }
        Map<String, Object> params = null;
        Map <String, Object>  result = dependienteDao.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_DEPENDIENTES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Nacionalidad>) result.get(Constantes.CONTAINSKEY_DEPENDIENTES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }
    @Test 
    public void TestObtiene() {
         log.debug("Deberia obtener un Dependiente");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Empleado empleado = new Empleado("test001", "test", "test", "test", "M", "address", "A",
                "curp", "rfc", "cuenta", "imss", 10, 100, BigDecimal.ZERO, "mo", "ife", "ra",
                Boolean.TRUE, "padre", "madre", "Ca", "Conyugue", Boolean.TRUE, Boolean.TRUE,
                "iglesia", "responsabilidad",empresa);
        currentSession().save(empleado);
        assertNotNull(empleado.getId());
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        dependiente.setEmpleado(empleado);
        dependiente.setNombre("test");
        dependiente.setStatus("A");
        currentSession().save(dependiente);
        assertNotNull(dependiente.getId());
        Dependiente prueba = dependienteDao.obtiene(dependiente.getId());
        assertEquals(prueba.getNombre(), dependiente.getNombre());
    }
    @Test
    public void TestCrearDependiente() {
    
        log.debug("Deberia crear Dependiente");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Empleado empleado = new Empleado("test001", "test", "test", "test", "M", "address", "A",
                "curp", "rfc", "cuenta", "imss", 10, 100, BigDecimal.ZERO, "mo", "ife", "ra",
                Boolean.TRUE, "padre", "madre", "Ca", "Conyugue", Boolean.TRUE, Boolean.TRUE,
                "iglesia", "responsabilidad",empresa);
        currentSession().save(empleado);
        assertNotNull(empleado.getId());
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        dependiente.setEmpleado(empleado);
        dependiente.setNombre("test");
        dependiente.setStatus("A");
        currentSession().save(dependiente);
        assertNotNull(dependiente.getId());

    }

    @Test
    public void TestActualizarDependiente() {
        log.debug("Deberia actulizar Dependiente");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Empleado empleado = new Empleado("test001", "test", "test", "test", "M", "address", "A",
                "curp", "rfc", "cuenta", "imss", 10, 100, BigDecimal.ZERO, "mo", "ife", "ra",
                Boolean.TRUE, "padre", "madre", "Ca", "Conyugue", Boolean.TRUE, Boolean.TRUE,
                "iglesia", "responsabilidad",empresa);
        currentSession().save(empleado);
        assertNotNull(empleado.getId());
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        dependiente.setStatus("A");
        dependiente.setNombre("test");
        dependiente.setEmpleado(empleado);
        dependienteDao.graba(dependiente);
        assertNotNull(dependiente.getId());
        dependiente.setTipoDependiente(TipoDependiente.ESPOSO);
        dependiente.setStatus("A");
        dependiente.setEmpleado(empleado);
        dependienteDao.graba(dependiente);
        Dependiente dependiente1 = dependienteDao.obtiene(dependiente.getId());
        assertNotNull(dependiente1);
        assertEquals(TipoDependiente.ESPOSO, dependiente.getTipoDependiente());
    }
    @Test
    public void TestEliminarDependiente()throws Exception{
        log.debug("Deberia eliminar Dependiente");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Empleado empleado = new Empleado("test001", "test", "test", "test", "M", "address", "A",
                "curp", "rfc", "cuenta", "imss", 10, 100, BigDecimal.ZERO, "mo", "ife", "ra",
                Boolean.TRUE, "padre", "madre", "Ca", "Conyugue", Boolean.TRUE, Boolean.TRUE,
                "iglesia", "responsabilidad",empresa);
        currentSession().save(empleado);
        assertNotNull(empleado.getId());
        
        Dependiente dependiente = new Dependiente();
        dependiente.setTipoDependiente(TipoDependiente.HIJO);
        dependiente.setEmpleado(empleado);
        dependiente.setStatus("A");
        dependiente.setNombre("test");
        dependienteDao.graba(dependiente);
        assertNotNull(dependiente.getId());
        String tipo=dependienteDao.elimina(dependiente.getId());
        assertNotNull(tipo);
        assertEquals(tipo, dependiente.getTipoDependiente().toString());
    }
}