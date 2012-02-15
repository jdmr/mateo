/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.dao.EmpresaDao;
import mx.edu.um.mateo.general.dao.EmpresaDaoTest;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.rh.model.CtaMayor;
import mx.edu.um.mateo.rh.model.Ejercicio;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nujev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class CtaMayorDaoTest {

    private static final Logger log = LoggerFactory.getLogger(EmpresaDaoTest.class);
    @Autowired
    private EmpresaDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class CtaMayorDao.
     */
    @Test
    public void deberiaMostrarListaDeCtaMayor() {
        log.debug("Debiera mostrar lista de ctaMayor");
        Ejercicio ejercicio = new Ejercicio("test", "test");
        for (int i = 0; i < 20; i++) {
            CtaMayor ctaMayor = new CtaMayor(ejercicio, "test", "test");
            currentSession().save(ctaMayor);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get("ctaMayor"));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Empresa>) result.get("ctaMayor")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }
}
