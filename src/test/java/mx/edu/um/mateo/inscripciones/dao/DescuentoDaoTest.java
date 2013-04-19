/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.HashMap;
import static org.junit.Assert.*;
import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.inscripciones.model.Descuento;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class DescuentoDaoTest extends BaseDaoTest{
    @Autowired
    private DescuentoDao instance;
    
    
    @Test
     public void testObtenerListaDeDescuentos() {     
        log.debug("Muestra lista de descuentos");
            Usuario usuario= obtieneUsuario();
            Descuento descuento=null;
            Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
                for(int i=0; i<5; i++){         
            
            
                descuento= new Descuento();
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
        Usuario usuario= obtieneUsuario();
        Descuento descuento=new Descuento();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        descuento.setDescripcion("Descuento1");
        descuento.setStatus("A");
        descuento.setContabiliza("S");
        instance.graba(descuento, organizacion);
        
        Descuento descuento1= instance.obtiene(descuento.getId());
        assertEquals(descuento.getId(),descuento1.getId());
        
       
    }
    
    @Test
    public void testGraba() throws Exception {
         Usuario usuario= obtieneUsuario();
        Descuento descuento=new Descuento();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
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
         Usuario usuario= obtieneUsuario();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();

        Descuento descuento= new Descuento("Descuento","A", "S", organizacion);
        instance.graba(descuento, organizacion);
        instance.elimina(descuento.getId());
        
       Descuento descuento1= instance.obtiene(descuento.getId());
       if (descuento1!=null){ 
           fail("no se borro el descuento");
       }
        
    }
}