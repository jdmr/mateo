/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import mx.edu.um.mateo.inscripciones.model.ccobro.Alumno;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CCobroAlumnoDaoHibernate {
    
    private static final Logger log = LoggerFactory.getLogger(AlumnoDaoHibernateTest.class);
   
    	
    
    
   @Test
    public void testObtenerNombre() throws Exception{
    
        log.debug("Obtiene un nombre");
        Alumno instance= new Alumno();
        String matricula= "1080506";
        instance.getNombre(matricula);
        assertEquals("DZUL ESCOBAR JORGE LUIS",instance.getNombre());
    }
   
   @Test
    public void testObtenerAlumno() throws Exception{
    
        log.debug("Obtiene un Alumno");
        Alumno instance= new Alumno();
        String matricula= "1080506";
        instance.getAlumno(matricula);
        assertEquals("MEX",instance.getNacionalidad());
    }
      
}
