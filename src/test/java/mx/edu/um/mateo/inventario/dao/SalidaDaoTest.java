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
public class SalidaDaoTest {

    private static final Logger log = LoggerFactory.getLogger(SalidaDaoTest.class);
    @Autowired
    private SalidaDao instance;
    @Autowired
    private EntradaDao entradaDao;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public SalidaDaoTest() {
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
     * Test of lista method, of class SalidaDao.
     */
    @SuppressWarnings("unchecked")
	@Test
    public void debieraMostrarListaDeSalidas() {
        log.debug("Debiera mostrar lista de salidas");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "tst-01", empresa);
        currentSession().save(almacen);
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        TipoCliente tipoCliente = new TipoCliente("tst-01", "tst-01", empresa);
        currentSession().save(tipoCliente);
        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        currentSession().save(cliente);
        for (int i = 0; i < 20; i++) {
            Salida salida = new Salida("test--" + i, "test--" + i, "test--" + i, "test--" + i, "test--" + i, estatus, cliente, almacen);
            salida.setFolio("TEST" + i);
            currentSession().save(salida);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("almacen", almacen.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get("salidas"));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Salida>) result.get("salidas")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of obtiene method, of class SalidaDao.
     */
    @Test
    public void debieraObtenerSalida() {
        log.debug("Debiera obtener salida");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "tst-01", empresa);
        currentSession().save(almacen);
        Estatus estatus = new Estatus(Constantes.ABIERTA);
        currentSession().save(estatus);
        TipoCliente tipoCliente = new TipoCliente("tst-01", "tst-01", empresa);
        currentSession().save(tipoCliente);
        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        currentSession().save(cliente);
        Salida salida = new Salida("tst-01", "tst-01", "tst-01", "tst-01", "tst-01", estatus, cliente, almacen);
        salida.setFolio("TST");
        currentSession().save(salida);
        Long id = salida.getId();

        Salida result = instance.obtiene(id);
        assertEquals("tst-01", result.getReporte());
    }

    /**
     * Debiera crear tipo de cliente
     */
    @Test
    public void debieraCrearSalida() {
        log.debug("Debiera crear Salida");
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
        TipoCliente tipoCliente = new TipoCliente("tst-01", "tst-01", empresa);
        currentSession().save(tipoCliente);
        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        currentSession().save(cliente);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01"); 
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Salida salida = new Salida("tst-01", "tst-01", "tst-01", "tst-01", "tst-01", estatus, cliente, almacen);
        salida = instance.crea(salida, usuario);
        assertNotNull(salida);
        assertNotNull(salida.getId());
        assertEquals("TS-tst-01tst-01TST000000001", salida.getFolio());
    }

