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

import java.util.*;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.apache.commons.lang.StringUtils;
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
 * @author jdmr
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class UsuarioDaoTest {

    private static final Logger log = LoggerFactory.getLogger(UsuarioDaoTest.class);
    @Autowired
    private UsuarioDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    public UsuarioDaoTest() {
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class UsuarioDao.
     */
    @Test
    public void debieraObtenerListaDeUsuarios() {
        log.debug("Debiera obtener lista de usuarios");

        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST01", empresa);
        currentSession().save(almacen);

        for (int i = 0; i < 20; i++) {
           Usuario usuario = new Usuario("bugs"+i+"@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
            usuario.setEmpresa(empresa);
            usuario.setAlmacen(almacen);
            usuario.setRoles(roles);
            currentSession().save(usuario);
        }

        Map<String, Object> params = null;
        Map<String, Object> result = instance.lista(params);
        @SuppressWarnings("unchecked")
		List<Usuario> usuarios = (List<Usuario>) result.get("usuarios");
        Long cantidad = (Long) result.get("cantidad");
        assertEquals(10, usuarios.size());
        assertTrue(20 <= cantidad);
    }

    /**
     * Test of obtiene method, of class UsuarioDao.
     */
    @Test
    public void debieraObtenerUsuario() {
        log.debug("Debiera obtener usuario");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST01", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Usuario result = instance.obtiene(id);
        assertEquals(usuario, result);
        assertTrue(result.getRoles().contains(rol));
        assertEquals("bugs@um.edu.mx",usuario.getUsername());
    }

    /**
     * Test of crea method, of class UsuarioDao.
     */
    @Test
    public void debieraCrearUsuario() {
        log.debug("Debiera crear usuario");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Empresa empresa = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST01", empresa);
        currentSession().save(almacen);
        EjercicioPK ejercicioPK = new EjercicioPK("TEST", organizacion);
        Byte x = new Byte("0");
        Ejercicio ejercicio = new Ejercicio(ejercicioPK, "TEST", "A", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, x, x);
        currentSession().save(ejercicio);
        currentSession().flush();

        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEjercicio(ejercicio);
        usuario = instance.crea(usuario, almacen.getId(), new String[]{rol.getAuthority()});
        Long id = usuario.getId();
        assertNotNull(id);
    }

    /**
     * Test of actualiza method, of class UsuarioDao.
     */
    @Test
    public void debieraActualizarUsuario() {
        log.debug("Debiera actualizar usuario");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST01", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        usuario.setNombre("PRUEBA");
        instance.actualiza(usuario, almacen.getId(), new String[]{rol.getAuthority()});

        Usuario prueba = instance.obtiene(id);
        assertEquals(usuario.getNombre(), prueba.getNombre());
    }

    @Test
    public void debieraCambiarRolDeUsuario() {
        log.debug("Debiera actualizar usuario");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Rol rol2 = new Rol("ROLE_TEST2");
        currentSession().save(rol2);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST01", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Usuario result = instance.obtiene(id);
        assertEquals(usuario, result);
        assertTrue(result.getRoles().contains(rol));

        result.setNombre("PRUEBA");
        instance.actualiza(result, almacen.getId(), new String[]{rol2.getAuthority()});

        Usuario prueba = instance.obtiene(id);
        assertEquals(result.getNombre(), prueba.getNombre());
        assertTrue(result.getRoles().contains(rol2));
    }

    /**
     * Test of elimina method, of class UsuarioDao.
     */
    @Test(expected = UltimoException.class)
    public void noDebieraEliminarUsuario() throws UltimoException {
        log.debug("Debiera actualizar usuario");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST01", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        instance.elimina(id);
        fail("Debio lanzar la excepcion de ultimo usuario");
    }

    /**
     * Test of elimina method, of class UsuarioDao.
     */
    @Test
    public void debieraEliminarUsuario() throws UltimoException {
        log.debug("Debiera actualizar usuario");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST01", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        usuario = new Usuario("test-02@test.com", "test-02", "TEST2", "TEST2","TEST2");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        String nombre = instance.elimina(id);
        assertEquals(usuario.getUsername(), nombre);

        Usuario result = instance.obtiene(id);
        assertNull(result);
    }

    @SuppressWarnings("unchecked")
	@Test
    public void debieraMostrarLosUsuariosFiltradosPorEmpresa() {
        log.debug("Mostrar los usuarios filtrados por empresa");
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Empresa empresa = new Empresa("TEST01", "TEST01", "TEST01", "000000000001", organizacion);
        currentSession().save(empresa);
        Empresa empresa2 = new Empresa("TEST02", "TEST02", "TEST02", "000000000001", organizacion);
        currentSession().save(empresa2);
        Almacen almacen = new Almacen("TST1", "TEST01", empresa);
        currentSession().save(almacen);
        Almacen almacen2 = new Almacen("TST2", "TEST02", empresa);
        currentSession().save(almacen2);

        for (int i = 0; i < 20; i++) {
            Usuario usuario = new Usuario("bugs"+i+"@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
            usuario.setEmpresa(empresa);
            usuario.setAlmacen(almacen);
            usuario.setRoles(roles);
            currentSession().save(usuario);
        }

        for (int i = 20; i < 50; i++) {
            Usuario usuario = new Usuario("bugs"+i+"@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
            usuario.setEmpresa(empresa2);
            usuario.setAlmacen(almacen2);
            usuario.setRoles(roles);
            currentSession().save(usuario);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("empresa", empresa.getId());
        Map<String, Object> result = instance.lista(params);
        List<Usuario> usuarios = (List<Usuario>) result.get("usuarios");
        Long cantidad = (Long) result.get("cantidad");
        assertEquals(10, usuarios.size());
        assertTrue(20 <= cantidad);

        params.put("empresa", empresa2.getId());
        result = instance.lista(params);
        usuarios = (List<Usuario>) result.get("usuarios");
        cantidad = (Long) result.get("cantidad");
        assertEquals(10, usuarios.size());
        assertTrue(30 <= cantidad);
    }
}
