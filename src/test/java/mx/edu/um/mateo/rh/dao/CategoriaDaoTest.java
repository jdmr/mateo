/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import mx.edu.um.mateo.rh.model.Categoria;
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
 * @author zorch
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional

public class CategoriaDaoTest {
    private static final Logger log = LoggerFactory.getLogger(CategoriaDaoTest.class);
    @Autowired
    private CategoriaDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    
    public CategoriaDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getCategorias method, of class CategoriaDao.
     */
    @Test
    public void testGetCategorias() {
        System.out.println("getCategorias");
        Categoria categoria= null;
        for(int i=0; i<10; i++){
        categoria = new Categoria();
        categoria.setNombre("prueba"+i);
        instance.saveCategoria(categoria);
        }
        List result = instance.getCategorias(categoria);
        assertEquals(10, result.size());
    }

    /**
     * Test of getCategoria method, of class CategoriaDao.
     */
    @Test
    public void testGetCategoria() {
        System.out.println("getCategoria");
        Categoria categoria= null;
        for(int i=0; i<10; i++){
        categoria = new Categoria();
        categoria.setNombre("prueba"+i);
        instance.saveCategoria(categoria);
        }

        Categoria result= instance.getCategoria(categoria.getId());
        assertNotNull(result);
    }

    /**
     * Test of saveCategoria method, of class CategoriaDao.
     */
    @Test
    public void testSaveCategoria() {
        System.out.println("saveCategoria");
        Categoria categoria= null;
        for(int i=0; i<10; i++){
        categoria = new Categoria();
        categoria.setNombre("prueba"+i);
        instance.saveCategoria(categoria);
        }
        assertNotNull(categoria.getId());
        
    }

    /**
     * Test of removeCategoria method, of class CategoriaDao.
     */
    @Test
    public void testRemoveCategoria() {
        System.out.println("getCategoria");
        Categoria categoria= null;
        
        categoria = new Categoria();
        categoria.setNombre("prueba");
        instance.saveCategoria(categoria);
        

        Categoria result= instance.getCategoria(categoria.getId());
        instance.removeCategoria(categoria.getId());
        try{
        result= instance.getCategoria(categoria.getId());
        fail("No borro la categoria");
        }catch(ObjectRetrievalFailureException e){
            ;//todo bien
        }
        
        
        
    }

    
}
