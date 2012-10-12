/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.model.Concepto;
import org.hibernate.Session;
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
 * @author AMDA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ConceptoDaoTest {
    
    @Autowired
    private ConceptoDao conceptoDao;
    private static final Logger log = LoggerFactory.getLogger(ConceptoDaoTest.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public ConceptoDaoTest() {
    }

    @Test
    public void testObtenerListaDeConceptos() {
        Map<String, Object> params = new TreeMap();
        Concepto c = null;
         Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        for (int i = 0; i < 5; i++) {
            c = new Concepto();
            c.setDescripcion("test" + i);
            c.setNombre("a"+i);
            c.setStatus("A");
            c.setTags("tag");
            c.setEmpresa(empresa);
            currentSession().save(c);
            //assertNotNull(c.getId());
        }
        params = conceptoDao.lista(params);
        //assertNotNull((List)params.get(Constantes.CONCEPTO_LIST));
        //assertEquals(5,((List)params.get(Constantes.CONCEPTO_LIST)).size());

    }

    @Test
    public void testObtieneConcepto() throws ObjectRetrievalFailureException {

        Concepto concepto = new Concepto();
        concepto.setDescripcion("test");
        concepto.setNombre("a");
        concepto.setStatus("A");
        concepto.setTags("tag");
        currentSession().save(concepto);
        
        Concepto concepto1 = conceptoDao.obtiene(concepto.getId());
        //assertEquals(concepto.getId(), concepto1.getId());

    }

    @Test
    public void testGrabaConcepto() {
        log.debug("entrando a graba concepto");
        Concepto concepto = new Concepto();
        concepto.setDescripcion("test");
        concepto.setNombre("a");
        concepto.setStatus("A");
        concepto.setTags("tag");
        conceptoDao.graba(concepto, null);
        //assertNotNull(concepto.getId());
        if(concepto.getId()==null){
            log.error("no graba");
        }
        log.debug("concepto{}",concepto.getId());
        try {
            log.debug("concepto{}",conceptoDao.obtiene(concepto.getId()));
        } catch (ObjectRetrievalFailureException ex) {
            java.util.logging.Logger.getLogger(ConceptoDaoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testEliminaConcepto() {
        Concepto concepto = new Concepto();
        concepto.setDescripcion("test");
        concepto.setNombre("a");
        concepto.setStatus("A");
        concepto.setTags("tag");
        currentSession().save(concepto);
        try {
            String nombre = conceptoDao.elimina(concepto.getId());
        } catch (ObjectRetrievalFailureException ex) {
            log.error("No existe concepto");
        }
        try {
            concepto = conceptoDao.obtiene(concepto.getId());
            //fail("Fallo al eliminar");
        } catch (ObjectRetrievalFailureException ex) {
            log.info("Elimino con exito");
        }
    }
}
