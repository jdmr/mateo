/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.*;
import mx.edu.um.mateo.Constants;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.rh.model.Puesto;
import mx.edu.um.mateo.rh.model.Seccion;
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
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class PuestoDaoTest {

    private static final Logger log = LoggerFactory.getLogger(PuestoDaoTest.class);
    @Autowired
    private SeccionDao seccionDao;
    @Autowired
    private PuestoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class PuestoDao.
     */
//    @SuppressWarnings("unchecked")
//    @Test
//    public void debieraMostrarListaDePuestos() {
//        log.debug("Debiera mostrar lista de puestos");
//        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
//        currentSession().save(organizacion);
//        Seccion seccion = null;
//        for (int i = 0; i < 5; i++) {
//            seccion = new Seccion();
//            seccion.setId((long) i);
//            seccion.setCategoriaId("1" + i);
//            seccion.setMaximo(Float.NaN);
//            seccion.setMinimo(Float.NaN);
//            seccion.setNombre("nombre" + i);
//            seccion.setRangoAcademico(Float.NaN);
//            currentSession().save(seccion);
//        }
//        Seccion seccion2 = seccionDao.obtiene((long) 3);
//        seccionDao.graba(seccion2);
//        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
//        currentSession().save(empresa);
//        Almacen almacen = new Almacen("TST", "TEST", empresa);
//        currentSession().save(almacen);
//        Usuario usuario = new Usuario("usaername", "password", "nombre", "apellido");
//        usuario.setAlmacen(almacen);
//        usuario.setEmpresa(empresa);
//        currentSession().save(usuario);
//        for (Integer i = 0; i < 20; i++) {
//            Puesto puesto = new Puesto("descripcion" + i, seccion2, empresa);
//            puesto.setCategoria(i);
//            puesto.setMaximo(i);
//            puesto.setMinimo(i);
//            puesto.setRangoAcademico(Double.NaN);
//            puesto.setStatus("a");
//            instance.graba(puesto, usuario);
//        }
//        Map<String, Object> params = new HashMap<>();
//        params.put("empresa", empresa.getId());
//        Map<String, Object> result = instance.lista(params);
//        assertNotNull(result.get(Constants.PUESTO_LIST));
//        assertNotNull(result.get("cantidad"));
//        assertEquals(10, ((List<Puesto>) result.get(Constants.PUESTO_LIST)).size());
//        assertEquals(20, ((Long) result.get("cantidad")).intValue());
//    }

    /**
     * Test of obtiene method, of class PuestoDao.
     */
    @Test
    public void debieraObtenerPuesto() {
        log.debug("Debiera obtener puesto");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Seccion seccion = new Seccion();
        seccion.setId(new Long(1));
        seccion.setCategoriaId("x");
        seccion.setMaximo(Float.NaN);
        seccion.setMinimo(Float.NaN);
        seccion.setNombre("nombre");
        seccion.setRangoAcademico(Float.NaN);
        currentSession().save(seccion);
        currentSession().save(seccion);

         Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Puesto puesto = new Puesto("tst-01", seccion, empresa);
        int i = 8;
        puesto.setCategoria(i);
        puesto.setMaximo(i);
        puesto.setMinimo(i);
        puesto.setRangoAcademico(Double.NaN);
        puesto.setStatus("a");
        Usuario usuario = new Usuario("usaername", "password", "nombre");
        usuario.setAlmacen(almacen);
        usuario.setEmpresa(empresa);
        currentSession().save(usuario);
        instance.graba(puesto, usuario);
        Long id = puesto.getId();
        Puesto result = instance.obtiene(id);
        assertEquals("tst-01", result.getDescripcion());

    }

    /**
     * Test of graba method, of class PuestoDao.
     */
    @Test
    public void debieraCrearPuesto() {
        log.debug("Debiera crear puesto");
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
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        Seccion seccion = new Seccion();
        seccion.setId(new Long(1));
        seccion.setCategoriaId("x");
        seccion.setMaximo(Float.NaN);
        seccion.setMinimo(Float.NaN);
        seccion.setNombre("nombre");
        seccion.setRangoAcademico(Float.NaN);
        currentSession().save(seccion);
        assertNotNull(id);

        Puesto puesto = new Puesto("tst-01", seccion, empresa);
        int i=4;
        puesto.setCategoria(i);
            puesto.setMaximo(i);
            puesto.setMinimo(i);
            puesto.setRangoAcademico(Double.NaN);
            puesto.setStatus("a");
        puesto = instance.graba(puesto, usuario);
        assertNotNull(puesto);
        assertNotNull(puesto.getId());
        assertEquals("tst-01", puesto.getDescripcion());
    }

    /**
     * Test of actualiza method, of class PuestoDao.
     */
    @Test
    public void debieraActualizarPuesto() {
        log.debug("Debiera actualizar puesto");
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
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        
        Seccion seccion = new Seccion();
        seccion.setId(new Long(1));
        seccion.setCategoriaId("x");
        seccion.setMaximo(Float.NaN);
        seccion.setMinimo(Float.NaN);
        seccion.setNombre("nombre");
        seccion.setRangoAcademico(Float.NaN);
        currentSession().save(seccion);
        Long id = usuario.getId();
        assertNotNull(id);

        Puesto puesto = new Puesto("tst-01", seccion, empresa);
        int i=4;
        puesto.setCategoria(i);
            puesto.setMaximo(i);
            puesto.setMinimo(i);
            puesto.setRangoAcademico(Double.NaN);
            puesto.setStatus("a");
        puesto = instance.graba(puesto, usuario);
        assertNotNull(puesto);
        assertNotNull(puesto.getId());
        assertEquals("tst-01", puesto.getDescripcion());

        puesto.setDescripcion("PRUEBA");
        instance.graba(puesto, usuario);

        Puesto prueba = instance.obtiene(puesto.getId());
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getDescripcion());
    }

    /**
     * Test of elimina method, of class PuestoDao.
     */
    @Test
    public void debieraEliminarPuesto() throws Exception {
        log.debug("Debiera actualizar puesto");
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
        
        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        
        Seccion seccion = new Seccion();
        seccion.setId(new Long(1));
        seccion.setCategoriaId("x");
        seccion.setMaximo(Float.NaN);
        seccion.setMinimo(Float.NaN);
        seccion.setNombre("nombre");
        seccion.setRangoAcademico(Float.NaN);
        currentSession().save(seccion);
        Long id = usuario.getId();
        assertNotNull(id);

        Puesto puesto = new Puesto("tst-01", seccion, empresa);
        int i = 8;
        puesto.setCategoria(i);
        puesto.setMaximo(i);
        puesto.setMinimo(i);
        puesto.setRangoAcademico(Double.NaN);
        puesto.setStatus("a");
        puesto = instance.graba(puesto, usuario);
        assertNotNull(puesto);
        assertNotNull(puesto.getId());
        assertEquals("tst-01", puesto.getDescripcion());

        String nombre = instance.elimina(puesto.getId());
        assertNotNull(nombre);
        assertEquals("tst-01", nombre);

        Puesto prueba = instance.obtiene(puesto.getId());
        assertNull(prueba);
    }
}
