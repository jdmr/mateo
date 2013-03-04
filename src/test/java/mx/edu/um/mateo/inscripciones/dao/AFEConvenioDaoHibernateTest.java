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
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AFEConvenio;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import org.hibernate.Session;
import static org.junit.Assert.*;
import org.hibernate.SessionFactory;
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
 * @author zorch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class AFEConvenioDaoHibernateTest {
     @Autowired
    private AFEConvenioDao instance;
    private static final Logger log = LoggerFactory.getLogger(AFEConvenioDaoHibernateTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    @Autowired
    private AlumnoDao alDao;
    
    @Test
    public void testObtenerListaAFEConvenios() {
     log.debug("Deberia obtener una lista de Convenios");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, empresa);
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        AFEConvenio afeConvenio = null;
        for (int i=0 ; i<20; i++){
            afeConvenio= new AFEConvenio(alumno,empresa,tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
            currentSession().save(afeConvenio);
            assertNotNull(afeConvenio.getId());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("empresa",empresa.getId());
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
    public void testGrabaAFEConvenios() {
     log.debug("Deberia grabar un Convenio");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, empresa);
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        AFEConvenio afeConvenio= new AFEConvenio(alumno,empresa,tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        currentSession().save(afeConvenio);
        assertNotNull(afeConvenio.getId());
        assertEquals(alumno.getMatricula(), "1080506");
    }
    
    @Test
    public void testObtieneAFEConvenios() {
     log.debug("Deberia obtener un Convenio");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10,empresa);
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        AFEConvenio afeConvenio= new AFEConvenio(alumno,empresa,tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        currentSession().save(afeConvenio);
        assertNotNull(afeConvenio.getId());
        
        AFEConvenio afeConvenio1= instance.obtiene(afeConvenio.getId());
        assertEquals(afeConvenio.getMatricula(), afeConvenio1.getMatricula());
        assertEquals(afeConvenio.getId(), afeConvenio1.getId());
    }
    
    @Test
    public void testEliminaAFEConvenios() {
     log.debug("Deberia Eliminar un Convenio");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        TiposBecas tipoBeca= new TiposBecas("Descripcion", Boolean.TRUE,new BigDecimal(10),new BigDecimal(12),Boolean.FALSE,Boolean.TRUE, 10, empresa);
        currentSession().save(tipoBeca);
        assertNotNull(tipoBeca.getId());
        Alumno alumno = alDao.obtiene("1080506");
        assertNotNull(alumno);
        AFEConvenio afeConvenio= new AFEConvenio(alumno,empresa,tipoBeca, new BigDecimal(10),10, Boolean.TRUE,"1080506");
        currentSession().save(afeConvenio);
        instance.elimina(afeConvenio.getId());        
        assertNotNull(afeConvenio.getId());
        assertEquals(afeConvenio.getMatricula(), "1080506");
        
        
    }
}
