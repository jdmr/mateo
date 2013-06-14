/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
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
public class CentroCostoDaoTest extends BaseDaoTest{
     @Autowired
    private CentroCostoDao instance;
     
     @Autowired
    private EjercicioDao ejercicio;
     
     
     @Test
     public void testListaDepartamento() {
        Usuario usuario= obtieneUsuario();
        Organizacion organizacion= usuario.getEmpresa().getOrganizacion();
        
        String idCosto= "1.01.1.01.0";
        String idCosto2="1.01.1.02.0";
        for (int i=0; i<10; i++){
            CentroCosto cCosto= new CentroCosto();
            cCosto.setDetalle("S");
            cCosto.getId().setIdCosto(idCosto+i);
            cCosto.getId().setEjercicio(usuario.getEjercicio());
            cCosto.setIniciales("ASDFG");
            cCosto.setNombre("Nombre");
            cCosto.setSeleccionado(Boolean.TRUE);
            currentSession().save(cCosto);
        }
        for (int i=0; i<10; i++){
            CentroCosto cCosto= new CentroCosto();
            cCosto.setDetalle("N");
            cCosto.getId().setIdCosto(idCosto2+i);
            cCosto.getId().setEjercicio(usuario.getEjercicio());
            cCosto.setIniciales("ASDFG");
            cCosto.setNombre("Nombre");
            cCosto.setSeleccionado(Boolean.TRUE);
            currentSession().save(cCosto);
        }
        log.debug("ENTRANDOO"); 
        log.debug("{}");
        Map<String, Object> params = new HashMap<>();
        params.put("id.idCosto", "1");        
        List <CentroCosto>  result = instance.listaDepartamento(usuario);
        log.debug("ENTRANDOO"); 
        log.debug("{}", result);
        
        for(int i=0; i<result.size(); i++){
            CentroCosto centroCosto=result.get(i);
            
            assertEquals(centroCosto.getDetalle(), "S");
            assertTrue(centroCosto.getId().getIdCosto().startsWith("1"));
        }
     }
}
