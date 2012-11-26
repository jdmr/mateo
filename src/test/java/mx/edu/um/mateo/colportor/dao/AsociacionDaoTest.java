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
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class AsociacionDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(AsociacionDaoTest.class);
    @Autowired
    private AsociacionDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class AsociacionDao.
     */
    @Test
    //PRUEBA PASO 100%
    public void debieraMostrarListaDeAsociaciones() {
        log.debug("Debiera mostrar lista de asociaciones");
        Union union = new Union("tst-01");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        for (int i = 0; i < 20; i++) {
            Asociacion asociacion = new Asociacion("tst-01", Constantes.STATUS_ACTIVO, union);
            currentSession().save(asociacion);
        }

        Map<String, Object> params = new HashMap<>();
        params.put(Constantes.ADDATTRIBUTE_UNION, union.getId());
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_ASOCIACIONES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Asociacion>) result.get(Constantes.CONTAINSKEY_ASOCIACIONES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class AsociacionDao.
     */
    @Test
    //PRUEBA PASO 100%
    public void debieraObtenerAsociacion() {
        log.debug("Debiera obtener asociacion");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Long id = asociacion.getId();
        assertNotNull(id);
        Asociacion result = instance.obtiene(id);
        assertEquals("test-01", result.getNombre());
    }

    /**
     * Test of crea method, of class AsociacionDao.
     */
    @Test
    //PRUEBA PASO 100%
    public void debieraCrearAsociacion() {
        log.debug("Debiera crear asociacion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen= new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Union union = new Union("TEST01");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol(Constantes.ROLE_TEST);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("tst-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        asociacion = instance.crea(asociacion, usuario);
        assertNotNull(asociacion);
        assertNotNull(asociacion.getId());
        assertEquals("test", asociacion.getNombre());
    }

    /**
     * Test of actualiza method, of class AsociacionDao.
     */
    @Test
    //PRUEBA PASO 100%
    public void debieraActualizarAsociacion() {
        log.debug("Debiera actualizar asociacion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen= new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Union union = new Union("TEST01");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol(Constantes.ROLE_TEST);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("tst-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        log.debug("Obteniendo asociacion");
        Asociacion result = instance.obtiene(asociacion.getId());
        assertNotNull(result);
        log.debug("Modificando nombre");
        result.setNombre("PRUEBA");
        log.debug("Enviando a actualizar asociacion");
        instance.actualiza(result, usuario);

        log.debug("Obteniendo asociacion nuevamente");
        Asociacion prueba = instance.obtiene(asociacion.getId());
        log.debug("Haciendo asserts");
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getNombre());
    }

    /**
     * Test of elimina method, of class AsociacionDao.
     */
    @Test
    //PRUEBA PASO 100%
    public void debieraEliminarAsociacion() {
        log.debug("Debiera actualizar asociacion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen= new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Union union = new Union("TEST01");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol(Constantes.ROLE_TEST);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("tst-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setAsociacion(asociacion);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        id = asociacion.getId();
        currentSession().refresh(asociacion);
        currentSession().refresh(union);

        asociacion = new Asociacion("tst-02", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));

        String nombre = instance.elimina(id, union.getId());
        assertNotNull(nombre);
        assertEquals("tst-01", nombre);

        asociacion = instance.obtiene(id);
        if (asociacion.getStatus() != Constantes.STATUS_INACTIVO) {
            fail("Fallo prueba Eliminar");
        }
    }
}
