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

import java.math.BigDecimal;
import java.util.*;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inventario.model.*;
import mx.edu.um.mateo.inventario.utils.*;
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
public class EntradaDaoTest {

    private static final Logger log = LoggerFactory.getLogger(EntradaDaoTest.class);
    @Autowired
    private EntradaDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public EntradaDaoTest() {
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
     * Test of lista method, of class EntradaDao.
     */
    @Test
    public void debieraMostrarListaDeEntradas() {
        log.debug("Debiera mostrar lista de entradas");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "tst-01", empresa);
        currentSession().save(almacen);
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        currentSession().save(proveedor);
        for (int i = 0; i < 20; i++) {
            Entrada entrada = new Entrada("test" + i, "test" + i, new Date(), estatus, proveedor, almacen);
            currentSession().save(entrada);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("almacen", almacen.getId());
        Map result = instance.lista(params);
        assertNotNull(result.get("entradas"));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Entrada>) result.get("entradas")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of obtiene method, of class EntradaDao.
     */
    @Test
    public void debieraObtenerEntrada() {
        log.debug("Debiera obtener entrada");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "tst-01", empresa);
        currentSession().save(almacen);
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        currentSession().save(proveedor);
        Entrada entrada = new Entrada("tst-01", "test-01", new Date(), estatus, proveedor, almacen);
        currentSession().save(entrada);
        Long id = entrada.getId();

        Entrada result = instance.obtiene(id);
        assertEquals("test-01", result.getFactura());
    }

    /**
     * Debiera crear tipo de cliente
     */
    @Test
    public void debieraCrearEntrada() {
        log.debug("Debiera crear Entrada");
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
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        currentSession().save(proveedor);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Entrada entrada = new Entrada("tst-01", "test-01", new Date(), estatus, proveedor, almacen);
        entrada = instance.crea(entrada, usuario);
        assertNotNull(entrada);
        assertNotNull(entrada.getId());
        assertEquals("test-01", entrada.getFactura());
    }