    /**
     * Debiera actualizar tipo de cliente
     */
    @Test
    public void debieraActualizarSalida() throws NoEstaAbiertaException {
        log.debug("Debiera actualizar salida");
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
        TipoCliente tipoCliente = new TipoCliente("tst-01", "tst-01", empresa);
        currentSession().save(tipoCliente);
        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        currentSession().save(cliente);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01"); 
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Salida salida = new Salida("tst-01", "tst-01", "tst-01", "tst-01", "tst-01", estatus, cliente, almacen);
        salida = instance.crea(salida, usuario);
        assertNotNull(salida);
        assertNotNull(salida.getId());
        assertEquals("TS-tst-01tst-01TST000000001", salida.getFolio());

        salida.setReporte("PRUEBA");
        instance.actualiza(salida, usuario);

        Salida prueba = instance.obtiene(salida.getId());
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getReporte());
    }

    @Test
    public void debieraCrearLotes() throws ProductoNoSoportaFraccionException, NoEstaAbiertaException, NoSePuedeCerrarException, NoCuadraException {
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
        TipoCliente tipoCliente = new TipoCliente("tst-01", "tst-01", empresa);
        currentSession().save(tipoCliente);
        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        currentSession().save(cliente);
        TipoProducto tipoProducto = new TipoProducto("TEST-1", "TEST-1", almacen);
        currentSession().save(tipoProducto);
        Producto producto1 = new Producto("TEST1", "TEST1", "TEST1", "TEST1", tipoProducto, almacen);
        currentSession().save(producto1);
        Producto producto2 = new Producto("TEST2", "TEST2", "TEST2", "TEST2", tipoProducto, almacen);
        currentSession().save(producto2);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01"); 
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Entrada entrada = new Entrada("tst-01", "test-01", new Date(), estatus, proveedor, almacen);
        entrada.setIva(new BigDecimal("32.00"));
        entrada.setTotal(new BigDecimal("232.00"));
        entrada = entradaDao.crea(entrada, usuario);
        assertNotNull(entrada);
        assertNotNull(entrada.getId());
        assertEquals("test-01", entrada.getFactura());

        LoteEntrada loteEntrada = new LoteEntrada(new BigDecimal(10), new BigDecimal(10), producto1, entrada);
        loteEntrada = entradaDao.creaLote(loteEntrada);
        assertNotNull(loteEntrada);
        assertNotNull(loteEntrada.getId());

        loteEntrada = new LoteEntrada(new BigDecimal("10"), new BigDecimal("10"), producto2, entrada);
        loteEntrada = entradaDao.creaLote(loteEntrada);
        assertNotNull(loteEntrada);
        assertNotNull(loteEntrada.getId());

        currentSession().refresh(entrada);
        assertEquals(2, entrada.getLotes().size());

        entradaDao.cierra(entrada, usuario);

        Entrada prueba = entradaDao.obtiene(entrada.getId());
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


        Salida salida = new Salida("tst-01", "tst-01", "tst-01", "tst-01", "tst-01", estatus, cliente, almacen);
        salida = instance.crea(salida, usuario);
        assertNotNull(salida);
        assertNotNull(salida.getId());
        assertEquals("TS-tst-01tst-01TST000000001", salida.getFolio());

        LoteSalida lote = new LoteSalida(new BigDecimal(5), producto1, salida);
        lote = instance.creaLote(lote);
        assertNotNull(lote);
        assertNotNull(lote.getId());

        lote = new LoteSalida(new BigDecimal(5), producto2, salida);
        lote = instance.creaLote(lote);
        assertNotNull(lote);
        assertNotNull(lote.getId());

        currentSession().refresh(salida);
        assertEquals(2, salida.getLotes().size());
    }

    @Test
    public void debieraEliminarSalida() throws Exception {
        log.debug("Debiera actualizar salida");
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
        TipoCliente tipoCliente = new TipoCliente("tst-01", "tst-01", empresa);
        currentSession().save(tipoCliente);
        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        currentSession().save(cliente);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01"); 
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Salida salida = new Salida("tst-01", "tst-01", "tst-01", "tst-01", "tst-01", estatus, cliente, almacen);
        salida = instance.crea(salida, usuario);
        assertNotNull(salida);
        assertNotNull(salida.getId());
        assertEquals("TS-tst-01tst-01TST000000001", salida.getFolio());

        String nombre = instance.elimina(salida.getId());
        assertNotNull(nombre);
        assertEquals("TS-tst-01tst-01TST000000001", nombre);

        Salida prueba = instance.obtiene(salida.getId());
        assertNull(prueba);
    }

    @Test
    public void debieraCerrarSalida() throws NoSePuedeCerrarException, NoCuadraException, NoEstaAbiertaException, NoHayExistenciasSuficientes, ProductoNoSoportaFraccionException {
        log.debug("Debiera cerrar salida");
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
        TipoCliente tipoCliente = new TipoCliente("tst-01", "tst-01", empresa);
        currentSession().save(tipoCliente);
        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        currentSession().save(cliente);
        TipoProducto tipoProducto = new TipoProducto("TEST-1", "TEST-1", almacen);
        currentSession().save(tipoProducto);
        Producto producto1 = new Producto("TEST1", "TEST1", "TEST1", "TEST1", tipoProducto, almacen);
        currentSession().save(producto1);
        Producto producto2 = new Producto("TEST2", "TEST2", "TEST2", "TEST2", tipoProducto, almacen);
        currentSession().save(producto2);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01"); 
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Entrada entrada = new Entrada("tst-01", "test-01", new Date(), estatus, proveedor, almacen);
        entrada.setIva(new BigDecimal("32.00"));
        entrada.setTotal(new BigDecimal("232.00"));
        entrada = entradaDao.crea(entrada, usuario);
        assertNotNull(entrada);
        assertNotNull(entrada.getId());
        assertEquals("test-01", entrada.getFactura());

        LoteEntrada loteEntrada = new LoteEntrada(new BigDecimal(10), new BigDecimal(10), producto1, entrada);
        loteEntrada = entradaDao.creaLote(loteEntrada);
        assertNotNull(loteEntrada);
        assertNotNull(loteEntrada.getId());

        loteEntrada = new LoteEntrada(new BigDecimal("10"), new BigDecimal("10"), producto2, entrada);
        loteEntrada = entradaDao.creaLote(loteEntrada);
        assertNotNull(loteEntrada);
        assertNotNull(loteEntrada.getId());

        currentSession().refresh(entrada);
        assertEquals(2, entrada.getLotes().size());

        entradaDao.cierra(entrada, usuario);

        Entrada prueba = entradaDao.obtiene(entrada.getId());
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


        Salida salida = new Salida("tst-01", "tst-01", "tst-01", "tst-01", "tst-01", estatus, cliente, almacen);
        salida = instance.crea(salida, usuario);
        assertNotNull(salida);
        assertNotNull(salida.getId());
        assertEquals("TS-tst-01tst-01TST000000001", salida.getFolio());

        LoteSalida lote = new LoteSalida(new BigDecimal(5), producto1, salida);
        lote = instance.creaLote(lote);
        assertNotNull(lote);
        assertNotNull(lote.getId());

        lote = new LoteSalida(new BigDecimal(5), producto2, salida);
        lote = instance.creaLote(lote);
        assertNotNull(lote);
        assertNotNull(lote.getId());

        currentSession().refresh(salida);
        assertEquals(2, salida.getLotes().size());

        instance.cierra(salida, usuario);

        Salida test = instance.obtiene(salida.getId());
        assertNotNull(test);
        assertEquals("S-tst-01tst-01TST000000001", test.getFolio());
    }
}
