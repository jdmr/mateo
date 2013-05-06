/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.inscripciones.model.AlumnoDescuento;
import mx.edu.um.mateo.inscripciones.model.Descuento;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
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
public class AlumnoDescuentoDaoTest extends BaseDaoTest{
    @Autowired
    private AlumnoDescuentoDao instance;
    
    
    @Test
     public void testObtenerListaDeDescuentodescuentos() {     
        log.debug("Muestra lista de tipos de descuentos");
            Usuario usuario= obtieneUsuario();
            Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
            Descuento descuento= new Descuento("Descuento","A","S", organizacion);
            currentSession().save(descuento);
            AlumnoDescuento alumnoDescuento;
                for(int i=0; i<5; i++){         
            
            
                alumnoDescuento= new AlumnoDescuento("1080506",descuento,new Date(), usuario,"S","A");
                instance.graba(alumnoDescuento, usuario);
                assertNotNull(alumnoDescuento.getId());
        }
         Map<String, Object> params= new HashMap<>();
         params.put("usuario",usuario.getId());
         Map<String, Object> result=instance.lista(params);
        
    }
    
    @Test
    public void testObtiene() {
        Usuario usuario= obtieneUsuario();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        Descuento descuento= new Descuento("Descuento","A","S", organizacion);
        currentSession().save(descuento);
        AlumnoDescuento alumnoDescuento= new AlumnoDescuento("1080506",descuento,new Date(), usuario,"S","A");
        instance.graba(alumnoDescuento, usuario);
        
        AlumnoDescuento alumnoDescuento1= instance.obtiene(alumnoDescuento.getId());
        assertEquals(alumnoDescuento.getId(),alumnoDescuento1.getId());
        
           }
     @Test
    public void testGraba() throws Exception {
        Usuario usuario= obtieneUsuario();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        Descuento descuento= new Descuento("Descuento","A","S", organizacion);
        currentSession().save(descuento);
        AlumnoDescuento alumnoDescuento= new AlumnoDescuento("1080506",descuento,new Date(), usuario,"S","A");
        instance.graba(alumnoDescuento, usuario);
        
        assertNotNull(alumnoDescuento.getId());
        assertEquals(alumnoDescuento.getMatricula(),"1080506");
        assertEquals(alumnoDescuento.getStatus(), "A");
    }
     
     
     @Test
    public void testElimina() throws Exception {
        Usuario usuario= obtieneUsuario();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        Descuento descuento= new Descuento("Descuento","A","S", organizacion);
        currentSession().save(descuento);
        AlumnoDescuento alumnoDescuento= new AlumnoDescuento("1080506",descuento,new Date(), usuario,"S","A");
        instance.graba(alumnoDescuento, usuario);
        instance.elimina(alumnoDescuento.getId());
        
       AlumnoDescuento alumnoDescuento1= instance.obtiene(alumnoDescuento.getId());
       if (alumnoDescuento1!=null){         

           fail("no se borro el tipo de descuento");
       }
        
    }
}
