/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.model.ClaveEmpleado;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ClaveEmpleadoDaoTest extends BaseDaoTest {

    @Autowired
    private ClaveEmpleadoDao dao;

    /**
     * Test of lista method, of class ClaveEmpleadoDao.
     */
    @Test
    public void testLista() {
        log.debug("test lista claveEmpleado");
        ClaveEmpleado claveEmpleado = null;
        Usuario usuario = obtieneUsuario();
        for (int i = 0; i < 20; i++) {
            claveEmpleado = new ClaveEmpleado("1110475", "a", "prueba dao", new Date());
            dao.graba(claveEmpleado, usuario);
        }
        Map<String, Object> params = null;
        Map result = dao.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_CLAVEEMPLEADO));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<ClaveEmpleado>) result.get(Constantes.CONTAINSKEY_CLAVEEMPLEADO)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class ClaveEmpleadoDao.
     */
    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        ClaveEmpleado claveEmpleado = new ClaveEmpleado("1110475", "a", "prueba dao", new Date());
        dao.graba(claveEmpleado, usuario);
        assertNotNull(claveEmpleado.getId());
        ClaveEmpleado claveEmpleado1 = dao.obtiene(claveEmpleado.getId());
        assertNotNull(claveEmpleado1);
        assertEquals(claveEmpleado.getId(), claveEmpleado1.getId());
    }

    /**
     * Test of graba method, of class ClaveEmpleadoDao.
     */
    @Test
    public void testGraba() {
        Usuario usuario = obtieneUsuario();
        ClaveEmpleado claveEmpleado = new ClaveEmpleado("1110475", "a", "prueba dao", new Date());
        dao.graba(claveEmpleado, usuario);
        assertNotNull(claveEmpleado.getId());
        ClaveEmpleado claveEmpleado1 = dao.obtiene(claveEmpleado.getId());
        assertNotNull(claveEmpleado1);
        assertEquals(claveEmpleado.getId(), claveEmpleado1.getId());
    }

    /**
     * Test of elimina method, of class ClaveEmpleadoDao.
     */
    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();
        ClaveEmpleado claveEmpleado = new ClaveEmpleado("1110475", "a", "prueba dao", new Date());
        dao.graba(claveEmpleado, usuario);
        assertNotNull(claveEmpleado.getId());
        ClaveEmpleado claveEmpleado1 = dao.obtiene(claveEmpleado.getId());
        assertNotNull(claveEmpleado1);
        assertEquals(claveEmpleado.getId(), claveEmpleado1.getId());

        String clave = dao.elimina(claveEmpleado1.getId());
        assertEquals(claveEmpleado1.getClave(), clave);
    }

}
