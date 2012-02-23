/*
 * The MIT License
 *
 * Copyright 2012 J. David Mendoza <jdmendoza@um.edu.mx>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.general.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class TipoClienteDaoTest {

    private static final Logger log = LoggerFactory.getLogger(TipoClienteDaoTest.class);
    @Autowired
    private TipoClienteDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public TipoClienteDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of lista method, of class TipoClienteDao.
     */
    @Test
    public void debieraMostrarListaDeProveedores() {
        log.debug("Debiera mostrar lista de proveedores");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        for (int i = 0; i < 20; i++) {
            TipoCliente tipoCliente = new TipoCliente("test" + i, "test" + i, empresa);
            currentSession().save(tipoCliente);
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get("tiposDeCliente"));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<TipoCliente>) result.get("tiposDeCliente")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of obtiene method, of class TipoClienteDao.
     */
    @Test
    public void debieraObtenerTipoCliente() {
        log.debug("Debiera obtener tipoCliente");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        TipoCliente tipoCliente = new TipoCliente("tst-01", "test-01", empresa);
        currentSession().save(tipoCliente);
        Long id = tipoCliente.getId();

        TipoCliente result = instance.obtiene(id);
        assertEquals("tst-01", result.getNombre());
    }

    /**
     * Test of crea method, of class ProveedorDao.
     */
    @Test
    public void debieraCrearTipoCliente() {
        log.debug("Debiera crear TipoCliente");
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
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        TipoCliente tipoCliente = new TipoCliente("tst-01", "test-01", empresa);
        tipoCliente = instance.crea(tipoCliente, usuario);
        assertNotNull(tipoCliente);
        assertNotNull(tipoCliente.getId());
        assertEquals("tst-01", tipoCliente.getNombre());
    }

    @Test
    public void debieraActualizarTipoCliente() {
        log.debug("Debiera actualizar tipoCliente");
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
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        TipoCliente tipoCliente = new TipoCliente("tst-01", "test-01", empresa);
        tipoCliente = instance.crea(tipoCliente, usuario);
        assertNotNull(tipoCliente);
        assertNotNull(tipoCliente.getId());
        assertEquals("tst-01", tipoCliente.getNombre());

        tipoCliente.setNombre("PRUEBA");
        instance.actualiza(tipoCliente, usuario);

        TipoCliente prueba = instance.obtiene(tipoCliente.getId());
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getNombre());
    }

    @Test
    public void debieraEliminarTipoCliente() throws Exception {
        log.debug("Debiera actualizar tipoCliente");
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
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        TipoCliente tipoCliente = new TipoCliente("tst-01", "test-01", empresa);
        tipoCliente = instance.crea(tipoCliente, usuario);
        assertNotNull(tipoCliente);
        assertNotNull(tipoCliente.getId());
        assertEquals("tst-01", tipoCliente.getNombre());

        String nombre = instance.elimina(tipoCliente.getId());
        assertNotNull(nombre);
        assertEquals("tst-01", nombre);

        TipoCliente prueba = instance.obtiene(tipoCliente.getId());
        assertNull(prueba);
    }
}
