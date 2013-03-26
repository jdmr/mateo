/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
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
import mx.edu.um.mateo.general.model.Proveedor;
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
 * @author jdmr
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class EmpresaDaoTest {

    private static final Logger log = LoggerFactory.getLogger(EmpresaDaoTest.class);
    @Autowired
    private EmpresaDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class EmpresaDao.
     */
    @SuppressWarnings("unchecked")
	@Test
    public void debieraMostrarListaDeEmpresas() {
        log.debug("Debiera mostrar lista de empresas");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        for (int i = 0; i < 20; i++) {
            Empresa empresa = new Empresa("test" + i, "test" + i, "test" + i, "000000000001", organizacion);
            currentSession().save(empresa);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("organizacion", organizacion.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get("empresas"));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Empresa>) result.get("empresas")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of obtiene method, of class EmpresaDao.
     */
    @Test
    public void debieraObtenerEmpresa() {
        log.debug("Debiera obtener empresa");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Long id = empresa.getId();
        Empresa result = instance.obtiene(id);
        assertEquals("test-01", result.getNombre());
    }

    /**
     * Test of crea method, of class EmpresaDao.
     */
    @Test
    public void debieraCrearEmpresa() {
        log.debug("Debiera crear empresa");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa prueba = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(prueba);
        Almacen almacen = new Almacen("TST", "TEST01", prueba);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(prueba);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        empresa = instance.crea(empresa, usuario);
        assertNotNull(empresa);
        assertNotNull(empresa.getId());
        assertEquals("tst-01", empresa.getCodigo());
    }

    /**
     * Test of actualiza method, of class EmpresaDao.
     */
    @Test
    public void debieraActualizarEmpresa() {
        log.debug("Debiera actualizar empresa");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa test = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(test);
        Almacen almacen = new Almacen("TST", "TEST01", test);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(test);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        id = test.getId();
        Proveedor proveedor = new Proveedor(test.getNombre(), test.getNombreCompleto(), test.getRfc(), true, test);
        currentSession().save(proveedor);
        TipoCliente tipoCliente = new TipoCliente("TIPO1", "TIPO1", test);
        currentSession().save(tipoCliente);
        Cliente cliente = new Cliente(test.getNombre(), test.getNombreCompleto(), test.getRfc(), tipoCliente, true, test);
        currentSession().save(cliente);
        currentSession().flush();
        currentSession().refresh(test);

        log.debug("Obteniendo empresa");
        Empresa empresa = instance.obtiene(id);
        log.debug("Modificando nombre");
        empresa.setNombre("PRUEBA");
        log.debug("Enviando a actualizar empresa");
        instance.actualiza(empresa, usuario);

        log.debug("Obteniendo empresa nuevamente");
        Empresa prueba = instance.obtiene(empresa.getId());
        log.debug("Haciendo asserts");
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getNombre());
    }

    /**
     * Test of elimina method, of class EmpresaDao.
     */
    @Test
    public void debieraEliminarEmpresa() throws Exception {
        log.debug("Debiera actualizar empresa");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa test = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(test);
        Almacen almacen = new Almacen("TST", "TEST01", test);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(test);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        id = test.getId();

        Empresa empresa = new Empresa("TEST02", "TEST02", "TEST02", "000000000001", organizacion);
        currentSession().save(empresa);

        String nombre = instance.elimina(id);
        assertNotNull(nombre);
        assertEquals("TEST01", nombre);

        Empresa prueba = instance.obtiene(id);
        assertNull(prueba);
    }
    
}
