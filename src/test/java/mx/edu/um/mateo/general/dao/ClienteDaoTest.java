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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mx.edu.um.mateo.general.model.Cliente;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.TipoCliente;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.Almacen;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ClienteDaoTest {

    private static final Logger log = LoggerFactory.getLogger(ClienteDaoTest.class);
    @Autowired
    private ClienteDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class ClienteDao.
     */
    @SuppressWarnings("unchecked")
	@Test
    public void debieraMostrarListaDeClientes() {
        log.debug("Debiera mostrar lista de clientes");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        TipoCliente tipoCliente = new TipoCliente("TEST-01", "TEST-01", empresa);
        currentSession().save(tipoCliente);
        for (int i = 0; i < 20; i++) {
            Cliente cliente = new Cliente("test" + i, "test" + i, "test0000000" + i, tipoCliente, empresa);
            instance.crea(cliente);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", empresa.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get("clientes"));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Cliente>) result.get("clientes")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of obtiene method, of class ClienteDao.
     */
    @Test
    public void debieraObtenerCliente() {
        log.debug("Debiera obtener cliente");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        TipoCliente tipoCliente = new TipoCliente("TEST-01", "TEST-01", empresa);
        currentSession().save(tipoCliente);
        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        currentSession().save(cliente);
        Long id = cliente.getId();
        Cliente result = instance.obtiene(id);
        assertEquals("tst-01", result.getNombre());
    }

    /**
     * Test of crea method, of class ClienteDao.
     */
    @Test
    public void debieraCrearCliente() {
        log.debug("Debiera crear cliente");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        TipoCliente tipoCliente = new TipoCliente("TEST-01", "TEST-01", empresa);
        currentSession().save(tipoCliente);
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

        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        cliente = instance.crea(cliente, usuario);
        assertNotNull(cliente);
        assertNotNull(cliente.getId());
        assertEquals("tst-01", cliente.getNombre());
    }

    /**
     * Test of actualiza method, of class ClienteDao.
     */
    @Test
    public void debieraActualizarCliente() {
        log.debug("Debiera actualizar cliente");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        TipoCliente tipoCliente = new TipoCliente("TEST-01", "TEST-01", empresa);
        currentSession().save(tipoCliente);
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

        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        cliente = instance.crea(cliente, usuario);
        assertNotNull(cliente);
        assertNotNull(cliente.getId());
        assertEquals("tst-01", cliente.getNombre());

        cliente.setNombre("PRUEBA");
        instance.actualiza(cliente, usuario);

        Cliente prueba = instance.obtiene(cliente.getId());
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getNombre());
    }

    /**
     * Test of elimina method, of class ClienteDao.
     */
    @Test
    public void debieraEliminarCliente() throws Exception {
        log.debug("Debiera actualizar cliente");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        TipoCliente tipoCliente = new TipoCliente("TEST-01", "TEST-01", empresa);
        currentSession().save(tipoCliente);
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

        Cliente cliente = new Cliente("tst-01", "test-01", "test-00000001", tipoCliente, empresa);
        cliente = instance.crea(cliente, usuario);
        assertNotNull(cliente);
        assertNotNull(cliente.getId());
        assertEquals("tst-01", cliente.getNombre());

        String nombre = instance.elimina(cliente.getId());
        assertNotNull(nombre);
        assertEquals("tst-01", nombre);

        Cliente prueba = instance.obtiene(cliente.getId());
        assertNull(prueba);
    }
}
