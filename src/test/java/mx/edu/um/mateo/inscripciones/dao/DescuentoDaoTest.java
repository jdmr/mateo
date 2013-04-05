/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.HashMap;
import static org.junit.Assert.*;
import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.inscripciones.model.Descuento;
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
 * @author zorch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class DescuentoDaoTest {
    @Autowired
    private DescuentoDao instance;
    private static final Logger log= LoggerFactory.getLogger(DescuentoDaoTest.class);
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session currentSession(){
        return sessionFactory.getCurrentSession();
    }
    
    
    @Test
     public void testObtenerListaDeDescuentos() {     
        log.debug("Muestra lista de descuentos");
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
                for(int i=0; i<5; i++){         
            
                Descuento descuento=new Descuento();
            
            
                descuento.setDescripcion("Descuento"+i);
                descuento.setStatus("A");
                descuento.setContabiliza("S");
                
            
                instance.graba(descuento, organizacion);
                assertNotNull(descuento.getId());
        }
         Map<String, Object> params= new HashMap<>();
         params.put("organizacion",organizacion.getId());
         Map<String, Object> result=instance.lista(params);
        
    }
    
    @Test
    public void testObtiene() {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Descuento descuento= new Descuento();
        descuento.setDescripcion("Descuento1");
        descuento.setStatus("A");
        descuento.setContabiliza("S");
        instance.graba(descuento, organizacion);
        
        Descuento descuento1= instance.obtiene(descuento.getId());
        assertEquals(descuento.getId(),descuento1.getId());
        
       
    }
    
    @Test
    public void testGraba() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Descuento descuento= new Descuento();
        descuento.setDescripcion("Descuento");
        descuento.setStatus("A");
        descuento.setContabiliza("S");
        instance.graba(descuento, organizacion);
        
        assertNotNull(descuento.getId());
        assertEquals(descuento.getDescripcion(),"Descuento");
        assertEquals(descuento.getStatus(), "A");
        assertEquals(descuento.getContabiliza(), "S");
    }
    @Test
    public void testElimina() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-02", "test-03");
        currentSession().save(organizacion);
        Descuento descuento= new Descuento("Descuento","A", "S", organizacion);
        currentSession().save(descuento);
        instance.elimina(descuento.getId());
        
       Descuento descuento1= instance.obtiene(descuento.getId());
       if (descuento1!=null){ 
           fail("no se borro el descuento");
       }
        
    }
}