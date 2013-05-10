/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.AFEConvenioDao;
import mx.edu.um.mateo.inscripciones.dao.AlumnoDao;
import mx.edu.um.mateo.inscripciones.model.AFEConvenio;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class AFEConvenioControllerTest extends BaseControllerTest{
    
    @Autowired
    private AFEConvenioDao instance;    
    @Autowired
    private AlumnoDao alDao;
    
    
    @Test
    public void testLista() throws Exception {
        log.debug("test obtener una lista de Convenios");
        
        Usuario usuario = obtieneUsuario();
       
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, usuario.getEmpresa());
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        AFEConvenio afeConvenio = null;
        for (int i=0 ; i<20; i++){
            afeConvenio= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
            instance.graba(afeConvenio, usuario);
            assertNotNull(afeConvenio.getId());
        }
        
         this.mockMvc.perform(get(Constantes.PATH_AFECONVENIO)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFECONVENIO_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_AFECONVENIO)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
//    
//    @Test
//    public void testNuevo() throws Exception {
//        log.debug("Test 'nuevo'");
//                
//        this.mockMvc.perform(get("/" + Constantes.PATH_AFECONVENIO_NUEVO))
//                
//                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFECONVENIO_NUEVO + ".jsp"))
//                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_AFECONVENIO))
//                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_TIPOSBECAS));
//    }
    
//    @Test
//    public void testCrear() throws Exception {
//        log.debug("Test Crea un Convenio");
//        Usuario usuario = obtieneUsuario();
//        
//        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, usuario.getEmpresa());
//        currentSession().save(tipoBeca);
//        assertNotNull(tipoBeca.getId());
//        Alumno alumno = alDao.obtiene("1080506");
//        assertNotNull(alumno);
//        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
//        instance.graba(afeConvenio, usuario);
//        assertNotNull(afeConvenio.getId());
//        assertEquals(alumno.getMatricula(), "1080506");
//        
//        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
//        
//        this.mockMvc.perform(post(Constantes.PATH_AFECONVENIO_GRABA)
//                .param("matricula", "1080506")
//                .param("tipoBeca.id", tipoBeca.getId().toString())
//                .param("importe", "300")
//                .param("numHoras", "320")
//                .param("diezma", "0")
//                .param("afeConvenio.id", afeConvenio.getId().toString()))
//                .andExpect(redirectedUrl(Constantes.PATH_AFECONVENIO_LISTA))
//                .andExpect(flash().attributeExists("message"))
//                .andExpect(flash().attribute("message", "afeConvenio.graba.message"));
//    }
    
//    @Test
//    public void testActualizar() throws Exception {
//        log.debug("Test Actualiza un Convenio");
//        Usuario usuario = obtieneUsuario();
//        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, usuario.getEmpresa());
//        currentSession().save(tipoBeca);
//        assertNotNull(tipoBeca.getId());
//        Alumno alumno = alDao.obtiene("1080506");
//        assertNotNull(alumno);
//        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
//       instance.graba(afeConvenio, usuario);
//        assertNotNull(afeConvenio.getId());
//        assertEquals(alumno.getMatricula(), "1080506");
//        
//        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
//        
//        this.mockMvc.perform(post(Constantes.PATH_AFECONVENIO_GRABA)
//                .param("matricula", "1080506")
//                .param("tipoBeca.id", tipoBeca.getId().toString())
//                .param("importe", "300")
//                .param("numHoras", "320")
//                .param("diezma", "0")
//                .param("afeConvenio.id", afeConvenio.getId().toString()))
//                .andExpect(redirectedUrl(Constantes.PATH_AFECONVENIO_LISTA))
//                .andExpect(flash().attributeExists("message"))
//                .andExpect(flash().attribute("message", "afeConvenio.graba.message"));
//    }
    
    @Test
    public void testVer() throws Exception {
        log.debug("Test mostrar un Convenio");
        Usuario usuario = obtieneUsuario();
        
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, usuario.getEmpresa());
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        currentSession().save(usuario);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        currentSession().save(afeConvenio);
        assertNotNull(afeConvenio.getId());
        assertEquals(alumno.getMatricula(), "1080506");
        this.mockMvc.perform(get(Constantes.PATH_AFECONVENIO_VER + "/" + afeConvenio.getId())).
                andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFECONVENIO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_AFECONVENIO));
    }
    
    @Test
    public void testElimina() throws Exception {
        log.debug("Test eliminar un Convenio");
        Usuario usuario = obtieneUsuario();  
        
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, usuario.getEmpresa());
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        currentSession().save(usuario);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        instance.graba(afeConvenio, usuario);
        assertNotNull(afeConvenio.getId());
        assertEquals(alumno.getMatricula(), "1080506");
        
        this.mockMvc.perform(post(Constantes.PATH_AFECONVENIO_ELIMINA)
                .param("id", afeConvenio.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "afeConvenio.elimina.message"))
                .andExpect(redirectedUrl(Constantes.PATH_AFECONVENIO_LISTA));
    }
    
    @Test
    public void testAsigancionConvenioPorDepartamentoInicio() throws Exception {
        this.mockMvc.perform(post(Constantes.PATH_AFECONVENIO_CONVENIO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFECONVENIO_CONVENIO + ".jsp"));
    }
    
    @Test
    public void testAsigancionConvenioPorDepartamentoLaMatriculaSiExiste() throws Exception {
       
        this.mockMvc.perform(post(Constantes.PATH_AFECONVENIO_ALUMNO)
                .param("matricula","1060755"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_AFECONVENIO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFECONVENIO_CONVENIO + ".jsp"));
    }
    
    @Test
    public void testAsigancionConvenioPorDepartamentoLaMatriculaNoExiste() throws Exception {
        
         this.mockMvc.perform(post(Constantes.PATH_AFECONVENIO_ALUMNO)
         .param("matricula","0000000"))
         
         .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_AFECONVENIO_CONVENIO + ".jsp"));
    }
    
    
    @Test
    public void testAsigancionConvenioPorDepartamento() throws Exception {
        Usuario usuario = obtieneUsuario();
        
        TiposBecas tipoBeca= new TiposBecas("Servicio Becario", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, usuario.getEmpresa());
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1060755");
        assertNotNull(alumno);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1060755");
        instance.graba(afeConvenio, usuario);
        assertEquals("1060755" , afeConvenio.getMatricula());
        Map<String, Object> params = new HashMap<>();
        params.put("empresa",usuario.getEmpresa().getId());
        Map <String, Object>  result = instance.lista(params);
        
        Integer items = ((List)result.get(Constantes.CONTAINSKEY_AFECONVENIO)).size();
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post(Constantes.PATH_AFECONVENIO_GRABA)
                .param("matricula", "1080506"))
                //.param("plaza.id", "") //cuando se cree plaza se debe asignar aqui
                .andExpect(redirectedUrl(Constantes.PATH_AFECONVENIO_LISTA))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "afeConvenio.graba.message"));
        
       result = instance.lista(params);
       List <AFEConvenio>lista = (List)result.get(Constantes.CONTAINSKEY_AFECONVENIO);
       for(AFEConvenio c:lista){
           log.debug("{}" , c);
       }
       assertEquals(items + 1, lista.size());
       
       assertEquals("1080506", lista.get(items).getAlumno().getMatricula());
       
        assertEquals("1060755", lista.get(items - 1).getAlumno().getMatricula());
       
    }
    
}
