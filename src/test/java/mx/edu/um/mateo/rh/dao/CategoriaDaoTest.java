/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.rh.model.Categoria;
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
 * @author Zorch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class CategoriaDaoTest {
    @Autowired
    private CategoriaDao instance;
    private static final Logger log= LoggerFactory.getLogger(CategoriaDaoTest.class);
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session currentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Test
     public void testObtenerListaDeCategorias() {     
        log.debug("Muestra lista de categorias");
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa( "test01","test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        for(int i=0; i<5; i++){
            
            
            Categoria categoria=new Categoria();
            
            
            categoria.setNombre("Categoria"+i);
            categoria.setStatus("AC");
            categoria.setEmpresa(empresa);
            instance.graba(categoria, null);
            //assertNotNull(c.getId());
        }
         Map<String, Object> params= new HashMap<>();
         params.put("empresa",empresa.getId());
         Map<String, Object> result=instance.lista(params);
        
        //assertNotNull((List)params.get(Constantes.CATEGORIA_LIST));
        //assertEquals((List)params.get(Constantes.CATEGORIA_LIST).size);
        
    }

     
    @Test
    public void testObtiene() {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        Categoria categoria= new Categoria();
        categoria.setNombre("Test1");
        categoria.setStatus("AC");
        categoria.setEmpresa(empresa);
        currentSession().save(categoria);
        
        Categoria categoria1= instance.obtiene(categoria.getId());
        //assertEquals(categoria.getId(),categoria1.getId());
        
       
    }
    @Test
    public void testGraba() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        Categoria categoria= new Categoria();
        categoria.setNombre("Test1");
        categoria.setStatus("AC");
        categoria.setEmpresa(empresa);
        currentSession().save(categoria);
        
        //assertNotNull(categoria.getId());
        //assertEquals(categoria.getNombre(),"Test1");
        //assertEquals(categoria.getStatus(), "AC");
        
        
        
    }
    @Test
    public void testElimina() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa); 
        Categoria categoria= new Categoria();
        categoria.setNombre("Test1");
        categoria.setStatus("AC");
        categoria.setEmpresa(empresa);
        currentSession().save(categoria);
        currentSession().delete(categoria);
        
//        assertNotNull(categoria.getId());
        //assertEquals(categoria.getNombre(),"Test1");
        //assertEquals(categoria.getStatus(), "AC");
        
    }
}

