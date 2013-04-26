package mx.edu.um.mateo.inscripciones.dao;

import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Periodo;
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
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class PeriodoDaoTest extends BaseDaoTest {
    
     private static final Logger log = LoggerFactory.getLogger(PeriodoDaoTest.class);
     @Autowired
     private PeriodoDao instance;
     
    @SuppressWarnings("unchecked") 
    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        Periodo periodo = null;
        for (int i = 0; i < 20; i++) {
            periodo = new Periodo("test" + i, "A" , "clave", new Date(), new Date());
            periodo.setOrganizacion(usuario.getEmpresa().getOrganizacion());
            instance.graba(periodo);
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_PERIODOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Periodo>) result.get(Constantes.CONTAINSKEY_PERIODOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }
    
    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        
        Periodo periodo = new Periodo("test", "A", "clave", new Date(), new Date());
        periodo.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        
        currentSession().save(periodo);
        assertNotNull(periodo.getId());
        
        Periodo periodo1 = instance.obtiene(periodo.getId());
        assertEquals(periodo.getDescripcion(), periodo1.getDescripcion());
    }
    
     @Test
    public void testCrear() {
        Usuario usuario = obtieneUsuario();  
        
        Periodo periodo = new Periodo("test", "A", "clave", new Date(), new Date());
        periodo.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        instance.graba(periodo);
        assertNotNull(periodo.getId());
        
        Periodo periodo1 = instance.obtiene(periodo.getId());
        assertEquals(periodo.getDescripcion(), periodo1.getDescripcion());
    }
     
      @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        Periodo periodo = new Periodo("test", "A", "clave", new Date(), new Date());
        periodo.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        instance.graba(periodo);
        assertNotNull(periodo.getId());

        Periodo prueba = instance.obtiene(periodo.getId());
        assertNotNull(periodo.getDescripcion(), prueba.getDescripcion());
        
        prueba.setDescripcion("PRUEBA");
        instance.actualiza(prueba);
        
        currentSession().refresh(periodo);
        assertEquals("PRUEBA", periodo.getDescripcion());
    }
      
      @Test
       public void testElimina() {
          Usuario usuario = obtieneUsuario();
          Periodo periodo = new Periodo("test", "A", "clave", new Date(), new Date());
          periodo.setOrganizacion(usuario.getEmpresa().getOrganizacion());
          currentSession().save(periodo);
          instance.elimina(periodo.getId());
          
          Periodo periodo1 = instance.obtiene(periodo.getId());
          
          assertEquals("I", periodo1.getStatus());
      }
}
