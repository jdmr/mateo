/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.rh.model.PerDed;
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
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class PerDedDaoTest {

    @Autowired
    private PerDedDao instance;
    private static final Logger log= LoggerFactory.getLogger(CategoriaDaoTest.class);
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session currentSession(){
        return sessionFactory.getCurrentSession();
    }
    
     @Test
     public void testObtenerListaDePerDed() {     
        log.debug("Muestra lista de perdeds");
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa( "test01","test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        for(int i=0; i<5; i++){
            
            
            PerDed perded = new PerDed();
            
            
            perded.setNombre("Categoria"+i);
            perded.setStatus("A");
            perded.setEmpresa(empresa);
            instance.graba(perded, null);
           assertNotNull(perded.getId());
        }
         Map<String, Object> params= new HashMap<>();
         params.put("empresa",empresa.getId());
         Map<String, Object> result=instance.lista(params);
        
        //assertNotNull((List)params.get(Constantes.CATEGORIA_LIST));
        
        
    }

     
    @Test
    public void testObtiene() {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        PerDed perded = new PerDed();
        perded.setNombre("Test1");
        perded.setStatus("AC");
        perded.setEmpresa(empresa);
        currentSession().save(perded);
        
        PerDed perded1= instance.obtiene(perded.getId());
        assertEquals(perded.getId(),perded1.getId());
        
       
    }
    @Test
    public void testGraba() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa);
        PerDed perded= new PerDed();
        perded.setNombre("Test1");
        perded.setStatus("AC");
        perded.setEmpresa(empresa);
        currentSession().save(perded);
        
        assertNotNull(perded.getId());
        assertEquals(perded.getNombre(),"Test1");
        assertEquals(perded.getStatus(), "AC");
        
        
        
    }
    @Test
    public void testElimina() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-02", "test-03", "000000000001", organizacion);
        currentSession().save(empresa); 
        PerDed perded= new PerDed();
        perded.setNombre("Test1");
        perded.setStatus("AC");
        perded.setEmpresa(empresa);
        currentSession().save(perded);
        currentSession().delete(perded);
        
        assertNotNull(perded.getId());
        assertEquals(perded.getNombre(),"Test1");
        assertEquals(perded.getStatus(), "AC");
        
    }
}
