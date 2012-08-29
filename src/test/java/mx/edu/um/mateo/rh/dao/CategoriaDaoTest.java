/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.rh.model.Categoria;
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
 * @author develop
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

    /**
     * Test of obtiene method, of class CategoriaDao.
     */
    @Test
    public void testObtiene() {
        System.out.println("obtiene");
        Long id = 1L;
        List<Categoria> lista = insertaCategorias(1);

        Map<String, Object> params = null;
        Map result = instance.getCategorias(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_CATEGORIAS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(1, ((List<Empresa>) result.get(Constantes.CONTAINSKEY_CATEGORIAS)).size());
        assertEquals(1, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
        
    }

    /**
     * Test of crea method, of class CategoriaDao.
     */
    @Test
    public void testCrea() {
        System.out.println("crea");
        List<Categoria> lista = insertaCategorias(1);
        Map<String, Object> params = null;
        Map result = instance.getCategorias(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_CATEGORIAS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(1, ((List<Empresa>) result.get(Constantes.CONTAINSKEY_CATEGORIAS)).size());
        assertEquals(1, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of crea method, of class CategoriaDao.
     */
    @Test
    public void testActualiza() {
        log.debug("Deberia actualizar categorias");
        List<Categoria> lista = insertaCategorias(1);
        log.debug("lista"+lista.size());
        Categoria categoria = lista.get(0);
        assertNotNull(categoria);
        categoria.setNombre("Juan1");

        instance.saveCategoria(categoria);
        log.debug("categorias >>" + categoria);
        assertEquals("Juan1", categoria.getNombre());
    }

    private List<Categoria> insertaCategorias(Integer i) {
        log.debug("Insertar categoria");
        List<Categoria> lista = new ArrayList();
        Categoria categoria = null;
        for (int j = 0; j < i; j++) {
            categoria = new Categoria();
            categoria.setNombre("prueba" + j);
            currentSession().save(categoria);
            log.debug(categoria.toString());
            lista.add(categoria);
        }
        return lista;
    }
}
