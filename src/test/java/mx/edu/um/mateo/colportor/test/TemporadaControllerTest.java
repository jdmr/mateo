/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.TemporadaDao;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import static org.junit.Assert.*;
import org.junit.*;
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
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class TemporadaControllerTest extends BaseControllerTest {

    @Autowired
    private TemporadaDao temporadaDao;
    
    /**
     * 
     * @throws Exception 
     */
    @Test
    public void testLista() throws Exception {
        log.debug("Debiera monstrar lista TEmporada");

        Usuario asociado = this.obtieneAsociado();
        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));

        for (int i = 0; i < 20; i++) {
            Temporada temporada = new Temporada("test" + i);
            temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
            currentSession().save(temporada);
            assertNotNull(temporada.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_TEMPORADA))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TEMPORADA_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.TEMPORADA_LIST))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));

    }


    @Test
    public void testVer() throws Exception {
        log.debug("Debiera mostrar  temporada");
        Usuario asociado = this.obtieneAsociado();
        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));
        
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        temporada = temporadaDao.crea(temporada);
        assertNotNull(temporada.getId());

        this.mockMvc.perform(get(Constantes.PATH_TEMPORADA_VER + "/" + temporada.getId()))
                .andExpect(view().name(Constantes.PATH_TEMPORADA_VER));
    }

    @Test
    public void testGraba() throws Exception {
        log.debug("Debiera crear temporada");
        Usuario asociado = this.obtieneAsociado();
        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));
        
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        temporada = temporadaDao.crea(temporada);
        assertNotNull(temporada.getId());
        
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        this.mockMvc.perform(post(Constantes.PATH_TEMPORADA_CREA)
                .param("nombre", "test")
                .param("fechaInicio", sdf.format(new Date()))
                .param("fechaFinal", sdf.format(new Date())))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.creada.message"))
                .andExpect(redirectedUrl(Constantes.PATH_TEMPORADA_VER + "/" + (temporada.getId()+1L)));
    }

    @Test
    public void testActualiza() throws Exception {
        log.debug("Debiera actualizar  temporada");
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        
        Usuario asociado = this.obtieneAsociado();
        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));
        
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        temporada = temporadaDao.crea(temporada);
        assertNotNull(temporada.getId());
        
        Map<String, Object> params = new TreeMap<>();
        params.put("organizacion", asociado.getEmpresa().getOrganizacion().getId());
        Integer nRows = ((List)temporadaDao.lista(params).get(Constantes.TEMPORADA_LIST)).size();

        this.mockMvc.perform(post(Constantes.PATH_TEMPORADA_ACTUALIZA)
                .param("id", temporada.getId().toString())
                .param("version", temporada.getVersion().toString())
                .param("nombre", "test1")
                .param("fechaInicio", sdf.format(new Date()))
                .param("fechaFinal", sdf.format(new Date()))
                .param("status", temporada.getStatus()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.actualizada.message"))
                .andExpect(redirectedUrl(Constantes.PATH_TEMPORADA_VER + "/" + temporada.getId()));
        
        assertEquals(nRows, (Integer)((List)temporadaDao.lista(params).get(Constantes.TEMPORADA_LIST)).size());
    }

    @Test
    public void testElimina() throws Exception {
        log.debug("Debiera eliminar  temporada");
        
        Usuario asociado = this.obtieneAsociado();
        authenticate(asociado, asociado.getPassword(), new ArrayList <GrantedAuthority> (asociado.getRoles()));
        
        Temporada temporada = new Temporada("test");
        temporada.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        temporadaDao.crea(temporada);
        assertNotNull(temporada.getId());

        this.mockMvc.perform(post(Constantes.PATH_TEMPORADA_ELIMINA)
                .param("id", temporada.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.eliminada.message"))
                .andExpect(redirectedUrl(Constantes.PATH_TEMPORADA));
    }
}
