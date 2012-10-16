/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao.impl;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.rh.dao.SeccionDao;
import mx.edu.um.mateo.rh.model.Seccion;
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
public class SeccionDaoHibernateTest {
 
    @Autowired
    private SeccionDao seccionDao;
    private static final Logger log = LoggerFactory.getLogger(SeccionDaoHibernateTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public SeccionDaoHibernateTest() {
    }
    
    /**
     * Test of lista method, of class ColegioDaoHibernate.
     */
    @Test
    public void testLista() {
        log.debug("lista");
        Map<String, Object> params = null;
        for (int i = 0; i <= 5; i++) {
        Seccion seccion = new Seccion();
            seccion.setNombre("Nombre" +i);
            seccion.setMaximo(new Float (5));
            seccion.setMinimo(new Float (2));
            seccion.setCategoriaId("CategoriaId");
            seccion.setRangoAcademico(new Float (1) );
            seccionDao.grabaSeccion(seccion);
            assertNotNull(seccion);
        }

        params = seccionDao.Lista(params);
//        assertNotNull((List) params.get(Constantes.CONTAINSKEY_COLEGIOS));
        assertEquals(6, ((List) params.get(Constantes.CONTAINSKEY_SECCIONES)).size());

    }
    /**
     * Test of getColegio method, of class ColegioDaoHibernate.
     */
    @Test
    public void testGetSeccion() {
        System.out.println("getSeccion");
        Seccion seccion = new Seccion();
        seccion.setNombre("Nombre");
            seccion.setMaximo(new Float (5));
            seccion.setMinimo(new Float (2));
            seccion.setCategoriaId("CategoriaId");
            seccion.setRangoAcademico(new Float (1) );
            seccionDao.grabaSeccion(seccion);
        currentSession().save(seccion);
        
        Seccion seccion1 = seccionDao.getSeccion(seccion.getId());
        assertEquals(seccion1.getId(), seccion.getId()); 
        //assertEquals(concepto.getId(), concepto1.getId());
    }

    /**gr
     * Test of saveColegio method, of class ColegioDaoHibernate.
     */
    @Test
    public void testGrabaSeccion() {
        System.out.println("grabaSeccion");
        Seccion seccion = new Seccion();
        seccion.setNombre("Nombre");
        seccion.setMaximo(new Float (5));
            seccion.setMinimo(new Float (2));
            seccion.setCategoriaId("CategoriaId");
            seccion.setRangoAcademico(new Float (1) );
        seccionDao.grabaSeccion(seccion);
        assertNotNull(seccion.getId());
    }

    /**
     * Test of updateColegio method, of class ColegioDaoHibernate.
     */
    @Test
    public void testUpdateSeccion() {
        System.out.println("updateSeccion");
        Seccion seccion = new Seccion();
        seccion.setNombre("Nombre");
        seccion.setMaximo(new Float (5));
            seccion.setMinimo(new Float (2));
            seccion.setCategoriaId("CategoriaId");
            seccion.setRangoAcademico(new Float (1) );
        currentSession().save(seccion);
        String nombre="Nombre2";
        seccion.setNombre(nombre);
        seccionDao.grabaSeccion(seccion);
        assertEquals(nombre, seccion.getNombre());
    }

    /**
     * Test of removeColegio method, of class ColegioDaoHibernate.
     */
        @Test
    public void testRemoveSeccion() {
        System.out.println("removeSeccion");
        Seccion seccion = new Seccion();
        seccion.setNombre("Nombre");
        seccion.setMaximo(new Float (5));
            seccion.setMinimo(new Float (2));
            seccion.setCategoriaId("CategoriaId");
            seccion.setRangoAcademico(new Float (1) );
        currentSession().save(seccion);
        try {
            seccionDao.removeSeccion(seccion.getId());
        } catch (ObjectRetrievalFailureException ex) {
            log.error("No existe concepto");
        }
        try {
            seccion = seccionDao.getSeccion(seccion.getId());
            //fail("Fallo al eliminar");
        } catch (ObjectRetrievalFailureException ex) {
            log.info("Elimino con exito");
        }
    }
}