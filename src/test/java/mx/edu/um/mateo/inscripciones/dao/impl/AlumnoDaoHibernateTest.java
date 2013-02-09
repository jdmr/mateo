/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.inscripciones.Alumno;
import mx.edu.um.mateo.inscripciones.AlumnoAcademico;
import mx.edu.um.mateo.inscripciones.Modalidad;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author zorch
 */
public class AlumnoDaoHibernateTest {
    
    private static final Logger log = LoggerFactory.getLogger(ClienteDaoTest.class);
    @Autowired
    private ClienteDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    	
    
    
    @Test
    public void testObtenerListaDeAlumnos() {
        log.debug("Debiera mostrar lista de Alumnos");
        Modalidad modalidad= new Modalidad("nombre","enLinea");
        currentSession().save(modalidad);
        AlumnoAcademico academico = new AlumnoAcademico( modalidad ,1);
        currentSession().save(academico);
        for (int i = 0; i < 20; i++) {
            
        
            Alumno alumno = new Alumno("108050" + i,
                    "zorch.mcclaud.da" + i+"@gmail.com", "test" + i,"Azul"+i,"Escobar"+i,academico);
            instance.crea(alumno);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("modalidad", modalidad.getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get("clientes"));
        assertNotNull(result.get("cantidad"));
        assertEquals(10, ((List<Cliente>) result.get("clientes")).size());
        assertEquals(20, ((Long) result.get("cantidad")).intValue());
    }
}
