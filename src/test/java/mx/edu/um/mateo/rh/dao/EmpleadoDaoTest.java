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
package mx.edu.um.mateo.rh.dao;

import java.math.BigDecimal;
import java.util.*;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.model.TipoEmpleado;
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
 * @author Omar Soto <osoto@um.edu.mx>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class EmpleadoDaoTest {

    private static final Logger log = LoggerFactory.getLogger(EmpleadoDaoTest.class);
    @Autowired
    private EmpleadoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class EmpleadoDao.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void debieraMostrarListaDeEmpleados() {
        log.debug("Debiera mostrar lista de empleados");
       Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id); 
        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setOrganizacion(organizacion);
        tipoEmpleado.setDescripcion("Denominacional");
        tipoEmpleado.setPrefijo("980");
        currentSession().save(tipoEmpleado);
        
        for (int i = 0; i < 20; i++) {

            Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo"+i+"@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password", tipoEmpleado);
            empleado.setAlmacen(almacen);
            empleado.setEmpresa(empresa);
            empleado.setRoles(roles);
            instance.graba(empleado);
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", empresa.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.EMPLEADO_LIST));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Empleado>) result.get(Constantes.EMPLEADO_LIST)).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of obtiene method, of class EmpleadoDao.
     */
    @Test
    public void debieraObtenerEmpleado() {
        log.debug("Debiera obtener empleado");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");  
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setOrganizacion(organizacion);
        tipoEmpleado.setDescripcion("Denominacional");
        tipoEmpleado.setPrefijo("980");
        currentSession().save(tipoEmpleado);

        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password", tipoEmpleado);
        
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
        instance.graba(empleado);
        Empleado result = instance.obtiene(empleado.getId());
        assertEquals("test", result.getNombre());
    }

    /**
     * Test of crea method, of class EmpleadoDao.
     */
    @Test
    public void debieraCrearEmpleado() {
        log.debug("Debiera crear empleado");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setOrganizacion(organizacion);
        tipoEmpleado.setDescripcion("Denominacional");
        tipoEmpleado.setPrefijo("980");
        currentSession().save(tipoEmpleado);

        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password", tipoEmpleado);
        
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
        instance.graba(empleado);
        assertNotNull(empleado);
        assertNotNull(empleado.getId());
        assertEquals("test", empleado.getNombre());
    }

    /**
     * Test of actualiza method, of class EmpleadoDao.
     */
    @Test
    public void debieraActualizarEmpleado() {
        log.debug("Debiera actualizar empleado");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);

        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setOrganizacion(organizacion);
        tipoEmpleado.setDescripcion("Denominacional");
        tipoEmpleado.setPrefijo("980");
        currentSession().save(tipoEmpleado);

        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password", tipoEmpleado);
        
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
        instance.graba(empleado);
        assertNotNull(empleado);
        assertNotNull(empleado.getId());
        assertEquals("test", empleado.getNombre());

        empleado.setNombre("PRUEBA");
        instance.graba(empleado, usuario);

        Empleado prueba = instance.obtiene(empleado.getId());
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getNombre());
    }

    /**
     * Test of elimina method, of class EmpleadoDao.
     */
    @Test
    public void debieraEliminarEmpleado() throws Exception {
        log.debug("Debiera eliminar empleado");
        log.debug("El empleado no debe eliminar");
        
    }
}
