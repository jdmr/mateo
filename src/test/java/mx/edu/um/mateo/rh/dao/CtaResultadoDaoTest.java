/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.rh.model.Ejercicio;
import org.slf4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import mx.edu.um.mateo.rh.model.CtaResultado;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional

public class CtaResultadoDaoTest extends BaseTest{
    
    private static final Logger log = LoggerFactory.getLogger(CtaResultadoDaoTest.class);
    @Autowired
    private CtaResultadoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * Test of obtiene method, of class CtaResultadoDao.
     */
    @Test
    public void deberiaMostrarListaDeCtaResultado() {
        log.debug("Debiera mostrar lista de ctaResultado");
        Ejercicio ejercicio = new Ejercicio("test", "A");
        currentSession().save(ejercicio);
        assertNotNull(ejercicio);
        log.debug("ejercicio >>" + ejercicio);
        for (int i = 0; i < 20; i++) {
            CtaResultado ctaResultado = new CtaResultado("test" + i, "test");
            currentSession().save(ctaResultado);
            assertNotNull(ctaResultado);
            log.debug("ctaResultado>>" + ctaResultado);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params); 
        assertNotNull(result.get("ctaResultado"));
        assertNotNull(result.get("cantidad"));

        assertEquals(10, ((List<Empresa>) result.get("ctaResultado")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }
}
