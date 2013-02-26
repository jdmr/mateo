/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertTrue; 
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import static org.junit.Assert.assertNotNull;
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
public class AlumnoDaoHibernateTest {
    
    private static final Logger log = LoggerFactory.getLogger(AlumnoDaoHibernateTest.class);
    @Autowired
    private AlumnoDao instance;
    	
    
    
    @Test
    public void testObtenerListaDeAlumnos() {
        log.debug("Obtiene lista de Alumnos");
        Map<String, Object> params = new HashMap<>();
        params.put("filtro", "DZUL");
        log.debug("{}",instance);
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.ALUMNO_LIST));
        java.util.List lista= (java.util.List) result.get(Constantes.ALUMNO_LIST);
        for(int i= 0; i<lista.size();i ++){
            Alumno alumno=(Alumno) lista.get(i);
            log.debug("{}",alumno);
            assertTrue((alumno.getApMaterno()).equals("DZUL")|| alumno.getApPaterno().equals("DZUL") || alumno.getNombre().equals("DZUL"));
            
        }
    }

    @Test
    public void testObtenerAlumno(){
    
        log.debug("Obtiene un Alumno");
        String matricula= "1080506";
        Alumno alumno= instance.obtiene(matricula);
        assertNotNull(alumno);
    }

}