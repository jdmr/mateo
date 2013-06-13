/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.inscripciones.model.AFETipoDescuento;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
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
public class AFETipoDescuentoDaoTest extends BaseDaoTest{
    @Autowired
    private AFETipoDescuentoDao instance;
    
    @Test
     public void testObtenerListaDeDescuentos() {     
        log.debug("Muestra lista de tipos de descuentos");
            Usuario usuario= obtieneUsuario();
            AFETipoDescuento afeTipoDescuento=null;
            Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
                for(int i=0; i<5; i++){         
            
            
                afeTipoDescuento= new AFETipoDescuento("Descuento","A", organizacion);
                instance.graba(afeTipoDescuento, organizacion);
                assertNotNull(afeTipoDescuento.getId());
        }
         Map<String, Object> params= new HashMap<>();
         params.put("organizacion",organizacion.getId());
         Map<String, Object> result=instance.lista(params);
        
    }
    
    @Test
    public void testObtiene() {
        Usuario usuario= obtieneUsuario();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        AFETipoDescuento afeTipoDescuento= new AFETipoDescuento("tipoDescuento","A", organizacion);
        instance.graba(afeTipoDescuento, organizacion);
        
        AFETipoDescuento afeTipoDescuento1= instance.obtiene(afeTipoDescuento.getId());
        assertEquals(afeTipoDescuento.getId(),afeTipoDescuento1.getId());
        
       
    }
    
    @Test
    public void testGraba() throws Exception {
         Usuario usuario= obtieneUsuario();
        AFETipoDescuento afeTipoDescuento;
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();
        afeTipoDescuento= new AFETipoDescuento("tipoDescuento","A", organizacion);
        instance.graba(afeTipoDescuento, organizacion);
        
        assertNotNull(afeTipoDescuento.getId());
        assertEquals(afeTipoDescuento.getDescripcion(),"tipoDescuento");
        assertEquals(afeTipoDescuento.getStatus(), "A");
    }
    
    
    @Test
    public void testElimina() throws Exception {
         Usuario usuario= obtieneUsuario();
        Organizacion organizacion=usuario.getEmpresa().getOrganizacion();

        AFETipoDescuento afeTipoDescuento= new AFETipoDescuento("tipoDescuento","A", organizacion);
        instance.graba(afeTipoDescuento, organizacion);
        instance.elimina(afeTipoDescuento.getId());
        assertEquals(afeTipoDescuento.getStatus(), "I");
        
       AFETipoDescuento afeTipoDescuento1= instance.obtiene(afeTipoDescuento.getId());
       if (afeTipoDescuento1.getStatus()!="I"){ 
           fail("no se borro el tipo de descuento");
       }
        
    }
}   
