package mx.edu.um.mateo.inscripciones.dao;

import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Periodo;
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
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class PeriodoDaoTest {
    
     private static final Logger log = LoggerFactory.getLogger(PeriodoDaoTest.class);
     @Autowired
     private PeriodoDao instance;
     @Autowired
     private SessionFactory sessionFactory;
     
     private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
     
    @SuppressWarnings("unchecked") 
    @Test
    public void debieraMostrarListaDePeriodo() {
        log.debug("Debiera mostrar lista de periodo");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        for (int i = 0; i < 20; i++) {
            Periodo periodo = new Periodo("test" + i, "A" , "clave", new Date(), new Date());
            periodo.setOrganizacion(organizacion);
            instance.graba(periodo);
        }
        Map<String, Object> params = new HashMap<>();
     
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_PERIODOS));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Periodo>) result.get(Constantes.CONTAINSKEY_PERIODOS)).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }
    
    @Test
    public void debieraObtenerPeriodo() {
        log.debug("Debiera obtener Periodo");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Periodo periodo = new Periodo("test", "A", "clave", new Date(), new Date());
        periodo.setOrganizacion(organizacion);
        currentSession().save(periodo);
        Long id = periodo.getId();
        Periodo result = instance.obtiene(id);
        assertEquals("test", result.getDescripcion());
    }
    
     @Test
    public void debieraCrearPeriodo() {
        log.debug("Debiera crear periodo");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Periodo periodo = new Periodo("test", "A", "clave", new Date(), new Date());
        periodo.setOrganizacion(organizacion);
        instance.graba(periodo);
        assertNotNull(periodo.getId());
        assertEquals("test", periodo.getDescripcion());
    }
     
      @Test
    public void debieraActualizarPeriodo() {
        log.debug("Debiera actualizar periodo");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Periodo periodo = new Periodo("test", "A", "clave", new Date(), new Date());
        periodo.setOrganizacion(organizacion);
        instance.graba(periodo);
        assertNotNull(periodo.getId());

        periodo.setDescripcion("PRUEBA");
        instance.graba(periodo);

        Periodo prueba = instance.obtiene(periodo.getId());
        assertNotNull(prueba);
        assertEquals("PRUEBA", prueba.getDescripcion());
    }   
}
