/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
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
package mx.edu.um.mateo.inventario.dao;

import java.util.*;

import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.inventario.model.TipoProducto;
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
public class TipoProductoDaoTest {

    private static final Logger log = LoggerFactory.getLogger(TipoProductoDaoTest.class);
    @Autowired
    private TipoProductoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public TipoProductoDaoTest() {
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
     * Test of lista method, of class TipoProductoDao.
     */
    @SuppressWarnings("unchecked")
	@Test
    public void debieraMostrarListaDeTiposDeProducto() {
        log.debug("Debiera mostrar lista de tiposDeProducto");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "tst-01", empresa);
        currentSession().save(almacen);
        for (int i = 0; i < 20; i++) {
            TipoProducto tipoProducto = new TipoProducto("test" + i, "test" + i, almacen);
            currentSession().save(tipoProducto);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("almacen", almacen.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get("tiposDeProducto"));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<TipoProducto>) result.get("tiposDeProducto")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of obtiene method, of class TipoProductoDao.
     */
    @Test
    public void debieraObtenerTipoProducto() {
        log.debug("Debiera obtener tipoProducto");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "tst-01", empresa);
        currentSession().save(almacen);
        TipoProducto tipoProducto = new TipoProducto("tst-01", "test-01", almacen);
        currentSession().save(tipoProducto);
        Long id = tipoProducto.getId();

        TipoProducto result = instance.obtiene(id);
        assertEquals("tst-01", result.getNombre());
    }

    /**
     * Debiera crear tipo de cliente
     */
    @Test
    public void debieraCrearTipoProducto() {
        log.debug("Debiera crear TipoProducto");
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

        TipoProducto tipoProducto = new TipoProducto("tst-01", "test-01", almacen);
        tipoProducto = instance.crea(tipoProducto, usuario);
        assertNotNull(tipoProducto);
        assertNotNull(tipoProducto.getId());
        assertEquals("tst-01", tipoProducto.getNombre());
    }

    /**
     * Debiera actualizar tipo de cliente
     */
    @Test
    public void debieraActualizarTipoProducto() {
        log.debug("Debiera actualizar tipoProducto");
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

        TipoProducto tipoProducto = new TipoProducto("tst-01", "test-01", almacen);
        tipoProducto = instance.crea(tipoProducto, usuario);
        assertNotNull(tipoProducto);
        assertNotNull(tipoProducto.getId());
        assertEquals("tst-01", tipoProducto.getNombre());

        tipoProducto.setNombre("PRUEBA");
        instance.actualiza(tipoProducto, usuario);

        TipoProducto prueba = instance.obtiene(tipoProducto.getId());
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getNombre());
    }

    /**
     * Debiera Eliminar tipo de cliente
     *
     * @throws Exception
     */
    @Test
    public void debieraEliminarTipoProducto() throws Exception {
        log.debug("Debiera actualizar tipoProducto");
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

        TipoProducto tipoProducto = new TipoProducto("tst-01", "test-01", almacen);
        tipoProducto = instance.crea(tipoProducto, usuario);
        assertNotNull(tipoProducto);
        assertNotNull(tipoProducto.getId());
        assertEquals("tst-01", tipoProducto.getNombre());

        String nombre = instance.elimina(tipoProducto.getId());
        assertNotNull(nombre);
        assertEquals("tst-01", nombre);

        TipoProducto prueba = instance.obtiene(tipoProducto.getId());
        assertNull(prueba);
    }
}
