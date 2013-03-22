/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.model.EmpleadoEstudios;
import mx.edu.um.mateo.rh.model.NivelEstudios;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
 * @author AMDA
 */
public class EmpleadoEstudiosDaoTest {
/*
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
    
    @Autowired
    private EmpleadoEstudiosDao instance;
    private static final Logger log= LoggerFactory.getLogger(EmpleadoEstudiosDaoTest.class);
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session currentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Test
     public void testObtenerListaDeCategorias() {     
        log.debug("Muestra lista de EmpleadoEstudios");
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa( "test01","test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password");        
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
        currentSession().save(empleado);
        for(int i=0; i<2; i++){
            
            
            EmpleadoEstudios empleadoEstudios=new EmpleadoEstudios();
            
            empleadoEstudios.setEmpleado(empleado);
            empleadoEstudios.setNombreEstudios("EmpleadoEstudios"+i);
            empleadoEstudios.setNivelEstudios(NivelEstudios.MAESTRIA);
            empleadoEstudios.setTitulado(false);
            empleadoEstudios.setFechaTitulacion(new Date());
            empleadoEstudios.setStatus("A");
            empleadoEstudios.setUserCaptura(null);
            empleadoEstudios.setFechaCaptura(new Date());
            instance.graba(empleadoEstudios);
           assertNotNull(empleadoEstudios.getId());
        }
         Map<String, Object> params= new HashMap<>();
         params.put("empresa",empresa.getId());
         params.put("empleado",empleado);
         Map<String, Object> result=instance.lista(params);
        
        //assertNotNull((List)params.get(Constantes.CATEGORIA_LIST));
        
        
    }

     
    @Test
    public void testObtiene() {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password");
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
        currentSession().save(empleado);
        EmpleadoEstudios empleadoEstudios= new EmpleadoEstudios();
        empleadoEstudios.setEmpleado(empleado);
        empleadoEstudios.setNombreEstudios("EmpleadoEstudios");
        empleadoEstudios.setNivelEstudios(NivelEstudios.SECUNDARIA);
        empleadoEstudios.setTitulado(false);
        empleadoEstudios.setFechaTitulacion(new Date());
        empleadoEstudios.setStatus("A");
        empleadoEstudios.setUserCaptura(null);
        empleadoEstudios.setFechaCaptura(new Date());
        currentSession().save(empleadoEstudios);
        
        EmpleadoEstudios empleadoEstudios1= instance.obtiene(empleadoEstudios.getId());
        assertEquals(empleadoEstudios.getId(),empleadoEstudios1.getId());
        
       
    }
    @Test
    public void testGraba() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password");
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
        currentSession().save(empleado);
        EmpleadoEstudios empleadoEstudios= new EmpleadoEstudios();
        empleadoEstudios.setEmpleado(empleado);
        empleadoEstudios.setNombreEstudios("EmpleadoEstudios");
        empleadoEstudios.setNivelEstudios(NivelEstudios.PREPARATORIA);
        empleadoEstudios.setTitulado(false);
        empleadoEstudios.setFechaTitulacion(new Date());
        empleadoEstudios.setStatus("A");
        empleadoEstudios.setUserCaptura(null);
        empleadoEstudios.setFechaCaptura(new Date());
        currentSession().save(empleadoEstudios);
        
        assertNotNull(empleadoEstudios.getId());
        assertEquals(empleadoEstudios.getNombreEstudios(),"EmpleadoEstudios");
        assertEquals(empleadoEstudios.getStatus(), "A");
        
        
        
    }
    @Test
    public void testElimina() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa); 
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Empleado empleado = new Empleado( "test", "apPaterno","apMaterno","correo@um.edu.mx","username","1080506", Boolean.TRUE,"M", "Direccion","A",
            "curp","RFCSTRI", "Cuenta", "imss",
            10, 1,new BigDecimal (1),"SI", "ife","A",
            "padre", "madre", "A", "conyuge",Boolean.FALSE, Boolean.TRUE, "iglesia",
                "responsabilidad","password");
        empleado.setAlmacen(almacen);
        empleado.setEmpresa(empresa);
        empleado.setRoles(roles);
        currentSession().save(empleado);
        EmpleadoEstudios empleadoEstudios= new EmpleadoEstudios();
        empleadoEstudios.setEmpleado(empleado);
        empleadoEstudios.setNombreEstudios("EmpleadoEstudios");
        empleadoEstudios.setNivelEstudios(NivelEstudios.LICENCIATURA);
        empleadoEstudios.setTitulado(false);
        empleadoEstudios.setFechaTitulacion(new Date());
        empleadoEstudios.setStatus("A");
        empleadoEstudios.setUserCaptura(null);
        empleadoEstudios.setFechaCaptura(new Date());
        currentSession().save(empleadoEstudios);
        String tmp = instance.elimina(empleadoEstudios.getId());
        
        assertEquals(empleadoEstudios.getNombreEstudios(),tmp);

        
        try{
            empleadoEstudios = instance.obtiene(empleadoEstudios.getId());
            //fail("no borro el empleado estudios");
        } catch(Exception e){
            
        }
        
    }
    */
}
