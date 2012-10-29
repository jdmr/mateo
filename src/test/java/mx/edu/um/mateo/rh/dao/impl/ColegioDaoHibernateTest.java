/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.rh.dao.ColegioDao;
import mx.edu.um.mateo.rh.model.Colegio;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.*;
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
 * @author IrasemaBalderas
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ColegioDaoHibernateTest {
 
    @Autowired
    private ColegioDao colegioDao;
    private static final Logger log = LoggerFactory.getLogger(ColegioDaoHibernateTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public ColegioDaoHibernateTest() {
    }

    /**
     * Test of lista method, of class ColegioDaoHibernate.
     */
    @Test
    public void Lista() {
        log.debug("lista");
        Map<String, Object> params = null;
        for (int i = 0; i <= 5; i++) {
        Colegio colegio = new Colegio();
            colegio.setNombre("Nombre");
            colegio.setStatus("A"+i);
            colegioDao.grabaColegio(colegio);
            assertNotNull(colegio);
        }

        params = colegioDao.lista(params);
//        assertNotNull((List) params.get(Constantes.CONTAINSKEY_COLEGIOS));
        assertEquals(6, ((List) params.get(Constantes.CONTAINSKEY_COLEGIOS)).size());

    }

    /**
     * Test of getColegio method, of class ColegioDaoHibernate.
     */
    @Test
    public void obtiene() {
        System.out.println("getColegio");
        Colegio colegio = new Colegio();
        colegio.setNombre("Nombre");
        colegio.setStatus("A");
        currentSession().save(colegio);
        
        Colegio colegio1 = colegioDao.getColegio(colegio.getId());
        assertEquals(colegio1.getId(), colegio.getId()); 
        //assertEquals(concepto.getId(), concepto1.getId());
    }

    /**
     * Test of saveColegio method, of class ColegioDaoHibernate.
     */
    @Test
    public void graba() {
        System.out.println("grabaColegio");
        Colegio colegio = new Colegio();
        colegio.setNombre("Nombre");
        colegio.setStatus("A");
        colegioDao.grabaColegio(colegio);
        assertNotNull(colegio.getId());
    }

    /**
     * Test of updateColegio method, of class ColegioDaoHibernate.
     */
    @Test
    public void testUpdateColegio() {
        System.out.println("updateColegio");
        Colegio colegio = new Colegio();
        colegio.setNombre("Nombre");
        colegio.setStatus("A");
        currentSession().save(colegio);
        String nombre="Nombre2";
        colegio.setNombre(nombre);
        colegioDao.grabaColegio(colegio);
        assertEquals(nombre, colegio.getNombre());
    }

    /**
     * Test of removeColegio method, of class ColegioDaoHibernate.
     */
    @Test
    public void elimina() {
        System.out.println("removeColegio");
        Colegio colegio = new Colegio();
        colegio.setNombre("Nombre");
        colegio.setStatus("A");
        currentSession().save(colegio);
        try {
            String nombre = colegioDao.removeColegio(colegio.getId());
        } catch (ObjectRetrievalFailureException ex) {
            log.error("No existe concepto");
        }
        try {
            colegio = colegioDao.getColegio(colegio.getId());
            //fail("Fallo al eliminar");
        } catch (ObjectRetrievalFailureException ex) {
            log.info("Elimino con exito");
        }
    }
}
