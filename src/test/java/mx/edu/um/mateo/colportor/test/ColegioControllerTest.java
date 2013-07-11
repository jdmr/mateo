/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
/**
 * TODO problemas con type long: user
 */
package mx.edu.um.mateo.colportor.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.dao.ColegioDao;
import mx.edu.um.mateo.rh.model.Colegio;
import static org.junit.Assert.assertNotNull;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class ColegioControllerTest extends BaseControllerTest {

    @Autowired
    private ColegioDao colegioDao;

    @Test
    public void lista() throws Exception {
        log.debug("Debiera mostrar lista de colegios");

        Usuario asociado = obtieneAsociado();
        authenticate(asociado, asociado.getPassword(), new ArrayList<GrantedAuthority>(asociado.getRoles()));

        for (int i = 0; i < 20; i++) {
            Colegio colegio = new Colegio();
            colegio.setNombre("Test");
            colegio.setStatus("A");
            colegio.setOrganizacion(asociado.getEmpresa().getOrganizacion());
            currentSession().save(colegio);
            assertNotNull(colegio.getId());
        }

        this.mockMvc.perform(get(Constantes.COLEGIO_PATH)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.COLEGIO_PATH_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_COLEGIOS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void ver() throws Exception {
        log.debug("Debiera mostrar colegio");

        Usuario asociado = obtieneAsociado();
        authenticate(asociado, asociado.getPassword(), new ArrayList<GrantedAuthority>(asociado.getRoles()));

        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegio.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(colegio);
        assertNotNull(colegio.getId());

        this.mockMvc.perform(get(Constantes.COLEGIO_PATH_VER + "/" + colegio.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.COLEGIO_PATH_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_COLEGIO));
    }

    @Test
    public void crear() throws Exception {
        log.debug("Debiera crear colegio");

        Usuario asociado = obtieneAsociado();
        authenticate(asociado, asociado.getPassword(), new ArrayList<GrantedAuthority>(asociado.getRoles()));

        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegio.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(colegio);
        assertNotNull(colegio.getId());

        this.mockMvc.perform(post(Constantes.COLEGIO_PATH_CREA)
                .param("nombre", colegio.getNombre().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.creado.message"))
                .andExpect(redirectedUrl(Constantes.COLEGIO_PATH));
    }

    @Test
    public void actualizar() throws Exception {
        log.debug("Debiera actualizar colegio");

        Usuario asociado = obtieneAsociado();
        authenticate(asociado, asociado.getPassword(), new ArrayList<GrantedAuthority>(asociado.getRoles()));

        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegio.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(colegio);
        assertNotNull(colegio.getId());
        assertNotNull(colegio.getVersion());
        
        Map<String, Object> params = new TreeMap();
        params.put("organizacion", asociado.getEmpresa().getOrganizacion().getId());
        params = colegioDao.lista(params);
        Integer items = ((List)params.get(Constantes.CONTAINSKEY_COLEGIOS)).size();
        
        String nombre = "modificado";

        this.mockMvc.perform(post(Constantes.COLEGIO_PATH_CREA)
                .param("id", colegio.getId().toString())
                .param("version", colegio.getVersion().toString())
                .param("nombre", nombre)
                .param("status", colegio.getStatus()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.actualizado.message"))
                .andExpect(redirectedUrl(Constantes.COLEGIO_PATH));
        
        params = colegioDao.lista(params);
        assertEquals(items,(Integer)((List)params.get(Constantes.CONTAINSKEY_COLEGIOS)).size());
        
        colegio = colegioDao.obtiene(colegio.getId());
        assertEquals(nombre, colegio.getNombre());
    }

    @Test
    public void eliminar() throws Exception {
        log.debug("Debiera eliminar colegio");

        Usuario asociado = obtieneAsociado();
        authenticate(asociado, asociado.getPassword(), new ArrayList<GrantedAuthority>(asociado.getRoles()));

        Colegio colegio = new Colegio();
        colegio.setNombre("Test");
        colegio.setStatus("A");
        colegio.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(colegio);
        assertNotNull(colegio.getId());

        this.mockMvc.perform(post(Constantes.COLEGIO_PATH_ELIMINA)
                .param("id", colegio.getId().toString()))
                .andExpect(redirectedUrl(Constantes.COLEGIO_PATH))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.eliminado.message"));
    }
}