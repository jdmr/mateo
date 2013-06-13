/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AlumnoPaquete;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.fail;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 *
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class AlumnoPaqueteDaoTest  extends BaseDaoTest{
    
    @Autowired
    private AlumnoPaqueteDao instance;


    /**
     * Test of getPaquetes method, of class PaqueteDao.
     */
    @Test
    public void testListaDeAlumnoPaquete() {

        Usuario usuario= obtieneUsuario();
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = null;
        for (int i = 0; i < 20; i++) {
            alumnoPaquete = new AlumnoPaquete();
            alumnoPaquete.setPaquete(paquete);
            alumnoPaquete.setMatricula("1110475");
            alumnoPaquete.setStatus("A");
            instance.graba(alumnoPaquete, usuario);
            assertNotNull(alumnoPaquete.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_ALUMNOPAQUETES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TiposBecas>) result.get(Constantes.CONTAINSKEY_ALUMNOPAQUETES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of getPaquete method, of class PaqueteDao.
     */
    @Test
    public void testObtenerAlumnoPaquete() {
        Usuario usuario= obtieneUsuario();
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        currentSession().save(alumnoPaquete);
        assertNotNull(alumnoPaquete.getId());
        AlumnoPaquete alumnoPaquete1 = instance.obtiene(alumnoPaquete.getId());
        assertEquals(alumnoPaquete.getMatricula(), alumnoPaquete1.getMatricula());

    }

    /**
     * Test of savePaquete method, of class PaqueteDao.
     */
    @Test
    public void testGrabaAlumnoPaquete() {
        Usuario usuario = obtieneUsuario();
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
        currentSession().save(paquete);
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        alumnoPaquete.getPaquete().setEmpresa(usuario.getEmpresa());
        instance.graba(alumnoPaquete, usuario);
        assertNotNull(alumnoPaquete.getId());
    }

    /**
     * Test of removePaquete method, of class PaqueteDao.
     */
    @Test
    public void debieraEliminarAlumnoPaquete() {
        Usuario usuario = obtieneUsuario();  
        Paquete paquete = new Paquete("Test","Test1",new BigDecimal("1110475"), new BigDecimal(12), new BigDecimal(12),"1", usuario.getEmpresa());
        currentSession().save(paquete);
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        alumnoPaquete.setPaquete(paquete);
        alumnoPaquete.setMatricula("1110475");
        alumnoPaquete.setStatus("A");
        alumnoPaquete.getPaquete().setEmpresa(usuario.getEmpresa());
        instance.graba(alumnoPaquete, usuario);
        assertNotNull(alumnoPaquete.getId());
        
        Long idTm = alumnoPaquete.getId();
        
        instance.elimina(idTm);
        try{
        AlumnoPaquete alumnoPaquete2 = instance.obtiene(idTm);
        fail("Error al eliminar alumno");
        }catch (ObjectRetrievalFailureException e){
            
        }
        
    }
}
