/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

<<<<<<< HEAD
import java.math.BigDecimal;
=======
>>>>>>> b20b2e03d2080fc6bc589dd66140f8133a75a89e
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AlumnoPaquete;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.fail;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 *
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class AlumnoPaqueteDaoTest {
    
    @Autowired
    private AlumnoPaqueteDao instance;
    private static final Logger log = LoggerFactory.getLogger(AlumnoPaqueteDaoTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public AlumnoPaqueteDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPaquetes method, of class PaqueteDao.
     */
    @Test
    public void debieraMostrarListaDeAlumnoPaquete() {

        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
<<<<<<< HEAD
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
=======
        Paquete paquete = new Paquete("Test","Test1","1110475", new Double(12), new Double(12),"1", usuario.getEmpresa());
>>>>>>> b20b2e03d2080fc6bc589dd66140f8133a75a89e
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = null;
        for (int i = 0; i < 20; i++) {
            alumnoPaquete = new AlumnoPaquete();
            alumnoPaquete.setPaquete(paquete);
            alumnoPaquete.setMatricula("1110475");
            alumnoPaquete.setStatus("A");
            instance.graba(alumnoPaquete, usuario);
            assertNotNull(alumnoPaquete.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", empresa.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_ALUMNOPAQUETES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TiposBecas>) result.get(Constantes.CONTAINSKEY_ALUMNOPAQUETES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of getPaquete method, of class PaqueteDao.
     */
    @Test
    public void debieraObtenerAlumnoPaquete() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
<<<<<<< HEAD
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
=======
        Paquete paquete = new Paquete("Test","Test1","1110475", new Double(12), new Double(12),"1", usuario.getEmpresa());
>>>>>>> b20b2e03d2080fc6bc589dd66140f8133a75a89e
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        currentSession().save(alumnoPaquete);
        assertNotNull(alumnoPaquete.getId());
        AlumnoPaquete alumnoPaquete1 = instance.obtiene(alumnoPaquete.getId());
        assertEquals(alumnoPaquete.getMatricula(), alumnoPaquete1.getMatricula());

    }

    /**
     * Test of savePaquete method, of class PaqueteDao.
     */
    @Test
    public void debieraCrearAlumnoPaquete() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
<<<<<<< HEAD
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
=======
        Paquete paquete = new Paquete("Test","Test1","1110475", new Double(12), new Double(12),"1", usuario.getEmpresa());
>>>>>>> b20b2e03d2080fc6bc589dd66140f8133a75a89e
        currentSession().save(paquete);
        Long id = usuario.getId();
        assertNotNull(id);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        alumnoPaquete.getPaquete().setEmpresa(empresa);
        instance.graba(alumnoPaquete, usuario);
        assertNotNull(alumnoPaquete.getId());
    }

    /**
     * Test of removePaquete method, of class PaqueteDao.
     */
    @Test
    public void debieraEliminarAlumnoPaquete() {
       Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
<<<<<<< HEAD
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
=======
        Paquete paquete = new Paquete("Test","Test1","1110475", new Double(12), new Double(12),"1", usuario.getEmpresa());
>>>>>>> b20b2e03d2080fc6bc589dd66140f8133a75a89e
        currentSession().save(paquete);
        Long id = usuario.getId();
        assertNotNull(id);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        alumnoPaquete.getPaquete().setEmpresa(empresa);
        instance.graba(alumnoPaquete, usuario);
        assertNotNull(alumnoPaquete.getId());
        
        Long idTm = alumnoPaquete.getId();
        
        instance.elimina(idTm);
        try{
        AlumnoPaquete alumnoPaquete2 = instance.obtiene(idTm);
        fail("Error al eliminar alumno");
        }catch (ObjectRetrievalFailureException e){
            
        }
        
    }
}
