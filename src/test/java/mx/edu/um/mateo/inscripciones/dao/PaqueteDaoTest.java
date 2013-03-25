/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

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
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
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

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class PaqueteDaoTest {

    @Autowired
    private PaqueteDao instance;
    private static final Logger log = LoggerFactory.getLogger(PaqueteDaoTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public PaqueteDaoTest() {
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
    public void testGetPaquetes() {
   

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
        Paquete paquete=null;
        for (int i = 0; i < 20; i++) {
            paquete = new Paquete();
            paquete.setAcfe("s");
            paquete.setDescripcion("test");
            paquete.setEnsenanza(new Double("1.2"));
            paquete.setInternado(new Double("2.2"));
            paquete.setMatricula("1110475");
            paquete.setNombre("Test");
            instance.graba(paquete, usuario);
            assertNotNull(paquete.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", empresa.getId());
        Map<String, Object> result = instance.getPaquetes(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_PAQUETES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TiposBecas>) result.get(Constantes.CONTAINSKEY_PAQUETES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of getPaquete method, of class PaqueteDao.
     */
    @Test
    public void testGetPaquete() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new Double("1.2"));
        paquete.setInternado(new Double("2.2"));
        paquete.setMatricula("1110475");
        paquete.setNombre(nombre);
        paquete.setEmpresa(empresa);
        currentSession().save(paquete);
        assertNotNull(paquete.getId());
        Paquete paquete1 = instance.getPaquete(paquete.getId());
        assertEquals(paquete.getNombre(), paquete1.getNombre());

    }

    /**
     * Test of savePaquete method, of class PaqueteDao.
     */
    @Test
    public void testSavePaquete() {
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
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new Double("1.2"));
        paquete.setInternado(new Double("2.2"));
        paquete.setMatricula("1110475");
        paquete.setNombre(nombre);
        paquete.setEmpresa(empresa);
        instance.graba(paquete, usuario);
        assertNotNull(paquete.getId());
    }

    /**
     * Test of removePaquete method, of class PaqueteDao.
     */
    @Test
    public void testRemovePaquete() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new Double("1.2"));
        paquete.setInternado(new Double("2.2"));
        paquete.setMatricula("1110475");
        paquete.setNombre(nombre);
        paquete.setEmpresa(empresa);
        currentSession().save(paquete);
        assertNotNull(paquete.getId());
        String descripcion = instance.removePaquete(paquete.getId());
        assertEquals(paquete.getDescripcion(), descripcion);
    }
}
