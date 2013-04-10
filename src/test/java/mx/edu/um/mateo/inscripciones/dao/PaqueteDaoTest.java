/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class PaqueteDaoTest extends BaseDaoTest {

    @Autowired
    private PaqueteDao instance;
    
    /**
     * Prueba la lista de paquetes
     */
    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        Paquete paquete=null;
        for (int i = 0; i < 20; i++) {
            paquete = new Paquete();
            paquete.setAcfe("s");
            paquete.setDescripcion("test");
            paquete.setEnsenanza(new BigDecimal("1.2"));
            paquete.setInternado(new BigDecimal("2.2"));
            paquete.setMatricula(new BigDecimal("3.3"));
            paquete.setNombre("Test");
            instance.crea(paquete, usuario);
            assertNotNull(paquete.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_PAQUETES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TiposBecas>) result.get(Constantes.CONTAINSKEY_PAQUETES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Prueba el obtener un paquete
     */
    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new BigDecimal("1.2"));
        paquete.setInternado(new BigDecimal("2.2"));
        paquete.setMatricula(new BigDecimal("3.3"));
        paquete.setNombre(nombre);
        paquete.setEmpresa(usuario.getEmpresa());
        currentSession().save(paquete);
        assertNotNull(paquete.getId());
        
        Paquete paquete1 = instance.obtiene(paquete.getId());
        assertEquals(paquete.getNombre(), paquete1.getNombre());

    }

    /**
     * Prueba el proceso de creacion del paquete
     */
    @Test
    public void testCrea() {
        Usuario usuario = obtieneUsuario();   
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new BigDecimal("1.2"));
        paquete.setInternado(new BigDecimal("2.2"));
        paquete.setMatricula(new BigDecimal("3.3"));
        paquete.setNombre(nombre);
        paquete.setEmpresa(usuario.getEmpresa());
        instance.crea(paquete, usuario);
        assertNotNull(paquete.getId());
        
        Paquete paquete1 = instance.obtiene(paquete.getId());
        assertEquals(paquete.getNombre(), paquete1.getNombre());
    }
    
    /**
     * Prueba el proceso de actualizacion
     */
    @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new BigDecimal("1.2"));
        paquete.setInternado(new BigDecimal("2.2"));
        paquete.setMatricula(new BigDecimal("3.3"));
        paquete.setNombre(nombre);
        paquete.setEmpresa(usuario.getEmpresa());
        instance.crea(paquete, usuario);
        assertNotNull(paquete.getId());
        
        Paquete paquete1 = instance.obtiene(paquete.getId());
        assertEquals(paquete.getNombre(), paquete1.getNombre());
        
        paquete1.setDescripcion("test2");
        instance.actualiza(paquete1, usuario);
        
        currentSession().refresh(paquete);
        assertEquals("test2", paquete.getDescripcion());
    }

    /**
     * Prueba el eliminar el paquete
     */
    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();        
        String nombre = "nombre";
        Paquete paquete = new Paquete();
        paquete.setAcfe("s");
        paquete.setDescripcion("test");
        paquete.setEnsenanza(new BigDecimal("1.2"));
        paquete.setInternado(new BigDecimal("2.2"));
        paquete.setMatricula(new BigDecimal("3.3"));
        paquete.setNombre(nombre);
        paquete.setEmpresa(usuario.getEmpresa());
        currentSession().save(paquete);
        assertNotNull(paquete.getId());
        
        String descripcion = instance.elimina(paquete.getId());
        assertEquals(paquete.getDescripcion(), descripcion);
        
        try{
            Paquete paquete1 = instance.obtiene(paquete.getId());        
            fail("Se encontro paquete "+paquete1);
        }catch(ObjectRetrievalFailureException e){
            log.debug("Se elimino con exito el paquete {}", descripcion);
        }
    }
}
