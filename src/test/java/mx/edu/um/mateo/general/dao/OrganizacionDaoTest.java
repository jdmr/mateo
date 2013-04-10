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
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
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
import org.springframework.security.core.GrantedAuthority;
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
public class OrganizacionDaoTest extends BaseDaoTest {

    private static final Logger log = LoggerFactory.getLogger(OrganizacionDaoTest.class);
    @Autowired
    private OrganizacionDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Test
    public void debieraMostrarListaDeOrganizaciones() {
        log.debug("Debiera mostrar lista de organizaciones");
        for (int i = 0; i < 20; i++) {
            Organizacion organizacion = new Organizacion("tst-" + i, "test-" + i, "test-" + i);
            currentSession().save(organizacion);
        }
        Map<String, Object> params = null;
        Map<String, Object> result = instance.lista(params);
        @SuppressWarnings("unchecked")
		List<Organizacion> organizaciones = (List<Organizacion>) result.get("organizaciones");
        Long cantidad = (Long) result.get("cantidad");
        assertEquals(10, organizaciones.size());
        assertTrue(20 <= cantidad);
    }

    @Test
    public void debieraObtenerOrganizacion() {
        log.debug("Debiera obtener organizacion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Long id = organizacion.getId();

        Organizacion result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals("tst-01", result.getCodigo());
    }

    @Test
    public void debieraCrearOrganizacion() {
        log.debug("Debiera crear organizacion");
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
        currentSession().flush();
        Long id = usuario.getId();
        assertNotNull(id);

        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        organizacion = new Organizacion("tst-01", "test-01", "test-01");
        organizacion = instance.crea(organizacion, usuario);
        assertNotNull(organizacion.getId());
    }

    @Test
    public void debieraActualizarOrganizacion() {
        log.debug("Debiera actualizar organizacion");
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
        currentSession().flush();
        Long id = usuario.getId();
        assertNotNull(id);
        id = organizacion.getId();

        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        Organizacion result = instance.obtiene(id);
        assertNotNull(result);
        result.setNombre("PRUEBA");
        instance.actualiza(result);

        Organizacion prueba = instance.obtiene(id);
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getNombre());
    }

    @Test
    public void debieraEliminarOrganizacion() throws UltimoException {
        log.debug("Debiera eliminar organizacion");

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
        id = organizacion.getId();
        currentSession().refresh(empresa);
        currentSession().refresh(organizacion);

        organizacion = new Organizacion("TEST02", "TEST02", "TEST02");
        currentSession().save(organizacion);

        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        String nombre = instance.elimina(id);
        assertEquals("TEST01", nombre);

        Organizacion prueba = instance.obtiene(id);
        assertNull(prueba);
    }
}
