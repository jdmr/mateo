/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class PaqueteDaoTest {

    private static final Logger log = LoggerFactory.getLogger(PaqueteDaoTest.class);
    @Autowired
    private PaqueteDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public PaqueteDaoTest() {
    }

    /**
     * Prueba la lista de paquetes
     */
    @Test
    public void testLista() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        
        Paquete paquete=null;
        for (int i = 0; i < 20; i++) {
            paquete = new Paquete();
            paquete.setAcfe("s");
            paquete.setDescripcion("test");
            paquete.setEnsenanza(new BigDecimal("1.2"));
            paquete.setInternado(new BigDecimal("2.2"));
            paquete.setMatricula(new BigDecimal("3.3"));
            paquete.setNombre("Test");
            instance.crea(paquete, usuario);
            assertNotNull(paquete.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", empresa.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_PAQUETES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TiposBecas>) result.get(Constantes.CONTAINSKEY_PAQUETES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Prueba el obtener un paquete
     */
    @Test
    public void testObtiene() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new BigDecimal("1.2"));
        paquete.setInternado(new BigDecimal("2.2"));
        paquete.setMatricula(new BigDecimal("3.3"));
        paquete.setNombre(nombre);
        paquete.setEmpresa(empresa);
        currentSession().save(paquete);
        assertNotNull(paquete.getId());
        
        Paquete paquete1 = instance.obtiene(paquete.getId());
        assertEquals(paquete.getNombre(), paquete1.getNombre());

    }

    /**
     * Prueba el proceso de creacion del paquete
     */
    @Test
    public void testCrea() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new BigDecimal("1.2"));
        paquete.setInternado(new BigDecimal("2.2"));
        paquete.setMatricula(new BigDecimal("3.3"));
        paquete.setNombre(nombre);
        paquete.setEmpresa(empresa);
        instance.crea(paquete, usuario);
        assertNotNull(paquete.getId());
        
        Paquete paquete1 = instance.obtiene(paquete.getId());
        assertEquals(paquete.getNombre(), paquete1.getNombre());
    }
    
    /**
     * Prueba el proceso de actualizacion
     */
    @Test
    public void testActualiza() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno", "apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new BigDecimal("1.2"));
        paquete.setInternado(new BigDecimal("2.2"));
        paquete.setMatricula(new BigDecimal("3.3"));
        paquete.setNombre(nombre);
        paquete.setEmpresa(empresa);
        instance.crea(paquete, usuario);
        assertNotNull(paquete.getId());
        
        Paquete paquete1 = instance.obtiene(paquete.getId());
        assertEquals(paquete.getNombre(), paquete1.getNombre());
        
        paquete1.setDescripcion("test2");
        instance.actualiza(paquete1, usuario);
        
        currentSession().refresh(paquete);
        assertEquals("test2", paquete.getDescripcion());
    }

    /**
     * Prueba el eliminar el paquete
     */
    @Test
    public void testRemove() {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new BigDecimal("1.2"));
        paquete.setInternado(new BigDecimal("2.2"));
        paquete.setMatricula(new BigDecimal("3.3"));
        paquete.setNombre(nombre);
        paquete.setEmpresa(empresa);
        currentSession().save(paquete);
        assertNotNull(paquete.getId());
        
        String descripcion = instance.elimina(paquete.getId());
        assertEquals(paquete.getDescripcion(), descripcion);
        
        try{
            Paquete paquete1 = instance.obtiene(paquete.getId());        
            fail("Se encontro paquete "+paquete1);
        }catch(ObjectRetrievalFailureException e){
            log.debug("Se elimino con exito el paquete {}", descripcion);
        }
    }
}
