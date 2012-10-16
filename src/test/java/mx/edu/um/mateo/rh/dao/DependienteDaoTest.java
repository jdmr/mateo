/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.model.Dependiente;
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
 * @author AMDA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class DependienteDaoTest {

//    @Autowired
//    private DependienteDao instance;
//    private static final Logger log = LoggerFactory.getLogger(DependienteDaoTest.class);
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    private Session currentSession() {
//        return sessionFactory.getCurrentSession();
//    }
//
//    @Test
//    public void testObtenerListaDeDependientes() {
//       
//        
//        for (int i = 0; i < 5; i++) {
//            Dependiente dependiente = new Dependiente();
//            dependiente.setNombre("a"+i);
//            dependiente.setFechaNacimiento(new Date());
//            dependiente.setEstudios("abcdfc"+i);
//            dependiente.setGrado(1);
//            dependiente.setStatus("B");
//            instance.graba(dependiente);
//            //assertNotNull(c.getId());
//        }
//         Map<String, Object> params = new TreeMap<String, Object>();
//        
//        Map<String, Object> result = instance.lista(params);
//      
//        //assertNotNull((List)params.get(Constantes.CONCEPTO_LIST));
//        //assertEquals(5,((List)params.get(Constantes.CONCEPTO_LIST)).size());
//
//    }
//
//    @Test
//    public void testObtieneDependiente() throws ObjectRetrievalFailureException {
//
//        Dependiente dependiente = new Dependiente();
//        dependiente.setNombre("a");
//        dependiente.setFechaNacimiento(new Date());
//        dependiente.setEstudios("abc");
//        dependiente.setGrado(1);
//        dependiente.setStatus("B");
//        instance.graba(dependiente);
//        
//        Dependiente dependiente1 = instance.obtiene(dependiente.getId());
//        //assertEquals(concepto.getId(), concepto1.getId());
//
//    }
//
//    @Test
//    public void testGrabaDependiente() {
//        Dependiente dependiente = new Dependiente();
//        dependiente.setNombre("a");
//        dependiente.setFechaNacimiento(new Date());
//        dependiente.setEstudios("abc");
//        dependiente.setGrado(1);
//        dependiente.setStatus("B");
//        instance.graba(dependiente);
//        //assertNotNull(dependiente.getId());
//               
//    }
//
//    @Test
//    public void testEliminaDependiente() {
//        Dependiente dependiente = new Dependiente();
//        dependiente.setNombre("a");
//        dependiente.setFechaNacimiento(new Date());
//        dependiente.setEstudios("abc");
//        dependiente.setGrado(1);
//        dependiente.setStatus("B");
//        currentSession().save(dependiente);
//        instance.elimina(dependiente.getId());
//        
//           
//          
//    }
}


