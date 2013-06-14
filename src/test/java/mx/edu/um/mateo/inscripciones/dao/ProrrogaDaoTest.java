/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Prorroga;
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
public class ProrrogaDaoTest extends BaseDaoTest {

    @Autowired
    private ProrrogaDao instance;

    /**
     * Test of lista method, of class ProrrogaDao.
     */
    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        Prorroga prorroga = null;
        for (int i = 0; i < 20; i++) {
            prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("2369.8"), "a");
            prorroga.setObservaciones("test");
            instance.graba(prorroga, usuario);
            assertNotNull(prorroga.getId());
        }
        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_PRORROGAS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Prorroga>) result.get(Constantes.CONTAINSKEY_PRORROGAS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class ProrrogaDao.
     */
    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();

        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("2369.8"), "a");
        prorroga.setObservaciones("test");
        instance.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
        String matricula = "1110476";
        prorroga.setMatricula(matricula);
        instance.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
        Prorroga prorroga1 = instance.obtiene(prorroga.getId());
        assertNotNull(prorroga1.getId());
        assertEquals(prorroga.getMatricula(), prorroga1.getMatricula());

    }

    /**
     * Test of graba method, of class ProrrogaDao.
     */
    @Test
    public void testGraba() {
        Usuario usuario = obtieneUsuario();
        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("2369.8"), "a");
        prorroga.setObservaciones("test");
        instance.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
        Prorroga prorroga1 = instance.obtiene(prorroga.getId());
        assertNotNull(prorroga1.getId());
        assertEquals(prorroga.getMatricula(), prorroga1.getMatricula());
    }

    /**
     * Test of graba method, of class ProrrogaDao.
     */
    @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("2369.8"), "a");
        prorroga.setObservaciones("test");
        instance.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());
        Prorroga prorroga1 = instance.obtiene(prorroga.getId());
        assertNotNull(prorroga1.getId());
        assertEquals(prorroga.getMatricula(), prorroga1.getMatricula());
        prorroga1.setMatricula("1110476");
        instance.graba(prorroga, usuario);
        currentSession().refresh(prorroga);
        assertEquals("1110476", prorroga.getMatricula());
    }

    /**
     * Test of elimina method, of class ProrrogaDao.
     */
    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();

        Prorroga prorroga = new Prorroga("1110475", new Date(), new Date(), "test", new Double("2369.8"), "a");
        prorroga.setObservaciones("test");
        instance.graba(prorroga, usuario);
        assertNotNull(prorroga.getId());

        String descripcion = instance.elimina(prorroga.getId());
        assertEquals(prorroga.getDescripcion(), descripcion);
        try {
            Prorroga prorroga1 = instance.obtiene(prorroga.getId());
            fail("Se encontro paquete " + prorroga1);
        } catch (ObjectRetrievalFailureException e) {
            log.debug("Se elimino con exito el paquete {}", descripcion);
        }
    }
}
