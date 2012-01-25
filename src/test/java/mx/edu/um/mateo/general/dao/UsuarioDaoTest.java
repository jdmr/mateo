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

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
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
@ContextConfiguration(locations = {"classpath:mateo.xml"})
@Transactional
public class UsuarioDaoTest {

    private static final Logger log = LoggerFactory.getLogger(UsuarioDaoTest.class);
    @Autowired
    private UsuarioDao instance;
    @Autowired
    private RolDao rolDao;

    public UsuarioDaoTest() {
    }

    /**
     * Test of lista method, of class UsuarioDao.
     */
    @Test
    public void debieraObtenerListaDeUsuarios() {
        log.debug("Debiera obtener lista de usuarios");

        Rol rol = new Rol("ROLE_TEST");
        rol = rolDao.crea(rol);
        for (int i = 0; i < 20; i++) {
            Usuario usuario = new Usuario("test-" + i, "test-" + i, "TEST " + i, "TEST", "test" + i + "@test.com", rol);
            instance.crea(usuario);
        }

        Map<String, Object> params = null;
        Map<String, Object> result = instance.lista(params);
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
        Rol rol = new Rol("ROLE_TEST");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("test-01", "test-01", "TEST1", "TEST", "test01@test.com", rol);
        usuario = instance.crea(usuario);
        Long id = usuario.getId();

        Usuario result = instance.obtiene(id);
        assertEquals(usuario, result);
        assertEquals("ROLE_TEST", result.getAuthorities().get(0).getAuthority());
    }

    /**
     * Test of crea method, of class UsuarioDao.
     */
    @Test
    public void debieraCrearUsuario() {
        log.debug("Debiera crear usuario");
        Rol rol = new Rol("ROLE_TEST");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("test-01", "test-01", "TEST1", "TEST", "test01@test.com", rol);
        usuario = instance.crea(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
    }

    /**
     * Test of actualiza method, of class UsuarioDao.
     */
    @Test
    public void debieraActualizarUsuario() {
        log.debug("Debiera actualizar usuario");
        Rol rol = new Rol("ROLE_TEST");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("test-01", "test-01", "TEST1", "TEST", "test01@test.com", rol);
        usuario = instance.crea(usuario);
        Long id = usuario.getId();

        Usuario result = instance.obtiene(id);
        assertEquals(usuario, result);
        
        result.setNombre("PRUEBA");
        instance.actualiza(result);
        
        Usuario prueba = instance.obtiene(id);
        assertEquals(result.getNombre(), prueba.getNombre());
    }

    /**
     * Test of elimina method, of class UsuarioDao.
     */
    @Test
    public void debieraEliminarUsuario() {
        log.debug("Debiera actualizar usuario");
        Rol rol = new Rol("ROLE_TEST");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("test-01", "test-01", "TEST1", "TEST", "test01@test.com", rol);
        usuario = instance.crea(usuario);
        Long id = usuario.getId();

        String nombre = instance.elimina(id);
        assertEquals(usuario.getUsername(), nombre);
        
        Usuario result = instance.obtiene(id);
        assertNull(result);
    }
}
