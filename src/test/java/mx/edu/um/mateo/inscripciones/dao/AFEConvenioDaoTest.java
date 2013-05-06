/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AFEConvenio;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class AFEConvenioDaoTest extends BaseDaoTest{
    @Autowired
    private AFEConvenioDao instance;
    @Autowired
    private AlumnoDao alDao;
    
    @Test
    public void testLista() {
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
        Map<String, Object> params = new HashMap<>();
        params.put("empresa",usuario.getEmpresa().getId());
        Map <String, Object>  result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_AFECONVENIO));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        Iterator<AFEConvenio> itr = ((List<AFEConvenio>) result.get(Constantes.CONTAINSKEY_AFECONVENIO)).iterator();
        while(itr.hasNext()) {
         AFEConvenio convenio = itr.next();
         assertNotNull(convenio.getAlumno());
        }
            
    }
    
    @Test
    public void testGraba() {
        Usuario usuario = obtieneUsuario();   
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, usuario.getEmpresa());
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        Alumno alumno2 = alDao.obtiene("1060755");
        assertNotNull(alumno);
        assertNotNull(alumno2);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        AFEConvenio afeConvenio2= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1060755");
        instance.graba(afeConvenio2, usuario);
        instance.graba(afeConvenio, usuario);
        assertNotNull(afeConvenio.getId());
        assertNotNull(afeConvenio2.getId());
        
        AFEConvenio afeConvenio1 = instance.obtiene(afeConvenio.getId());
        assertEquals("1080506", afeConvenio1.getAlumno().getMatricula());
        
         AFEConvenio afeConvenio3 = instance.obtiene(afeConvenio2.getId());
        assertEquals("1060755", afeConvenio3.getAlumno().getMatricula());
    }
    
    @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, usuario.getEmpresa());
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        instance.graba(afeConvenio, usuario);
        assertNotNull(afeConvenio.getId());
        
        AFEConvenio afeConvenio1 = instance.obtiene(afeConvenio.getId());
        assertEquals("1080506", afeConvenio1.getAlumno().getMatricula());
        
        afeConvenio1.setImporte(new BigDecimal("3.30"));
        instance.graba(afeConvenio1, usuario);
        
        assertEquals("3.30",  instance.obtiene(afeConvenio.getId()).getImporte().toString());
    }
    
   
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10,usuario.getEmpresa());
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        currentSession().save(afeConvenio);
        assertNotNull(afeConvenio.getId());
        
        AFEConvenio afeConvenio1= instance.obtiene(afeConvenio.getId());
        assertEquals(afeConvenio.getMatricula(), afeConvenio1.getMatricula());
        assertEquals(afeConvenio.getId(), afeConvenio1.getId());
    }
    
    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, usuario.getEmpresa());
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        AFEConvenio afeConvenio= new AFEConvenio("A",alumno,usuario.getEmpresa(),tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        instance.graba(afeConvenio, usuario);
        
        String matricula = instance.obtiene(afeConvenio.getId()).getMatricula();
        String matricula1 = instance.elimina(afeConvenio.getId());        
        assertEquals(matricula, matricula1);
        
        
            AFEConvenio afeConvenio1 = instance.obtiene(afeConvenio.getId());
            
            if(afeConvenio1 != null){
            fail("se encontro afeConvenio" + afeConvenio1);
            }
    }
}
