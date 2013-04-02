/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.math.BigDecimal;
import java.util.Date;
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
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import mx.edu.um.mateo.inscripciones.model.Institucion;
import mx.edu.um.mateo.inscripciones.model.Prorroga;
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
public class CobroCampoDaoTest {

    @Autowired
    private CobroCampoDao instance;
    private static final Logger log = LoggerFactory.getLogger(CobroCampoDaoTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public CobroCampoDaoTest() {
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
     * Test of lista method, of class CobroCampoDao.
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
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        currentSession().save(institucion);

        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        CobroCampo cobroCampo = null;
        for (int i = 0; i < 20; i++) {
            cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
            cobroCampo.setEmpresa(empresa);
            cobroCampo.setFechaAlta(new Date());
            cobroCampo.setFechaModificacion(new Date());
            cobroCampo.setStatus("A");
            cobroCampo.setUsuarioAlta(usuario);
            cobroCampo.setUsuarioModificacion(usuario);
            instance.graba(cobroCampo, usuario);
            assertNotNull(cobroCampo.getId());
        }
        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", empresa.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_COBROSCAMPOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Prorroga>) result.get(Constantes.CONTAINSKEY_COBROSCAMPOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class CobroCampoDao.
     */
    @Test
    public void testObtiene() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        currentSession().save(institucion);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(empresa);
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());
        CobroCampo cobroCampo1 = instance.obtiene(cobroCampo.getId());
        assertEquals(cobroCampo.getMatricula(), cobroCampo1.getMatricula());

    }

    /**
     * Test of graba method, of class CobroCampoDao.
     */
    @Test
    public void testGraba() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        currentSession().save(institucion);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(empresa);
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());
    }

    /**
     * Test of elimina method, of class CobroCampoDao.
     */
    @Test
    public void testElimina() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        currentSession().save(institucion);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(empresa);
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());
        String matricula = instance.elimina(cobroCampo.getId());
        assertEquals(matricula, cobroCampo.getMatricula());
    }
}
