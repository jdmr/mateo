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
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.UltimoException;
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
public class AlmacenDaoTest {

    private static final Logger log = LoggerFactory.getLogger(AlmacenDaoTest.class);
    @Autowired
    private AlmacenDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class AlmacenDao.
     */
    @SuppressWarnings("unchecked")
	@Test
    public void debieraMostrarListaDeAlmacenes() {
        log.debug("Debiera mostrar lista de almacenes");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        for (int i = 0; i < 20; i++) {
            Almacen almacen = new Almacen("TST"+i, "test" + i, empresa);
            currentSession().save(almacen);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("empresa", empresa.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get("almacenes"));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Almacen>) result.get("almacenes")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of obtiene method, of class AlmacenDao.
     */
    @Test
    public void debieraObtenerAlmacen() {
        log.debug("Debiera obtener almacen");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "test-01", empresa);
        currentSession().save(almacen);
        Long id = almacen.getId();
        assertNotNull(id);
        Almacen result = instance.obtiene(id);
        assertEquals("test-01", result.getNombre());
    }

    /**
     * Test of crea method, of class AlmacenDao.
     */
    @Test
    public void debieraCrearAlmacen() {
        log.debug("Debiera crear almacen");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen prueba = new Almacen("TST", "TEST01", empresa);
        currentSession().save(prueba);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(prueba);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Almacen almacen = new Almacen("TST1", "tst-01", empresa);
        almacen = instance.crea(almacen, usuario);
        assertNotNull(almacen);
        assertNotNull(almacen.getId());
        assertEquals("tst-01", almacen.getNombre());
    }

    /**
     * Test of actualiza method, of class AlmacenDao.
     */
    @Test
    public void debieraActualizarAlmacen() {
        log.debug("Debiera actualizar almacen");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen prueba = new Almacen("TST", "TEST01", empresa);
        currentSession().save(prueba);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(prueba);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        id = prueba.getId();
        assertNotNull(id);

        log.debug("Obteniendo almacen");
        Almacen almacen = instance.obtiene(id);
        log.debug("Modificando nombre");
        almacen.setNombre("PRUEBA");
        log.debug("Enviando a actualizar almacen");
        instance.actualiza(almacen, usuario);

        log.debug("Obteniendo almacen nuevamente");
        Almacen prueba2 = instance.obtiene(almacen.getId());
        log.debug("Haciendo asserts");
        assertNotNull(prueba2);
        assertEquals("PRUEBA", prueba2.getNombre());
    }

    /**
     * Test of elimina method, of class AlmacenDao.
     */
    @Test(expected=UltimoException.class)
    public void noDebieraEliminarAlmacen() throws Exception {
        log.debug("Debiera actualizar almacen");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen prueba = new Almacen("TST", "TEST01", empresa);
        currentSession().save(prueba);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(prueba);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        id = prueba.getId();
        assertNotNull(id);

        String nombre = instance.elimina(id, empresa.getId());
        assertNotNull(nombre);
        assertEquals("TEST01", nombre);

        Almacen prueba2 = instance.obtiene(id);
        assertNull(prueba2);
    }
    
    @Test
    public void debieraEliminarAlmacen() throws Exception {
        log.debug("Debiera actualizar almacen");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen prueba = new Almacen("TST1", "TEST01", empresa);
        currentSession().save(prueba);
        Almacen prueba2 = new Almacen("TST2", "TEST02", empresa);
        currentSession().save(prueba2);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(prueba);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        id = prueba.getId();
        assertNotNull(id);

        String nombre = instance.elimina(id, empresa.getId());
        assertNotNull(nombre);
        assertEquals("TEST01", nombre);

        Almacen almacen = instance.obtiene(id);
        assertNull(almacen);
    }
}
