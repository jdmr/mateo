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
 *
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.*;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.colportor.test.BaseTest;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class UnionDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(UnionDaoTest.class);
    @Autowired
    private UnionDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraMostrarListaDeUniones() {
        log.debug("Debiera mostrar lista de uniones");
        for (int i = 0; i < 20; i++) {
            Union union = new Union("tst-" + i);
            union.setStatus(Constantes.STATUS_ACTIVO);
            currentSession().save(union);
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_UNIONES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Union>) result.get(Constantes.CONTAINSKEY_UNIONES)).size());
        //assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    //PRUEBA PASA 100%
    public void debieraObtenerUnion() {
        log.debug("Debiera obtener union");
        Union union = new Union("tst-01");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        Long id = union.getId();

        Union result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals("tst-01", result.getNombre());
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraCrearUnion() {
        log.debug("Debiera crear union");
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
        currentSession().flush();
        Long id = usuario.getId();
        assertNotNull(id);
        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        union = new Union("tst-01");
        union = instance.crea(union, usuario);
        assertNotNull(union.getId());
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraActualizarUnion() {
        log.debug("Debiera actualizar union");
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
        currentSession().flush();
        Long id = usuario.getId();
        assertNotNull(id);

        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        Union result = instance.obtiene(union.getId());
        assertNotNull(result);
        result.setNombre("PRUEBA");
        instance.actualiza(result);

        Union prueba = instance.obtiene(union.getId());
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getNombre());
    }

    @Test
    //PRUEBA PASO 100%
    public void debieraEliminarUnion() {
        log.debug("Debiera eliminar union");
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
        id = union.getId();
        currentSession().refresh(asociacion);
        currentSession().refresh(union);

        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        union = new Union("TEST02");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);

        String nombre = instance.elimina(id);
        assertEquals("test", nombre);

        union = instance.obtiene(id);
        if (union.getStatus() != Constantes.STATUS_INACTIVO) {
            fail("Fallo  prueba Eliminar");
        }
    }
}