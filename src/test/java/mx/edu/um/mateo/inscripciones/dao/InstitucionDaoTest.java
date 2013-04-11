package mx.edu.um.mateo.inscripciones.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Institucion;
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
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class InstitucionDaoTest {

    private static final Logger log = LoggerFactory.getLogger(InstitucionDaoTest.class);
    @Autowired
    private InstitucionDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void debieraMostrarListaDeInstitucion() {
        log.debug("Debiera mostrar lista de institucion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Institucion institucion = null;
        for (int i = 0; i < 20; i++) {
            institucion = new Institucion();
            institucion.setNombre("Nombre-test");
            institucion.setPorcentaje(new BigDecimal("123"));
            institucion.setStatus("A");
            institucion.setOrganizacion(organizacion);
            instance.graba(institucion);
            assertNotNull(institucion.getId());
        }
        Map<String, Object> params = new HashMap<>();

        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_INSTITUCION));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Institucion>) result.get(Constantes.CONTAINSKEY_INSTITUCION)).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }

    @Test
    public void debieraObtenerInstitucion() {
        log.debug("Debiera obtener Institucion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Institucion institucion = new Institucion();
        institucion.setNombre("Institucion-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        instance.graba(institucion);
        assertNotNull(institucion.getId());
        Long id = institucion.getId();
        Institucion result = instance.obtiene(id);
        assertNotNull(result.getId());
        assertEquals("Institucion-test", result.getNombre());
    }

    @Test
    public void debieraCrearInstitucion() {
        log.debug("Debiera crear institucion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Institucion institucion = new Institucion();
        institucion.setNombre("Institucion-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        instance.graba(institucion);
        assertNotNull(institucion.getId());
        Long id = institucion.getId();
        Institucion result = instance.obtiene(id);
        assertEquals("Institucion-test", result.getNombre());
    }

    @Test
    public void debieraActualizarInstitucion() {
        log.debug("Debiera actualizar institucion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Institucion institucion = new Institucion();
        institucion.setNombre("Institucion-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        instance.graba(institucion);
        assertNotNull(institucion.getId());

        institucion.setNombre("PRUEBA");
        instance.graba(institucion);
        assertNotNull(institucion.getId());
        Institucion prueba = instance.obtiene(institucion.getId());
        assertNotNull(prueba.getId());
        assertEquals("PRUEBA", prueba.getNombre());
    }

    @Test
    public void debieraEliminarInstitucion() {
        log.debug("Debiera actualizar institucion");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Institucion institucion = new Institucion();
        institucion.setNombre("Institucion-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(organizacion);
        instance.graba(institucion);
        assertNotNull(institucion.getId());

        institucion.setNombre("PRUEBA");
        instance.graba(institucion);
        assertNotNull(institucion.getId());
        String nombre = instance.elimina(institucion.getId());
        assertEquals("PRUEBA", nombre);
    }
}
