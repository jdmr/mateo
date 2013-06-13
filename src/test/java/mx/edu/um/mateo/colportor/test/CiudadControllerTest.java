/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.test;

import mx.edu.um.mateo.colportor.dao.CiudadDao;
import mx.edu.um.mateo.colportor.model.Ciudad;
import mx.edu.um.mateo.colportor.model.Estado;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import static org.junit.Assert.assertNotNull;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class CiudadControllerTest extends BaseControllerTest {

    @Autowired
    private CiudadDao ciudadDao;

    @Test
    public void debieraMostrarListaDeCiudad() throws Exception {
        log.debug("Debiera monstrar Ciudad");
        Estado estado = new Estado("test3");
        currentSession().save(estado);
        for (int i = 0; i < 20; i++) {
            Ciudad ciudad = new Ciudad(Constantes.NOMBRE + i);
            ciudad.setEstado(estado);
            ciudadDao.crea(ciudad);
            assertNotNull(ciudad);
        }

        this.mockMvc.perform(get(Constantes.PATH_CIUDAD))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CIUDAD_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CIUDAD_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarCiudad() throws Exception {
        log.debug("Debiera mostrar  Ciudad");
        Estado estado = new Estado("test3");
        currentSession().save(estado);
        Ciudad ciudad = new Ciudad(Constantes.NOMBRE);
        ciudad.setEstado(estado);
        ciudad = ciudadDao.crea(ciudad);
        assertNotNull(ciudad);

        this.mockMvc.perform(get(Constantes.PATH_CIUDAD_VER + "/" + ciudad.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CIUDAD_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_CIUDAD));
    }

    @Test
    public void debieraCrearCiudad() throws Exception {
        log.debug("Debiera crear Ciudad");
        Estado estado = new Estado("test3");
        currentSession().save(estado);
        this.mockMvc.perform(
                post(Constantes.PATH_CIUDAD_CREA)
                .param("id", "t")
                .param("version", "id")
                .param("nombre", Constantes.NOMBRE)
                .param("estado", estado.getId().toString()))
                .andExpect(status().isOk());
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "estado.creada.message"));
    }

    @Test
    public void debieraActualizarCiudad() throws Exception {
        log.debug("Debiera actualizar  Ciudad");
        Estado estado = new Estado("test3");
        currentSession().save(estado);
        Ciudad ciudad = new Ciudad(Constantes.NOMBRE);
        ciudad.setEstado(estado);
        ciudad = ciudadDao.crea(ciudad);
        assertNotNull(ciudad);

        this.mockMvc.perform(post(Constantes.PATH_CIUDAD_ACTUALIZA)
                .param("id", "t")
                .param("version", "id")
                .param("nombre", Constantes.NOMBRE)
                .param("estado", estado.getId().toString()))
                .andExpect(status().isOk());
//                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
//                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "estado.actualizada.message"));
    }

    @Test
    public void debieraEliminarEstado() throws Exception {
        log.debug("Debiera eliminar CIudad");
        Ciudad ciudad = new Ciudad("test");
        ciudadDao.crea(ciudad);
        assertNotNull(ciudad);

        this.mockMvc.perform(post(Constantes.PATH_CIUDAD_ELIMINA)
                .param("id", ciudad.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "ciudad.eliminada.message"));
    }
}
