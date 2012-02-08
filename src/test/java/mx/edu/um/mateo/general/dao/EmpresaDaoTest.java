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
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.Almacen;
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
public class EmpresaDaoTest {

    private static final Logger log = LoggerFactory.getLogger(EmpresaDaoTest.class);
    @Autowired
    private EmpresaDao instance;
    @Autowired
    private OrganizacionDao organizacionDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UsuarioDao usuarioDao;
    
    public EmpresaDaoTest() {
    }

    /**
     * Test of lista method, of class EmpresaDao.
     */
    @Test
    public void debieraMostrarListaDeEmpresas() {
        log.debug("Debiera mostrar lista de empresas");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        organizacion = organizacionDao.crea(organizacion);
        for (int i = 0; i < 19; i++) {
            Empresa empresa = new Empresa("test" + i, "test" + i, "test" + i, organizacion);
            instance.crea(empresa);
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
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
        organizacion = organizacionDao.crea(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        instance.crea(empresa);
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
        organizacion = organizacionDao.crea(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        Long almacenId = 0l;
        actualizaUsuario:
        for (Empresa empresa : organizacion.getEmpresas()) {
            for (Almacen almacen : empresa.getAlmacenes()) {
                almacenId = almacen.getId();
                break actualizaUsuario;
            }
        }
        usuario = usuarioDao.crea(usuario, almacenId, new String[]{rol.getAuthority()});
        Long id = usuario.getId();
        assertNotNull(id);

        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
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
        organizacion = organizacionDao.crea(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        Long almacenId = 0l;
        actualizaUsuario:
        for (Empresa empresa : organizacion.getEmpresas()) {
            for (Almacen almacen : empresa.getAlmacenes()) {
                almacenId = almacen.getId();
                break actualizaUsuario;
            }
        }
        usuario = usuarioDao.crea(usuario, almacenId, new String[]{rol.getAuthority()});
        Long id = usuario.getId();
        assertNotNull(id);

        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        empresa = instance.crea(empresa, usuario);
        assertNotNull(empresa);
        assertNotNull(empresa.getId());
        assertEquals("tst-01", empresa.getCodigo());
        
        empresa.setNombre("PRUEBA");
        instance.actualiza(empresa, usuario);
        
        Empresa prueba = instance.obtiene(empresa.getId());
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
        organizacion = organizacionDao.crea(organizacion);
        Rol rol = new Rol("ROLE_TEST");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        Long almacenId = 0l;
        actualizaUsuario:
        for (Empresa empresa : organizacion.getEmpresas()) {
            for (Almacen almacen : empresa.getAlmacenes()) {
                almacenId = almacen.getId();
                break actualizaUsuario;
            }
        }
        usuario = usuarioDao.crea(usuario, almacenId, new String[]{rol.getAuthority()});
        Long id = usuario.getId();
        assertNotNull(id);

        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        empresa = instance.crea(empresa, usuario);
        assertNotNull(empresa);
        assertNotNull(empresa.getId());
        assertEquals("tst-01", empresa.getCodigo());

        String nombre = instance.elimina(empresa.getId());
        assertNotNull(nombre);
        assertEquals("test-01", nombre);
        
        Empresa prueba = instance.obtiene(empresa.getId());
        assertNull(prueba);
    }
}
