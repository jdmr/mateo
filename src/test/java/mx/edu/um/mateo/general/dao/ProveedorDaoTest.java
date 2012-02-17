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
public class ProveedorDaoTest {

    private static final Logger log = LoggerFactory.getLogger(ProveedorDaoTest.class);
    @Autowired
    private ProveedorDao instance;
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class ProveedorDao.
     */
    @Test
    public void debieraMostrarListaDeProveedores() {
        log.debug("Debiera mostrar lista de proveedores");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        currentSession().save(empresa);
        for (int i = 0; i < 20; i++) {
            Proveedor proveedor = new Proveedor("test" + i, "test" + i, "test0000000" + i, empresa);
            instance.crea(proveedor);
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get("proveedores"));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Proveedor>) result.get("proveedores")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of obtiene method, of class ProveedorDao.
     */
    @Test
    public void debieraObtenerProveedor() {
        log.debug("Debiera obtener proveedor");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        currentSession().save(empresa);
        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        instance.crea(proveedor);
        Long id = proveedor.getId();
        Proveedor result = instance.obtiene(id);
        assertEquals("tst-01", result.getNombre());
    }

    /**
     * Test of crea method, of class ProveedorDao.
     */
    @Test
    public void debieraCrearProveedor() {
        log.debug("Debiera crear proveedor");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        proveedor = instance.crea(proveedor, usuario);
        assertNotNull(proveedor);
        assertNotNull(proveedor.getId());
        assertEquals("tst-01", proveedor.getNombre());
    }

    /**
     * Test of actualiza method, of class ProveedorDao.
     */
    @Test
    public void debieraActualizarProveedor() {
        log.debug("Debiera actualizar proveedor");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        proveedor = instance.crea(proveedor, usuario);
        assertNotNull(proveedor);
        assertNotNull(proveedor.getId());
        assertEquals("tst-01", proveedor.getNombre());

        proveedor.setNombre("PRUEBA");
        instance.actualiza(proveedor, usuario);

        Proveedor prueba = instance.obtiene(proveedor.getId());
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getNombre());
    }

    /**
     * Test of elimina method, of class ProveedorDao.
     */
    @Test
    public void debieraEliminarProveedor() throws Exception {
        log.debug("Debiera actualizar proveedor");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Proveedor proveedor = new Proveedor("tst-01", "test-01", "test-00000001", empresa);
        proveedor = instance.crea(proveedor, usuario);
        assertNotNull(proveedor);
        assertNotNull(proveedor.getId());
        assertEquals("tst-01", proveedor.getNombre());

        String nombre = instance.elimina(proveedor.getId());
        assertNotNull(nombre);
        assertEquals("tst-01", nombre);

        Proveedor prueba = instance.obtiene(proveedor.getId());
        assertNull(prueba);
    }
}