    /**
     * Debiera actualizar tipo de cliente
     */
    @Test
    public void debieraActualizarEntrada() throws NoEstaAbiertaException {
        log.debug("Debiera actualizar entrada");
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
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        currentSession().save(proveedor);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Entrada entrada = new Entrada("tst-01", "test-01", new Date(), estatus, proveedor, almacen);
        entrada = instance.crea(entrada, usuario);
        assertNotNull(entrada);
        assertNotNull(entrada.getId());
        assertEquals("test-01", entrada.getFactura());

        entrada.setFactura("PRUEBA");
        instance.actualiza(entrada, usuario);

        Entrada prueba = instance.obtiene(entrada.getId());
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getFactura());
    }

    @Test(expected = NoEstaAbiertaException.class)
    public void noDebieraActualizarEntrada() throws NoEstaAbiertaException {
        log.debug("No debiera actualizar entrada no abierta");
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
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        Estatus estatus2 = new Estatus(Constantes.CERRADA);
        currentSession().save(estatus2);
        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        currentSession().save(proveedor);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Entrada entrada = new Entrada("tst-01", "test-01", new Date(), estatus, proveedor, almacen);
        entrada = instance.crea(entrada, usuario);
        assertNotNull(entrada);
        assertNotNull(entrada.getId());
        assertEquals("test-01", entrada.getFactura());

        entrada.setFactura("PRUEBA");
        entrada.setEstatus(estatus2);
        instance.actualiza(entrada, usuario);
        fail("Debiera lanzar una excepcion porque el estatus de la entrada es cerrada");
    }

    @Test
    public void debieraCrearLotes() throws ProductoNoSoportaFraccionException, NoEstaAbiertaException {
        log.debug("Debiera crear lotes");
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
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        Estatus estatus2 = new Estatus(Constantes.CERRADA);
        currentSession().save(estatus2);
        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        currentSession().save(proveedor);
        TipoProducto tipoProducto = new TipoProducto("TEST-1", "TEST-1", almacen);
        currentSession().save(tipoProducto);
        Producto producto1 = new Producto("TEST1", "TEST1", "TEST1", "TEST1", tipoProducto, almacen);
        currentSession().save(producto1);
        Producto producto2 = new Producto("TEST2", "TEST2", "TEST2", "TEST2", tipoProducto, almacen);
        currentSession().save(producto2);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Entrada entrada = new Entrada("tst-01", "test-01", new Date(), estatus, proveedor, almacen);
        entrada = instance.crea(entrada, usuario);
        assertNotNull(entrada);
        assertNotNull(entrada.getId());
        assertEquals("test-01", entrada.getFactura());

        LoteEntrada lote = new LoteEntrada(new BigDecimal(10), new BigDecimal(10), producto1, entrada);
        lote = instance.creaLote(lote);
        assertNotNull(lote);
        assertNotNull(lote.getId());

        lote = new LoteEntrada(new BigDecimal(10), new BigDecimal(10), producto2, entrada);
        lote = instance.creaLote(lote);
        assertNotNull(lote);
        assertNotNull(lote.getId());

        currentSession().refresh(entrada);
        assertEquals(2, entrada.getLotes().size());
    }

    /**
     * Debiera Eliminar tipo de cliente
     *
     * @throws Exception
     */
    @Test
    public void debieraEliminarEntrada() throws Exception {
        log.debug("Debiera actualizar entrada");
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
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        currentSession().save(proveedor);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Entrada entrada = new Entrada("tst-01", "test-01", new Date(), estatus, proveedor, almacen);
        entrada = instance.crea(entrada, usuario);
        assertNotNull(entrada);
        assertNotNull(entrada.getId());
        assertEquals("test-01", entrada.getFactura());

        String nombre = instance.elimina(entrada.getId());
        assertNotNull(nombre);
        assertEquals("TE-tst-01tst-01TST000000001", nombre);

        Entrada prueba = instance.obtiene(entrada.getId());
        assertNull(prueba);
    }

    @Test(expected = NoSePuedeCerrarEnCeroException.class)
    public void noDebieraCerrarEntrada() throws NoSePuedeCerrarException, NoCuadraException, NoSePuedeCerrarEnCeroException, NoEstaAbiertaException {
        log.debug("Debiera cerrar entrada");
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
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        Estatus estatus2 = new Estatus(Constantes.CERRADA);
        currentSession().save(estatus2);
        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        currentSession().save(proveedor);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Entrada entrada = new Entrada("tst-01", "test-01", new Date(), estatus, proveedor, almacen);
        entrada = instance.crea(entrada, usuario);
        assertNotNull(entrada);
        assertNotNull(entrada.getId());
        assertEquals("test-01", entrada.getFactura());

        instance.cierra(entrada, usuario);
        fail("Debiera lanzar excepcion de que no puede cerrar la entrada en cero");
    }

    @Test
    public void debieraCerrarEntradaConLotes() throws ProductoNoSoportaFraccionException, NoSePuedeCerrarException, NoCuadraException, NoSePuedeCerrarEnCeroException, NoEstaAbiertaException {
        log.debug("Debiera cerrar entrada con lotes");
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
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        Estatus estatus2 = new Estatus(Constantes.CERRADA);
        currentSession().save(estatus2);
        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        currentSession().save(proveedor);
        TipoProducto tipoProducto = new TipoProducto("TEST-1", "TEST-1", almacen);
        currentSession().save(tipoProducto);
        Producto producto1 = new Producto("TEST1", "TEST1", "TEST1", "TEST1", tipoProducto, almacen);
        currentSession().save(producto1);
        Producto producto2 = new Producto("TEST2", "TEST2", "TEST2", "TEST2", tipoProducto, almacen);
        producto2.setFraccion(true);
        currentSession().save(producto2);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Entrada entrada = new Entrada("tst-01", "test-01", new Date(), estatus, proveedor, almacen);
        entrada.setIva(new BigDecimal("32.00"));
        entrada.setTotal(new BigDecimal("232.00"));
        entrada = instance.crea(entrada, usuario);
        assertNotNull(entrada);
        assertNotNull(entrada.getId());
        assertEquals("test-01", entrada.getFactura());

        LoteEntrada lote = new LoteEntrada(new BigDecimal(10), new BigDecimal(10), producto1, entrada);
        lote = instance.creaLote(lote);
        assertNotNull(lote);
        assertNotNull(lote.getId());

        lote = new LoteEntrada(new BigDecimal("10"), new BigDecimal("10"), producto2, entrada);
        lote = instance.creaLote(lote);
        assertNotNull(lote);
        assertNotNull(lote.getId());

        currentSession().refresh(entrada);
        assertEquals(2, entrada.getLotes().size());

        instance.cierra(entrada, usuario);

        Entrada prueba = instance.obtiene(entrada.getId());
        assertNotNull(prueba);
        assertEquals("E-tst-01tst-01TST000000001", prueba.getFolio());

        currentSession().refresh(producto1);
        currentSession().refresh(producto2);
        assertEquals(new BigDecimal("232.00"), prueba.getTotal());
        assertEquals(new BigDecimal("32.00"), prueba.getIva());
        assertEquals(new BigDecimal("10.000"), producto1.getExistencia());
        assertEquals(new BigDecimal("10.00"), producto1.getPrecioUnitario());
        assertEquals(new BigDecimal("10.00"), producto1.getUltimoPrecio());
        assertEquals(new BigDecimal("10.000"), producto2.getExistencia());
        assertEquals(new BigDecimal("10.00"), producto2.getPrecioUnitario());
        assertEquals(new BigDecimal("10.00"), producto2.getUltimoPrecio());
    }
}
