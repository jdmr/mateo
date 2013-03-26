/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Prorroga;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class ProrrogaDaoTest {

    @Autowired
    private ProrrogaDao instance;
    private static final Logger log = LoggerFactory.getLogger(ProrrogaDaoTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public ProrrogaDaoTest() {
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
     * Test of lista method, of class ProrrogaDao.
     */
    @Test
    public void testLista() {
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
        Prorroga prorroga = null;
        for (int i = 0; i < 20; i++) {
            prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("2369.8"), "a");
            prorroga.setObservaciones("test");
            instance.graba(prorroga, usuario);
            assertNotNull(prorroga.getId());
        }
        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", empresa.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_PRORROGAS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Prorroga>) result.get(Constantes.CONTAINSKEY_PRORROGAS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class ProrrogaDao.
     */
    @Test
    public void testObtiene() {
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

        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("2369.8"), "a");
        prorroga.setObservaciones("test");
        instance.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
        String matricula = "1110476";
        prorroga.setMatricula(matricula);
        instance.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
        Prorroga prorroga1 = instance.obtiene(prorroga.getId());
        assertEquals(prorroga1.getMatricula(), prorroga.getMatricula());

    }

    /**
     * Test of graba method, of class ProrrogaDao.
     */
    @Test
    public void testGraba() {
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
        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("2369.8"), "a");
        prorroga.setObservaciones("test");
        instance.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
    }

    /**
     * Test of elimina method, of class ProrrogaDao.
     */
    @Test
    public void testElimina() {
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

        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("2369.8"), "a");
        prorroga.setObservaciones("test");
        instance.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());

        String descripcion = instance.elimina(prorroga.getId());
        assertEquals(prorroga.getDescripcion(), descripcion);

    }
}
