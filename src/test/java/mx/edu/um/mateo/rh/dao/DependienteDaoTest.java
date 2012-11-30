/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.rh.model.Dependiente;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.model.Nacionalidad;
import mx.edu.um.mateo.rh.model.TipoDependiente;
import mx.edu.um.mateo.rh.model.TipoEmpleado;
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
    @Autowired
    private TipoEmpleadoDao tipoEmpleadoDao;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Test
    public void testObtenerListaDependiente() {
        log.debug("Deberia obtener una lista de Dependientes");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setOrganizacion(organizacion);
        tipoEmpleado.setDescripcion("Denominacional");
        tipoEmpleado.setPrefijo("980");
        currentSession().save(tipoEmpleado);

        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password", tipoEmpleado);
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
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
    public void testObtiene() {
         log.debug("Deberia obtener un Dependiente");
           Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setOrganizacion(organizacion);
        tipoEmpleado.setDescripcion("Denominacional");
        tipoEmpleado.setPrefijo("980");
        currentSession().save(tipoEmpleado);

        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password", tipoEmpleado);
        
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
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
    public void testCrearDependiente() {
    
        log.debug("Deberia crear Dependiente");
           Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setOrganizacion(organizacion);
        tipoEmpleado.setDescripcion("Denominacional");
        tipoEmpleado.setPrefijo("980");
        currentSession().save(tipoEmpleado);

        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password", tipoEmpleado);
        
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
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
    public void testActualizarDependiente() {
        log.debug("Deberia actulizar Dependiente");
           Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setOrganizacion(organizacion);
        tipoEmpleado.setDescripcion("Denominacional");
        tipoEmpleado.setPrefijo("980");
        currentSession().save(tipoEmpleado);

        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password", tipoEmpleado);
        
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
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
    public void testEliminarDependiente()throws Exception{
        log.debug("Deberia eliminar Dependiente");
           Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setOrganizacion(organizacion);
        tipoEmpleado.setDescripcion("Denominacional");
        tipoEmpleado.setPrefijo("980");
        currentSession().save(tipoEmpleado);

        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password", tipoEmpleado);
        
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
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