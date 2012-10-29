/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.rh.model.Puesto;
import mx.edu.um.mateo.rh.model.Seccion;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    private PuestoDao instance;
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class PuestoDao.
     */
    @SuppressWarnings("unchecked")
	@Test
    public void debieraMostrarListaDePuestos() {
        log.debug("Debiera mostrar lista de puestos");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Seccion seccion = new Seccion();
        seccion.setCategoriaId("1");
        seccion.setMaximo(Float.NaN);
        seccion.setMinimo(Float.NaN);
        seccion.setNombre("nombre");
        seccion.setRangoAcademico(Float.NaN);
        currentSession().save(seccion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        for (Integer i = 0; i < 20; i++) {
            Puesto puesto = new Puesto("test" + i.toString(), i, seccion, i, i, "a", empresa);
            instance.graba(puesto, null);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", empresa.getId());
        Map<String, Object> result = instance.lista(params);
//        assertNotNull(result.get("puestos"));
//        assertNotNull(result.get("cantidad"));
//        assertEquals(10, ((List<Puesto>) result.get("puestos")).size());
//        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    /**
     * Test of obtiene method, of class PuestoDao.
     */
//    @Test
//    public void debieraObtenerPuesto() {
//        log.debug("Debiera obtener puesto");
//        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
//        currentSession().save(organizacion);
//        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
//        currentSession().save(empresa);
//        Puesto puesto = new Puesto("tst-01", "test-01", "test-00000001", empresa);
//        instance.graba(puesto);
//        Long id = puesto.getId();
//        Puesto result = instance.obtiene(id);
//        assertEquals("tst-01", result.getNombre());
//    }

    /**
     * Test of graba method, of class PuestoDao.
     */
//    @Test
//    public void debieraCrearPuesto() {
//        log.debug("Debiera crear puesto");
//        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
//        currentSession().save(organizacion);
//        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
//        currentSession().save(empresa);
//        Rol rol = new Rol("ROLE_TEST");
//        currentSession().save(rol);
//        Set<Rol> roles = new HashSet<>();
//        roles.add(rol);
//        Almacen almacen = new Almacen("TST", "TEST", empresa);
//        currentSession().save(almacen);
//        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
//        usuario.setEmpresa(empresa);
//        usuario.setAlmacen(almacen);
//        usuario.setRoles(roles);
//        currentSession().save(usuario);
//        Long id = usuario.getId();
//        assertNotNull(id);
//
//        Puesto puesto = new Puesto("tst-01", "test-01", "test-00000001", empresa);
//        puesto = instance.graba(puesto, usuario);
//        assertNotNull(puesto);
//        assertNotNull(puesto.getId());
//        assertEquals("tst-01", puesto.getNombre());
//    }

    /**
     * Test of actualiza method, of class PuestoDao.
     */
//    @Test
//    public void debieraActualizarPuesto() {
//        log.debug("Debiera actualizar puesto");
//        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
//        currentSession().save(organizacion);
//        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
//        currentSession().save(empresa);
//        Rol rol = new Rol("ROLE_TEST");
//        currentSession().save(rol);
//        Set<Rol> roles = new HashSet<>();
//        roles.add(rol);
//        Almacen almacen = new Almacen("TST", "TEST", empresa);
//        currentSession().save(almacen);
//        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
//        usuario.setEmpresa(empresa);
//        usuario.setAlmacen(almacen);
//        usuario.setRoles(roles);
//        currentSession().save(usuario);
//        Long id = usuario.getId();
//        assertNotNull(id);
//
//        Puesto puesto = new Puesto("tst-01", "test-01", "test-00000001", empresa);
//        puesto = instance.graba(puesto, usuario);
//        assertNotNull(puesto);
//        assertNotNull(puesto.getId());
//        assertEquals("tst-01", puesto.getNombre());
//
//        puesto.setNombre("PRUEBA");
//        instance.actualiza(puesto, usuario);
//
//        Puesto prueba = instance.obtiene(puesto.getId());
//        assertNotNull(prueba);
//        assertEquals("PRUEBA", prueba.getNombre());
//    }

    /**
     * Test of elimina method, of class PuestoDao.
     */
//    @Test
//    public void debieraEliminarPuesto() throws Exception {
//        log.debug("Debiera actualizar puesto");
//        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
//        currentSession().save(organizacion);
//        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
//        currentSession().save(empresa);
//        Rol rol = new Rol("ROLE_TEST");
//        currentSession().save(rol);
//        Set<Rol> roles = new HashSet<>();
//        roles.add(rol);
//        Almacen almacen = new Almacen("TST", "TEST", empresa);
//        currentSession().save(almacen);
//        Usuario usuario = new Usuario("bugs@um.edu.mx", "TEST-01", "TEST-01", "TEST-01");
//        usuario.setEmpresa(empresa);
//        usuario.setAlmacen(almacen);
//        usuario.setRoles(roles);
//        currentSession().save(usuario);
//        Long id = usuario.getId();
//        assertNotNull(id);
//
//        Puesto puesto = new Puesto("tst-01", "test-01", "test-00000001", empresa);
//        puesto = instance.graba(puesto, usuario);
//        assertNotNull(puesto);
//        assertNotNull(puesto.getId());
//        assertEquals("tst-01", puesto.getNombre());
//
//        String nombre = instance.elimina(puesto.getId());
//        assertNotNull(nombre);
//        assertEquals("tst-01", nombre);
//
//        Puesto prueba = instance.obtiene(puesto.getId());
//        assertNull(prueba);
//    }
}
