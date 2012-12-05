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

/**
 * TODO problemas con type long: user
 */

package mx.edu.um.mateo.colportor.dao;

import java.util.*;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
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

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        for (int i = 0; i < 20; i++) {
            Usuario usuario = new Usuario("test" + i + "@test.com", "test", "test", "test", "test");
            usuario.setEmpresa(empresa);
            usuario.setAlmacen(almacen);
            usuario.setAsociacion(asociacion);
            usuario.setRoles(roles);
            currentSession().save(usuario);
        }

        Map<String, Object> params = new HashMap();
        params.put(Constantes.ADDATTRIBUTE_ASOCIACION, asociacion);
        Map<String, Object> result = instance.lista(params);
        List<Usuario> usuarios = (List<Usuario>) result.get(Constantes.CONTAINSKEY_USUARIOS);
        Long cantidad = (Long) result.get(Constantes.CONTAINSKEY_CANTIDAD);
        assertEquals(10, usuarios.size());
        assertTrue(20 <= cantidad);
    }
    
    /**
     * En caso de que params no lleve una asociacion se grese lista vacia de usuarios
     */
     
    @Test
    public void debieraObtenerListaVaciaDeUsuarios() {
        log.debug("Debiera obtener lista vacia de usuarios");

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        for (int i = 0; i < 20; i++) {
            Usuario usuario = new Usuario("test" + i + "@test.com", "test", "test", "test", "test");
            usuario.setEmpresa(empresa);
            usuario.setAlmacen(almacen);
            usuario.setAsociacion(asociacion);
            usuario.setRoles(roles);
            currentSession().save(usuario);
        }

        Map<String, Object> params = null;
        Map<String, Object> result = instance.lista(params);
        List<Usuario> usuarios = (List<Usuario>) result.get(Constantes.CONTAINSKEY_USUARIOS);
        Long cantidad = (Long) result.get(Constantes.CONTAINSKEY_CANTIDAD);
        assertEquals(0, usuarios.size());
        assertTrue(0 <= cantidad);
    }

    /**
     * Test of obtiene method, of class UsuarioDao.
     */
    @Test
    public void debieraObtenerUsuario() {
        log.debug("Debiera obtener usuario");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("test@test.com", "test", "test", "test", "test");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Usuario result = instance.obtiene(id);
        assertEquals(usuario, result);
        assertTrue(result.getRoles().contains(rol));
        assertEquals("test@test.com", usuario.getUsername());
    }

    /**
     * Test of crea method, of class UsuarioDao.
     */
    @Test
    public void debieraCrearUsuario() {
        log.debug("Debiera crear usuario");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Usuario usuario = new Usuario("test@test.com", "test", "test", "test", "test");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario = instance.crea(usuario, asociacion.getId(), new String[]{rol.getAuthority()});
        Long id = usuario.getId();
        assertNotNull(id);
    }

    /**
     * Test of actualiza method, of class UsuarioDao.
     */
    @Test
    public void debieraActualizarUsuario() {
        log.debug("Debiera actualizar usuario");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("test@test.com", "test", "test", "test", "test");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        usuario.setNombre("PRUEBA");
        instance.actualiza(usuario, asociacion.getId(), new String[]{rol.getAuthority()});

        Usuario prueba = instance.obtiene(id);
        assertEquals(usuario.getNombre(), prueba.getNombre());
    }

    @Test
    public void debieraCambiarRolDeUsuario() {
        log.debug("Debiera Cambiar Rol de usuario");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Rol rol2 = new Rol("ROLE_TEST2");
        currentSession().save(rol2);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Usuario usuario = new Usuario("test@test.com", "test", "test", "test", "test");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        Usuario result = instance.obtiene(id);
        assertEquals(usuario, result);
        assertTrue(result.getRoles().contains(rol));

        instance.actualiza(result, asociacion.getId(), new String[]{rol2.getAuthority()});

        assertTrue(result.getRoles().contains(rol2));
    }

    /**
     * No debiera borrar al usuario si este es el unico que existe en la db 
     */
    @Test(expected = UltimoException.class)
    public void noDebieraEliminarUsuario() throws UltimoException {
        log.debug("No Debiera Eliminar usuario");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("test@test.com", "test", "test", "test", "test");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        instance.elimina(id);
        assertNotNull("usuario eliminado", usuario.getId());
        fail("Debio lanzar la excepcion de ultimo usuario");
    }
 
    /**
     * Test of elimina method, of class UsuarioDao.
     */
    @Test
    public void debieraEliminarUsuario() throws UltimoException {
        log.debug("Debiera eliminar usuario");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("test@test.com", "test", "test", "test", "test");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        usuario = new Usuario("test2@test.com", "test", "test", "test", "test");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        String nombre = instance.elimina(id);
        assertEquals(usuario.getUsername(), nombre);

        Usuario result = instance.obtiene(id);
        if(result != null){
            fail("Fallo prueba Eliminar");
        }
    }

}
