/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.dao;

//import com.lowagie.text.List;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.model.Libro;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.utils.UltimoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LibroDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(LibroDaoTest.class);
    @Autowired
    private LibroDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of obtiene method, of class LibroDao.
     */
    @Test
    public void deberiaMostrarListaDeLibro() {
        log.debug("Debiera mostrar lista de libro");
        for (int i = 0; i < 20; i++) {
            int test = 000;
            Libro libro = new Libro("tes" + i, "tes", "te", test + i);
            currentSession().save(libro);
            assertNotNull(libro);
            log.debug("libro>>" + libro);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_LIBROS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Empresa>) result.get(Constantes.CONTAINSKEY_LIBROS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerLibro() {
        log.debug("Debiera obtener libro");
        String nombre = "test";
        Libro libro = new Libro("test", "tes", "te", 000);
        currentSession().save(libro);
        assertNotNull(libro.getId());
        Long id = libro.getId();

        Libro result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, libro);
    }

    @Test
    public void deberiaCrearLibro() {
        log.debug("Deberia crear Libro");

        Libro libro = new Libro("test", "tes", "te", 0000);

        Libro libro2 = instance.crea(libro);
        assertNotNull(libro2);
        assertNotNull(libro2.getId());

        assertEquals(libro, libro2);
    }

    @Test
    public void deberiaActualizarLibro() {
        log.debug("Deberia actualizar Libro");

        Libro libro = new Libro("test", "tes", "te", 0000);
        assertNotNull(libro);
        currentSession().save(libro);

        String nombre = "test1";
        libro.setNombre(nombre);

        Libro libro2 = instance.actualiza(libro);
        assertNotNull(libro2);
        assertEquals(nombre, libro.getNombre());

        assertEquals(libro, libro2);
    }

    @Test
    public void deberiaEliminarLibro() throws UltimoException {
        log.debug("Debiera eliminar Libro");

        String nom = "test";
        Libro libro = new Libro("test", "tes", "te", 0000);
        currentSession().save(libro);
        assertNotNull(libro);

        String nombre = instance.elimina(libro.getId());
        assertEquals(nom, nombre);

        Libro prueba = instance.obtiene(libro.getId());
//        assertNull(prueba);
    }
}
